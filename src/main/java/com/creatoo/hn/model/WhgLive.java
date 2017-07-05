package com.creatoo.hn.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_live")
public class WhgLive {
    /**
     * 主键PK
     */
    @Id
    private String id;

    /**
     * 直播标题
     */
    private String livetitle;

    /**
     * 领域
     */
    private String domain;

    /**
     * 封面
     */
    private String livecover;

    /**
     * 首页大图
     */
    private String livelbt;

    /**
     * 置为轮播图：1、是，2、否
     */
    private Integer islbt;

    /**
     * 推荐：1、是，2、否
     */
    private Integer isrecommend;

    /**
     * 流程状态：0初始 initial，1送审 checkpending，2已审 checked，3已发 published
     */
    private Integer livestate;

    /**
     * 推流地址
     */
    private String flowaddr;

    /**
     * 创建人
     */
    private String livecreator;

    /**
     * 创建时间
     */
    private Date livecreattime;

    /**
     * 删除状态：1、已删除；2、未删除
     */
    private String isdel;

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
     * 获取直播标题
     *
     * @return livetitle - 直播标题
     */
    public String getLivetitle() {
        return livetitle;
    }

    /**
     * 设置直播标题
     *
     * @param livetitle 直播标题
     */
    public void setLivetitle(String livetitle) {
        this.livetitle = livetitle;
    }

    /**
     * 获取领域
     *
     * @return domain - 领域
     */
    public String getDomain() {
        return domain;
    }

    /**
     * 设置领域
     *
     * @param domain 领域
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * 获取封面
     *
     * @return livecover - 封面
     */
    public String getLivecover() {
        return livecover;
    }

    /**
     * 设置封面
     *
     * @param livecover 封面
     */
    public void setLivecover(String livecover) {
        this.livecover = livecover;
    }

    /**
     * 获取首页大图
     *
     * @return livelbt - 首页大图
     */
    public String getLivelbt() {
        return livelbt;
    }

    /**
     * 设置首页大图
     *
     * @param livelbt 首页大图
     */
    public void setLivelbt(String livelbt) {
        this.livelbt = livelbt;
    }

    /**
     * 获取置为轮播图：1、是，2、否
     *
     * @return islbt - 置为轮播图：1、是，2、否
     */
    public Integer getIslbt() {
        return islbt;
    }

    /**
     * 设置置为轮播图：1、是，2、否
     *
     * @param islbt 置为轮播图：1、是，2、否
     */
    public void setIslbt(Integer islbt) {
        this.islbt = islbt;
    }

    /**
     * 获取推荐：1、是，2、否
     *
     * @return isrecommend - 推荐：1、是，2、否
     */
    public Integer getIsrecommend() {
        return isrecommend;
    }

    /**
     * 设置推荐：1、是，2、否
     *
     * @param isrecommend 推荐：1、是，2、否
     */
    public void setIsrecommend(Integer isrecommend) {
        this.isrecommend = isrecommend;
    }

    /**
     * 获取流程状态：0初始 initial，1送审 checkpending，2已审 checked，3已发 published
     *
     * @return livestate - 流程状态：0初始 initial，1送审 checkpending，2已审 checked，3已发 published
     */
    public Integer getLivestate() {
        return livestate;
    }

    /**
     * 设置流程状态：0初始 initial，1送审 checkpending，2已审 checked，3已发 published
     *
     * @param livestate 流程状态：0初始 initial，1送审 checkpending，2已审 checked，3已发 published
     */
    public void setLivestate(Integer livestate) {
        this.livestate = livestate;
    }

    /**
     * 获取推流地址
     *
     * @return flowaddr - 推流地址
     */
    public String getFlowaddr() {
        return flowaddr;
    }

    /**
     * 设置推流地址
     *
     * @param flowaddr 推流地址
     */
    public void setFlowaddr(String flowaddr) {
        this.flowaddr = flowaddr;
    }

    /**
     * 获取创建人
     *
     * @return livecreator - 创建人
     */
    public String getLivecreator() {
        return livecreator;
    }

    /**
     * 设置创建人
     *
     * @param livecreator 创建人
     */
    public void setLivecreator(String livecreator) {
        this.livecreator = livecreator;
    }

    /**
     * 获取创建时间
     *
     * @return livecreattime - 创建时间
     */
    public Date getLivecreattime() {
        return livecreattime;
    }

    /**
     * 设置创建时间
     *
     * @param livecreattime 创建时间
     */
    public void setLivecreattime(Date livecreattime) {
        this.livecreattime = livecreattime;
    }

    /**
     * 获取删除状态：1、已删除；2、未删除
     *
     * @return isdel - 删除状态：1、已删除；2、未删除
     */
    public String getIsdel() {
        return isdel;
    }

    /**
     * 设置删除状态：1、已删除；2、未删除
     *
     * @param isdel 删除状态：1、已删除；2、未删除
     */
    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }
}