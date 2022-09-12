package easy.rocket.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Stopwatch;
import easy.rocket.config.RocketMqProperties;
import easy.rocket.model.Action;
import easy.rocket.model.ConsumeContext;
import easy.rocket.model.Message;
import easy.rocket.model.SubscribeRelation;
import easy.rocket.topic.RocketTopic;
import easy.rocket.util.ContinuousStopwatch;
import easy.rocket.util.JsonUtil;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author chenaiquan
 * @date 2022/6/14 00:46
 */
public abstract class AbstractOrderRocketConsumer<T extends RocketTopic>
  extends AbstractRocketConsumer<T>
  implements MessageListenerOrderly {
  private final Class<T> bindClazz;
  private final SubscribeRelation subscribeRelation;
  private final DefaultMQPushConsumer consumer;

  public AbstractOrderRocketConsumer(RocketMqProperties rocketMqProperties, SubscribeRelation subscribeRelation, Class<T> bindClazz) {
    this(rocketMqProperties, subscribeRelation, bindClazz, new DefaultMQPushConsumer());
  }

  public AbstractOrderRocketConsumer(RocketMqProperties rocketMqProperties, SubscribeRelation subscribeRelation, Class<T> bindClazz,
    DefaultMQPushConsumer consumer) {
    super(rocketMqProperties);
    this.bindClazz = bindClazz;
    this.subscribeRelation = subscribeRelation;
    this.consumer = consumer;

    try {
      this.start();
      logger.info("start order consumer success! {} {}", this.getClass().getSimpleName(), consumer.getConsumerGroup());
    } catch (MQClientException e) {
      logger.error("start order consumer error! {} {}", this.getClass().getSimpleName(), e.getErrorMessage(), e);
    }
  }

  public void start() throws MQClientException {
    this.consumer.setNamespace(this.rocketMqProperties.getNameSpace());
    String nameSrvAddr = this.rocketMqProperties.getNameSrvAddr();
    if (nameSrvAddr != null && !"".equals(nameSrvAddr)) {
      this.consumer.setNamesrvAddr(nameSrvAddr);
    }
    String topic = this.resolveTopicGroupName(this.subscribeRelation.topic());
    String group = this.resolveTopicGroupName(this.subscribeRelation.group());
    this.consumer.setConsumerGroup(group);
    this.consumer.subscribe(topic, this.subscribeRelation.tag());
    this.consumer.registerMessageListener(this);
    this.consumer.start();
  }


  @Override
  public ConsumeOrderlyStatus consumeMessage(List<MessageExt> messages, ConsumeOrderlyContext consumeOrderlyContext) {
    consumeOrderlyContext.setAutoCommit(true);
    ContinuousStopwatch continuousStopwatch = new ContinuousStopwatch(Stopwatch.createUnstarted());
    MessageExt messageExt = messages.stream().findFirst().orElse(null);
    if (messageExt == null) {
      return Action.Commit.orderlyStatus();
    }
    return this.trance(() -> this.consumeMessage(continuousStopwatch, messageExt), messageExt.getMsgId());
  }

  private ConsumeOrderlyStatus consumeMessage(ContinuousStopwatch continuousStopwatch, MessageExt messageExt) {
    Message message = this.convertMessage(messageExt);
    String body = new String(message.getBody(), StandardCharsets.UTF_8);
    String consumerName = this.getClass().getSimpleName();

    T topic;
    try {
      topic = JsonUtil.reader().forType(this.bindClazz).readValue(body);
    } catch (JsonProcessingException e) {
      logger.error("{} ons message: {} deserialize error: {}", consumerName, body, e.toString());
      return Action.Reconsume.orderlyStatus();
    }
    ConsumeContext context = new ConsumeContext();
    try {
      if (!this.accept(message, topic, context)) {
        logger.info("{} ignore ons message: {}", consumerName, body);
        return Action.Commit.orderlyStatus();
      }
    } catch (Throwable e) {
      continuousStopwatch.resetAndLog("consume time");
      logger.error("{} accept ons message exception: {}", consumerName, e, e);
      return Action.Reconsume.orderlyStatus();
    }

    logger.info("{} begin ons message: {}", consumerName, body);
    try {
      Action result = this.consume(message, topic, context);
      logger.info("{} {} ons message", consumerName, result);
      continuousStopwatch.resetAndLog("commit message");
      if (Action.Reconsume.equals(result) && message.getReconsumeTimes() >= thresholdOfErrorNotify(topic, message)) {
        logger.error("{} ons message {} {} reconsume times {}", consumerName, message.getTopic(), message.getMsgId(), message.getReconsumeTimes());
      }
      return result.orderlyStatus();
    } catch (Throwable e) {
      continuousStopwatch.resetAndLog("consume message");
      logger.error("{} ons message exception: {}", consumerName, e.toString(), e);
      return Action.Reconsume.orderlyStatus();
    }
  }

}
