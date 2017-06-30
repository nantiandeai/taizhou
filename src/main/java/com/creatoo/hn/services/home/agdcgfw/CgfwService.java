package com.creatoo.hn.services.home.agdcgfw;

import com.creatoo.hn.ext.bean.RetMobileEntity;
import com.creatoo.hn.ext.bean.RetMobileEntity.Pager;
import com.creatoo.hn.ext.emun.EnumOrderType;
import com.creatoo.hn.mapper.WhCommentMapper;
import com.creatoo.hn.mapper.WhgVenMapper;
import com.creatoo.hn.mapper.WhgVenRoomMapper;
import com.creatoo.hn.mapper.WhgVenRoomOrderMapper;
import com.creatoo.hn.mapper.WhgVenRoomTimeMapper;
import com.creatoo.hn.mapper.home.CrtCgfwMapper;
import com.creatoo.hn.model.*;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiTypeService;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.services.comm.SMSService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 场馆服务
 * @author wangxl
 * @version 2016.11.16
 */
@SuppressWarnings("all")
@Service
public class CgfwService {
	Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private CommService commService;

    @Autowired
    private CrtCgfwMapper crtCgfwMapper;
    @Autowired
    private WhgVenMapper whgVenMapper;
    @Autowired
    private WhgVenRoomMapper whgVenRoomMapper;
    @Autowired
    private WhgVenRoomTimeMapper whgVenRoomTimeMapper;
    @Autowired
    private WhgVenRoomOrderMapper whgVenRoomOrderMapper;
    
    @Autowired
    private WhCommentMapper whCommentMapper;

    @Autowired
    private SMSService smsService;

    //设备类型
    @Autowired
    private WhgYunweiTypeService whgYunweiTypeService;


    /**
	 * 分页查询场馆信息
	 * @param param
	 * @return
	 */
    public Map<String, Object> findList(Map<String, Object> param)throws Exception {
        //分页信息
        int page = Integer.parseInt(param.get("page").toString());
        int rows = Integer.parseInt(param.get("rows").toString());
        //带条件的分页查询
        PageHelper.startPage(page, rows);
        List<Map> list = this.crtCgfwMapper.selectVenList(param);
        PageInfo<Map> pageInfo = new PageInfo<Map>(list);
        List<Map> rowsList = pageInfo.getList();

        Map<String, Object> rtnMap = new HashMap<String, Object>();
        Pager pager = new RetMobileEntity.Pager();
		pager.setCount(rowsList.size());
		pager.setIndex(Integer.parseInt(param.get("page").toString()));
		pager.setSize(Integer.parseInt(param.get("rows").toString()));
		pager.setTotal(Integer.parseInt(String.valueOf(pageInfo.getTotal())));
        rtnMap.put("total", pageInfo.getTotal());
        rtnMap.put("rows", rowsList);
        rtnMap.put("pager", pager);
        rtnMap.put("date", new Date());
        return rtnMap;
    }

    /**
     * 分页查询场馆信息
     * @param param
     * @return
     */
    public Map<String, Object> findListforweb(Map<String, Object> param)throws Exception {
        //分页信息
        int page = Integer.parseInt(param.get("page").toString());
        int rows = Integer.parseInt(param.get("rows").toString());
        //带条件的分页查询
        PageHelper.startPage(page, rows);
        List<Map> list = this.crtCgfwMapper.selectVenListWeb(param);
        PageInfo<Map> pageInfo = new PageInfo<Map>(list);
        List<Map> rowsList = pageInfo.getList();

        Map<String, Object> rtnMap = new HashMap<String, Object>();
        Pager pager = new RetMobileEntity.Pager();
        pager.setCount(rowsList.size());
        pager.setIndex(Integer.parseInt(param.get("page").toString()));
        pager.setSize(Integer.parseInt(param.get("rows").toString()));
        pager.setTotal(Integer.parseInt(String.valueOf(pageInfo.getTotal())));
        rtnMap.put("total", pageInfo.getTotal());
        rtnMap.put("rows", rowsList);
        rtnMap.put("pager", pager);
        rtnMap.put("date", new Date());
        return rtnMap;
    }
    /**
     * 查询指定ID场馆
     * @param venid
     * @return
     * @throws Exception
     */
    public WhgVen findWhgVen4Id(String venid) throws Exception{
        return this.whgVenMapper.selectByPrimaryKey(venid);
    }

    
    /**
     * 取推荐场馆列表
     * @param size
     * @return
     * @throws Exception
     */
    public List selectWhgVen4Recommend(Integer size, String notId) throws Exception{
        WhgVen ven = new WhgVen();
        ven.setDelstate(0);
        ven.setState(6);
        ven.setRecommend(1);
        size = size == null ? 4 : size;
        Example example = new Example(WhgVen.class);
        Example.Criteria c = example.createCriteria().andEqualTo(ven);
        if (notId!=null){
            c.andNotEqualTo("id", notId);
        }
        example.orderBy("statemdfdate").desc();
        return this.whgVenMapper.selectByExampleAndRowBounds(example, new RowBounds(0,size));
    }

