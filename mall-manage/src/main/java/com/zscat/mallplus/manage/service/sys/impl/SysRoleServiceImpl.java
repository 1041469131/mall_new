package com.zscat.mallplus.manage.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.sys.ISysRolePermissionService;
import com.zscat.mallplus.manage.service.sys.ISysRoleService;
import com.zscat.mallplus.manage.service.sys.ISysUserService;
import com.zscat.mallplus.mbg.sys.entity.SysPermission;
import com.zscat.mallplus.mbg.sys.entity.SysRole;
import com.zscat.mallplus.mbg.sys.entity.SysRolePermission;
import com.zscat.mallplus.mbg.sys.mapper.SysPermissionMapper;
import com.zscat.mallplus.mbg.sys.mapper.SysRoleMapper;
import com.zscat.mallplus.mbg.sys.mapper.SysRolePermissionMapper;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 后台用户角色表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-14
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Resource
    private SysPermissionMapper permissionMapper;
    @Resource
    private SysRolePermissionMapper rolePermissionMapper;
    @Resource
    private SysRoleMapper roleMapper;
    @Resource
    private SysRolePermissionMapper rolePermissionRelationMapper;
    @Resource
    private ISysRolePermissionService rolePermissionRelationDao;
    @Resource
    private ISysUserService userService;
    @Override
    public List<SysPermission> getPermissionList(Long roleId) {
        return permissionMapper.getPermissionList(roleId);
    }
    @Override
    public List<SysRolePermission> getRolePermission(Long roleId) {
        return rolePermissionMapper.selectList(new QueryWrapper<SysRolePermission>().eq("role_id",roleId));
    }
    @Override
    public boolean saves(SysRole role) {
        role.setCreateTime(new Date());
        role.setStatus(MagicConstant.ROLE_STATUS_ON);
        role.setAdminCount(0);
        role.setSort(0);
        roleMapper.insert(role);
        updatePermission(role.getId(),role.getMenuIds());
        return true;
    }

    @Override
    public boolean updates( SysRole role) {
        role.setId(role.getId());
        updatePermission(role.getId(),role.getMenuIds());
        roleMapper.updateById(role);
        return true;
    }

    public void updatePermission(Long roleId, String permissionIds) {

        //先删除原有关系
        rolePermissionRelationMapper.delete(new QueryWrapper<SysRolePermission>().eq("role_id",roleId));
        //批量插入新关系
        List<SysRolePermission> relationList = new ArrayList<>();
        if (!StringUtils.isEmpty(permissionIds)){
            String[] mids = permissionIds.split(",");
            for (String permissionId : mids) {
                SysRolePermission relation = new SysRolePermission();
                relation.setRoleId(roleId);
                relation.setPermissionId(Long.valueOf(permissionId));
                relationList.add(relation);
            }
            rolePermissionRelationDao.saveBatch(relationList);
        }
    }

    @Override
    public int updateShowStatus(List<Long> ids, Integer showStatus) {
        return roleMapper.updateStatus(showStatus,ids);
    }
}
