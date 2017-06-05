package com.creatoo.hn.model;

import javax.persistence.*;

@Table(name = "wh_user_branch_rel")
public class WhUserBranchRel {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 系统用户ID
     */
    private String userid;

    /**
     * 分馆ID
     */
    private String branchid;

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
     * 获取系统用户ID
     *
     * @return userid - 系统用户ID
     */
    public String getUserid() {
        return userid;
    }

    /**
     * 设置系统用户ID
     *
     * @param userid 系统用户ID
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /**
     * 获取分馆ID
     *
     * @return branchid - 分馆ID
     */
    public String getBranchid() {
        return branchid;
    }

    /**
     * 设置分馆ID
     *
     * @param branchid 分馆ID
     */
    public void setBranchid(String branchid) {
        this.branchid = branchid;
    }
}