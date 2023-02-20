package easy.rocket.consumer;

import easy.rocket.config.RocketMqProperties;
import easy.rocket.model.Action;
import easy.rocket.model.Message;
import easy.rocket.model.SubscribeRelation;
import easy.rocket.topic.PoTopicTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author chenaiquan
 * @date 2022/6/14 16:17
 */
@Component
public class PoTopicTestConsumer extends AbstractOrderRocketConsumer<PoTopicTest> {
  private static final Logger logger = LoggerFactory.getLogger(PoTopicTestConsumer.class);

  private static final String TOPIC = PoTopicTest.TOPIC_NAME;
  private static final String GROUP = "po-topic-test-consumer";

  public PoTopicTestConsumer(RocketMqProperties rocketMqProperties) {
    super(rocketMqProperties, SubscribeRelation.Builder.newBuilder().topic(TOPIC).group(GROUP).build(), PoTopicTest.class);
  }

  @Override
  public boolean accept(Message message, PoTopicTest topic) {
    return true;
  }

  @Override
  public Action consume(Message message, PoTopicTest topic) {
    return Action.Commit;
  }
}
