package com.creatoo.hn.actions.admin.kulturbund;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**文化联盟资讯控制器
 * Created by caiyong on 2017/6/29.
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/admin/cultureinfo")
public class CultureInfoManager {

    private static Logger logger = Logger.getLogger(CultureInfoManager.class);

    @CrossOrigin
    @RequestMapping(value = "/list/{type}")
    public ModelAndView infoList(HttpServletRequest request,@PathVariable("type") String type){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("listType",type);
        modelAndView.setViewName("admin/kulturbund/cultureinfo/view_list");
        return modelAndView;
    }

}
