package com.zscat.mallplus.manage.service.oms.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.oms.IOmsOrderReturnSaleService;
import com.zscat.mallplus.mbg.oms.entity.OmsOrder;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnSale;
import com.zscat.mallplus.mbg.oms.mapper.OmsOrderMapper;
import com.zscat.mallplus.mbg.oms.mapper.OmsOrderReturnSaleMapper;
import com.zscat.mallplus.mbg.oms.vo.OmsUpdateStatusParam;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OmsOrderReturnSaleServiceImpl extends ServiceImpl<OmsOrderReturnSaleMapper,OmsOrderReturnSale> implements IOmsOrderReturnSaleService {

    @Autowired
    private OmsOrderReturnSaleMapper omsOrderReturnSaleMapper;

    @Autowired
    private OmsOrderMapper omsOrderMapper;

    @Override
    public int updateStatus(Long id, OmsUpdateStatusParam statusParam) {
        OmsOrderReturnSale oldOmsOrderReturnSale = omsOrderReturnSaleMapper.selectById(id);
        OmsOrder omsOrder = omsOrderMapper.selectById(oldOmsOrderReturnSale.getOrderId());
        Integer status = statusParam.getStatus();//售后装填
        Integer orderStatus = omsOrder.getStatus();//订单状态
        Integer saleType = oldOmsOrderReturnSale.getType();//售后的类型
        OmsOrderReturnSale omsOrderReturnSale = new OmsOrderReturnSale();
      if (status.equals(MagicConstant.RETURN_STATUS_WAITSEND)) {//寄回退货
            omsOrderReturnSale.setId(id);
            omsOrderReturnSale.setStatus(MagicConstant.RETURN_STATUS_WAITSEND);
            omsOrderReturnSale.setCompanyAddressId(statusParam.getCompanyAddressId());
            omsOrderReturnSale.setUpdateTime(new Date());
            omsOrderReturnSale.setHandleMan(statusParam.getHandleMan());
            omsOrderReturnSale.setHandleNote(statusParam.getHandleNote());
        } else if (status.equals(MagicConstant.RETURN_STATUS_FINISHED)) {//已完成
            omsOrderReturnSale.setId(id);
            omsOrderReturnSale.setStatus(MagicConstant.RETURN_STATUS_FINISHED);
            omsOrderReturnSale.setReceiveTime(new Date());
            omsOrderReturnSale.setReceiveMan(statusParam.getReceiveMan());
            omsOrderReturnSale.setReceiveNote(statusParam.getReceiveNote());
        } else if (status.equals(MagicConstant.RETURN_STATUS_REFUSE)||(saleType == MagicConstant.RETURN_APPLY_TYPE_REFUND &&
              orderStatus.equals(MagicConstant.ORDER_STATUS_YET_SEND))) {//当状态已拒绝或者类型是退款并且订单状态是已发货的状态
            omsOrderReturnSale.setId(id);
            omsOrderReturnSale.setStatus(MagicConstant.RETURN_STATUS_REFUSE);
            omsOrderReturnSale.setUpdateTime(new Date());
            omsOrderReturnSale.setHandleMan(statusParam.getHandleMan());
            omsOrderReturnSale.setHandleNote(statusParam.getHandleNote());
        } else {
            omsOrderReturnSale.setId(id);
            omsOrderReturnSale.setStatus(status);
            omsOrderReturnSale.setUpdateTime(new Date());
            omsOrderReturnSale.setHandleMan(statusParam.getHandleMan());
            omsOrderReturnSale.setHandleNote(statusParam.getHandleNote());
        }
        return omsOrderReturnSaleMapper.updateById(omsOrderReturnSale);
    }
}
