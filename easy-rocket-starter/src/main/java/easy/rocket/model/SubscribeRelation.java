package easy.rocket.model;

/**
 * @author chenaiquan
 * @date 2022/6/13 20:58
 */
public class SubscribeRelation {
  private final String group;
  private final String topic;
  private final String tag;

  private SubscribeRelation(String group, String topic, String tag) {
    this.group = group;
    this.topic = topic;
    this.tag = tag;
  }

  public String group() {
    return group;
  }

  public String topic() {
    return topic;
  }

  public String tag() {
    return tag;
  }


  public static final class Builder {
    private String group;
    private String topic;
    private String tag;

    private Builder() {
    }

    public static Builder newBuilder() {
      return new Builder();
    }

    public Builder group(String group) {
      this.group = group;
      return this;
    }

    public Builder topic(String topic) {
      this.topic = topic;
      return this;
    }

    public Builder tag(String tag) {
      this.tag = tag;
      return this;
    }

    public SubscribeRelation build() {
      return new SubscribeRelation(group, topic, tag);
    }
  }
}
