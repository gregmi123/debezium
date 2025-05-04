package com.cdc.debezium.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.jms.Queue;

/**
 * @author Gaurav Regmi
 */
@Configuration
@Slf4j
public class JmsConfig {

    @Value("${spring.activemq.broker-url}")
    private String brokerURL;

    @Bean
    public Queue queue() {
        return new ActiveMQQueue("dbzium.bankxp.queue");
    }

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(brokerURL);
        factory.setTrustAllPackages(true);
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(activeMQConnectionFactory());
        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
        return jmsTemplate;
    }

    @Bean(name = "messageFactory")
    public DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
        defaultJmsListenerContainerFactory.setConnectionFactory(activeMQConnectionFactory());
        defaultJmsListenerContainerFactory.setMessageConverter(jacksonJmsMessageConverter());
        defaultJmsListenerContainerFactory.setConcurrency("10-200");
        defaultJmsListenerContainerFactory.setTaskExecutor(taskExecutor());

        return defaultJmsListenerContainerFactory;
    }

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.BYTES);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor tE = new ThreadPoolTaskExecutor();
        tE.setCorePoolSize(7);
        tE.setMaxPoolSize(100);
        tE.setThreadNamePrefix("jms-thread-");
        tE.setKeepAliveSeconds(50);
        tE.setQueueCapacity(-1);
        return tE;
    }

}
