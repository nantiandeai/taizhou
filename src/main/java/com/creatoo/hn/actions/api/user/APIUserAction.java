package com.creatoo.hn.actions.api.user;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.actions.home.userCenter.UserCenterAction;
import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.ext.bean.RetMobileEntity;
import com.creatoo.hn.ext.bean.RetMobileEntity.Pager;
import com.creatoo.hn.model.*;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiTagService;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiTypeService;
import com.creatoo.hn.services.api.user.ApiUserService;
import com.creatoo.hn.services.comm.CommPropertiesService;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.services.home.agdwhhd.WhhdService;
import com.creatoo.hn.services.home.user.RegistService;
import com.creatoo.hn.services.home.userCenter.*;
import com.creatoo.hn.utils.RegistRandomUtil;
import com.creatoo.hn.utils.ReqParamsUtil;
import com.creatoo.hn.utils.WhConstance;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


/**
 * 用户个人中心修改
 * Created by wangxl on 2017/4/12.
 */
@RestController
@RequestMapping("/api/user")
public class APIUserAction {
    /**
     * 日志控制器
     */
    Logger log = Logger.getLogger(this.getClass().getName());

    /**
     * 用户接口服务
     */
    @Autowired
    private ApiUserService apiUserService;

    @Autowired
    private UserCenterService userCenterService;

    /**
     * 短信服务
     */
    @Autowired
    private SMSService smsService;

    @Autowired
    public CommService commService;

    /**
     * 属性文件读取BEAN
     */
    @Autowired
    private CommPropertiesService commPropertiesService;

    @Autowired
    public RegistService regService;

    @Autowired
    private VenueOrderService service;

    @Autowired
    private CollectionService colleService;

    @Autowired
    private UserCenterAction centerAction;

    @Autowired
    private CommentService commentSerice;

    @Autowired
    private WhhdService whhdService;

    @Autowired
    private UserRealService realService;

    @Autowired
    private WhgYunweiTypeService whgYunweiTypeService;

    @Autowired
    private WhgYunweiTagService whgYunweiTagService;
    /**
     * 发送手机验证码
     * @param request
     * @return
     */
    @SuppressWarnings("all")
    @CrossOrigin
    @RequestMapping(value = "sendSmsCode",method = RequestMethod.POST)
    public RetMobileEntity sendSmsCode(HttpServletRequest request){
        RetMobileEntity retMobileEntity = new RetMobileEntity();
        String type = getParam(request,"type");
        String mobile = getParam(request,"mobile");
        String sessionId = request.getSession().getId();
        if(null == type || null == mobile){
            retMobileEntity.setCode(1);
            retMobileEntity.setMsg("发送短信失败");
            return retMobileEntity;
        }
        int canSend = canSend(mobile);
        if(1 == canSend){
            retMobileEntity.setCode(1);
            retMobileEntity.setMsg("验证码一天最多发10次");
            return retMobileEntity;
        }
        if(2 == canSend){
            retMobileEntity.setCode(1);
            retMobileEntity.setMsg("120秒后重新发送验证码");
            return retMobileEntity;
        }
        if(-1 == canSend){
            retMobileEntity.setCode(1);
            retMobileEntity.setMsg("发送短信失败");
            return retMobileEntity;
        }
        /**
         * typ1:1:忘记密码;2:解绑;3:票务;4:注册;5:手机绑定
         */
        if("1".equals(type)){
            WhUser whUser = new WhUser();
            whUser.setPhone(mobile);
            Map map = apiUserService.getOneUser(whUser);
            if(null == map){
                retMobileEntity.setCode(1);
                retMobileEntity.setMsg("您输入的手机用户不存在");
                return retMobileEntity;
            }
            if(!doSendSms(mobile,sessionId)){
                retMobileEntity.setCode(1);
                retMobileEntity.setMsg("发送短信失败");
                return retMobileEntity;
            }
            retMobileEntity.setCode(0);
            map.put("sessionId",sessionId);
            retMobileEntity.setData(map);
            return retMobileEntity;
        }
        if("2".equals(type)){

        }
        if("3".equals(type)){

        }
        if("4".equals(type)){
            if(!doSendSms(mobile,sessionId)){
                retMobileEntity.setCode(1);
                retMobileEntity.setMsg("发送短信失败");
                return retMobileEntity;
            }
            Map map = new HashMap();
            map.put("sessionId",sessionId);
            retMobileEntity.setData(map);
            return retMobileEntity;
        }
        if("5".equals(type)){
            WhUser whUser = new WhUser();
            whUser.setPhone(mobile);
            Map res = apiUserService.getOneUser(whUser);
            if(null != res){
                retMobileEntity.setCode(1);
                retMobileEntity.setMsg("该手机号已注册");
                return retMobileEntity;
            }
            if(!doSendSms(mobile,sessionId)){
                retMobileEntity.setCode(1);
                retMobileEntity.setMsg("发送短信失败");
                return retMobileEntity;
            }
            Map map = new HashMap();
            map.put("sessionId",sessionId);
            retMobileEntity.setData(map);
            return retMobileEntity;
        }
        retMobileEntity.setCode(1);
        retMobileEntity.setMsg("业务缺失");
        return retMobileEntity;
    }

    /**
     * 发送短信
     * @param mobile
     * @param sessionId
     * @return
     */
    private boolean doSendSms(String mobile,String sessionId){
        //1.生成随机验证码
        try {
            RegistRandomUtil randomUtil = new RegistRandomUtil();
            String msgcontent = randomUtil.random();
            //2.将短信发送至手机
            Map<String, String> smsData = new HashMap<String, String>();
            smsData.put("validCode", msgcontent);
            smsService.t_sendSMS(mobile, "LOGIN_VALIDCODE", smsData, mobile);
            //将数据保存至code表
            smsService.insertWhCode(mobile,msgcontent,sessionId);
            return true;
        }catch (Exception e){
            log.error(e.toString());
            return false;
        }
    }

