package com.creatoo.hn.actions.home.ereading;

import com.alibaba.fastjson.JSON;
import com.creatoo.hn.services.home.adgszyd.SzydService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**数字阅读控制器
 * Created by caiyong on 2017/4/25.
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/reading")
public class EReadingAction {

    private static Logger logger = Logger.getLogger(EReadingAction.class);

    @Autowired
    private SzydService szydService;

    @RequestMapping("/index")
    public ModelAndView getReadingPage(HttpServletRequest request){
        ModelAndView view = new ModelAndView( "home/reading/index" );
        //获取数字阅读资讯
        List<Map> infoList = szydService.getReadingInfoList(1,20);
        if(null != infoList){
            view.addObject("infoList", JSON.toJSONString(infoList));
        }

        List<Map> carousel = szydService.getReadingCarousel(1,1);
        if(null != carousel){
            view.addObject("carousel",JSON.toJSONString(carousel));
        }

        return view;
    }

}
