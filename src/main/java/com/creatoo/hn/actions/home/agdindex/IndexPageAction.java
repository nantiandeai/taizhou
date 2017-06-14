package com.creatoo.hn.actions.home.agdindex;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.ext.emun.EnumLBTClazz;
import com.creatoo.hn.model.WhUser;
import com.creatoo.hn.model.WhgTra;
import com.creatoo.hn.services.comm.CommPropertiesService;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.services.home.agdindex.IndexPageService;
import com.creatoo.hn.services.home.userCenter.UserCenterService;
import com.creatoo.hn.utils.WhConstance;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ModelAndViewMethodReturnValueHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PC首页处理器
 * @author wangxl
 * @version 1.0.201703
 */
@SuppressWarnings("all")
@RestController
public class IndexPageAction {
	/**
	 * 日志
	 */
	Logger log = Logger.getLogger(this.getClass());

	/**
	 * 首页数据服务
	 */
	@Autowired
	private IndexPageService pageService;

	/**
	 * 用户信息服务
	 */
	@Autowired
	private UserCenterService userCenterService;

	/**
	 * 公共服务
	 */
	@Autowired
	private CommService commservice;

	/**
	 * 配置文件读取BEAN
	 */
	@Autowired
	private CommPropertiesService commPropertiesService;

	/**
	 * 跳转后台登录页
	 * @return
	 */
	@RequestMapping("consoleLogin")
	public ModelAndView consoleLogin(){
		ModelAndView view = new ModelAndView("admin/login");
		return view;
	}

	/**
	 * 首页登录验证
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return
	 */
	@RequestMapping("/home/validLogin")
	public ResponseBean validLogin(HttpServletRequest request, HttpServletResponse response){
		ResponseBean res = new ResponseBean();

		try{
			//返回数据
			Map<String, String> map = new HashMap<String, String>();

			//获取用户会话id
			WhUser whuser = (WhUser) request.getSession().getAttribute(WhConstance.SESS_USER_KEY);
			if(whuser != null && whuser.getId() != null){//已登录
				map.put("login", "true");
				int msgCount = this.userCenterService.selectMsgHeader(whuser.getId());
				map.put("msgCount", msgCount+"");

				String username = whuser.getNickname();
				if(username == null){
					username = whuser.getPhone();
					if(username != null){
						username = username.substring(0,3)+"*****"+username.substring(username.length()-3);
					}
				}
				if(username == null){
					username = whuser.getEmail();
					if(username != null){
						username = username.substring(0,username.indexOf("@"));
					}
				}
				if(username == null){
					username = whuser.getId();
				}
				map.put("username", whuser.getNickname());
			}else{//未登录
				map.put("login", "false");
				map.put("msgCount", "0");
			}

			//设置返回数据
			res.setData(map);
		} catch (Exception e){
			log.error(e.getMessage(), e);
			res.setSuccess(ResponseBean.FAIL);
			res.setErrormsg(e.getMessage());
		}

		return res;
	}

	/**
	 * 首页取数
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/home")
	public ModelAndView firstPage(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("/indexTz");
		try {
			//取轮播图
			model.addObject("lbtList", this.pageService.findLBT(EnumLBTClazz.LBT_PC.getValue(), 5));

			//取公告
			model.addObject("ggList", this.pageService.findZX("2016111900000014", 6));

			//取工作动态
			model.addObject("dtList", this.pageService.findZX("2016111900000015", 6));

			//取基层直击
			model.addObject("jcList", this.pageService.findZX("2016111900000016", 6));

			//取热点聚集
			model.addObject("rdList", this.pageService.findZX("2016111900000017", 6));

			//首页广告
			model.addObject("advList", this.pageService.findLBT(EnumLBTClazz.LBT_PC_ADV.getValue(), 5));

			//取文化活动

			model.addObject("actList", JSON.toJSONString(this.pageService.findActivity(6)));

			//取培训驿站
			List<WhgTra> traList = this.pageService.findTrain(4);
			model.addObject("traList", traList);
			model.addObject("traMap", this.pageService.findTrainCourse(traList));

			//取文化馆联盟
			model.addObject("cultList", this.pageService.findCult(30));

			//取场馆数据
			model.addObject("venList",JSON.toJSONString(this.pageService.selectVenueForIndexPage()));

			//热门资源
			String value = commPropertiesService.getPopularreSources();
			String page = "0";
			String size = "5";
			value = org.apache.commons.lang.StringUtils.replace(value,"PAGE",page);
			value = org.apache.commons.lang.StringUtils.replace(value,"SIZE",size);
			model.addObject("popResource",value);
			model.addObject("popSourceDoc",commPropertiesService.getPopSourceDocPre());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return model;
	}



	/** 首页广告图
	 * @return
	 */
	@RequestMapping("/idpage/banner")
	@ResponseBody
	public Object getBannerList(){
		try {
			return pageService.selectBannerList(null);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ArrayList<Object>();
		}
	}

