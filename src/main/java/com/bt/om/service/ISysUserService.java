package com.bt.om.service;

import java.util.List;
import java.util.Map;

import com.bt.om.entity.SysUser;
import com.bt.om.entity.SysUserDetail;
import com.bt.om.entity.SysUserRes;
import com.bt.om.entity.SysUserRole;
import com.bt.om.entity.vo.SysUserVo;
import com.bt.om.entity.vo.UserRoleVo;
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

	int updateUserRess(List<SysUserRes> sysUserRess, SysUserRes sysUserRes, UserRoleVo userRoleVo, UserRoleVo userRoleVo2);
	
	List<Integer> getCustomerIdsByAdminId(Integer userId);

	int deleteUserRess(SysUserRes sysUserRes, UserRoleVo userRoleVo);

	int insertUserRess(List<SysUserRes> sysUserRess, SysUserRes sysUserRes);

	int deleteCustomerRess(SysUserRes sysUserRes);

	List<SysUser> getIdNameByUserType(Integer userType);

	List<Integer> selectUserIdsByResIds(Map<String, Object> searchMap);

	int updateListUserRes(UserRoleVo userRoleVo);

	SysUserDetail getUserDetail(Integer userId);

	List<SysUser> getAvailableByUserType(Integer userType);

	int updateByPrimaryKeySelective(SysUser u);

	List<Integer> getUserId(int i);

	SysUser getUserNameById(Integer customerId);

	SysUser getUserAppType(Integer userId);

	void changeAppType(Integer id);

	void modify(SysUser sysUser);

	void addUser(SysUser sysUser, Integer roleId);

}
