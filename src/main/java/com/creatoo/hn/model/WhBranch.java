package com.creatoo.hn.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "wh_branch")
public class WhBranch {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 区域ID
     */
    private String areaid;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系方式
     */
    private String contactway;

    /**
     * 最后操作人
     */
    private String lastoperator;

    /**
     * 最后操作时间
     */
    private Date operatetime;

    /**
     * 状态:0、未启用；1、已启用
     */
    private String state;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public String getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取区域ID
     *
     * @return areaid - 区域ID
     */
    public String getAreaid() {
        return areaid;
    }

    /**
     * 设置区域ID
     *
     * @param areaid 区域ID
     */
    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    /**
     * 获取联系人
     *
     * @return contacts - 联系人
     */
    public String getContacts() {
        return contacts;
    }

    /**
     * 设置联系人
     *
     * @param contacts 联系人
     */
    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    /**
     * 获取联系方式
     *
     * @return contactway - 联系方式
     */
    public String getContactway() {
        return contactway;
    }

    /**
     * 设置联系方式
     *
     * @param contactway 联系方式
     */
    public void setContactway(String contactway) {
        this.contactway = contactway;
    }

    /**
     * 获取最后操作人
     *
     * @return lastoperator - 最后操作人
     */
    public String getLastoperator() {
        return lastoperator;
    }

    /**
     * 设置最后操作人
     *
     * @param lastoperator 最后操作人
     */
    public void setLastoperator(String lastoperator) {
        this.lastoperator = lastoperator;
    }

    /**
     * 获取最后操作时间
     *
     * @return operatetime - 最后操作时间
     */
    public Date getOperatetime() {
        return operatetime;
    }

    /**
     * 设置最后操作时间
     *
     * @param operatetime 最后操作时间
     */
    public void setOperatetime(Date operatetime) {
        this.operatetime = operatetime;
    }

    /**
     * 获取状态:0、未启用；1、已启用
     *
     * @return state - 状态:0、未启用；1、已启用
     */
    public String getState() {
        return state;
    }

    /**
     * 设置状态:0、未启用；1、已启用
     *
     * @param state 状态:0、未启用；1、已启用
     */
    public void setState(String state) {
        this.state = state;
    }
}