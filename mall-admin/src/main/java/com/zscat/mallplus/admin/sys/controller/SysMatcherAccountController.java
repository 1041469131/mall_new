package com.zscat.mallplus.admin.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.manage.service.sys.ISysMatcherAccountService;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.mbg.sys.entity.SysMatcherAccount;
import com.zscat.mallplus.mbg.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "SysMatcherAccountController", description = "搭配师业绩结算管理（查询会员的账户管理）")
@RequestMapping("/sys/matcherAccount")
public class SysMatcherAccountController {

    @Autowired
    private ISysMatcherAccountService iSysMatcherAccountService;

    @IgnoreAuth
    @ApiOperation("查询会员的账户信息")
    @RequestMapping(value = "/listMatcherAccounts", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult listMatcherAccounts(Long matcherId) {
        List<SysMatcherAccount> sysMatcherAccounts = iSysMatcherAccountService.list(new QueryWrapper<SysMatcherAccount>().eq("matcher_id",matcherId));
        return new CommonResult().success(sysMatcherAccounts);
    }

}
