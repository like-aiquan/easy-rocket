package easy.rocket.config;

import java.util.Properties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author chenaiquan
 * @date 2022/6/12 14:18
 */
@Component
@ConfigurationProperties(value = "rocketmq")
public class RocketMqProperties {
  /**
   * 用来区分环境，区分 rocket mq 的 topic 和 group 的命名 规范的命名才是写好代码的第一步
   */
  private String env;
  /**
   * rocket mq name srv address 当然 也可以使用系统的环境变量设置
   */
  private String nameSrvAddr;
  /**
   * 系统全局的 name space 可以没有 name space 是 topic  group 的前缀，所有 topic group 都在当前名称空间下才能互相正常消费，
   * 建议不用  感觉很鸡肋
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
}
