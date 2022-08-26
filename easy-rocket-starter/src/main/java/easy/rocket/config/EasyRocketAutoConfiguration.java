package easy.rocket.config;

import easy.rocket.fallback.FallBackService;
import easy.rocket.fallback.impl.DefaultFallBackServiceImpl;
import easy.rocket.listner.SendRockTransactionListener;
import easy.rocket.listner.SendRocketListener;
import easy.rocket.producer.NormalRocketProducer;
import easy.rocket.producer.OrderRocketProducer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.ObjectProvider;
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

  @Bean
  @ConditionalOnProperty(value = "rocketmq.producer.normal")
  @ConditionalOnBean(RocketMqProperties.class)
  public NormalRocketProducer orderMqProducer(RocketMqProperties properties, ObjectProvider<FallBackService> fallBackServices,
    @Qualifier("normalMqProducer") DefaultMQProducer orderMqProducers) {
    return new NormalRocketProducer(properties, fallBackServices.getIfUnique(), orderMqProducers);
  }

  @Bean
  @ConditionalOnProperty(value = "rocketmq.producer.order")
  @ConditionalOnBean(RocketMqProperties.class)
  public OrderRocketProducer normalMqProducer(RocketMqProperties properties, ObjectProvider<FallBackService> fallBackServices,
    @Qualifier("orderMqProducer") DefaultMQProducer normalMqProducer) {
    return new OrderRocketProducer(properties, fallBackServices.getIfUnique(), normalMqProducer);
  }

  @Bean
  @ConditionalOnProperty({"rocketmq.transactional.listener"})
  @ConditionalOnClass(TransactionalEventListener.class)
  public SendRockTransactionListener transactionalListener(ObjectProvider<NormalRocketProducer> normalMqProducer,
    ObjectProvider<OrderRocketProducer> orderMqProducer) {
    return new SendRockTransactionListener(normalMqProducer.getIfUnique(), orderMqProducer.getIfUnique());
  }

  @Bean
  @ConditionalOnProperty("rocketmq.transactional.listener")
  @ConditionalOnMissingBean(SendRockTransactionListener.class)
  public SendRocketListener missTransactionalListener(ObjectProvider<NormalRocketProducer> normalMqProducer,
    ObjectProvider<OrderRocketProducer> orderMqProducer) {
    return new SendRocketListener(normalMqProducer.getIfUnique(), orderMqProducer.getIfUnique());
  }

  @Bean
  @ConditionalOnProperty("rocketmq.listener")
  @ConditionalOnMissingBean(SendRockTransactionListener.class)
  public SendRocketListener listener(ObjectProvider<NormalRocketProducer> normalMqProducer, ObjectProvider<OrderRocketProducer> orderMqProducer) {
    return new SendRocketListener(normalMqProducer.getIfUnique(), orderMqProducer.getIfUnique());
  }

  @Bean
  @ConditionalOnProperty("rocketmq.log.fall.back")
  @ConditionalOnMissingBean(FallBackService.class)
  public FallBackService fallBackService() {
    return new DefaultFallBackServiceImpl();
  }
}
