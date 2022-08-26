package easy.rocket.service.impl;


import easy.rocket.service.SendMqTestService;
import easy.rocket.topic.NoTopicTest;
import easy.rocket.topic.PoTopicTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * @author chenaiquan
 * @date 2022/6/14 18:26
 */
@Service
public class SendMqTestServiceImpl implements SendMqTestService {
  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

  @Override
  public void send(int type, int ct) {
    if (ct == 1) {
      if (1 == type) {
        this.signOrder(1);
      } else {
        this.multipartOrder(type);
      }
      return;
    }
    if (1 == type) {
      this.sign();
    } else {
      this.multipart(type);
    }
  }

  private void multipartOrder(int type) {
    for (int i = 0; i < type; i++) {
      this.signOrder(i);
    }
  }

  private void signOrder(int i) {
    PoTopicTest topic = new PoTopicTest();
    topic.setId(10L);
    topic.setMessage("hello order topic " + i);
    this.applicationEventPublisher.publishEvent(topic);
  }

  private void multipart(int n) {
    for (int i = 0; i < n; i++) {
      this.sign();
    }
  }

  private void sign() {
    NoTopicTest topic = new NoTopicTest();
    topic.setId(1L);
    topic.setMassage("hello i am fine.");
    this.applicationEventPublisher.publishEvent(topic);
  }
}
