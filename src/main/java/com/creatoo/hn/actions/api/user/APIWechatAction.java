package com.creatoo.hn.actions.api.user;

import com.creatoo.hn.model.WhgUsrWeixin;
import com.creatoo.hn.services.comm.CommPropertiesService;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.services.home.user.wechat.CmsTerminalUserService;
import com.creatoo.hn.services.home.user.wechat.EnvironService;
import com.creatoo.hn.services.home.user.wechat.WechatService;
import com.creatoo.hn.utils.ReqParamsUtil;
import com.creatoo.hn.utils.social.wechat.AdvancedUtil;
import com.creatoo.hn.utils.social.wechat.SNSUserInfo;
import com.creatoo.hn.utils.social.wechat.WeixinOauth2Token;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 微信控制器
 * Created by caiyong on 2017/5/3.
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/api/wechat")
public class APIWechatAction {

    /**
     * 日志控制器
     */
    Logger log = Logger.getLogger(this.getClass());

    @Autowired
    public WechatService wechatService;
    @Autowired
    public EnvironService environService;

    @Autowired
    private CmsTerminalUserService terminalUserService ;

    @Autowired
    private CommPropertiesService commPropertiesService;

    @Autowired
    private CommService commService;

    @RequestMapping("login")
    @ResponseBody
    public Object wechatLogin(HttpServletRequest req){
        ModelAndView mav = new ModelAndView() ;
        String url = this.wechatService.getBaseAuthUrl(req);
        mav.setViewName(String.format("redirect:%s", url));
        return mav ;
    }

    /**
     * 微信公众号授权基础认证接口
     * @return 验证结果
     */
    @RequestMapping("/baseauth")
    @ResponseBody
    public Object baseAuth(HttpServletRequest req, HttpServletResponse resp){
        //获取请求参数
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(req);
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        String baseUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() ;
        System.out.println("baseUrl = " + baseUrl);
        ModelAndView mav = new ModelAndView();
        String code = paramMap.get("code").toString();
        String state = paramMap.get("state").toString();
        try {
            if (!"authdeny".equals(code)) {
                // 获取网页授权access_token
                //测试
                WeixinOauth2Token weixinOauth2Token = AdvancedUtil.getOauth2AccessToken(commPropertiesService.getWxAppId(), commPropertiesService.getWxAppSecret(), code);
                if(null == weixinOauth2Token){
                    String url = this.wechatService.getUserAuthUrl(req,state);
                    mav.setViewName(String.format("redirect:%s", url));
                    return mav;
                }
                // 网页授权接口访问凭证
                String accessToken = weixinOauth2Token.getAccessToken();
                // 用户标识
                String openId = weixinOauth2Token.getOpenId();
                // 获取用户信息
                SNSUserInfo snsUserInfo = AdvancedUtil.getSNSUserInfo(accessToken, openId);
                if(null == snsUserInfo){
                    String url = this.wechatService.getUserAuthUrl(req,state);
                    mav.setViewName(String.format("redirect:%s", url));
                    return mav;
                }
                insertWxUser(snsUserInfo);
                environService.getJsTicket();
                state = getState(snsUserInfo.getUnionid());
                String path = getPageForState(req,resp,state,openId);
                mav.setViewName(path);
                return mav;
            }else {
                state = "login";
                String path = getPageForState(req,resp,state);
                mav.setViewName(path);
                return mav;
            }
        } catch (Exception e) {
            log.error(e.toString());
            rtnMap.put("exception", e.getMessage());
            state = "login";
            String path = getPageForState(req,resp,state);
            mav.setViewName(path);
            return mav;
        }
    }

    /**
     * 确定绑定状态
     * @param unionid
     * @return
     */
    private String getState(String openId){
        WhgUsrWeixin whgUsrWeixin = wechatService.getSessionByOpenId(openId);
        if(null == whgUsrWeixin.getUserid()||whgUsrWeixin.getUserid().isEmpty()){
            return "bind";
        }
        return "user";
    }

