package com.zscat.mallplus.admin.component;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zscat.mallplus.manage.service.oms.IOmsOrderService;
import com.zscat.mallplus.mbg.oms.entity.OmsOrder;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 自动确认收货
 */
@Component
public class OrderReceivedTask {

  private Logger LOGGER = LoggerFactory.getLogger(OrderReceivedTask.class);
  @Autowired
  private IOmsOrderService omsOrderService;

  /**
   * cron表达式：每小时扫描一次
   */
  @Scheduled(cron = "0 0 0/1 * * ?")
  private void receivedOrder() {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_YEAR, -7);
    Date deliveryTime = calendar.getTime();
    List<OmsOrder> orderList = omsOrderService.list(
      new QueryWrapper<OmsOrder>().lambda().eq(OmsOrder::getStatus, MagicConstant.ORDER_STATUS_YET_SEND)
        .lt(OmsOrder::getDeliveryTime, deliveryTime));
    if (!CollectionUtils.isEmpty(orderList)) {
      LOGGER.info("自动收货开始");
      orderList.forEach(omsOrder -> {
        omsOrder.setStatus(MagicConstant.ORDER_STATUS_YET_DONE);
        omsOrder.setReceiveTime(new Date());
      });
      omsOrderService.saveBatch(orderList);
      LOGGER.info("自动收货开始");
    }
  }
}
