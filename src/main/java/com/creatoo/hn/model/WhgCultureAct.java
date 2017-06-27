package com.creatoo.hn.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "whg_culture_act")
public class WhgCultureAct {
    /**
     * 主键PK
     */
    @Id
    private String id;

    /**
     * 大型活动名
     */
    private String culactname;

    /**
     * 大型活动封面URL
     */
    private String culactcover;

    /**
     * 流程状态：0初始，1送审，2已审，3已发
     */
    private Integer culactstate;

    /**
     * 创建时间
     */
    private Date culactcreattime;

    /**
     * 创建人
     */
    private String culactuser;

    /**
     * 删除状态：1、已删除；2、未删除
     */
    private Integer isdel;

    /**
     * 大型活动类型
     */
    private String culactcontent;

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
     * 获取大型活动名
     *
     * @return culactname - 大型活动名
     */
    public String getCulactname() {
        return culactname;
    }

    /**
     * 设置大型活动名
     *
     * @param culactname 大型活动名
     */
    public void setCulactname(String culactname) {
        this.culactname = culactname;
    }

    /**
     * 获取大型活动封面URL
     *
     * @return culactcover - 大型活动封面URL
     */
    public String getCulactcover() {
        return culactcover;
    }

    /**
     * 设置大型活动封面URL
     *
     * @param culactcover 大型活动封面URL
     */
    public void setCulactcover(String culactcover) {
        this.culactcover = culactcover;
    }

    /**
     * 获取流程状态：0初始，1送审，2已审，3已发
     *
     * @return culactstate - 流程状态：0初始，1送审，2已审，3已发
     */
    public Integer getCulactstate() {
        return culactstate;
    }

    /**
     * 设置流程状态：0初始，1送审，2已审，3已发
     *
     * @param culactstate 流程状态：0初始，1送审，2已审，3已发
     */
    public void setCulactstate(Integer culactstate) {
        this.culactstate = culactstate;
    }

    /**
     * 获取创建时间
     *
     * @return culactcreattime - 创建时间
     */
    public Date getCulactcreattime() {
        return culactcreattime;
    }

    /**
     * 设置创建时间
     *
     * @param culactcreattime 创建时间
     */
    public void setCulactcreattime(Date culactcreattime) {
        this.culactcreattime = culactcreattime;
    }

    /**
     * 获取创建人
     *
     * @return culactuser - 创建人
     */
    public String getCulactuser() {
        return culactuser;
    }

    /**
     * 设置创建人
     *
     * @param culactuser 创建人
     */
    public void setCulactuser(String culactuser) {
        this.culactuser = culactuser;
    }

    /**
     * 获取删除状态：1、已删除；2、未删除
     *
     * @return isdel - 删除状态：1、已删除；2、未删除
     */
    public Integer getIsdel() {
        return isdel;
    }

    /**
     * 设置删除状态：1、已删除；2、未删除
     *
     * @param isdel 删除状态：1、已删除；2、未删除
     */
    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    /**
     * 获取大型活动类型
     *
     * @return culactcontent - 大型活动类型
     */
    public String getCulactcontent() {
        return culactcontent;
    }

    /**
     * 设置大型活动类型
     *
     * @param culactcontent 大型活动类型
     */
    public void setCulactcontent(String culactcontent) {
        this.culactcontent = culactcontent;
    }
}