package com.zscat.mallplus.manage.service.oms.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.oms.IOmsOrderItemService;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderItem;
import com.zscat.mallplus.mbg.oms.mapper.OmsOrderItemMapper;
import com.zscat.mallplus.mbg.oms.vo.OmsOrderItemVo;
import com.zscat.mallplus.mbg.pms.entity.PmsProductCommission;
import com.zscat.mallplus.mbg.pms.mapper.PmsProductCommissionMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单中所包含的商品 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
@Service
public class OmsOrderItemServiceImpl extends ServiceImpl<OmsOrderItemMapper, OmsOrderItem> implements IOmsOrderItemService {

    @Autowired
    private OmsOrderItemMapper omsOrderItemMapper;

    @Autowired
    private PmsProductCommissionMapper pmsProductCommissionMapper;

    @Override
    public List<OmsOrderItemVo> queryPrfitProportion(Long omsOrderId) {
        List<OmsOrderItem> omsOrderItems = omsOrderItemMapper.selectList(new QueryWrapper<OmsOrderItem>().eq("order_id",omsOrderId));
        List<OmsOrderItemVo> omsOrderItemVos = null;
        if(!CollectionUtils.isEmpty(omsOrderItems)){
            omsOrderItemVos = new ArrayList<>();
            for(OmsOrderItem omsOrderItem : omsOrderItems){
                OmsOrderItemVo omsOrderItemVo = new OmsOrderItemVo();
                BeanUtils.copyProperties(omsOrderItem,omsOrderItemVo);
                List<PmsProductCommission> pmsProductCommissions = pmsProductCommissionMapper.selectList(new QueryWrapper<PmsProductCommission>().eq("product_id",omsOrderItem.getProductId()));
                omsOrderItemVo.setPmsProductCommissions(pmsProductCommissions);
                omsOrderItemVos.add(omsOrderItemVo);
            }
        }
        return omsOrderItemVos;
    }
}
