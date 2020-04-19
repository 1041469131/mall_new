package com.zscat.mallplus.manage.service.sys.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.sys.ISysDictService;
import com.zscat.mallplus.mbg.sys.entity.SysDict;
import com.zscat.mallplus.mbg.sys.mapper.SysDictMapper;
import org.springframework.stereotype.Service;

@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper,SysDict> implements ISysDictService{
}
