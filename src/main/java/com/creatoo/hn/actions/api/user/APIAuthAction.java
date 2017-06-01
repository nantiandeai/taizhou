package com.creatoo.hn.actions.api.user;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.model.WhUser;
import com.creatoo.hn.services.api.user.ApiUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**实名认证控制器
 * Created by caiyong on 2017/5/3.
 */
@SuppressWarnings("all")
@Controller
@RequestMapping("/api/auth")
public class APIAuthAction {

    private static Logger logger = Logger.getLogger(APIAuthAction.class);

    @Autowired
    private ApiUserService apiUserService;

    /**
     * 2.1.7实名认证步骤A
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping("/verifya")
    public ResponseBean verifya(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String userId = request.getParameter("userId");
        String fullName = request.getParameter("fullName");
        String idCard = request.getParameter("idCard");
        //String  cardExpiry = request.getParameter("cardExpiry");数据库表没有该字段，暂时不做处理
        if(null == userId || null == fullName || null == idCard){
            responseBean.setSuccess("101");
            responseBean.setErrormsg("信息缺失");
            return responseBean;
        }
        WhUser whUser = new WhUser();
        whUser.setId(userId);
        Map findResult = null;
        findResult = apiUserService.getOneUser(whUser);
        if(null == findResult){
            responseBean.setSuccess("102");
            responseBean.setErrormsg("用户ID不存在");
            return responseBean;
        }
        try {
            whUser.setName(fullName);
            whUser.setIdcard(idCard);
            apiUserService.saveUserInfo(whUser);
            return responseBean;
        }catch (Exception e){
            logger.error(e.toString());
            responseBean.setSuccess("103");
            responseBean.setErrormsg("实名认证步骤A失败");
            return responseBean;
        }
    }

    /**
     * 实名认证步骤B
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping("/verifyb")
    public ResponseBean verifyb(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String userId = request.getParameter("userId");
        String frontId = request.getParameter("frontId");
        String backId = request.getParameter("backId");
        //String handheldId = request.getParameter("handheldId");数据库表没有该字段，暂时不做处理
        if(null == userId || null == frontId || null == backId){
            responseBean.setSuccess("101");
            responseBean.setErrormsg("信息缺失");
            return responseBean;
        }
        WhUser whUser = new WhUser();
        whUser.setId(userId);
        Map findResult = null;
        findResult = apiUserService.getOneUser(whUser);
        if(null == findResult){
            responseBean.setSuccess("102");
            responseBean.setErrormsg("用户ID不存在");
            return responseBean;
        }
        try {
            whUser.setIdcardface(frontId);
            whUser.setIdcardback(backId);
            apiUserService.saveUserInfo(whUser);
            return responseBean;
        }catch (Exception e){
            logger.error(e.toString());
            responseBean.setSuccess("103");
            responseBean.setErrormsg("实名认证步骤B失败");
            return responseBean;
        }
    }

}
