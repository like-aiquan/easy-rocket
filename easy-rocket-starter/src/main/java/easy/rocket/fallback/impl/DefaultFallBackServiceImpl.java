package easy.rocket.fallback.impl;

import easy.rocket.fallback.FallBackService;
import easy.rocket.topic.RocketTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenaiquan
 * @date 2022/6/14 21:57
 */
public class DefaultFallBackServiceImpl implements FallBackService {
  private static final Logger log = LoggerFactory.getLogger(FallBackService.class);

  @Override
  public void fallBack(RocketTopic topic, Throwable e) {
    log.error("send mq exception topic [{}] body [{}] {}", topic.topicName(), topic, e.getMessage(), e);
  }
}
