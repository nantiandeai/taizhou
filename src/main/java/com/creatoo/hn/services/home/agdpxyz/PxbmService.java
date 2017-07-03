package com.creatoo.hn.services.home.agdpxyz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.ext.emun.EnumBMState;
import com.creatoo.hn.ext.emun.EnumDelState;
import com.creatoo.hn.ext.emun.EnumState;
import com.creatoo.hn.mapper.*;
import com.creatoo.hn.model.*;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.services.comm.SMSService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * 培训报名服务类
 * Created by tangwei on 2017/4/6.
 */
@Service
public class PxbmService {
    Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private CommService commService;

    @Autowired
    public WhgTraMapper whTrainMapper;

    @Autowired
    public WhgTraEnrolMapper whgTraEnrolMapper;

    /**
     * 培训课程DAO
     */
    @Autowired
    public WhgTraCourseMapper whgTraCourseMapper;

    /**
     * 培训报名签到DAO
     */
    @Autowired
    public WhgTraEnrolCourseMapper whgTraEnrolCourseMapper;

    @Autowired
    private WhUserMapper userMapper;

    @Autowired
    private SMSService smsService;

    @Autowired
    private WhgYwiLbtMapper whgYwiLbtMapper;

    @Autowired
    private WhgTraMapper whgTraMapper;

    private final  String SUCCESS="0";//成功

    /**
     * 检查培训报名
     * @param trainId
     * @return
     */
    public String checkApplyTrain(String trainId,String userId){
        WhgTra train = whTrainMapper.selectByPrimaryKey(trainId);
        //培训不存在,或者状态不是已发布
        if(train == null || train.getState() != 6){
            return "100";
        }
        //培训报名已结束
        if(train.getEnrollendtime().before(Calendar.getInstance().getTime())){
            return "101";
        }
        //培训报名额已满
        if(!checkTrainMaxNumber(train)){
            return "102";
        }
        //培训报名重复
        if(!checkExistEnrol(train.getId(),userId)){
            return "103";
        }
        //实名制验证
        if(train.getIsrealname() ==1){
            WhUser user = userMapper.selectByPrimaryKey(userId);
            if(user.getIsrealname() == null || user.getIsrealname().intValue() != 1){
                return "104";
            }
        }
        return SUCCESS;
    }

    /**
     * 根据ID获取培训信息
     * @param trainId
     * @return
     */
    public WhgTra getTrainById(String trainId){
        return whTrainMapper.selectByPrimaryKey(trainId);
    }

