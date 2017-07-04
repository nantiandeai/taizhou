package com.creatoo.hn.services.api.kulturbund;

import com.creatoo.hn.mapper.WhgCultureActMapper;
import com.creatoo.hn.mapper.WhgCultureUnitMapper;
import com.creatoo.hn.mapper.WhgCultureZxMapper;
import com.creatoo.hn.model.WhgCultureAct;
import com.creatoo.hn.model.WhgCultureUnit;
import com.creatoo.hn.model.WhgCultureZx;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**文化联盟前台API服务
 * Created by caiyong on 2017/7/3.
 */
@Service
public class KulturbundServiceForApi {

    private static Logger logger = Logger.getLogger(KulturbundServiceForApi.class);

    @Autowired
    private WhgCultureUnitMapper whgCultureUnitMapper;

    @Autowired
    private WhgCultureActMapper whgCultureActMapper;

    @Autowired
    private WhgCultureZxMapper whgCultureZxMapper;

    public List<WhgCultureUnit> getCultureUnit(){
        Example example = new Example(WhgCultureUnit.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("unitstate",1);
        try {
            List<WhgCultureUnit> whgCultureUnitList = whgCultureUnitMapper.selectByExample(example);
            return whgCultureUnitList;
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    public PageInfo getCultureAct(Integer page, Integer rows){
        PageHelper.startPage(page,rows);
        Example example = new Example(WhgCultureAct.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("culactstate",3);
        example.setOrderByClause("culactcreattime desc");
        try {
            List<WhgCultureAct> whgCultureActList = whgCultureActMapper.selectByExample(example);
            return new PageInfo(whgCultureActList);
        }catch (Exception e){
          logger.error(e.toString());
          return null;
        }
    }

    public PageInfo getCultureZx(Integer page,Integer rows){
        PageHelper.startPage(page,rows);
        Example example = new Example(WhgCultureZx.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("culzxstate",3);
        example.setOrderByClause("culzxcreattime desc");
        try {
            List<WhgCultureZx> whgCultureZxList = whgCultureZxMapper.selectByExample(example);
            return new PageInfo(whgCultureZxList);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

}
