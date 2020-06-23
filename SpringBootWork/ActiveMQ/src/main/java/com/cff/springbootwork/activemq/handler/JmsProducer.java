package com.cff.springbootwork.activemq.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.cff.springbootwork.activemq.model.DefaultMqModel;

@Component
public class JmsProducer {
	@Autowired
	JmsTemplate jmsTemplate;

	@Value("${jms.destQueueName}")
	String destQueueName;

	public void send(DefaultMqModel defaultMqModel) {
		jmsTemplate.convertAndSend(destQueueName, defaultMqModel);
	}
}
