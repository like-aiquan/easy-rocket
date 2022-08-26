package easy.rocket.topic;

import java.util.Arrays;
import java.util.List;

/**
 * @author chenaiquan
 * @date 2022/6/12 14:04
 */
public interface RocketTopic {
  String topicName();

  String key();

  default int delayTimeLevel() {
    return 0;
  }

  default List<String> tags() {
    return Arrays.asList("*");
  }

  default boolean orderTopic() {
    return false;
  }

  default boolean logBody() {
    return true;
  }
}
