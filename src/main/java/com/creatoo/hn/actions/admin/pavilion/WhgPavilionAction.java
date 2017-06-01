package com.creatoo.hn.actions.admin.pavilion;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.model.WhSzzgExhhall;
import com.creatoo.hn.model.WhgSysUser;
import com.creatoo.hn.services.admin.pavilion.WhgPavilionInfoService;
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

/**数字展馆控制
 * Created by caiyong on 2017/4/21.
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/admin/pavilion")
public class WhgPavilionAction {

    private static Logger logger = Logger.getLogger(WhgPavilionAction.class);

    @Autowired
    private WhgPavilionInfoService whgPavilionInfoService;

    @Autowired
    private CommService commService;

    @RequestMapping("/view/{type}")
    public ModelAndView listview(HttpServletRequest request, ModelMap mmp, @PathVariable("type")String type){
        ModelAndView view = new ModelAndView();
        try {
            mmp.addAttribute("type", type);
            view.setViewName("admin/pavilion/list");
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
                if(null != id){
                    WhSzzgExhhall whSzzgExhhall = whgPavilionInfoService.getOntById(id);
                    if(null != whSzzgExhhall){
                        view.addObject("id",whSzzgExhhall.getId());
                        view.addObject("hallname",whSzzgExhhall.getHallname());
                        view.addObject("hallsummary",whSzzgExhhall.getHallsummary());
                        view.addObject("hallcover",whSzzgExhhall.getHallcover());
                        view.addObject("hallexterior360",whSzzgExhhall.getHallexterior360());
                        view.addObject("hallInterior360",whSzzgExhhall.getHallinterior360());
                        view.addObject("halladdress",whSzzgExhhall.getHalladdress());
                        view.addObject("hallphone",whSzzgExhhall.getHallphone());
                    }
                }
            }
            view.setViewName("admin/pavilion/edit");
        }catch (Exception e){
            logger.error(e.toString());
        }
        return view;
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
        PageInfo myPage = whgPavilionInfoService.getInfoList(Integer.valueOf(page),Integer.valueOf(rows),zxstate);
        responseBean.setRows((List)myPage.getList());
        responseBean.setTotal(myPage.getTotal());
        return responseBean;
    }

    @RequestMapping("/do/edit")
    public ResponseBean doEdit(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String type = request.getParameter("type");
        String hallname = request.getParameter("hallname");
        String hallsummary = request.getParameter("hallsummary");
        String hallcover = request.getParameter("hallcover");
        String hallexterior360 = request.getParameter("hallexterior360");
        String hallInterior360 = request.getParameter("hallInterior360");
        String halladdress = request.getParameter("halladdress");
        String hallphone = request.getParameter("hallphone");
        String editType = request.getParameter("editType");
        Map<String,String> map = new HashMap<String,String>();
        WhgSysUser whgSysUser =  (WhgSysUser) request.getSession().getAttribute("user");
        try {
            if("edit".equalsIgnoreCase(type)){
                String id = request.getParameter("id");
                WhSzzgExhhall whSzzgExhhall = new WhSzzgExhhall();
                whSzzgExhhall.setId(id);
                whSzzgExhhall.setHalladdress(halladdress);
                whSzzgExhhall.setHallcover(hallcover);
                whSzzgExhhall.setHallexterior360(hallexterior360);
                whSzzgExhhall.setHallinterior360(hallInterior360);
                whSzzgExhhall.setHallname(hallname);
                whSzzgExhhall.setHallphone(hallphone);
                whSzzgExhhall.setHallsummary(hallsummary);
                whSzzgExhhall.setLastoperator(whgSysUser.getId());
                whSzzgExhhall.setLastoperatortime(new Date());
                whSzzgExhhall.setIsdel(0);
                if("save".equalsIgnoreCase(editType)){
                    whSzzgExhhall.setHallstate(0);//编辑中
                }else {
                    whSzzgExhhall.setHallstate(1);//送审
                }
                if(0 != whgPavilionInfoService.updateOne(whSzzgExhhall)){
                    responseBean.setSuccess(ResponseBean.FAIL);
                    responseBean.setErrormsg("修改数字展馆失败");
                }
            }else if("add".equalsIgnoreCase(type)){
                String id = commService.getKey("wh_szzg_exhhall");
                WhSzzgExhhall whSzzgExhhall = new WhSzzgExhhall();
                whSzzgExhhall.setId(id);
                whSzzgExhhall.setHalladdress(halladdress);
                whSzzgExhhall.setHallcover(hallcover);
                whSzzgExhhall.setHallexterior360(hallexterior360);
                whSzzgExhhall.setHallinterior360(hallInterior360);
                whSzzgExhhall.setHallname(hallname);
                whSzzgExhhall.setHallphone(hallphone);
                whSzzgExhhall.setHallsummary(hallsummary);
                whSzzgExhhall.setLastoperator(whgSysUser.getId());
                whSzzgExhhall.setLastoperatortime(new Date());
                whSzzgExhhall.setIsdel(0);
                if("save".equalsIgnoreCase(editType)){
                    whSzzgExhhall.setHallstate(0);//编辑中
                }else {
                    whSzzgExhhall.setHallstate(1);//送审
                }
                whgPavilionInfoService.addOnePavilion(whSzzgExhhall);
            }
        }catch (Exception e){
            logger.error(e.toString());
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("添加数字展馆失败");
        }
        return responseBean;
    }

    @RequestMapping("do/updstate")
    public ResponseBean doUpdateState(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String id = request.getParameter("id");
        String toState = request.getParameter("toState");
        if(null == id){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("修改数字展馆状态失败");
            return responseBean;
        }
        try {
            if(null != toState && StringUtil.isNumeric(toState)){
                Integer hallState = Integer.valueOf(toState);
                if(0 != whgPavilionInfoService.updatePavilionStateById(id,hallState)){
                    responseBean.setSuccess(ResponseBean.FAIL);
                    responseBean.setErrormsg("修改数字展馆状态失败");
                }
            }
            return responseBean;
        }catch (Exception e){
            logger.error(e.toString());
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("修改数字展馆状态失败");
            return responseBean;
        }
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
            responseBean.setErrormsg("修改数字展馆删除状态失败");
            return responseBean;
        }
        try {
            if("1".equals(isdel)){
                //删除
                if(0 != whgPavilionInfoService.updatePavilionIsDelById(id,1)){
                    responseBean.setSuccess(ResponseBean.FAIL);
                    responseBean.setErrormsg("修改数字展馆删除状态失败");
                    return responseBean;
                }
            }else if("2".equals(isdel)){
                //还原
                if(0 != whgPavilionInfoService.updatePavilionIsDelById(id,0)){
                    responseBean.setSuccess(ResponseBean.FAIL);
                    responseBean.setErrormsg("修改数字展馆删除状态失败");
                    return responseBean;
                }
            }else if("3".equals(isdel)){
                //永久删除
                if(0 != whgPavilionInfoService.updatePavilionIsDelById(id,2)){
                    responseBean.setSuccess(ResponseBean.FAIL);
                    responseBean.setErrormsg("修改数字展馆删除状态失败");
                    return responseBean;
                }
            }
            return responseBean;
        }catch (Exception e){
            logger.error(e.toString());
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("修改数字展馆删除状态失败");
            return responseBean;
        }
    }
}
