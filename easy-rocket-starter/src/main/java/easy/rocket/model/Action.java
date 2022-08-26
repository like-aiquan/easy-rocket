package easy.rocket.model;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;

/**
 * @author chenaiquan
 * @date 2022/6/13 21:05
 */
public enum Action {
  Commit {
    @Override
    public ConsumeConcurrentlyStatus action() {
      return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    @Override
    public ConsumeOrderlyStatus orderlyStatus() {
      return ConsumeOrderlyStatus.SUCCESS;
    }
  },
  Reconsume {
    @Override
    public ConsumeConcurrentlyStatus action() {
      return ConsumeConcurrentlyStatus.RECONSUME_LATER;
    }

    @Override
    public ConsumeOrderlyStatus orderlyStatus() {
      return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
    }
  };

  public abstract ConsumeConcurrentlyStatus action();

  public abstract ConsumeOrderlyStatus orderlyStatus();
}
