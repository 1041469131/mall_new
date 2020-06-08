package com.zscat.mallplus.manage.service.pms.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.pms.IPmsSkuStockService;
import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import com.zscat.mallplus.mbg.pms.entity.PmsSkuStock;
import com.zscat.mallplus.mbg.pms.mapper.PmsProductMapper;
import com.zscat.mallplus.mbg.pms.mapper.PmsSkuStockMapper;
import com.zscat.mallplus.mbg.pms.vo.PmsSkuStockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * sku的库存 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Service
public class PmsSkuStockServiceImpl extends ServiceImpl<PmsSkuStockMapper, PmsSkuStock> implements IPmsSkuStockService {

    @Autowired
    private PmsSkuStockMapper pmsSkuStockMapper;

    @Autowired
    private PmsProductMapper pmsProductMapper;


    @Override
    public List<PmsSkuStock> getList(Long pid, String keyword) {
        QueryWrapper q = new QueryWrapper();
        q.eq("product_id",pid);

        if (!StringUtils.isEmpty(keyword)) {
            q.like("sku_code",keyword);
        }
        return pmsSkuStockMapper.selectList(q);
    }

    @Override
    public int update(Long pid, List<PmsSkuStock> skuStockList) {
        return pmsSkuStockMapper.replaceList(skuStockList);
    }

    @Override
    public List<PmsSkuStockVo> querySkuStockVos(String[] skuIds, Long userId) {
        return pmsSkuStockMapper.querySkuStockVos(skuIds,userId);
    }

    @Override
    @Transactional
    public void updateStockCount(Long skuId,Integer count,String returnType) {
        PmsSkuStock pmsSkuStock = pmsSkuStockMapper.selectById(skuId);
        Long productId = pmsSkuStock.getProductId();
        PmsProduct pmsProduct = pmsProductMapper.selectById(productId);
        if("0".equals(returnType)){
            pmsSkuStock.setStock(pmsSkuStock.getStock()- pmsSkuStock.getLockStock()-count);
            pmsSkuStock.setLockStock(pmsSkuStock.getLockStock()-count);
            pmsProduct.setStock(pmsProduct.getStock()-pmsSkuStock.getLockStock()-count);
            pmsProduct.setSale(pmsProduct.getSale()+pmsSkuStock.getLockStock()-count);
        }else{
            pmsSkuStock.setStock(pmsSkuStock.getStock()-count);
            pmsSkuStock.setLockStock(pmsSkuStock.getLockStock()-count);
            pmsProduct.setStock(pmsProduct.getStock()-count);
            pmsProduct.setSale(pmsProduct.getSale()+count);
           if(pmsProduct.getStock()<=0){
               //下架
               pmsProduct.setPublishStatus(0);
           }
        }
        pmsProductMapper.updateById(pmsProduct);
        pmsSkuStockMapper.updateById(pmsSkuStock);
    }

}
