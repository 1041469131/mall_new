package com.zscat.mallplus.admin.ums.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.admin.utils.GeneratorCodeUtil;
import com.zscat.mallplus.manage.assemble.SysUserAssemble;
import com.zscat.mallplus.manage.service.sms.ISmsService;
import com.zscat.mallplus.manage.service.sys.ISysCertificateService;
import com.zscat.mallplus.manage.service.sys.ISysUserService;
import com.zscat.mallplus.manage.service.ums.IUmsApplyMatcherService;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.mbg.annotation.SysLog;
import com.zscat.mallplus.mbg.sys.entity.SysCertificate;
import com.zscat.mallplus.mbg.sys.entity.SysUser;
import com.zscat.mallplus.mbg.ums.entity.UmsApplyMatcher;
import com.zscat.mallplus.mbg.ums.vo.UmsApplyMatcherVo;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Calendar;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(tags = "UmsApplyMatcherController", description = "会员申请搭配师管理,搭配师审核")
@RequestMapping("/ums/umsApplyMatcher")
public class UmsApplyMatcherController {

    @Autowired
    private IUmsApplyMatcherService iUmsApplyMatcherService;

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private ISmsService smsService;

    @Autowired
    private ISysCertificateService sysCertificateService;

    @ApiOperation(value = "搭配师审核列表")
    @RequestMapping(value = "/pageMatcher", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "ums", REMARK = "搭配师审核列表")
//    @PreAuthorize("hasAuthority('ums:UmsMember:read')")
    public CommonResult<Page<UmsApplyMatcherVo>> pageMatcher(@RequestBody UmsApplyMatcherVo umsApplyMatcherVo){
        Page<UmsApplyMatcherVo> umsApplyMatcherVoPage = iUmsApplyMatcherService.pageMatcher(umsApplyMatcherVo);
        return new CommonResult().success(umsApplyMatcherVoPage);
    }

    @ApiOperation(value = "后管审核搭配师")
    @RequestMapping(value = "/auditMatcher", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "ums", REMARK = "后管审核搭配师")
//    @PreAuthorize("hasAuthority('ums:UmsMember:read')")
    public CommonResult auditMatcher(@RequestBody UmsApplyMatcherVo umsApplyMatcherVo){
        Long matcherId = UserUtils.getCurrentMember().getId();
        umsApplyMatcherVo.setAuditId(matcherId);
        UmsApplyMatcher umsApplyMatcher = iUmsApplyMatcherService.getById(umsApplyMatcherVo.getId());
        if(MagicConstant.AUDIT_STATUS_PASSED.equals(umsApplyMatcherVo.getAuditStatus())){
            if(StringUtils.isEmpty(umsApplyMatcherVo.getAuditReson())){
                umsApplyMatcherVo.setAuditReson("审核通过");
                umsApplyMatcherVo.setRelateStatus("1");
            }
        }
        if(iUmsApplyMatcherService.updateById(umsApplyMatcherVo)){
            if(MagicConstant.AUDIT_STATUS_PASSED.equals(umsApplyMatcherVo.getAuditStatus())){
                BeanUtils.copyProperties(umsApplyMatcher,umsApplyMatcherVo );
                //设置随机用户名通知
                String userName = generatorUserName();
                smsService.sendUserName(umsApplyMatcherVo.getPhone(),userName);
                SysUser sysUser = SysUserAssemble.assembleSysUser(umsApplyMatcherVo,userName);
                iSysUserService.saves(sysUser);
                //设置证书
                SysCertificate sysCertificate=new SysCertificate();
                sysCertificate.setCardNumber(userName);
                sysCertificate.setName(sysUser.getName());
                sysCertificate.setPhone(sysUser.getPhone());
                sysCertificate.setStatus(1);
                sysCertificate.setSysUserId(sysUser.getId());
                sysCertificate.setType(1);
                sysCertificate.setCreateDate(new Date());
                Calendar calendar= Calendar.getInstance();
                calendar.add(Calendar.YEAR,1);
                sysCertificate.setExpiryDate(calendar.getTime());
                sysCertificateService.save(sysCertificate);
            }
        }
        return new CommonResult().success();
    }


    private String generatorUserName(){
        while(true){
            String username = GeneratorCodeUtil.generatorString(8);
            SysUser querySysUser = iSysUserService.getOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getUsername, username));
            if(querySysUser==null){
                return username;
            }
        }
    }
}
