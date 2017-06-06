package com.creatoo.hn.actions.admin.ereading;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.ext.emun.EnumTypeClazz;
import com.creatoo.hn.model.WhBranchRel;
import com.creatoo.hn.model.WhSzydZx;
import com.creatoo.hn.model.WhgSysUser;
import com.creatoo.hn.services.admin.branch.BranchService;
import com.creatoo.hn.services.admin.ereading.WhgEReadingInfoService;
import com.creatoo.hn.services.comm.CommService;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**数字阅读控制器
 * Created by caiyong on 2017/4/20.
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/admin/reading")
public class WhgEReadingAction {

    private static Logger logger = Logger.getLogger(WhgEReadingAction.class);

    @Autowired
    private WhgEReadingInfoService whgEReadingInfoService;

    @Autowired
    private CommService commService;

    @Autowired
    private BranchService branchService;
    /**
     * 进入数字阅读资讯列表
     * @param request
     * @param mmp
     * @param type
     * @return
     */
    @RequestMapping("/view/{type}")
    public ModelAndView listview(HttpServletRequest request, ModelMap mmp, @PathVariable("type")String type){
        ModelAndView view = new ModelAndView();
        try {
            mmp.addAttribute("type", type);
            view.setViewName("admin/reading/list");
        }catch (Exception e){
            logger.error(e.toString());
        }
        return view;
    }

    @RequestMapping("/edit/{type}")
    public ModelAndView editview(HttpServletRequest request, ModelMap mmp, @PathVariable("type")String type){
        ModelAndView view = new ModelAndView();
        try {
            mmp.addAttribute("type", type);
            if(type.equals("edit") || type.equals("view")){
            	String id = request.getParameter("id");
            	WhSzydZx whSzydZx = whgEReadingInfoService.t_serOne(id);
            	view.addObject("infotitle", whSzydZx.getInfotitle());
            	view.addObject("id", whSzydZx.getId());
            	view.addObject("infosummary", whSzydZx.getInfosummary());
            	view.addObject("infocover", whSzydZx.getInfocover());
            	view.addObject("author", whSzydZx.getAuthor());
            	view.addObject("infosource", whSzydZx.getInfosource());
            	view.addObject("infolink", whSzydZx.getInfolink());
                WhBranchRel whBranchRel = branchService.getBranchRel(id, EnumTypeClazz.TYPE_EREADING.getValue());
                if(null != whBranchRel){
                    view.addObject("whBranchRel",whBranchRel);
                }
            }
            view.setViewName("admin/reading/edit");
        }catch (Exception e){
            logger.error(e.toString());
        }
        return view;
    }

