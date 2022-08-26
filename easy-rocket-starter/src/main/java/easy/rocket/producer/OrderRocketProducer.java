package easy.rocket.producer;

import easy.rocket.config.RocketMqProperties;
import easy.rocket.fallback.FallBackService;
import easy.rocket.topic.RocketTopic;
import java.util.Objects;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * @author chenaiquan
 * @date 2022/6/14 13:53
 */
public class OrderRocketProducer extends AbstractRocketProducer {

  private final DefaultMQProducer producer;

  public OrderRocketProducer(RocketMqProperties producerProperties, FallBackService fallBackService, DefaultMQProducer producer) {
    super(producerProperties, fallBackService);

    this.producer = producer;
    this.start();
  }

  private void start() {
    try {
      this.producer.setProducerGroup(this.producerProperties.getProducerGroup());
      this.producer.setNamespace(this.producerProperties.getNameSpace());
      String nameSrvAddr = this.producerProperties.getNameSrvAddr();
      if (nameSrvAddr != null && !"".equals(nameSrvAddr)) {
        this.producer.setNamesrvAddr(nameSrvAddr);
      }
      this.producer.start();
      logger.info("start producer success {}", this.getClass().getSimpleName());
    } catch (MQClientException e) {
      logger.error("start producer error {} {}", this.getClass().getSimpleName(), e.getErrorMessage(), e);
    }
  }

  public void destroy() {
    this.producer.shutdown();
  }

  @Override
  protected SendResult send(Message message, RocketTopic topic) {
    if (!topic.orderTopic()) {
      logger.error("dont support normal topic topic [{}] body [{}]", topic.topicName(), topic);
      return null;
    }
    if (topic.key() == null || "".equals(topic.key())) {
      logger.error("order topic please set shadingKey [{}] shading key [{}] body [{}]", topic.topicName(), topic.key(), topic);
      return null;
    }
    try {
      return this.producer.send(message, (mqs, msg, arg) -> mqs.get(Objects.hash(topic.key()) % mqs.size()), topic.key());
    } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
