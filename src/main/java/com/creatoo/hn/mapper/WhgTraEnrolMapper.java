package com.creatoo.hn.mapper;

import com.creatoo.hn.model.WhgTraEnrol;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface WhgTraEnrolMapper extends Mapper<WhgTraEnrol> {

    /**
     * 根据用户Id获取用户报名订单/历史订单
     * @param params
     * @return
     */
    public List<Map<String,Object>> getTraEnrolListByUserId(Map<String,Object> params);
}