package com.creatoo.hn.mapper;

import com.creatoo.hn.model.WhgSpecilResource;
import com.creatoo.hn.model.WhgSpecilResourceSarch;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface WhgSpecilResourceMapper extends Mapper<WhgSpecilResource> {
    /**
     * 特色资源管理页面关联查询
     * @return
     */
    List<WhgSpecilResourceSarch> searchName(Map map);

    /**
     * 个人中心我的上传查询
     * @param userId 用户id
     * @return
     */
    List<WhgSpecilResource> searchPersonStyle(@Param("userId")String userId);

    /**
     * 市民风采列表查询
     */
    List<Map> selectPersonStyleList(@Param("type")String type,@Param("areaId")String areaId );

    /**
     * 特色资源列表查询
     * @param type 类型1.图片 2.视频 3.文档 4.音频
     * @return
     */
    List<Map> showStyleList(@Param("type")String type);
}