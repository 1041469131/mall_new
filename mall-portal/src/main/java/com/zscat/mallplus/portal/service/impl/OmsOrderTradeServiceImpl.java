package com.zscat.mallplus.portal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderTrade;
import com.zscat.mallplus.mbg.oms.mapper.OmsOrderTradeMapper;
import com.zscat.mallplus.portal.service.IOmsOrderTradeService;
import org.springframework.stereotype.Service;

@Service
public class OmsOrderTradeServiceImpl extends ServiceImpl<OmsOrderTradeMapper,OmsOrderTrade> implements IOmsOrderTradeService{
}
