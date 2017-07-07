package com.creatoo.hn.actions.admin.live;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.ext.emun.EnumTypeClazz;
import com.creatoo.hn.model.WhBranchRel;
import com.creatoo.hn.model.WhgLive;
import com.creatoo.hn.model.WhgSysUser;
import com.creatoo.hn.services.admin.branch.BranchService;
import com.creatoo.hn.services.admin.live.LiveService;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    @Autowired
    private BranchService branchService;

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
        String type = getParam(request,"type",null);
        String state = getParam(request,"state",null);
        if(null != type){
            map.put("type",type);
        }
        if(null != livetitle){
            map.put("livetitle",livetitle);
        }
        if(null != livestate){
            map.put("livestate",livestate);
        }
        if(null != isdel){
            map.put("isdel",isdel);
        }
        if(null != state){
            map.put("livestate",state);
        }
        WhgSysUser whgSysUser = (WhgSysUser) request.getSession().getAttribute("user");
        List<Map> relList = branchService.getBranchRelList(whgSysUser.getId(),EnumTypeClazz.TYPE_LIVE.getValue());
        if(null != relList && relList.size()>0){
            map.put("relList",relList);
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
            WhBranchRel whBranchRel = branchService.getBranchRel(id,EnumTypeClazz.TYPE_LIVE.getValue());
            if(null != whBranchRel){
                modelAndView.addObject("whBranchRel",whBranchRel);
            }
        }
        modelAndView.setViewName("admin/live/view_edit");
        return modelAndView;
    }

    @RequestMapping("/getFlowaddr")
    public ResponseBean getFlowaddr(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String flowAddr = "rtmp://video-center.alivecdn.com/tzswhg/Stream1?vhost=live.dgswhg.com";
        List list = new ArrayList();
        Map map = new HashMap();
        map.put("id","rtmp://video-center.alivecdn.com/tzswhg/Stream1?vhost=live.dgswhg.com");
        map.put("name","rtmp://video-center.alivecdn.com/tzswhg/Stream1?vhost=live.dgswhg.com");
        list.add(map);
        responseBean.setRows(list);
        return responseBean;
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        WhgLive whgLive = new WhgLive();
        whgLive.setLivetitle(getParam(request,"livetitle",null));
        whgLive.setDomain(getParam(request,"domain",null));
        whgLive.setLivecover(getParam(request,"livecover",null));
        whgLive.setLivelbt(getParam(request,"livelbt",null));
        whgLive.setIslbt(Integer.valueOf(getParam(request,"islbt","2")));
        whgLive.setIsrecommend(Integer.valueOf(getParam(request,"isrecommend","2")));
        whgLive.setFlowaddr(getParam(request,"flowaddr",null));
        whgLive.setLivedesc(getParam(request,"remark",null));
        whgLive.setLivetype(getParam(request,"livetype",null));
        try {
            whgLive.setStarttime(simpleDateFormat.parse(getParam(request,"starttime",null)));
            whgLive.setEndtime(simpleDateFormat.parse(getParam(request,"endtime",null)));
        }catch (Exception e){
            logger.error(e.toString());
        }
        if("add".equals(type)){
            WhgLive res = liveService.addOne(whgLive,(WhgSysUser)request.getSession().getAttribute("user"));
            if(null == res){
                responseBean.setSuccess(ResponseBean.FAIL);
                responseBean.setErrormsg("添加云直播失败");
                return responseBean;
            }
            String[] branch = request.getParameterValues("branch");
            for(String branchId : branch){
                branchService.setBranchRel(res.getId(), EnumTypeClazz.TYPE_LIVE.getValue(),branchId);
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
            branchService.clearBranchRel(id,EnumTypeClazz.TYPE_LIVE.getValue());
            //设置活动所属单位
            String[] branch = request.getParameterValues("branch");
            for(String branchId : branch){
                branchService.setBranchRel(id, EnumTypeClazz.TYPE_LIVE.getValue(),branchId);
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
