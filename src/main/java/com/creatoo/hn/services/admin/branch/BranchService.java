package com.creatoo.hn.services.admin.branch;

import com.creatoo.hn.mapper.WhBranchMapper;
import com.creatoo.hn.mapper.WhTypMapper;
import com.creatoo.hn.mapper.WhUserBranchRelMapper;
import com.creatoo.hn.model.WhBranch;
import com.creatoo.hn.model.WhTyp;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**分馆服务
 * Created by caiyong on 2017/6/2.
 */
@Service
public class BranchService {

    private static Logger logger = Logger.getLogger(BranchService.class);

    @Autowired
    private WhBranchMapper whBranchMapper;

    @Autowired
    private WhTypMapper whTypMapper;

    @Autowired
    private WhUserBranchRelMapper whUserBranchRelMapper;


    /**
     * 获取分馆列表
     * @param page
     * @param rows
     * @return
     */
    public PageInfo getBranchList(Integer page,Integer rows){
        PageHelper.startPage(page,rows);
        try {
            return new PageInfo(whBranchMapper.getBranchListAll(new HashMap()));
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 获取所有分馆
     * @return
     */
    public List<Map> getBranchListAll(Map map){
        try {
            return whBranchMapper.getBranchListAll(map);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 获取用户的所有分馆
     * @param userId
     * @return
     */
    public List<Map> getUserBranchInfo(String  userId){
        try {
            if(null == userId){
                return null;
            }
            return whUserBranchRelMapper.getUserBranchInfo(userId);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 获取所有的区域
     * @return
     */
    public List<WhTyp> getArea(){
        WhTyp whTyp = new WhTyp();
        whTyp.setType("8");
        try {
            return whTypMapper.select(whTyp);
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    /**
     * 修改分馆表
     * @param whBranch
     * @param updateType 1:添加;2:修改;3:删除
     * @return
     */
    public void saveBranch(WhBranch whBranch,int updateType){
        switch (updateType){
            case 1:{
                whBranchMapper.insert(whBranch);
                break;
            }
            case 2:{
                whBranchMapper.updateByPrimaryKeySelective(whBranch);
                break;
            }
            case 3:{
                whBranchMapper.deleteByPrimaryKey(whBranch);
                break;
            }
            default:{
                break;
            }
        }
    }

}
