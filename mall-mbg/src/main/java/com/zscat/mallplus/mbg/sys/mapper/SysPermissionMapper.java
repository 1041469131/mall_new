package com.zscat.mallplus.mbg.sys.mapper;

import com.zscat.mallplus.mbg.sys.entity.SysPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 后台用户权限表 Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-04-14
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    List<SysPermission> listMenuByUserId(Long id);

    List<SysPermission> getPermissionList(Long roleId);

    List<SysPermission> listUserPerms(Long id);

    int updateShowStatus(@Param("ids") List<Long> ids, @Param("showStatus") Integer showStatus);
}
