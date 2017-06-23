package com.creatoo.hn.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_culture_unit")
public class WhgCultureUnit {
    /**
     * 主键PK
     */
    @Id
    private String id;

    /**
     * 单位名
     */
    private String unitname;

    /**
     * 单位简介
     */
    private String unitdesc;

    /**
     * 单位封面
     */
    private String unitcover;

    /**
     * 创建时间
     */
    private Date unitcreatetime;

    /**
     * 单位状态：1、启用，2：停用
     */
    private Integer unitstate;

    /**
     * 单位链接
     */
    private String unitlink;

    /**
     * 单位编码
     */
    private String unitcode;

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
     * 获取单位名
     *
     * @return unitname - 单位名
     */
    public String getUnitname() {
        return unitname;
    }

    /**
     * 设置单位名
     *
     * @param unitname 单位名
     */
    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    /**
     * 获取单位简介
     *
     * @return unitdesc - 单位简介
     */
    public String getUnitdesc() {
        return unitdesc;
    }

    /**
     * 设置单位简介
     *
     * @param unitdesc 单位简介
     */
    public void setUnitdesc(String unitdesc) {
        this.unitdesc = unitdesc;
    }

    /**
     * 获取单位封面
     *
     * @return unitcover - 单位封面
     */
    public String getUnitcover() {
        return unitcover;
    }

    /**
     * 设置单位封面
     *
     * @param unitcover 单位封面
     */
    public void setUnitcover(String unitcover) {
        this.unitcover = unitcover;
    }

    /**
     * 获取创建时间
     *
     * @return unitcreatetime - 创建时间
     */
    public Date getUnitcreatetime() {
        return unitcreatetime;
    }

    /**
     * 设置创建时间
     *
     * @param unitcreatetime 创建时间
     */
    public void setUnitcreatetime(Date unitcreatetime) {
        this.unitcreatetime = unitcreatetime;
    }

    /**
     * 获取单位状态：1、启用，2：停用
     *
     * @return unitstate - 单位状态：1、启用，2：停用
     */
    public Integer getUnitstate() {
        return unitstate;
    }

    /**
     * 设置单位状态：1、启用，2：停用
     *
     * @param unitstate 单位状态：1、启用，2：停用
     */
    public void setUnitstate(Integer unitstate) {
        this.unitstate = unitstate;
    }

    /**
     * 获取单位链接
     *
     * @return unitlink - 单位链接
     */
    public String getUnitlink() {
        return unitlink;
    }

    /**
     * 设置单位链接
     *
     * @param unitlink 单位链接
     */
    public void setUnitlink(String unitlink) {
        this.unitlink = unitlink;
    }

    /**
     * 获取单位编码
     *
     * @return unitcode - 单位编码
     */
    public String getUnitcode() {
        return unitcode;
    }

    /**
     * 设置单位编码
     *
     * @param unitcode 单位编码
     */
    public void setUnitcode(String unitcode) {
        this.unitcode = unitcode;
    }
}