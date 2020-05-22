package com.zscat.mallplus.mbg.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.mbg.sys.entity.SysUserAccount;

public interface SysUserAccountMapper extends BaseMapper<SysUserAccount> {

    int insert(SysUserAccount record);

    int insertSelective(SysUserAccount record);

   }