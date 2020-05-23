package com.zscat.mallplus.manage.service.pms.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.pms.IPmsProductCategoryAttributeRelationService;
import com.zscat.mallplus.manage.service.pms.IPmsProductCategoryService;
import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import com.zscat.mallplus.mbg.pms.entity.PmsProductCategory;
import com.zscat.mallplus.mbg.pms.entity.PmsProductCategoryAttributeRelation;
import com.zscat.mallplus.mbg.pms.mapper.PmsProductCategoryAttributeRelationMapper;
import com.zscat.mallplus.mbg.pms.mapper.PmsProductCategoryMapper;
import com.zscat.mallplus.mbg.pms.mapper.PmsProductMapper;
import com.zscat.mallplus.mbg.pms.vo.PmsProductCategoryWithChildrenItem;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    @Autowired
    private PmsProductCategoryMapper categoryMapper;
    @Autowired
    private PmsProductMapper productMapper;
    @Autowired
    private IPmsProductCategoryAttributeRelationService pmsProductCategoryAttributeRelationService;
    @Autowired
    private PmsProductCategoryAttributeRelationMapper productCategoryAttributeRelationMapper;

    @Override
    public List<PmsProductCategoryWithChildrenItem> listWithChildren() {
        return categoryMapper.listWithChildren();
    }

    @Override
    public int updateNavStatus(List<Long> ids, Integer navStatus) {
        PmsProductCategory productCategory = new PmsProductCategory();
        productCategory.setNavStatus(navStatus);
        return categoryMapper.update(productCategory, new QueryWrapper<PmsProductCategory>().eq("id",ids));
    }

    @Override
    public int updateShowStatus(List<Long> ids, Integer showStatus) {
        PmsProductCategory productCategory = new PmsProductCategory();
        productCategory.setShowStatus(showStatus);
        return categoryMapper.update(productCategory, new QueryWrapper<PmsProductCategory>().eq("id",ids));
    }

    @Override
    public boolean updateAnd(PmsProductCategory entity) {
        PmsProductCategory productCategory = new PmsProductCategory();
        productCategory.setId(entity.getId());
        productCategory.setParentId(entity.getParentId());
        setCategoryLevel(productCategory);
        //更新商品分类时要更新商品中的名称
        PmsProduct product = new PmsProduct();
        product.setProductCategoryName(entity.getName());

        productMapper.update(product, new QueryWrapper<PmsProduct>().eq("product_category_id",entity.getId()));
        //同时更新筛选属性的信息
        if (!CollectionUtils.isEmpty(entity.getProductAttributeIdList())) {

            productCategoryAttributeRelationMapper.delete(new QueryWrapper<PmsProductCategoryAttributeRelation>().eq("product_category_id",entity.getId()));
            insertRelationList(entity.getId(), entity.getProductAttributeIdList());
        } else {
            productCategoryAttributeRelationMapper.delete(new QueryWrapper<PmsProductCategoryAttributeRelation>().eq("product_category_id",entity.getId()));

        }
         categoryMapper.updateById(productCategory);
        return true;
    }

    @Override
    public boolean saveAnd(PmsProductCategory entity) {
        PmsProductCategory productCategory = new PmsProductCategory();
        productCategory.setProductCount(0);
        BeanUtils.copyProperties(entity, productCategory);
        //没有父分类时为一级分类
        setCategoryLevel(productCategory);
        int count = categoryMapper.insert(productCategory);
        //创建筛选属性关联
        List<Long> productAttributeIdList = entity.getProductAttributeIdList();
        if (!CollectionUtils.isEmpty(productAttributeIdList)) {
            insertRelationList(productCategory.getId(), productAttributeIdList);
        }
        return true;
    }
    /**
     * 批量插入商品分类与筛选属性关系表
     *
     * @param productCategoryId      商品分类id
     * @param productAttributeIdList 相关商品筛选属性id集合
     */
    private void insertRelationList(Long productCategoryId, List<Long> productAttributeIdList) {
        List<PmsProductCategoryAttributeRelation> relationList = new ArrayList<>();
        for (Long productAttrId : productAttributeIdList) {
            PmsProductCategoryAttributeRelation relation = new PmsProductCategoryAttributeRelation();
            relation.setProductAttributeId(productAttrId);
            relation.setProductCategoryId(productCategoryId);
            relationList.add(relation);
        }
        pmsProductCategoryAttributeRelationService.saveBatch(relationList);
    }
    /**
     * 根据分类的parentId设置分类的level
     */
    private void setCategoryLevel(PmsProductCategory productCategory) {
        //没有父分类时为一级分类
        if (productCategory.getParentId() == 0) {
            productCategory.setLevel(0);
        } else {
            //有父分类时选择根据父分类level设置
            PmsProductCategory parentCategory = categoryMapper.selectById(productCategory.getParentId());
            if (parentCategory != null) {
                productCategory.setLevel(parentCategory.getLevel() + 1);
            } else {
                productCategory.setLevel(0);
            }
        }
    }
}
