package com.creatoo.hn.actions.api.collection;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.model.WhCollection;
import com.creatoo.hn.services.api.collection.APICollectionService;
import com.creatoo.hn.services.comm.CommService;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * Created by chenf on 2017/5/5.
 */
@RestController
@RequestMapping("/api/vote")
public class APICollectionAction {
    /**
     * 日志控制器
     */
    Logger log = Logger.getLogger(this.getClass().getName());
    /**
     * 点赞服务
     */
    @Autowired
    private APICollectionService collectionService;
    /**
     * 公共服务
     */
    @Autowired
    private CommService commService;
    /**
     * 获取点赞列表
     * @param userId 用户id
     * @param itemId 资源id
     * @param type 资源类型 1. 活动 2. 场馆活动室 3. 培训课程
     * @param index 页码
     * @param size 每页显示数量
     * @return
     */
    @CrossOrigin
    @RequestMapping("/list")
    public ResponseBean getList(String userId, String itemId, Integer type, Integer index, Integer size){
        ResponseBean res = new ResponseBean();
        try{
            if (null==userId||null==itemId||null==type||null==index||null==size){
                res.setSuccess("1000");
                res.setErrormsg("必要参数不完整！");
                return  res;
            }
           PageInfo<Map> colleList=collectionService.getCollectionList(userId,itemId,type,index,size);
            res.setData(colleList.getList());
            res.setTotal(colleList.getTotal());
            res.setPage(colleList.getPageNum());
            res.setPageSize(colleList.getPageSize());
        } catch (Exception e){
            log.error(e.getMessage(), e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
        }
        return res;
    }

    /**
     *添加点赞记录
     * @param userId  用户id
     * @param itemId 资源id
     * @param type 类型 1. 活动 2. 场馆活动室 3. 培训课程
     * @return
     */
    @CrossOrigin
    @RequestMapping("/create")
    public ResponseBean addColle(String userId,String itemId,Integer type){
        ResponseBean res = new ResponseBean();
        try {
            if (null==userId||null==itemId||null==type){
                res.setSuccess("1000");
                res.setErrormsg("必要参数不完整！");
                return  res;
            }
            WhCollection collection=new WhCollection();
            collection.setCmid(commService.getKey("wh_collection"));
            collection.setCmdate(new Date());
            collection.setCmopttyp(String.valueOf(2));
            collection.setCmrefid(itemId);
            collection.setCmuid(userId);
            collection.setCmreftyp(String.valueOf(type));
            collectionService.addCollection(collection);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
        }
        return res;
    }
}
