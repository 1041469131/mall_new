package com.zscat.mallplus.manage.service.pms.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.pms.IPmsProductCommissionService;
import com.zscat.mallplus.mbg.pms.entity.PmsProductCommission;
import com.zscat.mallplus.mbg.pms.mapper.PmsProductCommissionMapper;
import com.zscat.mallplus.mbg.pms.vo.PmsProductCommissionVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 商品的佣金比例分佣服务
 */
@Service
public class PmsProductCommissionServiceImpl extends ServiceImpl<PmsProductCommissionMapper,PmsProductCommission> implements IPmsProductCommissionService{

    @Autowired
    private PmsProductCommissionMapper pmsProductCommissionMapper;

    @Override
    @Transactional
    public void batchUpdateCommission(PmsProductCommissionVo pmsProductCommissionVo) {
        String[] productIds = pmsProductCommissionVo.getProductIds().split(",");
        List<PmsProductCommission> allPmsCommissionList = new ArrayList<>();
        List<PmsProductCommission> pmsProductCommissions = pmsProductCommissionVo.getPmsProductCommissions();
        for (String productId : productIds){
            if(!CollectionUtils.isEmpty(pmsProductCommissions)){
                for(PmsProductCommission pmsProductCommission: pmsProductCommissions){
                    PmsProductCommission newPmsCommission = new PmsProductCommission();
                    pmsProductCommission.setProductId(Long.valueOf(productId));
                    pmsProductCommission.setCreateTime(new Date().getTime());
                    pmsProductCommission.setCreateDate(new Date());
                    pmsProductCommission.setUpdateTime(new Date().getTime());
                    pmsProductCommission.setUpdateDate(new Date());
                    BeanUtils.copyProperties(pmsProductCommission,newPmsCommission);
                    allPmsCommissionList.add(newPmsCommission);
                }
                this.deletePmsCommissionByProductId(Long.valueOf(productId));
            }
        }
        if(!CollectionUtils.isEmpty(allPmsCommissionList)){
            this.saveBatch(allPmsCommissionList);
        }
    }


    @Override
    @Transactional
    public void deletePmsCommissionByProductId(Long productId){
        pmsProductCommissionMapper.delete(new QueryWrapper<PmsProductCommission>().eq("product_id",productId));
    }
}
