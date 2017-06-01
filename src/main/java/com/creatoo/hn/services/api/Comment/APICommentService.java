package com.creatoo.hn.services.api.Comment;

import com.creatoo.hn.mapper.WhCommentMapper;
import com.creatoo.hn.model.WhCollection;
import com.creatoo.hn.model.WhComment;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenf on 2017/5/5.
 */
@Service
public class APICommentService {
    @Autowired
    private WhCommentMapper commentMapper;

    /**
     * 获取用户评论列表
     * @param userId 用户id
     * @param itemId 资源id
     * @param type 资源类型
     * @param index 页码
     * @param size 每页显示条目
     * @return
     */
    public PageInfo<Map> getCommentList(String userId,String itemId,Integer type,Integer index,Integer size){
        PageHelper.startPage(index, size);
        Map param =new HashMap();
        param.put("userId",userId);
        param.put("itemId",itemId);
        param.put("type",type);
        List<Map> list=commentMapper.selectCommentWithUser(param);
        return new PageInfo<Map>(list) ;
    }

    /**
     * 添加评论
     * @param comment 评论对象
     * @return
     */
    public Integer addComment(WhComment comment){
        return commentMapper.insert(comment);
    }
}
