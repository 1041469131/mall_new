package com.zscat.mallplus.manage.service.oms;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.oms.entity.OmsOrder;
import com.zscat.mallplus.mbg.oms.vo.*;
import com.zscat.mallplus.mbg.pms.vo.PmsProductVo;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import com.zscat.mallplus.mbg.utils.CommonResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
public interface IOmsOrderService extends IService<OmsOrder> {
    /**
     * 修改订单收货人信息
     */
    @Transactional
    int updateReceiverInfo(OmsReceiverInfoParam receiverInfoParam);

    /**
     * 修改订单费用信息
     */
    @Transactional
    int updateMoneyInfo(OmsMoneyInfoParam moneyInfoParam);

    /**
     * 修改订单备注
     */
    @Transactional
    int updateNote(Long id, String note, Integer status);


    /**
     * 批量发货
     */
    @Transactional
    int delivery(List<OmsOrderDeliveryParam> deliveryParamList);

    /**
     * 批量关闭订单
     */
    @Transactional
    int close(List<Long> ids, String note);

    Object preSingelOrder(GroupAndOrderVo orderParam);

    Object generateSingleOrder(GroupAndOrderVo orderParam, UmsMember member);

    /**
     * 根据用户购物车信息生成确认单信息
     */
    ConfirmOrderResult generateConfirmOrder();

    /**
     * 根据提交信息生成订单
     */
    @Transactional
    CommonResult generateOrder(OrderParam orderParam);

    /**
     * 支付成功后的回调
     */
    @Transactional
    CommonResult paySuccess(Long orderId);

    /**
     * 自动取消超时订单
     */
    @Transactional
    CommonResult cancelTimeOutOrder();

    /**
     * 取消单个超时订单
     */
    @Transactional
    Object cancelOrder(Long orderId);

    /**
     * 发送延迟消息取消订单
     */
    void sendDelayMessageCancelOrder(Long orderId);


    ConfirmOrderResult submitPreview(OrderParam orderParam);

    int payOrder(TbThanks tbThanks);

    CommonResult confirmOrder(Long orderId);

    Map<String, Object> payPrepay(String str, String ipStr, BigDecimal totalFee, String isParentOrder, OmsOrder orderInfo, List<OmsOrder> orderList) throws Exception;

    List<OmsOrder> listOmsOrders(String outTradeNo);

    Page<OrderResult> listOmsOrderByPage(OmsOrderQueryParam oderParam);
}
