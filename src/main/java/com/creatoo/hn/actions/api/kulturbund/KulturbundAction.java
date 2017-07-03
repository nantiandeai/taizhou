package com.creatoo.hn.actions.api.kulturbund;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.model.WhgCultureAct;
import com.creatoo.hn.model.WhgCultureUnit;
import com.creatoo.hn.model.WhgCultureZx;
import com.creatoo.hn.services.api.kulturbund.KulturbundService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**文化联盟控制器
 * Created by caiyong on 2017/7/3.
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/api/kulturbund")
public class KulturbundAction {

    private static Logger logger = Logger.getLogger(KulturbundAction.class);

    @Autowired
    private KulturbundService kulturbundService;

    @CrossOrigin
    @RequestMapping(value = "/indexData",method = RequestMethod.POST)
    public ResponseBean indexData(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        List<WhgCultureUnit> whgCultureUnitList = kulturbundService.getCultureUnit();
        List<WhgCultureAct> whgCultureActList = kulturbundService.getCultureAct(1,6);
        List<WhgCultureZx> whgCultureZxList = kulturbundService.getCultureZx(1,15);
        Map map = new HashMap();
        if(null != whgCultureUnitList){
            map.put("whgCultureUnitList",whgCultureUnitList);
        }
        if(null != whgCultureActList){
            map.put("whgCultureActList",whgCultureActList);
        }
        if(null != whgCultureZxList){
            map.put("whgCultureZxList",whgCultureZxList);
        }
        responseBean.setData(map);
        return responseBean;
    }

}
