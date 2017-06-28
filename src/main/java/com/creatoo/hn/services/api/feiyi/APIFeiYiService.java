package com.creatoo.hn.services.api.feiyi;

import com.creatoo.hn.mapper.WhgCultHeritageMapper;
import com.creatoo.hn.mapper.WhgCultTalentsMapper;
import com.creatoo.hn.mapper.WhgHistoricalMapper;
import com.creatoo.hn.model.WhgCultHeritage;
import com.creatoo.hn.model.WhgCultTalents;
import com.creatoo.hn.model.WhgHistorical;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by luzhihuai on 2017/6/26
 */
@Service
public class APIFeiYiService {

    @Autowired
    private WhgCultHeritageMapper whgCultHeritageMapper;

    @Autowired
    private WhgHistoricalMapper whgHistoricalMapper;

    @Autowired
    private WhgCultTalentsMapper whgCultTalentsMapper;

    /**
     * 文化遗产分页查询
     *
     * @return
     * @throws Exception
     */
    public PageInfo<WhgCultHeritage> t_srchList4p(String type, Integer index, Integer size) throws Exception {
        //搜索条件
        Example example = new Example(WhgCultHeritage.class);
        Example.Criteria c = example.createCriteria();
        if (type != null && !"".equals(type)) {
            c.andEqualTo("type", type);
        }
        c.andEqualTo("state", 6).andEqualTo("isdel", 0);
        example.setOrderByClause("crtdate desc");
        //分页查询
        PageHelper.startPage(index, size);
        List<WhgCultHeritage> typeList = this.whgCultHeritageMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 文化遗产查询详情
     *
     * @param id 文化遗产id
     * @return 对象
     * @throws Exception
     */
    public WhgCultHeritage t_srchOne(String id) throws Exception {
        return this.whgCultHeritageMapper.selectByPrimaryKey(id);
    }

    /**
     * 文化遗产推荐列表
     *
     * @param size size
     * @return
     * @throws Exception
     */
    public List selectCultRecommend(Integer size, String notId) throws Exception {
        WhgCultHeritage cultHeritage = new WhgCultHeritage();
        cultHeritage.setIsdel(0);
        cultHeritage.setState(6);
        cultHeritage.setIsrecommend(1);
        size = size == null ? 6 : size;
        Example example = new Example(WhgCultHeritage.class);
        Example.Criteria c = example.createCriteria().andEqualTo(cultHeritage);
        if (notId != null) {
            c.andNotEqualTo("id", notId);
        }
        example.orderBy("statemdfdate").desc();
        return this.whgCultHeritageMapper.selectByExampleAndRowBounds(example, new RowBounds(0, size));
    }

    /**
     * 重点文物分页查询
     *
     * @return
     * @throws Exception
     */
    public PageInfo<WhgHistorical> selectHistorical(String type, Integer index, Integer size) throws Exception {
        //搜索条件
        Example example = new Example(WhgHistorical.class);
        Example.Criteria c = example.createCriteria();
        if (type != null && !"".equals(type)) {
            c.andEqualTo("type", type);
        }
        c.andEqualTo("state", 6).andEqualTo("isdel", 0);
        example.setOrderByClause("crtdate desc");
        //分页查询
        PageHelper.startPage(index, size);
        List<WhgHistorical> typeList = this.whgHistoricalMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 重点文物查询详情
     *
     * @param id 重点文物id
     * @return 对象
     * @throws Exception
     */
    public WhgHistorical searchHistorical(String id) throws Exception {
        return this.whgHistoricalMapper.selectByPrimaryKey(id);
    }

    /**
     * 重点文物推荐列表
     *
     * @param size size
     * @return
     * @throws Exception
     */
    public List selectHisRecommend(Integer size, String notId) throws Exception {
        WhgHistorical historical = new WhgHistorical();
        historical.setIsdel(0);
        historical.setState(6);
        historical.setIsrecommend(1);
        size = size == null ? 6 : size;
        Example example = new Example(WhgCultHeritage.class);
        Example.Criteria c = example.createCriteria().andEqualTo(historical);
        if (notId != null) {
            c.andNotEqualTo("id", notId);
        }
        example.orderBy("statemdfdate").desc();
        return this.whgCultHeritageMapper.selectByExampleAndRowBounds(example, new RowBounds(0, size));
    }

    /**
     * 文化人才分页查询
     *
     * @return 分页对象
     * @throws Exception
     */
    public PageInfo<WhgCultTalents> selectTalents(String type, Integer index, Integer size) throws Exception {
        //搜索条件
        Example example = new Example(WhgCultTalents.class);
        Example.Criteria c = example.createCriteria();
        if (type != null && !"".equals(type)) {
            c.andEqualTo("type", type);
        }
        c.andEqualTo("state", 6).andEqualTo("isdel", 0);
        example.setOrderByClause("crtdate desc");
        //分页查询
        PageHelper.startPage(index, size);
        List<WhgCultTalents> typeList = this.whgCultTalentsMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 文化人才查询详情
     *
     * @param id 文化人才id
     * @return 对象
     * @throws Exception
     */
    public WhgCultTalents searchTalents(String id) throws Exception {
        return this.whgCultTalentsMapper.selectByPrimaryKey(id);
    }

    /**
     * 重点文物推荐列表
     *
     * @param size size
     * @return
     * @throws Exception
     */
    public List selectTalRecommend(Integer size, String notId) throws Exception {
        WhgCultTalents talents = new WhgCultTalents();
        talents.setIsdel(0);
        talents.setState(6);
        talents.setIsrecommend(1);
        size = size == null ? 6 : size;
        Example example = new Example(WhgCultHeritage.class);
        Example.Criteria c = example.createCriteria().andEqualTo(talents);
        if (notId != null) {
            c.andNotEqualTo("id", notId);
        }
        example.orderBy("statemdfdate").desc();
        return this.whgCultHeritageMapper.selectByExampleAndRowBounds(example, new RowBounds(0, size));
    }

}
