package com.creatoo.hn.services.admin.live;

import com.creatoo.hn.mapper.WhgLiveMapper;
import com.creatoo.hn.model.WhgLive;
import com.creatoo.hn.model.WhgSysUser;
import com.creatoo.hn.services.comm.CommService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
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

    @Autowired
    private CommService commService;
    /**
     * 多维度查询
     * @param page
     * @param rows
     * @param map
     * @return
     */
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
            if(map.containsKey("isdel")){
                criteria.andEqualTo("isdel",Integer.valueOf((String)map.get("isdel")));
            }
            example.setOrderByClause("livecreattime desc");
            List<WhgLive> whgLiveList = whgLiveMapper.selectByExample(example);
            return new PageInfo(whgLiveList);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 添加1条记录
     * @param whgLive
     * @return
     */
    public WhgLive addOne(WhgLive whgLive, WhgSysUser whgSysUser){
        try {
            whgLive.setId(commService.getKey("whg_live"));
            whgLive.setLivestate(0);
            whgLive.setLivecreattime(new Date());
            whgLive.setLivecreator(whgSysUser.getId());
            whgLive.setIsdel(2);
            whgLiveMapper.insert(whgLive);
            return whgLive;
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 编辑数据入库
     * @param whgLive
     * @return
     */
    public WhgLive updateOne(WhgLive whgLive){
        try {
            WhgLive tmp = new WhgLive();
            tmp.setId(whgLive.getId());
            tmp = whgLiveMapper.selectOne(tmp);
            if(null == tmp){
                return null;
            }
            tmp.setLivetitle(whgLive.getLivetitle());
            tmp.setDomain(whgLive.getDomain());
            tmp.setLivecover(whgLive.getLivecover());
            tmp.setLivelbt(whgLive.getLivelbt());
            tmp.setIslbt(whgLive.getIslbt());
            tmp.setIsrecommend(whgLive.getIsrecommend());
            tmp.setFlowaddr(whgLive.getFlowaddr());
            whgLiveMapper.updateByPrimaryKey(tmp);
            return whgLive;
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 修改状态
     * @param whgLive
     * @return
     */
    public int updateState(WhgLive whgLive){
        try {
            WhgLive tmp = whgLiveMapper.selectByPrimaryKey(whgLive);
            tmp.setLivestate(whgLive.getLivestate());
            whgLiveMapper.updateByPrimaryKey(tmp);
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }

    /**
     * 删除/反删除
     * @param whgLive
     * @return
     */
    public int del(WhgLive whgLive){
        try {
            if(-1 == whgLive.getIsdel()){
                whgLiveMapper.deleteByPrimaryKey(whgLive);
            }else {
                WhgLive tmp = whgLiveMapper.selectByPrimaryKey(whgLive);
                if(null == tmp){
                    return 1;
                }
                tmp.setIsdel(whgLive.getIsdel());
                whgLiveMapper.updateByPrimaryKey(tmp);
            }
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }

    /**
     * 多维度单个查询
     * @param whgLive
     * @return
     */
    public WhgLive getOne(WhgLive whgLive){
        try {
            List<WhgLive> whgLiveList = whgLiveMapper.select(whgLive);
            if(null == whgLiveList || whgLiveList.isEmpty()){
                return null;
            }
            return whgLiveList.get(0);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }
}
