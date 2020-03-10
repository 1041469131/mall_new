package com.zscat.mallplus.mbg.utils.vo;

import java.util.List;

/**
 * 商品的sku的相关信息，目前和数据的格式相同
 * @Date: 2020/3/10
 * @Description
 */
public class ColorListVo {

    private Color color;

    private Img img;

    private List<SkuVo> skuList;


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Img getImg() {
        return img;
    }

    public void setImg(Img img) {
        this.img = img;
    }

    public List<SkuVo> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<SkuVo> skuList) {
        this.skuList = skuList;
    }
}
