package com.system.lms.fo.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface NoticeMapper {

    List<Map<String, Object>> selectNoticeByPaging(Map<String, Object> params);

    Integer selectNoticeCount();
}
