package com.zscat.mallplus.portal.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.manage.config.WxAppletProperties;
import com.zscat.mallplus.manage.service.oms.IOmsOrderItemService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderReturnSaleService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderTradeService;
import com.zscat.mallplus.manage.service.pms.IPmsSkuStockService;
import com.zscat.mallplus.manage.service.sys.ISysMatcherStatisticsService;
import com.zscat.mallplus.manage.utils.*;
import com.zscat.mallplus.manage.utils.applet.WechatRefundApiResult;
import com.zscat.mallplus.manage.utils.applet.WechatUtil;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.mbg.annotation.SysLog;
import com.zscat.mallplus.mbg.oms.entity.OmsOrder;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderItem;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnSale;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderTrade;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.IdGeneratorUtil;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import com.zscat.mallplus.portal.single.ApiBaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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


    private static Logger LOGGER = LoggerFactory.getLogger(PayController.class);

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
            totalFee = orderInfo.getTotalAmount().subtract(orderInfo.getCouponAmount()).subtract(orderInfo.getDiscountAmount());
            orderSn = orderInfo.getOrderSn();
        }else {
            orderList = orderService.list(new QueryWrapper<OmsOrder>().eq("supply_id",id));
            if(!CollectionUtils.isEmpty(orderList)){
                for(OmsOrder omsOrder:orderList){
                    totalFee = totalFee.add(omsOrder.getTotalAmount().subtract(omsOrder.getCouponAmount()).subtract(omsOrder.getDiscountAmount()));
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
                log.error("订单" + outTradeNo + "支付成功");
                // 业务处理
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
                        orderInfo.setTransactionId(result.getTransaction_id());
                        omsOrderTrades.add(assemblyOmsTrade(orderInfo,MagicConstant.DIRECT_IN));
                        iSysMatcherStatisticsService.refreshMatcherStatisticsByOrder(orderInfo);
                    }
                }
                if(!CollectionUtils.isEmpty(omsOrderTrades)){
                    orderService.saveOrUpdateBatch(list);
                    iOmsOrderTradeService.saveBatch(omsOrderTrades);
                }
                response.getWriter().write(setXml("SUCCESS", "OK"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * 订单退款请求
     */
    @IgnoreAuth
    @SysLog(MODULE = "pay", REMARK = "订单退款请求")
    @ApiOperation(value = "订单退款请求")
    @RequestMapping(value = "/refund",method = RequestMethod.POST)
    public CommonResult refund(@ApiParam("售后id") String saleId, @ApiParam("订单总金额") String totalFee,
                               @ApiParam("需要退款的金额") String refundMoney) {
        try {
            CommonResult commonResult = new CommonResult();
            OmsOrderReturnSale omsOrderReturnSale = iOmsOrderReturnSaleService.getById(Long.valueOf(saleId));
            OmsOrder omsOrder = orderService.getById(Long.valueOf(omsOrderReturnSale.getOrderId()));
            OmsOrderTrade omsOrderTrade = assemblyOmsTrade(omsOrder,MagicConstant.DIRECT_OUT);
            omsOrderTrade.setAmount(new BigDecimal(refundMoney));
            WechatRefundApiResult result = WechatUtil.wxRefund(omsOrder.getSupplyId().toString(),
                    Double.valueOf(totalFee),Double.valueOf(refundMoney));
            omsOrderTrade.setResponseMsg(JSON.toJSONString(result));
            if (result.getResult_code().equals("SUCCESS")) {
                omsOrder.setStatus(MagicConstant.ORDER_STATUS_YET_SHUTDOWN);
                omsOrderReturnSale.setStatus(MagicConstant.RETURN_STATUS_FINISHED);
                omsOrderTrade.setStatus(MagicConstant.OMS_TRADE_STATUS_SUCCESS);
                commonResult.success("退款成功");
            } else {
                omsOrder.setStatus(MagicConstant.ORDER_STATUS_INVALID_REFUND_FAILD);
                omsOrderReturnSale.setStatus(MagicConstant.RETURN_STATUS_REFUND_FAILD);
                omsOrderTrade.setStatus(MagicConstant.OMS_TRADE_STATUS_FAILD);
                commonResult.success("退款失败");
            }
            orderService.updateById(omsOrder);
            iOmsOrderReturnSaleService.updateById(omsOrderReturnSale);
            iOmsOrderTradeService.save(omsOrderTrade);
            return commonResult;
        }catch (Exception e){
            throw new RuntimeException(e);
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