package com.creatoo.hn.mapper.home;

import com.creatoo.hn.model.WhgActTime;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * Created by rbg on 2017/4/5.
 */
public interface CrtWhhdActTimeMapper extends Mapper<WhgActTime>{

    public List<WhgActTime> findPlayDate4actId(Map params);

    public List<String> getActDate(@Param("actId")String actId);

    public List<Map> getActTimes(@Param("actId") String actId,@Param("actDate") String actDate);
}
