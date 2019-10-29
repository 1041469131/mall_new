package com.zscat.mallplus.manage.service.oms.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.oms.IOmsOrderTradeService;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderTrade;
import com.zscat.mallplus.mbg.oms.mapper.OmsOrderTradeMapper;
import org.springframework.stereotype.Service;

@Service
public class OmsOrderTradeServiceImpl extends ServiceImpl<OmsOrderTradeMapper,OmsOrderTrade> implements IOmsOrderTradeService {
}
