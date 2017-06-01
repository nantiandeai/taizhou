package com.creatoo.hn.services.api.user;

import com.creatoo.hn.mapper.*;
import com.creatoo.hn.model.*;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.utils.MD5Util;
import com.creatoo.hn.utils.RegistRandomUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户模块接口服务
 * Created by wangxl on 2017/4/13.
 */
@SuppressWarnings("all")
@Service
public class ApiUserService {
    /**
     * 日志控制器
     */
    Logger log = Logger.getLogger(this.getClass().getName());

    /**
     * 公共服务，用于生成KEY等
     */
    @Autowired
    private CommService commService;

    /**
     * 短信服务
     */
    @Autowired
    private SMSService smsService;

    /**
     * 微信用户DAO
     */
    @Autowired
    private WhgUsrWeixinMapper whgUsrWeixinMapper;

    /**
     * 会员DAO
     */
    @Autowired
    private WhUserMapper whUserMapper;
    
    /**
     * 验证码DAO 
     */
    @Autowired
    private WhCodeMapper whCodeMapper;

    /**
     * 收藏DAO
     */
    @Autowired
    private WhCollectionMapper whCollectionMapper;

    /**
     * 活动DAO
     */
    @Autowired
    private WhgActActivityMapper whgActActivityMapper;

    /**
     * 场馆DAO
     */
    @Autowired
    private WhgVenMapper whgVenMapper;

    /**
     * 活动室DAO
     */
    @Autowired
    private WhgVenRoomMapper whgVenRoomMapper;

    /**
     * 运维分类DAO
     */
    @Autowired
    private WhgYwiTypeMapper whgYwiTypeMapper;

    @Autowired
    private WhCommentMapper whcommMapper;

    /**
     * 绑定手机
     * @param id 微信账号ID whg_usr_xeixin.id
     * @param phone 手机号
     * @return 100-绑定成功；101-手机格式不正确; 102-参数id无效； 103-手机号已经被其它账号绑定; 104-手机号已经被自己绑定
     * @throws Exception
     */
    public String bindPhone(String id, String phone)throws Exception{
        String code = "100";

        //手机格式不正确
        if(phone == null || !phone.matches("^1[3|4|5|7|8][0-9]\\d{8}$")){
            return "101";
        }

        //id有效
        WhgUsrWeixin whgUsrWeixin = whgUsrWeixinMapper.selectByPrimaryKey(id);
        if(whgUsrWeixin == null || whgUsrWeixin.getId() == null){
            return "102";
        }

        //根据phone找到会员
        Example example = new Example(WhUser.class);
        example.createCriteria().andEqualTo("phone", phone);
        List<WhUser> whuserList = this.whUserMapper.selectByExample(example);
        WhUser whUser = null;
        if(whuserList != null && whuserList.size() > 0){
            whUser = whuserList.get(0);
        }

        //存在用户
        if(whUser != null){
            //是否已经绑定
            WhgUsrWeixin userwx = new WhgUsrWeixin();
            userwx.setUserid(whUser.getId());
            userwx = this.whgUsrWeixinMapper.selectOne(userwx);
            if(userwx != null && userwx.getId() != null){
                if(userwx.getId().equals(id)){
                    return "103";//手机号已经被其它微信用户绑定
                }else{
                    return "104";//手机号已经被自己绑定
                }
            }else{//未被绑定-直接绑定到
                whgUsrWeixin.setUserid(whUser.getId());
                Example exa2 = new Example(WhgUsrWeixin.class);
                exa2.createCriteria().andEqualTo("id", id);
                this.whgUsrWeixinMapper.updateByExampleSelective(whgUsrWeixin, exa2);
            }
        }else{//不存在用户
            //插入用户记录
            WhUser whUser1 = new WhUser();
            String password = RegistRandomUtil.random();
            String passwordMD5 = MD5Util.toMd5(password);
            whUser1.setId(commService.getKey("whuser"));
            String nickname = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            whUser1.setPhone(phone);
            whUser1.setNickname(nickname);
            whUser1.setIsrealname(0);
            whUser1.setIsperfect(0);
            whUser1.setIsinner(0);
            whUser1.setPassword( passwordMD5 );
            this.whUserMapper.insert(whUser1);

            //发送短信，告诉密码
            Map<String, String> data = new HashMap<String, String>();
            smsService.t_sendSMS(phone, "LOGIN_PASSWROD", data);

            //修改微信用户表userid
            whgUsrWeixin.setUserid(whUser.getId());
            Example exa2 = new Example(WhgUsrWeixin.class);
            exa2.createCriteria().andEqualTo("id", id);
            this.whgUsrWeixinMapper.updateByExampleSelective(whgUsrWeixin, exa2);
        }

        return code;
    }

