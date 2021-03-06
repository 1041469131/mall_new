package com.zscat.mallplus.mbg.pms.vo;


import com.zscat.mallplus.mbg.pms.entity.PmsProductAttribute;
import com.zscat.mallplus.mbg.pms.entity.PmsProductAttributeCategory;

import java.util.List;

/**
 * 包含有分类下属性的dto
 * https://github.com/shenzhuan/mallplus on 2018/5/24.
 */
public class PmsProductAttributeCategoryItem extends PmsProductAttributeCategory {

    private List<PmsProductAttribute> productAttributeList;

    public List<PmsProductAttribute> getProductAttributeList() {
        return productAttributeList;
    }

    public void setProductAttributeList(List<PmsProductAttribute> productAttributeList) {
        this.productAttributeList = productAttributeList;
    }
}
