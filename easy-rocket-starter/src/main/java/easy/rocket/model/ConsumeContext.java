package easy.rocket.model;

/**
 * @author chenaiquan
 * @date 2022/6/13 21:12
 */
public class ConsumeContext {
  private int delayLevelWhenNextConsume = 0;
  private int ackIndex = Integer.MAX_VALUE;

  public int getDelayLevelWhenNextConsume() {
    return delayLevelWhenNextConsume;
  }

  public ConsumeContext setDelayLevelWhenNextConsume(int delayLevelWhenNextConsume) {
    this.delayLevelWhenNextConsume = delayLevelWhenNextConsume;
    return this;
  }

  public int getAckIndex() {
    return ackIndex;
  }

  public ConsumeContext setAckIndex(int ackIndex) {
    this.ackIndex = ackIndex;
    return this;
  }
}
