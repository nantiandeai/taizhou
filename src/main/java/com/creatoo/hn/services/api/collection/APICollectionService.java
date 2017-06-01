package com.creatoo.hn.services.api.collection;

import com.creatoo.hn.mapper.WhCollectionMapper;
import com.creatoo.hn.model.WhCollection;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jdk.nashorn.internal.ir.ReturnNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenf on 2017/5/3.
 */
@Service
public class APICollectionService {
    @Autowired
    private WhCollectionMapper collectionMapper;
    /**
     *
     * @param userId 用户id
     * @param itemId 资源id
     * @param type 活动类型 1. 活动 2. 场馆活动室 3. 培训课程
     * @param index 页码
     * @param size 每页显示条数
     */
    public PageInfo<Map> getCollectionList(String userId,String itemId,Integer type,Integer index,Integer size){
        PageHelper.startPage(index, size);
        Map param =new HashMap();
        param.put("userId",userId);
        param.put("itemId",itemId);
        param.put("type",type);
        List<Map> list=collectionMapper.selectCollectionWithUser(param);
        return new PageInfo<Map>(list) ;
    }

    /**
     * 添加点赞信息
     * @param collection 点赞实体
     * @return
     */
    public Integer addCollection(WhCollection collection){
         return collectionMapper.insert(collection);
    }

}
