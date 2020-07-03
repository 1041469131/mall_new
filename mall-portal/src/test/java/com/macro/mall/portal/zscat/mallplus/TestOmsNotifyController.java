package com.macro.mall.portal.zscat.mallplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.MallPortalApplication;
import com.zscat.mallplus.manage.config.WxAppletProperties;
import com.zscat.mallplus.manage.service.oms.IOmsOrderItemService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderReturnSaleService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderTradeService;
import com.zscat.mallplus.manage.service.pms.IPmsSkuStockService;
import com.zscat.mallplus.manage.service.sys.ISysMatcherStatisticsService;
import com.zscat.mallplus.mbg.oms.entity.OmsOrder;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderItem;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderTrade;
import com.zscat.mallplus.mbg.oms.mapper.OmsOrderItemMapper;
import com.zscat.mallplus.mbg.pms.entity.PmsProductCommission;
import com.zscat.mallplus.mbg.pms.mapper.PmsProductCommissionMapper;
import com.zscat.mallplus.mbg.sys.entity.SysUser;
import com.zscat.mallplus.mbg.utils.IdGeneratorUtil;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MallPortalApplication.class)
public class TestOmsNotifyController {
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
    private OmsOrderItemMapper omsOrderItemMapper;

    @Autowired
    private PmsProductCommissionMapper pmsProductCommissionMapper;
    @Test
    public void ordernotify() {
        //订单编号
        String outTradeNo="678763810995044352";
        String transactionId="4200000614202006259687940674";
        // 业务处理
        dealOrder(outTradeNo,transactionId);
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
    /**
     * 计算订单的收益
     * @param
     * @return
     */
    @Test
    public void calcProfit() {
        SysUser matcherUser=new SysUser();
        matcherUser.setLevel("1");
        String profitType="0";
        BigDecimal result=null;
        OmsOrder omsOrder=new OmsOrder();
        omsOrder.setId(348L);
        List<OmsOrderItem> omsOrderItems = omsOrderItemMapper.selectList(new QueryWrapper<OmsOrderItem>().eq("order_id",omsOrder.getId()));
        BigDecimal profit = BigDecimal.ZERO;//收益
        //查询订单下面的商品列表
        if(!CollectionUtils.isEmpty(omsOrderItems)){
            for (OmsOrderItem omsOrderItem : omsOrderItems){
                Long productId = omsOrderItem.getProductId();
                Integer productCount = omsOrderItem.getProductQuantity();
                BigDecimal price = omsOrderItem.getProductPrice();
                PmsProductCommission pmsProductCommission = pmsProductCommissionMapper.selectOne(new QueryWrapper<PmsProductCommission>().eq("product_id",productId).
                  eq("matcher_level",matcherUser.getLevel()));
                //商品的分佣查询为空或者不为空但是商品的分佣类型是不分佣
                if(pmsProductCommission == null ||(pmsProductCommission != null && "1".equals(pmsProductCommission.getPromoteType())) ){
                    result= BigDecimal.ZERO;
                    System.out.println("-------计算-------");
                    System.out.println(result);
                    return;
                }
                BigDecimal proportion = BigDecimal.ZERO;
                if("0".equals(profitType)){//商品佣金
                    proportion = pmsProductCommission.getCommissionProportion();//分佣比例
                }else{
                    proportion = pmsProductCommission.getInviteProportion();//邀请奖励
                }
                profit = profit.add(proportion.divide(new BigDecimal(100)).multiply(price).multiply(new BigDecimal(productCount)));
            }
        }
        result= profit;
        System.out.println("-------计算-------");
        System.out.println(result);
    }
}
