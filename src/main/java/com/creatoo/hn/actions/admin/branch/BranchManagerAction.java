package com.creatoo.hn.actions.admin.branch;

import com.alibaba.fastjson.JSON;
import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.model.WhBranch;
import com.creatoo.hn.model.WhTyp;
import com.creatoo.hn.model.WhgSysUser;
import com.creatoo.hn.services.admin.branch.BranchService;
import com.creatoo.hn.services.comm.CommService;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**分馆管理控制器
 * Created by caiyong on 2017/6/1.
 */
@SuppressWarnings("all")
@Controller
@RequestMapping("/admin/branch")
public class BranchManagerAction {

    private static Logger logger = Logger.getLogger(BranchManagerAction.class);

    @Autowired
    private BranchService branchService;

    @Autowired
    private CommService commService;

    /**
     * 列表页
     * @param request
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView getPage(HttpServletRequest request){
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("admin/branch/view_list");
            List<WhTyp> areaList = branchService.getArea();
            if(null != areaList){
                modelAndView.addObject("areaList", JSON.toJSONString(areaList));
            }
            return modelAndView;
    }

    /**
     * 分馆列表
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/branchList")
    public ResponseBean branchList(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String pageNo = getParam(request,"page","1");
        String pageSize = getParam(request,"rows","10");
        try {
            PageInfo myPage = branchService.getBranchList(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
            if(null == myPage){
                responseBean.setSuccess(ResponseBean.FAIL);
                responseBean.setErrormsg("获取分馆列表失败");
            }else {
                responseBean.setRows((List)myPage.getList());
                responseBean.setPage(myPage.getPageNum());
                responseBean.setPageSize(myPage.getPageSize());
                responseBean.setTotal(myPage.getTotal());
            }
        }catch (Exception e){
            logger.error(e.toString());
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("获取分馆列表失败");
        }finally {
            return responseBean;
        }
    }

    /**
     * 获取所有的分馆
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/branchListAll")
    public ResponseBean branchListAll(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        try {
            List<Map> list = branchService.getBranchListAll(new HashMap());
            responseBean.setRows(list);
        }catch (Exception e){
            logger.error(e.toString());
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("获取分馆信息失败");
        }finally {
            return responseBean;
        }
    }

    /**
     * 获取所有已启用的分馆
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/branchListStarted")
    public ResponseBean branchListStarted(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        try {
            Map param = new HashMap();
            param.put("state","1");
            List<Map> list = branchService.getBranchListAll(param);
            responseBean.setRows(list);
        }catch (Exception e){
            logger.error(e.toString());
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("获取分馆信息失败");
        }finally {
            return responseBean;
        }
    }


    @ResponseBody
    @RequestMapping("/saveBranch")
    public ResponseBean saveBranch(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String saveType = getParam(request,"saveType",null);
        String id = getParam(request,"id",null);
        String name = getParam(request,"name",null);
        String areaid = getParam(request,"areaid",null);
        String contacts = getParam(request,"contacts",null);
        String contactway = getParam(request,"contactway",null);
        String state = getParam(request,"state",null);
        if(null == saveType){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("操作失败");
            return responseBean;
        }
        WhgSysUser whgSysUser = (WhgSysUser)request.getSession().getAttribute("user");
        if("add".equals(saveType)){
            if(null == name || null == areaid){
                responseBean.setSuccess(ResponseBean.FAIL);
                responseBean.setErrormsg("添加分馆失败");
                return responseBean;
            }
            WhBranch whBranch = new WhBranch();
            try {
                whBranch.setId(commService.getKey("wh_branch"));
                whBranch.setName(name);
                whBranch.setAreaid(areaid);
                whBranch.setContacts(contacts);
                whBranch.setContactway(contactway);
                whBranch.setState("0");
                whBranch.setLastoperator(whgSysUser.getId());
                whBranch.setOperatetime(new Date());
                branchService.saveBranch(whBranch,1);
            }catch (Exception e){
                logger.error(e.toString());
                responseBean.setSuccess(ResponseBean.FAIL);
                responseBean.setErrormsg("添加分馆失败");
            }
            return responseBean;
        }

        if("edit".equals(saveType)){
            if(null == id){
                responseBean.setSuccess(ResponseBean.FAIL);
                responseBean.setErrormsg("修改分馆失败");
                return responseBean;
            }
            try {
                WhBranch whBranch = new WhBranch();
                whBranch.setId(id);
                if(null != name){
                    whBranch.setName(name);
                }
                if(null != areaid){
                    whBranch.setAreaid(areaid);
                }
                if(null != contacts){
                    whBranch.setContacts(contacts);
                }
                if(null != contactway){
                    whBranch.setContactway(contactway);
                }
                if(null != state){
                    whBranch.setState(state);
                }
                whBranch.setLastoperator(whgSysUser.getId());
                whBranch.setOperatetime(new Date());
                branchService.saveBranch(whBranch,2);
            }catch (Exception e){
                logger.error(e.toString());
                responseBean.setSuccess(ResponseBean.FAIL);
                responseBean.setErrormsg("修改分馆失败");
            }
            return responseBean;
        }
        if("del".equals(saveType)){
            if(null == id){
                responseBean.setSuccess(ResponseBean.FAIL);
                responseBean.setErrormsg("删除分馆失败");
                return responseBean;
            }
            try {
                WhBranch whBranch = new WhBranch();
                whBranch.setId(id);
                branchService.saveBranch(whBranch,3);
            }catch (Exception e){
                logger.error(e.toString());
                responseBean.setSuccess(ResponseBean.FAIL);
                responseBean.setErrormsg("删除分馆失败");
            }
            return responseBean;
        }
        responseBean.setSuccess(ResponseBean.FAIL);
        responseBean.setErrormsg("无此操作");
        return responseBean;
    }

    private String getParam(HttpServletRequest request,String paramName,String defaultValue){
        String value = request.getParameter(paramName);
        if(null == value || value.trim().isEmpty()){
            return defaultValue;
        }
        return value;
    }

}
