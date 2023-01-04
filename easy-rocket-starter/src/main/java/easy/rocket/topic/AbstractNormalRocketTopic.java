package easy.rocket.topic;

/**
 * @author chenaiquan
 * @date 2022/6/12 14:05
 */
public abstract class AbstractNormalRocketTopic implements RocketTopic {
  @Override
  public String key() {
    // normal topic do not need partition key
    return "normal";
  }
}
