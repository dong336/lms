package com.system.lms.fo.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QnaMapper {

    List<Map<String, Object>> selectQnaByPaging(Map<String, Object> params);

    Integer selectQnaCount();

    void insertQna(Map<String, Object> params);
}
