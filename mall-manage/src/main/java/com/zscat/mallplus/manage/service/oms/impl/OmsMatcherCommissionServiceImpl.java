package com.zscat.mallplus.manage.service.oms.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.oms.IOmsMatcherCommissionService;
import com.zscat.mallplus.mbg.oms.entity.OmsMatcherCommission;
import com.zscat.mallplus.mbg.oms.mapper.OmsMatcherCommissionMapper;
import com.zscat.mallplus.mbg.oms.vo.OmsMatcherCommissionVo;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OmsMatcherCommissionServiceImpl extends ServiceImpl<OmsMatcherCommissionMapper,OmsMatcherCommission> implements IOmsMatcherCommissionService{

    @Autowired
    private OmsMatcherCommissionMapper omsMatcherCommissionMapper;

    @Override
    public Page<OmsMatcherCommissionVo> pageOmsMathcerCommissions(OmsMatcherCommissionVo omsMatcherCommissionVo) {
        Page<OmsMatcherCommissionVo> omsMatcherCommissionVoPage = new Page<>(omsMatcherCommissionVo.getPageNum(),omsMatcherCommissionVo.getPageSize());
        return omsMatcherCommissionMapper.pageOmsMathcerCommissions(omsMatcherCommissionVoPage,omsMatcherCommissionVo);
    }

    @Override
    public List<OmsMatcherCommissionVo> listOmsMathcerCommissions(OmsMatcherCommissionVo omsMatcherCommissionVo) {
        String settleStatus = omsMatcherCommissionVo.getSettleStatuses();
        if(!StringUtils.isEmpty(settleStatus)){
            String[] statusList = settleStatus.split(",");
            omsMatcherCommissionVo.setStatusList(statusList);
        }
        return omsMatcherCommissionMapper.listOmsMathcerCommissions(omsMatcherCommissionVo);
    }

    @Override
    @Transactional
    public void updateSettleStatus(List<OmsMatcherCommission> omsMatcherCommissions) {
        //根据老的订单的状态进行结算状态的变更
        if(!CollectionUtils.isEmpty(omsMatcherCommissions)){
            for(OmsMatcherCommission omsMatcherCommission : omsMatcherCommissions){
                omsMatcherCommission.setUpdateTime(new Date().getTime());
                omsMatcherCommission.setUpdateDate(new Date());
                if(MagicConstant.SETTLE_STAUTS_WAITE.equals(omsMatcherCommission.getStatus())){
                    omsMatcherCommission.setStatus(MagicConstant.SETTLE_STAUTS_SETTLED);
                }else if(MagicConstant.SETTLE_STAUTS_WAITE_PARTREFUND.equals(omsMatcherCommission.getStatus())){
                    omsMatcherCommission.setStatus(MagicConstant.SETTLE_STAUTS_SETTLED_PARTREFUND);
                }
            }
            this.updateBatchById(omsMatcherCommissions);
        }
    }
}
