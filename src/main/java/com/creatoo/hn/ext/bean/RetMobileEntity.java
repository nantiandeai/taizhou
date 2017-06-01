package com.creatoo.hn.ext.bean;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**移动端返回实体
 * Created by caiyong on 2017/5/10.
 */
public class RetMobileEntity implements Serializable{

    /**
     * 返回码
     */
    private Integer code;

    /**
     * 返回信息
     */
    private String errormsg;

    /**
     * 返回数据实体
     */
    private Object data;

    /**
     * 分页信息类
     */
    public static class Pager implements Serializable{
        private Integer index;
        private Integer count;
        private Integer size;
        private Integer total;

        public void setCount(Integer count) {
            this.count = count;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public Integer getCount() {
            return count;
        }

        public Integer getIndex() {
            return index;
        }

        public Integer getSize() {
            return size;
        }

        public Integer getTotal() {
            return total;
        }
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setData(Object data) {
        if(data instanceof List){
            if(null == this.data){
                this.data = new HashMap();
            }
            ((Map)this.data).put("list",data);
        }else {
            this.data = data;
        }
    }

    public void pushExData(String key,String value){
        if(null != data && data instanceof Map){
            ((Map)data).put(key,value);
        }
    }

    public void setMsg(String msg) {
        this.errormsg = msg;
    }

    public void setPager(Pager pager) {
        if(null == this.data){
            this.data = new HashMap();
        }
        ((Map)this.data).put("pager",pager);
    }

    public Integer getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }

    public Pager getPager() {
        if(this.data instanceof  Map){
            return (Pager)((Map)this.data).get("pager");
        }
        return null;
    }

    public String getMsg() {
        return errormsg;
    }

    public static void main(String[] args){
        RetMobileEntity retMobileEntity = new RetMobileEntity();
        List list = new ArrayList();
        list.add("mabi");
        list.add("mabi");
        list.add("mabi");
        list.add("mabi");
        list.add("mabi");
        list.add("mabi");
        list.add("mabi");
        list.add("mabi");
        Pager pager = new RetMobileEntity.Pager();
        pager.setCount(8);
        pager.setIndex(1);
        pager.setSize(8);
        pager.setTotal(26);
        retMobileEntity.setCode(0);
        retMobileEntity.setMsg("OK");
        retMobileEntity.setData(list);
        retMobileEntity.setPager(pager);
        System.out.println(JSON.toJSONString(retMobileEntity));
    }
}

