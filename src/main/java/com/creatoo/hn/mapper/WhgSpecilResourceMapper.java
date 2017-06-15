package com.creatoo.hn.mapper;

import com.creatoo.hn.model.WhgSpecilResource;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface WhgSpecilResourceMapper extends Mapper<WhgSpecilResource> {
    /**
     * 特色资源管理页面关联查询
     * @return
     */
    List<WhgSpecilResource> searchName();
}