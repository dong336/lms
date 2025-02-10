package com.system.lms.fo.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FaqMapper {

    List<Map<String, Object>> selectFaqByPaging(Map<String, Object> params);

    Long selectFaqCount();
}
