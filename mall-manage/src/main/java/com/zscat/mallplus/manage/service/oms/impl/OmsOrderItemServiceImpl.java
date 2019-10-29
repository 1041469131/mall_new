package com.zscat.mallplus.manage.service.oms.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.oms.IOmsOrderItemService;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderItem;
import com.zscat.mallplus.mbg.oms.mapper.OmsOrderItemMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单中所包含的商品 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
@Service
public class OmsOrderItemServiceImpl extends ServiceImpl<OmsOrderItemMapper, OmsOrderItem> implements IOmsOrderItemService {

}
