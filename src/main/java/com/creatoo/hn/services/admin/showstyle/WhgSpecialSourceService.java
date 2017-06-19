package com.creatoo.hn.services.admin.showstyle;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.ext.emun.EnumBizState;
import com.creatoo.hn.ext.emun.EnumDelState;
import com.creatoo.hn.ext.emun.EnumTypeClazz;
import com.creatoo.hn.mapper.WhgSpecilResourceMapper;
import com.creatoo.hn.model.*;
import com.creatoo.hn.services.admin.branch.BranchService;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.utils.ReqParamsUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.lang.model.element.Name;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 台州特色资源管理service
 * Created by luzhihuai on 2017/6/13.
 */
@Service
public class WhgSpecialSourceService {
    private static Logger logger = Logger.getLogger(WhgCultHeritage.class);

    @Autowired
    private WhgSpecilResourceMapper whgSpecilResourceMapper;
    /**
     * 分馆
     */
    @Autowired
    private BranchService branchService;

    /**
     * 公共服务
     */
    @Autowired
    private CommService commService;

    /**
     * 分页查询分类列表信息
     *
     * @param request。
     */
    public PageInfo<WhgSpecilResourceSarch> t_srchList4p(HttpServletRequest request, WhgSpecilResource historical) throws Exception {
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
        //分页信息
        int page = Integer.parseInt((String) paramMap.get("page"));
        int rows = Integer.parseInt((String) paramMap.get("rows"));

        Map param = new HashMap();
        //搜索条件
        Example example = new Example(WhgCultHeritage.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if (historical != null && historical.getName() != null) {
            param.put("name", historical.getName());
        }

        String pageType = request.getParameter("type");
        //编辑列表
        if ("edit".equalsIgnoreCase(pageType)) {
            param.put("states", Arrays.asList(1, 5));
        }
        //审核列表，查 9待审核
        if ("check".equalsIgnoreCase(pageType)) {
            param.put("states", Arrays.asList(9));
        }
        //发布列表，查 2待发布 6已发布 4已下架
        if ("publish".equalsIgnoreCase(pageType)) {
            param.put("states", Arrays.asList(2,6,4));
        }
        //删除列表，查已删除 否则查未删除的
        if ("recycle".equalsIgnoreCase(pageType)) {
            param.put("isdel", 1);
        }
        if (request.getParameter("state") != null) {
            param.put("state", request.getParameter("state"));
        }

        List<WhgSpecilResourceSarch> list = this.whgSpecilResourceMapper.searchName(param);

        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgSpecilResource> typeList = this.whgSpecilResourceMapper.selectByExample(example);
        return new PageInfo<>(list);
    }
    /**
     * 查询单条记录
     *
     * @param id id
     * @return 对象
     * @throws Exception
     */
    public WhgSpecilResource t_srchOne(String id) throws Exception {
        return this.whgSpecilResourceMapper.selectByPrimaryKey(id);
    }

    /**
     * 添加
     *
     * @param source .
     */
    public void t_add(HttpServletRequest request, WhgSpecilResource source) throws Exception {
        WhgSysUser user = (WhgSysUser) request.getSession().getAttribute("user");
        String id = commService.getKey("whg_specil_resource");
        //设置活动所属单位
        String[] branch = request.getParameterValues("branch");
        for(String branchId : branch){
            branchService.setBranchRel(id, EnumTypeClazz.TYPE_RESOURCE.getValue(),branchId);
        }
        source.setId(id);
        source.setCrtdate(new Date());
        source.setIsrecommend(0);
        source.setIsdel(EnumDelState.STATE_DEL_NO.getValue());
        source.setStatemdfdate(new Date());
        source.setStatemdfuser(user.getId());
        source.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        int result = this.whgSpecilResourceMapper.insertSelective(source);
        if (result != 1) {
            throw new Exception("添加数据失败！");
        }
    }

    /**
     * 编辑
     *
     * @param
     */
    public void t_edit(HttpServletRequest request,WhgSpecilResource source) throws Exception {
        source.setStatemdfdate(new Date());
        //设置活动所属单位
        branchService.clearBranchRel(source.getId(),EnumTypeClazz.TYPE_RESOURCE.getValue());
//        String[] branch = request.getParameterValues("branch");
//        for(String branchId : branch){
            branchService.setBranchRel(source.getId(), EnumTypeClazz.TYPE_RESOURCE.getValue(),source.getBranch());
//        }
        int result = this.whgSpecilResourceMapper.updateByPrimaryKeySelective(source);
        if (result != 1) {
            throw new Exception("编辑数据失败！");
        }
    }

    /**
     * 删除
     *
     * @param id
     */
    public void t_del(String id) throws Exception {
        WhgSpecilResource specilResource = whgSpecilResourceMapper.selectByPrimaryKey(id);
        if (specilResource == null) {
            return;
        }
        if (specilResource.getIsdel() != null && specilResource.getIsdel().compareTo(1) == 0) {
            this.whgSpecilResourceMapper.deleteByPrimaryKey(id);
        } else {
            specilResource = new WhgSpecilResource();
            specilResource.setId(id);
            specilResource.setIsdel(1);
            this.whgSpecilResourceMapper.updateByPrimaryKeySelective(specilResource);
        }
    }

    /**
     * 还原
     *
     * @param id
     */
    public void t_undel(String id) {
        WhgSpecilResource specilResource = whgSpecilResourceMapper.selectByPrimaryKey(id);
        if (specilResource == null) {
            return;
        }
        specilResource = new WhgSpecilResource();
        specilResource.setId(id);
        specilResource.setIsdel(0);
        this.whgSpecilResourceMapper.updateByPrimaryKeySelective(specilResource);
    }

    /**
     * 修改状态
     *
     * @param ids        用逗号分隔的多个ID
     * @param formstates 修改之前的状态
     * @param tostate    修改后的状态
     * @return 执行操作返回结果的JSON信息
     */
    public ResponseBean t_updstate(String ids, String formstates, int tostate, WhgSysUser user) throws Exception {
        ResponseBean rb = new ResponseBean();
        if (ids == null) {
            rb.setSuccess(ResponseBean.FAIL);
            rb.setErrormsg("主键丢失");
            return rb;
        }
        Example example = new Example(WhgTra.class);
        example.createCriteria()
                .andIn("state", Arrays.asList(formstates.split("\\s*,\\s*")))
                .andIn("id", Arrays.asList(ids.split("\\s*,\\s*")));
        WhgSpecilResource specilResource = new WhgSpecilResource();
        specilResource.setState(tostate);
        specilResource.setStatemdfdate(new Date());
        specilResource.setStatemdfuser(user.getId());
        this.whgSpecilResourceMapper.updateByExampleSelective(specilResource, example);
        return rb;
    }
}
