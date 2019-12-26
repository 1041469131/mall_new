package com.zscat.mallplus.manage.service.pms;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.pms.entity.PmsSkuStock;
import com.zscat.mallplus.mbg.pms.vo.PmsSkuStockVo;

import java.util.List;

/**
 * <p>
 * sku的库存 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface IPmsSkuStockService extends IService<PmsSkuStock> {
    /**
     * 根据产品id和skuCode模糊搜索
     */
    List<PmsSkuStock> getList(Long pid, String keyword);

    /**
     * 批量更新商品库存信息
     */
    int update(Long pid, List<PmsSkuStock> skuStockList);

    List<PmsSkuStockVo> querySkuStockVos(String[] skuIds, Long userId);
}
