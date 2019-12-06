package com.zscat.mallplus.manage.service.cms;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.cms.entity.CmsHelpCategory;
import com.zscat.mallplus.mbg.cms.vo.CmsHelpCategoryVO;

import java.util.List;

/**
 * <p>
 * 帮助分类表 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
public interface ICmsHelpCategoryService extends IService<CmsHelpCategory> {

    List<CmsHelpCategoryVO> getCartProduct();

}
