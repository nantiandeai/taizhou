package com.creatoo.hn.mapper;

import com.creatoo.hn.model.WhUser;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.base.select.SelectOneMapper;

import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public interface WhUserMapper extends Mapper<WhUser>,SelectOneMapper<WhUser> {
	/**
	 * 个人中心消息提醒
	 * @param refuid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> msgAlert(@Param("refuid")String refuid);
	
	/**
	 * 个人中心头部消息提醒
	 * @param refuid
	 * @return
	 */
	public String msgHeader(@Param("refuid")String refuid);

	/**
	 * 获取过期的用户订单
	 * @param userid
	 * @return
	 */
	public List<Map> getUserActOrderTimeOut(@Param("userid")String userid);

	/**
	 * 获取未过期的用户订单
	 * @param userid
	 * @return
	 */
	public List<Map> getUserActOrderNotTimeOut(@Param("userid")String userid);

	
	/**
	 * 获取用户所有订单
	 * @param userid
	 * @returngetUserActOrderAll
	 */
	public List<Map> getUserActOrderAll(@Param("userid")String userid);

    /**
     * 根据条件查询用户信息
     * @param whUser
     * @return
     */
	public List<Map> getUserByCondition(@Param("whUser")WhUser whUser);

    /**
     * 根据条件统计活动数量
     * @param userId
     * @param queryType 1:未取票，2:已取票
     * @return
     */
	public Integer getActCount(@Param("userId") String userId,@Param("queryType") Integer queryType);

    /**
     * 根据条件统计场馆数量
     * @param userId
     * @param queryType 1:未取票，2:已取票
     * @return
     */
    public Integer getVenCount(@Param("userId") String userId,@Param("queryType") Integer queryType);

    /**
     * 获取用户活动订单
     * @param userId
     * @return
     */
    public List<Map> getUserAct(@Param("userId") String userId);

	/**
	 *  获取用户收藏
	 * @param userId
	 * @return
	 */
	public List<Map> getUserCollection(@Param("userId") String userId);
}