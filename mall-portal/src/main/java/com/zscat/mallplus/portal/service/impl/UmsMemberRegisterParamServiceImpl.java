package com.zscat.mallplus.portal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.mbg.ums.entity.UmsMemberRegisterParam;
import com.zscat.mallplus.mbg.ums.mapper.UmsMemberRegisterParamMapper;
import com.zscat.mallplus.portal.service.IUmsMemberRegisterParamService;
import org.springframework.stereotype.Service;

/**
 * 用户注册信息实现类
 */
@Service
public class UmsMemberRegisterParamServiceImpl extends ServiceImpl<UmsMemberRegisterParamMapper,UmsMemberRegisterParam> implements IUmsMemberRegisterParamService{
}
