package com.zscat.mallplus.manage.service.pms.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.pms.IPmsProductAttributeCategoryService;
import com.zscat.mallplus.mbg.pms.entity.PmsProductAttributeCategory;
import com.zscat.mallplus.mbg.pms.mapper.PmsProductAttributeCategoryMapper;
import com.zscat.mallplus.mbg.pms.vo.PmsProductAttributeCategoryItem;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 产品属性分类表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Service
public class PmsProductAttributeCategoryServiceImpl extends ServiceImpl<PmsProductAttributeCategoryMapper, PmsProductAttributeCategory> implements IPmsProductAttributeCategoryService {

    @Resource
    private PmsProductAttributeCategoryMapper productAttributeCategoryMapper;

    @Override
    public List<PmsProductAttributeCategoryItem> getListWithAttr() {
        return productAttributeCategoryMapper.getListWithAttr();
    }
}
