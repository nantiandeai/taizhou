package com.creatoo.hn.mapper;

import com.creatoo.hn.model.WhScanCollection;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface WhScanCollectionMapper extends Mapper<WhScanCollection> {
    /**
     * 查询扫描结果集
     * @param whScanCollection
     * @return
     */
    List<WhScanCollection> getWhScanCollectionByParam(@Param("whScanCollection") WhScanCollection whScanCollection);

    /**
     * 获取新的违规活动订单
     * @return
     */
    List<Map> getActIllegalOrder(@Param("violationtype") Integer violationtype);

    List<Map> getVenIllegalOrder();

    /**
     * 增加违规订单
     * @param myParam
     */
    void addIllegalOrder(@Param("myParam") Map myParam);

}