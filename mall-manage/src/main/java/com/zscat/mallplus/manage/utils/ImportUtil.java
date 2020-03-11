package com.zscat.mallplus.manage.utils;

import com.zscat.mallplus.manage.service.pms.*;
import com.zscat.mallplus.mbg.pms.entity.*;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import com.zscat.mallplus.mbg.utils.vo.ColorListVo;
import com.zscat.mallplus.mbg.utils.vo.ProductVo4Import;
import com.zscat.mallplus.mbg.utils.vo.SkuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
@Component
public class ImportUtil {

    @Autowired
    private IPmsSkuStockService iPmsSkuStockService;

    @Autowired
    private IPmsProductService iPmsProductService;

    @Autowired
    private IPmsProductCategoryService iPmsProductCategoryService;

    @Autowired
    private IPmsBrandService iPmsBrandService;

    @Autowired
    private IPmsProductAttributeCategoryService iPmsProductAttributeCategoryService;

    @Autowired
    private IPmsProductAttributeService iPmsProductAttributeService;

    @Autowired
    private IPmsProductAttributeValueService iPmsProductAttributeValueService;

    @Transactional
    public void importProduct(String productJsonStr){
        ProductVo4Import productVo4Import = JsonUtil.jsonToPojo(productJsonStr, ProductVo4Import.class);
        PmsProduct product = assemblyProduct(productVo4Import);
        iPmsProductService.save(product);
        assemblyAndSaveSku(productVo4Import,product);
        Long productCategoryId = assemblyAndSaveProductCategory("");
        Long branId = assemblyAndSaveBrand("");
        Long attrCategoryId = assemblyAndSaveProductAttrCategory("",productCategoryId);
        Long productAttrId = assemblyAndSaveProductAttrs(productVo4Import,attrCategoryId);
        assemblyAndSaveProductAttrValue(productVo4Import,productAttrId,product.getId());
        product.setProductCategoryId(productCategoryId);
        product.setProductAttributeCategoryId(attrCategoryId);
        iPmsProductService.updateById(product);
    }

    /**
     * 组装产品属性值
     * @param productVo4Import
     * @param productAttrId
     */
    private void assemblyAndSaveProductAttrValue(ProductVo4Import productVo4Import, Long productAttrId,Long productId) {
        PmsProductAttributeValue pmsProductAttributeValue = new PmsProductAttributeValue();
        pmsProductAttributeValue.setProductAttributeId(productAttrId);
        pmsProductAttributeValue.setProductId(productId);
        pmsProductAttributeValue.setValue("");//TODO
        iPmsProductAttributeValueService.save(pmsProductAttributeValue);
    }

    /**
     * 组装并且保存商品属性
     * @param productVo4Import
     */
    private Long assemblyAndSaveProductAttrs(ProductVo4Import productVo4Import,Long attrCategoryId) {
        PmsProductAttribute pmsProductAttribute = new PmsProductAttribute();
        pmsProductAttribute.setProductAttributeCategoryId(attrCategoryId);
        pmsProductAttribute.setName("");//TODO
        pmsProductAttribute.setSelectType(MagicConstant.SELECT_TYPE_ONLY);//属性选择类型：0->唯一；1->单选；2->多选
        pmsProductAttribute.setInputType(MagicConstant.INPUT_TYPE_LIST);//属性录入方式：0->手工录入；1->从列表中选取
        pmsProductAttribute.setInputList("");//TODO 可选值列表，以逗号隔开
        pmsProductAttribute.setFilterType(MagicConstant.FILTER_TYPE_NORMAL);//分类筛选样式：0->普通；1->颜色
        pmsProductAttribute.setSearchType(MagicConstant.SEARCH_TYPE);//检索类型；0->不需要进行检索；1->关键字检索；2->范围检索
        pmsProductAttribute.setRelatedStatus(MagicConstant.RELATED_STATUS_NOT);//相同属性产品是否关联；0->不关联；1->关联
        pmsProductAttribute.setHandAddStatus(MagicConstant.HAND_ADD_STATUS_NOT);//是否支持手动新增；0->不支持；1->支持
        pmsProductAttribute.setType(MagicConstant.ATTR_TYPE_SPECI);//TODO 属性的类型；0->规格；1->参数
        iPmsProductAttributeService.save(pmsProductAttribute);
        return pmsProductAttribute.getId();
    }

    /**
     * 组装品牌
     * @param s
     */
    private Long assemblyAndSaveBrand(String brandName) {
        PmsBrand pmsBrand = new PmsBrand();
        pmsBrand.setName(brandName);
        iPmsBrandService.save(pmsBrand);
        return pmsBrand.getId();
    }

    /**
     * 组装产品属性
     * @param s
     */
    private Long assemblyAndSaveProductAttrCategory(String attrName, Long productCategoryId) {
        PmsProductAttributeCategory pmsProductAttributeCategory = new PmsProductAttributeCategory();
        pmsProductAttributeCategory.setName(attrName);
        pmsProductAttributeCategory.setCategoryId(productCategoryId);
        iPmsProductAttributeCategoryService.save(pmsProductAttributeCategory);
        return pmsProductAttributeCategory.getId();
    }

    /**
     * 组装产品分类
     * @param s
     */
    private Long assemblyAndSaveProductCategory(String productCategoryName) {
        PmsProductCategory pmsProductCategory = new PmsProductCategory();
        pmsProductCategory.setParentId(0L);
        pmsProductCategory.setName(productCategoryName);
        pmsProductCategory.setLevel(MagicConstant.LEVEL_FIRST);
        pmsProductCategory.setProductCount(0);
        pmsProductCategory.setProductUnit("件");
        pmsProductCategory.setNavStatus(MagicConstant.NAV_STATUS_NOT_SHOW);
        pmsProductCategory.setShowStatus(MagicConstant.SHOW_STATUS_SHOW);
        pmsProductCategory.setSort(0);
        iPmsProductCategoryService.save(pmsProductCategory);
        return pmsProductCategory.getId();
    }

    /**
     * 组装并且保存sku信息
     * @param productVo4Import
     * @param product
     * @return
     */
    private void assemblyAndSaveSku(ProductVo4Import productVo4Import, PmsProduct product) {
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
        if(!CollectionUtils.isEmpty(pmsSkuStocks)){
            iPmsSkuStockService.saveBatch(pmsSkuStocks);
        }
    }

    /**
     * 组装商品的相关信息
     * @param productVo4Import
     * @return
     */
    private PmsProduct assemblyProduct(ProductVo4Import productVo4Import) {
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
