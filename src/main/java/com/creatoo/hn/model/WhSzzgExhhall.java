package com.creatoo.hn.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "wh_szzg_exhhall")
public class WhSzzgExhhall {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 展馆名称
     */
    private String hallname;

    /**
     * 展馆简介
     */
    private String hallsummary;

    /**
     * 展馆地址
     */
    private String halladdress;

    /**
     * 展馆联系电话
     */
    private String hallphone;

    /**
     * 展馆封面
     */
    private String hallcover;

    /**
     * 展馆外景全景图
     */
    private String hallexterior360;

    /**
     * 展馆内景全景图
     */
    @Column(name = "hallInterior360")
    private String hallinterior360;

    /**
     * 展馆排序(预留排序)
     */
    private Integer hallsort;

    /**
     * 是否推荐：0：否，1：是，默认0
     */
    private Integer isrecommend;

    /**
     * 流程状态：0初始，1送审，2已审，3已发
     */
    private Integer hallstate;

    /**
     * 最后操作人
     */
    private String lastoperator;

    /**
     * 最后操作时间
     */
    private Date lastoperatortime;

    /**
     * 是否删除。0：否，1：是
     */
    private Integer isdel;

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
     * 获取展馆名称
     *
     * @return hallname - 展馆名称
     */
    public String getHallname() {
        return hallname;
    }

    /**
     * 设置展馆名称
     *
     * @param hallname 展馆名称
     */
    public void setHallname(String hallname) {
        this.hallname = hallname;
    }

    /**
     * 获取展馆简介
     *
     * @return hallsummary - 展馆简介
     */
    public String getHallsummary() {
        return hallsummary;
    }

    /**
     * 设置展馆简介
     *
     * @param hallsummary 展馆简介
     */
    public void setHallsummary(String hallsummary) {
        this.hallsummary = hallsummary;
    }

    /**
     * 获取展馆地址
     *
     * @return halladdress - 展馆地址
     */
    public String getHalladdress() {
        return halladdress;
    }

    /**
     * 设置展馆地址
     *
     * @param halladdress 展馆地址
     */
    public void setHalladdress(String halladdress) {
        this.halladdress = halladdress;
    }

    /**
     * 获取展馆联系电话
     *
     * @return hallphone - 展馆联系电话
     */
    public String getHallphone() {
        return hallphone;
    }

    /**
     * 设置展馆联系电话
     *
     * @param hallphone 展馆联系电话
     */
    public void setHallphone(String hallphone) {
        this.hallphone = hallphone;
    }

    /**
     * 获取展馆封面
     *
     * @return hallcover - 展馆封面
     */
    public String getHallcover() {
        return hallcover;
    }

    /**
     * 设置展馆封面
     *
     * @param hallcover 展馆封面
     */
    public void setHallcover(String hallcover) {
        this.hallcover = hallcover;
    }

    /**
     * 获取展馆外景全景图
     *
     * @return hallexterior360 - 展馆外景全景图
     */
    public String getHallexterior360() {
        return hallexterior360;
    }

    /**
     * 设置展馆外景全景图
     *
     * @param hallexterior360 展馆外景全景图
     */
    public void setHallexterior360(String hallexterior360) {
        this.hallexterior360 = hallexterior360;
    }

    /**
     * 获取展馆内景全景图
     *
     * @return hallInterior360 - 展馆内景全景图
     */
    public String getHallinterior360() {
        return hallinterior360;
    }

    /**
     * 设置展馆内景全景图
     *
     * @param hallinterior360 展馆内景全景图
     */
    public void setHallinterior360(String hallinterior360) {
        this.hallinterior360 = hallinterior360;
    }

    /**
     * 获取展馆排序(预留排序)
     *
     * @return hallsort - 展馆排序(预留排序)
     */
    public Integer getHallsort() {
        return hallsort;
    }

    /**
     * 设置展馆排序(预留排序)
     *
     * @param hallsort 展馆排序(预留排序)
     */
    public void setHallsort(Integer hallsort) {
        this.hallsort = hallsort;
    }

    /**
     * 获取是否推荐：0：否，1：是，默认0
     *
     * @return isrecommend - 是否推荐：0：否，1：是，默认0
     */
    public Integer getIsrecommend() {
        return isrecommend;
    }

    /**
     * 设置是否推荐：0：否，1：是，默认0
     *
     * @param isrecommend 是否推荐：0：否，1：是，默认0
     */
    public void setIsrecommend(Integer isrecommend) {
        this.isrecommend = isrecommend;
    }

    /**
     * 获取流程状态：0初始，1送审，2已审，3已发
     *
     * @return hallstate - 流程状态：0初始，1送审，2已审，3已发
     */
    public Integer getHallstate() {
        return hallstate;
    }

    /**
     * 设置流程状态：0初始，1送审，2已审，3已发
     *
     * @param hallstate 流程状态：0初始，1送审，2已审，3已发
     */
    public void setHallstate(Integer hallstate) {
        this.hallstate = hallstate;
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
     * @return lastoperatortime - 最后操作时间
     */
    public Date getLastoperatortime() {
        return lastoperatortime;
    }

    /**
     * 设置最后操作时间
     *
     * @param lastoperatortime 最后操作时间
     */
    public void setLastoperatortime(Date lastoperatortime) {
        this.lastoperatortime = lastoperatortime;
    }

    /**
     * 获取是否删除。0：否，1：是
     *
     * @return isdel - 是否删除。0：否，1：是
     */
    public Integer getIsdel() {
        return isdel;
    }

    /**
     * 设置是否删除。0：否，1：是
     *
     * @param isdel 是否删除。0：否，1：是
     */
    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }
}