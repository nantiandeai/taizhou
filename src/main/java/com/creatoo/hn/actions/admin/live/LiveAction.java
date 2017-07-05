package com.creatoo.hn.actions.admin.live;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.model.WhgLive;
import com.creatoo.hn.model.WhgSysUser;
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

    /**
     * 列表页
     * @param request
     * @param type
     * @return
     */
    @RequestMapping("/list/{type}")
    public ModelAndView list(HttpServletRequest request, @PathVariable("type")String type){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("listType",type);
        modelAndView.setViewName("admin/live/view_list");
        return modelAndView;
    }

    /**
     * 多维度分页查询
     * @param request
     * @param type
     * @return
     */
    @RequestMapping("/search4p")
    public ResponseBean search4p(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        Map map = new HashMap();
        String page = getParam(request,"page","1");
        String rows = getParam(request,"rows","10");
        String livetitle = getParam(request,"livetitle",null);
        String livestate = getParam(request,"livestate",null);
        String isdel = getParam(request,"isdel",null);
        if(null != livetitle){
            map.put("livetitle",livetitle);
        }
        if(null != livestate){
            map.put("livestate",livestate);
        }
        if(null != isdel){
            map.put("isdel",isdel);
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
     * 编辑页
     * @param request
     * @param type
     * @return
     */
    @RequestMapping("/edit/{type}")
    public ModelAndView edit(HttpServletRequest request,@PathVariable("type")String type){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("editType",type);
        String id = getParam(request,"id",null);
        if(null != id){
            WhgLive whgLive = new WhgLive();
            whgLive.setId(id);
            whgLive = liveService.getOne(whgLive);
            if(null != whgLive){
                modelAndView.addObject("whgLive",whgLive);
            }
        }
        modelAndView.setViewName("admin/live/view_edit");
        return modelAndView;
    }

    /**
     * 处理编辑提交
     * @param request
     * @param type
     * @return
     */
    @RequestMapping("/doEdit/{type}")
    public ResponseBean doEdit(HttpServletRequest request,@PathVariable("type")String type){
        ResponseBean responseBean = new ResponseBean();
        WhgLive whgLive = new WhgLive();
        whgLive.setLivetitle(getParam(request,"livetitle",null));
        whgLive.setDomain(getParam(request,"domain",null));
        whgLive.setLivecover(getParam(request,"livecover",null));
        whgLive.setLivelbt(getParam(request,"livelbt",null));
        whgLive.setIslbt(Integer.valueOf(getParam(request,"islbt","2")));
        whgLive.setIsrecommend(Integer.valueOf(getParam(request,"isrecommend","2")));
        whgLive.setFlowaddr(getParam(request,"flowaddr",null));
        if("add".equals(type)){
            if(null == liveService.addOne(whgLive,(WhgSysUser)request.getSession().getAttribute("user"))){
                responseBean.setSuccess(ResponseBean.FAIL);
                responseBean.setErrormsg("添加云直播失败");
                return responseBean;
            }
        }else if("edit".equals(type)){
            String id = getParam(request,"id",null);
            if(null == id){
                responseBean.setSuccess(ResponseBean.FAIL);
                responseBean.setErrormsg("参数不足");
                return responseBean;
            }
            whgLive.setId(id);
            if(null == liveService.updateOne(whgLive)){
                responseBean.setSuccess(ResponseBean.FAIL);
                responseBean.setErrormsg("修改云直播失败");
                return responseBean;
            }
        }else {
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("无此操作");
            return responseBean;
        }
        return responseBean;
    }

    /**
     * 修改状态
     * @param request
     * @param type
     * @return
     */
    @RequestMapping("/updateState/{type}")
    public ResponseBean updateState(HttpServletRequest request,@PathVariable("type")String type){
        ResponseBean responseBean = new ResponseBean();
        String id = getParam(request,"id",null);
        if(null == id){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("参数不足");
            return responseBean;
        }
        WhgLive whgLive = new WhgLive();
        whgLive.setId(id);
        if("initial".equals(type)){
            whgLive.setLivestate(0);
        }else if("checkpending".equals(type)){
            whgLive.setLivestate(1);
        }else if("checked".equals(type)){
            whgLive.setLivestate(2);
        }else if("published".equals(type)){
            whgLive.setLivestate(3);
        }else {
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("无此操作");
            return responseBean;
        }
        if(0 != liveService.updateState(whgLive)){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("修改云直播状态失败");
            return responseBean;
        }
        return responseBean;
    }

    /**
     * 删除/反删除
     * @param request
     * @param type
     * @return
     */
    @RequestMapping("/del/{type}")
    public ResponseBean del(HttpServletRequest request,@PathVariable("type")String type){
        ResponseBean responseBean = new ResponseBean();
        String id = getParam(request,"id",null);
        if(null == id){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("参数不足");
            return responseBean;
        }
        WhgLive whgLive = new WhgLive();
        whgLive.setId(id);
        if("del".equals(type)){
            whgLive.setIsdel(1);
        }else if("undel".equals(type)){
            whgLive.setIsdel(2);
        }else if("delForever".equals(type)){
            whgLive.setIsdel(-1);
        }
        if(0 != liveService.del(whgLive)){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("操作失败");
            return responseBean;
        }
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
