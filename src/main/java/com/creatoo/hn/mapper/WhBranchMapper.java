package com.creatoo.hn.mapper;

import com.creatoo.hn.model.WhBranch;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface WhBranchMapper extends Mapper<WhBranch> {

    List<Map> getBranchListAll();

}