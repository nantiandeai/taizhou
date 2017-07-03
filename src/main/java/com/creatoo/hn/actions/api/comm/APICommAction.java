package com.creatoo.hn.actions.api.comm;

import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.utils.ReqParamsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 公共API接口
 * Created by wangxl on 2017/4/13.
 */
@RestController
@RequestMapping("/api/com")
public class APICommAction {

    @Autowired
    public CommService commservice;

    /**
     * 全局搜索关键字
     * @param req 请求对象
     * @param res 响应对象
     * @return 分页搜索数据
     */
    @CrossOrigin
    @RequestMapping(value = "/globalsrchcontent",method = RequestMethod.POST)
    public Object globalsrchcontent(HttpServletRequest req, HttpServletResponse res){
        //获取请求参数
        Map<String, Object> param = ReqParamsUtil.parseRequest(req);

        //分页查询
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            if(param.containsKey("srchkey")){
                param.put("srchkey", "%"+param.get("srchkey")+"%");
            }
            if(param.containsKey("index")){
                param.put("page", param.get("index"));
            }
            if(param.containsKey("size")){
                param.put("rows", param.get("size"));
            }
            rtnMap = this.commservice.globalsrchcontent(param);
        } catch (Exception e) {
            rtnMap.put("total", 0);
            rtnMap.put("rows", new ArrayList<Map<String, Object>>(0));
        }
        return rtnMap;
    }

}
