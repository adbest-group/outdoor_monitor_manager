package com.bt.om.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bt.om.entity.AdMonitorTask;
import com.bt.om.entity.SysUserExecute;
import com.bt.om.entity.SysUserHistory;
import com.bt.om.entity.vo.SysUserExecuteVo;
import com.bt.om.mapper.SysUserExecuteMapper;
import com.bt.om.mapper.SysUserHistoryMapper;
import com.bt.om.service.ISysUserExecuteService;
import com.bt.om.vo.web.SearchDataVo;

/**
 * APP用户 相关事务层
 */
@Service
public class SysUserExecuteService implements ISysUserExecuteService {
	
    @Autowired
    private SysUserExecuteMapper sysUserExecuteMapper;
    @Autowired
    private SysUserHistoryMapper sysUserHistoryMapper;

    /**
     * 通过用户名查询APP用户信息
     */
    @Override
    public SysUserExecute getByUsername(String username) {
        return sysUserExecuteMapper.selectByUsername(username);
    }

    /**
     * 通过后台用户id和用户类型查询APP用户信息
     */
    @Override
    public List<SysUserExecute> getByConditionMap(Map map) {
        return sysUserExecuteMapper.selectByConditionMap(map);
    }

    /**
     * 分页查询APP用户信息
     */
    @Override
    public void getPageData(SearchDataVo vo) {
        int count = sysUserExecuteMapper.getPageCount(vo.getSearchMap());
        vo.setCount(count);
        if (count > 0) {
            vo.setList(sysUserExecuteMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
        } else {
            vo.setList(new ArrayList<AdMonitorTask>());
        }
    }
    
    /**
     * 通过id查询APP用户信息
     */
    @Override
    public SysUserExecute getById(Integer id) {
        return sysUserExecuteMapper.selectByPrimaryKey(id);
    }
    
    /**
     * 查询媒体监测人员信息
     */
    @Override
    public SysUserExecuteVo selectByIdAndMedia(Integer id) {
        return sysUserExecuteMapper.selectByIdAndMedia(id);
    }

    /**
     * 检查用户名是否重复
     */
    @Override
    public List<SysUserExecute> isExistsName(String username) {
        return sysUserExecuteMapper.isExistsName(username,null);
    }

    /**
     * 检查用户名是否重复
     */
    @Override
    public List<SysUserExecute> isExistsName(String username,Integer id) {
        return sysUserExecuteMapper.isExistsName(username,id);
    }

    /**
     * 新增APP用户
     */
    @Override
    public void add(SysUserExecute userExecute) {
        sysUserExecuteMapper.insertSelective(userExecute);
        SysUserHistory userHistory = new SysUserHistory(); //新增用户信息
        userHistory.setUserId(userExecute.getId());
        userHistory.setUsertypeNew(userExecute.getUsertype());
        userHistory.setOperateIdNew(userExecute.getOperateId());
        userHistory.setCreateTime(new Date());
        userHistory.setUpdateTime(new Date());
        sysUserHistoryMapper.insertSelective(userHistory);
    }

    /**
     * 修改APP用户
     */
    @Override
    public void modify(SysUserExecute userExecute) {
        sysUserExecuteMapper.updateByPrimaryKey(userExecute);
    }

    /**
     * 修改APP用户mac地址信息
     */
	@Override
	public void addMacAddress(String mac) {
		sysUserExecuteMapper.insertMacAddress(mac);
	}

	/**
	 * 查找是否有该邀请码(手机号)
	 */
	@Override
	public SysUserExecute getMobile(String mobile) {
		return sysUserExecuteMapper.getMobile(mobile);
	}

	/**
	 * 查询媒体执行人员
	 */
	@Override
	public List<SysUserExecute> selectMediaNameByUserId(Integer id) {
		return sysUserExecuteMapper.selectMediaNameByUserId(id);
	}

	/**
	 * 更新用户的手机型号和手机系统版本
	 */
	@Override
	@Transactional
	public int updatePhoneModel(SysUserExecute sysUserExecute) {
		return sysUserExecuteMapper.updatePhoneModel(sysUserExecute);
	}

	/**
	 * 更新APP用户并且插入历史记录
	 */
	@Override
	@Transactional
	public void modifyUser(SysUserExecute userExecute, SysUserHistory userHistory) {
		sysUserExecuteMapper.updateByPrimaryKey(userExecute);
		if(userHistory.getUsertypeOld().equals(userHistory.getUsertypeNew())) {
			// 用户类型不变
			if(userHistory.getOperateIdOld() == null && userHistory.getOperateIdNew() == null) {
				// 社会人员 → 社会人员 不做操作
			} else {
				// 某家媒体人员 → 另一家媒体人员
				if(!userHistory.getOperateIdOld().equals(userHistory.getOperateIdNew())) {
					sysUserHistoryMapper.insertSelective(userHistory);
				}
			}
		} else {
			// 用户类型改变
			sysUserHistoryMapper.insertSelective(userHistory);
		}
	}
	
}
