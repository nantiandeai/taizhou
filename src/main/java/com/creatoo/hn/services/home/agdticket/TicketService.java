package com.creatoo.hn.services.home.agdticket;

import com.creatoo.hn.mapper.home.CrtTicketMapper;
import com.creatoo.hn.utils.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**取票服务
 * Created by Administrator on 2017/4/12.
 */
@SuppressWarnings("all")
@Service
public class TicketService {
    private static Logger log = Logger.getLogger(TicketService.class);

    @Autowired
    private CrtTicketMapper crtTicketMapper;

    public Map getOrderFromAct(String orderId){
        try{
            Map res = crtTicketMapper.queryActOrder(orderId);
            if(null != res){
                List<Map> ticketList = crtTicketMapper.queryActTicket((String) res.get("id"));
                if(null != ticketList && !ticketList.isEmpty()){
                    res.put("ticketList",ticketList);
                }
            }
            return res;
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    public Map getOrderFromVen(String orderId){
        try {
            return crtTicketMapper.queryVenOrder(orderId);
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    public StringBuffer getPrintTicketString(String type,Map order,Map ticket) {
        StringBuffer sb = new StringBuffer();
        StringBuffer ssb = new StringBuffer();
        SimpleDateFormat sdf3= new SimpleDateFormat("yyyyMM");
        //封装二维码路径生成二维码图片


        // Code.encode(validateSeat.toString(),"D:/gg.jpg");
        /**
         StringBuffer stringBuffer = new StringBuffer();
         stringBuffer.append(staticServer.getStaticServerUrl());
         stringBuffer.append(sdf3.format(new Date()));
         stringBuffer.append("/");
         stringBuffer.append(order.get("id"));
         stringBuffer.append(".jpg");
         */

        String piaoInfo="";
        String seatss="";
        if("activity".equals(type)){
            //LOGO
//   			 sb.append("<div class='piao_head'>");
//   			 sb.append("<img src='"+staticServer.getStaticServerUrl()+"hd_1.jpg' width='270'/>");
//   			 sb.append("</div>");

            //
            sb.append("<table width='100%' border='0' cellspacing='0' cellpadding='0' style='padding:8px 5px 0 5px;'>");
            //	 sb.append("<tr><td colspan='2'>&nbsp;</td></tr>");

            //活动名称
            sb.append("<tr style='display:none'>");
            sb.append("<td style='line-height:20px; padding:1px 0;' width='85' valign='top' ><p>活动名称:</p></td> ");
            sb.append("<td style='line-height:20px; padding:1px 0;'><p>"+order.get("name")+"</p></td> ");
            sb.append("</tr>");

            //活动名称
            sb.append("<tr>");
            sb.append("<td style='line-height:20px; padding:1px 0;' valign='top' ><p>活动名称:</p></td> ");
            sb.append("<td style='line-height:20px; padding:1px 0;'><p>"+order.get("name")+"</p></td> ");
            sb.append("</tr>");

            //活动时间
            sb.append("<tr>");
            sb.append("<td style='line-height:20px; padding:1px 0;'  valign='top' ><p>活动时间:</p></td> ");
            sb.append("<td style='line-height:20px; padding:1px 0;'><p>"+order.get("playstarttime")+"</p></td> ");
            sb.append("</tr>");

            //活动地点
            sb.append("<tr>");
            sb.append("<td style='line-height:20px; padding:1px 0;'  valign='top' ><p>活动地址:</p></td> ");
            sb.append("<td style='line-height:20px; padding:1px 0;'><p>"+order.get("address")+"</p></td> ");
            sb.append("</tr>");

            //票务信息
            /**
             String[] activityPayStatus=cmsActivityOrder.getPayStatus().split(",");
             */

            StringBuffer seatSB = new StringBuffer();//座位信息
            StringBuffer seatCode = new StringBuffer();//座位ID
            //取该订单下 未出票的 座位信息
            List<Map> details = (List<Map>)order.get("ticketList");

            for(Map  cmsActivityOrderDetail:details){
                String seat=(String) cmsActivityOrderDetail.get("seatcode");
                String seatId = (String) cmsActivityOrderDetail.get("seatid");
                seatCode.append(seatId);
                seatSB.append(seat);
                seatCode.append(",");
                seatSB.append(",");
                ssb.append("<div id='qrcodeImg"+seatId+"' data-val='"+order.get("ordernumber")+"' class='hideImgs' style='display:none' ></div>");
            }
            piaoInfo= seatSB.toString();
            //piaoInfo = (String )order.get("seatcode");
            if(null == piaoInfo || "".equals(piaoInfo)){
                piaoInfo ="测试用";
            }else {
                piaoInfo = piaoInfo.substring(0, piaoInfo.lastIndexOf(","));
            }
            seatss= seatCode.toString();
            if(null==seatss || "".equals(seatss)){
                seatss = "测试用";
            }else {
                seatss = seatss.substring(0, seatss.lastIndexOf(","));
            }
            sb.append("<tr>");
            sb.append("<td style='line-height:20px; padding:1px 0;'  valign='top' ><p>票务信息:</p></td> ");
            sb.append("<td style='line-height:20px; padding:1px 0;'><p>[piaoInfo]</p></td> ");
            sb.append("</tr>");



            //取票码
            sb.append("<tr>");
            sb.append("<td style='line-height:20px; padding:1px 0;'  valign='top' ><p>取&nbsp; 票&nbsp;&nbsp;码:</p></td> ");
            sb.append("<td style='line-height:20px; padding:1px 0;'><p clas s='last_pp'>"+order.get("ordernumber")+"</p></td> ");
            sb.append("</tr>");

            /**
             sb.append("<tr>");
             //sb.append("<td style='line-height:20px; padding:1px 0;'  valign='top' ><p>友情提示:</p></td> ");
             sb.append("<td colspan='2' style='line-height:20px; padding:1px 0;'><p></p></td> ");//暂时为空
             sb.append("</tr>");
             */

            //二维码 + 取票时间
            sb.append("<tr>");
            sb.append("<td>&nbsp;</td><td> ");
   			 /*sb.append("<td><img class='yanzheng' style='padding-top:10px;' src='"+stringBuffer.toString()+"' width='140' />");*/
            sb.append("<div id='qrcodeImg' data-val='"+order.get("id")+"'>[qrcodeImgdiv]</div>");
            sb.append(ssb.toString());
            sb.append("<div class='paio_t' style='font-size: 12px; padding-top:10px;'>取票时间:"+ DateUtils.parseDateToLongString(new Date())+"</div></td> ");
            sb.append("</tr>");



            sb.append("<tr><td>&nbsp;</td><td>&nbsp;</td></tr>");
            sb.append("<tr><td>&nbsp;</td><td>&nbsp;</td></tr>");
            sb.append("</table>");
            sb.append("****");
            sb.append(piaoInfo);
            sb.append("****");
            sb.append(seatss);
        }


        if("venue".equals(type)){
//   			sb.append("<img src='"+staticServer.getStaticServerUrl()+"cg_1.jpg' width='270' />");
            sb.append("<table width='100%' border='0' cellspacing='0' cellpadding='0' style='text-align:left; padding:8px 5px 0 5px;'>");
            sb.append("<tr style='margin-top: 15px;'>");
            sb.append("<td width='75' style='line-height:20px; padding:2px 0;' valign='top'><p>场馆名称:</p></td>");
            sb.append("<td style='line-height:20px; padding:2px 0;'><p>"+order.get("ven_title")+"</p></td>");
            sb.append("</tr>");

            sb.append("<tr>");
            sb.append("<td style='line-height:20px; padding:2px 0;' valign='top'><p>场馆地址:</p></td>");
            sb.append("<td style='line-height:20px; padding:2px 0;'><p>"+order.get("address")+"</p></td>");
            sb.append("</tr>");
            sb.append("</table>");

            sb.append("<div style='border:1px solid #6a6a6a; border-bottom:0; background:#f0f0f0; margin:10px 10px 10px 0;'>");

            String[] roomStatus = String.valueOf(order.get("ticketstatus")).split(",");
            //for (int i = 0; i < roomStatus.length; i++) {

                //开始时间
                Object timeday = order.get("timeday");
                Object timestart = order.get("timestart");
                Object timeend = order.get("timeend");
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                String timedayStr = sdf.format(timeday);
                java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("HH:mm");
                String timestartStr = sdf2.format(timestart);
                String timeendStr = sdf2.format(timeend);
                String timeScope = timedayStr+"  "+timestartStr+ "-"+timeendStr;

                //活动室名称
                String roomName = (String) order.get("room_title");
                sb.append("<div style='border-bottom:1px solid #6a6a6a; padding:5px; '>");
                sb.append("<table width='100%' border='0' cellspacing='0' cellpadding='0' style='text-align:left;'>");
                sb.append("<tr>");
                sb.append("<td width='80' valign='top'><p>活&nbsp; 动&nbsp;室:</p></td>");
                sb.append("<td><p>"+roomName+"</p></td>");
                sb.append("</tr>");

                sb.append("<tr>");
                sb.append("<td valign='top'><p>使用时间:</p></td>");
                sb.append("<td><p>"+timeScope+"</p></td>");
                sb.append("</tr>");
                sb.append("</table>");
                sb.append("</div>");

            //}

            sb.append("</div>");

            sb.append("<table width='100%' border='0' cellspacing='0' cellpadding='0' style='text-align:left; padding:8px 5px 0 5px;'>");
            sb.append("<tr>");
            sb.append("<td width='75' style='line-height:20px; padding:2px 0;' valign='top'><p>预订用户:</p></td>");
            sb.append("<td style='line-height:20px; padding:2px 0;'><p>"+order.get("ordercontact")+ " " + order.get("ordercontactphone") +"</p></td>");
            sb.append("</tr>");

            sb.append("<tr>");
            sb.append("<td style='line-height:20px; padding:2px 0;' valign='top'><p>取&nbsp; 票&nbsp;码:</p></td>");
            sb.append("<td style='line-height:20px; padding:2px 0;'><p>"+order.get("orderid")+"</p></td>");
            sb.append("</tr>");

            sb.append("<tr>");
            sb.append("<td>&nbsp;</td>");
   			/*sb.append("<td style='height:140px;'><img src='"+stringBuffer.toString()+"' style='padding-top:10px;' width='140' /></td>");*/
            sb.append("<td><div id='qrcodeImg2' data-val='"+order.get("orderid")+"' style='padding-top:10px;'></div>");
            sb.append("</tr>");

            sb.append("<tr>");
            sb.append("<td>&nbsp;</td>");
            sb.append("<td style='height:18px;'><p style='font-size:12px;padding-top:10px;'>取票时间:"+DateUtils.parseDateToLongString(new Date())+"</p></td>");
            sb.append("</tr>");

            sb.append("<tr><td>&nbsp;</td><td>&nbsp;</td></tr>");
            sb.append("<tr><td>&nbsp;</td><td>&nbsp;</td></tr>");
            sb.append("</table>");

        }
        System.out.println("sb:"+sb.toString());
        return sb;
    }

    public void updateActPrintTimes(String orderId,Integer times){
        try {
            crtTicketMapper.updateActOrderPrintTimes(orderId,times);
        }catch (Exception e){
            log.error(e.toString());
        }
    }

    public void updateVenPrintTimes(String orderId,Integer times){
        try{
            crtTicketMapper.updateVenOrderPrintTimes(orderId,times);
        }catch (Exception e){
            log.error(e.toString());
        }
    }
}
