package com.zscat.mallplus.mbg.cms.mapper;

import com.zscat.mallplus.mbg.cms.entity.CmsHelpCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.mbg.cms.vo.CmsHelpCategoryVO;

import java.util.List;

/**
 * <p>
 * 帮助分类表 Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
public interface CmsHelpCategoryMapper extends BaseMapper<CmsHelpCategory> {

    List<CmsHelpCategoryVO> getCartProduct();
}
