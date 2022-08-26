package easy.rocket.consumer;


import easy.rocket.config.RocketMqProperties;
import easy.rocket.model.Action;
import easy.rocket.model.ConsumeContext;
import easy.rocket.model.Message;
import easy.rocket.model.SubscribeRelation.Builder;
import easy.rocket.topic.NoTopicTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author chenaiquan
 * @date 2022/6/13 22:59
 */
@Component
public class NoTopicTestConsumer extends AbstractNormalRocketConsumer<NoTopicTest> {
  private static final Logger logger = LoggerFactory.getLogger(NoTopicTestConsumer.class);

  private static final String TOPIC = NoTopicTest.TOPIC_NAME;
  private static final String GROUP = "no-topic-test-consume";

  @Autowired
  public NoTopicTestConsumer(RocketMqProperties properties) {
    super(properties, Builder.newBuilder().topic(TOPIC).group(GROUP).build(), NoTopicTest.class);
  }

  @Override
  public boolean accept(Message message, NoTopicTest topic, ConsumeContext context) {
    return true;
  }

  @Override
  public Action consume(Message message, NoTopicTest topic, ConsumeContext context) {
    return Action.Commit;
  }
}
