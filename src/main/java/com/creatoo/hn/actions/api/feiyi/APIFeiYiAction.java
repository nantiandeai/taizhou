package com.creatoo.hn.actions.api.feiyi;

import com.creatoo.hn.ext.bean.RetMobileEntity;
import com.creatoo.hn.model.WhgCultHeritage;
import com.creatoo.hn.model.WhgCultTalents;
import com.creatoo.hn.model.WhgHistorical;
import com.creatoo.hn.model.WhgYwiType;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiTypeService;
import com.creatoo.hn.services.api.user.APIFeiYiService;
import com.creatoo.hn.services.comm.CommService;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数字非遗接口
 * Created by luzhihuai on 2017/6/26.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/feiyi")
public class APIFeiYiAction {
	/**
	 * 日志控制器
	 */
	Logger log = Logger.getLogger(this.getClass().getName());

	/**
	 * 公用服务类
	 */
	@Autowired
	public CommService commservice;

	@Autowired
	private APIFeiYiService apiFeiYiService;

	@Autowired
	private WhgYunweiTypeService whgYunweiTypeService;

	/**
	 * index 当前页
	 * size 每页条数
	 * 查询文化遗产列表
	 *
	 * @param request .
	 * @return 对象
	 */
	@CrossOrigin
	@RequestMapping(value = "/cultList", method = RequestMethod.POST)
	public RetMobileEntity actList(HttpServletRequest request, Integer index, Integer size) {
		RetMobileEntity retMobileEntity = new RetMobileEntity();
		String type = request.getParameter("type");//文化遗产类型
//		String district = request.getParameter("district");//区域
		if (index == null) {
			index = 1;
		}
		if (size == null) {
			size = 12;
		}
		PageInfo pageInfo;
		try {
			pageInfo = this.apiFeiYiService.t_srchList4p(type, index, size);
			if (null == pageInfo) {
				retMobileEntity.setCode(1);
				retMobileEntity.setMsg("获取文化遗产列表失败");
				return retMobileEntity;
			}
			retMobileEntity.setCode(0);
			retMobileEntity.setData(pageInfo.getList());
			RetMobileEntity.Pager pager = new RetMobileEntity.Pager();
			pager.setIndex(pageInfo.getPageNum());
			pager.setTotal(Long.valueOf(pageInfo.getTotal()).intValue());
			pager.setSize(pageInfo.getSize());
			pager.setCount(pageInfo.getPages());
			retMobileEntity.setPager(pager);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return retMobileEntity;
	}


	/**
	 * 查询文化遗产详情
	 * @param id 文化遗产id
	 * @return 对象
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@CrossOrigin
	@RequestMapping(value = "/cultDetail", method = RequestMethod.POST)
	public RetMobileEntity  cultDetail(String id){
		RetMobileEntity  res = new RetMobileEntity ();
		Map<String,Object> param = new HashMap<>();
		try {
			if(id ==null || "".equals(id)){
				res.setCode(101);
				res.setMsg("Id不允许为空");//Id不允许为空
			}else{
				WhgCultHeritage cultHeritage = this.apiFeiYiService.t_srchOne(id);
				List<WhgCultHeritage> culttj = this.apiFeiYiService.selectCultRecommend(null,id);
				param.put("cultdetail", cultHeritage);
				param.put("culttj", culttj);
				res.setCode(0);
				res.setData(param);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return res;
	}


	/**
	 * 非遗公共类型查询接口
	 * @return res
	 */
	@CrossOrigin
	@RequestMapping(value = "/selectType", method = RequestMethod.POST)
	public RetMobileEntity selectType(){
		RetMobileEntity res = new RetMobileEntity();
		Map<String, Object> rest = new HashMap<>();
		//文化遗产分类
		List<WhgYwiType> cultTheritage = whgYunweiTypeService.findWhgYwiTypeList("16");
		//重点文物分类
		List<WhgYwiType> historical = whgYunweiTypeService.findWhgYwiTypeList("17");
		//文化人才
		List<WhgYwiType> talents = whgYunweiTypeService.findWhgYwiTypeList("18");
		//区域
//		List<WhgYwiType> areaList = whgYunweiTypeService.findWhgYwiTypeList("6");
		rest.put("cultTheritage", cultTheritage);
//		rest.put("areaList", areaList);
		rest.put("historical", historical);
		rest.put("talents", talents);
		res.setCode(0);
		res.setData(rest);
		return res;
	}

	/**
	 * index 当前页
	 * size 每页条数
	 * 查询重点文物列表
	 *
	 * @param request .
	 * @return 对象
	 */
	@CrossOrigin
	@RequestMapping(value = "/historicalList", method = RequestMethod.POST)
	public RetMobileEntity historicalList(HttpServletRequest request, Integer index, Integer size) {
		RetMobileEntity retMobileEntity = new RetMobileEntity();
		String type = request.getParameter("type");//重点文物类型
		if (index == null) {
			index = 1;
		}
		if (size == null) {
			size = 12;
		}
		PageInfo pageInfo;
		try {
			pageInfo = this.apiFeiYiService.selectHistorical(type, index, size);
			if (null == pageInfo) {
				retMobileEntity.setCode(1);
				retMobileEntity.setMsg("获取重点文物列表失败");
				return retMobileEntity;
			}
			retMobileEntity.setCode(0);
			retMobileEntity.setData(pageInfo.getList());
			RetMobileEntity.Pager pager = new RetMobileEntity.Pager();
			pager.setIndex(pageInfo.getPageNum());
			pager.setTotal(Long.valueOf(pageInfo.getTotal()).intValue());
			pager.setSize(pageInfo.getSize());
			pager.setCount(pageInfo.getPages());
			retMobileEntity.setPager(pager);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return retMobileEntity;
	}

	/**
	 * 查询重点文物详情
	 * @param id 文化遗产id
	 * @return 对象
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@CrossOrigin
	@RequestMapping(value = "/historicalDetail", method = RequestMethod.POST)
	public RetMobileEntity  historicalDetail(String id){
		RetMobileEntity  res = new RetMobileEntity ();
		Map<String,Object> param = new HashMap<>();
		try {
			if(id ==null || "".equals(id)){
				res.setCode(101);
				res.setMsg("Id不允许为空");//Id不允许为空
			}else{
				WhgHistorical historical = this.apiFeiYiService.searchHistorical(id);
				List<WhgHistorical> historicaltj = this.apiFeiYiService.selectHisRecommend(null,id);
				param.put("historical", historical);
				param.put("historicaltj", historicaltj);
				res.setCode(0);
				res.setData(param);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * index 当前页
	 * size 每页条数
	 * 查询文化人才列表
	 *
	 * @param request .
	 * @return 对象
	 */
	@CrossOrigin
	@RequestMapping(value = "/talentsList", method = RequestMethod.POST)
	public RetMobileEntity talentsList(HttpServletRequest request, Integer index, Integer size) {
		RetMobileEntity retMobileEntity = new RetMobileEntity();
		String type = request.getParameter("type");//重点文物类型
		if (index == null) {
			index = 1;
		}
		if (size == null) {
			size = 12;
		}
		PageInfo pageInfo;
		try {
			pageInfo = this.apiFeiYiService.selectTalents(type, index, size);
			if (null == pageInfo) {
				retMobileEntity.setCode(1);
				retMobileEntity.setMsg("获取文化人才列表失败");
				return retMobileEntity;
			}
			retMobileEntity.setCode(0);
			retMobileEntity.setData(pageInfo.getList());
			RetMobileEntity.Pager pager = new RetMobileEntity.Pager();
			pager.setIndex(pageInfo.getPageNum());
			pager.setTotal(Long.valueOf(pageInfo.getTotal()).intValue());
			pager.setSize(pageInfo.getSize());
			pager.setCount(pageInfo.getPages());
			retMobileEntity.setPager(pager);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return retMobileEntity;
	}

	/**
	 * 查询文化人才详情
	 * @param id 文化遗产id
	 * @return 对象
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@CrossOrigin
	@RequestMapping(value = "/talentsDetail", method = RequestMethod.POST)
	public RetMobileEntity  talentsDetail(String id){
		RetMobileEntity  res = new RetMobileEntity ();
		Map<String,Object> param = new HashMap<>();
		try {
			if(id ==null || "".equals(id)){
				res.setCode(101);
				res.setMsg("Id不允许为空");//Id不允许为空
			}else{
				WhgCultTalents talents = this.apiFeiYiService.searchTalents(id);
				List<WhgCultTalents> talentstj = this.apiFeiYiService.selectTalRecommend(null,id);
				param.put("talents", talents);
				param.put("talentstj", talentstj);
				res.setCode(0);
				res.setData(param);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return res;
	}
}
