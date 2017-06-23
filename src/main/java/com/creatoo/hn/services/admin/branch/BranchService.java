package com.creatoo.hn.services.admin.branch;

import com.creatoo.hn.mapper.WhBranchMapper;
import com.creatoo.hn.mapper.WhBranchRelMapper;
import com.creatoo.hn.mapper.WhTypMapper;
import com.creatoo.hn.mapper.WhUserBranchRelMapper;
import com.creatoo.hn.model.WhBranch;
import com.creatoo.hn.model.WhBranchRel;
import com.creatoo.hn.model.WhTyp;
import com.creatoo.hn.services.comm.CommService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private WhBranchRelMapper whBranchRelMapper;

    @Autowired
    private CommService commService;
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

    /**
     * 设置分馆关联表
     * @param relId
     * @param relType
     * @param branchId
     * @return
     */
    public int setBranchRel(String relId,String relType,String branchId){
        try {
            WhBranchRel whBranchRel = new WhBranchRel();
            whBranchRel.setId(commService.getKey("wh_branch_rel"));
            whBranchRel.setRelid(relId);
            whBranchRel.setReltype(relType);
            whBranchRel.setBranchid(branchId);
            whBranchRelMapper.insert(whBranchRel);
            return 0;
        }catch (Exception e){
            logger.error(e.toString());
            return 1;
        }
    }

    public void clearBranchRel(String relId,String relType){
        try {
            Map map = new HashMap();
            map.put("relid",relId);
            map.put("reltype",relType);
            whBranchRelMapper.delByParam(map);
        }catch (Exception e){
            logger.error(e.toString());
        }
    }

    /**
     * 获取分管关联信息
     * @param relId
     * @param relType
     * @return
     */
    public WhBranchRel getBranchRel(String relId,String relType){
        try {
            WhBranchRel whBranchRel = new WhBranchRel();
            whBranchRel.setRelid(relId);
            whBranchRel.setReltype(relType);
            whBranchRel = whBranchRelMapper.selectOne(whBranchRel);
            return whBranchRel;
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    public List<Map> getBranchRelList(String userId,String relType){
        if("2015121200000000".equals(userId)){
            return null;
        }
        try {
            Map param = new HashMap();
            param.put("userId",userId);
            param.put("relType",relType);
            return whBranchRelMapper.getWhBranchRelByUserId(param);
        }catch (Exception e){
            logger.error(e.toString());
            return new ArrayList<Map>();
        }
    }
}
