package com.creatoo.hn.services.admin.ereading;

import com.creatoo.hn.mapper.WhSzydZxMapper;
import com.creatoo.hn.mapper.admin.CrtWhgReadingMapper;
import com.creatoo.hn.model.WhSzydZx;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**数字阅读资讯服务
 * Created by caiyong on 2017/4/20.
 */
@SuppressWarnings("all")
@Service
public class WhgEReadingInfoService {

    private static Logger logger = Logger.getLogger(WhgEReadingInfoService.class);

    @Autowired
    private CrtWhgReadingMapper crtWhgReadingMapper;
    
    @Autowired
    private WhSzydZxMapper whSzydZxMapper;
    /**
     * 获取数字阅读资讯列表
     * @param page 页数
     * @param rows 行数
     * @param zxstate 状态
     * @return
     */
    public PageInfo getInfoList(Integer page,Integer rows,Integer zxstate){
        if(null == page){
            page = 1;
        }
        if(null == rows){
            rows = 10;
        }
        if(null == zxstate){
            return null;
        }
        try {
            PageHelper.startPage(page,rows);
            List<Map> list = crtWhgReadingMapper.getInfoListByState(zxstate);
            return new PageInfo(list);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    public PageInfo getActList(Integer page,Integer rows){
        if(null == page){
            page = 1;
        }
        if(null == rows){
            rows = 10;
        }
        try{
            PageHelper.startPage(page,rows);
            return new PageInfo(crtWhgReadingMapper.getActListForReading());
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }

    }

    /**
     * 添加一个数字阅读资讯
     * @param map
     * @return
     */
    public int addOneReadingZx(Map map){
        try {
            crtWhgReadingMapper.addOneReadingZx(map);
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }
    
    /**
     * 查询单个对象信息
     * @param id
     * @return
     */
    public WhSzydZx t_serOne(String id){
    	WhSzydZx whSzydZx = new WhSzydZx();
    	whSzydZx.setId(id);
    	return whSzydZxMapper.selectOne(whSzydZx);
    }
    
    /**
     * 编辑对象
     * @param whSzydZx
     */
    public int upReadingZx(WhSzydZx whSzydZx){
    	return whSzydZxMapper.updateByPrimaryKeySelective(whSzydZx);
    }
    
    /**
     * 更新活动状态
     * @param statemdfdate 状态修改时间
     * @param ids 活动ID，多个用逗号分隔
     * @param fromState 改状态之前的限制状态
     * @param toState 修改后的状态
     * @throws Exception
     */
    public void t_updstate(String statemdfdate, String ids, String fromState, String toState)throws Exception{
        if(ids != null && toState != null){
            String[] idArr = ids.split(",");
            WhSzydZx record = whSzydZxMapper.selectByPrimaryKey(idArr[0]);
            record.setInfostate(Integer.parseInt(toState));
            record.setInfoupdatetime(new Date());
            this.whSzydZxMapper.updateByPrimaryKey(record);
        }
    }

    /**
     * 更新删除状态
     * @param statemdfdate 状态修改时间
     * @param ids 活动ID，多个用逗号分隔
     * @param fromState 改状态之前的限制状态
     * @param toState 修改后的状态
     * @throws Exception
     */
    public int t_updDelstate(String ids, String state){
        try {
            if(ids != null && state != null){
                String[] idArr = ids.split(",");
                WhSzydZx record = whSzydZxMapper.selectByPrimaryKey(idArr[0]);
                record.setIsdel(Integer.parseInt(state));
                record.setInfoupdatetime(new Date());
                this.whSzydZxMapper.updateByPrimaryKey(record);
            }
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }
    
}
