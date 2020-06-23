package com.cff.springbootwork.activemq.model;

import com.cff.springbootwork.activemq.util.Sqlca;

import java.sql.Connection;

public class SeccondMqModel extends DefaultMqModel {
	public String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "SeccondMqModel [remark=" + remark + ", title=" + title + ", content=" + content + ", type=" + type
				+ "]";
	}

	@Override
	public Object HandleDeQueuedModel(Connection connection) throws Exception
	{
		String sss = Sqlca.getString(connection,"select userName from sys_user where username='admin'");
		System.out.println(this.getTitle());
		return null;
	}

}
