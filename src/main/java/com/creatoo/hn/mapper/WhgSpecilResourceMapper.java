package com.creatoo.hn.mapper;

import com.creatoo.hn.model.WhgSpecilResource;
import com.creatoo.hn.model.WhgSpecilResourceSarch;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface WhgSpecilResourceMapper extends Mapper<WhgSpecilResource> {
    /**
     * 特色资源管理页面关联查询
     * @return
     */
    List<WhgSpecilResourceSarch> searchName(Map map);
}