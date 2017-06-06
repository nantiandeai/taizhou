package com.creatoo.hn.model;

import javax.persistence.*;

@Table(name = "wh_branch_rel")
public class WhBranchRel {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 关联ID
     */
    private String relid;

    /**
     * 关联类型:详见枚举EnumTypeClazz
     */
    private String reltype;

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
     * 获取关联ID
     *
     * @return relid - 关联ID
     */
    public String getRelid() {
        return relid;
    }

    /**
     * 设置关联ID
     *
     * @param relid 关联ID
     */
    public void setRelid(String relid) {
        this.relid = relid;
    }

    /**
     * 获取关联类型:详见枚举EnumTypeClazz
     *
     * @return reltype - 关联类型:详见枚举EnumTypeClazz
     */
    public String getReltype() {
        return reltype;
    }

    /**
     * 设置关联类型:详见枚举EnumTypeClazz
     *
     * @param reltype 关联类型:详见枚举EnumTypeClazz
     */
    public void setReltype(String reltype) {
        this.reltype = reltype;
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