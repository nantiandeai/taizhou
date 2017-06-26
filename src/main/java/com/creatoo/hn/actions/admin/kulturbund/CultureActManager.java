package com.creatoo.hn.actions.admin.kulturbund;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**文化联盟大型活动控制器
 * Created by caiyong on 2017/6/26.
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/admin/cultureact")
public class CultureActManager {
    private static Logger logger = Logger.getLogger(CultureActManager.class);

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
