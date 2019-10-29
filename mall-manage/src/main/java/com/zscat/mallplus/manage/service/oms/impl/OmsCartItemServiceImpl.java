package com.zscat.mallplus.manage.service.oms.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.oms.IOmsCartItemService;
import com.zscat.mallplus.mbg.oms.entity.OmsCartItem;
import com.zscat.mallplus.mbg.oms.mapper.OmsCartItemMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 购物车表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
@Service
public class OmsCartItemServiceImpl extends ServiceImpl<OmsCartItemMapper, OmsCartItem> implements IOmsCartItemService {

}
