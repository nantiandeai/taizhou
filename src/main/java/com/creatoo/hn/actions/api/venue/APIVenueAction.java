package com.creatoo.hn.actions.api.venue;

import com.creatoo.hn.ext.bean.RetMobileEntity;
import com.creatoo.hn.mapper.WhUserMapper;
import com.creatoo.hn.model.*;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiTypeService;
import com.creatoo.hn.services.home.agdcgfw.CgfwService;
import com.creatoo.hn.services.home.agdwhhd.WhhdService;
import com.creatoo.hn.services.home.userCenter.VenueOrderService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 场馆预定接口
 * Created by wangxl on 2017/4/12.
 */
@SuppressWarnings("ALL")
@CrossOrigin
@RestController
@RequestMapping("/api/venue")
public class APIVenueAction {
    /**
     * 日志控制器
     */
    Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    private WhUserMapper whUserMapper;

    @Autowired
    private CgfwService cgfwService;

    @Autowired
    private VenueOrderService venueOrderService;
    
   //设备类型
    @Autowired
    private WhgYunweiTypeService whgYunweiTypeService;
    
    @Autowired
    private WhhdService  whhdService;
    
    
    
    
    @CrossOrigin
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public RetMobileEntity list(int index,int size,WebRequest request){
    	RetMobileEntity res = new RetMobileEntity();
    	String type = request.getParameter("type");
    	Map<String,Object> param = new HashMap<>();
    	Map<String, Object> venueList = new HashMap<>();
    	param.put("page", index);
    	param.put("rows", size);
    	param.put("etype", type);
    	try {
    		venueList =  this.cgfwService.findList(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	res.setData(venueList);
    	return res;
    }
    
    @CrossOrigin
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public RetMobileEntity detail(String itemId,String userId,WebRequest request){
    	RetMobileEntity res = new RetMobileEntity();
    	Map<String,Object> param = new HashMap<>();
        //场馆信息
    	try {
    		if(itemId != null && itemId !=""){
    			WhgVen whgVen = this.cgfwService.findWhgVen4Id(itemId);
    			param.put("venue", whgVen);
    			if(whgVen.getEtype() != null && whgVen.getEtype() != "")
    			{
    				String eType = whgVen.getEtype();
    				String[] types = eType.split(",");
    				String type ="";
    				for(int i=0;i<types.length;i++){
    					WhgYwiType whgYwiType =whgYunweiTypeService.findWhgYwiType4Id(types[i]);
    					type += whgYwiType.getName()+",";
    	        	}
    				type = type.substring(0,type.length()-1);
    				param.put("type", type);
    			}
    			
    			param.put("roomlist", this.cgfwService.selectWhgVenroom4Ven(itemId));
    			//判断该场馆是否已经收藏
    			if(userId !=null && userId != "0"){
    				List<WhCollection> collectionList = whhdService.findCollection4UserIdAndItemId(userId, itemId,"2");
    				if(collectionList.size()>0){
    					param.put("scState", 1);
    				}
    			}
    			//有效的开放时段个数
                //int roomtimeCount = this.cgfwService.countRoomTimeOpen4Room(itemId);
               // param.put("roomtimeCount", roomtimeCount);
                //if (roomtimeCount > 0){
                    //有效预订的最后与开始时间
                //Map seday = this.cgfwService.roomTimeOpenSEday(itemId);
               // param.put("seday",seday);
                //}
                res.setData(param);
    		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return res;
    }
    
    /**
     * 活动室预定时段列表
     * @param bday
     * @param eday
     * @return
     */
    @SuppressWarnings("rawtypes")
	@CrossOrigin
    @RequestMapping(value = "/bookingtime", method = RequestMethod.POST)
    public RetMobileEntity bookingtime(String roomId,String bday,
            								String eday,String userId,
            								WebRequest request){
    	RetMobileEntity res = new RetMobileEntity();
        //param.put("seday",seday);
        Map<String, Object> rest = new HashMap<String, Object>();
        //装入当前时间
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
        
        //时分秒
        SimpleDateFormat sdf_ = new SimpleDateFormat("HH:mm:ss");//小写的mm表示的是分钟  
        
        //活动开始时间 精确到时分秒
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟  
        try {
        	WhgVenRoom whgVenRoom = cgfwService.findWhgVenroom4Id(roomId);
        	WhgVen whgVen = cgfwService.findWhgVen4Id(whgVenRoom.getVenid());
        	String facility = whgVenRoom.getFacility();
        	String facilityName = "";
        	if(facility !=null && facility != ""){
        		String[] facIds = facility.split(",");
            	for(int j=0;j<facIds.length;j++){
            		WhgYwiType whgYwiType =whgYunweiTypeService.findWhgYwiType4Id(facIds[j]);
            		if(whgYwiType != null)
            		facilityName += whgYwiType.getName()+",";
            	}
            	if(facilityName != "" && facilityName != null)
            	facilityName = facilityName.substring(0,facilityName.length()-1);
            	rest.put("facilityName", facilityName);
        	}
//            	if(!facility.trim().isEmpty()){
//    				String[] facIds = facility.split(",");
//    				String facilityName = "";
//    				for(int j=0;j<facIds.length;j++){
//    					WhgYwiType whgYwiType =whgYunweiTypeService.findWhgYwiType4Id(facIds[j]);
//    					facilityName += whgYwiType.getName()+",";
//    				}
//    				facilityName = facilityName.substring(0,facilityName.length()-1);
//    				rest.put("facilityName", facilityName);
//    			}

            	//活动室有效预订的最后与开始时间
            	Map seday = this.cgfwService.roomTimeOpenSEday(roomId);
            	if(seday.get("endDay") != null){
            		Date eday_ = sdf.parse(sdf.format((Date) seday.get("endDay")));
                	Date bday_ = sdf.parse(sdf.format((Date) seday.get("beginDay")));
                	List timeList = this.cgfwService.selectWhgVenroomtime(roomId, bday_, eday_);
                	String[] weekOfDays = {"日", "一", "二", "三", "四", "五", "六"};  
                	Date oldDay = null;
                	List<Object> timeInfoList =  new ArrayList<Object>();
                	for(int i=0;i<timeList.size();i++){
                		WhgVenRoomTime whgVenRoomTime = (WhgVenRoomTime) timeList.get(i);
                		Date day = whgVenRoomTime.getTimeday();
                		if(oldDay != null && oldDay.getTime() == day.getTime()){
                			continue;
                		}
                		oldDay = day;
                		//Date timeStart = whgVenRoomTime.getTimestart();
                		//long sTime = sdf_.parse(sdf_.format(timeStart)).getTime();
                		//if(day.getTime() + sTime > nowDate.getTime() ){
                			Map<String,Object> timeMap = new HashMap<>();
                			//根据日期查询该日期下的场次时间段
                			List<WhgVenRoomTime> timePlayList = cgfwService.findWhgVenRoomTime4IdAndDate(roomId, day);
                			List<WhgVenRoomTime> newTimePlayList = new ArrayList<>();
                			//将小于当前时间的场次过滤
                			for(WhgVenRoomTime venRoomTime : timePlayList ){
                				String playStartTime = sdf.format(venRoomTime.getTimeday()) +" "+sdf_.format(venRoomTime.getTimestart());
                				Date playsTime = sdf1.parse(playStartTime);
                				Date nowDate_ = new Date();
                				if(nowDate_.getTime() < playsTime.getTime()){
                					newTimePlayList.add(venRoomTime);
                				}
                			}
                			//将日期转换为星期
                			Calendar calendar = Calendar.getInstance();      
                    	    if(day != null){        
                    	         calendar.setTime(day);      
                    	    }        
                    	    int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;      
                    	    if (w < 0){        
                    	        w = 0;      
                    	    } 
                    	    String weekDay = weekOfDays[w];
                    	    timeMap.put("dayTime", calendar.get(Calendar.DAY_OF_MONTH));
                    	    timeMap.put("timePlay", newTimePlayList);
                    	    timeMap.put("weekDay", weekDay);
                    	    timeInfoList.add(timeMap);
                		//}
                		
                	}
                	rest.put("timeInfo", timeInfoList);
                	rest.put("roomOrderUser", this.cgfwService.selectWhgVenroomorder4User(roomId, bday_, eday_, userId) );
            	
        	
        	}
        	rest.put("whgVenRoom", whgVenRoom);
        	rest.put("whgVen", whgVen);
            res.setData(rest);

        } catch (Exception e) {
            log.error("loadRoomTimes error", e);
        }
        return res;
    }
    

    /**
     * 场馆预订检查
     * GET url： http://IP[:prot][/APP]/api/ven/check?roomtimeid=*&userid=*
     * POST: {url: http://IP[:prot][/APP]/api/ven/check, data:{roomtimeid:'*', userid:'*'}}
     * @param roomtimeid  活动室开放时段ID
     * @param userid 系统用户ID
     * @return  {
     *     success : true | false,
     *     code : 1001 | 1002 | 1003 |1004 |9999
     *     errmsg : '错误码参考信息'
     * }
     * code => errmsg
     * 1001 ： 活动室/场馆/开放时段状态异常
     * 1002 ： 用户信息获取失败
     * 1003 ： 目标为通过审核的预订时段
     * 1004 ： 重复申请
     * 9999 ： 系统异常
     */
    @CrossOrigin
    @RequestMapping("/check")
    public Object check(String roomtimeid, String userId){
        Map<String, Object> rest = new HashMap(); 

        try {
            //获取 user
            WhUser user = this.whUserMapper.selectByPrimaryKey(userId);

            this.cgfwService.testUserVenroomtimeInfo(user, roomtimeid, null, null, null);

            rest.put("success", true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rest.put("success", false);
            rest.putAll( this.cgfwService.praseTestError(e.getMessage()) );
        }

        return rest;
    }

    /**
     * 场馆预订
     * GET url: http://IP[:prot][/APP]/api/ven/order?roomtimeid=*&userid=*&userphone=138*&username=*&purpose=*
     * POST: {url: http://IP[:prot][/APP]/api/ven/order, data:{roomtimeid:'*', userid:'*', userphone:'*', username:'*', purpose:''}}
     * @param roomtimeid 活动室开放时段ID
     * @param userid 系统用户ID
     * @param userphone 预订人手机号
     * @param username 预订人姓名 len<=20
     * @param purpose  预订用途 len<=200
     * @return {
     *     success : true | false,
     *     data : {订单对象},
     *     code : 1001 | 1002 | 1003 |1004 |9999
     *     errmsg : '错误码参考信息'
     * }
     * code => errmsg
     * 1001 ： 活动室/场馆/开放时段状态异常
     * 1002 ： 用户信息获取失败
     * 1003 ： 目标为通过审核的预订时段
     * 1004 ： 重复申请
     * 9999 ： 系统异常
     * 1101 ： 手机号码格式不正确
     * 1102 ： 预订人数据格式不正确
     * 1103 ： 预订用途输入过长
     */
    @CrossOrigin
    @RequestMapping(value = "/bookingsave", method = RequestMethod.POST)
    public RetMobileEntity order(String roomtimeid, String userId, String userphone, String username, String purpose){
    	RetMobileEntity res = new RetMobileEntity();
        Map<String, Object> rest = new HashMap();
        try {
            //获取 user
            WhUser user = this.whUserMapper.selectByPrimaryKey(userId);

            WhgVenRoomOrder order = this.cgfwService.saveUserVenroomtimeInfo(roomtimeid, user, userphone, username, purpose);

            rest.put("success", true);
            rest.put("data", order);
            res.setData(rest);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.setCode(101);
            res.setMsg(e.getMessage().substring(16,e.getMessage().length()));
            rest.putAll( this.cgfwService.praseTestError(e.getMessage()) );
        }

        return res;
    }

    /**
     * 场馆预订取消申请
     * GET url: http://IP[:prot][/APP]/api/ven/unorder?orderid=*
     * POST: {url: http://IP[:prot][/APP]/api/ven/unorder, data:{orderid:'*'}}
     * @param orderid 订单记录ID
     * @return {success : true | false}
     */
    @CrossOrigin
    @RequestMapping("/unorder")
    public Object unorder(String orderid){
        Map<String, Object> rest = new HashMap();
        try {
            int count = this.venueOrderService.unOrder(orderid);
            rest.put("success", count>0);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rest.put("success", false);
        }

        return rest;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@CrossOrigin
    @RequestMapping(value = "/loadType", method = RequestMethod.POST)
    public RetMobileEntity loadType(){
    	RetMobileEntity res = new RetMobileEntity();
        Map<String, Object> rest = new HashMap();
        //场馆分类
        List<WhgYwiType> venueTypeList = whgYunweiTypeService.findWhgYwiTypeList("2");
        //区域
        List<WhgYwiType> areaList = whgYunweiTypeService.findWhgYwiTypeList("6");
        //活动分类
        List<WhgYwiType> actTypeList = whgYunweiTypeService.findWhgYwiTypeList("4");
        rest.put("venueTypeList", venueTypeList);
        rest.put("areaList", areaList);
        rest.put("actTypeList", actTypeList);
        res.setData(rest);
        return res;
    }
    
    public static void main(String[] args) throws ParseException {
    	String[] weekOfDays = {"日", "一", "二", "三", "四", "五", "六"}; 
    	Date nowDate = new Date();
    	Calendar calendar = Calendar.getInstance();      
	    if(nowDate != null){        
	         calendar.setTime(nowDate);      
	    }        
	    int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;      
	    if (w < 0){        
	        w = 0;      
	    } 
	    String weekDay = weekOfDays[w];
	    System.out.println(weekDay);
	}
}
