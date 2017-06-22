package com.creatoo.hn.actions.admin.kulturbund;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.model.WhgCultureUnit;
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

/**文化联盟单位管理
 * Created by caiyong on 2017/6/21.
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/admin/cultureunit")
public class CultureUnitManager {

    private static Logger logger = Logger.getLogger(CultureUnitManager.class);

    @Autowired
    private KulturbundService kulturbundService;

    @RequestMapping("/view/{type}")
    public ModelAndView listView(HttpServletRequest request, @PathVariable("type") String type) {
        ModelAndView modelAndView = new ModelAndView();
        if("list".equals(type)){
            modelAndView.setViewName("admin/kulturbund/cultureunit/view_list");
        }
        return modelAndView;
    }

    @RequestMapping("/edit/{type}")
    public ModelAndView editView(HttpServletRequest request, @PathVariable("type") String type) {
        ModelAndView modelAndView = new ModelAndView();
        String id = getParam(request,"id",null);
        modelAndView.addObject("editType",type);
        if(null != id){
            WhgCultureUnit param = new WhgCultureUnit();
            param.setId(id);
            WhgCultureUnit whgCultureUnit = kulturbundService.getOne(param);
            if(null != whgCultureUnit){
                modelAndView.addObject("whgCultureUnit",whgCultureUnit);
            }
        }
        modelAndView.setViewName("admin/kulturbund/cultureunit/view_edit");
        return modelAndView;
    }

    @RequestMapping("/srchList4p")
    public ResponseBean srchList4p(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String name = getParam(request,"name",null);
        String state = getParam(request,"state",null);
        try {
            Integer page = Integer.valueOf(this.getParam(request,"page","1"));
            Integer rows = Integer.valueOf(this.getParam(request,"rows","10"));
            PageInfo pageInfo = kulturbundService.getCultureUnit(page,rows,name,state);
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

    @RequestMapping("/doEdit")
    public ResponseBean doEdit(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String editType = getParam(request,"editType",null);
        if(null == editType){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("提交失败");
            return responseBean;
        }
        try {
            String id = getParam(request,"id",null);
            String unitname = getParam(request,"unitname",null);
            String unitdesc = getParam(request,"unitdesc",null);
            String unitcover = getParam(request,"unitcover",null);
            String unitlink = getParam(request,"unitlink",null);
            String unitcode = getParam(request,"unitcode",null);
            if("add".equals(editType)){
                WhgCultureUnit whgCultureUnit = new WhgCultureUnit();
                whgCultureUnit.setUnitname(unitname);
                whgCultureUnit.setUnitdesc(unitdesc);
                whgCultureUnit.setUnitcover(unitcover);
                whgCultureUnit.setUnitlink(unitlink);
                whgCultureUnit.setUnitcode(unitcode);
                kulturbundService.doAdd(whgCultureUnit);
            }else if("edit".equals(editType)){
                WhgCultureUnit whgCultureUnit = new WhgCultureUnit();
                whgCultureUnit.setId(id);
                whgCultureUnit.setUnitname(unitname);
                whgCultureUnit.setUnitdesc(unitdesc);
                whgCultureUnit.setUnitcover(unitcover);
                whgCultureUnit.setUnitlink(unitlink);
                whgCultureUnit.setUnitcode(unitcode);
                kulturbundService.doEdit(whgCultureUnit);
            }else {
                //无操作
                ;
            }
            return responseBean;
        }catch (Exception e){
            logger.error(e.toString());
            responseBean.setSuccess(ResponseBean.FAIL);
            if("add".equals(editType)){
                responseBean.setErrormsg("添加文化联盟单位失败");
            }else if("edit".equals(editType)){
                responseBean.setErrormsg("编辑文化联盟单位失败");
            }else {
                responseBean.setErrormsg("其他错误");
            }
            return responseBean;
        }
    }

    @RequestMapping("/updateState")
    public ResponseBean updateState(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String id = getParam(request,"id",null);
        String state = getParam(request,"state",null);
        if(null == id || null == state){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("参数不足");
            return responseBean;
        }
        try {
            WhgCultureUnit whgCultureUnit = new WhgCultureUnit();
            whgCultureUnit.setId(id);
            whgCultureUnit.setUnitstate(Integer.valueOf(state));
            kulturbundService.updateState(whgCultureUnit);
            return responseBean;
        }catch (Exception e){
            logger.error(e.toString());
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("修改状态失败");
            return responseBean;
        }
    }

    @RequestMapping("/delUnit")
    public ResponseBean delUnit(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String id = getParam(request,"id",null);
        if(null == id){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("参数不足");
            return responseBean;
        }
        try {
            WhgCultureUnit whgCultureUnit = new WhgCultureUnit();
            whgCultureUnit.setId(id);
            kulturbundService.delOne(whgCultureUnit);
            return responseBean;
        }catch (Exception e){
            logger.error(e.toString());
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("修改状态失败");
            return responseBean;
        }
    }

    private String getParam(HttpServletRequest request,String paramName,String defaultValue){
        String value = request.getParameter(paramName);
        if(null == value || value.trim().isEmpty()){
            return defaultValue;
        }
        return value;
    }

}
