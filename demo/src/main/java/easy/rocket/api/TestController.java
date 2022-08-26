package easy.rocket.api;

import easy.rocket.api.vo.R;
import easy.rocket.service.SendMqTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenaiquan
 * @date 2022/6/14 19:48
 */
@RestController
@RequestMapping("/api/v1/rocket")
public class TestController {
  @Autowired
  private SendMqTestService sendMqTestService;

  @GetMapping("/send/{type}/{ct}")
  public R test(@PathVariable int type, @PathVariable int ct) {
    this.sendMqTestService.send(type, ct);
    return R.success();
  }

}
