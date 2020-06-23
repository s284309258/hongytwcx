package com.platform.entity;

import java.io.Serializable;


/**
 * 实体
 * 表名 nideshop_user_level
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-16 16:52:22
 */
public class UserLevelEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Integer id;
    //
    private String name;
    //
    private String description;

    private Integer consumeMoney;

    private Integer referenceNum;

    private Integer taskNum;

    private Integer teamReward;

    private Integer referenceReward;

    private Integer upgrade;

    public Integer getUpgrade() {
        return upgrade;
    }

    public void setUpgrade(Integer upgrade) {
        this.upgrade = upgrade;
    }

    public Integer getConsumeMoney() {
        return consumeMoney;
    }

    public void setConsumeMoney(Integer consumeMoney) {
        this.consumeMoney = consumeMoney;
    }

    public Integer getReferenceNum() {
        return referenceNum;
    }

    public void setReferenceNum(Integer referenceNum) {
        this.referenceNum = referenceNum;
    }

    public Integer getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(Integer taskNum) {
        this.taskNum = taskNum;
    }

    public Integer getTeamReward() {
        return teamReward;
    }

    public void setTeamReward(Integer teamReward) {
        this.teamReward = teamReward;
    }

    public Integer getReferenceReward() {
        return referenceReward;
    }

    public void setReferenceReward(Integer referenceReward) {
        this.referenceReward = referenceReward;
    }

    /**
     * 设置：
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置：
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取：
     */
    public String getName() {
        return name;
    }

    /**
     * 设置：
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取：
     */
    public String getDescription() {
        return description;
    }
}
