package com.toby.conf;


import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Topic;

@Configuration
public class MqConfig {

    @Bean
    public Topic topic() {
        return new ActiveMQTopic("fsjkxt.topic");
    }
}
