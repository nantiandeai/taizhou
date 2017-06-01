package com.creatoo.hn.utils;

/**
 * Created by chenf on 2017/5/5.
 */
public enum SmsTmpEnum {
    BINDING_PHONE("BINDING_POHONE",1), //用户绑定手机号
    UN_BINDING_PHONE("UN_BINDING_PHONE",2), //用户解绑手机号
    VALIDATE_ADMIN("VALIDATE_ADMIN",1),//验票管理员授权
    USER_REGISTER("USER_REGISTER",1);//用户注册网站基本信息


     SmsTmpEnum(String code,Integer id){
        this.code=code;
        this.id=id;
    }
    private String code;
    private Integer id;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public static  String getTempCode(Integer id){
        for (SmsTmpEnum sms :SmsTmpEnum.values()){
                if (sms.getId().equals(id)){
                    return  sms.getCode();
                }
        }
        return  null;
    }
}
