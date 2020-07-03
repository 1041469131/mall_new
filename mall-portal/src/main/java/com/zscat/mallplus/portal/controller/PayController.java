package com.zscat.mallplus.portal.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.constant.WxPayConstants.SignType;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.zscat.mallplus.manage.config.WxAppletProperties;
import com.zscat.mallplus.manage.service.oms.IOmsMatcherCommissionItemService;
import com.zscat.mallplus.manage.service.oms.IOmsMatcherCommissionService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderItemService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderReturnApplyService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderReturnSaleService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderTradeService;
import com.zscat.mallplus.manage.service.pms.IPmsProductService;
import com.zscat.mallplus.manage.service.pms.IPmsSkuStockService;
import com.zscat.mallplus.manage.service.sys.ISysMatcherStatisticsService;
import com.zscat.mallplus.manage.utils.*;
import com.zscat.mallplus.manage.utils.applet.WechatRefundApiResult;
import com.zscat.mallplus.manage.utils.applet.WechatUtil;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.mbg.annotation.SysLog;
import com.zscat.mallplus.mbg.oms.entity.OmsMatcherCommission;
import com.zscat.mallplus.mbg.oms.entity.OmsMatcherCommissionItem;
import com.zscat.mallplus.mbg.oms.entity.OmsOrder;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderItem;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnApply;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnSale;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderTrade;
import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.IdGeneratorUtil;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import com.zscat.mallplus.portal.single.ApiBaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * 作者: @author Harmon <br>
 * 时间: 2017-08-11 08:32<br>
 * 描述: ApiIndexController <br>
 */
@Api(tags = "PayController",description = "订单管理")
@Slf4j
@RestController
@RequestMapping("/api/pay")
public class PayController extends ApiBaseAction {

    @Autowired
    private IOmsOrderService orderService;

    @Autowired
    private WxAppletProperties wxAppletProperties;

    @Autowired
    private IOmsOrderTradeService iOmsOrderTradeService;

    @Autowired
    private IOmsOrderReturnSaleService iOmsOrderReturnSaleService;

    @Autowired
    private IOmsOrderItemService iOmsOrderItemService;

    @Autowired
    private IPmsSkuStockService iPmsSkuStockService;

    @Autowired
    private ISysMatcherStatisticsService iSysMatcherStatisticsService;

    @Autowired
    private IPmsProductService pmsProductService;

    @Autowired
    private WxPayService wxService;

    @Autowired
    private IOmsOrderReturnApplyService omsOrderReturnApplyService;

    @Autowired
    private IOmsMatcherCommissionService omsMatcherCommissionService;

    @Autowired
    private IOmsMatcherCommissionItemService omsMatcherCommissionItemService;

    /**
     */
    @ApiOperation("跳转")
    @PostMapping("index")
    public Object index() {
        return ResponseUtil.toResponsSuccess("");
    }

