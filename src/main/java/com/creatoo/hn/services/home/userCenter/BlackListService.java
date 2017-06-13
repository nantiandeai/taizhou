package com.creatoo.hn.services.home.userCenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.mapper.WhScanCollectionMapper;
import com.creatoo.hn.mapper.WhgUsrBacklistMapper;
import com.creatoo.hn.model.WhgUsrBacklist;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.utils.SpringContextUtil;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**黑名单服务
 * Created by caiyong on 2017/6/9.
 */
@SuppressWarnings("all")
public class BlackListService {

    private static Logger logger = Logger.getLogger(BlackListService.class);

    private WhScanCollectionMapper whScanCollectionMapper;

    private CommService commService;

    private WhgUsrBacklistMapper whgUsrBacklistMapper;

    private int getCommService(){
        try {
            if(null == commService){
                ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
                commService = applicationContext.getBean(CommService.class);
            }
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }

    private int getWhScanCollectionMapper(){
        try {
            if(null == whScanCollectionMapper){
                ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
                whScanCollectionMapper = applicationContext.getBean(WhScanCollectionMapper.class);
            }
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }

    private int getWhgUsrBacklistMapper(){
        try {
            if(null == whgUsrBacklistMapper){
                ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
                whgUsrBacklistMapper = applicationContext.getBean(WhgUsrBacklistMapper.class);
            }
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }

    /**
     * 执行工作
     */
    public void doTask() throws Exception{
        /**
         * 执行步骤：
         * 1、扫描违规
         * 2、判定违规
         * 3、违规判定处理
         */
        if(0 != getCommService()){
            return;
        }
        if(0 != getWhScanCollectionMapper()){
            return;
        }
        if(0 != getWhgUsrBacklistMapper()){
            return;
        }
        doScan();
    }

    private void doScan() throws  Exception{
        scanByType(1);
        doJudge();
        autoClean();
    }

    /**
     *  按照扫描类型，扫描违反规则的订单
     * @param scanType
     */
    private void scanByType(Integer scanType) throws Exception{
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm:ss");
        switch (scanType){
            case 1:{
                /**
                 * 用户取消的订单
                 */
                List<Map> actIllegalOrder1 = whScanCollectionMapper.getActIllegalOrder(1);
                /**
                 * 用户没有参加的订单
                 */
                List<Map> actIllegalOrder2 = whScanCollectionMapper.getActIllegalOrder(2);
                /**
                 * 存入不良记录
                 */
                for(Map item : actIllegalOrder1){
                    Map map = new HashMap();
                    map.put("id",commService.getKey("wh_scan_collection"));
                    map.put("relid",item.get("id"));
                    map.put("reltype",scanType);
                    map.put("violationtype",1);
                    map.put("recordstate",1);
                    map.put("useid",item.get("userid"));
                    whScanCollectionMapper.addIllegalOrder(map);
                }
                for(Map item : actIllegalOrder2) {
                    Map map = new HashMap();
                    map.put("id",commService.getKey("wh_scan_collection"));
                    map.put("relid",item.get("id"));
                    map.put("reltype",scanType);
                    map.put("violationtype",2);
                    map.put("recordstate",1);
                    map.put("useid",item.get("userid"));
                    whScanCollectionMapper.addIllegalOrder(map);
                }
                break;
            }
            case 2:{
                List<Map> venIllegalOrder = whScanCollectionMapper.getVenIllegalOrder();
                for(Map item : venIllegalOrder){
                    LocalDateTime now = LocalDateTime.now();
                    Date timeDate = (Date) item.get("timeday");
                    Date timeEnd = (Date)item.get("timeend");
                    String dateTimeStr = simpleDateFormat1.format(timeDate) + " " + simpleDateFormat2.format(timeEnd);
                    LocalDateTime compileTime = LocalDateTime.parse(dateTimeStr);
                    if(now.isAfter(compileTime)){
                        Map map = new HashMap();
                        map.put("id",commService.getKey("wh_scan_collection"));
                        map.put("relid",item.get("id"));
                        map.put("reltype",scanType);
                        map.put("violationtype",2);
                        map.put("recordstate",1);
                        map.put("useid",item.get("userid"));
                        whScanCollectionMapper.addIllegalOrder(map);
                    }
                }
                break;
            }
            case 3:{

                break;
            }
            default:{
                break;
            }
        }
    }

    /**
     * LIST转换
     * @param list
     * @param name
     * @return
     */
    private List getArrayList(List list,String name){
        List res = new ArrayList();
        if(null == list || list.isEmpty()){
            return null;
        }
        try {
            for(Object item : list){
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(item));
                Object object = jsonObject.get(name);
                res.add(object);
            }
            return res;
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    private Map getRule(){
        List<Map> list3 = whScanCollectionMapper.getRule();
        if(null == list3 || list3.isEmpty()){
            return null;
        }
        Map rule = list3.get(0);
        return rule;
    }

    private void doJudge() throws Exception{
        /**
         * 获取用户的活动报名违规次数
         */
        List<Map> list1 = whScanCollectionMapper.getWhScanCollectionStatisticsResult(1,1);
        List<Map> list2 = whScanCollectionMapper.getWhScanCollectionStatisticsResult(1,2);
        List<Map> list3 = whScanCollectionMapper.getRule();
        Map rule = getRule();
        for(Map map : list1){
            Integer myCount = Integer.valueOf((String) map.get("myCount"));
            if(null == myCount){
                continue;
            }
            Integer cancelnum = (Integer)rule.get("cancelnum");
            if(cancelnum < myCount){
                WhgUsrBacklist whgUsrBacklist = new WhgUsrBacklist();
                whgUsrBacklist.setId(commService.getKey("whg_usr_backlist"));
                whgUsrBacklist.setUserid((String) map.get("useid"));
                whgUsrBacklist.setState(1);
                whgUsrBacklist.setType(1);
                whgUsrBacklist.setUserphone((String) map.get("phone"));
                whgUsrBacklist.setJointime(new Date());
                whgUsrBacklist.setReltype(1);
                whgUsrBacklistMapper.insert(whgUsrBacklist);
            }
        }
        for (Map map : list2){
            Integer myCount = (Integer) map.get("myCount");
            if(null == myCount){
                continue;
            }
            Integer cancelnum = (Integer)rule.get("missnum");
            if(cancelnum < myCount){
                WhgUsrBacklist whgUsrBacklist = new WhgUsrBacklist();
                whgUsrBacklist.setId(commService.getKey("whg_usr_backlist"));
                whgUsrBacklist.setUserid((String) map.get("useid"));
                whgUsrBacklist.setState(1);
                whgUsrBacklist.setType(2);
                whgUsrBacklist.setUserphone((String) map.get("phone"));
                whgUsrBacklist.setJointime(new Date());
                whgUsrBacklist.setReltype(1);
                whgUsrBacklistMapper.insert(whgUsrBacklist);
            }
        }
    }

    private void autoClean(){
        Map rule = getRule();
        WhgUsrBacklist whgUsrBacklist = new WhgUsrBacklist();
        whgUsrBacklist.setState(1);
        Integer days = 0;
        List<WhgUsrBacklist> list = whgUsrBacklistMapper.select(whgUsrBacklist);
        for(WhgUsrBacklist whgUsrBacklist1 : list){
            Date jointime = whgUsrBacklist1.getJointime();
            if(null == jointime){
                continue;
            }
            LocalDateTime temp = UDateToLocalDateTime(jointime);
            /**
             * 根据违规类型，获取自动取消的天数
             */
            if(1 == whgUsrBacklist1.getType()){
                days = (Integer) rule.get("cancelday");
            }else if(2 == whgUsrBacklist1.getType()){
                days = (Integer) rule.get("missday");
            }
            temp.plusDays(Long.valueOf(String.valueOf(days)));
            /**
             * 如果过了期限，则放开黑名单，并且之前的不良记录一笔勾销
             */
            if(LocalDateTime.now().isAfter(temp)){
                whScanCollectionMapper.updateBlackListState(whgUsrBacklist1.getId(),0);
                whScanCollectionMapper.updateWhScanCollectionState(whgUsrBacklist1.getUserid(),whgUsrBacklist1.getReltype(),whgUsrBacklist1.getType(),2);
            }
        }
    }

    public LocalDateTime UDateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime;
    }
}
