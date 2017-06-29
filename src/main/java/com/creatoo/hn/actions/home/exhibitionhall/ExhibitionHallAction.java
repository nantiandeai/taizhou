package com.creatoo.hn.actions.home.exhibitionhall;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.services.home.exhibitionhall.ExhibitionhallService;
import com.github.pagehelper.PageInfo;
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

/**数字展馆控制器
 * Created by caiyong on 2017/4/27.
 */
@SuppressWarnings("all")
@RestController
@RequestMapping("/exhibitionhall")
public class ExhibitionHallAction {

    private static Logger logger = Logger.getLogger(ExhibitionHallAction.class);

    @Autowired
    private ExhibitionhallService exhibitionhallService;

    @RequestMapping("/index")
    public ModelAndView getExhibitionHallPage(HttpServletRequest request){
        ModelAndView view = new ModelAndView( "home/exhibitionhall/index" );
        List<Map> photo360Link = exhibitionhallService.get360Photo();
        if(null != photo360Link){
            view.addObject("photo360Link",photo360Link);
        }
        PageInfo exhibitionHallPage = exhibitionhallService.getExhibitionhallList(1,6);//首页最多展示6个展馆
        view.addObject("exhibitionHallList",(List)exhibitionHallPage.getList());
        return view;
    }

    @RequestMapping("/list")
    public ModelAndView getExhibitionHallListPage(HttpServletRequest request){
        ModelAndView view = new ModelAndView( "home/exhibitionhall/halllist" );
        return view;
    }

    @CrossOrigin
    @RequestMapping(value = "/getHallList",method = RequestMethod.POST )
    public ResponseBean getHallList(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String page = request.getParameter("page");
        String rows = request.getParameter("pageSize");
        if(null == page || page.isEmpty()){
            page = "1";
        }
        if(null == rows || rows.isEmpty()){
            rows = "12";
        }
        try {
            List<Map> photo360Link = exhibitionhallService.get360Photo();
            PageInfo pageInfo = exhibitionhallService.getExhibitionhallList(Integer.valueOf(page),Integer.valueOf(rows));
            if(null == pageInfo){
                responseBean.setSuccess(ResponseBean.FAIL);
                responseBean.setErrormsg("查询展馆列表失败");
            }else {
                responseBean.setRows(photo360Link);
                responseBean.setData((List)pageInfo.getList());
                responseBean.setTotal(pageInfo.getTotal());
                responseBean.setPage(Integer.parseInt(page));
                responseBean.setPageSize(Integer.parseInt(rows));
            }
        }catch (Exception e){
            logger.error(e.toString());
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("查询展馆列表失败");
        }finally {
            return responseBean;
        }
    }

    @RequestMapping("/hallPage")
    public ModelAndView getHallPage(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView("home/exhibitionhall/hallPage");
        String id = request.getParameter("id");
        if(null != id && !id.isEmpty()){
            Map myHall = exhibitionhallService.getHallInfo(id);
            modelAndView.addObject("myHall",myHall);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/getExhibitList",method = RequestMethod.POST)
    @CrossOrigin
    public ResponseBean getExhibitList(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String id = request.getParameter("id");
        String page = request.getParameter("page");
        String rows = request.getParameter("pageSize");
        if (id == null && !"".equals(id)) {
            responseBean.setErrormsg("参数错误");
            return responseBean;
        }
        if(null == page || page.isEmpty()){
            page = "1";
        }
        if(null == rows || rows.isEmpty()){
            rows = "12";
        }
        try {
            PageInfo myPage = exhibitionhallService.getExhibitList(id,Integer.valueOf(page),Integer.valueOf(rows));
            if(null == myPage){
                responseBean.setSuccess(ResponseBean.FAIL);
                responseBean.setErrormsg("查询藏品列表失败");
            }else {
                responseBean.setRows((List)myPage.getList());
                responseBean.setTotal(myPage.getTotal());
                responseBean.setPageSize(Integer.parseInt(rows));
            }
        }catch (Exception e){
            logger.error(e.toString());
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("查询藏品列表失败");
        }
        return responseBean;
    }
}
