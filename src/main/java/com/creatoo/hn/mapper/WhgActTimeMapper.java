package com.creatoo.hn.mapper;

import com.creatoo.hn.model.WhgActTime;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface WhgActTimeMapper extends Mapper<WhgActTime> {

    Integer getActTicketChecked(@Param("orderId") String orderId);

}