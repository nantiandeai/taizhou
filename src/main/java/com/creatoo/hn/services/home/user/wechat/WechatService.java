package com.creatoo.hn.services.home.user.wechat;

import com.creatoo.hn.mapper.WhgUsrWeixinMapper;
import com.creatoo.hn.mapper.home.CrtWeChatMapper;
import com.creatoo.hn.model.WhgUsrWeixin;
import com.creatoo.hn.services.comm.CommPropertiesService;
import com.creatoo.hn.utils.social.wechat.CommonUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**微信专用服务
 * Created by caiyong on 2017/5/8.
 */
@SuppressWarnings("all")
@Service
public class WechatService {

    private static Logger logger = Logger.getLogger(WechatService.class);

    @Autowired
    private CrtWeChatMapper crtWeChatMapper;

    @Autowired
    private CommPropertiesService commPropertiesService;

    @Autowired
    private WhgUsrWeixinMapper whgUsrWeixinMapper;
    /**
     * 获取BaseAuthUrl
     * @param request
     * @return
     */
    public String getBaseAuthUrl(HttpServletRequest request){
        String path = request.getContextPath();
        path += "/api/wechat/baseauth?code=code&state=userCenter";
        return path;
    }

    /**
     * 获取UserAuthUrl
     * @param request
     * @return
     */
    public String getUserAuthUrl(HttpServletRequest request,String state){
        String appId = commPropertiesService.getWxAppId();
        String redirectUrl = String.format("%swechat/userauth", CommonUtil.getWebBaseUrl(request));
        String scope = "snsapi_userinfo";
        redirectUrl = CommonUtil.urlEncodeUTF8(redirectUrl);
        String url = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect", appId, redirectUrl, scope, state);
        return url;
    }

    /**
     * 获取WhWechat
     * @param openId
     * @return
     */
    public WhgUsrWeixin getSessionByOpenId(String openId){
        try {
            WhgUsrWeixin whgUsrWeixin = new WhgUsrWeixin();
            whgUsrWeixin.setOpenid(openId);
            List<WhgUsrWeixin> list = crtWeChatMapper.queryWhWeChat(whgUsrWeixin);
            if(null == list || list.isEmpty()){
                return null;
            }
            return list.get(0);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 获取WhWechat
     * @param unionid
     * @return
     */
    public WhgUsrWeixin getSessionByUnionId(String unionid){
        try {
            WhgUsrWeixin whgUsrWeixin = new WhgUsrWeixin();
            whgUsrWeixin.setUnionid(unionid);
            List<WhgUsrWeixin> list = crtWeChatMapper.queryWhWeChat(whgUsrWeixin);
            if(null == list || list.isEmpty()){
                return null;
            }
            return list.get(0);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    public int updateSession(WhgUsrWeixin whgUsrWeixin){
        try {
            whgUsrWeixinMapper.updateByPrimaryKey(whgUsrWeixin);
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }

    public int addSession(WhgUsrWeixin whgUsrWeixin){
        try {
            whgUsrWeixinMapper.insert(whgUsrWeixin);
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }
}
