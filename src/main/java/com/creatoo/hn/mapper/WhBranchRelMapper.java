package com.creatoo.hn.mapper;

import com.creatoo.hn.model.WhBranchRel;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Map;

public interface WhBranchRelMapper extends Mapper<WhBranchRel> {

    public void delByParam(@Param("param")Map map);

}