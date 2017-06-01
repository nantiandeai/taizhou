package com.creatoo.hn.actions.api.index;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.model.WhgActActivity;
import com.creatoo.hn.model.WhgYwiLbt;
import com.creatoo.hn.services.comm.CommPropertiesService;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.services.home.agdindex.IndexPageService;
import com.creatoo.hn.services.home.userCenter.UserCenterService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**首页控制器
 * Created by caiyong on 2017/5/3.
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/api/index")
public class APIIndexAction {

    private static Logger logger = Logger.getLogger(APIIndexAction.class);
    /**
     * 首页数据服务
     */
    @Autowired
    private IndexPageService pageService;

    /**
     * 用户信息服务
     */
    @Autowired
    private UserCenterService userCenterService;

    /**
     * 公共服务
     */
    @Autowired
    private CommService commservice;

    @Autowired
    private CommPropertiesService commPropertiesService;

    /**
     * 首页数据
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping("/list")
    public ResponseBean list(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        Map map = new HashMap();
        try {
            List<WhgActActivity> actList = pageService.findActivity(4);
            map.put("activity",actList);
            List<Map> venList = pageService.selectVenueForIndexPage();
            map.put("venue",venList);
            List<WhgYwiLbt> lbt = pageService.findLBT("2",4);
            map.put("lbt",lbt);

            responseBean.setData(map);
            return responseBean;
        }catch (Exception e){
            logger.error(e.toString());
            responseBean.setSuccess("101");
            responseBean.setErrormsg("数据获取失败");
            return responseBean;
        }
    }

    /**
     *
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/showAdver", method = RequestMethod.POST)
    public ResponseBean showAdver(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        try {
            List<WhgYwiLbt> lbt = pageService.findLBT("2",4);
            Map map = new HashMap();
            map.put("staticServerUrl",commPropertiesService.getUploadLocalServerAddr());
            map.put("lbt",lbt);
            responseBean.setData(map);
            return responseBean;
        }catch (Exception e){
            logger.error(e.toString());
            responseBean.setSuccess("101");
            responseBean.setErrormsg("获取广告图失败");
            return responseBean;
        }
    }
}
