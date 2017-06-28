package com.creatoo.hn.actions.api.showstyle;

import com.creatoo.hn.ext.bean.RetMobileEntity;
import com.creatoo.hn.model.WhgCultHeritage;
import com.creatoo.hn.model.WhgSpecilResource;
import com.creatoo.hn.model.WhgYwiLbt;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiLbtService;
import com.creatoo.hn.services.api.showstyle.APIShowStyleService;
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
 * 秀我风采接口
 * Created by luzhihuai on 2017/6/26.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/showStyle")
public class APIShowStyleAction {
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
	private APIShowStyleService apiShowStyleService;

	@Autowired
	private WhgYunweiLbtService whgYunweiLbtService;

	/**
	 * index 当前页
	 * size 每页条数
	 * 查询秀我风采列表
	 *
	 * @param request .
	 * @return 对象
	 */
	@CrossOrigin
	@RequestMapping(value = "/showStyleList", method = RequestMethod.POST)
	public RetMobileEntity showStyleList(HttpServletRequest request, Integer index, Integer size) {
		RetMobileEntity retMobileEntity = new RetMobileEntity();
		String type = request.getParameter("type");//文化遗产类型
		if (index == null) {
			index = 1;
		}
		if (size == null) {
			size = 12;
		}
		PageInfo pageInfo;
		try {
			pageInfo = this.apiShowStyleService.t_srchList4p(type, index, size);
			if (null == pageInfo) {
				retMobileEntity.setCode(1);
				retMobileEntity.setMsg("获取秀我风采列表失败");
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
	 * 查询秀我风采轮播图
	 * @return 对象
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@CrossOrigin
	@RequestMapping(value = "/showStylePic", method = RequestMethod.POST)
	public RetMobileEntity  showStylePic(){
		RetMobileEntity  res = new RetMobileEntity ();
		Map<String,Object> param = new HashMap<>();
		try {
			WhgYwiLbt whgYwiLbt = new WhgYwiLbt();
			whgYwiLbt.setType("13");
			whgYwiLbt.setState(1);
			List<WhgYwiLbt> stylelbt = this.whgYunweiLbtService.t_srchList(whgYwiLbt);
			param.put("stylelbt", stylelbt);
			res.setCode(0);
			res.setData(param);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 查询秀我风采详情
	 * @param id 文化遗产id
	 * @return 对象
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@CrossOrigin
	@RequestMapping(value = "/showStyleDetail", method = RequestMethod.POST)
	public RetMobileEntity  showStyleDetail(String id){
		RetMobileEntity  res = new RetMobileEntity ();
		Map<String,Object> param = new HashMap<>();
		try {
			if(id ==null || "".equals(id)){
				res.setCode(101);
				res.setMsg("Id不允许为空");//Id不允许为空
			}else{
				WhgSpecilResource resource = this.apiShowStyleService.t_srchOne(id);
				List<WhgCultHeritage> styletj = this.apiShowStyleService.selectStyleRecommend(null,id);
				param.put("resource", resource);
				param.put("styletj", styletj);
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
