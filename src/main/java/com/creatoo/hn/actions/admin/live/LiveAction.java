package com.creatoo.hn.actions.admin.live;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.services.admin.live.LiveService;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**云直播控制器
 * Created by caiyong on 2017/7/5.
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/admin/live")
public class LiveAction {

    private static Logger logger = Logger.getLogger(LiveAction.class);

    @Autowired
    private LiveService liveService;

    @RequestMapping("/list/{type}")
    public ModelAndView list(HttpServletRequest request, @PathVariable("type")String type){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("listType",type);
        modelAndView.setViewName("admin/live/view_list");
        return modelAndView;
    }

    @RequestMapping("/search4p/{type}")
    public ResponseBean search4p(HttpServletRequest request,@PathVariable("type")String type){
        ResponseBean responseBean = new ResponseBean();
        Map map = new HashMap();
        String page = getParam(request,"page","1");
        String rows = getParam(request,"rows","10");
        String livetitle = getParam(request,"livetitle",null);
        String livestate = getParam(request,"livestate",null);
        if(null != livetitle){
            map.put("livetitle",livetitle);
        }
        if(null != livestate){
            map.put("livestate",livestate);
        }
        PageInfo pageInfo = liveService.getLiveList(page,rows,map);
        if(null == pageInfo){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("获取云直播列表失败");
            return responseBean;
        }
        responseBean.setRows((List)pageInfo.getList());
        responseBean.setPage(pageInfo.getPageNum());
        responseBean.setPageSize(pageInfo.getPageSize());
        responseBean.setTotal(pageInfo.getTotal());
        return responseBean;
    }

    /**
     * 获取参数
     * @param request
     * @param paramName
     * @param defaultValue
     * @return
     */
    private String getParam(HttpServletRequest request,String paramName,String defaultValue){
        String value = request.getParameter(paramName);
        if(null == value || value.trim().isEmpty()){
            return defaultValue;
        }
        return value;
    }
}
