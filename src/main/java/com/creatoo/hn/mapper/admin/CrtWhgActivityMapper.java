package com.creatoo.hn.mapper.admin;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by rbg on 2017/3/23.
 */
@SuppressWarnings("all")
public interface CrtWhgActivityMapper {

    /**
     * 活动 后台管理列表 查询
     * @param act
     * @return
     */
    List<Map> srchListActivity(Map act);

    /**
     * 为活动后台获取活动订单数据
     * @return
     * added by caiyong 2017/4/7
     */
    List<Map> getActOrderForBackManager(@Param("activityId") String activityId);
}
