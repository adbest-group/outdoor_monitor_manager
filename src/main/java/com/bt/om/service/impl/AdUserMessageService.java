package com.bt.om.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bt.om.entity.AdMedia;
import com.bt.om.entity.AdUserMessage;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserExecute;
import com.bt.om.enums.AppUserTypeEnum;
import com.bt.om.exception.web.ExcelException;
import com.bt.om.mapper.AdMediaMapper;
import com.bt.om.mapper.AdUserMessageMapper;
import com.bt.om.mapper.SysUserExecuteMapper;
import com.bt.om.mapper.SysUserMapper;
import com.bt.om.service.IAdUserMessageService;
import com.bt.om.vo.web.SearchDataVo;

/**
 * 站内信相关事务层
 */
@Service
public class AdUserMessageService implements IAdUserMessageService{
	private static final String MOBILE_NULL = "手机号为空";
	private static final String MOBILE_PROBLEM = "手机号有误";
	private static final String MOBILE_REPEAT = "手机号已存在";
	
	private static final String NAME_NULL = "真实姓名为空";
	private static final String NAME_PROBLEM = "真实姓名有误";
	
	private static final String IMPORT_SUCC = "导入成功";
	private static final String IMPORT_FAIL = "导入失败";
	
	private static final String REGREX_MOBILE = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$";
	private static final String REGREX_NAME = "^[\u4E00-\u9FA5\uf900-\ufa2d·s]{2,20}$";
	
	@Autowired
	private AdUserMessageMapper adUserMessageMapper;
	
	@Autowired
	private AdMediaMapper adMediaMapper;
	
	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Autowired
	private SysUserExecuteMapper sysUserExecuteMapper;
	
	/**
	 * 分页查询用户站内信信息
	 */
	@Override
	public void getPageData(SearchDataVo vo) {
		int count = adUserMessageMapper.getPageCount(vo.getSearchMap());
        vo.setCount(count);
        if (count > 0) {
            vo.setList(adUserMessageMapper.getPageData(vo.getSearchMap(), new RowBounds(vo.getStart(), vo.getSize())));
        } else {
            vo.setList(new ArrayList<AdUserMessage>());
        }
	}

	/**
	 * 插入一条站内信
	 */
	@Override
	public void insertMessage(List<AdUserMessage> message) {
		adUserMessageMapper.insertMessage(message);
	}
	
	/**
	 * 批量导入媒体监测人员
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertBatchByExcel(List<List<Object>> listob, Integer operateId, Integer usertype,String password) {
		Date now = new Date();
		//待导入数据
		List<SysUserExecute> sysUserExecutes = new ArrayList<>();
		
		//[1] 获取媒体后台账号信息
		if(usertype == AppUserTypeEnum.MEDIA.getId()) {
			AdMedia adMedia = adMediaMapper.selectByPrimaryKey(operateId);
			SysUser sysUser = sysUserMapper.selectByPrimaryKey(adMedia.getUserId());
			operateId = sysUser.getId();
		}else if(usertype == AppUserTypeEnum.SOCIAL.getId()) {
			operateId = null;
		}
		
		//[2] 查询当前所有APP人员的手机号(唯一性)
		List<String> allMobile = sysUserExecuteMapper.getAllMobile();
		Set<String> allMobileSet = new HashSet<>(allMobile);
		
		//[3] 导入校验
		for (int i = 1; i < listob.size(); i++) {
            List<Object> lo = listob.get(i);
            if(lo.size() <= 4){
            	SysUserExecute sysUserExecute = new SysUserExecute();
            	sysUserExecute.setOperateId(operateId); //所属的媒体账号id/所属的广告主id（sys_user的id）
            	sysUserExecute.setUsertype(usertype);
            	sysUserExecute.setStatus(1); //1：可用
            	sysUserExecute.setCreateTime(now);
            	sysUserExecute.setUpdateTime(now);
            	
            	Boolean hasProblem = false;
            	
            	//获取手机号
            	if(lo.get(0) == null) {
            		lo.set(2, IMPORT_FAIL);
            		lo.set(3, MOBILE_NULL);
            		hasProblem = true;
            	} else {
            		String mobile = (String) lo.get(0);
            		//手机号唯一性校验
            		if(allMobileSet.contains(mobile)) {
            			lo.set(2, IMPORT_FAIL);
                		lo.set(3, MOBILE_REPEAT);
                		hasProblem = true;
            		} else {
            			allMobileSet.add(mobile);
            		}
            		
            		//手机号正则校验
            		if(hasProblem == false) {
            			if(Pattern.matches(REGREX_MOBILE, mobile)) {
                			sysUserExecute.setUsername(mobile);
                			sysUserExecute.setMobile(mobile);
                			sysUserExecute.setPassword(new Md5Hash(password, mobile).toString());
                		} else {
                			lo.set(2, IMPORT_FAIL);
                    		lo.set(3, MOBILE_PROBLEM);
                    		hasProblem = true;
                		}
            		}
            	}
            	
            	//获取真实姓名
            	if(hasProblem == false) {
            		if(lo.get(1) == null) {
                		lo.set(2, IMPORT_FAIL);
                		lo.set(3, NAME_NULL);
                		hasProblem = true;
                	} else {
                		//姓名正则校验
                		String realname = (String) lo.get(1);
                		if(Pattern.matches(REGREX_NAME, realname)) {
                			sysUserExecute.setRealname(realname);
                		} else {
                			lo.set(2, IMPORT_FAIL);
                    		lo.set(3, NAME_PROBLEM);
                    		hasProblem = true;
                		}
                	}
            	}
            	
            	if(hasProblem == false) {
            		//导入成功
            		lo.set(2, IMPORT_SUCC);
            		sysUserExecutes.add(sysUserExecute);
            	}
            } else {
                throw new ExcelException("批量导入文件有误, 导入失败");
            }
		}
		
		//[4] 批量导入
		if(sysUserExecutes != null && sysUserExecutes.size() > 0) {
			sysUserExecuteMapper.insertBatch(sysUserExecutes);
		}
	}

}
