package com.creatoo.hn.actions.api.comment;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.model.WhComment;
import com.creatoo.hn.services.api.Comment.APICommentService;
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
@RequestMapping("/api/comment")
public class APICommentAction {
    /**
     * 日志控制器
     */
    Logger log = Logger.getLogger(this.getClass().getName());
    /**
     *评论服务
     */
    @Autowired
    private APICommentService commentService;
    /**
     * 公共服务
     */
    @Autowired
    private CommService commService;
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
            PageInfo<Map> commentList=commentService.getCommentList(userId,itemId,type,index,size);
            res.setData(commentList.getList());
            res.setTotal(commentList.getTotal());
            res.setPage(commentList.getPageNum());
            res.setPageSize(commentList.getPageSize());
        } catch (Exception e){
            log.error(e.getMessage(), e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
        }
        return res;
    }
    @CrossOrigin
    @RequestMapping("/create")
    public ResponseBean addColle(String userId,String itemId,Integer type,String content){
        ResponseBean res = new ResponseBean();
        try {
            if (null==userId||null==itemId||null==type||null==content){
                res.setSuccess("1000");
                res.setErrormsg("必要参数不完整！");
                return  res;
            }
            WhComment comment=new WhComment();
            comment.setRmid(commService.getKey("wh_comment"));
            comment.setRmuid(userId);
            comment.setRmdate(new Date());
            comment.setRmcontent(content);
            comment.setRmreftyp(String.valueOf(type));
            comment.setRmrefid(itemId);
            comment.setRmstate(0);
            commentService.addComment(comment);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
        }
        return res;
    }

}
