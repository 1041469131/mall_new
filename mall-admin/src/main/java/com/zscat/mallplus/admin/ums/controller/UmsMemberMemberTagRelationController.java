package com.zscat.mallplus.admin.ums.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.manage.service.ums.IUmsMemberMemberTagRelationService;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.mbg.annotation.SysLog;
import com.zscat.mallplus.mbg.ums.entity.UmsMemberMemberTagRelation;
import com.zscat.mallplus.mbg.ums.vo.UmsMemberMemberTagRelationVo;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户和标签关系表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Slf4j
@RestController
@Api(tags = "UmsMemberMemberTagRelationController", description = "用户和标签关系表管理")
@RequestMapping("/ums/UmsMemberMemberTagRelation")
public class UmsMemberMemberTagRelationController {
    @Autowired
    private IUmsMemberMemberTagRelationService IUmsMemberMemberTagRelationService;

    @SysLog(MODULE = "ums", REMARK = "保存用户和标签关系表")
    @ApiOperation("保存用户和标签关系表")
    @PostMapping(value = "/create")
//    @PreAuthorize("hasAuthority('ums:UmsMemberMemberTagRelation:create')")
    @IgnoreAuth
    public Object saveUmsMemberMemberTagRelation(@RequestBody UmsMemberMemberTagRelationVo entity) {
        try {
            if (IUmsMemberMemberTagRelationService.saveTagRelation(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存用户和标签关系表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }
}
