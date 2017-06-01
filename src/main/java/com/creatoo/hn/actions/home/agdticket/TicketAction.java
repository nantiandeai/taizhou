package com.creatoo.hn.actions.home.agdticket;

import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.services.home.agdticket.TicketService;
import com.creatoo.hn.utils.CompareTime;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 取票控制器
 * Created by wangxl on 2017/4/12.
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/agdticket")
public class TicketAction {
    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(TicketAction.class);

    @Autowired
    private TicketService ticketService;

    /**
     * 进入取票界面
     * @param request 请求对象
     * @param response 响应对象
     * @return 取票界面
     */
    @RequestMapping("index")
    public ModelAndView showTicketPate(HttpServletRequest request, HttpServletResponse response){
        ModelAndView view = new ModelAndView();
        try{
            view.setViewName("/home/agdticket/index");
        } catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return view;
    }

    @RequestMapping("print")
    public void printTicket(HttpServletRequest request,HttpServletResponse response)throws Exception{
        JSONObject jsonObject = new JSONObject();
        String orderValidateCode = request.getParameter("orderValidateCode");
        
        if(null == orderValidateCode || orderValidateCode.isEmpty() ){
            jsonObject.put("data", "对不起，您的取票码错误或不存在");
            jsonObject.put("status", 1);
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().print(jsonObject.toString());
        }else{
            Map queryRes = null;
            queryRes = ticketService.getOrderFromAct(orderValidateCode);
            if(null != queryRes){
                jsonObject = getActivityTicketInfo(queryRes);
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().print(jsonObject.toString());
                return;
            }
            queryRes = ticketService.getOrderFromVen(orderValidateCode);
            Integer state = -1;
            if(null == queryRes){
                jsonObject.put("data", "对不起，您的取票码错误或不存在");
                jsonObject.put("status", 1);
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().print(jsonObject.toString());
                return;
            }
            state = Integer.parseInt(String.valueOf(queryRes.get("state")));
        	if(state == 0){
                jsonObject.put("data", "对不起，您的订单正预定申请中，不能取票");
                jsonObject.put("status", 1);
            }else if(state == 1){
                jsonObject.put("data", "对不起，您已经取消了预定，不能取票");
                jsonObject.put("status", 1);
            }else if(state == 2){
                jsonObject.put("data", "对不起，您的预定审核失败，不能取票");
                jsonObject.put("status", 1);
            }else{
                jsonObject = getRoomTicketInfo(queryRes);
            }
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().print(jsonObject.toString());
        }
    }

    public JSONObject getActivityTicketInfo(Map cmsActivityOrder){
        JSONObject jsonObject = new JSONObject();
        StringBuffer sb = new StringBuffer();
        try{
            if(StringUtils.isEmpty((String) cmsActivityOrder.get("activityid"))){
                jsonObject.put("data","对不起，您所订的活动有误，请联系管理员，添加活动关联!");
                jsonObject.put("status",1);
                return jsonObject;
            }
            if(2 == Integer.valueOf(cmsActivityOrder.get("orderisvalid").toString())){
                jsonObject.put("data","对不起，您的活动订单已失效!");
                jsonObject.put("status",1);
                return jsonObject;
            }
            if(3 == Integer.valueOf(cmsActivityOrder.get("orderisvalid").toString())){
                jsonObject.put("data","对不起，您所订的活动已退订!");
                jsonObject.put("status",1);
                return jsonObject;
            }
            if(3 <= Integer.valueOf((Integer) cmsActivityOrder.get("printtickettimes"))){
                jsonObject.put("data","您的取票码已出票，请勿重复取票!");
                jsonObject.put("status",1);
                return jsonObject;
            }
            
            // 文化活动时间
            Date date=new Date(); //系统当前时间
            SimpleDateFormat sdf2 =new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String nowDate2 = sdf2.format(date);
            int statusDate2 = CompareTime.timeCompare2((String) cmsActivityOrder.get("playstarttime"),nowDate2); //当前时间与活动开始时间比较
            //返回 0 表示时间日期相同， 1 表示日期1>日期2， -1 表示日期1<日期2
            if(statusDate2 == -1){
                jsonObject.put("data","对不起，您的所预订的活动已过期，不可取票!");
                jsonObject.put("status",1);
                return jsonObject;
            }
            // 生成活动订单票模板
            sb = ticketService.getPrintTicketString("activity", cmsActivityOrder, null);
            //int status = cmsActivityOrderService.updateActivityStatusAfterPrintTicket(cmsActivityOrder);
            //防止printtickettimes为空的情况
            Object temp = cmsActivityOrder.get("printtickettimes");
            if(null == temp){
                temp = 0;
            }
            Integer printTicketTimes = (Integer) temp + 1;
            //修改订单取票次数
            ticketService.updateActPrintTimes((String) cmsActivityOrder.get("id"),printTicketTimes);
            if(true){
                jsonObject.put("data",sb.toString());
                jsonObject.put("status",0);
                System.out.println(sb.toString());
                if(!sb.toString().equals("")){
                    String[] seats=sb.toString().split("\\*\\*\\*\\*");
                    System.out.println("seats:"+seats);
                    if(seats.length>1){
                        if(seats[0]!=""){
                            jsonObject.put("data",seats[0]);
                        }
                        if(seats[1]!=""){
                            jsonObject.put("piaoInfo",seats[1]);
                        }
                    }
                    if(seats.length>2){
                        jsonObject.put("seat",seats[2]);
                    }
                }
                jsonObject.put("type","activity");
            }

        }catch(Exception e){
            e.printStackTrace();
            jsonObject.put("data","取票信息有误");
            jsonObject.put("status",1);
        }
        System.out.println("打印取票信息" +sb.toString());
        return jsonObject;
    }

