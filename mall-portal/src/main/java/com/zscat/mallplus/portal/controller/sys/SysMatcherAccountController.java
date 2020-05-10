package com.zscat.mallplus.portal.controller.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.manage.service.sys.ISysMatcherAccountService;
import com.zscat.mallplus.manage.service.sys.ISysUserService;
import com.zscat.mallplus.manage.service.ums.IUmsApplyMatcherService;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.mbg.marking.entity.SmsHomeAdvertise;
import com.zscat.mallplus.mbg.sys.entity.SysMatcherAccount;
import com.zscat.mallplus.mbg.sys.entity.SysUser;
import com.zscat.mallplus.mbg.ums.entity.UmsApplyMatcher;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "SysMatcherAccountController", description = "搭配师账户信息管理")
@RequestMapping("/api/matcherAccount")
public class SysMatcherAccountController {

    @Autowired
    private ISysMatcherAccountService iSysMatcherAccountService;

    @Autowired
    private IUmsApplyMatcherService iUmsApplyMatcherService;

    @Autowired
    private ISysUserService iSysUserService;

    @IgnoreAuth
    @ApiOperation("查询会员的账户信息")
    @RequestMapping(value = "/listMatcherAccounts", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult listMatcherAccounts() {
        Long memberId = UserUtils.getCurrentUmsMember().getId();
        List<SysMatcherAccount> sysMatcherAccounts = iSysMatcherAccountService.list(new QueryWrapper<SysMatcherAccount>().eq("member_id",memberId));
        return new CommonResult().success(sysMatcherAccounts);
    }

    @IgnoreAuth
    @ApiOperation("保存或者修改账户信息")
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult saveOrUpdate(@RequestBody SysMatcherAccount sysMatcherAccount) {
        Long memberId = UserUtils.getCurrentUmsMember().getId();
        UmsApplyMatcher umsApplyMatcher = iUmsApplyMatcherService.getOne(new QueryWrapper<UmsApplyMatcher>().eq("member_id",memberId).eq("audit_status","1"));
        if(umsApplyMatcher == null){
            return new CommonResult().failed("该用户目前还不是搭配师");
        }
        SysUser sysUser = iSysUserService.getOne(new QueryWrapper<SysUser>().eq("phone",umsApplyMatcher.getPhone()));
        if(sysUser == null){
            return new CommonResult().failed("目前该用户还不是搭配师");
        }
        sysMatcherAccount.setMatcherId(sysUser.getId());
        sysMatcherAccount.setMemberId(memberId);
        if(sysMatcherAccount.getId() == null){
            sysMatcherAccount.setCreateDate(new Date());
            sysMatcherAccount.setCreateTime(new Date().getTime());
        }
        sysMatcherAccount.setUpdateDate(new Date());
        sysMatcherAccount.setUpdateTime(new Date().getTime());
        iSysMatcherAccountService.saveOrUpdate(sysMatcherAccount);
        return new CommonResult().success();
    }

    @IgnoreAuth
    @ApiOperation("删除账户信息")
    @RequestMapping(value = "/deleteAccount", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteAccount(Long accountId) {
        if(iSysMatcherAccountService.removeById(accountId)){
            return new CommonResult().success("删除成功");
        }else{
            return new CommonResult().failed("删除失败");
        }
    }
}
