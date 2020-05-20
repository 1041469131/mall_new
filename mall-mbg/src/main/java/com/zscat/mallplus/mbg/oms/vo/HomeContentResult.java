package com.zscat.mallplus.mbg.oms.vo;


import com.zscat.mallplus.mbg.cms.entity.CmsSubject;
import com.zscat.mallplus.mbg.marking.entity.SmsFlashPromotion;
import com.zscat.mallplus.mbg.marking.entity.SmsHomeAdvertise;
import com.zscat.mallplus.mbg.pms.entity.PmsBrand;
import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import com.zscat.mallplus.mbg.pms.entity.PmsProductAttributeCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 首页内容返回信息封装
 * https://github.com/shenzhuan/mallplus on 2019/1/28.
 */
public class HomeContentResult {
    //轮播广告
    private List<SmsHomeAdvertise> advertiseList;
    //推荐品牌
    private List<PmsBrand> brandList;
    //当前秒杀场次
    private SmsFlashPromotion homeFlashPromotion;
    //新品推荐
    private List<PmsProduct> newProductList;
    //人气推荐
    private List<PmsProduct> hotProductList;
    //推荐专题
    private List<CmsSubject> subjectList;

    private List<PmsProductAttributeCategory> cat_list;

    public List<SmsHomeAdvertise> getAdvertiseList() {
        return advertiseList;
    }

    public HomeContentResult setAdvertiseList(List<SmsHomeAdvertise> advertiseList) {
        this.advertiseList = advertiseList;
        return this;
    }

    public List<PmsBrand> getBrandList() {
        return brandList;
    }

    public HomeContentResult setBrandList(List<PmsBrand> brandList) {
        this.brandList = brandList;
        return this;
    }

    public SmsFlashPromotion getHomeFlashPromotion() {
        return homeFlashPromotion;
    }

    public HomeContentResult setHomeFlashPromotion(SmsFlashPromotion homeFlashPromotion) {
        this.homeFlashPromotion = homeFlashPromotion;
        return this;
    }

    public List<PmsProduct> getNewProductList() {
        return newProductList;
    }

    public HomeContentResult setNewProductList(List<PmsProduct> newProductList) {
        this.newProductList = newProductList;
        return this;
    }

    public List<PmsProduct> getHotProductList() {
        return hotProductList;
    }

    public HomeContentResult setHotProductList(List<PmsProduct> hotProductList) {
        this.hotProductList = hotProductList;
        return this;
    }

    public List<CmsSubject> getSubjectList() {
        return subjectList;
    }

    public HomeContentResult setSubjectList(List<CmsSubject> subjectList) {
        this.subjectList = subjectList;
        return this;
    }

    public List<PmsProductAttributeCategory> getCat_list() {
        return cat_list;
    }

    public HomeContentResult setCat_list(List<PmsProductAttributeCategory> cat_list) {
        this.cat_list = cat_list;
        return this;
    }
}
