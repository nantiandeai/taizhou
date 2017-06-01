package com.creatoo.hn.actions.admin.branch;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**分馆管理控制器
 * Created by caiyong on 2017/6/1.
 */
@SuppressWarnings("all")
@Controller
@RequestMapping("/admin/branch")
public class BranchManagerAction {

    private static Logger logger = Logger.getLogger(BranchManagerAction.class);

    @RequestMapping("/index")
    public ModelAndView getPage(HttpServletRequest request){
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("admin/branch/view_list");
            return modelAndView;
    }

}
