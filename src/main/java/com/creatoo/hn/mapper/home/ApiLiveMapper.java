package com.creatoo.hn.mapper.home;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/7.
 */
public interface ApiLiveMapper {

    List<Map> searchLiveDataByParam(@Param("param")Map param);

}
