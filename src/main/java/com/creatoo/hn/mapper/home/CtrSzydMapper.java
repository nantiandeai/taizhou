package com.creatoo.hn.mapper.home;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/25.
 */
@SuppressWarnings("all")
public interface CtrSzydMapper {

    /**
     * 获取数字阅读的资讯
     * @return
     */
    public List<Map> getReadingInfo();

    public List<Map> getCarouselForReading();

}
