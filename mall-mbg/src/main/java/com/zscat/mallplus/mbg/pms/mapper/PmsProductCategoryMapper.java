package com.zscat.mallplus.mbg.pms.mapper;

import com.zscat.mallplus.mbg.pms.vo.PmsProductCategoryWithChildrenItem;
import com.zscat.mallplus.mbg.pms.entity.PmsProductCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 产品分类 Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface PmsProductCategoryMapper extends BaseMapper<PmsProductCategory> {

    List<PmsProductCategoryWithChildrenItem> listWithChildren();
}
