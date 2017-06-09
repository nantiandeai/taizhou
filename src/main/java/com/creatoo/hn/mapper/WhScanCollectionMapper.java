package com.creatoo.hn.mapper;

import com.creatoo.hn.model.WhScanCollection;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface WhScanCollectionMapper extends Mapper<WhScanCollection> {

    public List<WhScanCollection> getWhScanCollectionByParam(@Param("whScanCollection") WhScanCollection whScanCollection);

}