package com.zscat.mallplus.manage.service.ums.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.ums.IUmsMemberRegisterParamService;
import com.zscat.mallplus.mbg.ums.entity.UmsMemberRegisterParam;
import com.zscat.mallplus.mbg.ums.mapper.UmsMemberRegisterParamMapper;
import org.springframework.stereotype.Service;

/**
 * 用户注册信息实现类
 */
@Service
public class UmsMemberRegisterParamServiceImpl extends ServiceImpl<UmsMemberRegisterParamMapper,UmsMemberRegisterParam> implements IUmsMemberRegisterParamService {
}
