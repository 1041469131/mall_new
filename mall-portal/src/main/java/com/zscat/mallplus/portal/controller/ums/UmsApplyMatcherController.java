package com.zscat.mallplus.portal.controller.ums;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.manage.service.ums.IUmsApplyMatcherService;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.mbg.ums.entity.UmsApplyMatcher;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@Api(tags = "UmsApplyMatherController", description = "会员申请搭配师")
@RequestMapping("/api/umsApplyMatcher")
public class UmsApplyMatcherController {

    @Autowired
    private IUmsApplyMatcherService iUmsApplyMatcherService;

    @IgnoreAuth
    @ApiOperation("保存或修改信息")
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody UmsApplyMatcher umsApplyMatcher) {
        UmsMember umsMember = UserUtils.getCurrentUmsMember();
        Long memberId = null;
        if(umsMember != null){
            memberId = UserUtils.getCurrentUmsMember().getId();
        }
        if(StringUtils.isEmpty(umsApplyMatcher.getPhone())){
            return new CommonResult().failed("手机号不能为空");
        }
        UmsApplyMatcher oldUmsApplyMatcher = iUmsApplyMatcherService.getOne(new QueryWrapper<UmsApplyMatcher>().eq("phone",umsApplyMatcher.getPhone()).in("audit_status", new Object[]{"0","1"}));
        if(oldUmsApplyMatcher == null){
            umsApplyMatcher.setRelateStatus("0");
            umsApplyMatcher.setMemberId(memberId);
            umsApplyMatcher.setCreateDate(new Date());
            umsApplyMatcher.setCreateTime(new Date().getTime());
            umsApplyMatcher.setUpdateDate(new Date());
            umsApplyMatcher.setUpdateTime(new Date().getTime());
            umsApplyMatcher.setAuditStatus(MagicConstant.AUDIT_STATUS_WAITING);
        }else{
            umsApplyMatcher.setUpdateTime(new Date().getTime());
            umsApplyMatcher.setUpdateDate(new Date());
            umsApplyMatcher.setId(oldUmsApplyMatcher.getId());
        }
        iUmsApplyMatcherService.saveOrUpdate(umsApplyMatcher);
        return new CommonResult().success();
    }

    @IgnoreAuth
    @ApiOperation("查询会员申请搭配师")
    @RequestMapping(value = "/queryUmsApplyMatcher", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult queryUmsApplyMatcher(String phone) {
        UmsApplyMatcher umsApplyMatcher = null;
        if(StringUtils.isEmpty(phone)){
            Long memberId = UserUtils.getCurrentUmsMember().getId();
            umsApplyMatcher = iUmsApplyMatcherService.getOne(new QueryWrapper<UmsApplyMatcher>().eq("member_id",memberId).orderByDesc("update_date"));
        }else{
            umsApplyMatcher = iUmsApplyMatcherService.getOne(new QueryWrapper<UmsApplyMatcher>().eq("phone",phone).orderByDesc("update_date"));
        }
        return new CommonResult().success(umsApplyMatcher);
    }

}
