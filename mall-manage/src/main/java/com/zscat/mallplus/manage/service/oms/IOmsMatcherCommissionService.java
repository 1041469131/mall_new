package com.zscat.mallplus.manage.service.oms;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.oms.entity.OmsMatcherCommission;
import com.zscat.mallplus.mbg.oms.vo.OmsMatcherCommissionVo;

import java.util.List;

/**
 * 订单对应的分佣金额服务接口
 */
public interface IOmsMatcherCommissionService extends IService<OmsMatcherCommission>{

    Page<OmsMatcherCommissionVo> pageOmsMathcerCommissions(OmsMatcherCommissionVo omsMatcherCommissionVo);

    List<OmsMatcherCommissionVo> listOmsMathcerCommissions(OmsMatcherCommissionVo omsMatcherCommissionVo);

    void updateSettleStatus(List<OmsMatcherCommission> orderIds);
}
