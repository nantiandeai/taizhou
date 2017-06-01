package com.creatoo.hn.services.admin.exhi;

import com.creatoo.hn.ext.emun.EnumState;
import com.creatoo.hn.mapper.WhgExhMapper;
import com.creatoo.hn.mapper.WhgYwiWhppMapper;
import com.creatoo.hn.model.WhgExh;
import com.creatoo.hn.model.WhgSysUser;
import com.creatoo.hn.model.WhgYwiWhpp;
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
 * 数字展览service
 *
 * @author luzhihuai
 * @version 1-201703
 *          Created by Administrator on 2017/4/26.
 */
@Service
public class WhgExhiService {
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
     * 数字展览DAO
     */
    @Autowired
    private WhgExhMapper whgExhMapper;

    /**
     * 分页查询分类列表信息
     *
     * @param request。
     */
    public PageInfo<WhgExh> t_srchList4p(HttpServletRequest request,WhgExh whgExh) throws Exception {
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
        //分页信息
        int page = Integer.parseInt((String) paramMap.get("page"));
        int rows = Integer.parseInt((String) paramMap.get("rows"));
        //开始分页
        PageHelper.startPage(page, rows);

        //搜索条件
        Example example = new Example(WhgExh.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if(whgExh != null && whgExh.getExhtitle() != null){
            c.andLike("exhtitle", "%"+whgExh.getExhtitle()+"%");
            whgExh.setExhtitle(null);
        }
        //其它条件
        c.andEqualTo(whgExh);

//        c.andEqualTo("state", 1);
//        c.andEqualTo("delstate", 0);
        example.setOrderByClause("exhcrttime desc");

        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgExh> typeList = this.whgExhMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 列表查询
     *
     * @return
     * @throws Exception
     */
//    public List<WhgYwiWhpp> t_srchList(WhgYwiWhpp whgYwiWhpp) throws Exception {
//        return this.whgYwiWhppMapper.select(whgYwiWhpp);
//    }

    /**
     * 查询单条记录
     * @param exhid exhid
     * @return 对象
     * @throws Exception
     */
    public WhgExh t_srchOne(String exhid)throws Exception{
        return this.whgExhMapper.selectByPrimaryKey(exhid);
    }

    /**
     * 添加
     *
     * @param
     */
    public void t_add(HttpServletRequest request, WhgExh whgExh) throws Exception {
        WhgSysUser user = (WhgSysUser) request.getSession().getAttribute("user");
        whgExh.setExhid(commService.getKey("whg_exh"));
        whgExh.setExhcrttime(new Date());
        whgExh.setExhcultid(user.getCultid());
        whgExh.setExhdeptid(user.getDeptid());
        whgExh.setExhstate(EnumState.STATE_YES.getValue());
        whgExh.setExhghp(0);
        this.whgExhMapper.insertSelective(whgExh);

    }

    /**
     * 编辑
     *
     * @param
     */
    public void t_edit(WhgExh whgExh) throws Exception {
        whgExh.setExhcrttime(new Date());
        this.whgExhMapper.updateByPrimaryKeySelective(whgExh);
    }


    /**
     * 更新文化品牌状态
     * @param ids 文化品牌ID，多个用逗号分隔
     * @param fromState 改状态之前的限制状态
     * @param toState 修改后的状态
     * @throws Exception
     */
    public void t_updstate(String ids, String fromState, String toState)throws Exception{
        if(ids != null && toState != null){
            String[] idArr = ids.split(",");
            Example example = new Example(WhgExh.class);
            Example.Criteria c = example.createCriteria();
            c.andIn("exhid", Arrays.asList(idArr));
            if(fromState != null){
                c.andEqualTo("exhstate", fromState);
            }
            WhgExh record = new WhgExh();
            record.setExhstate(Integer.parseInt(toState));
            Date now = new Date();
            record.setExhopttime(now);
            this.whgExhMapper.updateByExampleSelective(record, example);
        }
    }

    /**
     * 删除
     *
     * @param request
     * @throws Exception
     */
    public void t_del(HttpServletRequest request) throws Exception {
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
        String exhid = (String) paramMap.get("exhid");
        int result = this.whgExhMapper.deleteByPrimaryKey(exhid);
        if (result != 1) {
            throw new Exception("删除数据失败！");
        }
    }


}
