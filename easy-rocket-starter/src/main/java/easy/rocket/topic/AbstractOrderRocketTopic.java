package easy.rocket.topic;

/**
 * @author chenaiquan
 * @date 2022/6/12 15:39
 */
public abstract class AbstractOrderRocketTopic implements RocketTopic {

  @Override
  public boolean orderTopic() {
    return true;
  }
}
