package com.creatoo.hn.model;

import javax.persistence.Id;
import java.util.Date;

public class WhgSpecilResourceSarch {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 标题
     */
    private String name;

    /**
     *  所属单位id
     */
    private String branch;
    /**
     * 所属单位名字。非数据库字段，通过表关联查询而得
     */
    private String branchName;
    /**
     * 资源类型1.视频 2.图片 3.文档
     */
    private String type;

    /**
     * 封面图片
     */
    private String picture;

    /**
     * 资源地址
     */
    private String address;

    /**
     * 创建时间
     */
    private Date crtdate;

    /**
     * 状态：1可编辑，9待审核，2待发布，6已发布，4已下架，5已撤消
     */
    private Integer state;

    /**
     * 是否推荐：0：否，1：是，默认0
     */
    private Integer isrecommend;

    /**
     * 最后操作时间
     */
    private Date statemdfdate;

    /**
     * 最后操作人
     */
    private String statemdfuser;

    /**
     * 是否已删除。0：否，1：是
     */
    private Integer isdel;

    /**
     * 资源描述
     */
    private String introduction;

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
     * 获取标题
     *
     * @return name - 标题
     */
    public String getName() {
        return name;
    }

    /**
     * 设置标题
     *
     * @param name 标题
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取 所属单位
     *
     * @return branch -  所属单位
     */
    public String getBranch() {
        return branch;
    }

    /**
     * 设置 所属单位
     *
     * @param branch  所属单位
     */
    public void setBranch(String branch) {
        this.branch = branch;
    }

    /**
     * 获取资源类型1.视频 2.图片 3.文档
     *
     * @return type - 资源类型1.视频 2.图片 3.文档
     */
    public String getType() {
        return type;
    }

    /**
     * 设置资源类型1.视频 2.图片 3.文档
     *
     * @param type 资源类型1.视频 2.图片 3.文档
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取封面图片
     *
     * @return picture - 封面图片
     */
    public String getPicture() {
        return picture;
    }

    /**
     * 设置封面图片
     *
     * @param picture 封面图片
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     * 获取资源地址
     *
     * @return address - 资源地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置资源地址
     *
     * @param address 资源地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取创建时间
     *
     * @return crtdate - 创建时间
     */
    public Date getCrtdate() {
        return crtdate;
    }

    /**
     * 设置创建时间
     *
     * @param crtdate 创建时间
     */
    public void setCrtdate(Date crtdate) {
        this.crtdate = crtdate;
    }

    /**
     * 获取状态：1可编辑，9待审核，2待发布，6已发布，4已下架，5已撤消
     *
     * @return state - 状态：1可编辑，9待审核，2待发布，6已发布，4已下架，5已撤消
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态：1可编辑，9待审核，2待发布，6已发布，4已下架，5已撤消
     *
     * @param state 状态：1可编辑，9待审核，2待发布，6已发布，4已下架，5已撤消
     */
    public void setState(Integer state) {
        this.state = state;
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
     * 获取最后操作时间
     *
     * @return statemdfdate - 最后操作时间
     */
    public Date getStatemdfdate() {
        return statemdfdate;
    }

    /**
     * 设置最后操作时间
     *
     * @param statemdfdate 最后操作时间
     */
    public void setStatemdfdate(Date statemdfdate) {
        this.statemdfdate = statemdfdate;
    }

    /**
     * 获取最后操作人
     *
     * @return statemdfuser - 最后操作人
     */
    public String getStatemdfuser() {
        return statemdfuser;
    }

    /**
     * 设置最后操作人
     *
     * @param statemdfuser 最后操作人
     */
    public void setStatemdfuser(String statemdfuser) {
        this.statemdfuser = statemdfuser;
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

    /**
     * 获取资源描述
     *
     * @return introduction - 资源描述
     */
    public String getIntroduction() {
        return introduction;
    }

    /**
     * 设置资源描述
     *
     * @param introduction 资源描述
     */
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}