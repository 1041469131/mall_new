package com.zscat.mallplus.mbg.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.mbg.oms.vo.CartProduct;
import com.zscat.mallplus.mbg.pms.vo.PmsProductQueryParam;
import com.zscat.mallplus.mbg.pms.vo.PmsProductResult;
import com.zscat.mallplus.mbg.pms.vo.PmsProductVo;
import com.zscat.mallplus.mbg.pms.vo.PromotionProduct;
import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品信息 Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface PmsProductMapper extends BaseMapper<PmsProduct> {

    CartProduct getCartProduct(@Param("id") Long id);

    List<PromotionProduct> getPromotionProductList(@Param("ids") List<Long> ids);

    PmsProductResult getUpdateInfo(Long id);

    List<PmsProductResult> getProductResults(@Param("ids") List<Long> ids);

    Page<PmsProductVo> listPmsProductByPage(Page<PmsProductVo> pmsProductPage, @Param("queryParam") PmsProductQueryParam queryParam);

    Page<PmsProductVo> listPmsProductCollectByPage(Page<PmsProductVo> page, @Param("queryParam") PmsProductQueryParam queryParam);
}
