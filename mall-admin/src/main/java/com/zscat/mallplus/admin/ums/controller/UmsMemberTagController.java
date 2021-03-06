package com.zscat.mallplus.admin.ums.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.manage.service.ums.IUmsMemberMemberTagRelationService;
import com.zscat.mallplus.manage.service.ums.IUmsMemberTagService;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.mbg.annotation.SysLog;
import com.zscat.mallplus.mbg.ums.entity.UmsMemberMemberTagRelation;
import com.zscat.mallplus.mbg.ums.entity.UmsMemberTag;
import com.zscat.mallplus.mbg.ums.vo.UmsMemberTagVo;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户标签表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Slf4j
@RestController
@Api(tags = "UmsMemberTagController", description = "用户标签表管理")
@RequestMapping("/ums/UmsMemberTag")
public class UmsMemberTagController {

  @Autowired
  private IUmsMemberTagService IUmsMemberTagService;
  @Autowired
  private IUmsMemberMemberTagRelationService umsMemberMemberTagRelationService;

  @SysLog(MODULE = "ums", REMARK = "根据条件查询所有用户标签表列表")
  @ApiOperation("根据条件查询所有用户标签表列表")
  @GetMapping(value = "/list")
//    @PreAuthorize("hasAuthority('ums:UmsMemberTag:read')")
  public Object getUmsMemberTagByPage() {
    try {
      return new CommonResult().success(IUmsMemberTagService.listUmsMemberTags(UserUtils.getCurrentMember().getId()));
    } catch (Exception e) {
      log.error("根据条件查询所有用户标签表列表：%s", e.getMessage(), e);
    }
    return new CommonResult().failed();
  }


  @SysLog(MODULE = "ums", REMARK = "查询搭配师下标签粉丝数量列表")
  @ApiOperation("查询搭配师下标签粉丝数量列表")
  @GetMapping(value = "/listTagsCountByMatchUserId")
  public CommonResult<List<UmsMemberTagVo>> listTagsCountByMatchUserId() {
    return new CommonResult().success(IUmsMemberTagService.listTagsCountByMatchUserId(UserUtils.getCurrentMember().getId()));
  }


  @SysLog(MODULE = "ums", REMARK = "保存用户标签表")
  @ApiOperation("保存用户标签表")
  @PostMapping(value = "/create")
//    @PreAuthorize("hasAuthority('ums:UmsMemberTag:create')")
  public Object saveUmsMemberTag(@RequestBody UmsMemberTag entity) {
    try {
      if (entity.getGenType() == null) {
        entity.setGenType(2);
      }
      entity.setCreateTime(new Date());
      entity.setMatchUserId(UserUtils.getCurrentMember().getId());
      if (IUmsMemberTagService.save(entity)) {
        return new CommonResult().success();
      }
    } catch (Exception e) {
      log.error("保存用户标签表：%s", e.getMessage(), e);
      return new CommonResult().failed();
    }
    return new CommonResult().failed();
  }

  @SysLog(MODULE = "ums", REMARK = "更新用户标签表")
  @ApiOperation("更新用户标签表")
  @PostMapping(value = "/update")
//    @PreAuthorize("hasAuthority('ums:UmsMemberTag:update')")
  public Object updateUmsMemberTag(@RequestBody UmsMemberTag entity) {
    try {
      if (IUmsMemberTagService.updateById(entity)) {
        return new CommonResult().success();
      }
    } catch (Exception e) {
      log.error("更新用户标签表：%s", e.getMessage(), e);
      return new CommonResult().failed();
    }
    return new CommonResult().failed();
  }

  @SysLog(MODULE = "ums", REMARK = "删除用户标签表")
  @ApiOperation("删除用户标签表")
  @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
//    @PreAuthorize("hasAuthority('ums:UmsMemberTag:delete')")
  public Object deleteUmsMemberTag(@ApiParam("用户标签表id") @PathVariable Long id) {
    try {
      if (ValidatorUtils.empty(id)) {
        return new CommonResult().paramFailed("用户标签表id");
      }
      if (IUmsMemberTagService.removeById(id)) {
        umsMemberMemberTagRelationService.remove(new QueryWrapper<UmsMemberMemberTagRelation>().lambda().eq(UmsMemberMemberTagRelation::getTagId,id));
        return new CommonResult().success();
      }
    } catch (Exception e) {
      log.error("删除用户标签表：%s", e.getMessage(), e);
      return new CommonResult().failed();
    }
    return new CommonResult().failed();
  }

  @SysLog(MODULE = "ums", REMARK = "给用户标签表分配用户标签表")
  @ApiOperation("查询用户标签表明细")
  @GetMapping(value = "/{id}")
  @PreAuthorize("hasAuthority('ums:UmsMemberTag:read')")
  public Object getUmsMemberTagById(@ApiParam("用户标签表id") @PathVariable Long id) {
    try {
      if (ValidatorUtils.empty(id)) {
        return new CommonResult().paramFailed("用户标签表id");
      }
      UmsMemberTag coupon = IUmsMemberTagService.getById(id);
      return new CommonResult().success(coupon);
    } catch (Exception e) {
      log.error("查询用户标签表明细：%s", e.getMessage(), e);
      return new CommonResult().failed();
    }

  }

  @ApiOperation(value = "批量删除用户标签表")
  @RequestMapping(value = "/delete/batch", method = RequestMethod.POST)
  @ResponseBody
  @SysLog(MODULE = "pms", REMARK = "批量删除用户标签表")
  @PreAuthorize("hasAuthority('ums:UmsMemberTag:delete')")
  public Object deleteBatch(@RequestParam("ids") List<Long> ids) {
    boolean count = IUmsMemberTagService.removeByIds(ids);
    umsMemberMemberTagRelationService
      .remove(new QueryWrapper<UmsMemberMemberTagRelation>().lambda().in(UmsMemberMemberTagRelation::getTagId, ids));
    if (count) {
      return new CommonResult().success(count);
    } else {
      return new CommonResult().failed();
    }
  }

  @ApiOperation(value = "根据粉丝id查询用户的标签")
  @RequestMapping(value = "/listTagsByMemberId", method = RequestMethod.GET)
  @ResponseBody
  @SysLog(MODULE = "ums", REMARK = "根据粉丝id查询用户的标签")
//    @PreAuthorize("hasAuthority('ums:UmsMemberTag:delete')")
  public CommonResult<List<UmsMemberTag>> listTagsByMemberId(Long memberId) {
    List<UmsMemberTag> umsMemberTags = IUmsMemberTagService.listTagsByMemberId(memberId);
    return new CommonResult().success(umsMemberTags);
  }

}
