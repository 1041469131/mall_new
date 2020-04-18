package com.zscat.mallplus.admin.ums.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.manage.assemble.SysUserAssemble;
import com.zscat.mallplus.manage.service.sys.ISysUserService;
import com.zscat.mallplus.manage.service.ums.IUmsApplyMatcherService;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.mbg.annotation.SysLog;
import com.zscat.mallplus.mbg.sys.entity.SysUser;
import com.zscat.mallplus.mbg.ums.vo.UmsApplyMatcherVo;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(tags = "UmsApplyMatcherController", description = "会员申请搭配师管理")
@RequestMapping("/ums/umsApplyMatcher")
public class UmsApplyMatcherController {

    @Autowired
    private IUmsApplyMatcherService iUmsApplyMatcherService;

    @Autowired
    private ISysUserService iSysUserService;

    @ApiOperation(value = "搭配师审核列表")
    @RequestMapping(value = "/pageMatcher", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "ums", REMARK = "搭配师审核列表")
//    @PreAuthorize("hasAuthority('ums:UmsMember:read')")
    public CommonResult<Page<UmsApplyMatcherVo>> pageMatcher(@RequestBody UmsApplyMatcherVo umsApplyMatcherVo){
        Page<UmsApplyMatcherVo> umsApplyMatcherVoPage = iUmsApplyMatcherService.pageMatcher(umsApplyMatcherVo);
        return new CommonResult<>().success(umsApplyMatcherVoPage);
    }

    @ApiOperation(value = "后管审核搭配师")
    @RequestMapping(value = "/auditMatcher", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "ums", REMARK = "后管审核搭配师")
//    @PreAuthorize("hasAuthority('ums:UmsMember:read')")
    public CommonResult auditMatcher(@RequestBody UmsApplyMatcherVo umsApplyMatcherVo){
        Long matcherId = UserUtils.getCurrentMember().getId();
        umsApplyMatcherVo.setAuditId(matcherId);
        if(MagicConstant.AUDIT_STATUS_PASSED.equals(umsApplyMatcherVo.getAuditStatus())){
            if(StringUtils.isEmpty(umsApplyMatcherVo.getAuditReson())){
                umsApplyMatcherVo.setAuditReson("审核通过");
            }
        }
        if(iUmsApplyMatcherService.updateById(umsApplyMatcherVo)){
            if(MagicConstant.AUDIT_STATUS_PASSED.equals(umsApplyMatcherVo.getAuditStatus())){
                SysUser sysUser = SysUserAssemble.assembleSysUser(umsApplyMatcherVo);
                iSysUserService.saves(sysUser);
            }
        }
        return new CommonResult().success();
    }
}
