package com.zscat.mallplus.mbg.pms.vo;

/**
 * 查询单个产品进行修改时返回的结果
 * https://github.com/shenzhuan/mallplus on 2018/4/26.
 */
public class PmsProductResult extends PmsProductParam {

    //商品所选分类的父id
    private Long cateParentId;

    private  String  favoriteType ;// 1 已收藏 2 未收藏

    public String getFavoriteType() {
        return favoriteType;
    }

    public void setFavoriteType(String favoriteType) {
        this.favoriteType = favoriteType;
    }

    public Long getCateParentId() {
        return cateParentId;
    }

    public void setCateParentId(Long cateParentId) {
        this.cateParentId = cateParentId;
    }
}
