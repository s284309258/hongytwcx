package com.platform.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 商城_用户级别
 * 
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-15 08:03:41
 */
public class UserLevelVo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Integer id;
	//名称
	private String name;
	//描述
	private String description;

	private int consumeMoney;

	private int referenceNum;

	private int taskNum;

	private int teamReward;

	private int referenceReward;


	public int getConsumeMoney() {
		return consumeMoney;
	}

	public void setConsumeMoney(int consumeMoney) {
		this.consumeMoney = consumeMoney;
	}

	public int getReferenceNum() {
		return referenceNum;
	}

	public void setReferenceNum(int referenceNum) {
		this.referenceNum = referenceNum;
	}

	public int getTaskNum() {
		return taskNum;
	}

	public void setTaskNum(int taskNum) {
		this.taskNum = taskNum;
	}

	public int getTeamReward() {
		return teamReward;
	}

	public void setTeamReward(int teamReward) {
		this.teamReward = teamReward;
	}

	public int getReferenceReward() {
		return referenceReward;
	}

	public void setReferenceReward(int referenceReward) {
		this.referenceReward = referenceReward;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
