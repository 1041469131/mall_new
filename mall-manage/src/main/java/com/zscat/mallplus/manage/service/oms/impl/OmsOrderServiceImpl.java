package com.zscat.mallplus.manage.service.oms.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.config.WxAppletProperties;
import com.zscat.mallplus.manage.service.marking.ISmsCouponHistoryService;
import com.zscat.mallplus.manage.service.marking.ISmsCouponService;
import com.zscat.mallplus.manage.service.marking.ISmsGroupMemberService;
import com.zscat.mallplus.manage.service.marking.ISmsGroupService;
import com.zscat.mallplus.manage.service.oms.IOmsCartItemService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderItemService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderOperateHistoryService;
import com.zscat.mallplus.manage.service.oms.IOmsOrderService;
import com.zscat.mallplus.manage.service.pms.IPmsProductService;
import com.zscat.mallplus.manage.service.pms.IPmsSkuStockService;
import com.zscat.mallplus.manage.service.sms.ISmsService;
import com.zscat.mallplus.manage.service.ums.IUmsMemberReceiveAddressService;
import com.zscat.mallplus.manage.service.ums.IUmsMemberService;
import com.zscat.mallplus.manage.service.ums.RedisService;
import com.zscat.mallplus.manage.service.wechat.WechatApiService;
import com.zscat.mallplus.manage.utils.CharUtil;
import com.zscat.mallplus.manage.utils.DateUtils;
import com.zscat.mallplus.manage.utils.MapUtils;
import com.zscat.mallplus.manage.utils.ResponseUtil;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.manage.utils.XmlUtil;
import com.zscat.mallplus.manage.utils.applet.TemplateData;
import com.zscat.mallplus.manage.utils.applet.WX_TemplateMsgUtil;
import com.zscat.mallplus.manage.utils.applet.WechatUtil;
import com.zscat.mallplus.mbg.exception.ApiMallPlusException;
import com.zscat.mallplus.mbg.marking.entity.SmsCoupon;
import com.zscat.mallplus.mbg.marking.entity.SmsCouponHistory;
import com.zscat.mallplus.mbg.marking.entity.SmsCouponProductCategoryRelation;
import com.zscat.mallplus.mbg.marking.entity.SmsCouponProductRelation;
import com.zscat.mallplus.mbg.marking.entity.SmsGroup;
import com.zscat.mallplus.mbg.marking.entity.SmsGroupMember;
import com.zscat.mallplus.mbg.marking.vo.SmsCouponHistoryDetail;
import com.zscat.mallplus.mbg.oms.entity.OmsCartItem;
import com.zscat.mallplus.mbg.oms.entity.OmsOrder;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderItem;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderOperateHistory;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderSetting;
import com.zscat.mallplus.mbg.oms.mapper.OmsOrderItemMapper;
import com.zscat.mallplus.mbg.oms.mapper.OmsOrderMapper;
import com.zscat.mallplus.mbg.oms.mapper.OmsOrderOperateHistoryMapper;
import com.zscat.mallplus.mbg.oms.mapper.OmsOrderSettingMapper;
import com.zscat.mallplus.mbg.oms.vo.CartPromotionItem;
import com.zscat.mallplus.mbg.oms.vo.ConfirmOrderResult;
import com.zscat.mallplus.mbg.oms.vo.GroupAndOrderVo;
import com.zscat.mallplus.mbg.oms.vo.OmsMoneyInfoParam;
import com.zscat.mallplus.mbg.oms.vo.OmsOrderDeliveryParam;
import com.zscat.mallplus.mbg.oms.vo.OmsOrderDetail;
import com.zscat.mallplus.mbg.oms.vo.OmsOrderQueryParam;
import com.zscat.mallplus.mbg.oms.vo.OmsReceiverInfoParam;
import com.zscat.mallplus.mbg.oms.vo.OrderParam;
import com.zscat.mallplus.mbg.oms.vo.OrderResult;
import com.zscat.mallplus.mbg.oms.vo.TbThanks;
import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import com.zscat.mallplus.mbg.pms.entity.PmsSkuStock;
import com.zscat.mallplus.mbg.ums.entity.UmsIntegrationConsumeSetting;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import com.zscat.mallplus.mbg.ums.entity.UmsMemberReceiveAddress;
import com.zscat.mallplus.mbg.ums.mapper.UmsIntegrationConsumeSettingMapper;
import com.zscat.mallplus.mbg.ums.mapper.UmsMemberStatisticsInfoMapper;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.IdGeneratorUtil;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
@Service
@Slf4j
public class OmsOrderServiceImpl extends ServiceImpl<OmsOrderMapper, OmsOrder> implements IOmsOrderService {

  @Resource
  private OmsOrderMapper orderMapper;
  @Resource
  private IOmsOrderOperateHistoryService orderOperateHistoryDao;
  @Resource
  private OmsOrderOperateHistoryMapper orderOperateHistoryMapper;
  @Resource
  private RedisService redisService;
  @Resource
  private IPmsProductService productService;
  @Resource
  private IUmsMemberReceiveAddressService addressService;
  @Autowired
  private WxAppletProperties wxAppletProperties;
  @Resource
  private WechatApiService wechatApiService;
  @Resource
  private ISmsGroupService groupService;
  @Resource
  private ISmsGroupMemberService groupMemberService;
  @Resource
  private IOmsCartItemService cartItemService;
  @Resource
  private ISmsCouponService couponService;
  @Resource
  private UmsIntegrationConsumeSettingMapper integrationConsumeSettingMapper;
  @Resource
  private IPmsSkuStockService iPmsSkuStockService;
  @Resource
  private ISmsCouponHistoryService couponHistoryService;
  @Resource
  private IOmsOrderService orderService;
  @Resource
  private IOmsOrderItemService orderItemService;
  @Resource
  private IUmsMemberService memberService;
  @Resource
  private OmsOrderSettingMapper orderSettingMapper;
  @Autowired
  private OmsOrderItemMapper omsOrderItemMapper;
  @Autowired
  private UmsMemberStatisticsInfoMapper umsMemberStatisticsInfoMapper;
  @Autowired
  private ISmsService smsService;

  @Override
  public int delivery(List<OmsOrderDeliveryParam> deliveryParamList) {
    //批量发货
    int count = orderMapper.delivery(deliveryParamList);
    //添加操作记录
    List<OmsOrderOperateHistory> operateHistoryList = deliveryParamList.stream()
      .map(omsOrderDeliveryParam -> {
        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        history.setOrderId(omsOrderDeliveryParam.getOrderId());
        history.setCreateTime(new Date());
        history.setOperateMan("后台管理员");
        history.setOrderStatus(2);
        history.setNote("完成发货");
        return history;
      }).collect(Collectors.toList());
    orderOperateHistoryDao.saveBatch(operateHistoryList);
    deliveryParamList.forEach(delivery->{
      OmsOrder om = orderService.getById(delivery.getOrderId());
      smsService.deliveryNotify(om.getReceiverPhone(),delivery.getDeliverySn(),delivery.getDeliveryCompany());
    });
    return count;
  }

