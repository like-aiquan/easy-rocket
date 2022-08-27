package easy.rocket.topic;

/**
 * @author chenaiquan
 * @date 2022/6/12 15:29
 */
public class PoTopicTest extends AbstractOrderRocketTopic {
  public static final String TOPIC_NAME = "po-topic-test";

  private Long id;
  private String message;

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

  public PoTopicTest setId(Long id) {
    this.id = id;
    return this;
  }

  public String getMessage() {
    return message;
  }

  public PoTopicTest setMessage(String message) {
    this.message = message;
    return this;
  }

  @Override
  public String toString() {
    return "PoTopicTest{" +
        "id=" + id +
        ", message='" + message + '\'' +
        '}';
  }
}
