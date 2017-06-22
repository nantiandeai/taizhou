package com.creatoo.hn.services.admin.kulturbund;

import com.creatoo.hn.mapper.WhgCultureUnitMapper;
import com.creatoo.hn.model.WhgCultureUnit;
import com.creatoo.hn.services.comm.CommService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

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
