package com.creatoo.hn.services.api.ticket;

import com.creatoo.hn.mapper.*;
import com.creatoo.hn.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

/**取票订票服务
 * Created by caiyong on 2017/7/10.
 */
@SuppressWarnings("all")
@Service
public class ApiTicketService {

    private static Logger logger = Logger.getLogger(ApiTicketService.class);

    @Autowired
    private WhgActOrderMapper whgActOrderMapper;

    @Autowired
    private WhgActTicketMapper whgActTicketMapper;

    @Autowired
    private WhgVenRoomOrderMapper whgVenRoomOrderMapper;

    @Autowired
    private WhgActActivityMapper whgActActivityMapper;

    @Autowired
    private WhgActTimeMapper whgActTimeMapper;

    @Autowired
    private WhgVenRoomMapper whgVenRoomMapper;

    @Autowired
    private WhgVenMapper whgVenMapper;

    /**
     * 根据活动订单的取票码获取活动订单
     * @param ticketNo
     * @return
     */
    public WhgActOrder findActOrderByTicketNo(String ticketNo){
        try {
            Example example = new Example(WhgActOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("ordernumber",ticketNo);
            List<WhgActOrder> whgActOrderList = whgActOrderMapper.selectByExample(example);
            if(null == whgActOrderList || whgActOrderList.isEmpty()){
                return null;
            }
            return whgActOrderList.get(0);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 根据活动室订单的取票码获取活动室订单
     * @param ticketNo
     * @return
     */
    public WhgVenRoomOrder findVenRoomOrderByTicketNo(String ticketNo){
        try {
            Example example = new Example(WhgVenRoomOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orderid",ticketNo);
            List<WhgVenRoomOrder> whgVenRoomOrderList = whgVenRoomOrderMapper.selectByExample(example);
            if(null == whgVenRoomOrderList || whgVenRoomOrderList.isEmpty()){
                return null;
            }
            return whgVenRoomOrderList.get(0);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 获取活动信息
     * @param map
     * @return
     */
    public WhgActActivity findActByParam(Map map){
        try {
            Example example = new Example(WhgActActivity.class);
            Example.Criteria criteria = example.createCriteria();
            for(Object key : map.keySet()){
                criteria.andEqualTo((String)key,map.get(key));
            }
            List<WhgActActivity> whgActActivityList = whgActActivityMapper.selectByExample(example);
            if(null == whgActActivityList || whgActActivityList.isEmpty()){
                return null;
            }
            return whgActActivityList.get(0);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 获取活动场次信息
     * @param map
     * @return
     */
    public WhgActTime findActTimeByParam(Map map){
        try {
            Example example = new Example(WhgActTime.class);
            Example.Criteria criteria = example.createCriteria();
            for(Object key : map.keySet()){
                criteria.andEqualTo((String)key,map.get(key));
            }
            List<WhgActTime> whgActTimeList = whgActTimeMapper.selectByExample(example);
            if(null == whgActTimeList || whgActTimeList.isEmpty()){
                return null;
            }
            return whgActTimeList.get(0);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 获取活动票务信息
     * @param map
     * @return
     */
    public List<WhgActTicket> findActTicketByParam(Map map){
        try {
            Example example = new Example(WhgActTicket.class);
            Example.Criteria criteria = example.createCriteria();
            for(Object key : map.keySet()){
                criteria.andEqualTo((String)key,map.get(key));
            }
            List<WhgActTicket> whgActTicketList = whgActTicketMapper.selectByExample(example);
            if(null == whgActTicketList || whgActTicketList.isEmpty()){
                return null;
            }
            return whgActTicketList;
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 获取活动室信息
     * @param map
     * @return
     */
    public WhgVenRoom findVenRoomByParam(Map map){
        try {
            Example example = new Example(WhgVenRoom.class);
            Example.Criteria criteria = example.createCriteria();
            for(Object key : map.keySet()){
                criteria.andEqualTo((String)key,map.get(key));
            }
            List<WhgVenRoom> whgVenRoomList = whgVenRoomMapper.selectByExample(example);
            if(null == whgVenRoomList || whgVenRoomList.isEmpty()){
                return null;
            }
            return whgVenRoomList.get(0);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 修改活动订单状态
     * @param orderId
     * @param ticketStatus
     * @return
     */
    public Integer updateActOrderState(String orderId,Integer ticketStatus){
        try {
            WhgActOrder whgActOrder = new WhgActOrder();
            whgActOrder.setId(orderId);
            whgActOrder = whgActOrderMapper.selectOne(whgActOrder);
            if(null == whgActOrder){
                return 1;
            }
            whgActOrder.setTicketstatus(ticketStatus);
            whgActOrderMapper.updateByPrimaryKey(whgActOrder);
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }

    /**
     * 修改活动室订单状态
     * @param orderId
     * @param state
     * @return
     */
    public Integer updateVenRoomOrderState(String orderId,Integer state){
        try {
            WhgVenRoomOrder whgVenRoomOrder = new WhgVenRoomOrder();
            whgVenRoomOrder.setId(orderId);
            whgVenRoomOrder = whgVenRoomOrderMapper.selectOne(whgVenRoomOrder);
            if(null == whgVenRoomOrder){
                return 1;
            }
            whgVenRoomOrder.setTicketstatus(state);
            whgVenRoomOrderMapper.updateByPrimaryKey(whgVenRoomOrder);
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }
}
