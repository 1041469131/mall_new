package com.zscat.mallplus.manage.service.oms;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderItem;
import com.zscat.mallplus.mbg.oms.vo.OmsOrderItemVo;

import java.util.List;

/**
 * <p>
 * 订单中所包含的商品 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
public interface IOmsOrderItemService extends IService<OmsOrderItem> {

    List<OmsOrderItemVo> queryPrfitProportion(Long omsOrderId);
}
