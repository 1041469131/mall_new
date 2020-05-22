package com.zscat.mallplus.manage.service.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.sys.entity.SysPermission;
import com.zscat.mallplus.mbg.sys.entity.SysRole;
import com.zscat.mallplus.mbg.sys.entity.SysUser;
import com.zscat.mallplus.mbg.sys.vo.SysUserVO;

import java.util.List;

/**
 * <p>
 * 后台用户表 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-14
 */
public interface ISysUserService extends IService<SysUser> {

    String refreshToken(String token);

    String login(String username, String password);

    int updateUserRole(Long adminId, List<Long> roleIds);

    List<SysRole> getRoleListByUserId(Long adminId);

    int updatePermissionByUserId(Long adminId, List<Long> permissionIds);

    List<SysPermission> getPermissionListByUserId(Long adminId);

    boolean saves(SysUser entity);
    boolean updates(SysUser admin);



    List<SysPermission> listUserPerms(Long id);

    void removePermissRedis(Long id);

    /**
     * 随机获取系统用户
     * @return
     */
    SysUser getRandomSysUser();

    Page<SysUserVO> pageMatcherUsers(SysUserVO sysUser);

    Page<SysUserVO> pageMyInviteMatcherUsers(SysUserVO sysUser);
}
