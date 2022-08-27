package easy.rocket.listner;

import easy.rocket.producer.NormalRocketProducer;
import easy.rocket.producer.OrderRocketProducer;
import easy.rocket.topic.AbstractNormalRocketTopic;
import easy.rocket.topic.AbstractOrderRocketTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenaiquan
 */
public abstract class AbstractRocketMqEventListener {
  private static final Logger logger = LoggerFactory.getLogger(SendRocketListener.class);

  protected final NormalRocketProducer normalRocketProducer;
  protected final OrderRocketProducer orderRocketProducer;

  public AbstractRocketMqEventListener(NormalRocketProducer normal, OrderRocketProducer order) {
    this.normalRocketProducer = normal;
    this.orderRocketProducer = order;

    logStart();
  }

  private void logStart() {
    logger.info("start rocket mq listener success, [{}]", this.getClass().getSimpleName());
  }

  public void destroy() {
    if (normalRocketProducer != null) {
      this.normalRocketProducer.destroy();
      logDestroy(NormalRocketProducer.NORMAL);
    }

    if (orderRocketProducer != null) {
      this.orderRocketProducer.destroy();
      logDestroy(OrderRocketProducer.ORDER);
    }
  }

  private void logDestroy(String name) {
    logger.info("shut down {} producer success.", name);
  }

  protected final void sendNormal(AbstractNormalRocketTopic topic) {
    if (normalRocketProducer == null) {
      logger.info("please open normal producer! check config like 'rocketmq.producer.normal=true'");
      return;
    }

    try {
      this.normalRocketProducer.send(topic);
    } catch (Exception e) {
      logger.error("send message error topic [{}] body [{}] {}", topic.topicName(), topic, e.getMessage(), e);
    }
  }

  protected final void sendOrder(AbstractOrderRocketTopic topic) {
    if (orderRocketProducer == null) {
      logger.info("please open order producer! check config like 'rocketmq.producer.order=true'");
      return;
    }

    try {
      this.orderRocketProducer.send(topic);
    } catch (Exception e) {
      logger.error("send message error topic [{}] body [{}] {}", topic.topicName(), topic, e.getMessage(), e);
    }
  }
}
