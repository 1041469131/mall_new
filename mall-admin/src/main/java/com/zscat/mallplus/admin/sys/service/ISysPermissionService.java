package com.zscat.mallplus.admin.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.admin.bo.Tree;
import com.zscat.mallplus.mbg.sys.entity.SysPermission;
import com.zscat.mallplus.mbg.sys.entity.SysPermissionNode;

import java.util.List;

/**
 * <p>
 * 后台用户权限表 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-14
 */
public interface ISysPermissionService extends IService<SysPermission> {

    List<Tree<SysPermission>> getPermissionsByUserId(Long id);

    List<SysPermissionNode> treeList();

    int updateShowStatus(List<Long> ids, Integer showStatus);
}
