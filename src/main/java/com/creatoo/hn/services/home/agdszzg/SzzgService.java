package com.creatoo.hn.services.home.agdszzg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.creatoo.hn.mapper.ArtMapper;
import com.creatoo.hn.mapper.WhArtExhibitionMapper;
import com.creatoo.hn.mapper.WhArtMapper;
import com.creatoo.hn.mapper.WhCfgAdvMapper;
import com.creatoo.hn.mapper.WhKeyMapper;
import com.creatoo.hn.model.WhArt;
import com.creatoo.hn.model.WhArtExhibition;
import com.creatoo.hn.model.WhCfgAdv;
import com.creatoo.hn.model.WhCfgList;
import com.creatoo.hn.model.WhKey;
import com.creatoo.hn.model.WhUserTroupe;
import com.creatoo.hn.model.WhZxColinfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class SzzgService {
	
	@Autowired
	private WhArtExhibitionMapper whArtExhibitionMapper;
	@Autowired
	public ArtMapper artMapper;
	@Autowired
	public WhArtMapper whArtMapper;
	@Autowired
	private WhCfgAdvMapper whCfgAdvMapper;
	 
	
	
	public Map<String, Object> srchjpwhzlist(Map<String, Object> param,String arttypid) {
		//分页信息
		int page = Integer.parseInt((String)param.get("page"));
		int rows = Integer.parseInt((String)param.get("rows"));
		
		//带条件的分页查询
		PageHelper.startPage(page, rows);
		Example example = new Example(WhArt.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("arttyp", "2016101400000036");
		if (arttypid !=null) {
			criteria.andEqualTo("arttypid", arttypid);
		}
	
		criteria.andEqualTo("artstate", "3");
		if(param.containsKey("title") && param.get("title") != null){
			//criteria.andLike("arttitle", "%"+param.get("title")+"%");
			criteria.andCondition("(arttitle like '%"+param.get("title")+"%' or artshorttitle like '%"+param.get("title")+"%')");
		}
		List<WhArt> list = this.whArtMapper.selectByExample(example);
		
		// 取分页信息
        PageInfo<WhArt> pageInfo = new PageInfo<WhArt>(list);
       
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("total", pageInfo.getTotal());
        rtnMap.put("rows", pageInfo.getList());
		return rtnMap;
	}

	/**
	 * 查询广告
	 * @param type
	 * @return
	 */
	public List<WhCfgAdv> selectadv(String type) {
		Example example = new Example(WhCfgAdv.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("cfgadvpagetype",type);
		criteria.andEqualTo("cfgadvstate",1);
		example.setOrderByClause("cfgadvidx");
		return this.whCfgAdvMapper.selectByExample(example);
	}
}