    /**
     * 查相关场馆的活动室列表
     * @param venid
     * @return
     * @throws Exception
     */
    public List selectWhgVenroom4Ven(String venid) throws Exception{
        WhgVenRoom venRoom = new WhgVenRoom();
        venRoom.setVenid(venid);
        venRoom.setDelstate(0);
        venRoom.setState(6);

        Example example = new Example(WhgVenRoom.class);
        example.createCriteria().andEqualTo(venRoom);
        example.orderBy("statemdfdate").desc();
        List<WhgVenRoom> list = this.whgVenRoomMapper.selectByExample(example);
         return list;
    }

    /**
     * 查询推荐活动室
     * @param venid
     * @return
     * @throws Exception
     */
    public List selectVenroomtj(String roomId,String venid) throws Exception{
        WhgVenRoom venRoom = new WhgVenRoom();
//        venRoom.setVenid(venid);
        venRoom.setDelstate(0);
        venRoom.setState(6);
        venRoom.setRecommend(1);

        Example example = new Example(WhgVenRoom.class);
        example.createCriteria().andEqualTo(venRoom).andNotEqualTo("id",roomId);
        example.orderBy("statemdfdate").desc();
        List<WhgVenRoom> list = this.whgVenRoomMapper.selectByExample(example);
        return list;
    }


    /**
     * 查询指定ID的活动室
     * @param roomid
     * @return
     * @throws Exception
     */
    public WhgVenRoom findWhgVenroom4Id(String roomid) throws Exception{
        return this.whgVenRoomMapper.selectByPrimaryKey(roomid);
    }