    public JSONObject getRoomTicketInfo(Map cmsRoomOrder){
        JSONObject jsonObject = new JSONObject();
        StringBuffer sb = new StringBuffer();
        try{
            if(cmsRoomOrder == null || cmsRoomOrder.get("roomid") == null || StringUtils.isEmpty((String) cmsRoomOrder.get("roomid"))){
                jsonObject.put("data","对不起，您所订的场次对应的活动室有误，请联系管理员，添加活动室关联!");
                jsonObject.put("status",1);
                return jsonObject;
            }
            Integer temp = (Integer)cmsRoomOrder.get("state");
            if(null == temp){
                temp = 3;
            }
            if(1 == (Integer)temp){
                jsonObject.put("data","对不起，您所订的场次已退订!");
                jsonObject.put("status",1);
                return jsonObject;
            }else if(2 == (Integer)temp){
                jsonObject.put("data","对不起，您所订的场次审核失败!");
                jsonObject.put("status",1);
                return jsonObject;
            }
            temp = (Integer) cmsRoomOrder.get("printtickettimes");
            if(null == temp){
                temp = 0;
            }
            if(3 <= (Integer)temp){
                jsonObject.put("data","您的取票码已出票,请勿重复取票!");
                jsonObject.put("status",1);
                return jsonObject;
            }
            
          //验证是否订单已过期
            java.text.SimpleDateFormat sdf_ = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.text.SimpleDateFormat sdf2_ = new java.text.SimpleDateFormat("HH:mm");
            Object timeday = cmsRoomOrder.get("timeday");
            Object timestart = cmsRoomOrder.get("timestart");
            Object timeend = cmsRoomOrder.get("timeend");
            String timedayStr = sdf_.format(timeday);
            String timestartStr = sdf2_.format(timestart);
            String timeendStr = sdf2_.format(timeend);
            Date lastDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").parse(timedayStr+" "+timeendStr);
            if(new Date().getTime() > lastDate.getTime()){
                jsonObject.put("data","对不起，您的预定的活动室已经过期，不能取票!");
                jsonObject.put("status",1);
                return jsonObject;
            }
            sb = ticketService.getPrintTicketString("venue", cmsRoomOrder, cmsRoomOrder);
            Integer printtickettimes = temp + 1;
            ticketService.updateVenPrintTimes((String)cmsRoomOrder.get("id"),printtickettimes);
            jsonObject.put("data",sb.toString());
            jsonObject.put("status",0);
            jsonObject.put("type","venue");
        }catch(Exception e){
            e.printStackTrace();
            jsonObject.put("data","取票信息有误");
            jsonObject.put("status",1);
        }
        System.out.println("打印取票信息" +sb.toString());
        return jsonObject;
    }
}
