package com.zscat.mallplus.mbg.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.mbg.sys.entity.SysUser;
import com.zscat.mallplus.mbg.sys.vo.SysUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 后台用户表 Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-04-14
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    SysUser getRandomSysUser();

    Page<SysUserVO> pageMatcherUsers(Page<SysUserVO> page, @Param("sysUser") SysUserVO sysUser);

    Page<SysUserVO> pageMyInviteMatcherUsers(Page<SysUserVO> page, @Param("sysUser")SysUserVO sysUser);
}
