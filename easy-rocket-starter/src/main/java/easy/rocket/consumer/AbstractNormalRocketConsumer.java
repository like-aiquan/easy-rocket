package easy.rocket.consumer;

import com.google.common.base.Stopwatch;
import easy.rocket.config.RocketMqProperties;
import easy.rocket.model.Action;
import easy.rocket.model.ConsumeContext;
import easy.rocket.model.Message;
import easy.rocket.model.SubscribeRelation;
import easy.rocket.topic.AbstractNormalRocketTopic;
import easy.rocket.util.ContinuousStopwatch;
import easy.rocket.util.JsonUtil;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.annotation.PreDestroy;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author chenaiquan
 * @date 2022/6/13 21:12
 */
public abstract class AbstractNormalRocketConsumer<T extends AbstractNormalRocketTopic>
  extends AbstractRocketConsumer<T>
  implements MessageListenerConcurrently {

  private final DefaultMQPushConsumer consumer;
  private final SubscribeRelation subscribeRelation;
  private final Class<T> bindClazz;

  public AbstractNormalRocketConsumer(RocketMqProperties rocketMqProperties, SubscribeRelation subscribeRelation, Class<T> bindClazz) {
    this(rocketMqProperties, subscribeRelation, bindClazz, new DefaultMQPushConsumer());
  }

  public AbstractNormalRocketConsumer(RocketMqProperties rocketMqProperties, SubscribeRelation subscribeRelation,
    Class<T> bindClazz, DefaultMQPushConsumer consumer) {
    super(rocketMqProperties);
    this.bindClazz = bindClazz;
    this.subscribeRelation = subscribeRelation;
    this.consumer = consumer;

    try {
      this.start();
      logger.info("start consumer success! {}, {}", this.getClass().getSimpleName(), consumer.getConsumerGroup());
    } catch (MQClientException e) {
      logger.error("start consumer error! {} {}", this.getClass().getSimpleName(), e.getErrorMessage(), e);
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

  @PreDestroy
  public void shutdown() {
    this.consumer.shutdown();
    logger.info("consumer shut down hook [{}]", this.getClass().getSimpleName());
  }

  @Override
  public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messages, ConsumeConcurrentlyContext concurrentlyContext) {
    return this.consumeMessage(messages);
  }

  private ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messages) {
    ContinuousStopwatch continuousStopwatch = new ContinuousStopwatch(Stopwatch.createUnstarted());
    MessageExt messageExt = messages.stream().findFirst().orElse(null);
    if (messageExt == null) {
      return Action.Commit.action();
    }

    return this.trance(() -> this.consumeMessage(continuousStopwatch, messageExt), messageExt.getMsgId());
  }

  private ConsumeConcurrentlyStatus consumeMessage(ContinuousStopwatch continuousStopwatch, MessageExt messageExt) {
    Message message = this.convertMessage(messageExt);
    String body = new String(message.getBody(), StandardCharsets.UTF_8);
    String consumerName = this.getClass().getSimpleName();

    T topic = JsonUtil.read(this.bindClazz, body);
    ConsumeContext context = new ConsumeContext();
    try {
      if (!this.accept(message, topic, context)) {
        logger.info("{} ignore ons message: {}", consumerName, body);
        return Action.Commit.action();
      }
    } catch (Throwable e) {
      continuousStopwatch.resetAndLog("consume time", logger);
      logger.error("{} accept ons message exception: {}", consumerName, e, e);
      return Action.Reconsume.action();
    }

    logger.info("{} begin ons message: {}", consumerName, body);
    try {
      Action result = this.consume(message, topic, context);
      logger.info("{} {} ons message", consumerName, result);
      continuousStopwatch.resetAndLog("commit message", logger);
      if (Action.Reconsume.equals(result) && message.getReconsumeTimes() >= thresholdOfErrorNotify(topic, message)) {
        logger.error("{} ons message {} {} reconsume times {}", consumerName, message.getTopic(), message.getMsgId(), message.getReconsumeTimes());
      }
      return result.action();
    } catch (Throwable e) {
      continuousStopwatch.resetAndLog("consume message", logger);
      logger.error("{} ons message exception: {}", consumerName, e.getMessage(), e);
      return Action.Reconsume.action();
    }
  }

}
