package com.creatoo.hn.mapper;

import com.creatoo.hn.model.WhCollection;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface WhCollectionMapper extends Mapper<WhCollection> {
	/**
	 * 
	 * @return
	 */
	public List<HashMap> selectCollection(String userid);
	/**
	 * 
	 */
	public List<HashMap> selectTraitm(String userid);
	
	/**
	 * 我的活动收藏--查询
	 * @param cmuid
	 * @return
	 */
	public List<HashMap> selectMyActColle(String cmuid);
	
	/**
	 * 我的培训收藏--查询
	 * @param cmuid
	 * @return
	 */
	public List<HashMap> selectMyTraitmColle(String cmuid);

	/**
	 * 查询点赞列表
	 * @param param
	 * @return
	 */
	public List<Map> selectCollectionWithUser(Map<String,Object> param);

	/**
	 * 获取收藏数量
	 * @param cmreftyp
	 * @param cmrefid
	 * @return
	 */
	public Integer getCollectionCount(@Param("cmreftyp") String cmreftyp,@Param("cmrefid") String cmrefid);
}