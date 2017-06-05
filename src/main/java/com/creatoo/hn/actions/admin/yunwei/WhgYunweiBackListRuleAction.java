package com.creatoo.hn.actions.admin.yunwei;

import com.creatoo.hn.ext.annotation.WhgOPT;
import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.ext.emun.EnumOptType;
import com.creatoo.hn.model.WhgBacklistRule;
import com.creatoo.hn.model.WhgYwiSmstmp;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiBackListRuleService;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiSmsTmpService;
import com.creatoo.hn.utils.ReqParamsUtil;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 短信模板管理action
 *
 * @author luzhihuai
 * @version 1-201702
 *          Created by Administrator on 2017/6/2.
 */
@RestController
@RequestMapping("/admin/yunwei/rule")
public class WhgYunweiBackListRuleAction {
    /**
     * log
     */
    Logger log = Logger.getLogger(this.getClass());

    /**
     * 短信模板service
     */
    @Autowired
    private WhgYunweiBackListRuleService whgYunweiBackListRuleService;

    /**
     * 进入type(list|add|edit|view)视图
     *
     * @param request 请求对象
     * @return 视图
     */
    @RequestMapping("/view")
    @WhgOPT(optType = EnumOptType.SMS, optDesc = {"访问黑名单规则配置页面"}, valid = {"type=edit"})
    public ModelAndView listview(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("admin/yunwei/rule/view_edit" );
        try {
            WhgBacklistRule whgBacklistRule = this.whgYunweiBackListRuleService.t_srchOne();
            if(!"".equals(whgBacklistRule) || whgBacklistRule != null ){
                view.addObject("rule", whgBacklistRule);
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }


        return view;
    }


    /**
     * 查询详情
     * @param id
     * @return
     */
    @RequestMapping(value = "/srchOne")
    public ResponseBean srchOne(String id){
        ResponseBean res = new ResponseBean();
        try {
            WhgBacklistRule whgBacklistRule = this.whgYunweiBackListRuleService.t_srchOne();
            res.setData(whgBacklistRule);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 添加
     *
     * @param whgBacklistRule 实体
     * @return 对象
     */
    @RequestMapping(value = "/add")
    @WhgOPT(optType = EnumOptType.SMS, optDesc = {"添加"})
    public ResponseBean add(WhgBacklistRule whgBacklistRule) {
        ResponseBean res = new ResponseBean();
        try {
            if(whgBacklistRule.getId() != null && !"".equals(whgBacklistRule.getId())){
                this.whgYunweiBackListRuleService.t_edit(whgBacklistRule);
            }else{
                this.whgYunweiBackListRuleService.t_add(whgBacklistRule);
            }
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

}