    /**
     * 检查是否超出报名名额
     * @param train
     */
    private Boolean checkTrainMaxNumber (WhgTra train){
        Example example  = new Example(WhgTraEnrol.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("traid", train.getId());
        int count = whgTraEnrolMapper.selectCountByExample(example);
        if(count >= train.getMaxnumber()){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 检查报名是否重复
     * @param trainId
     * @param userId
     * @return
     */
    private Boolean checkExistEnrol (String trainId,String userId){
        Example example  = new Example(WhgTraEnrol.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("traid",trainId);
        c.andEqualTo("userid",userId);
        c.andIn("state", Arrays.asList(1,4,6));
        int count =  whgTraEnrolMapper.selectCountByExample(example);
        return count == 0 ? Boolean.TRUE:Boolean.FALSE;
    }

    /**
     * 添加培训报名
     * @param enrol
     * @param enrolBirthdayStr
     * @param userId
     * @return
     * @throws Exception
     */
    public int addTranEnrol(WhgTraEnrol enrol,String enrolBirthdayStr,String userId)throws Exception{
        WhgTra train = getTrainById(enrol.getTraid());
        String id = commService.getKey("whg_tra_enrol");
        //订单号和id保持一致
        enrol.setId(id);
        enrol.setOrderid(id);
        int state = EnumBMState.BM_SQ.getValue();
        Date now = Calendar.getInstance().getTime();
        //先报先得培训,直接报名通过
        if(train.getIsbasicclass() == 2){
            state = EnumBMState.BM_CG.getValue();
        }
        if(StringUtils.isNotEmpty(enrolBirthdayStr)){
            String []parsePatterns ={"yyyy-MM-dd"};
            enrol.setBirthday(DateUtils.parseDate(enrolBirthdayStr,parsePatterns));
        }
        enrol.setState(state);
        enrol.setStatemdfdate(now);
        enrol.setStatemdfuser(userId);
        enrol.setUserid(userId);
        enrol.setCrttime(now);
        int result = whgTraEnrolMapper.insertSelective(enrol);
        //发送短信
        if(result > 0 && state == EnumBMState.BM_CG.getValue()){
            Map<String,String> _map = new HashMap<>();
            _map.put("name",enrol.getRealname());
            _map.put("title",train.getTitle());
            _map.put("starttime",DateFormatUtils.format(train.getStarttime(),"yyyy-MM-dd HH:mm:ss"));
            _map.put("endtime",DateFormatUtils.format(train.getStarttime(),"yyyy-MM-dd HH:mm:ss"));
            String tempCode = "TRA_VIEW_PASS";
            //this.smsService.t_sendSMS(enrol.getContactphone(),tempCode,_map);
            this.smsService.t_sendSMS(enrol.getContactphone(),tempCode,_map, enrol.getTraid());
        }
        return result;
    }

    /**
     * 培训签到
     * @param courseId 课程ID
     * @param userId 会员ID
     * @return 100-签到成功; 101-已经签到; 102-已签到课程已取消; 109-签到异常
     * @throws Exception
     */
    public String signup(String traId, String enrolId, String courseId, String userId)throws Exception{
        String rtnCode = "100";//签到成功

        try {
            //查询签到信息
            WhgTraEnrolCourse record = new WhgTraEnrolCourse();
            record.setTraid(traId);
            record.setEnrolid(enrolId);
            record.setCourseid(courseId);
            record.setUserid(userId);
            record = this.whgTraEnrolCourseMapper.selectOne(record);

            if (record != null) {//有签到信息
                if(record.getSign().intValue() != 1){
                    record.setSign(1);//
                    record.setSigntime(new Date());
                }else{
                    rtnCode = "101";//已签到
                }
            } else {//没有签到信息
                //查询课程信息
                WhgTraCourse course = new WhgTraCourse();
                course.setId(courseId);
                course.setDelstate(EnumDelState.STATE_DEL_NO.getValue());
                course.setState(EnumState.STATE_YES.getValue());
                course = this.whgTraCourseMapper.selectOne(course);
                if(course != null && course.getId() != null){
                    record = new WhgTraEnrolCourse();
                    record.setId(commService.getKey("whg_tra_enrol_course"));
                    record.setSign(1);//
                    record.setSigntime(new Date());
                    record.setCoursestime(course.getStarttime());
                    record.setCoursestime(course.getEndtime());
                }else{
                    rtnCode = "102";//已签到课程已取消
                }
            }
        }catch (Exception e){
            rtnCode = "109";//签到失败
        }

        return rtnCode;
    }

    public Boolean isLogin(String userId){
        if(null == userId){
            return false;
        }
        WhUser whUser = new WhUser();
        whUser.setId(userId);
        WhUser whUser1 = userMapper.selectOne(whUser);
        if(null == whUser1){
            return false;
        }
        return true;
    }

    /**
     * 添加培训报名记录
     * @param whgTraEnrol
     * @return 0:成功,1:异常,2:重复提交
     */
    public int addTraSign(WhgTraEnrol whgTraEnrol){
        try {
            WhgTraEnrol temp = whgTraEnrolMapper.selectOne(whgTraEnrol);
            if(null != temp){
                return 2;
            }
            whgTraEnrol.setId(commService.getKey("whg_tra_enrol"));
            whgTraEnrol.setOrderid(whgTraEnrol.getId());
            whgTraEnrol.setState(1);
            whgTraEnrol.setCrttime(new Date());
            whgTraEnrolMapper.insert(whgTraEnrol);
            return 0;
        }catch (Exception e){
            log.error(e.toString());
            return 1;
        }
    }

    /**
     * 查询培训报名记录
     * @param whgTraEnrol
     * @return
     */
    public WhgTraEnrol findTraEnrol(WhgTraEnrol whgTraEnrol){
        try {
            Example example = new Example(WhgTraEnrol.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("traid",whgTraEnrol.getTraid());
            criteria.andEqualTo("userid",whgTraEnrol.getUserid());
            List<WhgTraEnrol> whgTraEnrolList = whgTraEnrolMapper.selectByExample(example);
            if(!whgTraEnrolList.isEmpty()){
                return whgTraEnrolList.get(0);
            }else {
                return null;
            }
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    /**
     * 根据报名订单号查询所有课程
     * @param orderId 培训报名列表
     * @return
     */
    public List<Map<String, String>> queryEnrolCourseList(String orderId)throws Exception{
        List<Map<String, String>> list = new ArrayList<>();


        //查询报名记录得到培训ID
        WhgTraEnrol record = new WhgTraEnrol();
        record.setOrderid(orderId);
        record = this.whgTraEnrolMapper.selectOne(record);//.selectByExample(example);

        //根据报名ID取已签到信息
        Example example2 = new Example(WhgTraEnrolCourse.class);
        example2.createCriteria().andEqualTo("enrolid", record.getId());
        List<WhgTraEnrolCourse> clist = this.whgTraEnrolCourseMapper.selectByExample(example2);
        Map<String, WhgTraEnrolCourse> courseMap = new HashMap<String, WhgTraEnrolCourse>();
        if(clist != null){
            for(WhgTraEnrolCourse ec : clist){
                courseMap.put(ec.getCourseid(), ec);
            }
        }

        //根据培训ID查询所有课程
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String traid = record.getTraid();
        Example example = new Example(WhgTraCourse.class);
        example.createCriteria().andEqualTo("traid", traid)
                .andEqualTo("state", EnumState.STATE_YES.getValue())
        .andEqualTo("delstate", EnumDelState.STATE_DEL_NO.getValue());
        example.setOrderByClause("starttime");
        List<WhgTraCourse> listCourse = this.whgTraCourseMapper.selectByExample(example);
        if(listCourse != null){
            for(WhgTraCourse course : listCourse){
                Map<String, String> cmap = new HashMap<String, String>();
                cmap.put("traid", traid);
                cmap.put("enrolid", record.getId());
                cmap.put("courseid", course.getId());
                cmap.put("starttime", sdf.format(course.getStarttime()));
                cmap.put("endtime", sdf.format(course.getEndtime()));

                String sign = "0";//签到状态 0-未签到, 1-已签到
                String signtime = "";//签到时间
                String userid = ""; //签到会员ID
                if(courseMap != null && courseMap.containsKey(course.getId())){
                    WhgTraEnrolCourse wec = courseMap.get(course.getId());
                    sign = wec.getSign()+"";
                    signtime = sdf.format(wec.getSigntime());
                    userid = wec.getUserid();
                }
                cmap.put("sign", sign);
                cmap.put("signtime", signtime);
                cmap.put("userid", userid);


                list.add(cmap);
            }
        }

        return list;
    }

    private String checkEmpty(String str){
        if(StringUtils.isEmpty(str) || str.trim().length() == 0){
            return null;
        }
        return str;
    }

    /**
     * 获取培训首页轮播图
     * @param type
     * @return
     */
    public List<WhgYwiLbt> getLbt(String type){
        try {
            Example example = new Example(WhgYwiLbt.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("type",type);
            criteria.andEqualTo("state",1);
            return whgYwiLbtMapper.selectByExample(example);
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    /**
     * 判断用户是否能报名
     * @param userId
     * @param whgTra
     * @return:
     *  0:可报名
     *  100:未登录
     *  101:未实名认证
     *  102:年龄段不合适
     *  103:超过报名人数
     *  104:还未到报名时间
     *  105:报名时间已过
     */
    public Integer canSign(String userId,WhgTra whgTra){
        if(null == userId){
            return 100;
        }
        Example example = new Example(WhUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",userId);
        List<WhUser> whUserList = userMapper.selectByExample(example);
        WhUser userTemp = 0 < whUserList.size()?whUserList.get(0):null;
        if(null == userTemp){
            return 100;
        }
        if(1 == whgTra.getIsrealname()){
            if(1 != userTemp.getIsrealname()){
                return 101;
            }
        }
        if(null != whgTra.getAge() && !whgTra.getAge().trim().isEmpty()){
            Integer userAge = getUserAge(userTemp);
            if(null == userAge){
                return 102;
            }
            if(!ageIsIn(whgTra.getAge(),userAge)){
                return 102;
            }
        }
        Integer enrolCount = countTraEnrol(whgTra.getId());
        if(null != enrolCount){
            if(enrolCount >= whgTra.getMaxnumber()){
                return 103;
            }
        }
        Date enrolStart = whgTra.getEnrollstarttime();
        Date enrolEnd = whgTra.getEnrollendtime();
        if(isAfter(enrolStart,new Date())){
            return 104;
        }
        if(isAfter(new Date(),enrolEnd)){
            return 105;
        }
        return 0;
    }

    private Boolean isAfter(Date date1,Date date2){
        LocalDate localDate1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if(localDate1.isAfter(localDate2)){
            return true;
        }
        return false;
    }

    private Integer countTraEnrol(String traId){
        try {
            Example example = new Example(WhgTraEnrol.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("traid",traId);
            List<WhgTraEnrol> whgTraEnrolList = whgTraEnrolMapper.selectByExample(example);
            if(null == whgTraEnrolList){
                return null;
            }
            return whgTraEnrolList.size();
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    private Boolean ageIsIn(String ageGrades,Integer userAge){
        if(ageGrades.trim().isEmpty()){
            return true;
        }
        try {
            Integer minAge = Integer.valueOf(ageGrades.substring(0,ageGrades.indexOf(",")));
            Integer maxAge = Integer.valueOf(ageGrades.substring(ageGrades.indexOf(",") + 1));
            if(minAge <= userAge && minAge <= maxAge){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            return null;
        }
    }

    private Integer getUserAge(WhUser whUser){
        Date brithDaty = whUser.getBirthday();
        Date now = new Date();
        if(null == brithDaty){
            return null;
        }
        int brithYear = brithDaty.getYear();
        int nowYear = now.getYear();
        return nowYear - brithYear;
    }

    /**
     * 多维度获取培训
     * @param page
     * @param rows
     * @param param
     * @return
     */
    public PageInfo getTraList(Integer page, Integer rows, Map param){
        if(null == page){
            page = 1;
        }
        if(null == rows){
            rows = 10;
        }
        try {
            PageHelper.startPage(page,rows);
            Example example = new Example(WhgTra.class);
            Example.Criteria criteria = example.createCriteria();
            String arttype = (String) param.get("arttype");
            if(null != arttype){
                criteria.andLike("arttype","%" + arttype + "%");
            }
            String area = (String)param.get("area");
            if(null != area){
                criteria.andEqualTo("area",area);
            }
            String sdate = (String)param.get("sdate");
            if(null != sdate){
                if("1".equals(sdate)){
                    criteria.andLessThan("starttime",new Date());
                    criteria.andGreaterThan("endtime",new Date());
                }else if("2".equals(sdate)){
                    criteria.andGreaterThanOrEqualTo("starttime",new Date());
                }else if("3".equals(sdate)){
                    criteria.andLessThan("endtime",new Date());
                }
            }
            String isbasictra = (String)param.get("isbasictra");
            if(null != isbasictra){
                criteria.andEqualTo("isbasictra",isbasictra);
            }
            criteria.andEqualTo("state",6);
            criteria.andEqualTo("delstate",0);
            example.setOrderByClause("statemdfdate desc");
            List<WhgTra> whgTraList = whgTraMapper.selectByExample(example);
            return new PageInfo(whgTraList);
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    /**
     * 判断用户是否可以报名培训
     * @param userId
     * @param traList
     * @return
     */
    public List judgeCanSign(String userId,List traList){
        List list = new ArrayList();
        if(null == traList){
            return null;
        }
        for(Object item : traList){
            WhgTra whgTra = (WhgTra)item;
            Integer canSign = this.canSign(userId,whgTra);
            WhgTraEnrol whgTraEnrol = new WhgTraEnrol();
            whgTraEnrol.setTraid(whgTra.getId());
            Integer signCount = whgTraEnrolMapper.selectCount(whgTraEnrol);
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(whgTra));
            jsonObject.put("canSign",canSign);
            jsonObject.put("signCount",signCount);
            list.add(jsonObject);
        }
        return list;
    }

    public WhgYwiLbt getCulturalMarketLbt(){
        Example example = new Example(WhgYwiLbt.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type","12");
        criteria.andEqualTo("state",1);
        criteria.andEqualTo("delstate",0);
        example.setOrderByClause("statemdfdate desc");
        List<WhgYwiLbt> whgYwiLbtList = whgYwiLbtMapper.selectByExample(example);
        if(whgYwiLbtList.isEmpty()){
            return null;
        }
        return whgYwiLbtList.get(0);
    }

    /**
     * 获取一个培训数据
     * @param traId
     * @return
     */
    public WhgTra getOneTra(String traId){
        WhgTra whgTra = new WhgTra();
        whgTra.setId(traId);
        try {
            WhgTra res = whgTraMapper.selectOne(whgTra);
            return res;
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    /**
     * 获取所有的课程
     * @param traId
     * @return
     */
    public List<WhgTraCourse> getCourseByTraId(String traId){
        WhgTraCourse whgTraCourse = new WhgTraCourse();
        whgTraCourse.setTraid(traId);
        try {
            List<WhgTraCourse> whgTraCourseList = whgTraCourseMapper.select(whgTraCourse);
            return whgTraCourseList;
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    /**
     * 为分页展示课程
     * @param page
     * @param rows
     * @param traId
     * @return
     */
    public PageInfo getCourseByTraId(Integer page,Integer rows,String traId){
        PageHelper.startPage(page,rows);
        Example example = new Example(WhgTraCourse.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("traid",traId);
        example.setOrderByClause("starttime");
        try {
            List<WhgTraCourse> whgTraCourseList = whgTraCourseMapper.selectByExample(example);
            return new PageInfo(whgTraCourseList);
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }

    /**
     * 获取推荐的培训
     * @param page
     * @param rows
     * @return
     */
    public PageInfo getRecommendTra(Integer page,Integer rows){
        PageHelper.startPage(page,rows);
        try {
            Example example = new Example(WhgTra.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("state",6);
            criteria.andEqualTo("recommend",1);
            example.setOrderByClause("statemdfdate desc");
            List<WhgTra> whgTraList = whgTraMapper.selectByExample(example);
            return new PageInfo(whgTraList);
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }
    }
}