    /**
     * 取消绑定手机
     * @param id id 微信账号ID whg_usr_xeixin.id
     * @param phone 手机号
     * @return 100-解绑成功；101-手机格式不正确; 102-参数id无效
     * @throws Exception
     */
    public String unbindPhone(String id, String phone)throws Exception{
        String code = "100";

        //手机格式不正确
        if(phone == null || !phone.matches("^1[3|4|5|7|8][0-9]\\d{8}$")){
            return "101";
        }

        //id有效
        WhgUsrWeixin whgUsrWeixin = whgUsrWeixinMapper.selectByPrimaryKey(id);
        if(whgUsrWeixin == null || whgUsrWeixin.getId() == null){
            return "102";
        }

        //解绑
        whgUsrWeixin.setUserid(null);
        Example exa2 = new Example(WhgUsrWeixin.class);
        exa2.createCriteria().andEqualTo("id", id);
        this.whgUsrWeixinMapper.updateByExample(whgUsrWeixin, exa2);

        return code;
    }

    /**
     * 微信注册
     * @param phone  手机号码
     * @param password  密码
     * @return 100-注册成功；101-手机格式不正确; 102-该号码已经存在
     */
    public String register(String phone, String password) throws Exception {
        String code = "100";
        //手机格式不正确
        if(phone == null || !phone.matches("^1[3|4|5|7|8][0-9]\\d{8}$")){
            return "101";
        }
        //根据phone找到会员
        Example example = new Example(WhUser.class);
        example.createCriteria().andEqualTo("phone", phone);
        List<WhUser> whuserList = this.whUserMapper.selectByExample(example);
        //没有用户
        if(whuserList != null && whuserList.size() > 0){
            //用户已经存在
            return "102";

        }else{
            //插入用户记录
            WhUser whUser = new WhUser();
            whUser.setId(commService.getKey("whuser"));
            String nickname = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            whUser.setPhone(phone);
            whUser.setNickname(nickname);
            whUser.setIsrealname(0);
            whUser.setIsperfect(0);
            whUser.setIsinner(0);
            whUser.setPassword( password);
            this.whUserMapper.insert(whUser);
        }
        return code;
    }

    /**
     * 用户注册
     * @param whUser
     * @return
     */
    public int register(WhUser whUser){
        try {
            whUser.setId(commService.getKey("whuser"));
            this.whUserMapper.insert(whUser);
            return 0;
        }catch (Exception e){
            log.error(e.toString());
            return 1;
        }
    }

