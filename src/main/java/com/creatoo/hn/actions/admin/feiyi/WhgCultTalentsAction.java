package com.creatoo.hn.actions.admin.feiyi;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.ext.emun.EnumTypeClazz;
import com.creatoo.hn.model.WhBranchRel;
import com.creatoo.hn.model.WhgCultTalents;
import com.creatoo.hn.model.WhgSysUser;
import com.creatoo.hn.services.admin.branch.BranchService;
import com.creatoo.hn.services.admin.feiyi.WhgCultTalentsService;
import com.creatoo.hn.services.comm.CommService;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 文化人才管理action
 * Created by luzhihuai on 2017/6/5.
 */
@RestController
@RequestMapping("/admin/cultalents")
public class WhgCultTalentsAction {
    /**
     * log
     */
    Logger log = Logger.getLogger(this.getClass());
    /**
     * 文化遗产管理service
     */
    @Autowired
    private WhgCultTalentsService whgCultTalentsService;

    @Autowired
    private BranchService branchService;

    @Autowired
    private CommService commService;

    /**
     * 进入type(list|add|edit|view)视图
     *
     * @param request 请求对象
     * @param type    视图类型(list|add|edit|view)
     * @return 视图
     */
    @RequestMapping("/view/{type}")
    public ModelAndView listview(HttpServletRequest request, @PathVariable("type") String type) {
        ModelAndView view = new ModelAndView();
        try {
            view.addObject("type", type);
            if ("add".equalsIgnoreCase(type)) {
                String id = request.getParameter("id");
                String targetShow = request.getParameter("targetShow");
                if (id != null) {
                    view.addObject("id", id);
                    view.addObject("targetShow", targetShow);
                    view.addObject("cult", whgCultTalentsService.t_srchOne(id));
                    WhBranchRel whBranchRel = branchService.getBranchRel(id,EnumTypeClazz.TYPE_TALENTS.getValue());
                    if(null != whBranchRel){
                        view.addObject("whBranchRel",whBranchRel);
                    }
                    view.setViewName("admin/feiyi/cultalents/view_edit");
                } else {
                    view.setViewName("admin/feiyi/cultalents/view_add");
                }
            } else {
                view.setViewName("admin/feiyi/cultalents/view_list");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return view;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return res
     */
    @RequestMapping(value = "/srchList4p")
    public ResponseBean srchList4p(HttpServletRequest request, WhgCultTalents talents) {
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser whgSysUser = (WhgSysUser)request.getSession().getAttribute("user");
            List<Map> relList = branchService.getBranchRelList(whgSysUser.getId(),EnumTypeClazz.TYPE_TALENTS.getValue());
            PageInfo<WhgCultTalents> pageInfo = whgCultTalentsService.t_srchList4p(request, talents,relList);
            res.setRows(pageInfo.getList());
            res.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 添加
     *
     * @return res
     */
    @RequestMapping("/add")
    public ResponseBean add(WhgCultTalents talents, HttpServletRequest request) {
        ResponseBean res = new ResponseBean();
        try {
            String newId = commService.getKey("whg_cult_talents");
            talents.setId(newId);
            this.whgCultTalentsService.t_add(request, talents);
            String branch = request.getParameter("branch");
            if(null != branch && !branch.trim().isEmpty()){
                branchService.setBranchRel(newId, EnumTypeClazz.TYPE_TALENTS.getValue(),branch);
            }
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("保存失败");
            log.error(res.getErrormsg(), e);
        }
        return res;
    }

    /**
     * 编辑
     *
     * @param talents talents
     * @return res
     */
    @RequestMapping(value = "/edit")
    public ResponseBean edit(WhgCultTalents talents,HttpServletRequest request) {
        ResponseBean res = new ResponseBean();
        try {
            this.whgCultTalentsService.t_edit(talents);
            branchService.clearBranchRel(talents.getId(),EnumTypeClazz.TYPE_TALENTS.getValue());
            String branch = request.getParameter("branch");
            if(null != branch && !branch.trim().isEmpty()){
                branchService.setBranchRel(talents.getId(), EnumTypeClazz.TYPE_TALENTS.getValue(),branch);
            }
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 删除
     *
     * @return
     */
    @RequestMapping("/del")
    public ResponseBean del(String id) {
        ResponseBean res = new ResponseBean();
        try {
            this.whgCultTalentsService.t_del(id);

        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("删除失败");
            log.error(res.getErrormsg(), e);
        }
        return res;
    }

    /**
     * 还原删除
     *
     * @param id id
     * @return res
     */
    @RequestMapping("/undel")
    public Object undel(String id) {
        ResponseBean rb = new ResponseBean();
        try {
            this.whgCultTalentsService.t_undel(id);
        } catch (Exception e) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("还原失败");
            log.error(rb.getErrormsg(), e);
        }
        return rb;
    }

    /**
     * 修改状态
     *
     * @param ids        id
     * @param formstates 当前状态
     * @param tostate    要改变的状态
     * @param session    session
     * @return res
     */
    @RequestMapping("/updstate")
    public ResponseBean updstate(String ids, String formstates, int tostate, HttpSession session) {
        ResponseBean res = new ResponseBean();
        try {
            WhgSysUser sysUser = (WhgSysUser) session.getAttribute("user");
            res = this.whgCultTalentsService.t_updstate(ids, formstates, tostate, sysUser);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("状态更改失败");
            log.error(res.getErrormsg() + " formstate: " + formstates + " tostate:" + tostate + " ids: " + ids, e);
        }
        return res;
    }

    /**
     * 推荐状态修改
     * @param request 请求对象
     * @param ids 用逗号分隔的多个ID
     * @param fromState 修改之前的状态
     * @param toState 修改后的状态
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/updCommend")
    public ResponseBean updCommend(HttpServletRequest request, String ids, String fromState, String toState){
        ResponseBean res = new ResponseBean();
        try {
            int c = this.whgCultTalentsService.t_updCommend(ids, fromState, toState, (WhgSysUser)request.getSession().getAttribute("user"));
            if(c != 1){
                res.setErrormsg("推荐失败");
            }
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }
}
