package com.zscat.mallplus.manage.service.oms.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.oms.IOmsOrderReturnSaleService;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.mbg.oms.entity.OmsOrder;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnSale;
import com.zscat.mallplus.mbg.oms.mapper.OmsOrderMapper;
import com.zscat.mallplus.mbg.oms.mapper.OmsOrderReturnSaleMapper;
import com.zscat.mallplus.mbg.oms.vo.OmsUpdateStatusParam;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class OmsOrderReturnSaleServiceImpl extends ServiceImpl<OmsOrderReturnSaleMapper,OmsOrderReturnSale> implements IOmsOrderReturnSaleService {

    @Autowired
    private OmsOrderReturnSaleMapper omsOrderReturnSaleMapper;

    @Autowired
    private OmsOrderMapper omsOrderMapper;

    @Override
    @Transactional
    public int updateStatus(Long id, Map<String,Object> saleMap) {
        OmsOrderReturnSale oldOmsOrderReturnSale = omsOrderReturnSaleMapper.selectById(id);
        OmsOrder omsOrder = omsOrderMapper.selectById(oldOmsOrderReturnSale.getOrderId());
        Integer saleStatus = (Integer) saleMap.get("saleStatus");//售后装填
        Integer orderStatus = omsOrder.getStatus();//订单状态
        Integer saleType = oldOmsOrderReturnSale.getType();//售后的类型
        OmsOrderReturnSale omsOrderReturnSale = new OmsOrderReturnSale();
        omsOrderReturnSale.setReturnAmount(saleMap.get("returnAmount") == null? BigDecimal.ZERO:(BigDecimal) saleMap.get("returnAmount"));
      if (saleStatus.equals(MagicConstant.RETURN_STATUS_REFUNDING)) {//退货中
            omsOrderReturnSale.setId(id);
            omsOrderReturnSale.setStatus(MagicConstant.RETURN_STATUS_REFUNDING);
            omsOrderReturnSale.setCompanyAddressId(saleMap.get("companyAddressId") == null?0L:(Long) saleMap.get("companyAddressId"));
            omsOrderReturnSale.setUpdateTime(new Date());
            omsOrderReturnSale.setHandleMan(UserUtils.getCurrentMember().getUsername());
            omsOrderReturnSale.setHandleNote((String)saleMap.get("note"));
        } else if (saleStatus.equals(MagicConstant.RETURN_STATUS_FINISHED)) {//已完成
            omsOrderReturnSale.setId(id);
            omsOrderReturnSale.setStatus(MagicConstant.RETURN_STATUS_FINISHED);
            omsOrderReturnSale.setReceiveTime(new Date());
            omsOrderReturnSale.setReceiveMan((String)saleMap.get("reviceMan"));
            omsOrderReturnSale.setReceiveNote((String)saleMap.get("receiveNote"));
            omsOrder.setStatus(MagicConstant.ORDER_STATUS_YET_SHUTDOWN);
            omsOrderMapper.updateById(omsOrder);
        } else {
            omsOrderReturnSale.setId(id);
            omsOrderReturnSale.setStatus(saleStatus);
            omsOrderReturnSale.setUpdateTime(new Date());
            omsOrderReturnSale.setHandleMan(UserUtils.getCurrentMember().getUsername());
            omsOrderReturnSale.setHandleNote((String)saleMap.get("note"));
        }
        return omsOrderReturnSaleMapper.updateById(omsOrderReturnSale);
    }
}
