package com.creatoo.hn.actions.api.train;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.ext.emun.EnumTypeClazz;
import com.creatoo.hn.model.WhgTra;
import com.creatoo.hn.model.WhgTraCourse;
import com.creatoo.hn.model.WhgTraEnrol;
import com.creatoo.hn.model.WhgYwiLbt;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.services.home.agdpxyz.PxbmService;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提供培训报名接口
 * Created by wangxl on 2017/4/12.
 */
@RestController
@RequestMapping("/api/tra")
public class APITrainAction {
    /**
     * 日志控制器
     */
    Logger log = Logger.getLogger(this.getClass().getName());

    /**
     * 报名服务
     */
    @Autowired
    private PxbmService service;

    @Autowired
    private CommService commService;

    /**
     * 检查能否报名
     * 访问路径 /api/tra/check/{traId}/{userId}
     * @param traId 培训ID
     * @param userId 用户ID
     * @return JSON: {
     * "success" : "1"                        //1表示可以报名，其它失败
     * "errormsg" : "100|101|102|103|104"     //100-培训已失效; 101-培训报名已结束; 102-培训报名额已满; 103-已经报名不能重报; 104-未实名认证
     * }
     */
    @CrossOrigin
    @RequestMapping("/check/{traId}/{userId}")
    public ResponseBean check(@PathVariable("traId")String traId, @PathVariable("userId")String userId){
        ResponseBean res = new ResponseBean();
        try{
            String validCode = this.service.checkApplyTrain(traId, userId);
            if(!"0".equals(validCode)){
                res.setSuccess(ResponseBean.FAIL);
                res.setErrormsg(validCode);
            }
        } catch (Exception e){
            log.error(e.getMessage(), e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
        }
        return res;
    }

    /**
     * 报名(POST提交数据)
     * 访问路径 /api/tra/enrol
     * @return JSON : {
     * "success" : "1"                        //1表示报名成功，其它失败
     * "errormsg" : "100|101|102|103|104|105|106|107"
     *   //100-培训已失效; 101-培训报名已结束; 102-培训报名额已满; 103-已经报名不能重报; 104-未实名认证; 105-报名失败 106-年龄段不符合培训要求 107-已报两次普及班
     * }
     */
    @CrossOrigin
    @RequestMapping(value = "/enrol", method = RequestMethod.POST)
    public ResponseBean enroll(WhgTraEnrol enrol, String userId){
        ResponseBean res = new ResponseBean();
        try{
            String validCode = this.service.checkApplyTrain(enrol.getTraid(), userId);
            if(!"0".equals(validCode)){
                res.setSuccess(ResponseBean.FAIL);
                res.setErrormsg(validCode);
            }else{
                try{
                    String birthday = new java.text.SimpleDateFormat("yyyy-MM-dd").format(enrol.getBirthday());
//                    validCode = service.validAge(enrol, birthday, userId);
                    if(!"0".equals(validCode)) {
                        res.setSuccess(ResponseBean.FAIL);
                        res.setErrormsg(validCode);
                    }else{
                        service.addTranEnrol(enrol, "", userId);
                        this.commService.addRepOrder(enrol.getTraid(),enrol.getId(), EnumTypeClazz.TYPE_TRAIN.getValue(),1);
                    }
                }catch (Exception e){
                    res.setSuccess(ResponseBean.FAIL);
                    res.setErrormsg(e.getMessage());
                }
            }
        } catch (Exception e){
            log.error(e.getMessage(), e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
        }
        return res;
    }

    /**
     *  培训订单号查询签名列表
     *  访问路径 /api/tra/signList/{orderId}
     * @param orderId 用户的培训订单号whg_tra_enrol.orderid
     * @return JSON : {
     * "success" : "1"                        //1表示请求成功，其它失败
     * "errormsg" : ""                        //请求失败时的异常信息
     *  "data": [
     *      {
     *          "traid":"培训ID",
     *          "enrolid":"报名ID",
     *          "courseid":"培训课程ID",
     *          "starttime":"课程开始时间yyyy-MM-dd HH:mm:ss",
     *          "endtime":"课程结束时间",
     *          "sign":"签到状态 0-未签到, 1-已签到"}
     *          "signtime":"签到时间yyyy-MM-dd HH:mm:ss",
     *          "userid":"签到会员ID"}
     *      ,...]                     //data=签名列表
     * }
     */
    @CrossOrigin
    @RequestMapping(value = "/signList/{orderId}")
    public ResponseBean signInfo(String orderId){
        ResponseBean res = new ResponseBean();
        try{
            List<Map<String, String>> eclist = this.service.queryEnrolCourseList(orderId);
            res.setData(eclist);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
        }
        return res;
    }

    /**
     * 培训课程签到
     * 访问路径 /api/tra/sign
     * @param trainId 培训ID whg_tra.id
     * @param enrolId 报名表ID whg_tra_enrol.id
     * @param courseId 培训报名中课程ID whg_tra_enrol_course.id
     * @param userId 用户ID wh_user.id
     * @return JSON ： {
     *  "success" : "1"             //1-签到成功, 其它签到失败
     *  "errormsg" : "100|101|102|103|109"             //100-签到成功; 101-已经签到; 102-已签到课程已取消; 109-签到异常
     * }
     */
    @CrossOrigin
    @RequestMapping(value = "/sign", method = RequestMethod.POST)
    public ResponseBean signUp(String trainId, String enrolId, String courseId, String userId){
        ResponseBean res = new ResponseBean();
        try{
            String code = this.service.signup(trainId, enrolId, courseId, userId);
            if(!"100".equals(code)){
                res.setSuccess(ResponseBean.FAIL);
            }
            res.setErrormsg(code);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("109");
        }
        return res;
    }

    /**
     * 获取培训首页数据
     * @param request
     * @return
     */
    @CrossOrigin
    @SuppressWarnings("all")
    @RequestMapping(value = "/indexData",method = RequestMethod.POST)
    public ResponseBean indexData(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        Map map = new HashMap();
        List<WhgYwiLbt> whgYwiLbtList = service.getLbt("12");
        if(null != whgYwiLbtList && !whgYwiLbtList.isEmpty()){
            map.put("whgYwiLbtList",whgYwiLbtList);
        }
        PageInfo pageInfo = service.getTraList(1,6,new HashMap());
        if(null != pageInfo){
            List list = pageInfo.getList();
            map.put("whgTraList",list);
        }
        responseBean.setData(map);
        return responseBean;
    }

    @CrossOrigin
    @SuppressWarnings("all")
    @RequestMapping(value = "/traListPt",method = RequestMethod.POST)
    public ResponseBean traListPt(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String userId = getParamValue(request,"userId",null);
        String index = getParamValue(request,"index","1");
        String size = getParamValue(request,"size","10");
        String type = getParamValue(request,"type",null);
        String district = getParamValue(request,"district",null);
        String sdate = getParamValue(request,"sdate",null);
        Map map = new HashMap();
        if(null != type){
            map.put("arttype",type);
        }
        if(null != district){
            map.put("area",district);
        }
        if(null != sdate){
            map.put("sdate",sdate);
        }
        map.put("isbasictra","0");
        try {
            PageInfo pageInfo = service.getTraList(Integer.valueOf(index),Integer.valueOf(size),map);
            if(null == pageInfo){
                responseBean.setSuccess(ResponseBean.FAIL);
                responseBean.setErrormsg("获取培训列表失败");
                return responseBean;
            }
            List traList = service.judgeCanSign(userId,(List)pageInfo.getList());
            responseBean.setRows(traList);
            responseBean.setPage(pageInfo.getPageNum());
            responseBean.setPageSize(pageInfo.getPageSize());
            responseBean.setTotal(pageInfo.getTotal());
            return responseBean;
        }catch (Exception e){
            log.error(e.toString());
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("获取培训列表失败");
            return responseBean;
        }
    }

    @CrossOrigin
    @SuppressWarnings("all")
    @RequestMapping(value = "/traListCulMarket",method = RequestMethod.POST)
    public ResponseBean traListCulMarket(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String userId = getParamValue(request,"userId",null);
        String index = getParamValue(request,"index","1");
        String size = getParamValue(request,"size","10");
        String type = getParamValue(request,"type",null);
        String district = getParamValue(request,"district",null);
        String sdate = getParamValue(request,"sdate",null);
        Map map = new HashMap();
        if(null != type){
            map.put("arttype",type);
        }
        if(null != district){
            map.put("area",district);
        }
        if(null != sdate){
            map.put("sdate",sdate);
        }
        map.put("isbasictra","1");
        try {
            PageInfo pageInfo = service.getTraList(Integer.valueOf(index),Integer.valueOf(size),map);
            if(null == pageInfo){
                responseBean.setSuccess(ResponseBean.FAIL);
                responseBean.setErrormsg("获取培训列表失败");
                return responseBean;
            }
            List traList = service.judgeCanSign(userId,(List)pageInfo.getList());
            WhgYwiLbt whgYwiLbt = service.getCulturalMarketLbt();
            if(null != whgYwiLbt){
                responseBean.setData(whgYwiLbt);
            }
            responseBean.setRows(traList);
            responseBean.setPage(pageInfo.getPageNum());
            responseBean.setPageSize(pageInfo.getPageSize());
            responseBean.setTotal(pageInfo.getTotal());
            return responseBean;
        }catch (Exception e){
            log.error(e.toString());
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("获取培训列表失败");
            return responseBean;
        }
    }

    /**
     * 获取培训详情
     * @param request
     * @return
     */
    @CrossOrigin
    @SuppressWarnings("all")
    @RequestMapping(value = "/traDetail",method = RequestMethod.POST)
    public ResponseBean traDetail(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String id = getParamValue(request,"id",null);
        String userId = getParamValue(request,"userId",null);
        if(null == id){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("培训ID不能为空");
            return responseBean;
        }
        WhgTra whgTra = service.getOneTra(id);
        if(null == whgTra){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("查询培训数据失败");
            return responseBean;
        }
        List<WhgTraCourse> whgTraCourseList = service.getCourseByTraId(whgTra.getId());
        Integer canSign = service.canSign(userId,whgTra);
        Map map = new HashMap();
        map.put("whgTra",whgTra);
        if(null != whgTraCourseList && !whgTraCourseList.isEmpty()){
            map.put("whgTraCourseList",whgTraCourseList);
        }
        if(null != canSign){
            map.put("canSign",canSign);
        }
        responseBean.setData(map);
        return responseBean;
    }

    /**
     * 获取培训课程
     * @param request
     * @return
     */
    @CrossOrigin
    @SuppressWarnings("all")
    @RequestMapping(value = "/traCourseList",method = RequestMethod.POST)
    public ResponseBean traCourseList(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String id = getParamValue(request,"id",null);
        String index = getParamValue(request,"index","1");
        String size = getParamValue(request,"size","10");
        if(null == id){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("培训ID不能为空");
            return responseBean;
        }
        PageInfo pageInfo = service.getCourseByTraId(Integer.valueOf(index),Integer.valueOf(size),id);
        if(null == pageInfo){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("查询培训课程失败");
            return responseBean;
        }
        responseBean.setRows((List)pageInfo.getList());
        responseBean.setPage(pageInfo.getPageNum());
        responseBean.setPageSize(pageInfo.getPageSize());
        responseBean.setTotal(pageInfo.getTotal());
        return responseBean;
    }

    /**
     * 获取推荐的培训
     * @param request
     * @return
     */
    @CrossOrigin
    @SuppressWarnings("all")
    @RequestMapping(value = "/getRecommendTra",method = RequestMethod.POST)
    public ResponseBean getRecommendTra(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        PageInfo pageInfo = service.getRecommendTra(1,10);
        if(null == pageInfo){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("获取推荐培训失败");
            return responseBean;
        }
        responseBean.setRows((List)pageInfo.getList());
        responseBean.setPage(pageInfo.getPageNum());
        responseBean.setPageSize(pageInfo.getPageSize());
        responseBean.setTotal(pageInfo.getTotal());
        return responseBean;
    }

    @CrossOrigin
    @SuppressWarnings("all")
    @RequestMapping(value = "/traSign",method = RequestMethod.POST)
    public ResponseBean traSign(HttpServletRequest request){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ResponseBean responseBean = new ResponseBean();
        String userId = getParamValue(request,"userId",null);
        String sex = getParamValue(request,"sex",null);
        String itemId = getParamValue(request,"itemId",null);
        String name = getParamValue(request,"name",null);
        String birthday = getParamValue(request,"birthday",null);
        String idCard = getParamValue(request,"idCard",null);
        String mobile = getParamValue(request,"mobile",null);

        if(!service.isLogin(userId)){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("未登录");
            return responseBean;
        }
        WhgTraEnrol whgTraEnrol = new WhgTraEnrol();
        whgTraEnrol.setUserid(userId);
        whgTraEnrol.setSex(Integer.valueOf(sex));
        whgTraEnrol.setTraid(itemId);
        whgTraEnrol.setCardno(idCard);
        whgTraEnrol.setRealname(name);
        whgTraEnrol.setContactphone(mobile);
        try {
            whgTraEnrol.setBirthday(simpleDateFormat.parse(birthday));
        }catch (Exception e){
            log.error(e.toString());
        }
        int res = service.addTraSign(whgTraEnrol);
        if(1 == res){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("报名失败");
        }else if(2 == res){
            responseBean.setSuccess(ResponseBean.FAIL);
            responseBean.setErrormsg("不能重复提交");
        }
        return responseBean;
    }

    /**
     * 查询一条培训报名记录
     * @param request
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/findTraEnrol",method = RequestMethod.POST)
    public ResponseBean findTraEnrol(HttpServletRequest request){
        ResponseBean responseBean = new ResponseBean();
        String traId = getParamValue(request,"traId",null);
        String userId = getParamValue(request,"userId",null);
        WhgTraEnrol whgTraEnrol = new WhgTraEnrol();
        whgTraEnrol.setTraid(traId);
        whgTraEnrol.setUserid(userId);
        WhgTraEnrol res = service.findTraEnrol(whgTraEnrol);
        if(null != res){
            responseBean.setData(res);
        }
        return responseBean;
    }

    /**
     * 获取请求的参数
     * @param request
     * @param paramName
     * @param defaultValue
     * @return
     */
    private String getParamValue(HttpServletRequest request,String paramName,String defaultValue){
        String value = request.getParameter(paramName);
        if(null == value || value.trim().isEmpty()){
            return defaultValue;
        }
        return value;
    }

    /**
     * 所有日期类型使用指定格式转换
     * @param webDataBinder
     * @throws Exception
     */
    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) throws Exception{
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setLenient(false);
        webDataBinder.registerCustomEditor(Date.class , new CustomDateEditor(simpleDateFormat , true));
    }
}
