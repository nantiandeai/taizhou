package com.creatoo.hn.actions.api.activity;

import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.ext.bean.RetMobileEntity;
import com.creatoo.hn.ext.emun.EnumOrderType;
import com.creatoo.hn.model.*;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.services.home.agdwhhd.WhhdService;
import com.creatoo.hn.utils.WhConstance;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 活动预定接口
 * Created by wangxl on 2017/4/12.
 */
@RestController
@RequestMapping("/api/activity")
public class APIActivityAction {
	/**
	 * 日志控制器
	 */
	Logger log = Logger.getLogger(this.getClass().getName());

	/**
	 * 公用服务类
	 */
	@Autowired
	public CommService commservice;

	/**
	 * 短信公开服务类
	 */
	@Autowired
	private SMSService smsService;

	@Autowired
	private WhhdService  whhdService;



	/**
	 * 检查能否报名
	 * 访问路径 /api/act/check/{actId}/{userId}
	 * @param actId  活动Id
	 * @param userId 用户Id
	 * @return JSON: {
	 * "success" : "1"             //1表示可以报名，其它失败
	 * "errormsg" : "100|104"     //100-培训已失效;  104-未实名认证
	 * }
	 */
	@CrossOrigin
	@RequestMapping("/check/{actId}/{userId}")
	public RetMobileEntity check(@PathVariable("actId")String actId, @PathVariable("userId")String userId){
		RetMobileEntity res = new RetMobileEntity();
		try{
			String validCode = this.whhdService.checkApplyAct(actId, userId);
			if(!"0".equals(validCode)){
				res.setCode(101);
				res.setMsg("培训已失效");
			}
		} catch (Exception e){
			log.error(e.getMessage(), e);
			res.setCode(105);
			res.setMsg("报名失败");
		}
		return res;
	}


	/**
	 * 活动预定验证
	 * @param request
	 * @param actId
	 * @param seatStr
	 * @param eventid
	 * @param seats
	 * @param session
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/checkActPublish", method = RequestMethod.POST)
	public RetMobileEntity checkActPublish(HttpServletRequest request,String actId,String seatStr,String eventid,int seats,HttpSession session){
		RetMobileEntity res = new RetMobileEntity();
		WhUser user = (WhUser) session.getAttribute(WhConstance.SESS_USER_KEY);
		try {
			WhgActActivity whgAct = whhdService.getActDetail(actId);
			if(whgAct.getState() != 6){
				res.setCode(101);
				res.setMsg("该活动已下架！");
			}
			if(seatStr != null && !"".equals(seatStr) ){
				String selectSeat[] = seatStr.split(",");
				WhgActSeat whgActSeat =whhdService.getWhgActTicket4ActId(actId, selectSeat[0]);
				if(whgActSeat.getSeatstatus() != 1){
					res.setCode(102);
					res.setMsg("该座位已被预定！");
				}
			}
			if(whgAct.getSellticket() == 2){
				int seatCount = whgAct.getTicketnum();
				JSONObject seatJson = whhdService.getSeat4ActId(actId,eventid,user.getId());
				int userSeatNum = seatJson.getIntValue("seatSizeUser");//当前用户订票数
				int seatSize = seatJson.getIntValue("seatSize");//当前活动已被预定票数
				if(seatCount - seatSize <= 0 ){
					res.setCode(103);
					res.setMsg("该活动已无可预订座位！");
				}
				if(userSeatNum + seats > whgAct.getSeats() ){
					res.setCode(104);
					res.setMsg("每位用户最多可以预定"+whgAct.getSeats()+"座位！");
				}
				if(seatSize + seats > seatCount ){
					res.setCode(105);
					res.setMsg("该活动已无可预订座位！");
				}
			}


			/*//查询当前活动下，该用户取消次数
			List<WhgActOrder> actOrderList = whhdService.findOrderList4Id(actId, user.getId());
			//查询该用户取消的所有活动次数
			List<WhgActOrder> orderList = whhdService.findOrderList4Id(null, user.getId());
			//如果用户一个活动取消两次或者一个用户统计取消活动订单超过10次则加入黑名单
			if(actOrderList.size()>=2 || orderList.size() >= 10 ){
				WhgUsrBacklist userBack = new WhgUsrBacklist();
				userBack.setUserid(user.getId());
				userBack.setState(1);
				userBack.setUserid(user.getId());
				List<WhgUsrBacklist> userBackList = whhdService.findWhgUsrBack4UserId(userBack);
				if(userBackList.size() < 1){
					userBack.setId(this.commservice.getKey("WhgUsrBacklist"));
					userBack.setState(1);
					userBack.setJointime(new Date());
					userBack.setType(1);
					userBack.setUserid(user.getId());
					userBack.setUserphone(user.getPhone());
					whhdService.addUserBack(userBack);
				}
				res.setCode(106);
				res.setMsg("您已经被系统限制执行操作，如需了解详细情况，请联系管理员！");
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(res.getCode() == null){
			res.setCode(0);
		}
		return res;

	}

