package com.creatoo.hn.actions.admin.kulturbund;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.model.WhgCultureAct;
import com.creatoo.hn.model.WhgSysUser;
import com.creatoo.hn.services.admin.kulturbund.KulturbundService;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**文化联盟大型活动控制器
 * Created by caiyong on 2017/6/26.
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/admin/cultureact")
public class CultureActManager {
    private static Logger logger = Logger.getLogger(CultureActManager.class);

    @Autowired
    private KulturbundService kulturbundService;

    /**
     * 列表页面入口
     * @param request
     * @param type
     * @return
     */
    @RequestMapping("/list/{type}")
    public ModelAndView actList(HttpServletRequest request, @PathVariable("type") String type){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("listType",type);
        modelAndView.setViewName("admin/kulturbund/cultureact/view_list");
        return modelAndView;
    }

    /**
     * 获取列表数据
     * @param request
     * @return
     */
    @RequestMapping("/srchList4p")
    public ResponseBean srchList4p(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String name = getParam(request,"name",null);
        String listType = getParam(request,"listType",null);
        try {
            Integer page = Integer.valueOf(this.getParam(request,"page","1"));
            Integer rows = Integer.valueOf(this.getParam(request,"rows","10"));
            PageInfo pageInfo = kulturbundService.getCultureAct(page,rows,name,listType);
            responseBean.setRows((List)pageInfo.getList());
            responseBean.setTotal(pageInfo.getTotal());
            responseBean.setPage(pageInfo.getPageNum());
            responseBean.setPageSize(pageInfo.getPageSize());
            return responseBean;
        }catch (Exception e){
            logger.error(e.toString());
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("获取文化联盟单位失败");
            return responseBean;
        }
    }

    /**
     * 文化联盟大型活动编辑页面
     * @param request
     * @param type
     * @return
     */
    @RequestMapping("/edit/{type}")
    public ModelAndView editView(HttpServletRequest request, @PathVariable("type") String type) {
        ModelAndView modelAndView = new ModelAndView();
        String id = getParam(request,"id",null);
        modelAndView.addObject("editType",type);
        if(null != id){
            WhgCultureAct param = new WhgCultureAct();
            param.setId(id);
            WhgCultureAct whgCultureAct = kulturbundService.getOne(param);
            if(null != whgCultureAct){
                modelAndView.addObject("whgCultureAct",whgCultureAct);
            }
        }
        modelAndView.setViewName("admin/kulturbund/cultureact/view_edit");
        return modelAndView;
    }

    /**
     * 编辑提交操作
     * @param request
     * @param type
     * @return
     */
    @RequestMapping("/doEdit/{type}")
    public ResponseBean doEdit(HttpServletRequest request,@PathVariable("type") String type){
        ResponseBean responseBean = new ResponseBean();
        String id = getParam(request,"id",null);
        String culactname = getParam(request,"culactname",null);
        String culactcover = getParam(request,"culactcover",null);
        String remark = getParam(request,"remark",null);
        WhgSysUser whgSysUser = (WhgSysUser)request.getSession().getAttribute("user");
        try {
            if("add".equals(type)){
                WhgCultureAct whgCultureAct = new WhgCultureAct();
                whgCultureAct.setCulactname(culactname);
                whgCultureAct.setCulactcover(culactcover);
                whgCultureAct.setCulactcontent(remark);
                whgCultureAct.setCulactuser(whgSysUser.getId());
                if(0 != kulturbundService.doAdd(whgCultureAct)){
                    responseBean.setSuccess(ResponseBean.FAIL);
                    responseBean.setErrormsg("添加失败");
                }
            }else if("edit".equals(type)){
                WhgCultureAct whgCultureAct = new WhgCultureAct();
                whgCultureAct.setId(id);
                whgCultureAct.setCulactname(culactname);
                whgCultureAct.setCulactcover(culactcover);
                whgCultureAct.setCulactcontent(remark);
                whgCultureAct.setCulactuser(whgSysUser.getId());
                if(0 != kulturbundService.doEdit(whgCultureAct)){
                    responseBean.setSuccess(ResponseBean.FAIL);
                    responseBean.setErrormsg("编辑失败");
                }
            }else {
                responseBean.setSuccess(ResponseBean.FAIL);
                responseBean.setErrormsg("无此操作");
            }
            return responseBean;
        }catch (Exception e){
            logger.error(e.toString());
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("操作失败");
            return responseBean;
        }
    }

    /**
     * 修改状态操作
     * @param request
     * @return
     */
    @RequestMapping("/doUpdateState")
    public ResponseBean doUpdateState(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String id = getParam(request,"id",null);
        String state = getParam(request,"state",null);
        if(null == id || null == state){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("参数不足");
            return responseBean;
        }
        WhgCultureAct whgCultureAct = new WhgCultureAct();
        whgCultureAct.setId(id);
        if("initial".equals(state)){
            whgCultureAct.setCulactstate(0);
        }else if("checkpending".equals(state)){
            whgCultureAct.setCulactstate(1);
        }else if("checked".equals(state)){
            whgCultureAct.setCulactstate(2);
        }else if("published".equals(state)){
            whgCultureAct.setCulactstate(3);
        }
        if(0 != kulturbundService.updateState(whgCultureAct)){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("修改状态失败");
        }
        return responseBean;
    }

    /**
     * 修改状态操作
     * @param request
     * @return
     */
    @RequestMapping("/doDel")
    public ResponseBean doDel(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String id = getParam(request,"id",null);
        String state = getParam(request,"state",null);
        if(null == id || null == state){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("参数不足");
            return responseBean;
        }
        WhgCultureAct whgCultureAct = new WhgCultureAct();
        whgCultureAct.setId(id);
        if("del".equals(state)){
            whgCultureAct.setIsdel(1);
        }else if("undel".equals(state)){
            whgCultureAct.setIsdel(2);
        }else if("delforever".equals(state)){
            if(0 != kulturbundService.doDel(whgCultureAct)){
                responseBean.setSuccess(ResponseBean.FAIL);
                responseBean.setErrormsg("删除失败");
            }
            return responseBean;
        }
        if(0 != kulturbundService.doDel(whgCultureAct)){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("修改状态失败");
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
