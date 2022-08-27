package easy.rocket;

import java.util.concurrent.TimeUnit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoConsumerApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoConsumerApplication.class, args);

    stay();
  }

  /**
   * keep current thread
   */
  static void stay() {
    // 单独的消费模块此处可以直接保持当前线程即可 无需 web 依赖
    while (true) {
      try {
        TimeUnit.HOURS.sleep(1);
      } catch (InterruptedException e) {
        break;
      }
    }
  }
}
