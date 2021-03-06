package com.zscat.mallplus.manage.service.marking.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.marking.ISmsHomeNewProductService;
import com.zscat.mallplus.mbg.marking.entity.SmsHomeNewProduct;
import com.zscat.mallplus.mbg.marking.mapper.SmsHomeNewProductMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 新鲜好物表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Service
public class SmsHomeNewProductServiceImpl extends ServiceImpl<SmsHomeNewProductMapper, SmsHomeNewProduct> implements ISmsHomeNewProductService {
    @Resource
    private SmsHomeNewProductMapper homeNewProductMapper;
    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        SmsHomeNewProduct record = new SmsHomeNewProduct();
        record.setRecommendStatus(recommendStatus);
        return homeNewProductMapper.update(record, new QueryWrapper<SmsHomeNewProduct>().in("id",ids));
    }
    @Override
    public int updateSort(Long id, Integer sort) {
        SmsHomeNewProduct homeNewProduct = new SmsHomeNewProduct();
        homeNewProduct.setId(id);
        homeNewProduct.setSort(sort);
        return homeNewProductMapper.updateById(homeNewProduct);
    }
}