    public Map getOneUser(WhUser whUser){
        try {
            List<Map> whUser1 = whUserMapper.getUserByCondition(whUser);
            if(0 == whUser1.size()){
                return null;
            }
            return whUser1.get(0);
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    /**
     * 修改用户资料
     * @param whUser 用户信息
     * @throws Exception
     */
    public void saveUserInfo(WhUser whUser)throws  Exception{
        Example example = new Example(WhUser.class);
        example.createCriteria().andEqualTo("id", whUser.getId());
        this.whUserMapper.updateByExampleSelective(whUser, example);
    }
    
    /**
     * 根据用户预留电话号码获取用户对象
     * @param phone
     * @return
     */
    public WhUser findUser4Phone(String phone){
    	WhUser whUser = new WhUser();
    	whUser.setPhone(phone);
    	return whUserMapper.selectOne(whUser);
    }
    
    /**
     * 根据手机验证码获取对象
     * @param msgContent
     * @return
     */
    public WhCode findWhCode4SmsContent(String msgContent){
    	WhCode whCode = new WhCode();
    	whCode.setMsgcontent(msgContent);
    	return whCodeMapper.selectOne(whCode);
    }

    /**
     * 根据手机验证码获取对象
     * @param msgContent
     * @return
     */
    public WhCode findWhCode4SmsContent(String phone,String code,String sessionId){
        WhCode whCode = new WhCode();
        whCode.setMsgcontent(code);
        whCode.setMsgphone(phone);
        if(sessionId != null && sessionId != ""){
            whCode.setSessid(sessionId);
        }

        return whCodeMapper.selectOne(whCode);
    }

    /**
     * 根据手机验证码获取对象
     * @param phone
     * @param msgContent
     * @return
     */
    public WhCode findWhCode4SmsContent(String phone,String msgContent){
        WhCode whCode = new WhCode();
        whCode.setMsgphone(phone);
        whCode.setMsgcontent(msgContent);
        return whCodeMapper.selectOne(whCode);
    }

    /**
     * 为用户中心页获取活动数量
     * @param userId
     * @return
     */
    public  Integer getActCountForUserCenter(String userId){
        try {
            Integer actCount = whUserMapper.getActCount(userId, 0);
            return actCount;
        }catch (Exception e){
            log.error(e.toString());
            return 0;
        }
    }

    /**
     * 未用户中心页获取场馆数量
     * @param userId
     * @return
     */
    public Integer getVenCountForUserCenter(String userId){
        try {
            Integer venCount = whUserMapper.getVenCount(userId, 0);
            return venCount;
        }catch (Exception e){
            log.error(e.toString());
            return 0;
        }
    }

    /**
     * 查询微信用户
     * @param whgUsrWeixin
     * @return
     */
    public WhgUsrWeixin getWxUser(WhgUsrWeixin whgUsrWeixin){
        try {
            return whgUsrWeixinMapper.selectOne(whgUsrWeixin);
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    /**
     * 保存微信用户
     * @param whgUsrWeixin
     * @return
     */
    public int saveWxUser(WhgUsrWeixin whgUsrWeixin){
        try {
            whgUsrWeixinMapper.updateByPrimaryKey(whgUsrWeixin);
            return 0;
        }catch (Exception e){
            log.error(e.toString());
            return 1;
        }
    }

    public WhUser newOneUserByWxUser(WhgUsrWeixin whgUsrWeixin,String mobile){
        try {
            String newUserId = commService.getKey("wh_user");
            WhUser whUser = new WhUser();
            whUser.setId(newUserId);
            whUser.setName(whgUsrWeixin.getNickname());
            whUser.setNickname(whgUsrWeixin.getNickname());
            whUser.setPhone(mobile);
            whUser.setPassword(MD5Util.toMd5(WhUser.defaultPassWord));//MD5加密
            whUser.setWxopenid(whgUsrWeixin.getOpenid());
            whUser.setHeadurl(whgUsrWeixin.getHeadimgurl());
            whUser.setSex(String.valueOf(whgUsrWeixin.getSex()));
            whUserMapper.insert(whUser);
            whUser.setPassword(WhUser.defaultPassWord);
            whgUsrWeixin.setUserid(whUser.getId());
            whgUsrWeixinMapper.updateByPrimaryKey(whgUsrWeixin);
            return whUser;
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    public WhUser newOneUserByWxUser(String mobile){
        try {
            String newUserId = commService.getKey("wh_user");
            WhUser whUser = new WhUser();
            whUser.setId(newUserId);
            whUser.setName(mobile);
            whUser.setNickname(mobile);
            whUser.setPhone(mobile);
            whUser.setPassword(MD5Util.toMd5(WhUser.defaultPassWord));//MD5加密
            whUserMapper.insert(whUser);
            whUser.setPassword(WhUser.defaultPassWord);
            return whUser;
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    public Map getUserCollection(String userId,String cmreftyp,String page,String rows){
        Map retData = new HashMap();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            int myPage = Integer.valueOf(page);
            int myRows = Integer.valueOf(rows);
            Example example=new Example(WhCollection.class);
            Example.Criteria c = example.createCriteria();
            if(userId!=null || "".equals(userId)){
                c.andEqualTo("cmuid",userId);
            }
            c.andEqualTo("cmopttyp","0");
            c.andEqualTo("cmreftyp", cmreftyp);
            example.setOrderByClause("cmdate desc");
            PageHelper.startPage(myPage, myRows);
            List<WhCollection> list=whCollectionMapper.selectByExample(example);
            List<Map> resList = new ArrayList<Map>();
            for(WhCollection whCollection : list){
                if("2".equals(cmreftyp)){
                    WhgVen whgVen = new WhgVen();
                    whgVen.setId(whCollection.getCmrefid());
                    whgVen = whgVenMapper.selectOne(whgVen);
                    if(null != whgVen){
                        Map map = new HashMap();
                        map.put("id",whgVen.getId());
                        map.put("imgurl",whgVen.getImgurl());
                        map.put("venueName",whgVen.getTitle());
                        map.put("venueAddress",whgVen.getAddress());
                        resList.add(map);
                    }
                }else if("3".equals(cmreftyp)){
                    WhgVenRoom whgVenRoom = new WhgVenRoom();
                    whgVenRoom.setId(whCollection.getCmrefid());
                    whgVenRoom = whgVenRoomMapper.selectOne(whgVenRoom);
                    if(null != whgVenRoom){
                        Map map = new HashMap();
                        map.put("id",whgVenRoom.getId());
                        map.put("imgurl",whgVenRoom.getImgurl());
                        map.put("roomName",whgVenRoom.getTitle());
                        map.put("typeName",getTypeName(whgVenRoom.getEtype()));
                        map.put("sizearea",whgVenRoom.getSizearea());
                        map.put("sizepeople",whgVenRoom.getSizepeople());
                        resList.add(map);
                    }
                }else {
                    WhgActActivity whgActActivity = new WhgActActivity();
                    whgActActivity.setId(whCollection.getCmrefid());
                    whgActActivity = whgActActivityMapper.selectOne(whgActActivity);
                    if(null != whgActActivity){
                        Map map = new HashMap();
                        map.put("id",whgActActivity.getId());
                        map.put("imgurl",whgActActivity.getImgurl());
                        map.put("cmtitle",whgActActivity.getName());
                        map.put("activityAddress",whgActActivity.getAddress());
                        map.put("activityStartTime",sdf.format(whgActActivity.getStarttime()));
                        resList.add(map);
                    }
                }
            }
            PageInfo pageInfo = new PageInfo(list);
            retData.put("pageInfo",pageInfo);
            retData.put("myData",resList);
            return retData;
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    /**
     * 获取分类名
     * @param typeId
     * @return
     */
    private String getTypeName(String typeId){
        try{
            WhgYwiType whgYwiType = new WhgYwiType();
            whgYwiType.setId(typeId);
            whgYwiType = whgYwiTypeMapper.selectOne(whgYwiType);
            if(null == whgYwiType){
                return null;
            }
            return whgYwiType.getName();
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    /**
     * 获取评论
     * @param reftyp
     * @param refid
     * @param page
     * @param rows
     * @return
     */
    public PageInfo getComm(String reftyp, String refid,String page,String rows){
        try {
            int pageNo = Integer.valueOf(page);
            int size = Integer.valueOf(rows);
            PageHelper.startPage(pageNo,size);
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("reftyp", reftyp);
            param.put("refid", refid);
            List list = this.whcommMapper.searchComment(param);
            return new PageInfo(list);
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }
    
    /**
     * 获取我的评论
     * @param whComment
     * @return
     */
    public PageInfo getMyComment(WhComment whComment,String page,String rows){
        try {
            PageHelper.startPage(Integer.valueOf(page),Integer.valueOf(rows));
            List<WhComment> list = whcommMapper.getUserComment(whComment);
            return new PageInfo(list);
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    public List<Map> getExInfoForComment(List list){
        List<Map> res = new ArrayList<Map>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i = 0;i < list.size();i++){
            WhComment whComment = (WhComment) list.get(i);
            if("1".equals(whComment.getRmreftyp())){
                String id = whComment.getRmrefid();
                WhgActActivity whgActActivity = new WhgActActivity();
                whgActActivity.setId(id);
                whgActActivity = whgActActivityMapper.selectOne(whgActActivity);
                if(null != whgActActivity){
                    Map map = new HashMap();
                    map.put("rmurl",whgActActivity.getImgurl());
                    map.put("rmdate",sdf.format(whComment.getRmdate()));
                    map.put("rmcontent",whComment.getRmcontent());
                    map.put("rmtitle",whgActActivity.getName());
                    map.put("address",whgActActivity.getAddress());
                    res.add(map);
                }
            }else if("6".equals(whComment.getRmreftyp())){
                String id = whComment.getRmrefid();
                WhgVen whgVen = new WhgVen();
                whgVen.setId(id);
                whgVen = whgVenMapper.selectOne(whgVen);
                if(null != whgVen){
                    Map map = new HashMap();
                    map.put("rmurl",whgVen.getImgurl());
                    map.put("rmdate",sdf.format(whComment.getRmdate()));
                    map.put("rmcontent",whComment.getRmcontent());
                    map.put("rmtitle",whgVen.getTitle());
                    map.put("address",whgVen.getAddress());
                    res.add(map);
                }
            }
        }

        return res;
    }

    /**
     * 获取用户活动列表
     * @param userId
     * @return
     */
    public PageInfo getActListForUserCenter(String userId,Integer page,Integer rows){

        return null;
    }
}
