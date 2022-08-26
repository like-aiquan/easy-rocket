package easy.rocket.listner;

import easy.rocket.producer.NormalRocketProducer;
import easy.rocket.producer.OrderRocketProducer;
import easy.rocket.topic.AbstractNormalRocketTopic;
import easy.rocket.topic.AbstractOrderRocketTopic;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * mq 推送
 *
 * @author chenaiquan
 * @date 2022/6/14 18:27
 * @see SendRocketListener
 */

public class SendRockTransactionListener extends AbstractRocketMqEventListener {

  public SendRockTransactionListener(NormalRocketProducer normalRocketProducer, OrderRocketProducer orderRocketProducer) {
    super(normalRocketProducer, orderRocketProducer);
  }

  @TransactionalEventListener
  public void onApplicationEvent(AbstractNormalRocketTopic topic) {
    this.sendNormal(topic);
  }

  @TransactionalEventListener
  public void onApplicationEvent(AbstractOrderRocketTopic topic) {
    this.sendOrder(topic);
  }
}
