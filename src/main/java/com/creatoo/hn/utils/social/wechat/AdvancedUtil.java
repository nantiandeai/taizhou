package com.creatoo.hn.utils.social.wechat;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("all")
public class AdvancedUtil {
	
	/**
	 * 日志控制器
	 */ 
	static Logger log = Logger.getLogger(AdvancedUtil.class);
	
	public static String getOpenId(String appId, String redirectUrl, String state) {
		// 拼接请求地址
        String requestUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECTURL&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
        requestUrl = requestUrl.replace("APPID", CommonUtil.urlEncodeUTF8(appId));
        requestUrl = requestUrl.replace("REDIRECTURL", CommonUtil.urlEncodeUTF8(redirectUrl));
        requestUrl = requestUrl.replace("STATE", CommonUtil.urlEncodeUTF8(state));
        // 获取网页授权凭证
        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
        if (null != jsonObject) {
            try {
                return jsonObject.toJSONString();
            } catch (Exception e) {
                int errorCode = Integer.parseInt(jsonObject.getString("errcode"));
                String errorMsg = jsonObject.getString("errmsg");
                log.error(String.format("获取网页授权凭证失败 errcode:%d errmsg:%s", errorCode, errorMsg));
                e.printStackTrace();
            }
        }
        return "";
	}
	
	/**
     * 获取网页授权凭证
     * 
     * @param appId 公众账号的唯一标识
     * @param appSecret 公众账号的密钥
     * @param code
     * @return WeixinAouth2Token
     */
    public static WeixinOauth2Token getOauth2AccessToken(String appId, String appSecret, String code) {
        WeixinOauth2Token wat = null;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        requestUrl = requestUrl.replace("APPID", appId);
        requestUrl = requestUrl.replace("SECRET", appSecret);
        requestUrl = requestUrl.replace("CODE", code);
        // 获取网页授权凭证
        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
        if (null != jsonObject) {
            try {
                wat = new WeixinOauth2Token();
                wat.setAccessToken(jsonObject.getString("access_token"));
                wat.setExpiresIn(Integer.parseInt(jsonObject.getString("expires_in")));
                System.out.println("expirese+in======================"+jsonObject.getString("expires_in").toString());
                wat.setRefreshToken(jsonObject.getString("refresh_token"));
                wat.setOpenId(jsonObject.getString("openid"));
                wat.setScope(jsonObject.getString("scope"));
            } catch (Exception e) {
                wat = null;
                int errorCode = Integer.parseInt(jsonObject.getString("errcode"));
                String errorMsg = jsonObject.getString("errmsg");
                log.error(String.format("获取网页授权凭证失败 errcode:%d errmsg:%s", errorCode, errorMsg));
                e.printStackTrace();
            }
        }
        return wat;
    }
    
    /**
     * 通过网页授权获取用户信息
     * 
     * @param accessToken 网页授权接口调用凭证
     * @param openId 用户标识
     * @return SNSUserInfo
     */
	@SuppressWarnings( { "deprecation", "unchecked" })
    public static SNSUserInfo getSNSUserInfo(String accessToken, String openId) {
        SNSUserInfo snsUserInfo = null;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
        // 通过网页授权获取用户信息
        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);

