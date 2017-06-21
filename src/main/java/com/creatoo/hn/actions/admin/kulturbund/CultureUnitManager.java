package com.creatoo.hn.actions.admin.kulturbund;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**文化联盟单位管理
 * Created by caiyong on 2017/6/21.
 */
@RestController
@RequestMapping("/admin/cultureunit")
public class CultureUnitManager {

    @RequestMapping("/view/{type}")
    public ModelAndView listview(HttpServletRequest request, @PathVariable("type") String type) {
        ModelAndView modelAndView = new ModelAndView();
        if("list".equals(type)){
            modelAndView.setViewName("admin/kulturbund/cultureunit/view_list");
        }
        return modelAndView;
    }

}
