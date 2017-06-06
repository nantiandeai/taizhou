package com.creatoo.hn.mapper.admin;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**数字展馆DAO
 * Created by caiyong on 2017/4/21.
 */
@SuppressWarnings("all")
public interface CrtWhgPavilionMapper {

    /**
     * 根据状态获取展馆
     * @param zxstate
     * @return
     */
    List<Map> getPavilionListByState(@Param("mystate") Integer zxstate);

    /**
     * 根据状态获取展馆
     * @param zxstate
     * @return
     */
    List<Map> getPavilionListByStateEx(@Param("mystate") Integer zxstate,@Param("relList") List relList);

    /**
     * 修改展馆状态
     * @param id
     * @param state
     */
    void updatePavilionState(@Param("myId") String id,@Param("mySate") Integer state);

    /**
     * 修改展馆删除状态
     * @param id
     * @param state
     */
    void updatePavilionIsDel(@Param("myId") String id,@Param("mySate") Integer state);

    /**
     * 根据状态获取藏品
     * @param zxstate
     * @return
     */
    List<Map> getAntiquesListByState(@Param("mystate") Integer zxstate);

    /**
     * 添加一个藏品
     * @param map
     */
    void addOneAntiques(@Param("map") Map map);

    /**
     * 修改一个藏品
     * @param map
     */
    void updateOneAntiques(@Param("map") Map map);

    /**
     * 变更藏品状态
     * @param id
     * @param myState
     */
    void updateOneAntiquesState(@Param("id") String id,@Param("myState") Integer myState);

    /**
     * 变更藏品删除状态
     * @param id
     * @param myState
     */
    void updateOneAntiquesDelState(@Param("id") String id,@Param("myState") Integer myState);

    /**
     * 获取所有展馆
     * @return
     */
    List<Map> getAllPavilion();

    /**
     * 查询藏品
     * @param id
     * @return
     */
    List<Map> getExhbitById(@Param("id") String id);
}
