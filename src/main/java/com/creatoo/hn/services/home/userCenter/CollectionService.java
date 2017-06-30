package com.creatoo.hn.services.home.userCenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.mapper.*;
import com.creatoo.hn.model.*;
import com.creatoo.hn.utils.ReqParamsUtil;
import com.creatoo.hn.utils.WhConstance;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人用户中心--收藏业务类
 * 
 * @author dzl
 *
 */
@SuppressWarnings("ALL")
@Service
public class CollectionService {

	private static Logger logger = Logger.getLogger(CollectionService.class);

	@Autowired
	private WhCollectionMapper collectionMapper;

	@Autowired
	private WhgActActivityMapper whgActActivityMapper;

	@Autowired
	private WhgVenMapper whgVenMapper;

	@Autowired
	private WhgVenRoomMapper whgVenRoomMapper;

	@Autowired
	private WhgTraMapper whgTraMapper;

	/**
	 * 我的活动收藏查询
	 * 
	 * @param cmuid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Object SelectMyActColle(String cmuid)throws Exception {
		List<HashMap> myAct = this.collectionMapper.selectMyActColle(cmuid);
		return myAct;
	}
    
	/**
	 * 我的培训收藏查询
	 * @param cmuid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Object SelectMyTraitmColle(String cmuid)throws Exception {
		List<HashMap> myTraitm = this.collectionMapper.selectMyTraitmColle(cmuid);
		return myTraitm;
	}
	
	/**
	 * 添加收藏信息
	 * @param whcolle
	 * @return
	 */
   public int addMyColle(WhCollection whcolle)throws Exception{
     return this.collectionMapper.insert(whcolle);
   }
	
   /**
	 * 添加点赞信息
	 * @param whcolle
	 * @return
	 */
  public Object addGood(WhCollection whcolle)throws Exception{
    return this.collectionMapper.insert(whcolle);
  }
	/**
	 * 删除我的收藏
	 * @param cmid
	 * @return
	 */
	public Object removeCollection(String cmid)throws Exception{
		return this.collectionMapper.deleteByPrimaryKey(cmid);
	}
	
	/**
	 * 删除用户收藏
	 * @param reftyp 收藏关联类型
	 * @param refid	 收藏关联id	
	 * @param uid 用户id	
	 * @return
	 */
	public int removeCommColle(String reftyp, String refid, String uid)throws Exception{
//		Example example = new Example(WhCollection.class);
//		Criteria c = example.createCriteria();
//		c.andEqualTo("cmopttyp", "0"); //0-收藏,1-浏览,2-推荐,3-置顶
//		c.andEqualTo("cmuid" , uid);	//用户id
//
//		if(reftyp != null && !"".equals(reftyp)){
//			c.andEqualTo("cmreftyp",reftyp);
//		}
//		if(refid != null && !"".equals(refid)){
//			c.andEqualTo("cmrefid", refid);
//		}
		WhCollection record = new WhCollection();
		
		record.setCmreftyp(reftyp);
		record.setCmrefid(refid);
		record.setCmuid(uid);
		record.setCmopttyp("0");
		return this.collectionMapper.delete(record);
	}
	

	/**
	 * 判断用户是否收藏
	 * @param reftyp 收藏对象类型
	 * @param refid 收藏对象标识
	 * @return true-是, false-否
	 */
	public boolean isColle(String uid, String reftyp, String refid)throws Exception {
		boolean is = false;
		
		Example example = new Example(WhCollection.class);
		Criteria c = example.createCriteria();
		c.andEqualTo("cmopttyp", "0");//0-收藏,1-浏览,2-推荐,3-置顶
		c.andEqualTo("cmuid", uid);//用户id
		
		if(reftyp != null && !"".equals(reftyp)){
			c.andEqualTo("cmreftyp", reftyp);
		}
		
		if(refid != null &&  !"".equals(refid)){
			c.andEqualTo("cmrefid", refid);
		}
         is = this.collectionMapper.selectCountByExample(example) > 0;
		return is;
	}
    
	
	/**
	 * 判断用户是否点赞
	 * @param reftyp 收藏关联类型
	 * @param refid 收藏关联id
	 * @return
	 */
	public boolean IsGood(String dzID,String reftyp, String refid)throws Exception {
		boolean is = false;
		Example example = new Example(WhCollection.class);
		Criteria c = example.createCriteria();
		c.andEqualTo("cmopttyp", "2"); //0-收藏,1-浏览,2-推荐,3-置顶
		c.andEqualTo("cmuid",dzID);	//用户点赞地址	dzID:已登录时存入用户id（uid） 未登录时：存入ip地址(dzIP)
		
		if(reftyp != null && !"".equals(reftyp)){
			c.andEqualTo("cmreftyp",reftyp);
		}
		if(refid != null && !"".equals(refid)){
			c.andEqualTo("cmrefid", refid);
		}
		
        is = this.collectionMapper.selectCountByExample(example) > 0;
		return is;
	}
	
