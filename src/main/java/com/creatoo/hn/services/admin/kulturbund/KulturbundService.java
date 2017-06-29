package com.creatoo.hn.services.admin.kulturbund;

import com.creatoo.hn.mapper.WhgCultureActMapper;
import com.creatoo.hn.mapper.WhgCultureActfragMapper;
import com.creatoo.hn.mapper.WhgCultureUnitMapper;
import com.creatoo.hn.mapper.WhgCultureZxMapper;
import com.creatoo.hn.model.WhgCultureAct;
import com.creatoo.hn.model.WhgCultureActfrag;
import com.creatoo.hn.model.WhgCultureUnit;
import com.creatoo.hn.model.WhgCultureZx;
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
    private WhgCultureZxMapper whgCultureZxMapper;

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
     * 获取文化联盟大型资讯数据，支持分页
     * @param page
     * @param rows
     * @param name
     * @param state
     * @return
     */
    public PageInfo getCultureInfo(Integer page,Integer rows,String name,String state){
        PageHelper.startPage(page,rows);
        Example example = new Example(WhgCultureZx.class);
        Example.Criteria criteria = example.createCriteria();
        if(null != name){
            criteria.andLike("culzxtitle","%"+name+"%");
        }
        if(null != state){
            if("edit".equals(state)){
                criteria.andIn("culzxstate", Arrays.asList(0));
                criteria.andEqualTo("isdel",2);
            }else if("check".equals(state)){
                criteria.andIn("culzxstate", Arrays.asList(1));
                criteria.andEqualTo("isdel",2);
            }else if("publish".equals(state)){
                criteria.andIn("culzxstate", Arrays.asList(2,3));
                criteria.andEqualTo("isdel",2);
            }else if("cycle".equals(state)){
                criteria.andEqualTo("isdel",1);
            }
        }
        example.setOrderByClause("culzxcreattime desc");
        List<WhgCultureZx> list = whgCultureZxMapper.selectByExample(example);
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
     * 添加文化联盟大型活动
     * @param whgCultureAct
     * @return
     */
    public int doAdd(WhgCultureAct whgCultureAct){
        try {
            whgCultureAct.setId(commService.getKey("whg_culture_act"));
            whgCultureAct.setCulactcreattime(new Date());
            whgCultureAct.setCulactstate(0);
            whgCultureAct.setIsdel(2);
            whgCultureActMapper.insert(whgCultureAct);
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }

    /**
     * 添加文化联盟资讯
     * @param whgCultureZx
     * @return
     */
    public int doAdd(WhgCultureZx whgCultureZx){
        try {
            whgCultureZx.setId(commService.getKey("whg_culture_zx"));
            whgCultureZx.setCulzxstate(0);
            whgCultureZx.setCulzxcreattime(new Date());
            whgCultureZx.setIsdel(2);
            whgCultureZxMapper.insert(whgCultureZx);
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
     * 修改文化联盟资讯
     * @param whgCultureZx
     * @return
     */
    public int doEdit(WhgCultureZx whgCultureZx){
        try {
            WhgCultureZx temp = whgCultureZxMapper.selectByPrimaryKey(whgCultureZx.getId());
            if(null != temp){
                whgCultureZx.setCulzxcreattime(temp.getCulzxcreattime());
                whgCultureZx.setCulzxstate(temp.getCulzxstate());
                whgCultureZx.setCulzxcreator(temp.getCulzxcreator());
                whgCultureZx.setIsdel(temp.getIsdel());
                whgCultureZxMapper.updateByPrimaryKey(whgCultureZx);
            }
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }

    /**
     * 修改文化联盟活动
     * @param whgCultureUnit
     * @return
     */
    public int doEdit(WhgCultureAct whgCultureAct){
        try {
            WhgCultureAct temp = whgCultureActMapper.selectByPrimaryKey(whgCultureAct.getId());
            if(null != temp){
                whgCultureAct.setCulactcreattime(temp.getCulactcreattime());
                whgCultureAct.setCulactstate(temp.getCulactstate());
                whgCultureAct.setIsdel(temp.getIsdel());
                whgCultureActMapper.updateByPrimaryKey(whgCultureAct);
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
     * 修改文化联盟活动状态
     * @param whgCultureAct
     * @return
     */
    public int updateState(WhgCultureAct whgCultureAct){
        try {
            WhgCultureAct tmp = whgCultureActMapper.selectByPrimaryKey(whgCultureAct);
            if(null == tmp){
                return 1;
            }
            tmp.setCulactstate(whgCultureAct.getCulactstate());
            whgCultureActMapper.updateByPrimaryKey(tmp);
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }

    /**
     * 修改文化联盟资讯状态
     * @param whgCultureZx
     * @return
     */
    public int updateState(WhgCultureZx whgCultureZx){
        try {
            WhgCultureZx tmp = whgCultureZxMapper.selectByPrimaryKey(whgCultureZx);
            if(null == tmp){
                return 1;
            }
            tmp.setCulzxstate(whgCultureZx.getCulzxstate());
            whgCultureZxMapper.updateByPrimaryKey(tmp);
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
     * @param whgCultureZx
     * @return
     */
    public WhgCultureZx getOne(WhgCultureZx whgCultureZx){
        try {
            return whgCultureZxMapper.selectOne(whgCultureZx);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 查询一个文化联盟资讯
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
     * 修改删除状态
     * @param whgCultureAct
     * @return
     */
    public int doDel(WhgCultureAct whgCultureAct){
        try {
            WhgCultureAct tmp = whgCultureActMapper.selectByPrimaryKey(whgCultureAct);
            tmp.setIsdel(whgCultureAct.getIsdel());
            whgCultureActMapper.updateByPrimaryKey(tmp);
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }

    /**
     * 修改删除状态
     * @param whgCultureZx
     * @return
     */
    public int doDel(WhgCultureZx whgCultureZx){
        try {
            WhgCultureZx tmp = whgCultureZxMapper.selectByPrimaryKey(whgCultureZx);
            tmp.setIsdel(whgCultureZx.getIsdel());
            whgCultureZxMapper.updateByPrimaryKey(tmp);
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
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

    /**
     * 删除一个文化联盟活动
     * @param whgCultureAct
     * @return
     */
    public int delOne(WhgCultureAct whgCultureAct){
        try {
            whgCultureActMapper.deleteByPrimaryKey(whgCultureAct);
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }

    /**
     * 删除一个文化联盟资讯
     * @param whgCultureZx
     * @return
     */
    public int delOne(WhgCultureZx whgCultureZx){
        try {
            whgCultureZxMapper.deleteByPrimaryKey(whgCultureZx);
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }
}
