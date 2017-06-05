package com.creatoo.hn.services.admin.yunwei;

import com.creatoo.hn.ext.emun.EnumTagClazz;
import com.creatoo.hn.mapper.WhgBacklistRuleMapper;
import com.creatoo.hn.model.WhgActOrder;
import com.creatoo.hn.model.WhgBacklistRule;
import com.creatoo.hn.model.WhgUsrBacklist;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.services.home.agdwhhd.WhhdService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 黑名单规则配置service
 *
 * @author heyi
 * @version 1-201706
 *          Created by Administrator on 2017/6/2.
 */
@Service
public class WhgYunweiBackListRuleService {
    /**
     * 日志
     */
    Logger log = Logger.getLogger(this.getClass().getName());

    /**
     * 公共服务
     */
    @Autowired
    private CommService commService;

    /**
     * 黑名单配置服务
     */
    @Autowired
    private WhgBacklistRuleMapper whgBacklistRuleMapper;

    /**
     * 文化活动
     */
    @Autowired
    private WhhdService whhdService;

    /**
     * 查询单条记录
     * @return 对象
     * @throws Exception
     */
    public WhgBacklistRule t_srchOne()throws Exception{
        List<WhgBacklistRule> list = this.whgBacklistRuleMapper.selectAll();
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    /**
     * 添加
     *
     * @param
     */
    public void t_add(WhgBacklistRule wyl) throws Exception {
        WhgBacklistRule backlistRule = new WhgBacklistRule();

        if (wyl.getCancelday() != null && wyl.getCancelday() >0) {
            backlistRule.setCancelday(wyl.getCancelday());
        }
        if (wyl.getCancelnum() != null && wyl.getCancelday() >0) {
            backlistRule.setCancelnum(wyl.getCancelnum());
        }
        if (wyl.getMissday() != null && wyl.getMissday() >0) {
            backlistRule.setMissday(wyl.getMissday());
        }
        if (wyl.getMissnum()!= null && wyl.getMissnum() >0) {
            backlistRule.setMissnum(wyl.getMissnum());
        }

        backlistRule.setId(commService.getKey("whg_backlist_rule"));

        int result = this.whgBacklistRuleMapper.insertSelective(backlistRule);
        if (result != 1) {
            throw new Exception("添加数据失败！");
        }

    }

    public Map<String,Object> validateBackListRule(String itemId, String type, String userId,String phone) throws Exception {
        Map<String,Object> map = new HashedMap();
        WhgBacklistRule backlistRule = this.t_srchOne();
        //判断该用户是否已经加入黑名单
        List<WhgUsrBacklist> userBackList = whhdService.findWhgUsrBack4UserId(userId);
        if (backlistRule != null && (userBackList == null || userBackList.size() < 1)){
            int cancelNum = backlistRule.getCancelnum();
            int missNum = backlistRule.getMissnum();

            //type 1、场馆 3、活动 4、培训
            //场馆
            if(type.equals(EnumTagClazz.TAG_VENUE)){

            }
            //活动
            if(type.equals(EnumTagClazz.TAG_ACTIVITY)){
                //查询该用户取消的所有活动次数
                List<WhgActOrder> orderList = whhdService.findOrderList4Id(null,userId);
                //如果用户一个活动取消两次或者一个用户统计取消活动订单超过黑名单设置的规则次数则加入黑名单
                if( orderList.size() >= cancelNum ){
                    String id = this.commService.getKey("WhgUsrBacklist");
                    whhdService.addUserBack(id,userId,phone);
                    map.put("code","0");
                    map.put("errrorMsg","您已经被系统限制执行操作，如需了解详细情况，请联系管理员！");
                }
            }
            //培训
            if(type.equals(EnumTagClazz.TAG_TRAIN)){

            }
        }
        return  map;

    }

    /**
     * 编辑
     *
     * @param
     */
    public void t_edit(WhgBacklistRule wyl) throws Exception {
        String id = wyl.getId();
        WhgBacklistRule backlistRule = this.whgBacklistRuleMapper.selectByPrimaryKey(id);

        if (backlistRule != null) {
            if (wyl.getCancelday() != null && wyl.getCancelday() >0) {
                backlistRule.setCancelday(wyl.getCancelday());
            }
            if (wyl.getCancelnum() != null && wyl.getCancelday() >0) {
                backlistRule.setCancelnum(wyl.getCancelnum());
            }
            if (wyl.getMissday() != null && wyl.getMissday() >0) {
                backlistRule.setMissday(wyl.getMissday());
            }
            if (wyl.getMissnum()!= null && wyl.getMissnum() >0) {
                backlistRule.setMissnum(wyl.getMissnum());
            }
        }
        int result = this.whgBacklistRuleMapper.updateByPrimaryKeySelective(backlistRule);
        if (result != 1) {
            throw new Exception("编辑数据失败！");
        }
    }


}
