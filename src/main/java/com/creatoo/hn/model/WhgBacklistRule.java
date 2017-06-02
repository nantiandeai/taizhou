package com.creatoo.hn.model;

import javax.persistence.*;

@Table(name = "whg_backlist_rule")
public class WhgBacklistRule {
    /**
     * 主键Id
     */
    @Id
    private String id;

    /**
     * 取消次数
     */
    private Integer cancelnum;

    /**
     * 因取消活动加入黑名单天数
     */
    private Integer cancelday;

    /**
     * 预定活动未参加次数
     */
    private Integer missnum;

    /**
     * 因为未参加活动加入黑名单天数
     */
    private Integer missday;

    /**
     * 获取主键Id
     *
     * @return id - 主键Id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置主键Id
     *
     * @param id 主键Id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取取消次数
     *
     * @return cancelnum - 取消次数
     */
    public Integer getCancelnum() {
        return cancelnum;
    }

    /**
     * 设置取消次数
     *
     * @param cancelnum 取消次数
     */
    public void setCancelnum(Integer cancelnum) {
        this.cancelnum = cancelnum;
    }

    /**
     * 获取因取消活动加入黑名单天数
     *
     * @return cancelday - 因取消活动加入黑名单天数
     */
    public Integer getCancelday() {
        return cancelday;
    }

    /**
     * 设置因取消活动加入黑名单天数
     *
     * @param cancelday 因取消活动加入黑名单天数
     */
    public void setCancelday(Integer cancelday) {
        this.cancelday = cancelday;
    }

    /**
     * 获取预定活动未参加次数
     *
     * @return missnum - 预定活动未参加次数
     */
    public Integer getMissnum() {
        return missnum;
    }

    /**
     * 设置预定活动未参加次数
     *
     * @param missnum 预定活动未参加次数
     */
    public void setMissnum(Integer missnum) {
        this.missnum = missnum;
    }

    /**
     * 获取因为未参加活动加入黑名单天数
     *
     * @return missday - 因为未参加活动加入黑名单天数
     */
    public Integer getMissday() {
        return missday;
    }

    /**
     * 设置因为未参加活动加入黑名单天数
     *
     * @param missday 因为未参加活动加入黑名单天数
     */
    public void setMissday(Integer missday) {
        this.missday = missday;
    }
}