	/**
	 * 预定活动
	 * 访问路径 /api/act/bookingsave
	 * @param actId  活动Id
	 * @param eventId  订单信息 可参考whg_act_order(场次、预定人姓名、预定人手机号码)，POST的数据为此表的字段小写
	 * @param userId  用户Id
	 * @param seatStr  在线选座 座位编号：座位1,座位2
	 * @param seats  自由选座 座位数
	 * @return JSON : {
	 * "success" : "1"        //1表示报名成功，其它失败
	 * "errormsg" : "105"     //101-活动Id不允许为空;102-活动场次Id不允许为空;103-用户Id不允许为空;104-座位数必须大于0;105-报名失败
	 * }
	 */

	@SuppressWarnings("unused")
	@CrossOrigin
	@RequestMapping(value = "/bookingsave", method = RequestMethod.POST)
	public RetMobileEntity bookingsave(String actId,String eventId, String userId,String orderPhoneNo,String seatStr,int seats,String name ){
		RetMobileEntity res = new RetMobileEntity();
		WhgActActivity whgActActivity = whhdService.getActDetail(actId);
		Map<String,Object> map = whhdService.checkActOrder(actId, eventId, userId, seatStr, seats);
		try {
			if(Integer.parseInt(String.valueOf(map.get("code"))) != 0){
				res.setCode(Integer.parseInt(String.valueOf(map.get("code"))));
				res.setMsg(String.valueOf(map.get("msg")));
			}
			else{
				res.setCode(0);
				String id = this.commservice.getKey("WhgActOrder");
				whhdService.saveActOrder(id,actId, eventId, userId, orderPhoneNo, seatStr, seats, name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * 查询活动列表
	 * @param request
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/actList", method = RequestMethod.POST)
	public RetMobileEntity actList(HttpServletRequest request){
		RetMobileEntity retMobileEntity = new RetMobileEntity();
		String type = getParam(request,"type",null);//活动类型
		String district = getParam(request,"district",null);//区域
		String sdate = getParam(request,"sdate",null);//排序值
		String index = getParam(request,"index","1");//pageNo
		String size = getParam(request,"size","10");//pageSize
		Map param = new HashMap();
		if(null != type){
			param.put("etype", type);
		}
		if(null != district){
			param.put("areaid", district);
		}
		if(null != sdate){
			param.put("sdate", sdate);
		}
		PageInfo pageInfo = whhdService.getActListForActFrontPage(index,size,param);
		if(null == pageInfo){
			retMobileEntity.setCode(1);
			retMobileEntity.setMsg("获取活动列表失败");
			return retMobileEntity;
		}
		retMobileEntity.setCode(0);
		retMobileEntity.setData((List)pageInfo.getList());
		RetMobileEntity.Pager pager = new RetMobileEntity.Pager();
		pager.setIndex(pageInfo.getPageNum());
		pager.setTotal(Long.valueOf(pageInfo.getTotal()).intValue());
		pager.setSize(pageInfo.getSize());
		pager.setCount(pageInfo.getPages());
		retMobileEntity.setPager(pager);
		return retMobileEntity;
	}

	/**
	 * 查询活动详情
	 * @param actvid
	 * @param request
	 * @return
	 * @throws Exception
	 * [{value: '110000',text: '北京市', children: [{value: '110101',text: '东城区' },{value: '2',text: '区' }]}]
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@CrossOrigin
	@RequestMapping(value = "/detail", method = RequestMethod.POST)
	public RetMobileEntity  detail(String actvid,String userId,WebRequest request ){
		RetMobileEntity  res = new RetMobileEntity ();
		Map<String,Object> param = new HashMap<>();
		try {
			if(actvid ==null || "".equals(actvid)){
				res.setCode(101);
				res.setMsg("活动Id不允许为空");//活动Id不允许为空
			}else{
				//活动详情
				WhgActActivity actdetail = this.whhdService.getActDetail(actvid);
				//判断该活动是否已经收藏
				if(userId !=null && userId != "0"){
					List<WhCollection> collectionList = whhdService.findCollection4UserIdAndItemId(userId, actvid,"4");
					if(collectionList.size()>0){
						param.put("scState", 1);
					}
				}
				List<Object> userList =  new ArrayList<Object>();
				//活动场次信息
				List<String> dateList = whhdService.getActDate(actvid);
				List myTimeList = new ArrayList();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if(null != dateList && !dateList.isEmpty()){
					for(String date : dateList){
						Map item = new HashMap();
						item.put("text",date);
						List<Map> timeList = whhdService.getActTimes(actvid,date);
						List<Map> children = new ArrayList<Map>();

						if(null != timeList && !timeList.isEmpty()){
							for(Map map : timeList){
								Date playStartTime = sdf.parse(String.valueOf(map.get("playstarttime")));
								Date nowDate = new Date();
								if(nowDate.getTime() < playStartTime.getTime()){
									String text = (String) map.get("playstime") + "-" + (String)map.get("playetime");
									String value = (String)map.get("id");
									Map child = new HashMap();
									child.put("text",text);
									child.put("value",value);
									children.add(child);
								}
							}
							item.put("children",children);
							myTimeList.add(item);
						}
					}
				}
				/**
				 List<WhgActTime> actvitm = this.whhdService.getPlayDate4ActId(actvid);
				 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				 if(actvitm != null && actvitm.size()>0){
				 for(int i=0;i<actvitm.size();i++) {
				 Map param_ = new HashMap();
				 //map = new HashMap<String, Object>();

				 param_.put("text", sdf.format(actvitm.get(i).getPlaydate()));
				 param_.put("value", actvitm.get(i).getId());
				 List<WhgActTime> timePlayList = whhdService.getActTimeList(actvid,actvitm.get(i).getPlaydate());
				 //将小于当前时间的场次过滤
				 Map eventMap =  null;
				 for(WhgActTime actTime : timePlayList ){
				 Date playStartTime = actTime.getPlaystarttime();
				 Date nowDate = new Date();
				 List<Object> timeList = new ArrayList<Object>();
				 if(nowDate.getTime() < playStartTime.getTime()){
				 eventMap = new HashMap();
				 eventMap.put("text", actTime.getPlaystime()+"-"+actTime.getPlayetime());
				 eventMap.put("value", actTime.getId());
				 timeList.add(eventMap);
				 // 将Map Key 转化为List
				 param_.put("children", timeList);
				 }
				 }
				 userList.add(param_);
				 }
				 }
				 */
				param.put("timeList", myTimeList);
				param.put("actdetail", actdetail);
				res.setData(param);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return res;
	}


	/**
	 * 查询座位信息
	 * @param actId
	 * @param userId
	 * @param eventId
	 * @param request
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/findSeat4ActId", method = RequestMethod.POST)
	public RetMobileEntity findSeat4ActId(String actId,String userId,String eventId,WebRequest request){
		RetMobileEntity res = new RetMobileEntity();
		Map<String,Object> map = new HashMap<>();
		try {
			JSONObject seatJson = whhdService.getSeat4ActId(actId,eventId,userId);
			WhgActActivity actModel = whhdService.getActDetail(actId);
			WhgActTime whgActTime = whhdService.selectOnePlay(eventId );
			int totalSeatSize = 0;
			if(actModel.getSellticket() == 2){
				totalSeatSize = actModel.getTicketnum();
			}else{
				totalSeatSize = whhdService.getWhgActSeat4ActId(actId);
			}
			map.put("mapType", seatJson.get("mapType"));
			map.put("statusMap", seatJson.get("statusMap"));
			map.put("seatSize",seatJson.get("seatSize"));
			map.put("totalSeatSize",totalSeatSize);
			map.put("seatSizeUser",seatJson.get("seatSizeUser"));
			map.put("act", actModel);
			map.put("actTime", whgActTime);
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
		}
		res.setData(map);
		return res;
	}

	private String getParam(HttpServletRequest request,String paramName,String defaultValue){
		String value = request.getParameter(paramName);
		if(null == value || value.trim().isEmpty()){
			return defaultValue;
		}
		return value;
	}

}
