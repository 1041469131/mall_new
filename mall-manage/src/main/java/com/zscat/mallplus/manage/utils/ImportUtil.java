package com.zscat.mallplus.manage.utils;

import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import com.zscat.mallplus.mbg.pms.entity.PmsSkuStock;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import com.zscat.mallplus.mbg.utils.vo.ColorListVo;
import com.zscat.mallplus.mbg.utils.vo.Img;
import com.zscat.mallplus.mbg.utils.vo.ProductVo4Import;
import com.zscat.mallplus.mbg.utils.vo.SkuVo;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 导入工具
 * @Date: 2020/3/10
 * @Description
 */
//@Component
public class ImportUtil {

    public static void importProduct(String productJsonStr){
        ProductVo4Import productVo4Import = JsonUtil.jsonToPojo(productJsonStr, ProductVo4Import.class);
        PmsProduct product = assemblyProduct(productVo4Import);
        assemblyAndSaveSku(productVo4Import,product);
        System.out.println("单元测试");
    }

    /**
     * 组装并且保存sku信息
     * @param productVo4Import
     * @param product
     * @return
     */
    private static void assemblyAndSaveSku(ProductVo4Import productVo4Import, PmsProduct product) {
        List<PmsSkuStock> pmsSkuStocks = null;
        List<ColorListVo> colorListVoList = productVo4Import.getColorList();
        int totalStock = 0;
        if(!CollectionUtils.isEmpty(colorListVoList)){
            pmsSkuStocks = new ArrayList<>();
            for (ColorListVo colorListVo : colorListVoList){
                List<SkuVo> skuVos = colorListVo.getSkuList();
                if(!CollectionUtils.isEmpty(skuVos)){
                    for (SkuVo skuVo : skuVos){
                        PmsSkuStock pmsSkuStock = new PmsSkuStock();
                        pmsSkuStock.setProductId(product.getId());
                        pmsSkuStock.setSkuCode(null);
                        pmsSkuStock.setPrice(new BigDecimal(skuVo.getPrice()));
                        pmsSkuStock.setStock(Integer.valueOf(skuVo.getCount()));//库存
                        pmsSkuStock.setLowStock(10);//最低库存
                        pmsSkuStock.setSp1(colorListVo.getColor().getName());//销售属性 颜色
                        pmsSkuStock.setSp2(skuVo.getName());//销售属性 尺码
                        pmsSkuStock.setPromotionPrice(new BigDecimal(skuVo.getPrice()));//单品促销价格
                        pmsSkuStock.setLockStock(5);//锁定库存
                        pmsSkuStock.setMeno("导入的数据");
                        pmsSkuStock.setPic(colorListVo.getImg().getPreview());//展示图片
                        pmsSkuStocks.add(pmsSkuStock);
                        totalStock = totalStock + Integer.valueOf(skuVo.getCount());
                    }
                }
            }
        }
        product.setStock(totalStock);
    }

    /**
     * 组装商品的相关信息
     * @param productVo4Import
     * @return
     */
    private static PmsProduct assemblyProduct(ProductVo4Import productVo4Import) {
        PmsProduct pmsProduct = new PmsProduct();
        pmsProduct.setFeightTemplateId(0L);
        pmsProduct.setName(productVo4Import.getTitle());
        pmsProduct.setPic(productVo4Import.getImgList().get(0).getPreview());
        pmsProduct.setProductSn(productVo4Import.getOfferId());//货号 offerId是不是商品编号
        pmsProduct.setDeleteStatus(MagicConstant.DELETE_NOT);//删除状态：0->未删除；1->已删除
        pmsProduct.setPublishStatus(MagicConstant.PUBLISH_STATUS_UP);//上架状态：0->下架；1->上架
        pmsProduct.setNewStatus(MagicConstant.NEW_STATUS_NEW);//新品状态:0->不是新品；1->新品
        pmsProduct.setRecommandStatus(Integer.valueOf(MagicConstant.RECOMMEND_TYPE_YES));//推荐状态；0->不推荐；1->推荐
        pmsProduct.setVerifyStatus(MagicConstant.VERIFY_STATUS_UNVERIFY);//审核状态：0->未审核；1->审核通过
        pmsProduct.setSort(0);//排序
        pmsProduct.setSale(Integer.valueOf(productVo4Import.getSale()));//销量
        pmsProduct.setPrice(new BigDecimal(0));//TODO
        pmsProduct.setMarketPrice(new BigDecimal(0));//TODO
        pmsProduct.setGiftGrowth(0);//赠送的成长值
        pmsProduct.setGiftPoint(0);//赠送的积分
        pmsProduct.setUsePointLimit(0);//限制使用的积分数
        pmsProduct.setSubTitle(productVo4Import.getTitle());//副标题
        pmsProduct.setDescription(productVo4Import.getTitle());//商品描述
        pmsProduct.setCommodityPrice(new BigDecimal(0));//TODO
        pmsProduct.setStock(0);//库存 需要统计sku的总数
        pmsProduct.setLowStock(10);//库存预警值
        pmsProduct.setUnit("件");//单位
        pmsProduct.setWeight(BigDecimal.ZERO);//商品重量，默认为克
        pmsProduct.setPreviewStatus(MagicConstant.PREVIEW_STATUS_NOT);//是否为预告商品：0->不是；1->是
        pmsProduct.setServiceIds("1,2,3");//以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮
        pmsProduct.setAlbumPics("");//画册图片，连产品图片限制为5张，以逗号分割TODO
        pmsProduct.setPromotionType(MagicConstant.PROMOTION_TYPE_INIT);//促销类型：0->没有促销使用原价;1->使用促销价；2->使用会员价；3->使用阶梯价格；4->使用满减价格；5->限时购
        pmsProduct.setBrandName("");//品牌名称
        pmsProduct.setCreateTime(new Date());
        return pmsProduct;
    }
}
