package com.creatoo.hn.services.api.showstyle;

import com.creatoo.hn.mapper.WhgSpecilResourceMapper;
import com.creatoo.hn.model.WhgCultHeritage;
import com.creatoo.hn.model.WhgSpecilResource;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

/**
 * Created by luzhihuai on 2017/6/28
 */
@Service
public class APIShowStyleService {

    @Autowired
    private WhgSpecilResourceMapper whgSpecilResourceMapper;


    /**
     * 秀我风采分页查询
     *
     * @return
     * @throws Exception
     */
    public PageInfo t_srchList4p(String type, Integer index, Integer size) throws Exception {
        //搜索条件
//        Example example = new Example(WhgSpecilResource.class);
//        Example.Criteria c = example.createCriteria();
//        if (type != null && !"".equals(type)) {
//            c.andEqualTo("type", type);
//        }
//        c.andEqualTo("state", 6).andEqualTo("isdel", 0).andEqualTo("intype",1);
//        example.setOrderByClause("crtdate desc");
        //分页查询
        PageHelper.startPage(index, size);
        List<Map> typeList = this.whgSpecilResourceMapper.showStyleList(type);
        return new PageInfo<>(typeList);
    }

    /**
     * 秀我风采查询详情
     *
     * @param id 文化遗产id
     * @return 对象
     * @throws Exception
     */
    public WhgSpecilResource t_srchOne(String id) throws Exception {
        return this.whgSpecilResourceMapper.selectByPrimaryKey(id);
    }

    /**
     * 秀我风采推荐列表
     *
     * @param size size
     * @return
     * @throws Exception
     */
    public List selectStyleRecommend(Integer size, String notId,Integer intype) throws Exception {
        WhgSpecilResource resource = new WhgSpecilResource();
        resource.setIsdel(0);
        resource.setState(6);
        resource.setType("2");
        resource.setIntype(intype);
        size = size == null ? 6 : size;
        Example example = new Example(WhgCultHeritage.class);
        Example.Criteria c = example.createCriteria().andEqualTo(resource);
        if (notId != null) {
            c.andNotEqualTo("id", notId);
        }
        example.orderBy("crtdate").desc();
        return this.whgSpecilResourceMapper.selectByExampleAndRowBounds(example, new RowBounds(0, size));
    }

    /**
     * 个人中心我的上传查询
     *
     * @return 分页对象
     * @throws Exception
     */
    public PageInfo<WhgSpecilResource> searchPersonStyle(String userId, Integer index, Integer size) throws Exception {
        //分页查询
        PageHelper.startPage(index, size);
        List<WhgSpecilResource> list = this.whgSpecilResourceMapper.searchPersonStyle(userId);
        return new PageInfo<>(list);
    }

    /**
     * 市民风采风采分页查询
     *
     * @return
     * @throws Exception
     */
    public PageInfo cityStyle(String type, String areaId, Integer index, Integer size) throws Exception {
        //搜索条件
//        Example example = new Example(WhgSpecilResource.class);
//        Example.Criteria c = example.createCriteria();
//        if (type != null && !"".equals(type)) {
//            c.andEqualTo("type", type);
//        }
//        if (aireId != null && !"".equals(aireId)) {
//            c.andEqualTo("areaid", aireId);
//        }
//        c.andEqualTo("state", 6).andEqualTo("isdel", 0).andEqualTo("intype",2);
//        example.setOrderByClause("crtdate desc");
//        //分页查询
        PageHelper.startPage(index, size);
        List<Map> typeList = this.whgSpecilResourceMapper.selectPersonStyleList(type,areaId);
        return new PageInfo<>(typeList);
    }
}
