package com.creatoo.hn.actions.api.kulturbund;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.model.WhgCultureUnit;
import com.creatoo.hn.services.api.kulturbund.KulturbundServiceForApi;
import com.github.pagehelper.PageInfo;
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
    private KulturbundServiceForApi kulturbundServiceForApi;

    @CrossOrigin
    @RequestMapping(value = "/indexData",method = RequestMethod.POST)
    public ResponseBean indexData(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        List<WhgCultureUnit> whgCultureUnitList = kulturbundServiceForApi.getCultureUnit();
        PageInfo whgCultureActList = kulturbundServiceForApi.getCultureAct(1,6);
        PageInfo whgCultureZxList = kulturbundServiceForApi.getCultureZx(1,15);
        Map map = new HashMap();
        if(null != whgCultureUnitList){
            map.put("whgCultureUnitList",whgCultureUnitList);
        }
        if(null != whgCultureActList){
            map.put("whgCultureActList",(List)whgCultureActList.getList());
        }
        if(null != whgCultureZxList){
            map.put("whgCultureZxList",(List)whgCultureZxList.getList());
        }
        responseBean.setData(map);
        return responseBean;
    }

    /**
     * 获取文化联盟资讯，支持分页查询
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/cultureZx4p",method = RequestMethod.POST)
    public ResponseBean cultureZx4p(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String index = getParamValue(request,"index","1");
        String size = getParamValue(request,"size","15");
        PageInfo pageInfo = kulturbundServiceForApi.getCultureZx(Integer.valueOf(index),Integer.valueOf(size));
        if(null == pageInfo){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("获取文化联盟资讯失败");
            return responseBean;
        }
        responseBean.setPage(pageInfo.getPageNum());
        responseBean.setPageSize(pageInfo.getPageSize());
        responseBean.setRows((List)pageInfo.getList());
        responseBean.setTotal(pageInfo.getTotal());
        return responseBean;
    }

    /**
     * 获取请求的参数
     * @param request
     * @param paramName
     * @param defaultValue
     * @return
     */
    private String getParamValue(HttpServletRequest request,String paramName,String defaultValue){
        String value = request.getParameter(paramName);
        if(null == value || value.trim().isEmpty()){
            return defaultValue;
        }
        return value;
    }
}
