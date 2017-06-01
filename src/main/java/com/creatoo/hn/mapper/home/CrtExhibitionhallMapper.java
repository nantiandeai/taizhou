package com.creatoo.hn.mapper.home;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**展馆DAO
 * Created by caiyong on 2017/4/28.
 */
@SuppressWarnings("all")
public interface CrtExhibitionhallMapper {

    /**
     * 获取360大图
     * @return
     */
    List<Map> get360Photo();

    /**
     * 获取展馆数据
     * @return
     */
    List<Map> getExhibitionhallList();

    /**
     * 获取展馆信息
     * @param id
     * @return
     */
    List<Map> getExhibitionhallInfo(@Param("id") String id);

    /**
     * 获取藏品列表
     * @param id
     * @return
     */
    List<Map> getExhibitList(@Param("id") String id);

}
