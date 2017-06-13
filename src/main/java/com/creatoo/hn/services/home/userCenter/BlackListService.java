package com.creatoo.hn.services.home.userCenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.mapper.WhScanCollectionMapper;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.utils.SpringContextUtil;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**黑名单服务
 * Created by caiyong on 2017/6/9.
 */
@SuppressWarnings("all")
public class BlackListService {

    private static Logger logger = Logger.getLogger(BlackListService.class);

    private WhScanCollectionMapper whScanCollectionMapper;

    private CommService commService;

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
        doScan();
    }

    private void doScan() throws  Exception{
        scanByType(1);
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

    private void doJudge(){

    }

    private void doJudgeHandle(){

    }

}
