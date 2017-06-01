package com.creatoo.hn.services.home.adgszyd;

import com.creatoo.hn.mapper.home.CtrSzydMapper;
import com.github.pagehelper.PageHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**数字阅读服务
 * Created by caiyong on 2017/4/25.
 */
@Service
public class SzydService {

    private static Logger logger = Logger.getLogger(SzydService.class);

    @Autowired
    private CtrSzydMapper ctrSzydMapper;

    public List<Map> getReadingInfoList(Integer page,Integer rows){
        PageHelper.startPage(page,rows);
        try{
            return ctrSzydMapper.getReadingInfo();
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    public List<Map> getReadingCarousel(Integer page,Integer rows){
        PageHelper.startPage(page,rows);
        try {
            return ctrSzydMapper.getCarouselForReading();
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    public List<Map> getAvailableActList(Integer page,Integer rows){
        PageHelper.startPage(page,rows);
        try {
            return null;
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

}
