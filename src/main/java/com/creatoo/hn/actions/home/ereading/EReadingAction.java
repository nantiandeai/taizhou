package com.creatoo.hn.actions.home.ereading;

import com.alibaba.fastjson.JSON;
import com.creatoo.hn.ext.bean.RetMobileEntity;
import com.creatoo.hn.model.WhgYwiType;
import com.creatoo.hn.services.home.adgszyd.SzydService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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

    /**
     * 数字阅读前端接口
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/szreading", method = RequestMethod.POST)
    public RetMobileEntity reading(){
        RetMobileEntity res = new RetMobileEntity();
        Map<String, Object> rest = new HashMap();

        List<Map> infoList = szydService.getReadingInfoList(1,20);

        List<Map> carousel = szydService.getReadingCarousel(1,1);

        rest.put("infoList", infoList);
        rest.put("carousel", carousel);
        res.setData(rest);
        res.setCode(0);
        return res;
    }

}
