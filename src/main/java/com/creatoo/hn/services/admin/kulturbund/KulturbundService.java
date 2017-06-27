package com.creatoo.hn.services.admin.kulturbund;

import com.creatoo.hn.mapper.WhgCultureActMapper;
import com.creatoo.hn.mapper.WhgCultureActfragMapper;
import com.creatoo.hn.mapper.WhgCultureUnitMapper;
import com.creatoo.hn.model.WhgCultureAct;
import com.creatoo.hn.model.WhgCultureActfrag;
import com.creatoo.hn.model.WhgCultureUnit;
import com.creatoo.hn.services.comm.CommService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**文化联盟服务
 * Created by caiyong on 2017/6/22.
 */
@Service
@SuppressWarnings("all")
public class KulturbundService {

    private static Logger logger = Logger.getLogger(KulturbundService.class);

    @Autowired
    private WhgCultureUnitMapper whgCultureUnitMapper;

    @Autowired
    private WhgCultureActMapper whgCultureActMapper;

    @Autowired
    private WhgCultureActfragMapper whgCultureActfragMapper;

    @Autowired
    private CommService commService;

    /**
     * 获取文化联盟单位数据，支持分页
     * @param page
     * @param rows
     * @return
     */
    public PageInfo getCultureUnit(Integer page,Integer rows,String name,String state){
        PageHelper.startPage(page,rows);
        Example example = new Example(WhgCultureUnit.class);
        Example.Criteria criteria = example.createCriteria();
        if(null != name){
            criteria.andLike("unitname","%"+name+"%");
        }
        if(null != state && !"0".equals(state)){
            criteria.andEqualTo("unitstate",Integer.valueOf(state));
        }
        example.setOrderByClause("unitcreatetime desc");
        List<WhgCultureUnit> list = whgCultureUnitMapper.selectByExample(example);
        return new PageInfo(list);
    }

    /**
     * 获取文化联盟大型活动数据，支持分页
     * @param page
     * @param rows
     * @param name
     * @param state
     * @return
     */
    public PageInfo getCultureAct(Integer page,Integer rows,String name,String state){
        PageHelper.startPage(page,rows);
        Example example = new Example(WhgCultureAct.class);
        Example.Criteria criteria = example.createCriteria();
        if(null != name){
            criteria.andLike("culactname","%"+name+"%");
        }
        if(null != state){
            if("edit".equals(state)){
                criteria.andIn("culactstate", Arrays.asList(0));
                criteria.andEqualTo("isdel",2);
            }else if("check".equals(state)){
                criteria.andIn("culactstate", Arrays.asList(1));
                criteria.andEqualTo("isdel",2);
            }else if("publish".equals(state)){
                criteria.andIn("culactstate", Arrays.asList(2,3));
                criteria.andEqualTo("isdel",2);
            }else if("cycle".equals(state)){
                criteria.andEqualTo("isdel",1);
            }
        }
        example.setOrderByClause("culactcreattime desc");
        List<WhgCultureAct> list = whgCultureActMapper.selectByExample(example);
        return new PageInfo(list);
    }



    /**
     * 添加文化联盟单位
     * @param whgCultureUnit
     * @return
     */
    public int doAdd(WhgCultureUnit whgCultureUnit){
        try {
            whgCultureUnit.setId(commService.getKey("whg_culture_unit"));
            whgCultureUnit.setUnitcreatetime(new Date());
            whgCultureUnit.setUnitstate(2);
            whgCultureUnitMapper.insert(whgCultureUnit);
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }

    /**
     * 修改文化联盟单位
     * @param whgCultureUnit
     * @return
     */
    public int doEdit(WhgCultureUnit whgCultureUnit){
        try {
            WhgCultureUnit temp = whgCultureUnitMapper.selectByPrimaryKey(whgCultureUnit.getId());
            if(null != temp){
                whgCultureUnit.setUnitcreatetime(temp.getUnitcreatetime());
                whgCultureUnit.setUnitstate(temp.getUnitstate());
                whgCultureUnitMapper.updateByPrimaryKey(whgCultureUnit);
            }
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }

    /**
     * 修改文化联盟单位状态
     * @param whgCultureUnit
     * @return
     */
    public int updateState(WhgCultureUnit whgCultureUnit){
        try {
            WhgCultureUnit tmp = whgCultureUnitMapper.selectByPrimaryKey(whgCultureUnit);
            if(null == tmp){
                return 1;
            }
            tmp.setUnitstate(whgCultureUnit.getUnitstate());
            whgCultureUnitMapper.updateByPrimaryKey(tmp);
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }

    /**
     * 查询一个文化联盟单位
     * @param whgCultureUnit
     * @return
     */
    public WhgCultureUnit getOne(WhgCultureUnit whgCultureUnit){
        try {
            return whgCultureUnitMapper.selectOne(whgCultureUnit);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 查询一个文化联盟大型活动
     * @param whgCultureAct
     * @return
     */
    public WhgCultureAct getOne(WhgCultureAct whgCultureAct){
        try {
            return whgCultureActMapper.selectOne(whgCultureAct);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 查询多个文化联盟大型活动片段
     * @param whgCultureActfrag
     * @return
     */
    public List<WhgCultureActfrag> getAll(WhgCultureActfrag whgCultureActfrag){
        try {
            return whgCultureActfragMapper.select(whgCultureActfrag);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 删除一个文化联盟单位
     * @param whgCultureUnit
     * @return
     */
    public int delOne(WhgCultureUnit whgCultureUnit){
        try {
            whgCultureUnitMapper.deleteByPrimaryKey(whgCultureUnit);
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }
}
