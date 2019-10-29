package com.zscat.mallplus.manage.service.pms.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.pms.IPmsProductFullReductionService;
import com.zscat.mallplus.mbg.pms.entity.PmsProductFullReduction;
import com.zscat.mallplus.mbg.pms.mapper.PmsProductFullReductionMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品满减表(只针对同商品) 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Service
public class PmsProductFullReductionServiceImpl extends ServiceImpl<PmsProductFullReductionMapper, PmsProductFullReduction> implements IPmsProductFullReductionService {

}
