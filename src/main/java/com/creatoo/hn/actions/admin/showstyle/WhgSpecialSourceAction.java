package com.creatoo.hn.actions.admin.showstyle;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.ext.emun.EnumTypeClazz;
import com.creatoo.hn.model.WhBranchRel;
import com.creatoo.hn.model.WhgHistorical;
import com.creatoo.hn.model.WhgSpecilResource;
import com.creatoo.hn.model.WhgSysUser;
import com.creatoo.hn.services.admin.branch.BranchService;
import com.creatoo.hn.services.admin.showstyle.WhgSpecialSourceService;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 台州特色资源管理action
 * Created by luzhihuai on 2017/6/2.
 */
@RestController
@RequestMapping("/admin/specialResource")
public class WhgSpecialSourceAction {
    /**
     * log
     */
    Logger log = Logger.getLogger(this.getClass());
    /**
     * 文化遗产管理service
     */
    @Autowired
    private WhgSpecialSourceService whgSpecialSourceService;

    @Autowired
    private BranchService branchService;

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
                    view.addObject("source", whgSpecialSourceService.t_srchOne(id));
                    view.setViewName("admin/showstyle/specialResource/view_edit");
                    //分馆权限部分
                    WhBranchRel whBranchRel = branchService.getBranchRel(id, EnumTypeClazz.TYPE_RESOURCE.getValue());
                    if(null != whBranchRel){
                        view.addObject("whBranchRel",whBranchRel);
                    }
                } else {
                    view.setViewName("admin/showstyle/specialResource/view_add");
                }
            } else {
                view.setViewName("admin/showstyle/specialResource/view_list");
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
    public ResponseBean srchList4p(HttpServletRequest request, WhgSpecilResource resource) {
        ResponseBean res = new ResponseBean();
        try {
            PageInfo<WhgSpecilResource> pageInfo = whgSpecialSourceService.t_srchList4p(request, resource);
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
    public ResponseBean add(WhgSpecilResource resource, HttpServletRequest request) {
        ResponseBean res = new ResponseBean();
        try {
            this.whgSpecialSourceService.t_add(request, resource);
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
     * @param resource resource
     * @return res
     */
    @RequestMapping(value = "/edit")
    public ResponseBean edit(HttpServletRequest request, WhgSpecilResource resource) {
        ResponseBean res = new ResponseBean();
        try {
            this.whgSpecialSourceService.t_edit(request, resource);
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
            this.whgSpecialSourceService.t_del(id);

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
            this.whgSpecialSourceService.t_undel(id);
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
            res = this.whgSpecialSourceService.t_updstate(ids, formstates, tostate, sysUser);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("状态更改失败");
            log.error(res.getErrormsg() + " formstate: " + formstates + " tostate:" + tostate + " ids: " + ids, e);
        }
        return res;
    }
}
