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

    /**
     * 获取违规场馆订单
     * @return
     */
    List<Map> getVenIllegalOrder();

    /**
     * 增加违规订单
     * @param myParam
     */
    void addIllegalOrder(@Param("myParam") Map myParam);

    /**
     * 统计所有的不良记录
     * @param relType
     * @param violationType
     * @return
     */
    List<Map> getWhScanCollectionStatisticsResult(@Param("reltype") Integer relType,@Param("violationtype") Integer violationType);

    /**
     * 获取判定规则
     * @return
     */
    List<Map> getRule();

    /**
     * 修改黑名单状态
     * @param id
     * @param state
     */
    void updateBlackListState(@Param("id") String id,@Param("state") Integer state);

    /**
     * 修改不良记录状态
     * @param userId
     * @param relType
     * @param violationType
     */
    void updateWhScanCollectionState(@Param("userid") String userId,@Param("reltype") Integer relType,@Param("violationtype") Integer violationType,@Param("recordstate") Integer recordState);

}