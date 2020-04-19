package com.zscat.mallplus.manage.service.pms.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.pms.IPmsProductCommissionService;
import com.zscat.mallplus.mbg.pms.entity.PmsProductCommission;
import com.zscat.mallplus.mbg.pms.mapper.PmsProductCommissionMapper;
import org.springframework.stereotype.Service;

/**
 * 商品的佣金比例分佣服务
 */
@Service
public class PmsProductCommissionServiceImpl extends ServiceImpl<PmsProductCommissionMapper,PmsProductCommission> implements IPmsProductCommissionService{
}
