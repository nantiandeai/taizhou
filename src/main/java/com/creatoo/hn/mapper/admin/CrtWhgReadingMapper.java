package com.creatoo.hn.mapper.admin;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**数字阅读DAO
 * Created by caiyong on 2017/4/20.
 */
@SuppressWarnings("all")
public interface CrtWhgReadingMapper {
    /**
     * 根据状态获取资讯
     * @param zxstate
     * @return
     */
    List<Map> getInfoListByState(@Param("mystate") Integer zxstate);

    /**
     * 根据状态获取资讯
     * @param zxstate
     * @return
     */
    List<Map> getInfoListByStateEx(@Param("mystate") Integer zxstate,@Param("relList")List relList);
    /**
     * 添加一个数字阅读的资讯
     * @param param
     */
    void addOneReadingZx(@Param("param") Map param);

    List<Map> getActListForReading();
}