    /**
     * 获取支付的请求参数
     */
    @SysLog(MODULE = "pay", REMARK = "获取支付的请求参数")
    @ApiOperation("获取支付的请求参数,当在购物车里面进行支付的时候，id等于supplyId（相当于一个父订单），isParentOrder=0,当在我的订单中待付款的时候id等于id(订单的id)，isParentOrder=1")
    @GetMapping("prepay")
    public Object payPrepay(@RequestParam(value = "id", required = false, defaultValue = "0") Long id, String isParentOrder) {
        OmsOrder orderInfo = null;
        List<OmsOrder> orderList = null;
        BigDecimal totalFee = BigDecimal.ZERO;
        String orderSn = "";
        if(MagicConstant.IS_NOT_PARENT.equals(isParentOrder)){
            orderInfo = orderService.getById(id);
            totalFee = orderInfo.getPayAmount().subtract(orderInfo.getDiscountAmount());
            orderSn = orderInfo.getOrderSn();
            if(!checkProduct(orderInfo)){
                return ResponseUtil.toResponsFail("下单失败,该商品已下架");
            }
        }else {
            orderList = orderService.list(new QueryWrapper<OmsOrder>().eq("supply_id",id));
            if(!CollectionUtils.isEmpty(orderList)){
                for(OmsOrder omsOrder:orderList){
                    totalFee = totalFee.add(omsOrder.getPayAmount().subtract(omsOrder.getDiscountAmount()));
                    if(!checkProduct(omsOrder)){
                        return ResponseUtil.toResponsFail("下单失败,该商品已下架");
                    }
                }
            }
            orderSn = String.valueOf(id);
        }


        try {
            String ipStr = getClientIp();
            return orderService.payPrepay(ipStr,orderSn,totalFee,isParentOrder,orderInfo,orderList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.toResponsFail("下单失败,error=" + e.getMessage());
        }
    }

    private boolean checkProduct(OmsOrder orderInfo) {
        List<OmsOrderItem> omsOrderItems = iOmsOrderItemService.list(new QueryWrapper<OmsOrderItem>().eq("order_id",orderInfo.getId()));
        for (OmsOrderItem omsOrderItem : omsOrderItems) {
            PmsProduct pmsProduct = pmsProductService.getById(omsOrderItem.getProductId());
            if(pmsProduct.getPublishStatus()==0||pmsProduct.getDeleteStatus()==1){
               return false;
            }
        }
       return  true;
    }


    /**
     * 获取支付的请求参数
     */
    @SysLog(MODULE = "pay", REMARK = "获取订单详情")
    @ApiOperation("获取订单详情,当在购物车里面进行支付的时候，id等于supplyId（相当于一个父订单），isParentOrder=0,当在我的订单中待付款的时候id等于id(订单的id)，isParentOrder=1")
    @GetMapping("queryOrder")
    public List<OmsOrder>  queryOrder(@RequestParam(value = "id", required = false, defaultValue = "0") Long id, String isParentOrder) {
        List<OmsOrder> orderList = new ArrayList<>();
        if(MagicConstant.IS_NOT_PARENT.equals(isParentOrder)){
            orderList.add(orderService.getById(id));
        }else {
            orderList.addAll(orderService.list(new QueryWrapper<OmsOrder>().eq("supply_id",id)));
        }
        return orderList;
    }
        /**
         * 微信查询订单状态
         */
    @SysLog(MODULE = "pay", REMARK = "查询订单状态")
    @ApiOperation( "查询订单状态")
    @GetMapping("query")
    public Object orderQuery( @RequestParam(value = "id", required = false, defaultValue = "0") Long id) {
        UmsMember user = UserUtils.getCurrentUmsMember();
        //
        OmsOrder orderDetail = orderService.getById(id);
        if (id == null) {
            return ResponseUtil.toResponsFail("订单不存在");
        }
        Map<Object, Object> parame = new TreeMap<Object, Object>();
        parame.put("appid", wxAppletProperties.getAppId());
        // 商家账号。
        parame.put("mch_id", wxAppletProperties.getMchId());
        String randomStr = CharUtil.getRandomNum(18).toUpperCase();
        // 随机字符串
        parame.put("nonce_str", randomStr);
        // 商户订单编号
        parame.put("out_trade_no", orderDetail.getOrderSn());

        String sign = WechatUtil.arraySign(parame, wxAppletProperties.getPaySignKey());
        // 数字签证
        parame.put("sign", sign);

        String xml = MapUtils.convertMap2Xml(parame);
        log.info("xml:" + xml);
        Map<String, Object> resultUn = null;
        try {
            resultUn = XmlUtil.xmlStrToMap(WechatUtil.requestOnce(wxAppletProperties.getOrderquery(), xml));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.toResponsFail("查询失败,error=" + e.getMessage());
        }
        // 响应报文
        String return_code = MapUtils.getString("return_code", resultUn);
        String return_msg = MapUtils.getString("return_msg", resultUn);

        if (!"SUCCESS".equals(return_code)) {
            return ResponseUtil.toResponsFail("查询失败,error=" + return_msg);
        }

        String trade_state = MapUtils.getString("trade_state", resultUn);
        if ("SUCCESS".equals(trade_state)) {
            // 更改订单状态
            // 业务处理
            OmsOrder orderInfo = new OmsOrder();
            orderInfo.setId(id);
            orderInfo.setStatus(2);
            orderInfo.setConfirmStatus(1);
            orderInfo.setPaymentTime(new Date());
            orderService.updateById(orderInfo);
            return ResponseUtil.toResponsMsgSuccess("支付成功");
        } else if ("USERPAYING".equals(trade_state)) {
            // 重新查询 正在支付中
           /* Integer num = (Integer) J2CacheUtils.get(J2CacheUtils.SHOP_CACHE_NAME, "queryRepeatNum" + id + "");
            if (num == null) {
                J2CacheUtils.put(J2CacheUtils.SHOP_CACHE_NAME, "queryRepeatNum" + id + "", 1);
                this.orderQuery(id);
            } else if (num <= 3) {
                J2CacheUtils.remove(J2CacheUtils.SHOP_CACHE_NAME, "queryRepeatNum" + id);
                this.orderQuery(id);
            } else {
                return toResponsFail("查询失败,error=" + trade_state);
            }*/

        } else {
            // 失败
            return ResponseUtil.toResponsFail("查询失败,error=" + trade_state);
        }

        return ResponseUtil.toResponsFail("查询失败，未知错误");
    }

    /**
     * 微信订单回调接口
     *
     * @return
     */
    @SysLog(MODULE = "pay", REMARK = "微信订单回调接口")
    @ApiOperation("微信订单回调接口")
    @RequestMapping(value = "/notify", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @IgnoreAuth
    public void notify(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            InputStream in = request.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.close();
            in.close();
            //xml数据
            String reponseXml = new String(out.toByteArray(), "utf-8");

            WechatRefundApiResult result = (WechatRefundApiResult) XmlUtil.xmlStrToBean(reponseXml, WechatRefundApiResult.class);
            String result_code = result.getResult_code();
            if (result_code.equalsIgnoreCase("FAIL")) {
                //订单编号
                String out_trade_no = result.getOut_trade_no();
                log.error("订单" + out_trade_no + "支付失败");
                response.getWriter().write(setXml("SUCCESS", "OK"));
            } else if (result_code.equalsIgnoreCase("SUCCESS")) {
                //订单编号
                String outTradeNo = result.getOut_trade_no();
                String transactionId = result.getTransaction_id();
                log.error("订单" + outTradeNo + "支付成功");
                // 业务处理
                dealOrder(outTradeNo, transactionId);
                response.getWriter().write(setXml("SUCCESS", "OK"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    @ApiOperation(value = "退款回调通知处理")
    @PostMapping("/notify/refund")
    @IgnoreAuth
    public String parseRefundNotifyResult(@RequestBody String xmlData) throws WxPayException {
        final WxPayRefundNotifyResult result = this.wxService.parseRefundNotifyResult(xmlData);
        String outRefundNo = result.getReqInfo().getOutRefundNo();
        Long saleId=Long.valueOf(outRefundNo);
        OmsOrderReturnSale omsOrderReturnSale = iOmsOrderReturnSaleService.getById(Long.valueOf(saleId));
        Long orderId = omsOrderReturnSale.getOrderId();
        OmsOrder omsOrder = orderService.getById(orderId);
        if(!omsOrderReturnSale.getStatus().equals(MagicConstant.RETURN_STATUS_FINISHED)&&result.getReturnCode().equals("SUCCESS")) {
            //退款回调处理
            //查询出saleId的product明细
            List<OmsOrderReturnApply> list = omsOrderReturnApplyService
              .list(new QueryWrapper<OmsOrderReturnApply>().lambda().eq(OmsOrderReturnApply::getSaleId, saleId));
            //查询不是该退款笔订单已退款的商品数量 用于计算是不是全额退款
           int orderReturnApplyCount= omsOrderReturnApplyService.count(
              new QueryWrapper<OmsOrderReturnApply>().lambda().eq(OmsOrderReturnApply::getOrderId, orderId)
                .eq(OmsOrderReturnApply::getStatus, MagicConstant.RETURN_STATUS_FINISHED).ne(OmsOrderReturnApply::getSaleId,saleId));
           //查询出订单里面的商品数量
            int itemCount = iOmsOrderItemService.count(new QueryWrapper<OmsOrderItem>().lambda().eq(OmsOrderItem::getOrderId, orderId));
            //查询出该笔订单的分佣
            List<OmsMatcherCommission> omsMatcherCommissionList = omsMatcherCommissionService
              .list(new QueryWrapper<OmsMatcherCommission>().lambda().eq(OmsMatcherCommission::getOrderId, orderId));
            //退款的分佣明细
            List<Long> orderItemIds = list.stream().map(OmsOrderReturnApply::getOrderItemId).collect(Collectors.toList());
            List<OmsMatcherCommissionItem> omsMatcherCommissionItemList = omsMatcherCommissionItemService.list(
              new QueryWrapper<OmsMatcherCommissionItem>().lambda().eq(OmsMatcherCommissionItem::getOrderId, orderId)
                .in(OmsMatcherCommissionItem::getOrderItemId, orderItemIds));
            if((orderReturnApplyCount+list.size())>=itemCount){
                omsOrder.setStatus(MagicConstant.ORDER_STATUS_YET_SHUTDOWN);
                omsMatcherCommissionList.forEach(omsMatcherCommission -> {
                    omsMatcherCommission.setStatus(MagicConstant.SETTLE_STAUTS_NOSETTLED);
                    omsMatcherCommission.setProfit(new BigDecimal(0));
                });
                omsMatcherCommissionItemList.forEach(item ->item.setStatus("0"));
            }else{
                //部分退款
                //收益 需要减去退款部分
                omsMatcherCommissionList.forEach(omsMatcherCommission ->{
                    omsMatcherCommission.setStatus(MagicConstant.SETTLE_STAUTS_WAITE_PARTREFUND);
                    omsMatcherCommissionItemList.forEach(item -> {
                        if(item.getProfitType().equals(omsMatcherCommission.getProfitType())){
                            omsMatcherCommission.setProfit(omsMatcherCommission.getProfit().subtract(item.getProfit()));
                        }
                        item.setStatus("0");
                    });

                });
            }
            OmsOrderTrade omsOrderTrade = assemblyOmsTrade(omsOrder, MagicConstant.DIRECT_OUT);
            omsOrderTrade.setResponseMsg(JSON.toJSONString(result));
            omsOrderReturnSale.setStatus(MagicConstant.RETURN_STATUS_FINISHED);
            omsOrderTrade.setStatus(MagicConstant.OMS_TRADE_STATUS_SUCCESS);
            orderService.updateById(omsOrder);
            iOmsOrderReturnSaleService.updateById(omsOrderReturnSale);
            iOmsOrderTradeService.save(omsOrderTrade);
            list.forEach(l->l.setStatus(MagicConstant.RETURN_STATUS_FINISHED));
            omsOrderReturnApplyService.saveOrUpdateBatch(list);
            if(!CollectionUtils.isEmpty(omsMatcherCommissionList)){
                omsMatcherCommissionService.saveBatch(omsMatcherCommissionList);
                omsMatcherCommissionItemService.saveOrUpdateBatch(omsMatcherCommissionItemList);
            }
        }
        return WxPayNotifyResponse.success("成功");
    }


    private void dealOrder(String outTradeNo, String transactionId) {
        List<OmsOrder> list = orderService.listOmsOrders(outTradeNo);
        List<OmsOrderTrade> omsOrderTrades = null;
        if(!CollectionUtils.isEmpty(list)){
            omsOrderTrades = new ArrayList<>();
            for(OmsOrder orderInfo:list){
                if(orderInfo.getStatus().equals(MagicConstant.ORDER_STATUS_WAIT_SEND)){
                    continue;
                }
                List<OmsOrderItem> omsOrderItems = iOmsOrderItemService.list(new QueryWrapper<OmsOrderItem>().eq("order_id",orderInfo.getId()));
                if(!CollectionUtils.isEmpty(omsOrderItems)){
                    for(OmsOrderItem omsOrderItem : omsOrderItems){
                        iPmsSkuStockService.updateStockCount(omsOrderItem.getProductSkuId(),omsOrderItem.getProductQuantity(),"1");
                    }
                }
                orderInfo.setStatus(MagicConstant.ORDER_STATUS_WAIT_SEND);
                orderInfo.setPaymentTime(new Date());
                orderInfo.setTransactionId(transactionId);
                omsOrderTrades.add(assemblyOmsTrade(orderInfo,MagicConstant.DIRECT_IN));
                iSysMatcherStatisticsService.refreshMatcherStatisticsByOrder(orderInfo);
            }
        }
        if(!CollectionUtils.isEmpty(omsOrderTrades)){
            orderService.saveOrUpdateBatch(list);
            iOmsOrderTradeService.saveBatch(omsOrderTrades);
        }
    }

    //返回微信服务
    public static String setXml(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }

    //模拟微信回调接口
    public static String callbakcXml(String orderNum) {
        return "<xml><appid><![CDATA[wx2421b1c4370ec43b]]></appid><attach><![CDATA[支付测试]]></attach><bank_type><![CDATA[CFT]]></bank_type><fee_type><![CDATA[CNY]]></fee_type> <is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[10000100]]></mch_id><nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str><openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid> <out_trade_no><![CDATA[" + orderNum + "]]></out_trade_no>  <result_code><![CDATA[SUCCESS]]></result_code> <return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign><sub_mch_id><![CDATA[10000100]]></sub_mch_id> <time_end><![CDATA[20140903131540]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id></xml>";
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