	/**
	 * 获取点赞次数
	 * @param reftyp
	 * @param refid
	 * @return
	 * @throws Exception
	 */
	public int dianZhanShu(String reftyp, String refid)throws Exception{
		Example example = new Example(WhCollection.class);
		Criteria c = example.createCriteria();
		c.andEqualTo("cmopttyp", "2"); //0-收藏,1-浏览,2-推荐,3-置顶
		if(reftyp != null && !"".equals(reftyp)){
			c.andEqualTo("cmreftyp",reftyp);
		}
		if(refid != null && !"".equals(refid)){
			c.andEqualTo("cmrefid", refid);
		}
		return this.collectionMapper.selectCountByExample(example);
	}
	
	/**
	 * 获取收藏次数
	 * @param reftyp
	 * @param refid
	 * @return
	 * @throws Exception
	 */
	public int shouCanShu(String reftyp, String refid)throws Exception{
		Example example = new Example(WhCollection.class);
		Criteria c = example.createCriteria();
		c.andEqualTo("cmopttyp", "0"); //0-收藏,1-浏览,2-推荐,3-置顶
		if(reftyp != null && !"".equals(reftyp)){
			c.andEqualTo("cmreftyp",reftyp);
		}
		if(refid != null && !"".equals(refid)){
			c.andEqualTo("cmrefid", refid);
		}
		return this.collectionMapper.selectCountByExample(example);
	}
	
	
	
	public Object loadcoll(WebRequest request,HttpSession session)throws Exception{
		//获取请求参数
		Map<String, Object> param = ReqParamsUtil.parseRequest(request);
		
		int page = Integer.parseInt((String)param.get("page"));
		int rows = Integer.parseInt((String)param.get("rows"));
		String cmreftyp = (String)param.get("cmreftyp");

		String userid=(String) session.getAttribute(WhConstance.SESS_USER_ID_KEY);
		Example example=new Example(WhCollection.class);
		Criteria c = example.createCriteria();
		if(userid!=null || "".equals(userid)){
			c.andEqualTo("cmuid",userid);
		}
		c.andEqualTo("cmopttyp","0");
        c.andEqualTo("cmreftyp", cmreftyp);
		example.setOrderByClause("cmdate desc");

        PageHelper.startPage(page, rows);
		List<WhCollection> list=this.collectionMapper.selectByExample(example);

		PageInfo<WhCollection> pinfo = new PageInfo<WhCollection>(list);

		Map<String, Object> res = new HashMap<String, Object>();
		res.put("total", pinfo.getTotal());
		res.put("rows", pinfo.getList());
		return res;		
	}

	/**
	 * 查询收藏数量
	 * @param cmreftyp
	 * @param cmrefid
	 * @return
	 */
	public int getCollectionCount(String cmreftyp, String cmrefid){
		try {
			return collectionMapper.getCollectionCount(cmreftyp,cmrefid);
		}catch (Exception e){
			logger.error(e.toString());
			return 0;
		}
	}

	public JSONObject getTargetInfo(String cmreftyp, String cmrefid){
		try {
			if("4".equals(cmreftyp)){
				//活动
				WhgActActivity whgActActivity = new WhgActActivity();
				whgActActivity.setId(cmrefid);
				whgActActivity = whgActActivityMapper.selectOne(whgActActivity);
				if(null == whgActActivity){
					return null;
				}
				return JSON.parseObject(JSON.toJSONString(whgActActivity));
			}else if("5".equals(cmreftyp)){
				//培训
				WhgTra whgTra = new WhgTra();
				whgTra.setId(cmrefid);
				whgTra = whgTraMapper.selectOne(whgTra);
				if(whgTra == null){
					return null;
				}
				return JSON.parseObject(JSON.toJSONString(whgTra));
			}else if("2".equals(cmreftyp)){
				//场馆
				WhgVen whgVen = new WhgVen();
				whgVen.setId(cmrefid);
				whgVen = whgVenMapper.selectOne(whgVen);
				if(null == whgVen){
					return null;
				}
				return JSON.parseObject(JSON.toJSONString(whgVen));
			}else if("3".equals(cmreftyp)) {
				//活动室收藏
				WhgVenRoom whgRoom = new WhgVenRoom();
				whgRoom.setId(cmrefid);
				whgRoom = whgVenRoomMapper.selectOne(whgRoom);
				if(null == whgRoom){
					return null;
				}
				return JSON.parseObject(JSON.toJSONString(whgRoom));
			}else{
				return null;
			}
		}catch (Exception e){
			logger.error(e.toString());
			return null;
		}

	}
}
