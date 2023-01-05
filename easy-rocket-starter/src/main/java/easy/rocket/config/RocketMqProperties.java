package easy.rocket.config;

import easy.rocket.util.SpringEnvironmentContextUtil;
import java.util.Properties;
import org.apache.rocketmq.common.utils.NameServerAddressUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

/**
 * @author chenaiquan
 * @date 2022/6/12 14:18
 */
@ConfigurationProperties(value = "rocketmq")
public class RocketMqProperties {
  private static final Logger logger = LoggerFactory.getLogger(RocketMqProperties.class);
  /**
   * 用来区分环境，区分 rocket mq 的 topic 和 group 的命名 规范的命名才是写好代码的第一步
   */
  private String env;
  /**
   * rocket mq name srv address 当然 也可以使用系统的环境变量设置
   */
  private String nameSrvAddr;
  /**
   * 系统全局的 name space 可以没有 name space 是 topic  group 的前缀，所有 topic group 都在当前名称空间下才能互相正常消费， 建议不用  感觉很鸡肋
   */
  private String nameSpace;
  /**
   * 默认 producer 组
   */
  private String producerGroup;

  public Properties generateProperties() {
    Properties properties = new Properties();
    return properties;
  }

  public String getEnv() {
    return env;
  }

  public RocketMqProperties setEnv(String env) {
    this.env = env;
    return this;
  }

  public String getNameSrvAddr() {
    return nameSrvAddr;
  }

  public RocketMqProperties setNameSrvAddr(String nameSrvAddr) {
    this.nameSrvAddr = nameSrvAddr;
    return this;
  }

  public String getNameSpace() {
    return nameSpace;
  }

  public RocketMqProperties setNameSpace(String nameSpace) {
    this.nameSpace = nameSpace;
    return this;
  }

  public String getProducerGroup() {
    return producerGroup;
  }

  public RocketMqProperties setProducerGroup(String producerGroup) {
    this.producerGroup = producerGroup;
    return this;
  }

  public RocketMqProperties checkProperties() {
    String env = this.getEnv();
    String group = this.getProducerGroup();

    if (!StringUtils.hasText(env)) {
      env = SpringEnvironmentContextUtil.firstActiveProfile();
      if (!StringUtils.hasText(env)) {
        throw new NullPointerException("check set env!");
      }
      this.setEnv(env);
    }

    if (!StringUtils.hasText(group)) {
      this.setProducerGroup(this.getEnv());
      logger.warn("empty producer group, will use like env");
    }

    // fixme 是否可以？
    // fixme 不用校验 name srv  如果是 consumer 模块启动时会直接开始拉取的动作，会直接报错
    // fixme producer 模块会推送失败 可以使用或实现 fallbackService 记录错误
    String nameSrvAddr = this.getNameSrvAddr();
    if (!StringUtils.hasText(nameSrvAddr)) {
      nameSrvAddr = NameServerAddressUtils.getNameServerAddresses();
      if (!StringUtils.hasText(nameSrvAddr)) {
        throw new NullPointerException("check set name srv addr");
      }
      this.setNameSrvAddr(nameSrvAddr);
    }
    return this;
  }
}
