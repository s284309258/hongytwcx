package com.cff.springbootwork.activemq.model;


import com.cff.springbootwork.activemq.util.Sqlca;

import java.sql.Connection;

public class DefaultMqModel implements QueuesModelFace{

	public String title;
	public String content;
	public Integer type;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "DefaultMqModel [title=" + title + ", content=" + content + ", type=" + type + "]";
	}

	@Override
	public Object HandleDeQueuedModel(Connection connection) throws Exception {
		String sss = Sqlca.getString(connection,"select userName from sys_user where username='admin'");
		System.out.println(this.getTitle());
		return null;
	}
}
