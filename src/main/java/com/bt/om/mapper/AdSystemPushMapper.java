package com.bt.om.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import com.bt.om.entity.AdSystemPush;

public interface AdSystemPushMapper {

    int insert(AdSystemPush adSystemPush);

    AdSystemPush selectByPrimaryKey(Integer id);

    int getPageCount(Map<String, Object> searchMap);

    List<AdSystemPush> getPageData(Map<String, Object> searchMap, RowBounds rowBounds);
}