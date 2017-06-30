package com.creatoo.hn.actions.api.sms;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.services.api.sms.APISmsService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.utils.SmsTmpEnum;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenf on 2017/5/5.
 */
@RestController
@RequestMapping("/api/misc")
public class APISmsAction {
    /**
     * 日志控制器
     */
    Logger log = Logger.getLogger(this.getClass().getName());
    @Autowired
    private SMSService smsService;

    /**
     * 发送短信
     * @param userId   用户id
     * @param type 短信模板类型
     * @param mobile 电话号码
     * @return
     */
    @CrossOrigin
    @RequestMapping("/sms")
    public ResponseBean sendMsg(String userId, Integer type, String mobile) {
        ResponseBean res = new ResponseBean();
        try {
            if (null==userId||null==type||null==mobile){
                res.setSuccess("1000");
                res.setErrormsg("必要参数不完整！");
                return  res;
            }
            String tempCode= SmsTmpEnum.getTempCode(type);
            Map<String,String> data=new HashMap<String,String>();
            //此处数据封装待编写
            smsService.t_sendSMS(tempCode,mobile,data,userId);

        } catch (Exception e) {
            log.error(e.getMessage(),e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
        }
        return  res;
    }
}