    /**
     * 判断能否发送短信
     * @param mobile
     * @return 0:可以发送;1:验证码一天最多发10次;2:120秒后重新发送验证码;-1:发生异常
     */
    private int canSend(String mobile){
        Date now = new Date();
        try{
            //获取手机发送记录
            List<WhCode> phoneCodeList = this.regService.getPhoneTime(mobile, now);
            //判断手机发送记录列表是否为空
            if(null == phoneCodeList || 0 == phoneCodeList.size()){
                return 0;
            }
            Date phonetime = phoneCodeList.get(0).getMsgtime();
            if (phoneCodeList.size() >= 10) {
                //验证码一天最多发10次
                return 1;
            } else if (((now.getTime() - phonetime.getTime()) / 1000) < 120) {
                //120秒后重新发送验证码
                return 2;
            } else {
                return 0;
            }
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 短信验证校验
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/checkSmsCodeForForgetPwd",method = RequestMethod.POST)
    public RetMobileEntity checkSmsCodeForForgetPwd(HttpServletRequest request){
        RetMobileEntity retMobileEntity = new RetMobileEntity();
        String userId = getParam(request,"myUserId");
        String smsCode = getParam(request,"smsCode");
        String mobile = getParam(request,"mobile");
        String lastSend = getParam(request,"lastSend");
        if(null == userId || null == smsCode || null == mobile || null == lastSend){
            retMobileEntity.setCode(1);
            retMobileEntity.setMsg("短信验证码校验失败");
            return retMobileEntity;
        }
        try {
            WhCode whCode = apiUserService.findWhCode4SmsContent(mobile,smsCode,lastSend);
            if(null == whCode){
                retMobileEntity.setCode(1);
                retMobileEntity.setMsg("短信验证码校验失败");
                return retMobileEntity;
            }
            Map map = new HashMap();
            map.put("myUserId",userId);
            map.put("mobile",mobile);
            retMobileEntity.setCode(0);
            retMobileEntity.setData(map);
            return retMobileEntity;
        }catch (Exception e){
            log.error(e.toString());
            retMobileEntity.setCode(1);
            retMobileEntity.setMsg("短信验证码校验失败");
            return retMobileEntity;
        }
    }

    /**
     * 绑定手机号
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/bindMobile",method = RequestMethod.POST)
    public ResponseBean bindMobile(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String mobile = getParam(request,"mobile");
        String code = getParam(request,"code");
        String lastSend = getParam(request,"lastSend");
        String wxoid = getParam(request,"wxoid");
        String myUserId = getParam(request,"userId");
        try {
            WhCode whCode = apiUserService.findWhCode4SmsContent(mobile,code,lastSend);
            if(null == whCode){
                responseBean.setCode("101");
                responseBean.setErrormsg("短信验证码校验失败");
                return responseBean;
            }
            if(null != myUserId){
                WhUser whUser = new WhUser();
                whUser.setId(myUserId);
                whUser.setPhone(mobile);
                apiUserService.saveUserInfo(whUser);
                responseBean.setCode("0");
                return responseBean;
            }
            WhgUsrWeixin whgUsrWeixin = new WhgUsrWeixin();
            whgUsrWeixin.setOpenid(wxoid);
            whgUsrWeixin = apiUserService.getWxUser(whgUsrWeixin);
            if(null == whgUsrWeixin){
                responseBean.setCode("101");
                responseBean.setErrormsg("手机绑定失败");
                return responseBean;
            }
            WhUser whUser = new WhUser();
            whUser.setPhone(mobile);
            Map res = apiUserService.getOneUser(whUser);
            if(null != res){
                //如果改手机好存在
                whgUsrWeixin.setUserid((String) res.get("id"));
                if(0 != apiUserService.saveWxUser(whgUsrWeixin)){
                    responseBean.setCode("101");
                    responseBean.setErrormsg("手机绑定失败");
                    return responseBean;
                }
            }else {
                WhUser newWhUser = apiUserService.newOneUserByWxUser(whgUsrWeixin,mobile);
                Map<String, String> smsData = new HashMap<String, String>();
                smsData.put("userName", newWhUser.getName());
                smsData.put("password", newWhUser.getPassword());
                smsService.t_sendSMS(mobile, "LOGIN_PASSWROD", smsData,mobile);
                responseBean.setData(newWhUser);
            }
        }catch (Exception e){
            log.error(e.toString());
            responseBean.setCode("101");
            responseBean.setErrormsg("短信验证码校验失败");
            return responseBean;
        }
        return responseBean;
    }

    /**
     * 手机注册
     * @param request
     * @return
     */
    @SuppressWarnings("all")
    @CrossOrigin
    @RequestMapping(value = "/mobileRegist",method = RequestMethod.POST)
    public RetMobileEntity mobileRegist(HttpServletRequest request){
        RetMobileEntity retMobileEntity = new RetMobileEntity();
        String mobile = getParam(request,"mobile");
        String code = getParam(request,"code");
        String lastSend = getParam(request,"lastSend");
        try {
            WhCode whCode = apiUserService.findWhCode4SmsContent(mobile,code,lastSend);
            if(null == whCode){
                retMobileEntity.setCode(1);
                retMobileEntity.setMsg("短信验证码校验失败");
                return retMobileEntity;
            }
            WhUser searchRes = new WhUser();
            searchRes.setPhone(mobile);
            Map res = apiUserService.getOneUser(searchRes);
            if(null != res){
                retMobileEntity.setCode(0);
                retMobileEntity.setMsg("该手机号已经注册过了");
                retMobileEntity.setData(res);
                return retMobileEntity;
            }
            WhUser newWhUser = apiUserService.newOneUserByWxUser(mobile);
            Map<String, String> smsData = new HashMap<String, String>();
            smsData.put("userName", newWhUser.getName());
            smsData.put("password", newWhUser.getPassword());
            smsService.t_sendSMS(mobile, "LOGIN_PASSWROD", smsData,mobile);
            retMobileEntity.setCode(0);
            retMobileEntity.setMsg("注册成功");
            retMobileEntity.setData(newWhUser);
            return retMobileEntity;
        }catch (Exception e){
            log.error(e.toString());
            retMobileEntity.setCode(1);
            retMobileEntity.setMsg("手机注册失败");
            return retMobileEntity;
        }
    }

    /**
     * 用户注册
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping("/register")
    public ResponseBean apiRegister(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String mobile = request.getParameter("mobile");
        String code = request.getParameter("code");
        String password = request.getParameter("password");
        String sex = request.getParameter("sex");
        String birthday = request.getParameter("birthday");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            WhCode whCode = apiUserService.findWhCode4SmsContent(mobile,code);
            if(null == whCode){
                responseBean.setSuccess("101");
                responseBean.setErrormsg("短信验证失败");
                return responseBean;
            }
            WhUser whUser = apiUserService.findUser4Phone(mobile);
            if(null != whUser){
                responseBean.setSuccess("102");
                responseBean.setErrormsg("该手机用户已存在");
                return responseBean;
            }
            if(mobile == null || !mobile.matches("^1[3|4|5|7|8][0-9]\\d{8}$")){
                responseBean.setSuccess("103");
                responseBean.setErrormsg("手机号格式不正确");
                return responseBean;
            }
            WhUser whUser1 = new WhUser();
            whUser1.setPhone(mobile);
            whUser1.setName(mobile);
            whUser1.setNickname(mobile);
            whUser1.setPassword(password);
            whUser1.setSex(sex);
            whUser1.setBirthday(sdf.parse(birthday));
            whUser1.setRegisttime(new Date());
            if(0 != apiUserService.register(whUser1)){
                responseBean.setSuccess("104");
                responseBean.setErrormsg("注册失败");
                return responseBean;
            }
            return responseBean;
        }catch (Exception e){
            log.error(e.toString());
            responseBean.setSuccess("104");
            responseBean.setErrormsg("注册失败");
            return responseBean;
        }
    }

    /**
     * 用户登录（手机号登录）
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value="/doLogin",method = RequestMethod.POST)
    public ResponseBean doLogin(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        if(null == userName || null == password){
            responseBean.setCode("1");
            responseBean.setErrormsg("用户名或密码错误");
            return responseBean;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        WhUser whUser = new WhUser();
        whUser.setPhone(userName);
        //whUser.setPassword(password);
        Map findResult = null;
        try {
            findResult = apiUserService.getOneUser(whUser);
            if(null == findResult){
                responseBean.setCode("1");
                responseBean.setErrormsg("用户名或密码错误");
                return responseBean;
            }
            if(userName.equals(findResult.get("phone")) && password.equals(findResult.get("password"))){
                Map map = new HashMap();
                map.put("userId",findResult.get("id"));
                map.put("userName",findResult.get("name"));
                map.put("mobile",findResult.get("phone"));
                map.put("sex",findResult.get("sex"));
                map.put("birthday",findResult.get("birthday"));
                map.put("nickName",findResult.get("nickname"));
                map.put("authState",findResult.get("isrealname"));
                map.put("userHeadImgUrl",findResult.get("headurl"));
                map.put("staticServerUrl",commPropertiesService.getUploadLocalServerAddr());
                responseBean.setData(map);

                //插入用户登录时间信息
                try {
                    String logintimeId = this.commService.getKey("whg_rep_login");
                    WhgRepLogin whgRepLogin = new WhgRepLogin();
                    whgRepLogin.setId(logintimeId);
                    whgRepLogin.setDevtype(0);
                    whgRepLogin.setLogintime(new Date());
                    whgRepLogin.setUserid((String)findResult.get("id"));
                    this.commService.insertLoginTime(whgRepLogin);
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            } else {
                responseBean.setCode("1");
                responseBean.setErrormsg("用户名或密码错误");
            }
            return responseBean;
        }catch (Exception e){
            log.error(e.toString());
            responseBean.setCode("2");
            responseBean.setErrormsg("登录失败");
            return responseBean;
        }
    }

    /**
     * 关联手机
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping("/relationMobile")
    public ResponseBean relationMobile(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String userId = request.getParameter("userId");
        String mobile = request.getParameter("mobile");
        if(null == userId){
            responseBean.setCode("1");
            responseBean.setErrormsg("绑定失败");
            return responseBean;
        }
        if(null == mobile){
            responseBean.setCode("1");
            responseBean.setErrormsg("绑定失败");
            return responseBean;
        }
        if(!mobile.matches("^1[3|4|5|7|8][0-9]\\d{8}$")){
            responseBean.setCode("3");
            responseBean.setErrormsg("手机格式错误");
            return responseBean;
        }
        WhUser whUser = new WhUser();
        whUser.setPhone(mobile);
        Map findResult = null;
        findResult = apiUserService.getOneUser(whUser);
        if(null != findResult){
            responseBean.setCode("2");
            responseBean.setErrormsg("该手机号已存在");
            return responseBean;
        }
        whUser.setId(userId);
        try {
            apiUserService.saveUserInfo(whUser);
            return responseBean;
        }catch (Exception e){
            log.error(e.toString());
            responseBean.setCode("1");
            responseBean.setErrormsg("绑定失败");
            return responseBean;
        }
    }

    /**
     * 找回密码
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/setPasswd",method = RequestMethod.POST)
    public ResponseBean setPasswd(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String mobile = request.getParameter("mobile");
        String code = request.getParameter("code");
        String password = request.getParameter("password");
        if(null == mobile || null == code){
            responseBean.setSuccess("101");
            responseBean.setErrormsg("手机号或验证码不能为空");
            return responseBean;
        }
        WhCode whCode = apiUserService.findWhCode4SmsContent(mobile,code);
        if(null == whCode){
            responseBean.setSuccess("102");
            responseBean.setErrormsg("验证码校验错误");
            return responseBean;
        }
        WhUser whUser = new WhUser();
        whUser.setPhone(mobile);
        Map findResult = null;
        findResult = apiUserService.getOneUser(whUser);

        if(null == findResult){
            responseBean.setSuccess("103");
            responseBean.setErrormsg("手机号不存在");
            return responseBean;
        }
        if(null == password){
            responseBean.setSuccess("104");
            responseBean.setErrormsg("密码不能为空");
            return responseBean;
        }
        if(!mobile.matches("^1[3|4|5|7|8][0-9]\\d{8}$")){
            responseBean.setSuccess("105");
            responseBean.setErrormsg("手机格式错误");
            return responseBean;
        }
        whUser.setPassword(password);
        try {
            apiUserService.saveUserInfoPhone(whUser);
            return responseBean;
        }catch (Exception e){
            log.error(e.toString());
            responseBean.setSuccess("106");
            responseBean.setErrormsg("重置密码失败");
            return responseBean;
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/coverPwd",method = RequestMethod.POST)
    public RetMobileEntity coverPwd(HttpServletRequest request){
        RetMobileEntity retMobileEntity = new RetMobileEntity();
        String uid = getParam(request,"uid");
        String pwd = getParam(request,"pwd");
        if(null == uid || null == pwd){
            retMobileEntity.setCode(1);
            retMobileEntity.setMsg("修改密码失败");
            return retMobileEntity;
        }
        try {
            WhUser whUser = new WhUser();
            whUser.setId(uid);
            Map temp = apiUserService.getOneUser(whUser);
            if(null == temp){
                retMobileEntity.setCode(1);
                retMobileEntity.setMsg("修改密码失败");
                return retMobileEntity;
            }
            whUser.setPassword(pwd);
            apiUserService.saveUserInfo(whUser);
            retMobileEntity.setCode(0);
            retMobileEntity.setData(temp);
            return retMobileEntity;
        }catch (Exception e){
            log.error(e.toString());
            retMobileEntity.setCode(1);
            retMobileEntity.setMsg("修改密码失败");
            return retMobileEntity;
        }
    }

    /**
     * 密码修改
     */
    @CrossOrigin
    @RequestMapping(value = "/upPwd",method = RequestMethod.POST)
    public RetMobileEntity upPwd(HttpServletRequest request,String userId,String oldPwd,String newPwd){
        RetMobileEntity retMobileEntity = new RetMobileEntity();
        if(oldPwd == null  ){
            retMobileEntity.setCode(101);
            retMobileEntity.setMsg("旧密码不允许为空！");
        }
        if( newPwd == null){
            retMobileEntity.setCode(102);
            retMobileEntity.setMsg("新密码不允许为空！");
        }
        try {
            if(userId != null){
                WhUser whUser = new WhUser();
                whUser.setId(userId);
                Map temp = apiUserService.getOneUser(whUser);
                if(temp.get("password") != oldPwd){
                    retMobileEntity.setCode(103);
                    retMobileEntity.setMsg("旧密码不正确！");
                }else{
                    whUser.setPassword(newPwd);
                    apiUserService.saveUserInfo(whUser);
                    retMobileEntity.setCode(0);
                    retMobileEntity.setMsg("密码修改成功！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retMobileEntity;
    }

    /**
     * 微信注册(拆)
     * 访问地址 /api/user/register/{phone}/{password}
     * @param phone 手机号码
     * @param password  密码
     * @return
     */
    @CrossOrigin
    @RequestMapping("/register/{phone}/{password}")
    public ResponseBean register(@PathVariable("phone") String phone, @PathVariable("password") String password){
        ResponseBean res = new ResponseBean();
        try {
            String validCode = this.apiUserService.register(phone, password);
            if(!"100".equals(validCode)){
                res.setSuccess(ResponseBean.FAIL);
                res.setErrormsg(validCode);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
        }
        return res;
    }



    /**
     * 发送验证码
     * 访问地址 /api/user/sendSMS/{phone}/{code}
     * @param phone 手机号码
     * @param code  验证码
     * @return JSON : {
     *     "success" : "1"                             //1-成功； 其它失败
     *     "errormsg" : "100|101|102"          //100-发送成功；101-手机格式不正确; 102-发生异常
     * }
     */
    @CrossOrigin
    @RequestMapping("/sendSMS/{phone}/{code}")
    public ResponseBean sendValidCode(String phone, String code){
        ResponseBean res = new ResponseBean();
        try {
            //手机格式不正确
            if(phone == null || !phone.matches("^1[3|4|5|7|8][0-9]\\d{8}$")){
                res.setSuccess(ResponseBean.FAIL);
                res.setErrormsg("101");
            }else{
                Map<String, String> smsData = new HashMap<String, String>();
                smsData.put("validCode", code);
                smsService.t_sendSMS(phone, "LOGIN_VALIDCODE", smsData,phone);
                res.setErrormsg("100");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("102");
        }
        return res;
    }

    /**
     * 找回密码
     * @param mobile  手机号码
     * @param code	  短信验证码
     * @param password 新密码
     * @return JSON : {
     *     "success" : "1"                             //1-成功； 其它失败
     *     "errormsg" : "100|101|102"          //100-发送成功；101-用户手机不存在; 102-验证码不正确;103-发送异常
     * }
     */

    public ResponseBean setPasswd(String mobile,String code,String password){
        ResponseBean res = new ResponseBean();
        WhUser whUser = apiUserService.findUser4Phone(mobile);
        //验证手机号码是否存在
        if(whUser == null){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("101");
        }
        //验证验证码是否填写正确
        WhCode whCode = apiUserService.findWhCode4SmsContent(code);
        if(whCode == null){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("102");
        }
        //修改密码
        try {
            whUser.setPassword(password);
            apiUserService.saveUserInfo(whUser);
        } catch (Exception e) {
            e.printStackTrace();
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("103");
        }
        return res;
    }

    /**
     * 保存用户资料(POST提交，表单数据为需要修改的whusr表的字段)
     * 访问地址 /api/user/info/save
     * @param whUser 需要修改的用户信息，参考whusr表结构, POST表单数据如：{
     *       "id": "userid,whuser.id的值",
     *       "name": "修改后的用户姓名",
     *       "nickname": "修改后的用户昵称"， ....更多whusr表的字段参数
     * }
     * @return JSON : {
     *     "success" : "1"                  //1-成功； 其它失败
     *     "errormsg" : "异常消息"          //失败时的异常消息
     * }
     */
    @CrossOrigin
    @RequestMapping(value = "/info/save",method = RequestMethod.POST)
    public ResponseBean saveUserInfo(WhUser whUser){
        ResponseBean res = new ResponseBean();
        try {
            this.apiUserService.saveUserInfo(whUser);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("102");
        }
        return res;
    }

    /**
     * 根据openId拿用户数据
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/wechatLogin",method = RequestMethod.POST)
    public ResponseBean wechatLogin(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String openId = getParam(request,"openId");
        String userId = null;
        if(null == openId){
            responseBean.setCode("101");
            responseBean.setErrormsg("获取用户资料失败");
            return responseBean;
        }
        Map oneUser = null;
        WhgUsrWeixin whgUsrWeixin = new WhgUsrWeixin();
        whgUsrWeixin.setOpenid(openId);
        whgUsrWeixin = apiUserService.getWxUser(whgUsrWeixin);
        if(null != whgUsrWeixin){
            userId = whgUsrWeixin.getUserid();
        }
        if(null != userId){
            WhUser whUser = new WhUser();
            whUser.setId(userId);
            oneUser = apiUserService.getOneUser(whUser);
        }
        return responseBean;
    }

    /**
     * 获取用户详情
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/userDetail",method = RequestMethod.POST)
    public ResponseBean userDetail(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String userId = getUserId(request);
        String wxoid = getParam(request,"wxoid");
        if(null == userId && null == wxoid){
            responseBean.setCode("101");
            responseBean.setErrormsg("获取用户资料失败");
            return responseBean;
        }
        Map oneUser = null;
        if(null != userId){
            WhUser whUser = new WhUser();
            whUser.setId(userId);
            oneUser = apiUserService.getOneUser(whUser);
        }else {
            WhgUsrWeixin whgUsrWeixin = new WhgUsrWeixin();
            whgUsrWeixin.setOpenid(wxoid);
            whgUsrWeixin = apiUserService.getWxUser(whgUsrWeixin);
            if(null != whgUsrWeixin){
                userId = whgUsrWeixin.getUserid();
            }
            if(null != userId){
                WhUser whUser = new WhUser();
                whUser.setId(userId);
                oneUser = apiUserService.getOneUser(whUser);
            }
        }
        if(null == oneUser){
            responseBean.setCode("101");
            responseBean.setErrormsg("获取用户资料失败");
            return responseBean;
        }
        Map map = new HashMap();
        map.put("userId",oneUser.get("id"));
        map.put("userName",oneUser.get("name"));
        map.put("mobile",oneUser.get("phone"));
        map.put("job", oneUser.get("job"));
        map.put("sex",oneUser.get("sex"));
        map.put("birthday",oneUser.get("birthday"));
        map.put("nickName",oneUser.get("nickname"));
        map.put("authState",oneUser.get("isrealname"));
        map.put("userHeadImgUrl",oneUser.get("headurl"));
        map.put("staticServerUrl",commPropertiesService.getUploadLocalServerAddr());
        responseBean.setCode("0");
        responseBean.setData(map);
        return responseBean;
    }

    /**
     * 获取参数
     * @param request
     * @param paramName
     * @return
     */
    private String getParam(HttpServletRequest request,String paramName){
        String value = request.getParameter(paramName);
        if(null != value && !value.isEmpty()){
            return value;
        }
        return null;
    }

    /**
     * 获取参数，带默认值
     * @param request
     * @param paramName
     * @param myDefault
     * @return
     */
    private String getParam(HttpServletRequest request,String paramName,String myDefault){
        String value = request.getParameter(paramName);
        if(null != value && !value.isEmpty()){
            return value;
        }
        return myDefault;
    }

    /**
     * 获取用户未取票活动
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/actCount",method = RequestMethod.POST)
    public ResponseBean useractivity(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String userId = getUserId(request);
        if(null == userId){
            responseBean.setCode("101");
            responseBean.setErrormsg("获取活动数量失败");
            return responseBean;
        }
        Integer actCount = apiUserService.getActCountForUserCenter(userId);
        responseBean.setCode("0");
        responseBean.setData(actCount);
        return responseBean;
    }

    /**
     * 获取用户ID
     * @param request
     * @return
     */
    private String getUserId(HttpServletRequest request){
        String[] userId = request.getParameterValues("userId");
        if(null == userId){
            return null;
        }
        if(1 == userId.length){
            return userId[0];
        }
        int i = 0;
        for(;i < userId.length;i++){
            if(!"0".equals(userId[i])){
                break;
            }
        }
        if(i >= userId.length){
            return null;
        }
        return userId[i];
    }

    /**
     * 获取用户未取票场馆数量
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/venCount",method = RequestMethod.POST)
    public ResponseBean venCount(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String userId = getUserId(request);
        if(null == userId){
            responseBean.setCode("101");
            responseBean.setErrormsg("获取场馆数量失败");
            return responseBean;
        }
        Integer actCount = apiUserService.getVenCountForUserCenter(userId);
        responseBean.setCode("0");
        responseBean.setData(actCount);
        return responseBean;
    }

    /***
     * 获取用户活动列表
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/userActivityList",method = RequestMethod.POST)
    public ResponseBean userActivityList(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String userId = getUserId(request);
        if(null == userId){
            responseBean.setCode("101");
            responseBean.setErrormsg("获取用户活动订单失败");
            return responseBean;
        }

        return responseBean;
    }


    /**
     * 获取用户收藏信息
     * @param request
     * @return
     */
    @SuppressWarnings("all")
    @CrossOrigin
    @RequestMapping(value = "/collectList",method = RequestMethod.POST)
    public RetMobileEntity collectList(HttpServletRequest request){
        RetMobileEntity retMobileEntity = new RetMobileEntity();
        String userId = getParam(request,"userId");
        String wxoid = getParam(request,"wxoid");
        String type = getParam(request,"type");
        String index = getParam(request,"index");
        String rows = getParam(request,"rows","6");
        if(null == userId){
            if(null == wxoid){
                retMobileEntity.setCode(1);
                retMobileEntity.setMsg("查询用户收藏失败");
                return retMobileEntity;
            }
            WhgUsrWeixin whgUsrWeixin = new WhgUsrWeixin();
            whgUsrWeixin.setOpenid(wxoid);
            whgUsrWeixin = apiUserService.getWxUser(whgUsrWeixin);
            if(null == whgUsrWeixin.getUserid()){
                retMobileEntity.setCode(1);
                retMobileEntity.setMsg("查询用户收藏失败");
                return retMobileEntity;
            }
            userId = whgUsrWeixin.getUserid();
        }
        if(null == type){
            retMobileEntity.setCode(1);
            retMobileEntity.setMsg("查询用户收藏失败");
            return retMobileEntity;
        }
        if(null == index || !StringUtils.isNumber(index)){
            index = "1";
        }
        String cmreftyp = null;
        if("1".equals(type)){
            cmreftyp = "4";
        }else if("2".equals(type)){
            cmreftyp = "2";
        }else {
            cmreftyp = "3";
        }
        Map retData = apiUserService.getUserCollection(userId,cmreftyp,index,rows);
        PageInfo pageInfo = (PageInfo)retData.get("pageInfo");
        retMobileEntity.setCode(0);
        retMobileEntity.setData((List)retData.get("myData"));
        RetMobileEntity.Pager pager = new RetMobileEntity.Pager();
        pager.setTotal(((Long)pageInfo.getTotal()).intValue());
        pager.setIndex(pageInfo.getPageNum());
        pager.setSize(pageInfo.getPageSize());
        pager.setCount(pageInfo.getPages());
        retMobileEntity.setPager(pager);
        retMobileEntity.pushExData("staticServerUrl",commPropertiesService.getUploadLocalServerAddr());
        return retMobileEntity;
    }


    /***
     * 获取我的活动
     * @param request
     * @return
     */
    @SuppressWarnings("unused")
    @CrossOrigin
    @RequestMapping(value = "/activity",method = RequestMethod.POST)
    public RetMobileEntity activity(int index,int size,String type,String userId,HttpServletRequest request){
        RetMobileEntity rme = new RetMobileEntity();
        if(null == userId){
            rme.setCode(101);
            rme.setMsg("获取用户活动订单失败");
            return rme;
        }
        PageInfo pageInfo = this.userCenterService.getOrderForCenter(index,size,Integer.valueOf(type),userId);
        Map<String,Object> param = new HashMap<>();
        Pager pager = new RetMobileEntity.Pager();
        pager.setCount(pageInfo.getList().size());
        pager.setIndex(index);
        pager.setSize(size);
        pager.setTotal(Integer.parseInt(String.valueOf(pageInfo.getTotal())));
        param.put("actOrderList",pageInfo.getList());
        param.put("pager", pager);
        param.put("total", pageInfo.getTotal());
        rme.setData(param);
        return rme;
    }

    /**
     * 取消订单
     * @param request
     * @param request
     * @return
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/cancel",method = RequestMethod.POST)
    public RetMobileEntity cancel(HttpServletRequest request,String itemId,String userId,String type){
        RetMobileEntity rme = new RetMobileEntity();
        //type 订单类型 1. 活动 2. 场馆活动室 3. 培训课程
        try {
            if(itemId == null ||"".equals(itemId) ){
                rme.setCode(101);
                rme.setMsg("订单ID不允许为空!");
                return rme;
            }
            if(type.equals("1")){
                WhgActOrder actOrder = userCenterService.findOrderDetail(itemId);
                int canCancel = canActCancel(actOrder);
                if(1 == canCancel){
                    rme.setCode(101);
                    rme.setMsg("该订单不存在");
                    return rme;
                }else if(2 == canCancel){
                    rme.setCode(101);
                    rme.setMsg("该订单不存在");
                    return rme;
                }else if(3 == canCancel){
                    rme.setCode(101);
                    rme.setMsg("已经有验过的票，不能取消");
                    return rme;
                }else {
                    actOrder.setTicketstatus(3);
                    actOrder.setOrderisvalid(2);
                    userCenterService.upActOrder(actOrder);
                    rme.setCode(0);
                    rme.setMsg("活动订单取消成功！");
                }
            }else if(type.equals("2")){
                int count =  this.service.unOrder(itemId);
                rme.setData(count);
                rme.setCode(0);
                rme.setMsg("活动室订单取消成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rme;

    }

    /**
     *  判断活动订单能否取消
     * @param actOrder
     * @return：0:可以取消；1:该订单不存在；2:活动已经不能取消；3:已经有人验票
     */
    private int canActCancel(WhgActOrder actOrder){
        if(null == actOrder){
            return 1;
        }
        WhgActTime whgActTime = whhdService.getActTimeInfo(actOrder.getEventid());
        if(null == whgActTime){
            return 1;
        }
        LocalDateTime today =  LocalDateTime.now();
        today.plusDays(2);
        LocalDateTime startTime = date2LocalDateTime(whgActTime.getPlaystarttime());
        if(startTime.isBefore(today)){
            return 2;
        }
        if(0 < whhdService.getActTicketChecked(actOrder.getId())){
            return 3;
        }
        return 0;
    }

    private LocalDateTime date2LocalDateTime(Date date){
        try {
            Instant instant = date.toInstant();
            ZoneId zone = ZoneId.systemDefault();
            return LocalDateTime.ofInstant(instant, zone);
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    /***
     * 获取我预定的场馆
     * @param request
     * @return
     */
    @SuppressWarnings("unused")
    @CrossOrigin
    @RequestMapping(value = "/venue",method = RequestMethod.POST)
    public RetMobileEntity venue(int index,int size,String userId,HttpServletRequest request){
        RetMobileEntity rme = new RetMobileEntity();
        Map<String,Object> param = new HashMap<>();
        //分页查询
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        param.put("page", index);
        param.put("rows", size);

        if(null == userId){
            rme.setCode(101);
            rme.setMsg("获取用户场馆订单失败");
            return rme;
        }
        param.put("userid", userId);
        try {
            rtnMap = this.service.findOrder4User(index, size, param);
            rme.setData(rtnMap);
            Pager pager = new RetMobileEntity.Pager();
            pager.setCount(rtnMap.size());
            pager.setIndex(index);
            pager.setSize(size);
            pager.setTotal(Integer.parseInt(String.valueOf(rtnMap.get("total"))));
            rtnMap.put("pager", pager);
            rme.setPager(pager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rme;
    }

    /**
     * 查询个人中心我的场馆预定数据（web端）
     * @return
     */
    @RequestMapping(value = "/loadVenueOrder", method = RequestMethod.POST)
    @CrossOrigin
    public Object loadVenueOrder(HttpServletRequest req) {
        //分页查询
        Map<String, Object> rtnMap = new HashMap<>();
        try {
            //获取请求参数
            Map<String, Object> param = ReqParamsUtil.parseRequest(req);
            param.put("userid", param.get("userId"));
            rtnMap = this.service.findOrder(param);
            rtnMap.put("code", 0);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            rtnMap.put("code", 1);
            rtnMap.put("total", 0);
            rtnMap.put("rows", new ArrayList<Map<String, Object>>(0));
        }
        return rtnMap;
    }

    /**
     * 判断用户是否点亮收藏(web端)
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/selectColle",method = RequestMethod.POST)
    @CrossOrigin
    public Object isColle(WebRequest request) {
        Map<String, Object> map = new HashMap<>();
        String success = "0";
        String errMsg = "";
        int scNum = 0;
        try {
            //取得收藏关联参数
            String reftyp = request.getParameter("reftyp"); // 收藏关联类型
            String refid = request.getParameter("refid"); // 收藏关联id
            String userId = request.getParameter("userId"); // 收藏关联id
            //获取收藏数
            scNum = this.colleService.shouCanShu(reftyp, refid);

            // 判断用户是是否登录
            if (userId == null) {
                success = "2";
                errMsg = "请登录后再收藏";
            } else {
                // 判断用户是否已收藏
                boolean iscolle = this.colleService.isColle(userId, reftyp, refid);
                if (iscolle) {
                    success = "1"; // 已收藏
                }
            }
        } catch (Exception e) {
            success = "3";
            errMsg = e.getMessage();
        }
        map.put("scNum",scNum );
        map.put("errMsg", errMsg);
        map.put("success", success);
        return map;
    }

    /**
     * 添加收藏
     * @param request
     * @return
     */
    @SuppressWarnings("all")
    @CrossOrigin
    @RequestMapping(value = "/addUserCollect",method = RequestMethod.POST)
    public RetMobileEntity addUserCollect(HttpServletRequest request){
        RetMobileEntity retMobileEntity = new RetMobileEntity();
        String myUserId = getParam(request,"userId");
        String itemId = getParam(request,"itemId");
        String operateType = getParam(request,"operateType");
        String type = getParam(request,"type");
        String refurl = getParam(request,"refurl");
        String cmreftyp = null;
        if("1".equals(type)){
            cmreftyp = "4";
        }else if("2".equals(type)){
            cmreftyp = "2";
        }else {
            cmreftyp = "3";
        }
        try {
            // 判断会话是否为空
            if (null == myUserId) {
                retMobileEntity.setCode(1);
                retMobileEntity.setMsg("请登录后再收藏");
                return retMobileEntity;
            } else {
                JSONObject targetObject = colleService.getTargetInfo(cmreftyp,itemId);
                if(null == targetObject){
                    retMobileEntity.setCode(1);
                    retMobileEntity.setMsg("收藏失败");
                    return retMobileEntity;
                }
                // 添加收藏
                WhCollection whCollection = new WhCollection();
                whCollection.setCmid(this.commService.getKey("whcolle"));
                whCollection.setCmuid(myUserId); // 用户id
                whCollection.setCmdate(new Date()); // 用户收藏时间
                whCollection.setCmopttyp(operateType);// 操作类型为收藏
                whCollection.setCmrefid(itemId);
                whCollection.setCmurl(refurl);//收藏访问地址
                whCollection.setCmreftyp(cmreftyp);
                if("4".equals(cmreftyp)){
                    //活动
                    whCollection.setCmtitle(targetObject.getString("name"));
                }else if("2".equals(cmreftyp)){
                    //场馆
                    whCollection.setCmtitle(targetObject.getString("title"));
                }else if("3".equals(cmreftyp)){
                    //活动室
                    whCollection.setCmtitle(targetObject.getString("title"));
                }
                this.colleService.addMyColle(whCollection);
                this.centerAction.addNewAlert(myUserId,"4");//用户中心我的收藏消息提醒
                retMobileEntity.setCode(0);
                retMobileEntity.setMsg("收藏成功");
                return retMobileEntity;
            }
        } catch (Exception e) {
            log.error(e.toString());
            retMobileEntity.setCode(3);
            retMobileEntity.setMsg("收藏失败");
            return retMobileEntity;
        }
    }

    /**
     * 取消收藏
     * @param request
     * @return
     */
    @SuppressWarnings("all")
    @CrossOrigin
    @RequestMapping(value = "/deleteCollect",method = RequestMethod.POST)
    public RetMobileEntity deleteCollect(HttpServletRequest request){
        RetMobileEntity retMobileEntity = new RetMobileEntity();
        int scNum = 0;
        try {
            //取得收藏关联参数
            String type = getParam(request,"type"); // 收藏关联类型
            String itemId = getParam(request,"itemId"); // 收藏关联id
            String myUserId = getParam(request,"userId");
            String cmreftyp = null;
            if("1".equals(type)){
                cmreftyp = "4";
            }else if("2".equals(type)){
                cmreftyp = "2";
            }else {
                cmreftyp = "3";
            }
            //获取收藏数
            scNum = this.colleService.shouCanShu(cmreftyp, itemId);
            // 判断会话是否为空
            if (myUserId == null) {
                retMobileEntity.setCode(1);
                retMobileEntity.setMsg("请先登录");
                return retMobileEntity;
            }
            // 删除用户收藏记录
            int c = this.colleService.removeCommColle(cmreftyp, itemId, myUserId);
            if (c != 1) {
                retMobileEntity.setCode(101);
            } else {
                retMobileEntity.setCode(0);
            }
            retMobileEntity.setData(scNum);
            return retMobileEntity;
        } catch (Exception e) {
            log.error(e.toString());
            retMobileEntity.setCode(3);
            retMobileEntity.setMsg(e.getMessage());
            return retMobileEntity;
        }
    }

    /**
     * 添加评论
     * @param request
     * @return
     */
    @SuppressWarnings("all")
    @CrossOrigin
    @RequestMapping(value = "/addComment",method = RequestMethod.POST)
    public RetMobileEntity addComment(HttpServletRequest request){
        RetMobileEntity retMobileEntity = new RetMobileEntity();
        String myUserId = getParam(request,"userId");
        String itemId = getParam(request,"itemId");
        String content = getParam(request,"content");
        String type = getParam(request,"type");
        String title = getParam(request,"title");
        try {
            WhComment whComment = new WhComment();
            whComment.setRmid(commService.getKey("wh_comment"));
            whComment.setRmuid(myUserId);
            whComment.setRmcontent(content);
            whComment.setRmrefid(itemId);
            whComment.setRmreftyp(type);
            whComment.setRmdate(new Date());
            whComment.setRmtitle(title);
            whComment.setRmstate(0);
            whComment.setRmtyp(0);
            this.commentSerice.addMyComm(whComment);
            retMobileEntity.setCode(0);
            return retMobileEntity;
        }catch (Exception e){
            log.error(e.toString());
            retMobileEntity.setCode(1);
            retMobileEntity.setMsg(e.toString());
            return retMobileEntity;
        }
    }

    /**
     * 获取评论
     * @param request
     * @return
     */
    @SuppressWarnings("all")
    @CrossOrigin
    @RequestMapping(value = "/showComment",method = RequestMethod.POST)
    public RetMobileEntity showComment(HttpServletRequest request){
        RetMobileEntity retMobileEntity = new RetMobileEntity();
        String myUserId = getParam(request,"userId");
        String itemId = getParam(request,"itemId");
        String type = getParam(request,"type");
        String index = getParam(request,"index","1");
        String rows = getParam(request,"rows","6");
        PageInfo pageInfo = apiUserService.getComm(type,itemId,index,rows);
        if(null == pageInfo){
            retMobileEntity.setCode(1);
            retMobileEntity.setMsg("获取评论失败");
            return retMobileEntity;
        }
        retMobileEntity.setCode(0);
        retMobileEntity.setData(pageInfo.getList());
        Pager pager = new Pager();
        pager.setCount(pageInfo.getSize());
        pager.setIndex(pageInfo.getPageNum());
        pager.setTotal(Long.valueOf(pageInfo.getTotal()).intValue());
        pager.setSize(pageInfo.getPageSize());
        retMobileEntity.setPager(pager);
        retMobileEntity.pushExData("showType",type);
        return retMobileEntity;
    }

    /**
     * 修改用户昵称
     * @param request
     * @return
     */
    @SuppressWarnings("all")
    @CrossOrigin
    @RequestMapping(value = "/doRename",method = RequestMethod.POST)
    public RetMobileEntity doRename(HttpServletRequest request){
        RetMobileEntity retMobieEntity = new RetMobileEntity();
        String myUserId = getParam(request,"userId");
        String nickName = getParam(request,"nickName");
        WhUser whUser = new WhUser();
        try {
            whUser.setId(myUserId);
            whUser.setNickname(nickName);
            apiUserService.saveUserInfo(whUser);
            retMobieEntity.setCode(0);
            whUser.setNickname(null);
            Map map = apiUserService.getOneUser(whUser);
            if(null != map){
                retMobieEntity.setData(map);
            }
            return retMobieEntity;
        }catch (Exception e){
            log.error(e.toString());
            retMobieEntity.setCode(1);
            retMobieEntity.setMsg(e.toString());
            return retMobieEntity;
        }
    }

    /**
     * 修改用户性别
     * @param request
     * @return
     */
    @SuppressWarnings("all")
    @CrossOrigin
    @RequestMapping(value = "/doChangeSex",method = RequestMethod.POST)
    public RetMobileEntity doChangeSex(HttpServletRequest request){
        RetMobileEntity retMobieEntity = new RetMobileEntity();
        String myUserId = getParam(request,"userId");
        String sex = getParam(request,"sex");
        WhUser whUser = new WhUser();
        try {
            whUser.setId(myUserId);
            whUser.setSex(sex);
            apiUserService.saveUserInfo(whUser);
            whUser.setSex(null);
            retMobieEntity.setCode(0);
            Map map = apiUserService.getOneUser(whUser);
            if(null != map){
                retMobieEntity.setData(map);
            }
            return retMobieEntity;
        }catch (Exception e){
            log.error(e.toString());
            retMobieEntity.setCode(1);
            retMobieEntity.setMsg(e.toString());
            return retMobieEntity;
        }
    }

    /**
     * 修改用户生日
     * @param request
     * @return
     */
    @SuppressWarnings("all")
    @CrossOrigin
    @RequestMapping(value = "/doChangeBirthDay",method = RequestMethod.POST)
    public RetMobileEntity doChangeBirthDay(HttpServletRequest request){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        RetMobileEntity retMobileEntity = new RetMobileEntity();
        String myUserId = getParam(request,"userId");
        String birthday = getParam(request,"birthday");
        WhUser whUser = new WhUser();
        try {
            whUser.setId(myUserId);
            whUser.setBirthday(sdf.parse(birthday));
            apiUserService.saveUserInfo(whUser);
            whUser.setBirthday(null);
            retMobileEntity.setCode(0);
            Map map = apiUserService.getOneUser(whUser);
            if(null != map){
                retMobileEntity.setData(map);
            }
            return retMobileEntity;
        }catch (Exception e){
            log.error(e.toString());
            retMobileEntity.setCode(1);
            retMobileEntity.setMsg(e.toString());
            return retMobileEntity;
        }
    }

    @SuppressWarnings("all")
    @CrossOrigin
    @RequestMapping(value = "/commentList",method = RequestMethod.POST)
    public RetMobileEntity commentList(HttpServletRequest request){
        RetMobileEntity retMobileEntity = new RetMobileEntity();
        String myUserId = getParam(request,"userId");
        String type = getParam(request,"type","1");
        String index = getParam(request,"index","1");
        String rows = getParam(request,"rows","10");
        if(null == myUserId){
            retMobileEntity.setCode(1);
            retMobileEntity.setMsg("获取我的评论失败");
            return retMobileEntity;
        }
        WhComment whComment = new WhComment();
        whComment.setRmuid(myUserId);
        if("1".equals(type)){
            whComment.setRmreftyp("1");
        }else {
            whComment.setRmreftyp("6");
        }
        PageInfo pageInfo = apiUserService.getMyComment(whComment,index,rows);
        if(null == pageInfo){
            retMobileEntity.setCode(1);
            retMobileEntity.setMsg("获取我的评论失败");
            return retMobileEntity;
        }
        List<Map> resList = apiUserService.getExInfoForComment((List)pageInfo.getList());

        retMobileEntity.setCode(0);
        retMobileEntity.setData(resList);
        Pager pager = new Pager();
        pager.setCount(pageInfo.getPages());
        pager.setSize(pageInfo.getSize());
        pager.setIndex(pageInfo.getPageNum());
        pager.setTotal(Long.valueOf(pageInfo.getTotal()).intValue());
        retMobileEntity.setPager(pager);
        retMobileEntity.pushExData("staticServerUrl",commPropertiesService.getUploadLocalServerAddr());
        return retMobileEntity;
    }

    /**
     * 判断原始密码（接口用）
     * @param request
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/isPassWord",method = RequestMethod.POST)
    public Object isPassWord(WebRequest request, String passWord, String userId) {
        Map<String, Object> res = new HashMap<>();
//		String passWord = request.getParameter("passWord");
//		String id = request.getParameter("userId");
        WhUser user;
        try {
            user = (WhUser) userCenterService.getList(userId);
            String pwd = user.getPassword();
            if (passWord != null && pwd.equals(passWord)) {
                res.put("success", 0);
            } else {
                res.put("error", "密码不一致");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 修改用户密码(接口用)
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/modifyPassword",method = RequestMethod.POST)
    public Object modifyPassword(WebRequest request){
        String success = "0";
        String errMsg = "";
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            //获取页面数据
            String newPwdMd5 = request.getParameter("newPwdMd5");
            String id = request.getParameter("userId");
            //获取用户
            WhUser user = (WhUser) userCenterService.getList(id);
            user.setPassword(newPwdMd5);
            this.userCenterService.modifyPwd(user);
        } catch (Exception e) {
            errMsg = e.getMessage();
            e.printStackTrace();
        }
        map.put("success", success);
        map.put("errMsg", errMsg);
        return map;
    }

    /**
     * 判断原始手机 (接口用)
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/PrePhone",method = RequestMethod.POST)
    public Object PrePhone(WebRequest request) {
        String success = "0";
        String errMsg = "";
        Map<String,Object> map = new HashMap<>();
        try {
            String id = request.getParameter("userId");
            WhUser user = (WhUser) userCenterService.getList(id);
            String phone = user.getPhone();
            String preMsgPhone = request.getParameter("preMsgPhone");
            if(preMsgPhone != null && phone.equals(preMsgPhone)){
                success = "0";	//与原始手机匹配
            }else{
                success = "2";	//与原始手机不匹配
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("success", success);
        map.put("errMsg", errMsg);
        return map;
    }

    /**
     * 个人中心-安全设置-绑定手机号码（接口用）
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/bindPhone", method = RequestMethod.POST)
    public Object modifyPhone(WebRequest request) {
        String success = "0";
        String errMsg = "";
        Map<String, Object> map = new HashMap<>();
        try {
            String phone = request.getParameter("phone");
            String id = request.getParameter("userId");
            String code = request.getParameter("code");
            if (code == null || phone == null || id == null) {
                errMsg = "参数错误";
            } else {
                WhCode whCode = apiUserService.findWhCode4SmsContent(phone, code);
                if (null == whCode) {
                    map.put("errMsg", "短信验证码校验失败");
                    return map;
                } else {
                    WhUser user = (WhUser) userCenterService.getList(id);
                    user.setPhone(phone);
                    this.userCenterService.modifyPhone(user);
                }
            }
        } catch (Exception e) {
            errMsg = e.getMessage();
            e.printStackTrace();
        }
        map.put("success", success);
        map.put("errMsg", errMsg);
        return map;
    }

    /**
     * 判断用户是否点亮点赞
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/isDianZan",method = RequestMethod.POST)
    @CrossOrigin
    public Object IsGood(HttpServletRequest servletRequest,WebRequest request) {
        Map<String, Object> map = new HashMap<>();
        String success = "0";
        String errMsg = "";
        String num = "0";

        try {
            //取得收藏关联参数
            String reftyp = request.getParameter("reftyp"); // 收藏关联类型
            String refid = request.getParameter("refid"); // 收藏关联id
            String userId = request.getParameter("userId"); // 用户id

            //判断用户id是否为空
            if(userId == null){
                // 获取点赞ip地址
                ReqParamsUtil IP = new ReqParamsUtil();
                String dzIP = IP.gerClientIP(servletRequest);
                //判断是否有点赞记录
                boolean isgood = this.colleService.IsGood(dzIP, reftyp, refid);
                if (isgood) {
                    success = "1"; // 已点赞
                }
            }else{
                //用户id不为空
                boolean isgood = this.colleService.IsGood(userId, reftyp, refid);
                if (isgood) {
                    success = "1"; // 已点赞
                }
            }

            //设置被点赞次数
            num = this.colleService.dianZhanShu(reftyp, refid)+"";
        } catch (Exception e) {
            success = "2";
            errMsg = e.getMessage();
        }
        map.put("success", success);
        map.put("errMsg", errMsg);
        map.put("num", num);
        return map;
    }

    /**
     * 添加点赞
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/addDianZan",method = RequestMethod.POST)
    @CrossOrigin
    public Object addGood(HttpServletRequest servletRequest, WebRequest request,
                          WhCollection whcolle) {
        String success = "0";
        String errMsg = "";
        // 获取点赞ip地址
        ReqParamsUtil IP = new ReqParamsUtil();
        String dzIP = IP.gerClientIP(servletRequest);

        Map<String, Object> map = new HashMap<String, Object>();
        String num = "0";
        try {
            // 取得用户id
//            String uid = (String) session.getAttribute(WhConstance.SESS_USER_ID_KEY);
            String uid = request.getParameter("userId");
            String reftyp = request.getParameter("reftyp");
            String refid = request.getParameter("refid");
            // 判断用户id是否为空	 null:根据ip地址添加点赞记录	 不为null:根据用户id添加点赞记录
            if (uid == null) {
                whcolle.setCmid(this.commService.getKey("whcolle"));
                whcolle.setCmuid(dzIP);
                whcolle.setCmdate(new Date()); // 收藏时间
                whcolle.setCmopttyp("2"); // 操作类型为点赞
                whcolle.setCmreftyp(reftyp);
                whcolle.setCmrefid(refid);
                this.colleService.addGood(whcolle);
            } else {
                whcolle.setCmid(this.commService.getKey("whcolle"));
                whcolle.setCmuid(uid);
                whcolle.setCmdate(new Date()); // 收藏时间
                whcolle.setCmopttyp("2"); // 操作类型为点赞
                whcolle.setCmreftyp(reftyp);
                whcolle.setCmrefid(refid);
                this.colleService.addGood(whcolle);
            }
            num = this.colleService.dianZhanShu(reftyp, refid)+"";
        } catch (Exception e) {
            success = "1";
            errMsg = e.getMessage();
        }
        map.put("success", success);
        map.put("errMsg", errMsg);
        map.put("num", num);
        return map;
    }

    /**
     * 处理身份证图片上传
     * @param file
     * @param filemake
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/uploadIdCard",method = RequestMethod.POST)
    public Object uploadIdCard(MultipartFile file, String filemake, HttpServletRequest request){
        Map<String, Object> res = new HashMap<>();
        try {
            String id = request.getParameter("userId");
            //根据用户id获取用户
            WhUser user = (WhUser) userCenterService.getList(id);
            if(user != null){
                user = this.realService.saveUserIdCardPic(user, file, filemake, request);
                res.put("success", 0);
                res.put("msg", user);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.put("success", false);
            res.put("msg", e.getMessage());
        }
        return res;
    }

    /**
     * 保存用户姓名和身份证号
     * @param name
     * @param idcard
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/saveInfo",method = RequestMethod.POST)
    public Object saveInfo(String name, String idcard, HttpServletRequest request) {
        Map<String, Object> res = new HashMap<>();
        try {
            String id = request.getParameter("userId");
            //根据用户id获取用户
            WhUser user = (WhUser) userCenterService.getList(id);
            if (user != null) {
                user.setName(name);
                user.setIdcard(idcard);
                user = this.realService.saveInfo(user);
                res.put("success", 0);
                res.put("msg", user);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.put("error", false);
            res.put("msg", e.getMessage());
        }
        return res;
    }

    /**
     * 查询用户信息
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/getUserInfo",method = RequestMethod.POST)
    public Object getUserInfo(HttpServletRequest request) {
        Map<String, Object> res = new HashMap<>();
        try {
            String id = request.getParameter("userId");
            //根据用户id获取用户
            WhUser user = (WhUser) userCenterService.getList(id);
            if (user != null) {
                res.put("success", 0);
                res.put("data", user);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.put("error", false);
            res.put("msg", e.getMessage());
        }
        return res;
    }

    /**
     * 删除个人中心评论
     */
    @RequestMapping(value = "/removeContent",method = RequestMethod.POST)
    @CrossOrigin
    public Object removeContent(WebRequest request){
        Map<String, Object> res = new HashMap<>();
        try {
            res.put("code",0);
            this.commentSerice.removeContent(request);
        } catch (Exception e) {
            res.put("code",101);
        }
        return res;
    }

    /**
     * 点评个人中心页面加载数据（web端）
     * @param request
     * @return
     */
    @RequestMapping(value = "/getComment",method = RequestMethod.POST)
    @CrossOrigin
    public Object getComment(WebRequest request,HttpSession session){
        Map<String, Object> res = new HashMap<>();
        try {
            res.put("data", this.commentSerice.loadcomLoad(request, session));
            return res;
        } catch (Exception e) {
            log.error(e.getMessage());
            res.put("error","加载失败");
        }
        return res;
    }

    /**
     * 评论回复
     * @param rmids
     * @return
     */
    @RequestMapping(value = "/commentHuifu",method = RequestMethod.POST)
    @CrossOrigin
    public Object commentHuifu(String rmids) {
        Map<String, Object> res = new HashMap<>();
        List<Object> list;
        try {
            list = this.commentSerice.searchCommentHuifu(rmids);
            res.put("code", 0);
            res.put("data", list);
        } catch (Exception e) {
            res.put("code", 101);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 类型字点
     * @return
     */
    @RequestMapping(value = "/dict",method = RequestMethod.POST)
    @CrossOrigin
    public Object dict() {
        Map<String, Object> res = new HashMap<>();
        List<WhgYwiType> list;
        try {
            list = this.whgYunweiTypeService.findWhgYwiTypeList(null);
            res.put("code", 0);
            res.put("data", list);
        } catch (Exception e) {
            res.put("code", 101);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 标签字点
     * @return
     */
    @RequestMapping(value = "/tagdict",method = RequestMethod.POST)
    @CrossOrigin
    public Object tagdict() {
        Map<String, Object> res = new HashMap<>();
        List list;
        try {
            list = this.whgYunweiTagService.dict();
            res.put("code", 0);
            res.put("data", list);
        } catch (Exception e) {
            res.put("code", 101);
            log.error(e.getMessage(), e);
        }
        return res;
    }
}


