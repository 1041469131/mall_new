package com.zscat.mallplus.manage.service.cms.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.cms.ICmsHelpCategoryService;
import com.zscat.mallplus.mbg.cms.entity.CmsHelpCategory;
import com.zscat.mallplus.mbg.cms.mapper.CmsHelpCategoryMapper;
import com.zscat.mallplus.mbg.cms.vo.CmsHelpCategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 帮助分类表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
@Service
public class CmsHelpCategoryServiceImpl extends ServiceImpl<CmsHelpCategoryMapper, CmsHelpCategory> implements ICmsHelpCategoryService {

    @Autowired
    private CmsHelpCategoryMapper cmsHelpCategoryMapper;

    @Override
    public List<CmsHelpCategoryVO> getCartProduct() {
        return cmsHelpCategoryMapper.getCartProduct();
    }
}
