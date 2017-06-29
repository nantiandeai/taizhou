package com.creatoo.hn.actions.admin.kulturbund;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.model.WhgCultureZx;
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

/**文化联盟资讯控制器
 * Created by caiyong on 2017/6/29.
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/admin/cultureinfo")
public class CultureInfoManager {

    private static Logger logger = Logger.getLogger(CultureInfoManager.class);

    @Autowired
    private KulturbundService kulturbundService;

    /**
     * 资讯列表页
     * @param request
     * @param type
     * @return
     */
    @RequestMapping(value = "/list/{type}")
    public ModelAndView infoList(HttpServletRequest request,@PathVariable("type") String type){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("listType",type);
        modelAndView.setViewName("admin/kulturbund/cultureinfo/view_list");
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
            PageInfo pageInfo = kulturbundService.getCultureInfo(page,rows,name,listType);
            responseBean.setRows((List)pageInfo.getList());
            responseBean.setTotal(pageInfo.getTotal());
            responseBean.setPage(pageInfo.getPageNum());
            responseBean.setPageSize(pageInfo.getPageSize());
            return responseBean;
        }catch (Exception e){
            logger.error(e.toString());
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("获取文化联盟资讯失败");
            return responseBean;
        }
    }

    @RequestMapping("/edit/{type}")
    public ResponseBean edit(HttpServletRequest request,@PathVariable("type") String type){
        ResponseBean responseBean = new ResponseBean();
        String culzxtitle = getParam(request,"culzxtitle",null);
        String culzxdesc = getParam(request,"culzxdesc",null);
        String culzxlink = getParam(request,"culzxlink",null);
        String id = getParam(request,"id",null);
        WhgSysUser whgSysUser = (WhgSysUser)request.getSession().getAttribute("user");
        if("add".equals(type)){
            WhgCultureZx whgCultureZx = new WhgCultureZx();
            whgCultureZx.setCulzxtitle(culzxtitle);
            whgCultureZx.setCulzxdesc(culzxdesc);
            whgCultureZx.setCulzxlink(culzxlink);
            whgCultureZx.setCulzxcreator(whgSysUser.getId());
            if(0 != kulturbundService.doAdd(whgCultureZx)){
                responseBean.setSuccess(ResponseBean.FAIL);
                responseBean.setErrormsg("添加文化联盟资讯失败");
            }
        }else if("edit".equals(type)){
            WhgCultureZx whgCultureZx = new WhgCultureZx();
            whgCultureZx.setCulzxtitle(culzxtitle);
            whgCultureZx.setCulzxdesc(culzxdesc);
            whgCultureZx.setCulzxlink(culzxlink);
            whgCultureZx.setId(id);
            if(0 != kulturbundService.doEdit(whgCultureZx)){
                responseBean.setSuccess(ResponseBean.FAIL);
                responseBean.setErrormsg("修改文化联盟资讯失败");
            }
        }
        return responseBean;
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
        WhgCultureZx whgCultureZx = new WhgCultureZx();
        whgCultureZx.setId(id);
        if("initial".equals(state)){
            whgCultureZx.setCulzxstate(0);
        }else if("checkpending".equals(state)){
            whgCultureZx.setCulzxstate(1);
        }else if("checked".equals(state)){
            whgCultureZx.setCulzxstate(2);
        }else if("published".equals(state)){
            whgCultureZx.setCulzxstate(3);
        }
        if(0 != kulturbundService.updateState(whgCultureZx)){
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
        WhgCultureZx whgCultureZx = new WhgCultureZx();
        whgCultureZx.setId(id);
        if("del".equals(state)){
            whgCultureZx.setIsdel(1);
        }else if("undel".equals(state)){
            whgCultureZx.setIsdel(2);
        }else if("delforever".equals(state)){
            if(0 != kulturbundService.doDel(whgCultureZx)){
                responseBean.setSuccess(ResponseBean.FAIL);
                responseBean.setErrormsg("删除失败");
            }
            return responseBean;
        }
        if(0 != kulturbundService.doDel(whgCultureZx)){
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
