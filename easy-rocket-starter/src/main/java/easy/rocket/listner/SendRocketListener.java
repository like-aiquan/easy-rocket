package easy.rocket.listner;

import easy.rocket.producer.NormalRocketProducer;
import easy.rocket.producer.OrderRocketProducer;
import easy.rocket.topic.AbstractNormalRocketTopic;
import easy.rocket.topic.AbstractOrderRocketTopic;
import org.springframework.context.event.EventListener;

/**
 * mq 推送
 *
 * @author chenaiquan
 * @date 2022/6/14 18:27
 * @see SendRockTransactionListener
 */

public class SendRocketListener extends AbstractRocketMqEventListener {

  public SendRocketListener(NormalRocketProducer normalRocketProducer, OrderRocketProducer orderRocketProducer) {
    super(normalRocketProducer, orderRocketProducer);
  }

  @EventListener
  public void onApplicationEvent(AbstractNormalRocketTopic topic) {
    this.sendNormal(topic);
  }

  @EventListener
  public void onApplicationEvent(AbstractOrderRocketTopic topic) {
    this.sendOrder(topic);
  }
}
