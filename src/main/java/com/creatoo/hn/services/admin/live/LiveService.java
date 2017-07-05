package com.creatoo.hn.services.admin.live;

import com.creatoo.hn.mapper.WhgLiveMapper;
import com.creatoo.hn.model.WhgLive;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

/**云直播服务
 * Created by caiyong on 2017/7/5.
 */
@SuppressWarnings("all")
@Service
public class LiveService {

    private static Logger logger = Logger.getLogger(LiveService.class);

    @Autowired
    private WhgLiveMapper whgLiveMapper;

    public PageInfo getLiveList(String page, String rows, Map map){
        try {
            PageHelper.startPage(Integer.valueOf(page),Integer.valueOf(rows));
            Example example = new Example(WhgLive.class);
            Example.Criteria criteria = example.createCriteria();
            if(map.containsKey("livetitle")){
                criteria.andLike("livetitle","%"+(String) map.get("livetitle")+"%");
            }
            if(map.containsKey("livestate")){
                criteria.andEqualTo("livestate",Integer.valueOf((String)map.get("livestate")));
            }
            example.setOrderByClause("livecreattime desc");
            List<WhgLive> whgLiveList = whgLiveMapper.selectByExample(example);
            return new PageInfo(whgLiveList);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }


}
