package com.cff.springbootwork.activemq.service;

import com.cff.springbootwork.activemq.model.QueuesModelFace;
import com.cff.springbootwork.activemq.util.Sqlca;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cff.springbootwork.activemq.model.DefaultMqModel;
import com.cff.springbootwork.activemq.model.SeccondMqModel;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class BusinessSerivce {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DataSource dataSource;

	public void doBusiness(QueuesModelFace queuesModelFace) throws Exception {
		Connection connection = dataSource.getConnection();
		queuesModelFace.HandleDeQueuedModel(connection);
	}
}
