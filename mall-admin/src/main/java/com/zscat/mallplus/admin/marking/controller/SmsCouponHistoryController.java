package com.zscat.mallplus.admin.marking.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.manage.service.marking.ISmsCouponHistoryService;
import com.zscat.mallplus.mbg.annotation.SysLog;
import com.zscat.mallplus.mbg.marking.entity.SmsCouponHistory;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 优惠券使用、领取历史表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Slf4j
@RestController
@Api(tags = "SmsCouponHistoryController", description = "优惠券使用、领取历史表管理")
@RequestMapping("/marking/SmsCouponHistory")
public class SmsCouponHistoryController {
    @Autowired
    private ISmsCouponHistoryService ISmsCouponHistoryService;

    @SysLog(MODULE = "marking", REMARK = "根据条件查询所有优惠券使用、领取历史表列表")
    @ApiOperation("根据条件查询所有优惠券使用、领取历史表列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('marking:SmsCouponHistory:read')")
    public Object getSmsCouponHistoryByPage(SmsCouponHistory entity,
                                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize
    ) {
        try {
            return new CommonResult().success(ISmsCouponHistoryService.page(new Page<SmsCouponHistory>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有优惠券使用、领取历史表列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "marking", REMARK = "保存优惠券使用、领取历史表")
    @ApiOperation("保存优惠券使用、领取历史表")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('marking:SmsCouponHistory:create')")
    public Object saveSmsCouponHistory(@RequestBody SmsCouponHistory entity) {
        try {
            if (ISmsCouponHistoryService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存优惠券使用、领取历史表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "marking", REMARK = "更新优惠券使用、领取历史表")
    @ApiOperation("更新优惠券使用、领取历史表")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('marking:SmsCouponHistory:update')")
    public Object updateSmsCouponHistory(@RequestBody SmsCouponHistory entity) {
        try {
            if (ISmsCouponHistoryService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新优惠券使用、领取历史表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "marking", REMARK = "删除优惠券使用、领取历史表")
    @ApiOperation("删除优惠券使用、领取历史表")
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('marking:SmsCouponHistory:delete')")
    public Object deleteSmsCouponHistory(@ApiParam("优惠券使用、领取历史表id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("优惠券使用、领取历史表id");
            }
            if (ISmsCouponHistoryService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除优惠券使用、领取历史表：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "marking", REMARK = "给优惠券使用、领取历史表分配优惠券使用、领取历史表")
    @ApiOperation("查询优惠券使用、领取历史表明细")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('marking:SmsCouponHistory:read')")
    public Object getSmsCouponHistoryById(@ApiParam("优惠券使用、领取历史表id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("优惠券使用、领取历史表id");
            }
            SmsCouponHistory coupon = ISmsCouponHistoryService.getById(id);
            return new CommonResult().success(coupon);
        } catch (Exception e) {
            log.error("查询优惠券使用、领取历史表明细：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }

    }

    @ApiOperation(value = "批量删除优惠券使用、领取历史表")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量删除优惠券使用、领取历史表")
    @PreAuthorize("hasAuthority('marking:SmsCouponHistory:delete')")
    public Object deleteBatch(@RequestParam("ids") List<Long> ids) {
        boolean count = ISmsCouponHistoryService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }

}
