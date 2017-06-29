package com.creatoo.hn.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "whg_culture_zx")
public class WhgCultureZx {
    /**
     * 主键PK
     */
    @Id
    private String id;

    /**
     * 资讯标题
     */
    private String culzxtitle;

    /**
     * 资讯简介
     */
    private String culzxdesc;

    /**
     * 资讯链接
     */
    @Column(name = "culzxLink")
    private String culzxlink;

    /**
     * 流程状态：0初始 initial，1送审 checkpending，2已审 checked，3已发 published
     */
    private Integer culzxstate;

    /**
     * 资讯创建人
     */
    private String culzxcreator;

    /**
     * 资讯创建时间
     */
    private Date culzxcreattime;

    /**
     * 删除状态：1、已删除；2、未删除
     */
    private Integer isdel;

    /**
     * 获取主键PK
     *
     * @return id - 主键PK
     */
    public String getId() {
        return id;
    }

    /**
     * 设置主键PK
     *
     * @param id 主键PK
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取资讯标题
     *
     * @return culzxtitle - 资讯标题
     */
    public String getCulzxtitle() {
        return culzxtitle;
    }

    /**
     * 设置资讯标题
     *
     * @param culzxtitle 资讯标题
     */
    public void setCulzxtitle(String culzxtitle) {
        this.culzxtitle = culzxtitle;
    }

    /**
     * 获取资讯简介
     *
     * @return culzxdesc - 资讯简介
     */
    public String getCulzxdesc() {
        return culzxdesc;
    }

    /**
     * 设置资讯简介
     *
     * @param culzxdesc 资讯简介
     */
    public void setCulzxdesc(String culzxdesc) {
        this.culzxdesc = culzxdesc;
    }

    /**
     * 获取资讯链接
     *
     * @return culzxLink - 资讯链接
     */
    public String getCulzxlink() {
        return culzxlink;
    }

    /**
     * 设置资讯链接
     *
     * @param culzxlink 资讯链接
     */
    public void setCulzxlink(String culzxlink) {
        this.culzxlink = culzxlink;
    }

    /**
     * 获取流程状态：0初始 initial，1送审 checkpending，2已审 checked，3已发 published
     *
     * @return culzxstate - 流程状态：0初始 initial，1送审 checkpending，2已审 checked，3已发 published
     */
    public Integer getCulzxstate() {
        return culzxstate;
    }

    /**
     * 设置流程状态：0初始 initial，1送审 checkpending，2已审 checked，3已发 published
     *
     * @param culzxstate 流程状态：0初始 initial，1送审 checkpending，2已审 checked，3已发 published
     */
    public void setCulzxstate(Integer culzxstate) {
        this.culzxstate = culzxstate;
    }

    /**
     * 获取资讯创建人
     *
     * @return culzxcreator - 资讯创建人
     */
    public String getCulzxcreator() {
        return culzxcreator;
    }

    /**
     * 设置资讯创建人
     *
     * @param culzxcreator 资讯创建人
     */
    public void setCulzxcreator(String culzxcreator) {
        this.culzxcreator = culzxcreator;
    }

    /**
     * 获取资讯创建时间
     *
     * @return culzxcreattime - 资讯创建时间
     */
    public Date getCulzxcreattime() {
        return culzxcreattime;
    }

    /**
     * 设置资讯创建时间
     *
     * @param culzxcreattime 资讯创建时间
     */
    public void setCulzxcreattime(Date culzxcreattime) {
        this.culzxcreattime = culzxcreattime;
    }

    /**
     * 获取删除状态：1、已删除；2、未删除
     *
     * @return isdel - 删除状态：1、已删除；2、未删除
     */
    public Integer getIsdel() {
        return isdel;
    }

    /**
     * 设置删除状态：1、已删除；2、未删除
     *
     * @param isdel 删除状态：1、已删除；2、未删除
     */
    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }
}