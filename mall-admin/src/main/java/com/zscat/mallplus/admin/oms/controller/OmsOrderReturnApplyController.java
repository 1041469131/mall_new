package com.zscat.mallplus.admin.oms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.manage.service.oms.IOmsOrderReturnApplyService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderReturnSaleService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderService;
import com.zscat.mallplus.mbg.annotation.SysLog;
import com.zscat.mallplus.mbg.oms.entity.OmsOrder;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnApply;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnSale;
import com.zscat.mallplus.mbg.oms.vo.OmsReturnParam;
import com.zscat.mallplus.mbg.oms.vo.OmsUpdateStatusParam;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.ValidatorUtils;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 订单退货申请
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Slf4j
@RestController
@Api(tags = "OmsOrderReturnApplyController", description = "订单退货申请管理")
@RequestMapping("/oms/OmsOrderReturnApply")
public class OmsOrderReturnApplyController {
    @Resource
    private IOmsOrderReturnApplyService IOmsOrderReturnApplyService;

    @Autowired
    private IOmsOrderReturnSaleService iOmsOrderReturnSaleService;

    @Autowired
    private IOmsOrderService iOmsOrderService;

    @SysLog(MODULE = "oms", REMARK = "根据条件查询所有订单退货申请列表")
    @ApiOperation("根据条件查询所有订单退货申请列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('oms:OmsOrderReturnApply:read')")
    public Object getOmsOrderReturnApplyByPage(OmsOrderReturnApply entity,
                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize
    ) {
        try {
            return new CommonResult().success(IOmsOrderReturnApplyService.page(new Page<OmsOrderReturnApply>(pageNum, pageSize), new QueryWrapper<>(entity)));
        } catch (Exception e) {
            log.error("根据条件查询所有订单退货申请列表：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "oms", REMARK = "保存订单退货申请")
    @ApiOperation("保存订单退货申请")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('oms:OmsOrderReturnApply:create')")
    public Object saveOmsOrderReturnApply(@RequestBody OmsOrderReturnApply entity) {
        try {
            if (IOmsOrderReturnApplyService.save(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("保存订单退货申请：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "oms", REMARK = "更新订单退货申请")
    @ApiOperation("更新订单退货申请")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('oms:OmsOrderReturnApply:update')")
    public Object updateOmsOrderReturnApply(@RequestBody OmsOrderReturnApply entity) {
        try {
            if (IOmsOrderReturnApplyService.updateById(entity)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("更新订单退货申请：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "oms", REMARK = "删除订单退货申请")
    @ApiOperation("删除订单退货申请")
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('oms:OmsOrderReturnApply:delete')")
    public Object deleteOmsOrderReturnApply(@ApiParam("订单退货申请id") @PathVariable Long id) {
        try {
            if (ValidatorUtils.empty(id)) {
                return new CommonResult().paramFailed("订单退货申请id");
            }
            if (IOmsOrderReturnApplyService.removeById(id)) {
                return new CommonResult().success();
            }
        } catch (Exception e) {
            log.error("删除订单退货申请：%s", e.getMessage(), e);
            return new CommonResult().failed();
        }
        return new CommonResult().failed();
    }


    @ApiOperation(value = "批量删除订单退货申请")
    @RequestMapping(value = "/delete/batch", method = RequestMethod.POST)
    @ResponseBody
    @SysLog(MODULE = "pms", REMARK = "批量删除订单退货申请")
    @PreAuthorize("hasAuthority('oms:OmsOrderReturnApply:delete')")
    public Object deleteBatch(@RequestParam("ids") List<Long> ids) {
        boolean count = IOmsOrderReturnApplyService.removeByIds(ids);
        if (count) {
            return new CommonResult().success(count);
        } else {
            return new CommonResult().failed();
        }
    }


    /**********************新增的接口*************************/

    @SysLog(MODULE = "oms", REMARK = "查询售后表的数据")
    @ApiOperation("根据条件查询所有售后表的数据")
    @GetMapping(value = "/listReturnSales")
    @PreAuthorize("hasAuthority('oms:OmsOrderReturnApply:read')")
    public Object listReturnSales(OmsOrderReturnSale entity,
                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        try {
            return new CommonResult().success(iOmsOrderReturnSaleService.page(new Page<OmsOrderReturnSale>(pageNum, pageSize), new QueryWrapper<>(entity).orderByDesc("update_time")));
        } catch (Exception e) {
            log.error("根据条件查询所有售后表的数据：%s", e.getMessage(), e);
        }
        return new CommonResult().failed();
    }


    @SysLog(MODULE = "oms", REMARK = "根据售后id查询售后商品列表")
    @ApiOperation("根据售后id查询售后商品列表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('oms:OmsOrderReturnApply:read')")
    public CommonResult<OmsReturnParam> listReturnApplys(@PathVariable Long id) {
        try {
            OmsReturnParam omsReturnParam = new OmsReturnParam();
            OmsOrderReturnSale omsOrderReturnSale = iOmsOrderReturnSaleService.getById(id);
            List<OmsOrderReturnApply> omsOrderReturnApplies = IOmsOrderReturnApplyService.list(new QueryWrapper<OmsOrderReturnApply>().eq("sale_id",id ));
            omsReturnParam.setOmsOrderReturnSale(omsOrderReturnSale);
            omsReturnParam.setOmsOrderReturnApplies(omsOrderReturnApplies);
            return new CommonResult().success(omsReturnParam);
        } catch (Exception e) {
            log.error("根据售后id查询售后商品列表的数据：%s", e.getMessage(), e);
        }
        return new CommonResult().failed("查询售后列表失败");
    }

    @SysLog(MODULE = "oms", REMARK = "当售后类型为退款，判断该订单是否已发货")
    @ApiOperation("当售后类型为退款，判断该订单是否已发货")
    @RequestMapping(value = "/judgeRetrunSaleStatus", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Boolean> judgeRetrunSaleStatus(@Param("saleId") @ApiParam("售后id") Long saleId) {
        OmsOrderReturnSale omsOrderReturnSale = iOmsOrderReturnSaleService.getById(saleId);
        if(omsOrderReturnSale != null){
            OmsOrder omsOrder = iOmsOrderService.getById(omsOrderReturnSale.getOrderId());
            if(omsOrder.getStatus() == MagicConstant.ORDER_STATUS_YET_SEND &&
                    omsOrderReturnSale.getType() == MagicConstant.RETURN_APPLY_TYPE_REFUND){//当售后是退款，并且订单状态是已发货的状态
                return new CommonResult<>().success(true);
            }else{
                return new CommonResult<>().success(false);
            }
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "oms", REMARK = "修改售后申请的状态")
    @ApiOperation("修改售后申请的状态")
    @RequestMapping(value = "/update/status/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object updateStatus(@PathVariable Long id,@RequestBody OmsUpdateStatusParam statusParam) {
//        int count = iOmsOrderReturnSaleService.updateStatus(statusParam.getId(), statusParam);
//        if (count > 0) {
//            return new CommonResult().success(count);
//        }
        return new CommonResult().failed();
    }
}
