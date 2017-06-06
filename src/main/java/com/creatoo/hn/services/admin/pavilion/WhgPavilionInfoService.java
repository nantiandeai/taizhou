package com.creatoo.hn.services.admin.pavilion;

import com.creatoo.hn.mapper.WhSzzgExhhallMapper;
import com.creatoo.hn.mapper.admin.CrtWhgPavilionMapper;
import com.creatoo.hn.model.WhSzzgExhhall;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**数字展馆服务
 * Created by caiyong on 2017/4/21.
 */
@SuppressWarnings("all")
@Service
public class WhgPavilionInfoService {

    private static Logger logger = Logger.getLogger(WhgPavilionInfoService.class);

    @Autowired
    private CrtWhgPavilionMapper crtWhgPavilionMapper;

    @Autowired
    private WhSzzgExhhallMapper whSzzgExhhallMapper;

    /**
     * 获取数字展馆列表
     * @param page 页数
     * @param rows 行数
     * @param zxstate 状态
     * @return
     */
    public PageInfo getInfoList(Integer page, Integer rows, Integer zxstate){
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
            List<Map> list = crtWhgPavilionMapper.getPavilionListByState(zxstate);
            return new PageInfo(list);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 获取数字展馆列表
     * @param page 页数
     * @param rows 行数
     * @param zxstate 状态
     * @return
     */
    public PageInfo getInfoList(Integer page, Integer rows, Integer zxstate,List relList){
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
            List<Map> list = crtWhgPavilionMapper.getPavilionListByStateEx(zxstate,relList);
            return new PageInfo(list);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    public int addOnePavilion(WhSzzgExhhall whSzzgExhhall){
        try{
            whSzzgExhhallMapper.insert(whSzzgExhhall);
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }

    public WhSzzgExhhall getOntById(String id){
        try {
            WhSzzgExhhall param = new WhSzzgExhhall();
            param.setId(id);
            return whSzzgExhhallMapper.selectOne(param);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    public int updatePavilionStateById(String id,Integer state){
        try {
            crtWhgPavilionMapper.updatePavilionState(id,state);
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }

    /**
     * 修改展馆删除状态
     * @param id
     * @param state
     * @return
     */
    public int updatePavilionIsDelById(String id,Integer state){
        try {
            crtWhgPavilionMapper.updatePavilionIsDel(id,state);
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }

    public int updateOne(WhSzzgExhhall whSzzgExhhall){
        try{
            whSzzgExhhallMapper.updateByPrimaryKey(whSzzgExhhall);
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }
}
