package com.zscat.mallplus.admin.ums.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.manage.service.ums.IUmsMemberRegisterParamService;
import com.zscat.mallplus.manage.service.ums.IUmsMemberService;
import com.zscat.mallplus.mbg.annotation.SysLog;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import com.zscat.mallplus.mbg.ums.entity.UmsMemberRegisterParam;
import com.zscat.mallplus.mbg.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiang.li create date 2020/6/1 description
 */
@Slf4j
@RestController
@Api(tags = "UmsMemberRegisterParamController", description = "用户注册参数")
@RequestMapping("/ums/UmsMemberRegisterParam")
public class UmsMemberRegisterParamController {

  @Autowired
  private IUmsMemberRegisterParamService umsMemberRegisterParamService;

  @SysLog(MODULE = "ums", REMARK = "查询所有用户注册参数")
  @ApiOperation("查询所有用户注册参数")
  @GetMapping(value = "/list")
  public CommonResult list() {
        return new CommonResult().success(umsMemberRegisterParamService.list( new QueryWrapper<>()));
  }
}