  @Override
  public int close(List<Long> ids, String note) {
    OmsOrder record = new OmsOrder();
    record.setStatus(4);
    int count = orderMapper.update(record, new QueryWrapper<OmsOrder>().eq("delete_status", 0).in("id", ids));
    List<OmsOrderOperateHistory> historyList = ids.stream().map(orderId -> {
      OmsOrderOperateHistory history = new OmsOrderOperateHistory();
      history.setOrderId(orderId);
      history.setCreateTime(new Date());
      history.setOperateMan("后台管理员");
      history.setOrderStatus(4);
      history.setNote("订单关闭:" + note);
      return history;
    }).collect(Collectors.toList());
    orderOperateHistoryDao.saveBatch(historyList);
    return count;
  }

  @Override
  public int updateReceiverInfo(OmsReceiverInfoParam receiverInfoParam) {
    OmsOrder order = new OmsOrder();
    order.setId(receiverInfoParam.getOrderId());
    order.setReceiverName(receiverInfoParam.getReceiverName());
    order.setReceiverPhone(receiverInfoParam.getReceiverPhone());
    order.setReceiverPostCode(receiverInfoParam.getReceiverPostCode());
    order.setReceiverDetailAddress(receiverInfoParam.getReceiverDetailAddress());
    order.setReceiverProvince(receiverInfoParam.getReceiverProvince());
    order.setReceiverCity(receiverInfoParam.getReceiverCity());
    order.setReceiverRegion(receiverInfoParam.getReceiverRegion());
    order.setModifyTime(new Date());
    int count = orderMapper.updateById(order);
    //插入操作记录
    OmsOrderOperateHistory history = new OmsOrderOperateHistory();
    history.setOrderId(receiverInfoParam.getOrderId());
    history.setCreateTime(new Date());
    history.setOperateMan("后台管理员");
    history.setOrderStatus(receiverInfoParam.getStatus());
    history.setNote("修改收货人信息");
    orderOperateHistoryMapper.insert(history);
    return count;
  }

  @Override
  public int updateMoneyInfo(OmsMoneyInfoParam moneyInfoParam) {
    OmsOrder order = new OmsOrder();
    order.setId(moneyInfoParam.getOrderId());
    order.setFreightAmount(moneyInfoParam.getFreightAmount());
    order.setDiscountAmount(moneyInfoParam.getDiscountAmount());
    order.setModifyTime(new Date());
    int count = orderMapper.updateById(order);
    //插入操作记录
    OmsOrderOperateHistory history = new OmsOrderOperateHistory();
    history.setOrderId(moneyInfoParam.getOrderId());
    history.setCreateTime(new Date());
    history.setOperateMan("后台管理员");
    history.setOrderStatus(moneyInfoParam.getStatus());
    history.setNote("修改费用信息");
    orderOperateHistoryMapper.insert(history);
    return count;
  }

  @Override
  public int updateNote(Long id, String note, Integer status) {
    OmsOrder order = new OmsOrder();
    order.setId(id);
    order.setNote(note);
    order.setModifyTime(new Date());
    int count = orderMapper.updateById(order);
    OmsOrderOperateHistory history = new OmsOrderOperateHistory();
    history.setOrderId(id);
    history.setCreateTime(new Date());
    history.setOperateMan("后台管理员");
    history.setOrderStatus(status);
    history.setNote("修改备注信息：" + note);
    orderOperateHistoryMapper.insert(history);
    return count;
  }

  @Override
  public int payOrder(TbThanks tbThanks) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String time = sdf.format(new Date());
    tbThanks.setTime(time);
    tbThanks.setDate(new Date());
        /*TbMember tbMember=tbMemberMapper.selectByPrimaryKey(Long.valueOf(tbThanks.getUserId()));
        if(tbMember!=null){
            tbThanks.setUsername(tbMember.getUsername());
        }
        if(tbThanksMapper.insert(tbThanks)!=1){
            throw new XmallException("保存捐赠支付数据失败");
        }*/

    //设置订单为已付款
    OmsOrder tbOrder = orderMapper.selectById(tbThanks.getOrderId());
    if (tbOrder == null) {
      throw new ApiMallPlusException("订单不存在");
    }
    tbOrder.setStatus(1);
    tbOrder.setPayType(tbThanks.getPayType());
    tbOrder.setPaymentTime(new Date());
    tbOrder.setModifyTime(new Date());
    if (orderMapper.updateById(tbOrder) != 1) {
      throw new ApiMallPlusException("更新订单失败");
    }
    //恢复所有下单商品的锁定库存，扣减真实库存
    OmsOrderItem queryO = new OmsOrderItem();
    queryO.setOrderId(tbThanks.getOrderId());
    List<OmsOrderItem> list = orderItemService.list(new QueryWrapper<>(queryO));

    int count = orderMapper.updateSkuStock(list);
    //发送通知确认邮件
    String tokenName = UUID.randomUUID().toString();
    String token = UUID.randomUUID().toString();

