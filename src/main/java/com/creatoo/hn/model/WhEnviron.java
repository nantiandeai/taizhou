package com.creatoo.hn.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "wh_environ")
public class WhEnviron {
    @Id
    private String environid;

    private String accesstoken;

    private Integer tokenexpiry;

    private Date tokentime;

    private String jsticket;

    private Integer ticketexpiry;

    private Date tickettime;

    /**
     * 应用类型1：PC微信授权，2：微网站微信授权，3：APP微信授权
     */
    private Integer apptype;

    /**
     * @return environid
     */
    public String getEnvironid() {
        return environid;
    }

    /**
     * @param environid
     */
    public void setEnvironid(String environid) {
        this.environid = environid;
    }

    /**
     * @return accesstoken
     */
    public String getAccesstoken() {
        return accesstoken;
    }

    /**
     * @param accesstoken
     */
    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    /**
     * @return tokenexpiry
     */
    public Integer getTokenexpiry() {
        return tokenexpiry;
    }

    /**
     * @param tokenexpiry
     */
    public void setTokenexpiry(Integer tokenexpiry) {
        this.tokenexpiry = tokenexpiry;
    }

    /**
     * @return tokentime
     */
    public Date getTokentime() {
        return tokentime;
    }

    /**
     * @param tokentime
     */
    public void setTokentime(Date tokentime) {
        this.tokentime = tokentime;
    }

    /**
     * @return jsticket
     */
    public String getJsticket() {
        return jsticket;
    }

    /**
     * @param jsticket
     */
    public void setJsticket(String jsticket) {
        this.jsticket = jsticket;
    }

    /**
     * @return ticketexpiry
     */
    public Integer getTicketexpiry() {
        return ticketexpiry;
    }

    /**
     * @param ticketexpiry
     */
    public void setTicketexpiry(Integer ticketexpiry) {
        this.ticketexpiry = ticketexpiry;
    }

    /**
     * @return tickettime
     */
    public Date getTickettime() {
        return tickettime;
    }

    /**
     * @param tickettime
     */
    public void setTickettime(Date tickettime) {
        this.tickettime = tickettime;
    }

    /**
     * 获取应用类型1：PC微信授权，2：微网站微信授权，3：APP微信授权
     *
     * @return apptype - 应用类型1：PC微信授权，2：微网站微信授权，3：APP微信授权
     */
    public Integer getApptype() {
        return apptype;
    }

    /**
     * 设置应用类型1：PC微信授权，2：微网站微信授权，3：APP微信授权
     *
     * @param apptype 应用类型1：PC微信授权，2：微网站微信授权，3：APP微信授权
     */
    public void setApptype(Integer apptype) {
        this.apptype = apptype;
    }
}