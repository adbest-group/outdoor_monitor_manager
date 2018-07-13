package com.bt.om.service;

import com.bt.om.entity.OperateLog;
import com.bt.om.vo.web.SearchDataVo;

/**
 * Created by jiayong.mao on 2018/5/24.
 */
public interface IOperateLogService {
	public void getPageData(SearchDataVo vo);
    public OperateLog getById(Integer id);
    public void save(OperateLog operateLog);
    public void modify(OperateLog operateLog);
}
