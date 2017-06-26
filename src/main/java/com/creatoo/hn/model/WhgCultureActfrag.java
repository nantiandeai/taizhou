package com.creatoo.hn.model;

import javax.persistence.*;

@Table(name = "whg_culture_actfrag")
public class WhgCultureActfrag {
    /**
     * 主键PK
     */
    @Id
    private String id;

    /**
     * 大型活动关联ID
     */
    private String culactid;

    /**
     * 片段状态：1、启用；2、停用
     */
    private Integer fragstate;

    /**
     * 片段内容
     */
    private String fragcontent;

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
     * 获取大型活动关联ID
     *
     * @return culactid - 大型活动关联ID
     */
    public String getCulactid() {
        return culactid;
    }

    /**
     * 设置大型活动关联ID
     *
     * @param culactid 大型活动关联ID
     */
    public void setCulactid(String culactid) {
        this.culactid = culactid;
    }

    /**
     * 获取片段状态：1、启用；2、停用
     *
     * @return fragstate - 片段状态：1、启用；2、停用
     */
    public Integer getFragstate() {
        return fragstate;
    }

    /**
     * 设置片段状态：1、启用；2、停用
     *
     * @param fragstate 片段状态：1、启用；2、停用
     */
    public void setFragstate(Integer fragstate) {
        this.fragstate = fragstate;
    }

    /**
     * 获取片段内容
     *
     * @return fragcontent - 片段内容
     */
    public String getFragcontent() {
        return fragcontent;
    }

    /**
     * 设置片段内容
     *
     * @param fragcontent 片段内容
     */
    public void setFragcontent(String fragcontent) {
        this.fragcontent = fragcontent;
    }
}