    @RequestMapping("/do/edit")
    public ResponseBean doEdit(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String type = request.getParameter("type");
        String submitType = request.getParameter("submitType");
        String infotitle = request.getParameter("infotitle");
        String infosummary = request.getParameter("infosummary");
        String infocover = request.getParameter("infocover");
        String infosource = request.getParameter("infosource");
        String infolink = request.getParameter("infolink");
        String author = request.getParameter("author");
        Map map = new HashMap();
        int state = 0;
        if("1".equals(submitType)){
            state = 0;
        }else{
            state = 1;
        }

        try {
            if("edit".equalsIgnoreCase(type)){
            	String id = request.getParameter("id");
            	WhSzydZx whSzydZx = whgEReadingInfoService.t_serOne(id);
            	whSzydZx.setAuthor(author);
            	whSzydZx.setInfocover(infocover);
            	whSzydZx.setInfolink(infolink);
            	whSzydZx.setInfosource(infosource);
            	whSzydZx.setInfotitle(infotitle);
            	whSzydZx.setInfosummary(infosummary);
                whSzydZx.setInfoupdatetime(new Date());
                whSzydZx.setInfostate(state);
            	if( whgEReadingInfoService.upReadingZx(whSzydZx) < 1){
                    responseBean.setSuccess(ResponseBean.FAIL);
                    responseBean.setErrormsg("修改数字阅读资讯失败");
                }
                branchService.clearBranchRel(id,EnumTypeClazz.TYPE_EREADING.getValue());
                //设置活动所属单位
                String[] branch = request.getParameterValues("branch");
                for(String branchId : branch){
                    branchService.setBranchRel(id, EnumTypeClazz.TYPE_EREADING.getValue(),branchId);
                }
            }else if("add".equalsIgnoreCase(type)){
                String id = commService.getKey("wh_zxinfo");
                map.put("id",id);
                if(null != infotitle){
                    map.put("infotitle",infotitle);
                }
                if(null != infosummary){
                    map.put("infosummary",infosummary);
                }
                if(null != infocover){
                    map.put("infocover",infocover);
                }
                if(null != infosource){
                    map.put("infosource",infosource);
                }
                if(null != infolink){
                    map.put("infolink",infolink);
                }
                if(null != author){
                    map.put("author",author);
                }
                map.put("infostate",state);
                if(0 != whgEReadingInfoService.addOneReadingZx(map)){
                    responseBean.setSuccess(ResponseBean.FAIL);
                    responseBean.setErrormsg("添加数字阅读资讯失败");
                }
                //设置活动所属单位
                String[] branch = request.getParameterValues("branch");
                for(String branchId : branch){
                    branchService.setBranchRel(id, EnumTypeClazz.TYPE_EREADING.getValue(),branchId);
                }
            }
        }catch (Exception e){
            logger.error(e.toString());
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("添加数字阅读资讯失败");
        }
        return responseBean;
    }
    
    
    /**
     * 修改状态
     * @param request 请求对象
     * @param ids 用逗号分隔的多个ID
     * @param fromState 修改之前的状态
     * @param toState 修改后的状态
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/updstate")
    public ResponseBean updstate(String statemdfdate, HttpServletRequest request, String ids, String fromState, String toState){
        ResponseBean res = new ResponseBean();
        try {
        	whgEReadingInfoService.t_updstate(statemdfdate, ids, fromState, toState);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
        }
        return res;
    }

    /**
     * 获取数字阅读资讯
     * @param request
     * @return
     */
    @RequestMapping("/getList")
    public ResponseBean getList(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String type = request.getParameter("type");
        if(null == type){
            return null;
        }
        Integer zxstate = null;
        if("edit".equalsIgnoreCase(type)){
            zxstate = 0;
        }else if("check".equalsIgnoreCase(type)){
            zxstate = 1;
        }else if("publish".equalsIgnoreCase(type)){
            zxstate = 2;
        }else if("recycle".equals(type)){
            zxstate = 3;
        }
        String page = request.getParameter("page");
        String rows = request.getParameter("rows");
        if(null == page || !StringUtil.isNumeric(page)){
            page = "0";
        }
        if(null == rows || !StringUtil.isNumeric(rows)){
            rows = "10";
        }
        WhgSysUser whgSysUser = (WhgSysUser) request.getSession().getAttribute("user");
        List<Map> relList = branchService.getBranchRelList(whgSysUser.getId(),EnumTypeClazz.TYPE_ACTIVITY.getValue());
        PageInfo myPage = whgEReadingInfoService.getInfoList(Integer.valueOf(page),Integer.valueOf(rows),zxstate,relList);
        responseBean.setRows((List)myPage.getList());
        responseBean.setTotal(myPage.getTotal());
        return responseBean;
    }

    @RequestMapping("/getActList")
    public ResponseBean getActList(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String page = request.getParameter("page");
        String rows = request.getParameter("rows");
        if(null == page || !StringUtil.isNumeric(page)){
            page = "0";
        }
        if(null == rows || !StringUtil.isNumeric(rows)){
            rows = "10";
        }
        PageInfo myPage = whgEReadingInfoService.getActList(Integer.valueOf(page),Integer.valueOf(rows));
        responseBean.setRows((List)myPage.getList());
        responseBean.setTotal(myPage.getTotal());
        return responseBean;
    }

    /**
     * 修改删除状态
     * @param request
     * @return
     */
    @RequestMapping("do/updisdel")
    public ResponseBean updisdel(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String id = request.getParameter("id");
        String isdel = request.getParameter("isdel");
        if(null == id){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("修改数字阅读资讯删除状态失败");
            return responseBean;
        }
        try {
            if("1".equals(isdel)){
                //删除
                if(0 != whgEReadingInfoService.t_updDelstate(id,"1")){
                    responseBean.setSuccess(ResponseBean.FAIL);
                    responseBean.setErrormsg("修改数字阅读资讯删除状态失败");
                    return responseBean;
                }
            }else if("2".equals(isdel)){
                //还原
                if(0 != whgEReadingInfoService.t_updDelstate(id,"0")){
                    responseBean.setSuccess(ResponseBean.FAIL);
                    responseBean.setErrormsg("修改数字阅读资讯删除状态失败");
                    return responseBean;
                }
            }else if("3".equals(isdel)){
                //永久删除
                if(0 != whgEReadingInfoService.t_updDelstate(id,"2")){
                    responseBean.setSuccess(ResponseBean.FAIL);
                    responseBean.setErrormsg("修改数字阅读资讯删除状态失败");
                    return responseBean;
                }
            }
            return responseBean;
        }catch (Exception e){
            logger.error(e.toString());
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("修改数字阅读资讯删除状态失败");
            return responseBean;
        }
    }
}
