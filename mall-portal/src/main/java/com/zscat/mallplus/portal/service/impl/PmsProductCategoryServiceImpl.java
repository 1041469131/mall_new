package com.zscat.mallplus.portal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.mbg.pms.entity.PmsProductCategory;
import com.zscat.mallplus.mbg.pms.mapper.PmsProductCategoryMapper;
import com.zscat.mallplus.portal.service.IPmsProductCategoryService;
import com.zscat.mallplus.mbg.pms.vo.PmsProductCategoryWithChildrenItem;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 产品分类 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Service
public class PmsProductCategoryServiceImpl extends ServiceImpl<PmsProductCategoryMapper, PmsProductCategory> implements IPmsProductCategoryService {

    @Resource
    private PmsProductCategoryMapper categoryMapper;

    @Override
    public List<PmsProductCategoryWithChildrenItem> listWithChildren() {
        return categoryMapper.listWithChildren();
    }
}
