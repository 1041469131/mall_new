package com.zscat.mallplus.admin.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.mbg.annotation.SysLog;
import com.zscat.mallplus.mbg.sys.entity.SysRole;
import com.zscat.mallplus.mbg.sys.entity.SysRolePermission;
import com.zscat.mallplus.mbg.sys.entity.SysUserRole;
import com.zscat.mallplus.sys.service.ISysRolePermissionService;
import com.zscat.mallplus.sys.service.ISysRoleService;
import com.zscat.mallplus.sys.service.ISysUserRoleService;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 后台角色表 前端控制器
 * </p>
 *
 * @author zscat
 * @since 2019-04-14
 */
@Slf4j
@Api(value = "角色管理", description = "", tags = {"角色管理"})
@RestController
@RequestMapping("/sys/sysRole")
public class SysRoleController extends ApiController {

    @Resource
    private ISysRoleService sysRoleService;

    @Resource
    private ISysUserRoleService sysUserRoleService;

    @Resource
    private ISysRolePermissionService sysRolePermissionService;

    @SysLog(MODULE = "sys", REMARK = "根据条件查询所有角色列表")
    @ApiOperation("根据条件查询所有角色列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('sys:role:read')")
    public Object getRoleByPage(SysRole entity,
                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize
    ) {
        try {
            if(StringUtils.isEmpty(entity.getName())){
                entity.setName(null);
            }
            return new CommonResult().success(sysRoleService.page(new Page<SysRole>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有角色列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "保存角色")
    @ApiOperation("保存角色")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('sys:role:create')")
    public Object saveRole(@RequestBody SysRole entity) {
        try {
            if (sysRoleService.saves(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存角色：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "更新角色")
    @ApiOperation("更新角色")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('sys:role:update')")
    public Object updateRole(@RequestBody SysRole entity) {
        try {
            if (sysRoleService.updates(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新角色：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "删除角色")
    @ApiOperation("删除角色")
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public Object deleteRole(@ApiParam("角色id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("角色id");
            }
            List<SysUserRole> sysUserRoles = sysUserRoleService.list(new QueryWrapper<SysUserRole>().eq("role_id",id));
            if(!CollectionUtils.isEmpty(sysUserRoles)){
                return new CommonResult().failed("该角色下有相应的用户，不能删除该角色");
            }
            if (sysRoleService.removeById(id)) {
                sysRolePermissionService.remove(new QueryWrapper<SysRolePermission>().eq("role_id",id));
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除角色：%s", e.getMessage(), e);
            return new CommonResult().failed(e.getMessage() );
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "sys", REMARK = "给角色分配角色")
    @ApiOperation("查询角色明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('sys:role:read')")
    public Object getRoleById(@ApiParam("角色id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("角色id");
            }
            SysRole coupon = sysRoleService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询角色明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除角色")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量删除角色")
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public Object deleteBatch(@RequestParam("ids") List<Long> ids) {
        boolean count = sysRoleService.removeByIds(ids);
        List<SysUserRole> sysUserRoles = sysUserRoleService.list(new QueryWrapper<SysUserRole>().in("role_id",ids));
        if(!CollectionUtils.isEmpty(sysUserRoles)){
            return new CommonResult().failed("批量中有些角色有相应的用户，不能删除该角色");
        }
        if (count) {
            sysRolePermissionService.remove(new QueryWrapper<SysRolePermission>().in("role_id",ids));
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }
    @SysLog(MODULE = "sys", REMARK = "获取相应角色权限")
    @ApiOperation("获取相应角色权限")
    @RequestMapping(value = "/permission/{roleId}", method = RequestMethod.GET)
    @ResponseBody
    public Object getPermissionList(@PathVariable Long roleId) {
        List<SysRolePermission> permissionList = sysRoleService.getRolePermission(roleId);
        return new CommonResult().success(permissionList);
    }
    @SysLog(MODULE = "sys", REMARK = "获取相应角色权限-单表")
    @ApiOperation("获取相应角色权限-单表")
    @RequestMapping(value = "/rolePermission/{roleId}", method = RequestMethod.GET)
    @ResponseBody
    public Object rolePermission(@PathVariable Long  roleId) {
        List<SysRolePermission> rolePermission = sysRoleService.getRolePermission(roleId);
        return new CommonResult().success(rolePermission);
    }


    @SysLog(MODULE = "sys", REMARK = "更新角色禁用状态")
    @ApiOperation("更新角色禁用状态")
    @RequestMapping(value = "/update/showStatus")
    @PreAuthorize("hasAuthority('sys:role:update')")
    public Object updateShowStatus(@RequestParam("ids") List<Long> ids, @RequestParam("showStatus") Integer showStatus) {
        int count = sysRoleService.updateShowStatus(ids,showStatus);
        if(count > 0){
            return new CommonResult().success();
        }
        return new CommonResult().failed();
    }
}

