package easy.rocket.consumer;

import easy.rocket.model.Action;
import easy.rocket.model.ConsumeContext;
import easy.rocket.model.Message;
import easy.rocket.topic.RocketTopic;
import org.apache.rocketmq.client.exception.MQClientException;

/**
 * @author chenaiquan
 * @date 2022/6/13 20:56
 */
public interface RocketConsumer<T extends RocketTopic> {

  boolean accept(Message message, T topic, ConsumeContext context);

  Action consume(Message message, T topic, ConsumeContext context);

  void start() throws MQClientException;
}
