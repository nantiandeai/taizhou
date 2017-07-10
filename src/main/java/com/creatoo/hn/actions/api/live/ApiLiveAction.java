package com.creatoo.hn.actions.api.live;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.model.WhgLive;
import com.creatoo.hn.model.WhgYwiKey;
import com.creatoo.hn.services.api.live.LiveServiceForApi;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**云直播接口控制器
 * Created by caiyong on 2017/7/7.
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/api/live")
public class ApiLiveAction {

    private static Logger logger = Logger.getLogger(ApiLiveAction.class);

    @Autowired
    private LiveServiceForApi liveServiceForApi;

    @CrossOrigin
    @RequestMapping(value = "/indexData",method = RequestMethod.POST)
    public ResponseBean indexData(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        List<WhgYwiKey> whgYwiKeyList = liveServiceForApi.getAllDomainByType("11");
        List list = new ArrayList();
        for(WhgYwiKey whgYwiKey : whgYwiKeyList){
            Map map = new HashMap();
            map.put("title",whgYwiKey.getName());
            map.put("keyId",whgYwiKey.getId());
            WhgLive whgLive = new WhgLive();
            whgLive.setDomain(whgYwiKey.getId());
            whgLive.setLivestate(3);
            whgLive.setIsdel(2);
            PageInfo pageInfo = liveServiceForApi.getLiveList(1,4,whgLive);
            List myList = new ArrayList();
            if(null != pageInfo){
                myList = pageInfo.getList();
            }
            map.put("list",myList);
            list.add(map);
        }
        responseBean.setRows(list);
        return responseBean;
    }

    @CrossOrigin
    @RequestMapping(value = "/list4p",method = RequestMethod.POST)
    public ResponseBean list4p(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String domain = getParamValue(request,"domain",null);
        String type = getParamValue(request,"type",null);
        String sort = getParamValue(request,"sort",null);
        String index = getParamValue(request,"index","1");
        String size = getParamValue(request,"size","16");
        PageInfo pageInfo = liveServiceForApi.searchLiveList(index,size,domain,type,sort);
        if(null == pageInfo){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("获取直播列表失败");
            return responseBean;
        }
        responseBean.setRows((List)pageInfo.getList());
        responseBean.setPage(pageInfo.getPageNum());
        responseBean.setPageSize(pageInfo.getPageSize());
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
