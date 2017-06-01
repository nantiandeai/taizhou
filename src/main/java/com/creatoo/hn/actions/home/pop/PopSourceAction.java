package com.creatoo.hn.actions.home.pop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.services.comm.CommPropertiesService;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**热门资源控制器
 * Created by caiyong on 2017/5/12.
 */
@RestController
@RequestMapping("/pop")
public class PopSourceAction {

    private static Logger logger = Logger.getLogger(PopSourceAction.class);

    @Autowired
    private CommPropertiesService commPropertiesService;

    /**
     * 热门资源首页
     * @param request
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request){
        ModelAndView view = new ModelAndView( "home/pop/index" );
        view.addObject("popSourceDoc",commPropertiesService.getPopSourceDocPre());
        return view;
    }

    @SuppressWarnings("all")
    @ResponseBody
    @RequestMapping("/list")
    public ResponseBean list(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String page = getParam(request,"page","1");
        String rows = getParam(request,"rows","20");
        page = updatePageNo(page);
        String value = commPropertiesService.getPopularreSources();
        value = org.apache.commons.lang.StringUtils.replace(value,"PAGE",page);
        value = org.apache.commons.lang.StringUtils.replace(value,"SIZE",rows);
        try {
            Request myRequest = new Request.Builder().url(value).build();
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(myRequest).execute();
            if (response.isSuccessful()) {
                String myBody = response.body().string();
                JSONObject jsonObject = JSON.parseObject(myBody);
                responseBean.setCode("0");
                responseBean.setData(jsonObject);
                return responseBean;
            } else {
                responseBean.setCode("101");
                responseBean.setErrormsg("获取热门资源失败");
                return responseBean;
            }
        }catch (Exception e){
            logger.error(e.toString());
            responseBean.setCode("101");
            responseBean.setErrormsg("获取热门资源失败");
            return responseBean;
        }
    }

    private String updatePageNo(String page){
        try {
            Integer temp = Integer.valueOf(page);
            return String.valueOf(temp - 1);
        }catch (Exception e){
            logger.error(e.toString());
            return "0";
        }

    }
    /**
     * 获取参数
     * @param request
     * @param paramName
     * @return
     */
    private String getParam(HttpServletRequest request,String paramName){
        String value = request.getParameter(paramName);
        if(null != value && !value.isEmpty()){
            return value;
        }
        return null;
    }

    /**
     * 获取参数，带默认值
     * @param request
     * @param paramName
     * @param myDefault
     * @return
     */
    private String getParam(HttpServletRequest request,String paramName,String myDefault){
        String value = request.getParameter(paramName);
        if(null != value && !value.isEmpty()){
            return value;
        }
        return myDefault;
    }
}
