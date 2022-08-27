package easy.rocket.topic;

/**
 * TopicTest
 *
 * @author chenaiquan
 * @date 2022/6/12 14:03
 */
public class NoTopicTest extends AbstractNormalRocketTopic {
  public static final String TOPIC_NAME = "no-topic-test";

  private Long id;
  private String massage;

  @Override
  public String topicName() {
    return TOPIC_NAME;
  }

  @Override
  public String key() {
    return id.toString();
  }

  public Long getId() {
    return id;
  }

  public NoTopicTest setId(Long id) {
    this.id = id;
    return this;
  }

  public String getMassage() {
    return massage;
  }

  public NoTopicTest setMassage(String massage) {
    this.massage = massage;
    return this;
  }

  @Override
  public String toString() {
    return "NoTopicTest{" +
        "id=" + id +
        ", massage='" + massage + '\'' +
        '}';
  }
}
