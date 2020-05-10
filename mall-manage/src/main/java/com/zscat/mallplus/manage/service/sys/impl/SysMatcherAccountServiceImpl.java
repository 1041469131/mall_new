package com.zscat.mallplus.manage.service.sys.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.sys.ISysMatcherAccountService;
import com.zscat.mallplus.mbg.sys.entity.SysMatcherAccount;
import com.zscat.mallplus.mbg.sys.mapper.SysMatcherAccountMapper;
import org.springframework.stereotype.Service;

@Service
public class SysMatcherAccountServiceImpl extends ServiceImpl<SysMatcherAccountMapper,SysMatcherAccount> implements ISysMatcherAccountService{
}