    /**
     * 查活动室开放预订时段在当前时间后的个数
     * @param roomid
     * @return
     * @throws Exception
     */
    public int countRoomTimeOpen4Room(String roomid) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String timedayStr = sdf.format(new Date());
        Example example = new Example(WhgVenRoomTime.class);
        example.createCriteria()
                .andEqualTo("state", 1)
                .andEqualTo("roomid", roomid)
                .andGreaterThanOrEqualTo("timeday", sdf.parse(timedayStr));
        int count = this.whgVenRoomTimeMapper.selectCountByExample(example);
        return count;
    }

    /**
     * 活动室开放预订的可用时间
     * @param roomid
     * @return
     * @throws Exception
     */
    public Map<String, Object> roomTimeOpenSEday(String roomid) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String timedayStr = sdf.format(new Date());
        Example example = new Example(WhgVenRoomTime.class);
        example.createCriteria()
                .andEqualTo("state", 1)
                .andEqualTo("roomid", roomid)
                .andGreaterThanOrEqualTo("timeday", sdf.parse(timedayStr));
        example.orderBy("timeday").asc();

        Map<String, Object> rest = new HashMap();
        List<WhgVenRoomTime> times = this.whgVenRoomTimeMapper.selectByExampleAndRowBounds(example, new RowBounds(0,1));
        if (times!=null && times.size()>0){
            WhgVenRoomTime time = times.get(0);
            rest.put("beginDay", time.getTimeday());
        }

        example.clear();
        example.createCriteria()
                .andEqualTo("state", 1)
                .andEqualTo("roomid", roomid)
                .andGreaterThanOrEqualTo("timeday", sdf.parse(timedayStr));
        example.orderBy("timeday").desc();

        times = this.whgVenRoomTimeMapper.selectByExampleAndRowBounds(example, new RowBounds(0,1));
        if (times!=null && times.size()>0){
            WhgVenRoomTime time = times.get(0);
            rest.put("endDay", time.getTimeday());
        }
        return rest;
    }

    /**
     * 查询指定活动室的相关活动室列表
     * @param room
     * @return
     * @throws Exception
     */
    public List selectWhgVenroom4Room(WhgVenRoom room) throws Exception{
        Example example = new Example(WhgVenRoom.class);
        example.createCriteria().andEqualTo("venid", room.getVenid())
                .andEqualTo("state", 6)
                .andEqualTo("delstate", 0)
                .andNotEqualTo("id", room.getId());
        example.orderBy("statemdfdate").desc();

        return this.whgVenRoomMapper.selectByExample(example);
    }

    /**
     * 查时段内的活动室预定信息
     * @param bday
     * @param eday
     * @return
     * @throws Exception
     */
    public List selectWhgVenroomtime(String roomid, Date bday, Date eday) throws Exception{
        Example example = new Example(WhgVenRoomTime.class);
        example.createCriteria()
                .andEqualTo("roomid", roomid)
                .andGreaterThanOrEqualTo("timeday", bday)
                .andLessThanOrEqualTo("timeday", eday);
        example.setOrderByClause("timeday asc, timestart asc");
        return this.whgVenRoomTimeMapper.selectByExample(example);
    }
    
    /**
     * 查时段内的活动室成功预订信息
     * @param roomid
     * @param bday
     * @param eday
     * @return
     * @throws Exception
     */
    public List selectWhgVenroomorder4OK(String roomid, Date bday, Date eday) throws Exception{
        Example example = new Example(WhgVenRoomOrder.class);
        example.createCriteria()
                .andEqualTo("roomid", roomid)
                .andEqualTo("state", 3)
                .andGreaterThanOrEqualTo("timeday", bday)
                .andLessThanOrEqualTo("timeday", eday);
        example.setOrderByClause("timeday asc, timestart asc");
        return this.whgVenRoomOrderMapper.selectByExample(example);
    }

    /**
     * 查时段内的活动室 用户ID 的预订申请
     * @param roomid
     * @param bday
     * @param eday
     * @param userid
     * @return
     * @throws Exception
     */
    public List selectWhgVenroomorder4User(String roomid, Date bday, Date eday, String userid) throws Exception{
        Example example = new Example(WhgVenRoomOrder.class);
        example.createCriteria()
                .andEqualTo("roomid", roomid)
                .andEqualTo("userid", userid)
                .andEqualTo("state", 0)
                .andGreaterThanOrEqualTo("timeday", bday)
                .andLessThanOrEqualTo("timeday", eday);
        example.setOrderByClause("timeday asc, timestart asc");
        return this.whgVenRoomOrderMapper.selectByExample(example);
    }

    /**
     * 查询指定ID的预订时段信息
     * @param id
     * @return
     * @throws Exception
     */
    public WhgVenRoomTime findWhgVenroomtime4Id(String id) throws Exception{
        return this.whgVenRoomTimeMapper.selectByPrimaryKey(id);
    }
    
    /**
     * 查询某天的所有场次
     * @param roomId
     * @param timeday
     * @return
     */
    public List<WhgVenRoomTime> findWhgVenRoomTime4IdAndDate(String roomId,Date timeday){
    	WhgVenRoomTime wvrt = new WhgVenRoomTime();
    	wvrt.setRoomid(roomId);
    	wvrt.setState(1);
    	wvrt.setTimeday(timeday);
    	return whgVenRoomTimeMapper.select(wvrt);
    }

    /**
     * 保存用户预订时段
     * @param roomtimeid
     * @param userphone
     * @param user
     * @throws Exception
     */
    public WhgVenRoomOrder saveUserVenroomtimeInfo(String roomtimeid, WhUser user, String userphone, String username, String purpose) throws Exception{

        if (userphone == null || !userphone.trim().matches("^1[3-8]+\\d{9}$")){
            throw new Exception("errcode1101 ### 手机号码格式不正确");
        }
        if (username==null || username.isEmpty() || username.trim().length()>20){
            throw new Exception("errcode1102 ### 预订人数据格式不正确");
        }
        if (purpose != null && purpose.replaceAll("\r\n","").length() > 200){
            throw new Exception("errcode1103 ### 预订用途输入过长");
        }

        //调用验证
        testUserVenroomtimeInfo(user, roomtimeid, userphone, username, purpose);

        WhgVenRoomTime time = this.findWhgVenroomtime4Id(roomtimeid);

        WhgVenRoomOrder order = new WhgVenRoomOrder();
        order.setId(this.commService.getKey("WhgVenRoomOrder"));
        order.setOrderid(this.commService.getOrderId(EnumOrderType.ORDER_VEN.getValue()));

        order.setRoomid(time.getRoomid());
        order.setTimeday(time.getTimeday());
        order.setTimestart(time.getTimestart());
        order.setTimeend(time.getTimeend());
        order.setTimelong(time.getTimelong());
        order.setState(0);

        order.setCrtdate(new Date());
        order.setUserid(user.getId());
        order.setOrdercontactphone(userphone);
        order.setOrdercontact(username);

        order.setTicketstatus(1);
        order.setPrinttickettimes(0);
        order.setTicketcheckstate(1);

        order.setPurpose(purpose);

        this.whgVenRoomOrderMapper.insert(order);

        try {
            WhgVenRoom room = this.findWhgVenroom4Id(order.getRoomid());
            Map<String, String> data = new HashMap<>();
            data.put("userName", order.getOrdercontact());
            data.put("title", room.getTitle());
            data.put("orderNum", order.getOrderid());
            //this.smsService.t_sendSMS(order.getOrdercontactphone(), "VEN_ORDER_ADD", data);
            if (room.getHasfees()!=null && room.getHasfees().intValue() == 1) {
                this.smsService.t_sendSMS(order.getOrdercontactphone(), "VEN_ORDER_ADD_CHARGE", data, room.getId());
            } else {
                this.smsService.t_sendSMS(order.getOrdercontactphone(), "VEN_ORDER_ADD", data, room.getId());
            }
        } catch (Exception e) {
            log.error("roomOrderAdd sendSMS error", e);
        }

        return order;

    }

    /**
     * 验证用户预订时段
     */
    public void testUserVenroomtimeInfo(WhUser user, String roomtimeid, String userphone, String username, String purpose) throws Exception{
        WhgVenRoomTime time = this.findWhgVenroomtime4Id(roomtimeid);
        WhgVenRoom room = this.findWhgVenroom4Id(time.getRoomid());
        WhgVen ven = this.findWhgVen4Id(room.getVenid());

        //是否有不存在的或不可用的关连
        if(room==null || room.getDelstate().compareTo(new Integer(0))!=0 || room.getState().compareTo(new Integer(6))!=0){
            throw new Exception("errcode1001 ### 申请的活动室状态不可用");
        }
        if(ven==null || ven.getDelstate().compareTo(new Integer(0))!=0 || ven.getState().compareTo(new Integer(6))!=0){
            throw new Exception("errcode1001 ### 申请的活动室所属场馆状态不可用");
        }
        if(time==null || time.getState().compareTo(new Integer(1))!=0){
            throw new Exception("errcode1001 ### 申请的活动室所选开放时段已关闭预订");
        }

        //用户信息可用性
        if(user == null){
            throw new Exception("errcode1002 ### 预订用户信息已失效，请重新登录");
        }

        //活动室时段是否已被预定成功
        WhgVenRoomOrder order = new WhgVenRoomOrder();
        order.setRoomid(time.getRoomid());
        order.setTimeday(time.getTimeday());
        order.setTimestart(time.getTimestart());
        order.setTimeend(time.getTimeend());
        order.setState(3);
        int contState3 = this.whgVenRoomOrderMapper.selectCount(order);
        if (contState3 > 0){
            throw new Exception("errcode1003 ### 申请的活动室已被其他用户预定");
        }
        //活动室时段是否已被相同的用户或联系手机重复预订
        order.setState(null);
        Example example = new Example(WhgVenRoomOrder.class);
        example.or().andEqualTo(order).andEqualTo("userid", user.getId()).andIn("state", Arrays.asList(0,3));
        if (userphone != null) {
            example.or().andEqualTo(order).andEqualTo("ordercontactphone", userphone).andIn("state", Arrays.asList(0,3));
        }
        int contUser = this.whgVenRoomOrderMapper.selectCountByExample(example);
        if (contUser > 0){
            throw new Exception("errcode1004 ### 重复申请了相同活动室开放时段，请查看已申请的预订");
        }

    }
    
    
    public Map<String, Object> praseTestError(String error){
        Map<String, Object> rest = new HashMap<String, Object>();
        rest.put("code", 9999);
        rest.put("errmsg", "操作失败");
        if(error==null || !error.startsWith("errcode")){
            return rest;
        }

        String[] errInfo = error.split("\\s*###\\s*");
        if (errInfo.length == 2){
            rest.put("code", errInfo[0].replace("errcode", ""));
            rest.put("errmsg", errInfo[1]);
        }
        return rest;
    }
}
