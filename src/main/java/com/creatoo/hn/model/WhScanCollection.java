package com.creatoo.hn.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "wh_scan_collection")
public class WhScanCollection {
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
     * 关联类型:1、活动；2、活动室预约；3、培训
     */
    private Integer reltype;

    /**
     * 记录时间
     */
    private Date recordtime;

    /**
     * 违规类型:1、擅自取消；2、未参加
     */
    private Integer violationtype;

    /**
     * 记录状态：1、有效；2、无效
     */
    private Integer recordstate;

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
     * 获取关联类型:1、活动；2、活动室预约；3、培训
     *
     * @return reltype - 关联类型:1、活动；2、活动室预约；3、培训
     */
    public Integer getReltype() {
        return reltype;
    }

    /**
     * 设置关联类型:1、活动；2、活动室预约；3、培训
     *
     * @param reltype 关联类型:1、活动；2、活动室预约；3、培训
     */
    public void setReltype(Integer reltype) {
        this.reltype = reltype;
    }

    /**
     * 获取记录时间
     *
     * @return recordtime - 记录时间
     */
    public Date getRecordtime() {
        return recordtime;
    }

    /**
     * 设置记录时间
     *
     * @param recordtime 记录时间
     */
    public void setRecordtime(Date recordtime) {
        this.recordtime = recordtime;
    }

    /**
     * 获取违规类型:1、擅自取消；2、未参加
     *
     * @return violationtype - 违规类型:1、擅自取消；2、未参加
     */
    public Integer getViolationtype() {
        return violationtype;
    }

    /**
     * 设置违规类型:1、擅自取消；2、未参加
     *
     * @param violationtype 违规类型:1、擅自取消；2、未参加
     */
    public void setViolationtype(Integer violationtype) {
        this.violationtype = violationtype;
    }

    /**
     * 获取记录状态：1、有效；2、无效
     *
     * @return recordstate - 记录状态：1、有效；2、无效
     */
    public Integer getRecordstate() {
        return recordstate;
    }

    /**
     * 设置记录状态：1、有效；2、无效
     *
     * @param recordstate 记录状态：1、有效；2、无效
     */
    public void setRecordstate(Integer recordstate) {
        this.recordstate = recordstate;
    }
}