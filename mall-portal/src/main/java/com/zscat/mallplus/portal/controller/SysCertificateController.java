package com.zscat.mallplus.portal.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.manage.service.sys.ISysCertificateService;
import com.zscat.mallplus.mbg.annotation.SysLog;
import com.zscat.mallplus.mbg.sys.entity.SysCertificate;
import com.zscat.mallplus.mbg.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiang.li create date 2020/6/22 description
 */
@Slf4j
@Api(description = "证书管理", tags = "SysCertificateController")
@RestController
@RequestMapping("/api/sysCertificate")
public class SysCertificateController {

  @Autowired
  private ISysCertificateService sysCertificateService;

  @SysLog(MODULE = "sys", REMARK = "根据条件查询所有用户证书列表")
  @ApiOperation("根据条件查询所有用户证书列表")
  @GetMapping(value = "/list/{matcherUserId}/{type}")
  public CommonResult<List<SysCertificate>> list(@ApiParam("搭配师id")@PathVariable String matcherUserId,@ApiParam("类型 1是搭配师证书") @PathVariable Integer type) {
    List<SysCertificate> sysCertificates = sysCertificateService
      .list(new QueryWrapper<SysCertificate>().lambda().eq(SysCertificate::getStatus, 1).eq(SysCertificate::getSysUserId, matcherUserId)
        .eq(SysCertificate::getType, type));
    return new CommonResult().success(sysCertificates);
  }

}
