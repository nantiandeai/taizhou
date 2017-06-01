package com.creatoo.hn.services.admin.pavilion;

import com.creatoo.hn.mapper.WhSzzgExhibitMapper;
import com.creatoo.hn.mapper.WhgYwiKeyMapper;
import com.creatoo.hn.mapper.admin.CrtWhgPavilionMapper;
import com.creatoo.hn.model.WhgYwiKey;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**藏品服务
 * Created by caiyong on 2017/4/26.
 */
@SuppressWarnings("all")
@Service
public class WhgAntiquesInfoService {

    private static Logger logger = Logger.getLogger(WhgAntiquesInfoService.class);

    @Autowired
    private CrtWhgPavilionMapper crtWhgPavilionMapper;

    @Autowired
    private WhSzzgExhibitMapper whSzzgExhibitMapper;

    @Autowired
    private WhgYwiKeyMapper whgYwiKeyMapper;
    /**
     * 获取藏品列表(支持分页)
     * @param page
     * @param rows
     * @param zxstate
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
            List<Map> list = crtWhgPavilionMapper.getAntiquesListByState(zxstate);
            return new PageInfo(list);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 添加一个藏品
     * @param map
     * @return
     */
    public int addOneExhbitMapper(Map map){
        try {
            crtWhgPavilionMapper.addOneAntiques(map);
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }

    /**
     * 修改一个藏品
     * @param map
     * @return
     */
    public int updateOneExhbitMapper(Map map){
        try{
            crtWhgPavilionMapper.updateOneAntiques(map);
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }

    /**
     * 变更一个藏品状态
     * @param id
     * @param myState
     * @return
     */
    public int changeOneExhbitState(String id,Integer myState){
        try{
            crtWhgPavilionMapper.updateOneAntiquesState(id,myState);
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }

    /**
     * 变更一个藏品删除状态
     * @param id
     * @param myState
     * @return
     */
    public int changeOneExhbitDelState(String id,Integer myState){
        try{
            crtWhgPavilionMapper.updateOneAntiquesDelState(id,myState);
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }

    /**
     * 获取展馆列表
     * @return
     */
    public PageInfo getAllPavilion(Integer page,Integer rows){
        try {
            PageHelper.startPage(page,rows);
            return new PageInfo(crtWhgPavilionMapper.getAllPavilion());
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 获取藏品
     * @param id
     * @return
     */
    public Map getOneExhbit(String id){
        try {
            List<Map> list = crtWhgPavilionMapper.getExhbitById(id);
            if(null == list || list.isEmpty()){
                return null;
            }
            return list.get(0);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    public List<WhgYwiKey> getAllExhType(){
        WhgYwiKey whgYwiKey = new WhgYwiKey();
        whgYwiKey.setType("6");
        try {
            List<WhgYwiKey> list = whgYwiKeyMapper.select(whgYwiKey);
            if(null == list || list.isEmpty()){
                return null;
            }
            return list;
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }
}
