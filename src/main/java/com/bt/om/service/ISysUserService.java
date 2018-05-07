package com.bt.om.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserDetail;
import com.bt.om.entity.SysUserRole;
import com.bt.om.entity.vo.SysUserVo;
import com.bt.om.vo.web.SearchDataVo;

/**
 * Created by caiting on 2018/1/17.
 */
public interface ISysUserService {

    /**
     * 根据ID获取用户信息
     *
     * @param id
     * @return
     */
    SysUserVo findUserinfoById(Integer id);

    /**
     * 根据登录账户获取用户信息
     *
     * @param username
     * @return
     */
    SysUserVo findByUsername(String username);

    /**
     * 获取用户列表记录数
     *
     * @param searchMap
     * @return
     */
    int getPageCount(Map<String, Object> searchMap);

    /**
     * 获取用户列表信息
     *
     * @param vo
     * @return
     */
    List<SysUserVo> getPageData(SearchDataVo vo);

    /**
     * 登录账号唯一性验证
     *
     * @param username
     * @return
     */
    List<SysUserVo> isExistsName(String username);

    int update(SysUserVo user);

    //传参数id用于修改时，判断排除当前id的prefix
    boolean isExistsPrefix(String prefix,Integer id);

//	/**
//	 * 保存用户信息
//	 *
//	 * @param SysUser
//	 * @param roleIds
//	 */
//	void saveUser(SysUser SysUser, String roleIds);
//
//	/**
//	 * 删除用户信息
//	 *
//	 * @param id
//	 * @return
//	 */
//	int delUserById(Integer id);
//
//	/**
//	 * 更新登录账号状态
//	 *
//	 * @param id
//	 * @param status
//	 * @return
//	 */
//	int updateUserStatus(Integer id, Integer status);
//
//	/**
//	 * 新增用户
//	 *
//	 * @param ottvUser
//	 */
	void addUser(SysUser sysUser);
	
	List<SysUserVo> getAllByUserType(Integer userType);
//
//	/**
//	 * 查询所有的销售
//	 *
//	 * @return
//	 */
//	List<SysUser> findAllSale();
    int createDepartmentLeader(SysUser record, SysUserDetail sysUserDetail, SysUserRole sysUserRole);
	List<SysUser>findLeaderList();
	int updatePasswordAndName(SysUser record);
	int deleteUserById(Integer id);

	int updateStatus(SysUser status);
	List<SysUser>findAllTask();
	int addUsers(SysUser record);
}