    /**
     * 添加或修改微信用户
     * @param snsUserInfo
     * @throws Exception
     */
    private void insertWxUser(SNSUserInfo snsUserInfo) throws Exception{
        String openId = snsUserInfo.getOpenId();
        WhgUsrWeixin whgUsrWeixin = wechatService.getSessionByOpenId(openId);
        if (null == whgUsrWeixin) {
            whgUsrWeixin = new WhgUsrWeixin();
            whgUsrWeixin.setUnionid(snsUserInfo.getUnionid());
            whgUsrWeixin.setOpenid(openId);
            whgUsrWeixin.setId(commService.getKey("whg_usr_weixin"));
            whgUsrWeixin.setCity(snsUserInfo.getCity());
            whgUsrWeixin.setCountry(snsUserInfo.getCountry());
            whgUsrWeixin.setCrtdate(new Date());
            whgUsrWeixin.setNickname(filterExpression(snsUserInfo.getNickname()));
            whgUsrWeixin.setHeadimgurl(snsUserInfo.getHeadImgUrl());
            whgUsrWeixin.setProvince(snsUserInfo.getProvince());
            whgUsrWeixin.setSex(snsUserInfo.getSex());
            whgUsrWeixin.setCrtdate(new Date());
            //应对测试环境没有unionid的情况
            if(null == whgUsrWeixin.getUnionid()){
                whgUsrWeixin.setUnionid(whgUsrWeixin.getOpenid());
            }
            wechatService.addSession(whgUsrWeixin);
        } else {
            java.util.Date now = new java.util.Date();
            whgUsrWeixin.setOpenid(snsUserInfo.getOpenId());
            whgUsrWeixin.setCity(snsUserInfo.getCity());
            whgUsrWeixin.setCountry(snsUserInfo.getCountry());
            whgUsrWeixin.setCrtdate(new Date());
            whgUsrWeixin.setNickname(filterExpression(snsUserInfo.getNickname()));
            whgUsrWeixin.setHeadimgurl(snsUserInfo.getHeadImgUrl());
            whgUsrWeixin.setProvince(snsUserInfo.getProvince());
            whgUsrWeixin.setSex(snsUserInfo.getSex());
            //应对测试环境没有unionid的情况
            if(null == whgUsrWeixin.getUnionid()){
                whgUsrWeixin.setUnionid(whgUsrWeixin.getOpenid());
            }
            wechatService.updateSession(whgUsrWeixin);
        }
    }

    /**
     * 微信验证接口
     * @return 验证结果
     */
    @RequestMapping("/userauth")
    public Object userAuth(HttpServletRequest req, HttpServletResponse resp){
        //获取请求参数
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(req);
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        String baseUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() ;
        ModelAndView mav = new ModelAndView();
          String code = paramMap.get("code").toString();
        String state = paramMap.get("state").toString();
        String path = null;
        try {
            if (!"authdeny".equals(code)) {
                // 获取网页授权access_token
                WeixinOauth2Token weixinOauth2Token = AdvancedUtil.getOauth2AccessToken(commPropertiesService.getWxAppId(), commPropertiesService.getWxAppSecret(), code);
//                WeixinOauth2Token weixinOauth2Token = AdvancedUtil.getOauth2AccessToken("wxd8c81cbf81ab3361", "3ebeef6c65619496d39fa848d363f166", code);
                // 网页授权接口访问凭证
                String accessToken = weixinOauth2Token.getAccessToken();
                // 用户标识
                String openId = weixinOauth2Token.getOpenId();
                // 获取用户信息
                SNSUserInfo snsUserInfo = AdvancedUtil.getSNSUserInfo(accessToken, openId);
                if(null == snsUserInfo){
                    state = "login";
                    path = getPageForState(req,resp,state,openId);
                    mav.setViewName(path);
                    return mav;
                }
                //开始存入授权信息
                insertWxUser(snsUserInfo);
                environService.getJsTicket();
                state = getState(snsUserInfo.getOpenId());
                path = getPageForState(req,resp,state,openId);
                mav.setViewName(path);
                return mav;
                //resp.sendRedirect(baseUrl+path);
            }else {
                state = "login";
                path = getPageForState(req,resp,state);
                mav.setViewName(path);
                return mav;
            }
        } catch (Exception e) {
            e.printStackTrace();
            rtnMap.put("exception", e.getMessage());
            state = "login";
            path = getPageForState(req,resp,state);
            mav.setViewName(path);
            return mav;
        }
    }

    /**
     * 过滤表情符
     * @param nickName
     * @return
     */
    private String filterExpression(String nickName){
        Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher emojiMatcher = emoji.matcher(nickName);
        if (emojiMatcher.find()) {
            nickName = emojiMatcher.replaceAll("*");
        }
        return nickName;
    }

    /**
     * 微信验证接口
     * @return 验证结果
     */
    @RequestMapping("/userid")
    @ResponseBody
    public Object userId(HttpServletRequest req, HttpServletResponse resp){
        //获取请求参数
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(req);
        Map<String, Object> rtnMap = new HashMap<String, Object>();

        try {
//    		String openId = req.getSession().getAttribute("WechatOpenId").toString();
//    		rtnMap.put("WechatOpenId", openId); 1·

//        	WhWechat wechat = new WhWechat();
//        	wechat.setOpenid("test");
//        	wechatService.addSession(wechat);
            String openId = req.getSession().getAttribute("WechatOpenId").toString();
            WhgUsrWeixin wechat = wechatService.getSessionByOpenId(openId);
            rtnMap.put("openid", wechat.getOpenid());
//        	wechat.setSex("1");
//        	wechatService.updateSession(wechat);

        } catch (Exception e) {
            rtnMap.put("exception", e.getMessage());
            e.printStackTrace();
        }

        return rtnMap;
    }