        if (null != jsonObject) {
            try {
                snsUserInfo = new SNSUserInfo();
                // 用户的标识
                snsUserInfo.setOpenId(jsonObject.getString("openid"));
                // 昵称
                snsUserInfo.setNickname(jsonObject.getString("nickname"));
                // 性别（1是男性，2是女性，0是未知）
                snsUserInfo.setSex(Integer.parseInt(jsonObject.getString("sex")));
                // 用户所在国家
                snsUserInfo.setCountry(jsonObject.getString("country"));
                // 用户所在省份
                snsUserInfo.setProvince(jsonObject.getString("province"));
                // 用户所在城市
                snsUserInfo.setCity(jsonObject.getString("city"));
                // 用户头像
                snsUserInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
                // 用户唯一ID
                snsUserInfo.setUnionid(jsonObject.getString("unionid"));
                // 用户特权信息
                snsUserInfo.setPrivilegeList(JSON.parseArray(jsonObject.getString("privilege"), String.class));
            } catch (Exception e) {
                snsUserInfo = null;
                int errorCode = Integer.parseInt(jsonObject.getString("errcode"));
                String errorMsg = jsonObject.getString("errmsg");
                log.error(String.format("获取用户信息失败 errcode:%d errmsg:%s", errorCode, errorMsg));
                e.printStackTrace();
            }
        }
        return snsUserInfo;
    }
	

	/**
     * 刷新网页授权Token
     * 
     * @param appId 公众账号的唯一标识
     * @param token 公众账号的刷新密钥
     * @return WeixinAouth2Token
     */
    public static WeixinRefreshToken refreshOAuthToken(String appId, String token) {
    	WeixinRefreshToken wat = null;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESHTOKEN";
        requestUrl = requestUrl.replace("APPID", appId);
        requestUrl = requestUrl.replace("REFRESHTOKEN", token);
        // 获取网页授权凭证
        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
        if (null != jsonObject) {
            try {
            	if (!jsonObject.containsKey("errcode") || Integer.parseInt(jsonObject.getString("errcode")) == 0) {
            		wat = new WeixinRefreshToken();
            		wat.setAccessToken(jsonObject.getString("access_token"));
            		wat.setOpenId(jsonObject.getString("openid"));
            		wat.setRefreshToken(jsonObject.getString("refresh_token"));
            		wat.setScope(jsonObject.getString("scope"));
                    wat.setExpiresIn(Integer.parseInt(jsonObject.getString("expires_in")));
            	}
            } catch (Exception e) {
                int errorCode = Integer.parseInt(jsonObject.getString("errcode"));
                String errorMsg = jsonObject.getString("errmsg");
                log.error(String.format("刷新access_token授权凭证失败 errcode:%d errmsg:%s", errorCode, errorMsg));
                e.printStackTrace();
            }
        }
        return wat;
    }
	
    /**
     * 获取全局接口授权票据
     * 
     * @param appId 公众账号的唯一标识
     * @param appSecret 公众账号的密钥
     * @param code
     * @return WeixinAouth2Token
     */
    public static WeixinAccessToken getGlobalToken(String appId, String secret) {
    	WeixinAccessToken wat = null;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        requestUrl = requestUrl.replace("APPID", appId);
        requestUrl = requestUrl.replace("APPSECRET", secret);
        // 获取网页授权凭证
        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
        if (null != jsonObject) {
            try {
            	if (!jsonObject.containsKey("errcode") || Integer.parseInt(jsonObject.getString("errcode")) == 0) {
                    wat = new WeixinAccessToken();
                    wat.setAccessToken(jsonObject.getString("access_token"));
                    wat.setExpiresIn(Integer.parseInt(jsonObject.getString("expires_in")));
            	}
            } catch (Exception e) {
                wat = null;
                int errorCode = Integer.parseInt(jsonObject.getString("errcode"));
                String errorMsg = jsonObject.getString("errmsg");
                log.error(String.format("获取网页JS授权凭证失败 errcode:%d errmsg:%s", errorCode, errorMsg));
                e.printStackTrace();
            }
        }
        return wat;
    }
    
	/**
     * 获取网页JS授权票据
     * 
     * @param appId 公众账号的唯一标识
     * @param appSecret 公众账号的密钥
     * @param code
     * @return WeixinAouth2Token
     */
    public static WeixinJsTicket getJsTicket(String accessToken) {
    	WeixinJsTicket wat = null;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESSTOKEN&type=jsapi";
        requestUrl = requestUrl.replace("ACCESSTOKEN", accessToken);
        // 获取网页授权凭证
        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
        if (null != jsonObject) {
            try {
            	if (!jsonObject.containsKey("errcode") || Integer.parseInt(jsonObject.getString("errcode")) == 0) {
                    wat = new WeixinJsTicket();
                    wat.setTicket(jsonObject.getString("ticket"));
                    wat.setExpiresIn(Integer.parseInt(jsonObject.getString("expires_in")));
            	}
            } catch (Exception e) {
                wat = null;
                int errorCode = Integer.parseInt(jsonObject.getString("errcode"));
                String errorMsg = jsonObject.getString("errmsg");
                log.error(String.format("获取网页JS授权凭证失败 errcode:%d errmsg:%s", errorCode, errorMsg));
                e.printStackTrace();
            }
        }
        return wat;
    }
	
	public static String getQrAuthUrl(String appId, String redirectUrl, String state) {
		// 拼接请求地址
        String requestUrl = "https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECTURL&response_type=code&scope=snsapi_login&state=STATE#wechat_redirect";
        requestUrl = requestUrl.replace("APPID", CommonUtil.urlEncodeUTF8(appId));
        requestUrl = requestUrl.replace("REDIRECTURL", CommonUtil.urlEncodeUTF8(redirectUrl));
        requestUrl = requestUrl.replace("STATE", CommonUtil.urlEncodeUTF8(state));
        return requestUrl;
	}

	public static String getWxAuthUrl(String appId, String redirectUrl, String state) {
        // 拼接请求地址
        String requestUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECTURL&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
        requestUrl = requestUrl.replace("APPID", CommonUtil.urlEncodeUTF8(appId));
        requestUrl = requestUrl.replace("REDIRECTURL", CommonUtil.urlEncodeUTF8(redirectUrl));
        requestUrl = requestUrl.replace("STATE", CommonUtil.urlEncodeUTF8(state));
        return requestUrl;
    }
}