	/** 首页培训块加载
	 * @return
	 */
	@RequestMapping("/idpage/pxyz")
	@ResponseBody
	public Object getPxyzList(){
		Map<String, Object> resmap = new HashMap<String,Object>();
		try {
			resmap.put("rows", pageService.selectPxyzList(null, 3) );
			resmap.put("type", this.commservice.findWhtyp("0") );
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return resmap;
	}


	/** 首页活动块加载
	 * @return
	 */
	@RequestMapping("/idpage/whhd")
	@ResponseBody
	public Object getWhhdList(){
		Map<String, Object> resmap = new HashMap<String,Object>();
		try {
			resmap.put("type", this.commservice.findWhtyp("1"));
			resmap.put("rows", pageService.selectWhhdList(null));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return resmap;
	}

	/** 首页非遗块加载
	 * @return
	 */
	@RequestMapping("/idpage/feiyi")
	@ResponseBody
	public Object getFeiyiList(){
		Map<String, Object> resmap = new HashMap<String,Object>();
		try {
			resmap.put("rows", pageService.selectFeiyiList(null));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return resmap;
	}

	/** 首页公告加载
	 * @return
	 */
	@RequestMapping("/idpage/gonggao")
	@ResponseBody
	public Object getGonggao(){
		Map<String, Object> resmap = new HashMap<String,Object>();
		try {
			resmap.put("rows", pageService.selectZxinfo("2016111900000014",4));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return resmap;
	}

	/** 工作状态加载
	 * @return
	 */
	@RequestMapping("/idpage/gongz")
	@ResponseBody
	public Object getGongz(){
		Map<String, Object> resmap = new HashMap<String,Object>();
		try {
			resmap.put("rows", pageService.selectZxinfo("2016111900000015",4));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return resmap;
	}

	/** 首页小广告加载
	 * @return
	 */
	@RequestMapping("/idpage/getSmalladv")
	@ResponseBody
	public Object getSmalladv(){
		Map<String, Object> resmap = new HashMap<String,Object>();
		try {
			resmap.put("rows", pageService.selectZxinfo("2016120500000001",6));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return resmap;
	}

	/**
	 * 首页 文化联盟
	 * @return
	 */
	@RequestMapping("/idpage/selectVenue")
	@ResponseBody
	public Object selectVenue(){
		Map<String, Object> resmap = new HashMap<String,Object>();
		try {
			resmap.put("rows", pageService.selectVenue());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return resmap;
	}

	@ResponseBody
	@RequestMapping("/home/popSource")
	public ResponseBean popSource(HttpServletRequest request){
		ResponseBean responseBean = new ResponseBean();
		String value = commPropertiesService.getPopularreSources();
		String page = "0";
		String size = "5";
		value = org.apache.commons.lang.StringUtils.replace(value,"PAGE",page);
		value = org.apache.commons.lang.StringUtils.replace(value,"SIZE",size);
		try {
			Request myRequest = new Request.Builder().url(value).build();
			OkHttpClient client = new OkHttpClient();
			Response response = client.newCall(myRequest).execute();
			if (response.isSuccessful()) {
				String myBody = response.body().string();
				JSONObject jsonObject = JSON.parseObject(myBody);
				responseBean.setCode("0");
				responseBean.setData(jsonObject);
				return responseBean;
			} else {
				responseBean.setCode("101");
				responseBean.setErrormsg("获取热门资源失败");
				return responseBean;
			}
		}catch (Exception e){
			log.error(e.toString());
			responseBean.setCode("101");
			responseBean.setErrormsg("获取热门资源失败");
			return responseBean;
		}
	}
}
