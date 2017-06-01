package com.creatoo.hn.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "wh_szyd_zx")
public class WhSzydZx {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 资讯标题
     */
    private String infotitle;

    /**
     * 资讯简介
     */
    private String infosummary;

    /**
     * 资讯作者
     */
    private String author;

    /**
     * 流程状态：1：可编辑, 9：待审核, 2：待发布，6：已发布, 4：已下架
     */
    private Integer infostate;

    /**
     * 创建时间
     */
    private Date infocreatetime;

    /**
     * 资讯排序
     */
    private Integer infosort;

    /**
     * 是否推荐：0：否，1：是，默认0
     */
    private Integer isrecommend;

    /**
     * 来源
     */
    private String infosource;

    /**
     * 最后修改时间
     */
    private Date infoupdatetime;

    /**
     * 是否删除：0：否，1：是
     */
    private Integer isdel;

    /**
     * 资讯封面url
     */
    private String infocover;

    /**
     * 资讯链接
     */
    private String infolink;

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
     * 获取资讯标题
     *
     * @return infotitle - 资讯标题
     */
    public String getInfotitle() {
        return infotitle;
    }

    /**
     * 设置资讯标题
     *
     * @param infotitle 资讯标题
     */
    public void setInfotitle(String infotitle) {
        this.infotitle = infotitle;
    }

    /**
     * 获取资讯简介
     *
     * @return infosummary - 资讯简介
     */
    public String getInfosummary() {
        return infosummary;
    }

    /**
     * 设置资讯简介
     *
     * @param infosummary 资讯简介
     */
    public void setInfosummary(String infosummary) {
        this.infosummary = infosummary;
    }

    /**
     * 获取资讯作者
     *
     * @return author - 资讯作者
     */
    public String getAuthor() {
        return author;
    }

    /**
     * 设置资讯作者
     *
     * @param author 资讯作者
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * 获取流程状态：1：可编辑, 9：待审核, 2：待发布，6：已发布, 4：已下架
     *
     * @return infostate - 流程状态：1：可编辑, 9：待审核, 2：待发布，6：已发布, 4：已下架
     */
    public Integer getInfostate() {
        return infostate;
    }

    /**
     * 设置流程状态：1：可编辑, 9：待审核, 2：待发布，6：已发布, 4：已下架
     *
     * @param infostate 流程状态：1：可编辑, 9：待审核, 2：待发布，6：已发布, 4：已下架
     */
    public void setInfostate(Integer infostate) {
        this.infostate = infostate;
    }

    /**
     * 获取创建时间
     *
     * @return infocreatetime - 创建时间
     */
    public Date getInfocreatetime() {
        return infocreatetime;
    }

    /**
     * 设置创建时间
     *
     * @param infocreatetime 创建时间
     */
    public void setInfocreatetime(Date infocreatetime) {
        this.infocreatetime = infocreatetime;
    }

    /**
     * 获取资讯排序
     *
     * @return infosort - 资讯排序
     */
    public Integer getInfosort() {
        return infosort;
    }

    /**
     * 设置资讯排序
     *
     * @param infosort 资讯排序
     */
    public void setInfosort(Integer infosort) {
        this.infosort = infosort;
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
     * 获取来源
     *
     * @return infosource - 来源
     */
    public String getInfosource() {
        return infosource;
    }

    /**
     * 设置来源
     *
     * @param infosource 来源
     */
    public void setInfosource(String infosource) {
        this.infosource = infosource;
    }

    /**
     * 获取最后修改时间
     *
     * @return infoupdatetime - 最后修改时间
     */
    public Date getInfoupdatetime() {
        return infoupdatetime;
    }

    /**
     * 设置最后修改时间
     *
     * @param infoupdatetime 最后修改时间
     */
    public void setInfoupdatetime(Date infoupdatetime) {
        this.infoupdatetime = infoupdatetime;
    }

    /**
     * 获取是否删除：0：否，1：是
     *
     * @return isdel - 是否删除：0：否，1：是
     */
    public Integer getIsdel() {
        return isdel;
    }

    /**
     * 设置是否删除：0：否，1：是
     *
     * @param isdel 是否删除：0：否，1：是
     */
    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    /**
     * 获取资讯封面url
     *
     * @return infocover - 资讯封面url
     */
    public String getInfocover() {
        return infocover;
    }

    /**
     * 设置资讯封面url
     *
     * @param infocover 资讯封面url
     */
    public void setInfocover(String infocover) {
        this.infocover = infocover;
    }

    /**
     * 获取资讯链接
     *
     * @return infolink - 资讯链接
     */
    public String getInfolink() {
        return infolink;
    }

    /**
     * 设置资讯链接
     *
     * @param infolink 资讯链接
     */
    public void setInfolink(String infolink) {
        this.infolink = infolink;
    }
}