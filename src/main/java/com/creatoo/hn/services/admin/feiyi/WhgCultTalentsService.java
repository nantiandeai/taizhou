package com.creatoo.hn.services.admin.feiyi;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.ext.emun.EnumBizState;
import com.creatoo.hn.ext.emun.EnumDelState;
import com.creatoo.hn.mapper.WhgCultTalentsMapper;
import com.creatoo.hn.model.WhgCultHeritage;
import com.creatoo.hn.model.WhgCultTalents;
import com.creatoo.hn.model.WhgSysUser;
import com.creatoo.hn.model.WhgTra;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.utils.ReqParamsUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 文化人才管理service
 * Created by luzhihuai on 2017/6/5.
 */
@Service
public class WhgCultTalentsService {
    private static Logger logger = Logger.getLogger(WhgCultHeritage.class);

    @Autowired
    private WhgCultTalentsMapper whgCultTalentsMapper;

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
    public PageInfo<WhgCultTalents> t_srchList4p(HttpServletRequest request, WhgCultTalents talents) throws Exception {
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
        //分页信息
        int page = Integer.parseInt((String) paramMap.get("page"));
        int rows = Integer.parseInt((String) paramMap.get("rows"));

        //搜索条件
        Example example = new Example(WhgCultHeritage.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if (talents != null && talents.getName() != null) {
            c.andLike("name", "%" + talents.getName() + "%");
            talents.setName(null);
        }

        String pageType = request.getParameter("type");
        //编辑列表
        if ("edit".equalsIgnoreCase(pageType)){
            c.andIn("state", Arrays.asList(1,5));
        }
        //审核列表，查 9待审核
        if ("check".equalsIgnoreCase(pageType)){
            c.andEqualTo("state", 9);
        }
        //发布列表，查 2待发布 6已发布 4已下架
        if ("publish".equalsIgnoreCase(pageType)){
            c.andIn("state", Arrays.asList(2,6,4));
        }
        //删除列表，查已删除 否则查未删除的
        if ("recycle".equalsIgnoreCase(pageType)){
            c.andEqualTo("isdel", 1);
        }else{
            c.andEqualTo("isdel", 0);
        }
        if(request.getParameter("state") != null){
            int state = Integer.parseInt(request.getParameter("state"));
            c.andEqualTo("state", state);
        }

        example.setOrderByClause("crtdate desc");

        //分页查询
        PageHelper.startPage(page, rows);
            List<WhgCultTalents> typeList = this.whgCultTalentsMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 分页查询分类列表信息
     *
     * @param request。
     */
    @SuppressWarnings("all")
    public PageInfo<WhgCultTalents> t_srchList4p(HttpServletRequest request, WhgCultTalents talents,List<Map> relList) throws Exception {


        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
        //分页信息
        int page = Integer.parseInt((String) paramMap.get("page"));
        int rows = Integer.parseInt((String) paramMap.get("rows"));

        //搜索条件
        Example example = new Example(WhgCultHeritage.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if (talents != null && talents.getName() != null) {
            c.andLike("name", "%" + talents.getName() + "%");
            talents.setName(null);
        }

        String pageType = request.getParameter("type");
        //编辑列表
        if ("edit".equalsIgnoreCase(pageType)){
            c.andIn("state", Arrays.asList(1,5));
        }
        //审核列表，查 9待审核
        if ("check".equalsIgnoreCase(pageType)){
            c.andEqualTo("state", 9);
        }
        //发布列表，查 2待发布 6已发布 4已下架
        if ("publish".equalsIgnoreCase(pageType)){
            c.andIn("state", Arrays.asList(2,6,4));
        }
        //删除列表，查已删除 否则查未删除的
        if ("recycle".equalsIgnoreCase(pageType)){
            c.andEqualTo("isdel", 1);
        }else{
            c.andEqualTo("isdel", 0);
        }
        if(request.getParameter("state") != null){
            int state = Integer.parseInt(request.getParameter("state"));
            c.andEqualTo("state", state);
        }

        if(null != relList){
            List list = new ArrayList();
            for(Map item : relList){
                String relid = (String)item.get("relid");
                list.add(relid);
            }
            c.andIn("id",list);
        }

        example.setOrderByClause("crtdate desc");

        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgCultTalents> typeList = this.whgCultTalentsMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 查询单条记录
     *
     * @param id id
     * @return 对象
     * @throws Exception
     */
    public WhgCultTalents t_srchOne(String id) throws Exception {
        return this.whgCultTalentsMapper.selectByPrimaryKey(id);
    }

    /**
     * 添加
     *
     * @param talents .
     */
    public void t_add(HttpServletRequest request, WhgCultTalents talents) throws Exception {
        WhgSysUser user = (WhgSysUser) request.getSession().getAttribute("user");

        //talents.setId(commService.getKey("whg_cult_talents"));
        talents.setCrtdate(new Date());
        talents.setIsrecommend(0);
        talents.setIsdel(EnumDelState.STATE_DEL_NO.getValue());
        talents.setStatemdfdate(new Date());
        talents.setStatemdfuser(user.getId());
        talents.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        int result = this.whgCultTalentsMapper.insertSelective(talents);
        if (result != 1) {
            throw new Exception("添加数据失败！");
        }
    }

    /**
     * 编辑
     *
     * @param
     */
    public void t_edit(WhgCultTalents talents) throws Exception {
        talents.setStatemdfdate(new Date());
        int result = this.whgCultTalentsMapper.updateByPrimaryKeySelective(talents);
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
        WhgCultTalents cultTalents = whgCultTalentsMapper.selectByPrimaryKey(id);
        if (cultTalents == null) {
            return;
        }
        if (cultTalents.getIsdel() != null && cultTalents.getIsdel().compareTo(1) == 0) {
            this.whgCultTalentsMapper.deleteByPrimaryKey(id);
        } else {
            cultTalents = new WhgCultTalents();
            cultTalents.setId(id);
            cultTalents.setIsdel(1);
            this.whgCultTalentsMapper.updateByPrimaryKeySelective(cultTalents);
        }
    }

    /**
     * 还原
     *
     * @param id
     */
    public void t_undel(String id) {
        WhgCultTalents cultTalents = whgCultTalentsMapper.selectByPrimaryKey(id);
        if (cultTalents == null) {
            return;
        }
        cultTalents = new WhgCultTalents();
        cultTalents.setId(id);
        cultTalents.setIsdel(0);
        this.whgCultTalentsMapper.updateByPrimaryKeySelective(cultTalents);
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
        WhgCultTalents cultTalents = new WhgCultTalents();
        cultTalents.setState(tostate);
        cultTalents.setStatemdfdate(new Date());
        cultTalents.setStatemdfuser(user.getId());
        this.whgCultTalentsMapper.updateByExampleSelective(cultTalents, example);
        return rb;
    }
}
