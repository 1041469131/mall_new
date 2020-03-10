package com.zscat.mallplus.mbg.utils.vo;

import java.util.List;

/**
 * 导入商品的包装类
 * @Date: 2020/3/10
 * @Description
 */
public class ProductVo4Import{

    /**标题*****/
    private String title;

    /**图片列表**/
    private List<Img> imgList;

    private List<ColorListVo> colorList;

    private String offerId;

    private String sale;

    private OriginalPriceVo originalPrice;

    private List<ObjContent> objContentList;

    private SupplierInfo supplierInfo;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Img> getImgList() {
        return imgList;
    }

    public void setImgList(List<Img> imgList) {
        this.imgList = imgList;
    }

    public List<ColorListVo> getColorList() {
        return colorList;
    }

    public void setColorList(List<ColorListVo> colorList) {
        this.colorList = colorList;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public OriginalPriceVo getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(OriginalPriceVo originalPrice) {
        this.originalPrice = originalPrice;
    }

    public List<ObjContent> getObjContentList() {
        return objContentList;
    }

    public void setObjContentList(List<ObjContent> objContentList) {
        this.objContentList = objContentList;
    }

    public SupplierInfo getSupplierInfo() {
        return supplierInfo;
    }

    public void setSupplierInfo(SupplierInfo supplierInfo) {
        this.supplierInfo = supplierInfo;
    }
}
