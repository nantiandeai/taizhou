package com.creatoo.hn.services.admin.feiyi;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.ext.emun.EnumBizState;
import com.creatoo.hn.ext.emun.EnumDelState;
import com.creatoo.hn.mapper.WhgHistoricalMapper;
import com.creatoo.hn.model.WhgCultHeritage;
import com.creatoo.hn.model.WhgHistorical;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 重点文物管理service
 * Created by luzhihuai on 2017/6/5.
 */
@Service
public class WhgHistoricalService {
    private static Logger logger = Logger.getLogger(WhgCultHeritage.class);

    @Autowired
    private WhgHistoricalMapper whgHistoricalMapper;

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
    public PageInfo<WhgHistorical> t_srchList4p(HttpServletRequest request, WhgHistorical historical) throws Exception {
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
        //分页信息
        int page = Integer.parseInt((String) paramMap.get("page"));
        int rows = Integer.parseInt((String) paramMap.get("rows"));

        //搜索条件
        Example example = new Example(WhgCultHeritage.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if (historical != null && historical.getName() != null) {
            c.andLike("name", "%" + historical.getName() + "%");
            historical.setName(null);
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
            List<WhgHistorical> typeList = this.whgHistoricalMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 查询单条记录
     *
     * @param id id
     * @return 对象
     * @throws Exception
     */
    public WhgHistorical t_srchOne(String id) throws Exception {
        return this.whgHistoricalMapper.selectByPrimaryKey(id);
    }

    /**
     * 添加
     *
     * @param historical .
     */
    public void t_add(HttpServletRequest request, WhgHistorical historical) throws Exception {
        WhgSysUser user = (WhgSysUser) request.getSession().getAttribute("user");

        historical.setId(commService.getKey("whg_historical"));
        historical.setCrtdate(new Date());
        historical.setIsrecommend(0);
        historical.setIsdel(EnumDelState.STATE_DEL_NO.getValue());
        historical.setStatemdfdate(new Date());
        historical.setStatemdfuser(user.getId());
        historical.setState(EnumBizState.STATE_CAN_EDIT.getValue());
        int result = this.whgHistoricalMapper.insertSelective(historical);
        if (result != 1) {
            throw new Exception("添加数据失败！");
        }
    }

    /**
     * 编辑
     *
     * @param
     */
    public void t_edit(WhgHistorical historical) throws Exception {
        historical.setStatemdfdate(new Date());
        int result = this.whgHistoricalMapper.updateByPrimaryKeySelective(historical);
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
        WhgHistorical historical = whgHistoricalMapper.selectByPrimaryKey(id);
        if (historical == null) {
            return;
        }
        if (historical.getIsdel() != null && historical.getIsdel().compareTo(1) == 0) {
            this.whgHistoricalMapper.deleteByPrimaryKey(id);
        } else {
            historical = new WhgHistorical();
            historical.setId(id);
            historical.setIsdel(1);
            this.whgHistoricalMapper.updateByPrimaryKeySelective(historical);
        }
    }

    /**
     * 还原
     *
     * @param id
     */
    public void t_undel(String id) {
        WhgHistorical historical = whgHistoricalMapper.selectByPrimaryKey(id);
        if (historical == null) {
            return;
        }
        historical = new WhgHistorical();
        historical.setId(id);
        historical.setIsdel(0);
        this.whgHistoricalMapper.updateByPrimaryKeySelective(historical);
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
        WhgHistorical historical = new WhgHistorical();
        historical.setState(tostate);
        historical.setStatemdfdate(new Date());
        historical.setStatemdfuser(user.getId());
        this.whgHistoricalMapper.updateByExampleSelective(historical, example);
        return rb;
    }
}
