package com.creatoo.hn.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "wh_szzg_exhibit")
public class WhSzzgExhibit {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 展品名称
     */
    private String exhname;

    /**
     * 展品简介
     */
    private String exhsummary;

    /**
     * 展品类别
     */
    private String exhtype;

    /**
     * 展品主题
     */
    private String exhtheme;

    /**
     * 展品照片
     */
    private String exhphoto;

    /**
     * 流程状态：0初始，1送审，2已审，3已发
     */
    private Integer exhstate;

    /**
     * 展馆排序(预留排序)
     */
    private Integer exhsort;

    /**
     * 是否推荐：0：否，1：是，默认0
     */
    private Integer isrecommend;

    /**
     * 最后操作人
     */
    private String lastoperator;

    /**
     * 最后操作时间
     */
    private Date lastoperatortime;

    /**
     * 所属展馆
     */
    private String hallid;

    /**
     * 是否已删除。0：否，1：是
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
     * 获取展品名称
     *
     * @return exhname - 展品名称
     */
    public String getExhname() {
        return exhname;
    }

    /**
     * 设置展品名称
     *
     * @param exhname 展品名称
     */
    public void setExhname(String exhname) {
        this.exhname = exhname;
    }

    /**
     * 获取展品简介
     *
     * @return exhsummary - 展品简介
     */
    public String getExhsummary() {
        return exhsummary;
    }

    /**
     * 设置展品简介
     *
     * @param exhsummary 展品简介
     */
    public void setExhsummary(String exhsummary) {
        this.exhsummary = exhsummary;
    }

    /**
     * 获取展品类别
     *
     * @return exhtype - 展品类别
     */
    public String getExhtype() {
        return exhtype;
    }

    /**
     * 设置展品类别
     *
     * @param exhtype 展品类别
     */
    public void setExhtype(String exhtype) {
        this.exhtype = exhtype;
    }

    /**
     * 获取展品主题
     *
     * @return exhtheme - 展品主题
     */
    public String getExhtheme() {
        return exhtheme;
    }

    /**
     * 设置展品主题
     *
     * @param exhtheme 展品主题
     */
    public void setExhtheme(String exhtheme) {
        this.exhtheme = exhtheme;
    }

    /**
     * 获取展品照片
     *
     * @return exhphoto - 展品照片
     */
    public String getExhphoto() {
        return exhphoto;
    }

    /**
     * 设置展品照片
     *
     * @param exhphoto 展品照片
     */
    public void setExhphoto(String exhphoto) {
        this.exhphoto = exhphoto;
    }

    /**
     * 获取流程状态：0初始，1送审，2已审，3已发
     *
     * @return exhstate - 流程状态：0初始，1送审，2已审，3已发
     */
    public Integer getExhstate() {
        return exhstate;
    }

    /**
     * 设置流程状态：0初始，1送审，2已审，3已发
     *
     * @param exhstate 流程状态：0初始，1送审，2已审，3已发
     */
    public void setExhstate(Integer exhstate) {
        this.exhstate = exhstate;
    }

    /**
     * 获取展馆排序(预留排序)
     *
     * @return exhsort - 展馆排序(预留排序)
     */
    public Integer getExhsort() {
        return exhsort;
    }

    /**
     * 设置展馆排序(预留排序)
     *
     * @param exhsort 展馆排序(预留排序)
     */
    public void setExhsort(Integer exhsort) {
        this.exhsort = exhsort;
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
     * 获取所属展馆
     *
     * @return hallid - 所属展馆
     */
    public String getHallid() {
        return hallid;
    }

    /**
     * 设置所属展馆
     *
     * @param hallid 所属展馆
     */
    public void setHallid(String hallid) {
        this.hallid = hallid;
    }

    /**
     * 获取是否已删除。0：否，1：是
     *
     * @return isdel - 是否已删除。0：否，1：是
     */
    public Integer getIsdel() {
        return isdel;
    }

    /**
     * 设置是否已删除。0：否，1：是
     *
     * @param isdel 是否已删除。0：否，1：是
     */
    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }
}