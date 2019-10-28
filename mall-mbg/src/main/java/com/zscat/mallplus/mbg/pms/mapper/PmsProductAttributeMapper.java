package com.zscat.mallplus.mbg.pms.mapper;

import com.zscat.mallplus.mbg.pms.vo.ProductAttrInfo;
import com.zscat.mallplus.mbg.pms.entity.PmsProductAttribute;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 商品属性参数表 Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface PmsProductAttributeMapper extends BaseMapper<PmsProductAttribute> {

    List<ProductAttrInfo> getProductAttrInfo(Long productCategoryId);
}
