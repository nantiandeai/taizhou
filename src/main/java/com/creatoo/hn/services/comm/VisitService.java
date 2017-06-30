package com.creatoo.hn.services.comm;

import com.creatoo.hn.mapper.WhgVisitDataMapper;
import com.creatoo.hn.model.WhgVisitData;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wangxl on 2017/6/19.
 */
@Service
public class VisitService {
    /**
     * 日志
     */
    Logger log = Logger.getLogger(this.getClass());

    /**
     * 公共服务
     */
    @Autowired
    private CommService commService;

    /**
     * Visit Data DAO
     */
    @Autowired
    private WhgVisitDataMapper whgVisitDataMapper;

    /**
     * 保存浏览记录
     * @param vType 访问类型(1:PC,2:weixin,3:Android,4:IOS)
     * @param vDate 访问日期(yyyy-MM-dd)
     * @param vIp 访问IP(127.0.0.1)
     * @param visitor 访客(访客标识)
     * @param vPage 访问页面(url)
     * @param vCount 访问量
     * @throws Exception
     */
    public void saveVisit(String vType, String vDate, String vIp, String visitor, String vPage, String vCount)throws  Exception{
        int vCnt = 1;
        int vTyp = 1;
        try{
            vCnt = Integer.parseInt(vCount);
            vTyp = Integer.parseInt(vType);
        }catch(Exception e){}

        Example example = new Example(WhgVisitData.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("visitType", vTyp);
        c.andEqualTo("visitDate", vDate);
        c.andEqualTo("visitIp", vIp);
        c.andEqualTo("visitUser", visitor);
        c.andEqualTo("visitPage", vPage);

        int rows = this.whgVisitDataMapper.selectCountByExample(example);
        if(rows > 0){
            WhgVisitData wvd = new WhgVisitData();
            wvd.setVisitCount(vCnt);
            wvd.setVisitTime(new Date());
            this.whgVisitDataMapper.updateByExampleSelective(wvd, example);
        }else{
            WhgVisitData wvd = new WhgVisitData();
            wvd.setVisitId(commService.getKey("whg_visit_data"));
            wvd.setVisitType(vTyp);
            wvd.setVisitIp(vIp);
            wvd.setVisitPage(vPage);
            wvd.setVisitCount(vCnt);
            wvd.setVisitDate(vDate);
            wvd.setVisitTime(new Date());
            wvd.setVisitUser(visitor);

            //页面类型（资讯，活动，场馆，培训）， ID
            Entity entity = parseEntity(vPage);
            int vetype = entity.getvEtype();
            String veid = entity.getvEid();
            wvd.setVisitEtype(vetype);
            wvd.setVisitEid(veid);

            this.whgVisitDataMapper.insert(wvd);
        }
    }

    class Entity {
        private int vEtype = 0;
        private String vEid = "";

        public int getvEtype() {
            return vEtype;
        }

        public void setvEtype(int vEtype) {
            this.vEtype = vEtype;
        }

        public String getvEid() {
            return vEid;
        }

        public void setvEid(String vEid) {
            this.vEid = vEid;
        }
    }

    /**
     * 根据访问连接解析是否是活动/场馆/培训/资讯类型及其ID
     * @param vPage 访问连接地址
     * @return Entity.vEtype: 1-活动, 2-培训, 3-场馆, 4-活动室, 5-资讯, 9-其它 Entity.vEid：对象ID
     */
    private Entity parseEntity(String vPage){
        int vEtype = 9;
        String vEid = "";
        if(vPage.indexOf("agdwhhd/activityinfo?actvid=") > -1){
            vEtype = 1;//活动
            vEid = vPage.substring(vPage.indexOf("actvid=")+7);//活动ID
        }else if(vPage.indexOf("agdpxyz/traininfo?traid=") > -1){
            vEtype = 2;//培训
            vEid = vPage.substring(vPage.indexOf("traid=")+6);//培训ID
        }else if(vPage.indexOf("agdcgfw/venueinfo?venid=") > -1){
            vEtype = 3;//场馆
            vEid = vPage.substring(vPage.indexOf("venid=")+6);//场馆ID
        }else if(vPage.indexOf("agdcgfw/venroominfo?roomid=") > -1){
            vEtype = 4;//活动室
            vEid = vPage.substring(vPage.indexOf("roomid=")+7);//活动室D
        }else{
            String[] zxArr = new String[]{
                    "agdgwgk/faguiinfo?id=",
                    "agdgwgk/zhinaninfo?id=",
                    "agdzxdt/noticeinfo?id=",
                    "agdzxdt/workinginfo?id=",
                    "agdzxdt/unitsinfo?id=",
                    "agdzxdt/hotinfo?id=",
                    "agdwhhd/noticeinfo?id=",
                    "agdwhhd/newsinfo?id=",
                    "agdpxyz/noticeinfo?id=",
                    "agdpxyz/newsinfo?id=",
                    "agdfyzg/noticeinfo?id=",
                    "agdfyzg/newsinfo?id=",
                    "agdfyzg/falvwenjianinfo?id=",
                    "agdfyzg/shenbaoinfo?id=",
                    "agdzyfw/newsinfo?id=",
                    "agdzyfw/tashaninfo?id=",
                    "agdzyfw/zhengceinfo?id="
            };
            for(String zxUrl : zxArr){
                if(vPage.indexOf(zxUrl) > -1){
                    vEtype = 5;//资讯
                    vEid = vPage.substring(vPage.indexOf("id=")+3);//资讯ID
                    break;
                }
            }
        }

        Entity entity = new Entity();
        entity.setvEtype(vEtype);
        entity.setvEid(vEid);
        return entity;
    }

    public static void main(String[] args) {
        Date now = new Date();
        System.out.println(new SimpleDateFormat("yyyy-MM-dd("+new SimpleDateFormat("EEEE").format(now)+")ahh:mm").format(now));
    }
}