    /**
     * 微信验证接口
     * @return 验证结果
     */
    @RequestMapping("/userinfo")
    @ResponseBody
    public Object userinfo(HttpServletRequest req, HttpServletResponse resp){
        Map<String, Object> rtnMap = new HashMap<String, Object>();

        try {
            String openId = req.getSession().getAttribute("WechatOpenId").toString();
            WhgUsrWeixin wechat = wechatService.getSessionByOpenId(openId);

            Map<String, Object> userMap = new HashMap<String, Object>();
            rtnMap.put("code", 0);
            rtnMap.put("msg", "");
            userMap.put("openId", wechat.getOpenid());
            userMap.put("nickName", wechat.getNickname());
            userMap.put("avatarUrl", wechat.getHeadimgurl());
            userMap.put("newCourse", 0);
            userMap.put("newActivity", 0);
            rtnMap.put("data", userMap);

        } catch (Exception e) {
            rtnMap.put("msg", e.getMessage());
            rtnMap.put("code", -1);
            e.printStackTrace();
        }

        return rtnMap;
    }



    /**
     * 微信验证接口
     * @return 验证结果
     */
    @RequestMapping("/jsticket")
    @ResponseBody
    public Object jsticket(HttpServletRequest req, HttpServletResponse resp){
        Map<String, Object> rtnMap = new HashMap<String, Object>();

        return rtnMap;
    }

    /**
     * 微信WAP首页
     * @return 验证结果
     */
    @RequestMapping("/index")
    public Object index(HttpServletRequest req, HttpServletResponse resp){

        //获取请求参数
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(req);
        Map<String, Object> rtnMap = new HashMap<String, Object>();

        ModelAndView mav = new ModelAndView();

        try {
            if (req.getSession().getAttribute("WechatOpenId") == null) {
                mav.setViewName("redirect:/userauth");
            }
            else {
                mav.setViewName("redirect:/pages/wap/wechat/index.html");
            }
        } catch (Exception e) {
            rtnMap.put("exception", e.getMessage());
            e.printStackTrace();
        }

        return mav;
    }

    /**
     * 微信开发者平台验证接口
     * @return 验证结果
     */
    @RequestMapping("/validate")
    @ResponseBody
    public Object validate(HttpServletRequest req, HttpServletResponse resp){
        //获取请求参数
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(req);
        Map<String, Object> rtnMap = new HashMap<String, Object>();



        return rtnMap;
    }

    /**
     * 不存储openId，只获得返回页面
     * @param req
     * @param res
     * @param state
     * @return
     */
    public String getPageForState(HttpServletRequest req, HttpServletResponse res, String state){
        String baseUrl = req.getScheme() + "://" + req.getServerName();// + ":" + req.getServerPort() ;
        String base = baseUrl+"/tzy/";
        String page = "index.html";
        if ("init".equals(state)) {
            page = "index.html";
        }
        else if ("checkin".equals(state)) {
            page = "page/checkin.html";
        }
        else if ("bind".equals(state)) {
            page = "page/userbind.html";
        }
        else if ("act".equals(state)) {
            page = "page/activity.html";
        }
        else if ("ven".equals(state)) {
            page = "page/venue.html";
        }
        else if ("tra".equals(state)) {
            page = "page/train.html";
        }
        else if ("user".equals(state)) {
            page = "page/user.html";
        }
        else if ("login".equals(state)) {
            page = "page/authlogin.html";
        }
        else if ("reg".equals(state)) {
            page = "page/authreg.html";
        }
        page = String.format("redirect:%s%s", base, page);
        return page;
    }

    /**
     * 获取返回页面并存储openId
     * @param req
     * @param res
     * @param state
     * @param openId
     * @return
     * @throws UnsupportedEncodingException
     */
    public String getPageForState(HttpServletRequest req, HttpServletResponse res, String state, String openId) throws UnsupportedEncodingException {
        String baseUrl = req.getScheme() + "://" + req.getServerName();// + ":" + req.getServerPort() ;
        String base = baseUrl+"/tzy/";
        String page = "index.html";
        if ("init".equals(state)) {
            page = "index.html";
        }
        else if ("checkin".equals(state)) {
            page = "page/checkin.html";
        }
        else if ("bind".equals(state)) {
            page = "page/userbind.html";
        }
        else if ("act".equals(state)) {
            page = "page/activity.html";
        }
        else if ("ven".equals(state)) {
            page = "page/venue.html";
        }
        else if ("tra".equals(state)) {
            page = "page/train.html";
        }
        else if ("user".equals(state)) {
            page = "page/user.html";
        }
        else if ("login".equals(state)) {
            page = "page/authlogin.html";
        }
        else if ("reg".equals(state)) {
            page = "page/authreg.html";
        }
        Cookie wxoid = new Cookie("wxoid", URLEncoder.encode(openId, "UTF-8"));
        Cookie flag = new Cookie("dgflag", "1");
        flag.setPath("/");
        wxoid.setPath("/");
        res.addCookie(wxoid);
        res.addCookie(flag);
        page = String.format("redirect:%s%s", base, page);
        return page;
    }

}
