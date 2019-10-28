package com.zscat.mallplus.portal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import com.zscat.mallplus.mbg.pms.vo.PmsProductAndGroup;
import com.zscat.mallplus.mbg.pms.vo.PmsProductResult;

/**
 * <p>
 * 商品信息 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface IPmsProductService extends IService<PmsProduct> {

    PmsProductAndGroup getProductAndGroup(Long id);

    PmsProductResult getUpdateInfo(Long id);
}
