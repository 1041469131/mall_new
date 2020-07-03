package com.zscat.mallplus.admin.oms.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.constant.WxPayConstants.SignType;
import com.github.binarywang.wxpay.service.WxPayService;
import com.zscat.mallplus.manage.config.WxAppletProperties;
import com.zscat.mallplus.manage.service.oms.IOmsOrderTradeService;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderTrade;
import com.zscat.mallplus.mbg.oms.vo.OmsOrderReturnApplyVO;
import com.zscat.mallplus.mbg.oms.vo.OmsOrderReturnSaleVO;
import com.zscat.mallplus.mbg.oms.vo.OmsOrderSaleParam;
import com.zscat.mallplus.manage.service.oms.IOmsOrderReturnApplyService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderReturnSaleService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderService;
import com.zscat.mallplus.manage.utils.JsonUtil;
import com.zscat.mallplus.mbg.annotation.SysLog;
import com.zscat.mallplus.mbg.oms.entity.OmsOrder;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnApply;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnSale;
import com.zscat.mallplus.mbg.oms.vo.OmsReturnParam;
import com.zscat.mallplus.mbg.oms.vo.OmsReturnSaleDetail;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.IdGeneratorUtil;
import com.zscat.mallplus.mbg.utils.ValidatorUtils;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.math.BigDecimal;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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
    private IOmsOrderReturnApplyService omsOrderReturnApplyService;

    @Autowired
    private IOmsOrderReturnSaleService iOmsOrderReturnSaleService;

    @Autowired
    private IOmsOrderService omsOrderService;

    @Autowired
    private WxPayService wxService;

    @Autowired
    private WxAppletProperties wxAppletProperties;

    @Autowired
    private IOmsOrderTradeService iOmsOrderTradeService;

    @SysLog(MODULE = "oms", REMARK = "根据条件查询所有订单退货申请列表")
    @ApiOperation("根据条件查询所有订单退货申请列表")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('oms:OmsOrderReturnApply:read')")
    public Object getOmsOrderReturnApplyByPage(OmsOrderReturnApply entity,
                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize
    ) {
        try {
            return new CommonResult().success(omsOrderReturnApplyService.page(new Page<OmsOrderReturnApply>(pageNum, pageSize), new QueryWrapper<>(entity)));
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
            if (omsOrderReturnApplyService.save(entity)) {
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
            if (omsOrderReturnApplyService.updateById(entity)) {
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
            if (omsOrderReturnApplyService.removeById(id)) {
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
        boolean count = omsOrderReturnApplyService.removeByIds(ids);
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
    public CommonResult< Page<OmsOrderReturnSaleVO>> listReturnSales(@ApiParam("售后查询参数") OmsOrderSaleParam queryParam) {
        return new CommonResult().success(iOmsOrderReturnSaleService.listVoByPage(queryParam));
    }


    @SysLog(MODULE = "oms", REMARK = "根据售后id查询售后商品列表")
    @ApiOperation("根据售后id查询售后商品列表")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('oms:OmsOrderReturnApply:read')")
    public CommonResult<OmsReturnSaleDetail> listReturnApplys(@PathVariable Long id) {
        try {
            OmsReturnSaleDetail omsReturnSaleDetail=new OmsReturnSaleDetail();
            List<OmsOrderReturnApplyVO> omsOrderReturnApplyVOS = omsOrderReturnApplyService.listBySaleId(id);
            OmsOrderReturnSale omsOrderReturnSale = iOmsOrderReturnSaleService.getById(id);
            omsReturnSaleDetail.setOmsOrderReturnSale(omsOrderReturnSale);
            omsReturnSaleDetail.setOmsOrderReturnApplyVOs(omsOrderReturnApplyVOS);
            if(!CollectionUtils.isEmpty(omsOrderReturnApplyVOS)){
                Long orderId = omsOrderReturnApplyVOS.get(0).getOrderId();
                OmsOrder omsOrder = omsOrderService.getById(orderId);
                omsReturnSaleDetail.setOmsOrder(omsOrder);
            }
            return new CommonResult().success(omsReturnSaleDetail);
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
            OmsOrder omsOrder = omsOrderService.getById(omsOrderReturnSale.getOrderId());
            if(omsOrder.getStatus().equals(MagicConstant.ORDER_STATUS_YET_SEND) &&
              omsOrderReturnSale.getType().equals(MagicConstant.RETURN_APPLY_TYPE_REFUND)){
                //当售后是退款，并且订单状态是已发货的状态
                return new CommonResult().success(true);
            }else{
                return new CommonResult().success(false);
            }
        }
        return new CommonResult().failed();
    }

    @SysLog(MODULE = "oms", REMARK = "修改售后申请的状态")
    @ApiOperation("修改售后申请的状态")
    @RequestMapping(value = "/update/status", method = RequestMethod.POST)
    @ResponseBody
    public Object updateStatus(@RequestBody @ApiParam OmsOrderReturnSale updateSale) {
        OmsOrderReturnSale returnSale=new OmsOrderReturnSale();
        returnSale.setStatus(updateSale.getStatus());
        returnSale.setId(updateSale.getId());
        returnSale.setHandleNote(updateSale.getHandleNote());
        int count = iOmsOrderReturnSaleService.updateStatus(returnSale);
        if (count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }

    /**
     * 订单退款请求
     */
    @IgnoreAuth
    @SysLog(MODULE = "pay", REMARK = "订单退款请求")
    @ApiOperation(value = "订单退款请求")
    @RequestMapping(value = "/refund",method = RequestMethod.POST)
    public CommonResult refund(@RequestBody @ ApiParam OmsOrderReturnSale returnSale) {
        try {
            CommonResult commonResult = new CommonResult();
            OmsOrderReturnSale omsOrderReturnSale = iOmsOrderReturnSaleService.getById(Long.valueOf(returnSale.getId()));
            OmsOrder omsOrder = omsOrderService.getById(Long.valueOf(omsOrderReturnSale.getOrderId()));
            OmsOrderTrade omsOrderTrade = assemblyOmsTrade(omsOrder,MagicConstant.DIRECT_OUT);
            //判断退款金额 必须低于支付金额
            if(returnSale.getRealReturnAmount().compareTo(omsOrderReturnSale.getReturnAmount())>0){
                return  commonResult.failed("退金额大于售后退款金额");
            }
            omsOrderReturnSale.setRealReturnAmount(returnSale.getRealReturnAmount());
            omsOrderTrade.setAmount(returnSale.getRealReturnAmount());
//            WechatRefundApiResult result1 = WechatUtil.wxRefund(omsOrder.getSupplyId().toString(),
//                  omsOrder.getTotalAmount(),new BigDecimal(refundMoney));
            WxPayRefundRequest wxPayRefundRequest=new WxPayRefundRequest();
            //订单总金额，单位为分，只能为整数
            wxPayRefundRequest.setTotalFee(omsOrder.getPayAmount().multiply(new BigDecimal(100)).intValue());
            //退款总金额，订单总金额，单位为分，只能为整数
            wxPayRefundRequest.setRefundFee(returnSale.getRealReturnAmount().multiply(new BigDecimal(100)).intValue());
            //商户侧传给微信的订单号
            wxPayRefundRequest.setOutTradeNo(omsOrder.getSupplyId().toString());
            wxPayRefundRequest.setTransactionId(omsOrder.getTransactionId());
            wxPayRefundRequest.setSignType(SignType.MD5);
            //商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
            wxPayRefundRequest.setOutRefundNo(omsOrderReturnSale.getId().toString());
            wxPayRefundRequest.setNotifyUrl(wxAppletProperties.getRefundUrl());
            WxPayRefundResult result = wxService.refund(wxPayRefundRequest);
            omsOrderTrade.setResponseMsg(JSON.toJSONString(result));
            if (result.getResultCode().equals("SUCCESS")) {
                //退款回调处理 看是否全部订单退款 如果是部分退款 订单状态不变？还是改成关闭
//                omsOrder.setStatus(MagicConstant.ORDER_STATUS_YET_SHUTDOWN);
                omsOrderReturnSale.setStatus(MagicConstant.RETURN_STATUS_REFUND);
//                omsOrderTrade.setStatus(MagicConstant.OMS_TRADE_STATUS_SUCCESS);
                commonResult.success("退款成功");
            } else {
                omsOrder.setStatus(MagicConstant.ORDER_STATUS_INVALID_REFUND_FAILD);
                omsOrderReturnSale.setStatus(MagicConstant.RETURN_STATUS_REFUND_FAILD);
                omsOrderTrade.setStatus(MagicConstant.OMS_TRADE_STATUS_FAILD);
                omsOrderService.updateById(omsOrder);
                iOmsOrderTradeService.save(omsOrderTrade);
                commonResult.success("退款失败");
            }
            iOmsOrderReturnSaleService.updateById(omsOrderReturnSale);
            return commonResult;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 组装交易流水
     */
    private OmsOrderTrade assemblyOmsTrade(OmsOrder orderInfo,Integer direct) {
        OmsOrderTrade omsOrderTrade = new OmsOrderTrade();
        omsOrderTrade.setAmount(orderInfo.getPayAmount());
        omsOrderTrade.setCreateTime(new Date());
        omsOrderTrade.setDirect(direct);
        omsOrderTrade.setOrderCn(orderInfo.getOrderSn());
        omsOrderTrade.setOrdId(orderInfo.getId());
        omsOrderTrade.setPrepayId(orderInfo.getPrepayId());
        omsOrderTrade.setSupplyId(orderInfo.getSupplyId());
        omsOrderTrade.setId(IdGeneratorUtil.getIdGeneratorUtil().nextId());
        return omsOrderTrade;
    }

}
