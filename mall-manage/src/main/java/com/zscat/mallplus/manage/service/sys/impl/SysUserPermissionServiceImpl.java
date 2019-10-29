package com.zscat.mallplus.manage.service.sys.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.sys.ISysUserPermissionService;
import com.zscat.mallplus.mbg.sys.entity.SysUserPermission;
import com.zscat.mallplus.mbg.sys.mapper.SysUserPermissionMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 后台用户和权限关系表(除角色中定义的权限以外的加减权限) 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-14
 */
@Service
public class SysUserPermissionServiceImpl extends ServiceImpl<SysUserPermissionMapper, SysUserPermission> implements ISysUserPermissionService {

}
