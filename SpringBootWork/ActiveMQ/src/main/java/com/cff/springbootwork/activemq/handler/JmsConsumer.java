package com.cff.springbootwork.activemq.handler;

import com.cff.springbootwork.activemq.model.QueuesModelFace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.cff.springbootwork.activemq.model.DefaultMqModel;
import com.cff.springbootwork.activemq.service.BusinessSerivce;

@Component
public class JmsConsumer {

	@Autowired
	BusinessSerivce businessSerivce;

	@JmsListener(destination = "${jms.destQueueName}")
	public void processMessage(QueuesModelFace queuesModelFace) throws Exception {
		businessSerivce.doBusiness(queuesModelFace);
	}
}
