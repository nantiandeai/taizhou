package com.creatoo.hn.mapper;

import com.creatoo.hn.model.WhUserBranchRel;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface WhUserBranchRelMapper extends Mapper<WhUserBranchRel> {

    public List<Map> getUserBranchInfo(@Param("userId") String userId);

    public void clearBranch(@Param("userId") String userId);

}