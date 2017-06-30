package com.creatoo.hn.services.api.sms;

import com.creatoo.hn.services.comm.SMSService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by chenf on 2017/5/5.
 */
@Service
public class APISmsService {
    /**
     * 日志控制器
     */
    Logger log = Logger.getLogger(this.getClass().getName());
    @Autowired
    private SMSService smsService;

    /**
     * 发送短信服务
     * @param tmpCode
     * @param mobile
     * @param data
     * @return
     */
//    public void sendMsg(String tmpCode,String mobile,Map<String, String> data,)throws Exception{
//         smsService.t_sendSMS(mobile,tmpCode,data);
//    }
}
