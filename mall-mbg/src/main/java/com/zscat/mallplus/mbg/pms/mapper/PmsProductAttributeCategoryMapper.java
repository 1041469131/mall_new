package com.zscat.mallplus.mbg.pms.mapper;

import com.zscat.mallplus.mbg.pms.vo.PmsProductAttributeCategoryItem;
import com.zscat.mallplus.mbg.pms.entity.PmsProductAttributeCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 产品属性分类表 Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface PmsProductAttributeCategoryMapper extends BaseMapper<PmsProductAttributeCategory> {

    List<PmsProductAttributeCategoryItem> getListWithAttr();
}
