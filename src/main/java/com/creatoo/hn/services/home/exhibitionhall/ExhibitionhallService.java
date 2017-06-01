package com.creatoo.hn.services.home.exhibitionhall;

import com.creatoo.hn.mapper.home.CrtExhibitionhallMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**数字展馆服务
 * Created by caiyong on 2017/4/28.
 */
@SuppressWarnings("all")
@Service
public class ExhibitionhallService {

    private static Logger logger = Logger.getLogger(ExhibitionhallService.class);

    @Autowired
    private CrtExhibitionhallMapper crtExhibitionhallMapper;

    /**
     * 获取360展示大图
     * @return
     */
    public List<Map> get360Photo(){
        try {
            PageHelper.startPage(1,6);
            List<Map> list = crtExhibitionhallMapper.get360Photo();
            if(null == list || list.isEmpty()){
                return null;
            }
            return list;//只返回最近更新的图
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 获取展馆列表
     * @param page
     * @param rows
     * @return
     */
    public PageInfo getExhibitionhallList(Integer page, Integer rows){
        try {
            PageHelper.startPage(page ,rows);
            return new PageInfo(crtExhibitionhallMapper.getExhibitionhallList());
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 获取单个展馆信息
     * @param id
     * @return
     */
    public Map getHallInfo(String id){
        try {
            List<Map> myList = crtExhibitionhallMapper.getExhibitionhallInfo(id);
            if(null == myList || myList.isEmpty()){
                return null;
            }
            return myList.get(0);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    public PageInfo getExhibitList(String id,Integer page,Integer rows){
        try {
            PageHelper.startPage(page,rows);
            List<Map> list = crtExhibitionhallMapper.getExhibitList(id);
            return new PageInfo(list);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }
}
