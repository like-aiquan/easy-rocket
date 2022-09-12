package easy.rocket.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import easy.rocket.fallback.FallBackService;
import easy.rocket.fallback.impl.DefaultFallBackServiceImpl;
import easy.rocket.listner.SendRockTransactionListener;
import easy.rocket.listner.SendRocketListener;
import easy.rocket.producer.NormalRocketProducer;
import easy.rocket.producer.OrderRocketProducer;
import easy.rocket.util.JsonUtil;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author chenaiquan
 * @date 2022/6/14 16:44
 */
public class EasyRocketAutoConfiguration {

  public EasyRocketAutoConfiguration(ObjectMapper mapper) {
    JsonUtil.init(mapper);
  }

  @Bean
  @ConditionalOnClass(DefaultMQProducer.class)
  @ConditionalOnMissingBean(name = "normalMqProducer")
  public DefaultMQProducer normalMqProducer() {
    return new DefaultMQProducer();
  }

  @Bean
  @ConditionalOnClass(DefaultMQProducer.class)
  @ConditionalOnMissingBean(name = "orderMqProducer")
  public DefaultMQProducer orderMqProducer() {
    return new DefaultMQProducer();
  }

  @Bean(destroyMethod = "destroy")
  @ConditionalOnProperty(value = "rocketmq.producer.normal")
  @ConditionalOnBean(RocketMqProperties.class)
  public NormalRocketProducer normalProducer(RocketMqProperties properties, ObjectProvider<FallBackService> fallBackServices,
    @Qualifier("normalMqProducer") DefaultMQProducer normalMqProducer) {
    return new NormalRocketProducer(properties, fallBackServices.getIfUnique(), normalMqProducer);
  }

  @Bean(destroyMethod = "destroy")
  @ConditionalOnProperty(value = "rocketmq.producer.order")
  @ConditionalOnBean(RocketMqProperties.class)
  public OrderRocketProducer orderProducer(RocketMqProperties properties, ObjectProvider<FallBackService> fallBackServices,
    @Qualifier("orderMqProducer") DefaultMQProducer orderMqProducer) {
    return new OrderRocketProducer(properties, fallBackServices.getIfUnique(), orderMqProducer);
  }

  @Bean(destroyMethod = "destroy")
  @ConditionalOnProperty({"rocketmq.transactional.listener"})
  @ConditionalOnClass(TransactionalEventListener.class)
  public SendRockTransactionListener transactionalListener(ObjectProvider<NormalRocketProducer> normalMqProducer,
    ObjectProvider<OrderRocketProducer> orderMqProducer) {
    return new SendRockTransactionListener(normalMqProducer.getIfUnique(), orderMqProducer.getIfUnique());
  }

  @Bean(destroyMethod = "destroy")
  @ConditionalOnProperty("rocketmq.transactional.listener")
  @ConditionalOnMissingBean(SendRockTransactionListener.class)
  public SendRocketListener missTransactionalListener(ObjectProvider<NormalRocketProducer> normalProducer,
    ObjectProvider<OrderRocketProducer> orderProducer) {
    return new SendRocketListener(normalProducer.getIfUnique(), orderProducer.getIfUnique());
  }

  @Bean(destroyMethod = "destroy")
  @ConditionalOnProperty("rocketmq.listener")
  @ConditionalOnMissingBean(SendRockTransactionListener.class)
  public SendRocketListener listener(ObjectProvider<NormalRocketProducer> normalProducer, ObjectProvider<OrderRocketProducer> orderProducer) {
    return new SendRocketListener(normalProducer.getIfUnique(), orderProducer.getIfUnique());
  }

  @Bean
  @ConditionalOnProperty("rocketmq.log.fall.back")
  @ConditionalOnMissingBean(FallBackService.class)
  public FallBackService fallBackService() {
    return new DefaultFallBackServiceImpl();
  }
}
