package com.cff.springbootwork.activemq.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cff.springbootwork.activemq.handler.JmsProducer;
import com.cff.springbootwork.activemq.model.DefaultMqModel;
import com.cff.springbootwork.activemq.model.SeccondMqModel;

@RestController
@RequestMapping("/activemq")
public class ActiveMQRest {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	JmsProducer jmsProducer;

	@RequestMapping(value = "/test", method = { RequestMethod.GET })
	public DefaultMqModel test() {
		DefaultMqModel defaultMqModel = new DefaultMqModel();
		defaultMqModel.setContent("hahahahahahhahaha哈啊哈哈");
		defaultMqModel.setTitle("测试");
		defaultMqModel.setType(1);
		jmsProducer.send(defaultMqModel);
		return defaultMqModel;
	}

	@RequestMapping(value = "/test2", method = { RequestMethod.GET })
	public SeccondMqModel test2() {
		SeccondMqModel seccondMqModel = new SeccondMqModel();
		seccondMqModel.setContent("asdasdasd哈啊哈哈");
		seccondMqModel.setTitle("测试2");
		seccondMqModel.setRemark("第二个");
		seccondMqModel.setType(2);
		jmsProducer.send(seccondMqModel);
		return seccondMqModel;
	}

}
