package easy.rocket.service;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:application.properties")
class SendMqTestServiceTest {
  @Autowired
  private SendMqTestService sendMqTestService;

  @BeforeAll
  static void env() {
    System.setProperty("user.name_srv", "192.168.10.150:9876");
  }

  @Test
  void contextLoads() {
    this.sendMqTestService.send(1000, 2);
  }

}
