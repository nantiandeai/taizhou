package com.creatoo.hn.actions.admin.pavilion;

import com.alibaba.fastjson.JSON;
import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.ext.emun.EnumTypeClazz;
import com.creatoo.hn.model.WhBranchRel;
import com.creatoo.hn.model.WhSzzgExhibit;
import com.creatoo.hn.model.WhgSysUser;
import com.creatoo.hn.model.WhgYwiKey;
import com.creatoo.hn.services.admin.branch.BranchService;
import com.creatoo.hn.services.admin.pavilion.WhgAntiquesInfoService;
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
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**藏品控制器
 * Created by caiyong on 2017/4/26.
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/admin/antiques")
public class WhgAntiquesAction {

    private static Logger logger = Logger.getLogger(WhgAntiquesAction.class);

    @Autowired
    private CommService commService;

    @Autowired
    private WhgAntiquesInfoService whgAntiquesInfoService;

    @Autowired
    private WhgPavilionInfoService whgPavilionInfoService;

    @Autowired
    private BranchService branchService;

    @RequestMapping("/view/{type}")
    public ModelAndView listview(HttpServletRequest request, ModelMap mmp, @PathVariable("type")String type){
        ModelAndView view = new ModelAndView();
        try {
            mmp.addAttribute("type", type);
            view.setViewName("admin/antiques/list");
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
        } else if("recycle".equals(type)){
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
        WhgSysUser whgSysUser = (WhgSysUser)request.getSession().getAttribute("user");
        PageInfo myPage = whgAntiquesInfoService.getInfoList(Integer.valueOf(page),Integer.valueOf(rows),zxstate,whgSysUser.getId());
        responseBean.setRows((List)myPage.getList());
        responseBean.setTotal(myPage.getTotal());
        return responseBean;
    }

    @RequestMapping("/edit/{type}")
    public ModelAndView editview(HttpServletRequest request, ModelMap mmp, @PathVariable("type")String type){
        ModelAndView view = new ModelAndView();
        try {
            mmp.addAttribute("type", type);
            if(type.equals("edit") || type.equals("view")){
                String id = request.getParameter("id");
                if(null != id){
                    Map whSzzgExhibit = whgAntiquesInfoService.getOneExhbit(id);
                    if(null != whSzzgExhibit){
                        mmp.addAttribute("id",whSzzgExhibit.get("id"));
                        mmp.addAttribute("exhname",whSzzgExhibit.get("exhname"));
                        mmp.addAttribute("exhsummary",whSzzgExhibit.get("exhsummary"));
                        mmp.addAttribute("exhtype",whSzzgExhibit.get("exhtype"));
                        mmp.addAttribute("exhtheme",whSzzgExhibit.get("exhtheme"));
                        mmp.addAttribute("exhphoto",whSzzgExhibit.get("exhphoto"));
                        mmp.addAttribute("exhstate",whSzzgExhibit.get("exhstate"));
                        mmp.addAttribute("exhsort",whSzzgExhibit.get("exhsort"));
                        mmp.addAttribute("isrecommend",whSzzgExhibit.get("isrecommend"));
                        mmp.addAttribute("lastoperator",whSzzgExhibit.get("lastoperator"));
                        mmp.addAttribute("hallid",whSzzgExhibit.get("hallid"));
                        mmp.addAttribute("hallname",whSzzgExhibit.get("hallname"));
                    }
                }
            }
            List<WhgYwiKey> whgYwiKeyList = whgAntiquesInfoService.getAllExhType();
            if(null != whgYwiKeyList){
                mmp.addAttribute("whgYwiKeyList", JSON.toJSONString(whgYwiKeyList));
            }
            view.setViewName("admin/antiques/edit");
        }catch (Exception e){
            logger.error(e.toString());
        }
        return view;
    }

    @RequestMapping("/do/edit")
    public ResponseBean doEdit(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String type = request.getParameter("type");
        String editType = request.getParameter("editType");
        Map map = new HashMap();
        Class myClass = WhSzzgExhibit.class;
        Field[] fields = myClass.getDeclaredFields();
        for(Field field : fields){
            String name = field.getName();
            String value = request.getParameter(name);
            if(null != value && !value.isEmpty()){
                map.put(name,value);
            }
        }
        if("save".equalsIgnoreCase(editType)){
            map.put("exhstate",0);
        }else{
            map.put("exhstate",1);
        }
        map.put("isdel",0);
        WhgSysUser whgSysUser =  (WhgSysUser) request.getSession().getAttribute("user");
        try {
            if("edit".equalsIgnoreCase(type)){
                map.put("lastoperator",whgSysUser.getId());
                if(0 != whgAntiquesInfoService.updateOneExhbitMapper(map)){
                    responseBean.setSuccess(ResponseBean.FAIL);
                    responseBean.setErrormsg("修改藏品失败");
                }
                branchService.clearBranchRel((String )map.get("id"),EnumTypeClazz.TYPE_EXHIBIT.getValue());
                String hallid = (String )map.get("hallid");
                WhBranchRel whBranchRel = branchService.getBranchRel(hallid,EnumTypeClazz.TYPE_HALL.getValue());
                if(null != whBranchRel){
                    branchService.setBranchRel((String )map.get("id"),EnumTypeClazz.TYPE_EXHIBIT.getValue(),whBranchRel.getBranchid());
                }
            }else if("add".equalsIgnoreCase(type)){
                String id = commService.getKey("wh_szzg_exhibit");
                map.put("id",id);
                map.put("lastoperator",whgSysUser.getId());
                if(0 != whgAntiquesInfoService.addOneExhbitMapper(map)){
                    responseBean.setSuccess(ResponseBean.FAIL);
                    responseBean.setErrormsg("添加藏品失败");
                }
                String hallid = (String )map.get("hallid");
                WhBranchRel whBranchRel = branchService.getBranchRel(hallid,EnumTypeClazz.TYPE_HALL.getValue());
                if(null != whBranchRel){
                    branchService.setBranchRel(id,EnumTypeClazz.TYPE_EXHIBIT.getValue(),whBranchRel.getBranchid());
                }
            }
        }catch (Exception e){
            logger.error(e.toString());
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("添加藏品失败");
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
            responseBean.setErrormsg("修改藏品状态失败");
            return responseBean;
        }
        try {
            if(null != toState && StringUtil.isNumeric(toState)){
                Integer exhstate = Integer.valueOf(toState);
                if(0 != whgAntiquesInfoService.changeOneExhbitState(id,exhstate)){
                    responseBean.setSuccess(ResponseBean.FAIL);
                    responseBean.setErrormsg("修改藏品状态失败");
                }
            }
            return responseBean;
        }catch (Exception e){
            logger.error(e.toString());
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("修改藏品状态失败");
            return responseBean;
        }
    }

    @RequestMapping("/getPavilionList")
    public ResponseBean getPavilionList(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String page = request.getParameter("page");
        String rows = request.getParameter("rows");
        if(null == page || page.isEmpty()){
            page = "1";
        }
        if(null == rows || rows.isEmpty()){
            rows = "10";
        }
        WhgSysUser whgSysUser = (WhgSysUser)request.getSession().getAttribute("user");
        List<Map> relList = branchService.getBranchRelList(whgSysUser.getId(), EnumTypeClazz.TYPE_HALL.getValue());
        PageInfo myPage = whgPavilionInfoService.getInfoList(Integer.valueOf(page),Integer.valueOf(rows),2,relList);
        if(null == myPage){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("获取展馆状态失败");
        }
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
            responseBean.setErrormsg("修改藏品删除状态失败");
            return responseBean;
        }
        try {
            if("1".equals(isdel)){
                //删除
                if(0 != whgAntiquesInfoService.changeOneExhbitDelState(id,1)){
                    responseBean.setSuccess(ResponseBean.FAIL);
                    responseBean.setErrormsg("修改藏品删除状态失败");
                    return responseBean;
                }
            }else if("2".equals(isdel)){
                //还原
                if(0 != whgAntiquesInfoService.changeOneExhbitDelState(id,0)){
                    responseBean.setSuccess(ResponseBean.FAIL);
                    responseBean.setErrormsg("修改藏品删除状态失败");
                    return responseBean;
                }
            }else if("3".equals(isdel)){
                //永久删除
                if(0 != whgAntiquesInfoService.changeOneExhbitDelState(id,2)){
                    responseBean.setSuccess(ResponseBean.FAIL);
                    responseBean.setErrormsg("修改藏品删除状态失败");
                    return responseBean;
                }
            }
            return responseBean;
        }catch (Exception e){
            logger.error(e.toString());
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("修改藏品删除状态失败");
            return responseBean;
        }
    }
}
