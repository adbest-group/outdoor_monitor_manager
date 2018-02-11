package com.bt.om.security;

import com.bt.om.entity.SysMenu;
import com.bt.om.entity.SysRole;
import com.bt.om.entity.SysUser;
import com.bt.om.entity.vo.SysMenuVo;
import com.bt.om.entity.vo.SysUserVo;
import com.bt.om.enums.SessionKey;
import com.bt.om.service.ISysMenuService;
import com.bt.om.service.ISysRoleService;
import com.bt.om.service.ISysUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * shiro权限服务
 *
 * @author jade
 */
@Service("shiroService")
public class ShiroService extends AuthorizingRealm {

    // private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysMenuService sysMenuService;

    @Autowired
    ISysRoleService sysRoleService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        SimpleAuthorizationInfo authorizationInfo = (SimpleAuthorizationInfo) ShiroUtils.getSessionAttribute(SessionKey.SESSION_AUTHORIZATION_INFO.toString());

        if (authorizationInfo == null) {
            authorizationInfo = new SimpleAuthorizationInfo();

            SysUser user = (SysUser) ShiroUtils.getSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString());
            String username = user.getUsername();

            //角色列表
            List<SysRole> roles = sysRoleService.findRoleByUserId(user.getId());
            if (roles != null && roles.size() > 0) {
                for (SysRole role : roles) {
                    authorizationInfo.addRole(role.getRoleName());
                }
            }


            // #这里需要重构
            List<SysMenuVo> resultList = sysMenuService.findMenuListByUsername(username);
            if (resultList != null && resultList.size() > 0) {
                for (SysMenuVo sysMenu : resultList) {
                    authorizationInfo.addStringPermission(sysMenu.getUrl());
                }
            }
            ShiroUtils.setSessionAttribute(SessionKey.SESSION_AUTHORIZATION_INFO.toString(),authorizationInfo);
        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {

        UsernamePasswordToken token = (UsernamePasswordToken) arg0;
        SysUserVo userObj = null;
        try {
            userObj = sysUserService.findByUsername(token.getUsername());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (userObj != null) {
            // 用户信息保存到session
            ShiroUtils.setSessionAttribute(SessionKey.SESSION_LOGIN_USER.toString(), userObj);

            if (userObj.getMenuList() != null && userObj.getMenuList().size() > 0) {
                List<SysMenuVo> menuList = new ArrayList<SysMenuVo>();
                for (SysMenuVo menu : userObj.getMenuList()) {
                    if (menu.getParentId() == 0) {
                        menuList.add(menu);
                        for (SysMenuVo m : userObj.getMenuList()) {
                            if (m.getParentId().equals(menu.getId())) {
                                menu.add(m);
                            }
                        }
                    }
                }

                ShiroUtils.setSessionAttribute(SessionKey.SESSION_USER_MENU.toString(), menuList);
            } else {
                ShiroUtils.setSessionAttribute(SessionKey.SESSION_USER_MENU.toString(), new ArrayList<SysMenu>());
            }

            // 获取用户显示的菜单
            return new SimpleAuthenticationInfo(userObj.getUsername(), userObj.getPassword(), getName());
        }

        return null;
    }
}
