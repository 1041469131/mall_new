package com.zscat.mallplus.manage.service.pms.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.pms.IPmsProductCategoryAttributeRelationService;
import com.zscat.mallplus.mbg.pms.entity.PmsProductCategoryAttributeRelation;
import com.zscat.mallplus.mbg.pms.mapper.PmsProductCategoryAttributeRelationMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品的分类和属性的关系表，用于设置分类筛选条件（只支持一级分类） 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Service
public class PmsProductCategoryAttributeRelationServiceImpl extends ServiceImpl<PmsProductCategoryAttributeRelationMapper, PmsProductCategoryAttributeRelation> implements IPmsProductCategoryAttributeRelationService {

}
