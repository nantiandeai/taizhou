package com.creatoo.hn.services.api.live;

import com.creatoo.hn.mapper.WhgLiveMapper;
import com.creatoo.hn.mapper.WhgYwiKeyMapper;
import com.creatoo.hn.mapper.WhgYwiTypeMapper;
import com.creatoo.hn.model.WhgLive;
import com.creatoo.hn.model.WhgYwiKey;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**云直播接口服务
 * Created by caiyong on 2017/7/7.
 */
@Service
public class LiveServiceForApi {

    private static Logger logger = Logger.getLogger(LiveServiceForApi.class);

    @Autowired
    private WhgYwiTypeMapper whgYwiTypeMapper;

    @Autowired
    private WhgYwiKeyMapper whgYwiKeyMapper;

    @Autowired
    private WhgLiveMapper whgLiveMapper;

    public List<WhgYwiKey> getAllDomainByType(String type){
        try {
            Example example = new Example(WhgYwiKey.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("type",type);
            criteria.andEqualTo("state",1);
            criteria.andEqualTo("delstate",0);
            example.setOrderByClause("idx");
            List<WhgYwiKey> whgYwiKeyList = whgYwiKeyMapper.selectByExample(example);
            return whgYwiKeyList;
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    public PageInfo getLiveList(Integer page,Integer rows,WhgLive whgLive){
        try {
            PageHelper.startPage(page,rows);
            List<WhgLive> whgLiveList = whgLiveMapper.select(whgLive);
            return new PageInfo(whgLiveList);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    public PageInfo searchLiveList(String page,String rows,String domain,String type,String sort){
        try {
            PageHelper.startPage(Integer.valueOf(page),Integer.valueOf(rows));

            return new PageInfo();
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

}
