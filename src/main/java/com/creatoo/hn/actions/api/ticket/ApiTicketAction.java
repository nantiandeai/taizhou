package com.creatoo.hn.actions.api.ticket;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.model.*;
import com.creatoo.hn.services.api.ticket.ApiTicketService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**取票，订票接口控制器
 * Created by caiyong on 2017/7/10.
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/api/ticket")
public class ApiTicketAction {

    private static Logger logger = Logger.getLogger(ApiTicketAction.class);

    @Autowired
    private ApiTicketService apiTicketService;

    @CrossOrigin
    @RequestMapping(value = "/pickUp",method = RequestMethod.POST)
    public ResponseBean pickUp(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String ticketNo = getParamValue(request,"ticketNo",null);
        if(null == ticketNo){
            responseBean.setSuccess("101");
            responseBean.setErrormsg("取票码不能为空");
            return responseBean;
        }
        WhgActOrder whgActOrder = apiTicketService.findActOrderByTicketNo(ticketNo);
        WhgVenRoomOrder whgVenRoomOrder = apiTicketService.findVenRoomOrderByTicketNo(ticketNo);
        if(null != whgActOrder){
            return actOrderHandle(whgActOrder);
        }
        if(null != whgVenRoomOrder){
            return venRoomOrderHandle(whgVenRoomOrder);
        }
        responseBean.setSuccess("102");
        responseBean.setErrormsg("此取票码不存在");
        return responseBean;
    }

    @CrossOrigin
    @RequestMapping(value = "/completePickUp",method = RequestMethod.POST)
    public ResponseBean completePickUp(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String orderId = getParamValue(request,"orderId",null);
        String orderType = getParamValue(request,"orderType",null);
        if(null == orderId || null == orderType){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("参数不足");
            return responseBean;
        }
        if("act".equalsIgnoreCase(orderType)){
            apiTicketService.updateActOrderState(orderId,3);
        }else if("ven".equalsIgnoreCase(orderType)){
            apiTicketService.updateVenRoomOrderState(orderId,2);
        }else {
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("修改票务状态失败");
        }
        return responseBean;
    }

    private ResponseBean actOrderHandle(WhgActOrder whgActOrder){
        ResponseBean responseBean = new ResponseBean();
        Map map = new HashMap();
        map.put("id",whgActOrder.getActivityid());
        WhgActActivity whgActActivity = apiTicketService.findActByParam(map);
        if(null == whgActActivity){
            responseBean.setSuccess("103");
            responseBean.setErrormsg("活动不存在");
            return responseBean;
        }
        if(6 != whgActActivity.getState()){
            responseBean.setSuccess("104");
            responseBean.setErrormsg("此活动已下架");
            return responseBean;
        }
        map.put("id",whgActOrder.getEventid());
        WhgActTime whgActTime = apiTicketService.findActTimeByParam(map);
        if(null == whgActTime){
            responseBean.setSuccess("105");
            responseBean.setErrormsg("此活动场次不存在");
            return responseBean;
        }
        if(0 == whgActTime.getState()){
            responseBean.setSuccess("105");
            responseBean.setErrormsg("此活动场次不存在");
            return responseBean;
        }
        Date date = whgActTime.getPlayendtime();
        if(1 == compareToday(date)){
            responseBean.setSuccess("106");
            responseBean.setErrormsg("此活动场次已结束");
            return responseBean;
        }
        map.clear();
        map.put("orderid",whgActOrder.getId());
        List<WhgActTicket> whgActTicketList = apiTicketService.findActTicketByParam(map);
        if(null == whgActTicketList){
            responseBean.setSuccess("107");
            responseBean.setErrormsg("获取此活动票务失败");
            return responseBean;
        }
        if(2 == whgActOrder.getTicketstatus()){
            responseBean.setSuccess("108");
            responseBean.setErrormsg("不能重复取票");
            return responseBean;
        }
        if(3 == whgActOrder.getTicketstatus()){
            responseBean.setSuccess("108");
            responseBean.setErrormsg("订单已取消");
            return responseBean;
        }
        Map res = new HashMap();
        res.put("act",whgActActivity);
        res.put("order",whgActOrder);
        res.put("time",whgActTime);
        res.put("ticket",whgActTicketList);
        responseBean.setData(res);
        return responseBean;
    }

    private ResponseBean venRoomOrderHandle(WhgVenRoomOrder whgVenRoomOrder){
        ResponseBean responseBean = new ResponseBean();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(0 == whgVenRoomOrder.getState() || 2 == whgVenRoomOrder.getState()){
            responseBean.setSuccess("101");
            responseBean.setErrormsg("此活动室预订未审核通过");
            return responseBean;
        }
        if(1 == whgVenRoomOrder.getState()){
            responseBean.setSuccess("102");
            responseBean.setErrormsg("此活动室预订已取消");
            return responseBean;
        }
        Map map = new HashMap();
        map.put("id",whgVenRoomOrder.getRoomid());
        WhgVenRoom whgVenRoom = apiTicketService.findVenRoomByParam(map);
        if(null == whgVenRoom){
            responseBean.setSuccess("103");
            responseBean.setErrormsg("此活动室不存在");
            return responseBean;
        }
        Date timeday = whgVenRoomOrder.getTimeday();
        Date timeend = whgVenRoomOrder.getTimeend();
        String strTemp = simpleDateFormat1.format(timeday) + " " + simpleDateFormat2.format(timeend);
        try {
            Date dateTemp = simpleDateFormat3.parse(strTemp);
            if(1 == compareToday(dateTemp)){
                responseBean.setSuccess("104");
                responseBean.setErrormsg("活动室订单已过期");
                return responseBean;
            }
        }catch (Exception e){
            logger.error(e.toString());
            responseBean.setSuccess("105");
            responseBean.setErrormsg("获取此活动室票务信息失败");
            return responseBean;
        }
        Map res = new HashMap();
        res.put("order",whgVenRoomOrder);
        res.put("venRoom",whgVenRoom);
        responseBean.setData(res);
        return responseBean;
    }

    /**
     * 1:before，2：after，-1：其他
     * @param date
     * @return
     */
    private Integer compareToday(Date date){
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        if(localDateTime.isBefore(LocalDateTime.now())){
            return 1;
        }
        if(localDateTime.isAfter(LocalDateTime.now())){
            return 2;
        }
        return -1;
    }

    /**
     * 获取请求的参数
     * @param request
     * @param paramName
     * @param defaultValue
     * @return
     */
    private String getParamValue(HttpServletRequest request,String paramName,String defaultValue){
        String value = request.getParameter(paramName);
        if(null == value || value.trim().isEmpty()){
            return defaultValue;
        }
        return value;
    }
}