    // emailUtil.sendEmailDealThank(EMAIL_SENDER,"【mallcloud商城】支付待审核处理",tokenName,token,tbThanks);
    return count;
  }

  /**
   * 确认收货
   */
  @Override
  public CommonResult confirmOrder(Long orderId) {
    OmsOrder order = new OmsOrder();
    order.setId(orderId);
    order.setStatus(3);
    int count = orderMapper.updateById(order);
    if (count > 0) {
      return new CommonResult().success("确认收货成功");
    }
    return new CommonResult().failed("确认收货失败");
  }

  @Override
  public void sendDelayMessageCancelOrder(Long orderId) {
    //获取订单超时时间
    OmsOrderSetting orderSetting = orderSettingMapper.selectById(1L);
    long delayTimes = orderSetting.getNormalOrderOvertime() * 60 * 1000;
    //发送延迟消息
    //  cancelOrderSender.sendMessage(orderId, delayTimes);
  }

  /**
   *
   */
  @Override
  public ConfirmOrderResult submitPreview(OrderParam orderParam) {

    String type = orderParam.getType();

    UmsMember currentMember = UserUtils.getCurrentUmsMember();
    List<CartPromotionItem> cartPromotionItemList = new ArrayList<>();//购物车列表
    if ("3".equals(type)) { // 1 商品详情 2 勾选购物车 3全部购物车的商品
      cartPromotionItemList = cartItemService.listPromotion(currentMember.getId(), null);
    }
    if ("1".equals(type)) {
      String cartId = orderParam.getCartId();
      if (org.apache.commons.lang.StringUtils.isBlank(cartId)) {
        throw new ApiMallPlusException("参数为空");
      }
      OmsCartItem omsCartItem = cartItemService.selectById(Long.valueOf(cartId));
      List<OmsCartItem> list = new ArrayList<>();
      list.add(omsCartItem);
      if (!CollectionUtils.isEmpty(list)) {
        cartPromotionItemList = cartItemService.calcCartPromotion(list);
      }
    } else if ("2".equals(type)) {
      String cart_id_list1 = orderParam.getCartIds();
      if (org.apache.commons.lang.StringUtils.isBlank(cart_id_list1)) {
        throw new ApiMallPlusException("参数为空");
      }
      String[] ids1 = cart_id_list1.split(",");
      List<Long> resultList = new ArrayList<>(ids1.length);
      for (String s : ids1) {
        resultList.add(Long.valueOf(s));
      }
      cartPromotionItemList = cartItemService.listPromotion(currentMember.getId(), resultList);
    }
    ConfirmOrderResult result = new ConfirmOrderResult();
    //获取购物车信息

    result.setCartPromotionItemList(cartPromotionItemList);
    //获取用户收货地址列表
    UmsMemberReceiveAddress queryU = new UmsMemberReceiveAddress();
    queryU.setMemberId(currentMember.getId());
    List<UmsMemberReceiveAddress> memberReceiveAddressList = addressService.list(new QueryWrapper<>(queryU));
    result.setMemberReceiveAddressList(memberReceiveAddressList);
    UmsMemberReceiveAddress address = addressService.getDefaultItem();
    //获取用户可用优惠券列表
    List<SmsCouponHistoryDetail> couponHistoryDetailList = couponService.getCouponHistoryDetailByCart(cartPromotionItemList, 1);
    result.setCouponHistoryDetailList(couponHistoryDetailList);
    //获取用户积分
    result.setMemberIntegration(currentMember.getIntegration());
    //获取积分使用规则
    UmsIntegrationConsumeSetting integrationConsumeSetting = integrationConsumeSettingMapper.selectById(1L);
    result.setIntegrationConsumeSetting(integrationConsumeSetting);
    //计算总金额、活动优惠、应付金额
    ConfirmOrderResult.CalcAmount calcAmount = calcCartAmount(cartPromotionItemList);
    result.setCalcAmount(calcAmount);
    result.setAddress(address);
    return result;

  }

  @Override
  public ConfirmOrderResult generateConfirmOrder() {
    ConfirmOrderResult result = new ConfirmOrderResult();
    //获取购物车信息
    UmsMember currentMember = UserUtils.getCurrentUmsMember();
    List<CartPromotionItem> cartPromotionItemList = cartItemService.listPromotion(currentMember.getId(), null);
    result.setCartPromotionItemList(cartPromotionItemList);
    //获取用户收货地址列表
    UmsMemberReceiveAddress queryU = new UmsMemberReceiveAddress();
    queryU.setMemberId(currentMember.getId());
    List<UmsMemberReceiveAddress> memberReceiveAddressList = addressService.list(new QueryWrapper<>(queryU));
    result.setMemberReceiveAddressList(memberReceiveAddressList);
    //获取用户可用优惠券列表
    List<SmsCouponHistoryDetail> couponHistoryDetailList = couponService.getCouponHistoryDetailByCart(cartPromotionItemList, 1);
    result.setCouponHistoryDetailList(couponHistoryDetailList);
    //获取用户积分
    result.setMemberIntegration(currentMember.getIntegration());
    //获取积分使用规则
    UmsIntegrationConsumeSetting integrationConsumeSetting = integrationConsumeSettingMapper.selectById(1L);
    result.setIntegrationConsumeSetting(integrationConsumeSetting);
    //计算总金额、活动优惠、应付金额
    ConfirmOrderResult.CalcAmount calcAmount = calcCartAmount(cartPromotionItemList);
    result.setCalcAmount(calcAmount);
    return result;
  }

  @Override
  //@Transactional
  public CommonResult generateOrder(OrderParam orderParam) {
    String type = orderParam.getType();
    UmsMember currentMember = UserUtils.getCurrentUmsMember();
    //购物车中促销信息的封装
    List<CartPromotionItem> cartPromotionItemList = new ArrayList<>();
    // 1 商品详情 2 勾选购物车 3全部购物车的商品
    if (MagicConstant.CART_PRODUCT_TYPE_DETAIL.equals(type)) {
      Long cartId = Long.valueOf(orderParam.getCartId());
      OmsCartItem omsCartItem = cartItemService.selectById(cartId);
      if (omsCartItem != null) {
        List<OmsCartItem> list = new ArrayList<>();
        list.add(omsCartItem);
        cartPromotionItemList = cartItemService.calcCartPromotion(list);
      }
    } else if (MagicConstant.CART_PRODUCT_TYPE_CHECKED.equals(type)) {
      List<Long> resultList = Arrays.stream(orderParam.getCartIds().split(",")).map(Long::valueOf).collect(Collectors.toList());
      cartPromotionItemList = cartItemService.listPromotion(currentMember.getId(), resultList);
    } else if (MagicConstant.CART_PRODUCT_TYPE_ALLCHECKED.equals(type)) {
      cartPromotionItemList = cartItemService.listPromotion(currentMember.getId(), null);
    }

    return splitOrder(cartPromotionItemList, orderParam);
  }


  @Override
  public CommonResult paySuccess(Long orderId) {
    //修改订单支付状态
    OmsOrder order = new OmsOrder();
    order.setId(orderId);
    order.setStatus(1);
    order.setPaymentTime(new Date());
    orderService.updateById(order);
    //恢复所有下单商品的锁定库存，扣减真实库存
    OmsOrderItem queryO = new OmsOrderItem();
    queryO.setOrderId(orderId);
    List<OmsOrderItem> list = orderItemService.list(new QueryWrapper<>(queryO));
    int count = orderMapper.updateSkuStock(list);
    return new CommonResult().success("支付成功", count);
  }

  /**
   * 推送消息
   */
  public void push(UmsMember umsMember, OmsOrder order, String page, String formId, String name) {
    log.info("发送模版消息：userId=" + umsMember.getId() + ",orderId=" + order.getId() + ",formId=" + formId);
    if (StringUtils.isEmpty(formId)) {
      log.error("发送模版消息：userId=" + umsMember.getId() + ",orderId=" + order.getId() + ",formId=" + formId);
    }
    String accessToken = null;
    try {
      accessToken = wechatApiService.getAccessToken();

      String templateId = wxAppletProperties.getTemplateId();
      Map<String, TemplateData> param = new HashMap<String, TemplateData>();
      param.put("keyword1", new TemplateData(DateUtils.format(order.getCreateTime(), "yyyy-MM-dd"), "#EE0000"));

      param.put("keyword2", new TemplateData(name, "#EE0000"));
      param.put("keyword3", new TemplateData(order.getOrderSn(), "#EE0000"));
      param.put("keyword3", new TemplateData(order.getPayAmount() + "", "#EE0000"));

      JSONObject jsonObject = JSONObject.fromObject(param);
      //调用发送微信消息给用户的接口    ********这里写自己在微信公众平台拿到的模板ID
      WX_TemplateMsgUtil.sendWechatMsgToUser(umsMember.getWeixinOpenid(), templateId, page + "?id=" + order.getId(), jsonObject, accessToken);

    } catch (Exception e) {
      e.printStackTrace();
    }


  }

  @Override
  @Transactional
  public CommonResult cancelTimeOutOrder() {
    OmsOrderSetting orderSetting = orderSettingMapper.selectById(1L);
    //查询超时、未支付的订单及订单详情
    List<OmsOrderDetail> timeOutOrders = orderMapper.getTimeOutOrders(orderSetting.getNormalOrderOvertime());
    if (CollectionUtils.isEmpty(timeOutOrders)) {
      return new CommonResult().failed("暂无超时订单");
    }
    //修改订单状态为交易取消
    List<Long> ids = new ArrayList<>();
    for (OmsOrderDetail timeOutOrder : timeOutOrders) {
      ids.add(timeOutOrder.getId());
    }
    orderMapper.updateOrderStatus(ids, 4);
    for (OmsOrderDetail timeOutOrder : timeOutOrders) {
      //解除订单商品库存锁定
      orderMapper.releaseSkuStockLock(timeOutOrder.getOrderItemList());
      //修改优惠券使用状态
      updateCouponStatus(timeOutOrder.getCouponId(), timeOutOrder.getMemberId(), 0);
      //返还使用积分
      if (timeOutOrder.getUseIntegration() != null) {
        UmsMember member = memberService.getById(timeOutOrder.getMemberId());
        memberService.updateIntegration(timeOutOrder.getMemberId(), member.getIntegration() + timeOutOrder.getUseIntegration());
      }
    }
    return new CommonResult().success(null);
  }

  @Override
  @Transactional
  public Object cancelOrder(Long orderId) {
    //查询为付款的取消订单
    OmsOrder cancelOrder = orderMapper.selectById(orderId);
    if (cancelOrder != null) {
      //修改订单状态为取消
      cancelOrder.setStatus(MagicConstant.ORDER_STATUS_YET_SHUTDOWN);
      orderMapper.updateById(cancelOrder);
      OmsOrderItem queryO = new OmsOrderItem();
      queryO.setOrderId(orderId);
      List<OmsOrderItem> list = orderItemService.list(new QueryWrapper<>(queryO));
      //解除订单商品库存锁定
      orderMapper.releaseSkuStockLock(list);
      //修改优惠券使用状态
      updateCouponStatus(cancelOrder.getCouponId(), cancelOrder.getMemberId(), 0);
      //返还使用积分
      if (cancelOrder.getUseIntegration() != null) {
        UmsMember member = memberService.getById(cancelOrder.getMemberId());
        memberService.updateIntegration(cancelOrder.getMemberId(), member.getIntegration() + cancelOrder.getUseIntegration());
      }
      return new CommonResult().success("取消订单成功");
    } else {
      return new CommonResult().failed("根据订单id查询不到订单");
    }
  }

  @Override
  public Object preSingelOrder(GroupAndOrderVo orderParam) {
    ConfirmOrderResult result = new ConfirmOrderResult();
    result.setGroupAndOrderVo(orderParam);
    PmsProduct goods = productService.getById(orderParam.getGoodsId());
    result.setGoods(goods);
    //获取用户收货地址列表
    List<UmsMemberReceiveAddress> memberReceiveAddressList = addressService.list(new QueryWrapper<>());
    result.setMemberReceiveAddressList(memberReceiveAddressList);
    UmsMemberReceiveAddress address = addressService.getDefaultItem();

    result.setAddress(address);
    return result;
  }

  /**
   * 推送消息
   */
  public void push(GroupAndOrderVo umsMember, OmsOrder order, String page, String formId) {
    log.info("发送模版消息：userId=" + umsMember.getMemberId() + ",orderId=" + order.getId() + ",formId=" + formId);
    if (StringUtils.isEmpty(formId)) {
      log.error("发送模版消息：userId=" + umsMember.getMemberId() + ",orderId=" + order.getId() + ",formId=" + formId);
    }
    String accessToken = null;
    try {
      accessToken = wechatApiService.getAccessToken();

      String templateId = wxAppletProperties.getTemplateId();
      Map<String, TemplateData> param = new HashMap<String, TemplateData>();
      param.put("keyword1", new TemplateData(DateUtils.format(order.getCreateTime(), "yyyy-MM-dd"), "#EE0000"));

      param.put("keyword2", new TemplateData(order.getGoodsName(), "#EE0000"));
      param.put("keyword3", new TemplateData(order.getOrderSn(), "#EE0000"));
      param.put("keyword3", new TemplateData(order.getPayAmount() + "", "#EE0000"));

      JSONObject jsonObject = JSONObject.fromObject(param);
      //调用发送微信消息给用户的接口    ********这里写自己在微信公众平台拿到的模板ID
      WX_TemplateMsgUtil.sendWechatMsgToUser(umsMember.getWxid(), templateId, page + "?id=" + order.getId(), jsonObject, accessToken);

    } catch (Exception e) {
      e.printStackTrace();
    }


  }

  @Transactional
  @Override
  public Object generateSingleOrder(GroupAndOrderVo orderParam, UmsMember member) {
    String type = orderParam.getType();
    orderParam.setMemberId(member.getId());
    orderParam.setName(member.getIcon());
    PmsProduct goods = productService.getById(orderParam.getGoodsId());

    if (goods.getStock() < 0) {
      return new CommonResult().failed("库存不足，无法下单");
    }

    //根据商品合计、运费、活动优惠、优惠券、积分计算应付金额
    OmsOrder order = new OmsOrder();
    order.setDiscountAmount(new BigDecimal(0));
    order.setTotalAmount(goods.getPrice());
    order.setPayAmount(goods.getPrice());
    order.setFreightAmount(new BigDecimal(0));
    order.setPromotionAmount(new BigDecimal(0));

    order.setSupplyId(goods.getSupplyId());
    order.setCouponAmount(new BigDecimal(0));

    order.setIntegration(0);
    order.setIntegrationAmount(new BigDecimal(0));

    order.setGoodsId(goods.getId());
    order.setGoodsName(order.getGoodsName());
    //转化为订单信息并插入数据库
    order.setMemberId(orderParam.getMemberId());
    order.setCreateTime(new Date());
    order.setMemberUsername(member.getUsername());
    //支付方式：0->未支付；1->支付宝；2->微信
    order.setPayType(orderParam.getPayType());
    //订单来源：0->PC订单；1->app订单
    order.setSourceType(orderParam.getSourceType());
    //订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单
    order.setStatus(0);
    //订单类型：0->正常订单；1->秒杀订单
    order.setOrderType(orderParam.getOrderType());
    //收货人信息：姓名、电话、邮编、地址
    UmsMemberReceiveAddress address = addressService.getById(orderParam.getAddressId());
    order.setReceiverName(address.getName());
    order.setReceiverPhone(address.getPhoneNumber());
    order.setReceiverPostCode(address.getPostCode());
    order.setReceiverProvince(address.getProvince());
    order.setReceiverCity(address.getCity());
    order.setReceiverRegion(address.getRegion());
    order.setReceiverDetailAddress(address.getDetailAddress());
    //0->未确认；1->已确认
    order.setConfirmStatus(0);
    order.setDeleteStatus(0);
    //计算赠送积分
    order.setIntegration(0);
    //计算赠送成长值
    order.setGrowth(0);
    //生成订单号
    order.setOrderSn(String.valueOf(IdGeneratorUtil.getIdGeneratorUtil().nextId()));
    SmsGroup group = groupService.getById(orderParam.getGroupId());
    if (group != null) {
      order.setPayAmount(group.getGroupPrice());
    }
    // TODO: 2018/9/3 bill_*,delivery_*
    //插入order表和order_item表
    this.save(order);

    if ("0".equals(type)) { // 0 下单 1 拼团 2 发起拼团

    }
    if ("1".equals(type)) {
      SmsGroupMember sm = new SmsGroupMember();
      sm.setGroupId(orderParam.getGroupId());
      sm.setMemberId(orderParam.getMemberId());
      List<SmsGroupMember> smsGroupMemberList = groupMemberService.list(new QueryWrapper<>(sm));
      if (smsGroupMemberList != null && smsGroupMemberList.size() > 0) {
        return new CommonResult().failed("你已经参加此拼团");
      }

      Date endTime = DateUtils.convertStringToDate(DateUtils.addHours(group.getEndTime(), group.getHours()));
      Long nowT = System.currentTimeMillis();
      if (nowT > group.getStartTime().getTime() && nowT < endTime.getTime()) {
        if (orderParam.getMemberId() == null || orderParam.getMemberId() < 1) {
          orderParam.setMemberId(orderParam.getMainId());
        }
        orderParam.setStatus(2);
        orderParam.setCreateTime(new Date());
        orderParam.setOrderId(order.getId());
        groupMemberService.save(orderParam);
      } else {
        return new CommonResult().failed("活动已经结束");
      }
    } else if ("2".equals(type)) {
      group = groupService.getById(orderParam.getGroupId());
      Date endTime = DateUtils.convertStringToDate(DateUtils.addHours(group.getEndTime(), group.getHours()));
      Long nowT = System.currentTimeMillis();
      if (nowT > group.getStartTime().getTime() && nowT < endTime.getTime()) {
        if (orderParam.getMemberId() == null || orderParam.getMemberId() < 1) {
          orderParam.setMemberId(orderParam.getMainId());
        }
        orderParam.setStatus(2);
        orderParam.setCreateTime(new Date());
        orderParam.setOrderId(order.getId());
        orderParam.setMainId(orderParam.getMemberId());
        groupMemberService.save(orderParam);
      } else {
        return new CommonResult().failed("活动已经结束");
      }

    }
    Map<String, Object> result = new HashMap<>();
    result.put("order", order);

    if (orderParam.getSourceType() == 1) {
      push(orderParam, order, orderParam.getPage(), orderParam.getFormId());
    }
    return new CommonResult().success("下单成功", result);
  }


  /**
   * 生成18位订单编号:8位日期+2位平台号码+2位支付方式+6位以上自增id
   */
  private String generateOrderSn(OmsOrder order) {
    StringBuilder sb = new StringBuilder();
    String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
    sb.append(date);
    sb.append(String.format("%02d", order.getSourceType()));
    sb.append(String.format("%02d", order.getPayType()));
    sb.append(order.getMemberId());
    return sb.toString();
  }

  /**
   * 计算总金额
   */
  private BigDecimal calcTotalAmount(List<OmsOrderItem> orderItemList) {
    BigDecimal totalAmount = new BigDecimal("0");
    for (OmsOrderItem item : orderItemList) {
      totalAmount = totalAmount.add(item.getProductPrice().multiply(new BigDecimal(item.getProductQuantity())));
    }
    return totalAmount;
  }

  /**
   * 锁定下单商品的所有库存
   */
  private void lockStock(List<CartPromotionItem> cartPromotionItemList) {
    for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
      PmsSkuStock skuStock = iPmsSkuStockService.getById(cartPromotionItem.getProductSkuId());
      skuStock.setLockStock(skuStock.getLockStock() + cartPromotionItem.getQuantity());
      iPmsSkuStockService.updateById(skuStock);
    }
  }

  /**
   * 判断下单商品是否都有库存
   */
  private boolean hasStock(List<CartPromotionItem> cartPromotionItemList) {
    for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
      if (cartPromotionItem.getRealStock() <= 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * 计算购物车中商品的价格
   */
  private ConfirmOrderResult.CalcAmount calcCartAmount(List<CartPromotionItem> cartPromotionItemList) {
    ConfirmOrderResult.CalcAmount calcAmount = new ConfirmOrderResult.CalcAmount();
    calcAmount.setFreightAmount(new BigDecimal(0));
    BigDecimal totalAmount = new BigDecimal("0");
    BigDecimal promotionAmount = new BigDecimal("0");
    for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
      totalAmount = totalAmount.add(cartPromotionItem.getPrice().multiply(new BigDecimal(cartPromotionItem.getQuantity())));
      promotionAmount = promotionAmount.add(cartPromotionItem.getReduceAmount().multiply(new BigDecimal(cartPromotionItem.getQuantity())));
    }
    calcAmount.setTotalAmount(totalAmount);
    calcAmount.setPromotionAmount(promotionAmount);
    calcAmount.setPayAmount(totalAmount.subtract(promotionAmount));
    return calcAmount;
  }


  /**
   * 删除下单商品的购物车信息
   */
  private void deleteCartItemList(List<CartPromotionItem> cartPromotionItemList, UmsMember currentMember) {
    List<Long> ids = new ArrayList<>();
    for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
      ids.add(cartPromotionItem.getId());
    }
    cartItemService.delete(currentMember.getId(), ids);
  }

  /**
   * 计算该订单赠送的成长值
   */
  private Integer calcGiftGrowth(List<OmsOrderItem> orderItemList) {
    Integer sum = 0;
    for (OmsOrderItem orderItem : orderItemList) {
      sum = sum + orderItem.getGiftGrowth() * orderItem.getProductQuantity();
    }
    return sum;
  }

  /**
   * 计算该订单赠送的积分
   */
  private Integer calcGifIntegration(List<OmsOrderItem> orderItemList) {
    int sum = 0;
    for (OmsOrderItem orderItem : orderItemList) {
      sum += orderItem.getGiftIntegration() * orderItem.getProductQuantity();
    }
    return sum;
  }

  /**
   * 将优惠券信息更改为指定状态
   *
   * @param couponId 优惠券id
   * @param memberId 会员id
   * @param useStatus 0->未使用；1->已使用
   */
  private void updateCouponStatus(Long couponId, Long memberId, Integer useStatus) {
    if (couponId == null) {
      return;
    }
    //查询第一张优惠券
    SmsCouponHistory queryC = new SmsCouponHistory();
    queryC.setCouponId(couponId);
    queryC.setMemberId(memberId);
    if (Objects.equals(useStatus, MagicConstant.COUPON_USE_STATUS_4_NO)) {
      queryC.setUseStatus(MagicConstant.COUPON_USE_STATUS_4_YES);
    } else {
      queryC.setUseStatus(MagicConstant.COUPON_USE_STATUS_4_NO);
    }
    List<SmsCouponHistory> couponHistoryList = couponHistoryService.list(new QueryWrapper<>(queryC));
    if (!CollectionUtils.isEmpty(couponHistoryList)) {
      SmsCouponHistory couponHistory = couponHistoryList.get(0);
      couponHistory.setUseTime(new Date());
      couponHistory.setUseStatus(useStatus);
      couponHistoryService.updateById(couponHistory);
    }
  }

  private void handleRealAmount(List<OmsOrderItem> orderItemList) {
    for (OmsOrderItem orderItem : orderItemList) {
      //原价-促销价格-优惠券抵扣-积分抵扣
      BigDecimal realAmount = orderItem.getProductPrice()
        .subtract(orderItem.getPromotionAmount())
        .subtract(orderItem.getCouponAmount() == null ? BigDecimal.ZERO : orderItem.getCouponAmount())
        .subtract(orderItem.getIntegrationAmount());
      orderItem.setRealAmount(realAmount);
    }
  }

  /**
   * 获取订单促销信息
   */
  private String getOrderPromotionInfo(List<OmsOrderItem> orderItemList) {
    StringBuilder sb = new StringBuilder();
    for (OmsOrderItem orderItem : orderItemList) {
      sb.append(orderItem.getPromotionName());
      sb.append(",");
    }
    String result = sb.toString();
    if (result.endsWith(",")) {
      result = result.substring(0, result.length() - 1);
    }
    return result;
  }

  /**
   * 计算订单应付金额
   */
  private BigDecimal calcPayAmount(OmsOrder order) {
    //总金额+运费-促销优惠-优惠券优惠-积分抵扣
    BigDecimal payAmount = order.getTotalAmount()
      .add(order.getFreightAmount())
      .subtract(order.getPromotionAmount())
      .subtract(order.getCouponAmount())
      .subtract(order.getIntegrationAmount());
    return payAmount;
  }

  /**
   * 计算订单优惠券金额
   */
  private BigDecimal calcIntegrationAmount(List<OmsOrderItem> orderItemList) {
    BigDecimal integrationAmount = new BigDecimal(0);
    for (OmsOrderItem orderItem : orderItemList) {
      if (orderItem.getIntegrationAmount() != null) {
        integrationAmount = integrationAmount
          .add(orderItem.getIntegrationAmount().multiply(new BigDecimal(orderItem.getProductQuantity())));
      }
    }
    return integrationAmount;
  }

  /**
   * 计算订单优惠券金额
   */
  private BigDecimal calcCouponAmount(List<OmsOrderItem> orderItemList) {
    BigDecimal couponAmount = new BigDecimal(0);
    for (OmsOrderItem orderItem : orderItemList) {
      if (orderItem.getCouponAmount() != null) {
        couponAmount = couponAmount.add(orderItem.getCouponAmount().multiply(new BigDecimal(orderItem.getProductQuantity())));
      }
    }
    return couponAmount;
  }

  /**
   * 计算订单活动优惠
   */
  private BigDecimal calcPromotionAmount(List<OmsOrderItem> orderItemList) {
    BigDecimal promotionAmount = new BigDecimal(0);
    for (OmsOrderItem orderItem : orderItemList) {
      if (orderItem.getPromotionAmount() != null) {
        promotionAmount = promotionAmount.add(orderItem.getPromotionAmount().multiply(new BigDecimal(orderItem.getProductQuantity())));
      }
    }
    return promotionAmount;
  }

  /**
   * 获取可用积分抵扣金额
   *
   * @param useIntegration 使用的积分数量
   * @param totalAmount 订单总金额
   * @param currentMember 使用的用户
   * @param hasCoupon 是否已经使用优惠券
   */
  private BigDecimal getUseIntegrationAmount(Integer useIntegration, BigDecimal totalAmount, UmsMember currentMember, boolean hasCoupon) {
    BigDecimal zeroAmount = new BigDecimal(0);
    //判断用户是否有这么多积分
    if (useIntegration.compareTo(currentMember.getIntegration()) > 0) {
      return zeroAmount;
    }
    //根据积分使用规则判断使用可用
    //是否可用于优惠券共用
    UmsIntegrationConsumeSetting integrationConsumeSetting = integrationConsumeSettingMapper.selectById(1L);
    if (hasCoupon && integrationConsumeSetting.getCouponStatus().equals(0)) {
      //不可与优惠券共用
      return zeroAmount;
    }
    //是否达到最低使用积分门槛
    if (useIntegration.compareTo(integrationConsumeSetting.getUseUnit()) < 0) {
      return zeroAmount;
    }
    //是否超过订单抵用最高百分比
    BigDecimal integrationAmount = new BigDecimal(useIntegration)
      .divide(new BigDecimal(integrationConsumeSetting.getUseUnit()), 2, RoundingMode.HALF_EVEN);
    BigDecimal maxPercent = new BigDecimal(integrationConsumeSetting.getMaxPercentPerOrder())
      .divide(new BigDecimal(100), 2, RoundingMode.HALF_EVEN);
    if (integrationAmount.compareTo(totalAmount.multiply(maxPercent)) > 0) {
      return zeroAmount;
    }
    return integrationAmount;
  }

  /**
   * 对优惠券优惠进行处理
   *
   * @param orderItemList order_item列表
   * @param couponHistoryDetail 可用优惠券详情
   */
  private void handleCouponAmount(List<OmsOrderItem> orderItemList, SmsCouponHistoryDetail couponHistoryDetail) {
    SmsCoupon coupon = couponHistoryDetail.getCoupon();
    if (Objects.equals(coupon.getUseType(), MagicConstant.COUPON_USE_TYPE_ALL)) {
      //全场通用
      calcPerCouponAmount(orderItemList, coupon);
    } else if (Objects.equals(coupon.getUseType(), MagicConstant.COUPON_USE_TYPE_PRODUCT_CATEGORY)) {
      //指定分类
      List<OmsOrderItem> couponOrderItemList = getCouponOrderItemByRelation(couponHistoryDetail, orderItemList, 0);
      calcPerCouponAmount(couponOrderItemList, coupon);
    } else if (Objects.equals(coupon.getUseType(), MagicConstant.COUPON_USE_TYPE_PRODUCT)) {
      //指定商品
      List<OmsOrderItem> couponOrderItemList = getCouponOrderItemByRelation(couponHistoryDetail, orderItemList, 1);
      calcPerCouponAmount(couponOrderItemList, coupon);
    } else {
      throw new RuntimeException("不存在该商品分类");
    }
  }

  /**
   * 对每个下单商品进行优惠券金额分摊的计算
   *
   * @param orderItemList 可用优惠券的下单商品商品
   */
  private void calcPerCouponAmount(List<OmsOrderItem> orderItemList, SmsCoupon coupon) {
    BigDecimal totalAmount = calcTotalAmount(orderItemList);
    for (OmsOrderItem orderItem : orderItemList) {
      //(商品价格/可用商品总价)*优惠券面额
      BigDecimal couponAmount = orderItem.getProductPrice().divide(totalAmount, 3, RoundingMode.HALF_EVEN).multiply(coupon.getAmount());
      orderItem.setCouponAmount(couponAmount);
    }
  }

  /**
   * 获取与优惠券有关系的下单商品
   *
   * @param couponHistoryDetail 优惠券详情
   * @param orderItemList 下单商品
   * @param type 使用关系类型：0->相关分类；1->指定商品
   */
  private List<OmsOrderItem> getCouponOrderItemByRelation(SmsCouponHistoryDetail couponHistoryDetail, List<OmsOrderItem> orderItemList,
    int type) {
    List<OmsOrderItem> result = new ArrayList<>();
    if (type == 0) {
      List<Long> categoryIdList = couponHistoryDetail.getCategoryRelationList().stream()
        .map(SmsCouponProductCategoryRelation::getProductCategoryId).collect(Collectors.toList());
      for (OmsOrderItem orderItem : orderItemList) {
        if (categoryIdList.contains(orderItem.getProductCategoryId())) {
          result.add(orderItem);
        } else {
          orderItem.setCouponAmount(new BigDecimal(0));
        }
      }
    } else if (type == 1) {
      List<Long> productIdList = couponHistoryDetail.getProductRelationList().stream()
        .map(SmsCouponProductRelation::getProductId).collect(Collectors.toList());
      for (OmsOrderItem orderItem : orderItemList) {
        if (productIdList.contains(orderItem.getProductId())) {
          result.add(orderItem);
        } else {
          orderItem.setCouponAmount(new BigDecimal(0));
        }
      }
    }
    return result;
  }

  /**
   * 获取该用户可以使用的优惠券
   *
   * @param cartPromotionItemList 购物车优惠列表
   * @param couponId 使用优惠券id
   */
  private SmsCouponHistoryDetail getUseCoupon(List<CartPromotionItem> cartPromotionItemList, Long couponId) {
    List<SmsCouponHistoryDetail> couponHistoryDetailList = couponService.getCouponHistoryDetailByCart(cartPromotionItemList, 1);
    for (SmsCouponHistoryDetail couponHistoryDetail : couponHistoryDetailList) {
      if (couponHistoryDetail.getCoupon().getId().equals(couponId)) {
        return couponHistoryDetail;
      }
    }
    return null;
  }

  /**
   * 进行拆单
   */
  private CommonResult splitOrder(List<CartPromotionItem> splitCartPromotionItemList, OrderParam orderParam) {
    Map<String, List<CartPromotionItem>> brankMap = splitCartPromotionItemList.stream()
      .collect(Collectors.groupingBy(CartPromotionItem::getProductBrand));
    UmsMember currentMember = UserUtils.getCurrentUmsMember();
    Long supplyId = IdGeneratorUtil.getIdGeneratorUtil().nextId();
    for (Entry<String, List<CartPromotionItem>> entry : brankMap.entrySet()) {
      List<CartPromotionItem> cartPromotionItemList = entry.getValue();
      String name = cartPromotionItemList.get(0).getProductName();
      //组装订单列表,相当于一个品牌一个订单列表
      List<OmsOrderItem> orderItemList = assemblyOrderItems(cartPromotionItemList);
      //判断购物车中商品是否都有库存
      if (!hasStock(cartPromotionItemList)) {
        return new CommonResult().failed("库存不足，无法下单");
      }
      //使用优惠券
      String msg = dealCoupon(orderParam, orderItemList, cartPromotionItemList, currentMember.getId());
      if (StringUtils.isNotEmpty(msg)) {
        return new CommonResult().failed(msg);
      }
      //使用积分
      String integrationMsg = dealUseIntegration(orderParam, orderItemList, currentMember);
      if (StringUtils.isNotEmpty(integrationMsg)) {
        return new CommonResult().failed(integrationMsg);
      }
      //计算order_item的实付金额
      handleRealAmount(orderItemList);
      //进行库存锁定
      lockStock(cartPromotionItemList);
      //生成并保存订单
      OmsOrder order = generateAndSaveOrder(orderItemList, orderParam, currentMember, supplyId);
      //根据商品合计、运费、活动优惠、优惠券、积分计算应付金额
      for (OmsOrderItem orderItem : orderItemList) {
        orderItem.setOrderId(order.getId());
        orderItem.setOrderSn(order.getOrderSn());
      }
      orderItemService.saveBatch(orderItemList);
      //如使用积分需要扣除积分
      if (orderParam.getUseIntegration() != null) {
        order.setUseIntegration(orderParam.getUseIntegration());
        memberService.updateIntegration(currentMember.getId(), currentMember.getIntegration() - orderParam.getUseIntegration());
      }
      //删除购物车中的下单商品
      deleteCartItemList(cartPromotionItemList, currentMember);

      String platform = orderParam.getPlatform();
      if ("1".equals(platform)) {
        //推过微信的推送接口想微信用户发送消息模板
        push(currentMember, order, orderParam.getPage(), orderParam.getFormId(), name);
      }
    }
    return new CommonResult().success("下单成功", supplyId.toString());
  }

  /**
   * 生成并保存订单
   */
  private OmsOrder generateAndSaveOrder(List<OmsOrderItem> orderItemList, OrderParam orderParam, UmsMember currentMember, Long supplyId) {
    OmsOrder order = new OmsOrder();
    order.setDiscountAmount(new BigDecimal(0));
    order.setTotalAmount(calcTotalAmount(orderItemList));
    order.setFreightAmount(new BigDecimal(0));
    order.setPromotionAmount(calcPromotionAmount(orderItemList));
    order.setPromotionInfo(getOrderPromotionInfo(orderItemList));
    if (orderParam.getCouponId() == null) {
      order.setCouponAmount(new BigDecimal(0));
    } else {
      order.setCouponId(orderParam.getCouponId());
      order.setCouponAmount(calcCouponAmount(orderItemList));
    }
    if (orderParam.getUseIntegration() == null) {
      order.setIntegration(0);
      order.setIntegrationAmount(new BigDecimal(0));
    } else {
      order.setIntegration(orderParam.getUseIntegration());
      order.setIntegrationAmount(calcIntegrationAmount(orderItemList));
    }
    order.setPayAmount(calcPayAmount(order));
    //转化为订单信息并插入数据库
    order.setMemberId(currentMember.getId());
    order.setCreateTime(new Date());
    order.setMemberUsername(currentMember.getUsername());
    //支付方式：0->未支付；1->支付宝；2->微信
    order.setPayType(orderParam.getPayType());
    //订单来源：0->PC订单；1->app订单
    order.setSourceType(MagicConstant.SOURCE_TYPE_APP);
    //订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单
    order.setStatus(MagicConstant.ORDER_STATUS_WAIT_PAY);
    //订单类型：0->正常订单；1->秒杀订单
    order.setOrderType(MagicConstant.ORDER_TYPE_NORMAL);
    //收货人信息：姓名、电话、邮编、地址
    UmsMemberReceiveAddress address = addressService.getById(orderParam.getAddressId());
    order.setReceiverName(address.getName());
    order.setReceiverPhone(address.getPhoneNumber());
    order.setReceiverPostCode(address.getPostCode());
    order.setReceiverProvince(address.getProvince());
    order.setReceiverCity(address.getCity());
    order.setReceiverRegion(address.getRegion());
    order.setReceiverDetailAddress(address.getDetailAddress());
    //0->未确认；1->已确认
    order.setConfirmStatus(MagicConstant.CONFIRM_NOT);
    order.setDeleteStatus(MagicConstant.DELETE_NOT);
    //计算赠送积分
    order.setIntegration(calcGifIntegration(orderItemList));
    //计算赠送成长值
    order.setGrowth(calcGiftGrowth(orderItemList));
    //生成订单号
    order.setOrderSn(String.valueOf(IdGeneratorUtil.getIdGeneratorUtil().nextId()));
    order.setSupplyId(supplyId);
    // TODO: 2018/9/3 bill_*,delivery_*
    //插入order表和order_item表
    orderService.save(order);
    return order;
  }

  /**
   * 使用积分
   */
  private String dealUseIntegration(OrderParam orderParam, List<OmsOrderItem> orderItemList, UmsMember currentMember) {
    //判断是否使用积分
    if (orderParam.getUseIntegration() == null) {
      //不使用积分
      for (OmsOrderItem orderItem : orderItemList) {
        orderItem.setIntegrationAmount(new BigDecimal(0));
      }
    } else {
      //使用积分
      BigDecimal totalAmount = calcTotalAmount(orderItemList);
      BigDecimal integrationAmount = getUseIntegrationAmount(orderParam.getUseIntegration(), totalAmount, currentMember,
        orderParam.getCouponId() != null);
      if (integrationAmount.compareTo(new BigDecimal(0)) == 0) {
        return "积分不可用";
      } else {
        //可用情况下分摊到可用商品中
        for (OmsOrderItem orderItem : orderItemList) {
          BigDecimal perAmount = orderItem.getProductPrice().divide(totalAmount, 3, RoundingMode.HALF_EVEN).multiply(integrationAmount);
          orderItem.setIntegrationAmount(perAmount);
        }
      }
    }
    return null;
  }

  /**
   * 使用优惠券
   */
  private String dealCoupon(OrderParam orderParam, List<OmsOrderItem> orderItemList, List<CartPromotionItem> cartPromotionItemList,
    Long memberId) {
    //判断使用使用了优惠券
    if (orderParam.getCouponId() == null) {
      //不用优惠券
      for (OmsOrderItem orderItem : orderItemList) {
        orderItem.setCouponAmount(new BigDecimal(0));
      }
    } else {
      //使用优惠券
      SmsCouponHistoryDetail couponHistoryDetail = getUseCoupon(cartPromotionItemList, orderParam.getCouponId());
      if (couponHistoryDetail != null) {
        //领券的时间
        Date useDate = couponHistoryDetail.getCreateTime();
        Date delayDate = DateUtils.delayDate(useDate,
          //领券的时间+有效时间
          Long.valueOf(couponHistoryDetail.getEffectDay() == null ? 0 : couponHistoryDetail.getEffectDay()));
        if (couponHistoryDetail == null || delayDate.before(new Date())) {
          return "该优惠券不可用";
        }
        //对下单商品的优惠券进行处理
        handleCouponAmount(orderItemList, couponHistoryDetail);
        updateCouponStatus(orderParam.getCouponId(), memberId, MagicConstant.COUPON_USE_STATUS_4_YES);
      }
    }
    return null;
  }

  /**
   * 组装订单列表
   */
  private List<OmsOrderItem> assemblyOrderItems(List<CartPromotionItem> cartPromotionItemList) {
    return cartPromotionItemList.stream().map(cartPromotionItem -> {
      OmsOrderItem orderItem = new OmsOrderItem();
      orderItem.setProductAttr(cartPromotionItem.getProductAttr());
      orderItem.setProductId(cartPromotionItem.getProductId());
      orderItem.setProductName(cartPromotionItem.getProductName());
      orderItem.setProductPic(cartPromotionItem.getProductPic());
      orderItem.setProductAttr(cartPromotionItem.getProductAttr());
      orderItem.setProductBrand(cartPromotionItem.getProductBrand());
      orderItem.setProductSn(cartPromotionItem.getProductSn());
      orderItem.setProductPrice(cartPromotionItem.getPrice());
      orderItem.setProductQuantity(cartPromotionItem.getQuantity());
      orderItem.setProductSkuId(cartPromotionItem.getProductSkuId());
      orderItem.setProductSkuCode(cartPromotionItem.getProductSkuCode());
      orderItem.setProductCategoryId(cartPromotionItem.getProductCategoryId());
      orderItem.setPromotionAmount(cartPromotionItem.getReduceAmount());
      orderItem.setPromotionName(cartPromotionItem.getPromotionMessage());
      orderItem.setGiftIntegration(cartPromotionItem.getIntegration());
      orderItem.setGiftGrowth(cartPromotionItem.getGrowth());
      return orderItem;
    }).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public Map<String, Object> payPrepay(String ipStr, String orderSn, BigDecimal totalFee, String isParentOrder,
    OmsOrder orderInfo, List<OmsOrder> orderList) throws Exception {
    String nonceStr = CharUtil.getRandomString(32);
    Map<Object, Object> resultObj = new TreeMap();
    Map<Object, Object> parame = new TreeMap<Object, Object>();
    assembleParame(parame, orderSn, totalFee, UserUtils.getCurrentUmsMember(), ipStr);

    String xml = MapUtils.convertMap2Xml(parame);
    log.info("xml:" + xml);
    Map<String, Object> resultUn = XmlUtil.xmlStrToMap(WechatUtil.requestOnce(wxAppletProperties.getUniformorder(), xml));
    // 响应报文
    String return_code = MapUtils.getString("return_code", resultUn);
    String return_msg = MapUtils.getString("return_msg", resultUn);
    if (return_code.equalsIgnoreCase("FAIL")) {
      return ResponseUtil.toResponsFail("支付失败," + return_msg);
    } else if (return_code.equalsIgnoreCase("SUCCESS")) {
      // 返回数据
      String result_code = MapUtils.getString("result_code", resultUn);
      String err_code_des = MapUtils.getString("err_code_des", resultUn);
      if (result_code.equalsIgnoreCase("FAIL")) {
        return ResponseUtil.toResponsFail("支付失败," + err_code_des);
      } else if (result_code.equalsIgnoreCase("SUCCESS")) {
        String prepay_id = MapUtils.getString("prepay_id", resultUn);
        // 先生成paySign 参考https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=5
        resultObj.put("appId", wxAppletProperties.getAppId());
        resultObj.put("timeStamp", DateUtils.timeToStr(System.currentTimeMillis() / 1000, DateUtils.DATE_TIME_PATTERN));
        resultObj.put("nonceStr", nonceStr);
        resultObj.put("package", "prepay_id=" + prepay_id);
        resultObj.put("signType", "MD5");
        String paySign = WechatUtil.arraySign(resultObj, wxAppletProperties.getPaySignKey());
        resultObj.put("paySign", paySign);
        if (MagicConstant.IS_NOT_PARENT.equals(isParentOrder)) {
          updateOmsOrder(orderInfo, prepay_id);
        } else {
          if (!org.springframework.util.CollectionUtils.isEmpty(orderList)) {
            for (OmsOrder omsOrder : orderList) {
              updateOmsOrder(omsOrder, prepay_id);
            }
          }
        }
        return ResponseUtil.toResponsObject(200, "微信统一订单下单成功", resultObj);
      }
    }
    return null;
  }

  /**
   * 组装调用生成订单支付的参数
   */
  private void assembleParame(Map<Object, Object> parame, String tradeNo, BigDecimal totalFee, UmsMember user, String ipStr) {
    parame.put("appid", wxAppletProperties.getAppId());
    // 商家账号。
    parame.put("mch_id", wxAppletProperties.getMchId());
    String randomStr = CharUtil.getRandomNum(18).toUpperCase();
    // 随机字符串
    parame.put("nonce_str", randomStr);
    // 商户订单编号
    parame.put("out_trade_no", tradeNo);

    // 商品描述
    parame.put("body", "微信-支付");
    //支付金额
    parame.put("total_fee", totalFee.multiply(new BigDecimal(100)).intValue());
    // 回调地址
    parame.put("notify_url", wxAppletProperties.getNotifyUrl());
    // 交易类型APP
    parame.put("trade_type", wxAppletProperties.getTradeType());
    parame.put("spbill_create_ip", ipStr);
    parame.put("openid", user.getWeixinOpenid());
    String sign = WechatUtil.arraySign(parame, wxAppletProperties.getPaySignKey());
    // 数字签证
    parame.put("sign", sign);
  }

  /**
   * 更新订单
   */
  private void updateOmsOrder(OmsOrder orderInfo, String prepayId) {
    // 业务处理
    orderInfo.setPrepayId(prepayId);
    orderService.updateById(orderInfo);
  }

  @Override
  public List<OmsOrder> listOmsOrders(String outTradeNo) {
    return orderMapper.listOmsOrders(outTradeNo);
  }

  @Override
  public Page<OrderResult> listOmsOrderByPage(OmsOrderQueryParam oderParam) {
    Integer pageNum = oderParam.getPageNum() == null ? 1 : oderParam.getPageNum();
    Integer pageSize = oderParam.getPageSize() == null ? 10 : oderParam.getPageSize();
    Page<OrderResult> page = new Page<>(pageNum, pageSize);
    Page<OrderResult> orderResultPage = orderMapper.listOmsOrderByPage(page, oderParam);
    return orderResultPage;
  }

}
