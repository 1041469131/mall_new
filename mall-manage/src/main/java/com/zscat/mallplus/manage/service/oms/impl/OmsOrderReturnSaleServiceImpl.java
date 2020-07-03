package com.zscat.mallplus.manage.service.oms.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.oms.IOmsOrderReturnSaleService;
import com.zscat.mallplus.manage.service.sys.ISysMatcherStatisticsService;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.mbg.oms.entity.OmsOrder;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnApply;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnSale;
import com.zscat.mallplus.mbg.oms.mapper.OmsOrderMapper;
import com.zscat.mallplus.mbg.oms.mapper.OmsOrderReturnApplyMapper;
import com.zscat.mallplus.mbg.oms.mapper.OmsOrderReturnSaleMapper;
import com.zscat.mallplus.mbg.oms.vo.OmsOrderReturnSaleVO;
import com.zscat.mallplus.mbg.oms.vo.OmsOrderSaleParam;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OmsOrderReturnSaleServiceImpl extends ServiceImpl<OmsOrderReturnSaleMapper, OmsOrderReturnSale> implements
  IOmsOrderReturnSaleService {

  @Autowired
  private OmsOrderReturnSaleMapper omsOrderReturnSaleMapper;

  @Autowired
  private OmsOrderMapper omsOrderMapper;

  @Autowired
  private ISysMatcherStatisticsService iSysMatcherStatisticsService;

  @Autowired
  private OmsOrderReturnApplyMapper omsOrderReturnApplyMapper;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public int updateStatus(OmsOrderReturnSale returnSale) {
    OmsOrderReturnSale oldOmsOrderReturnSale = omsOrderReturnSaleMapper.selectById(returnSale.getId());
    List<OmsOrderReturnApply> omsOrderReturnApplies = omsOrderReturnApplyMapper
      .selectList(new QueryWrapper<OmsOrderReturnApply>().lambda().eq(OmsOrderReturnApply::getSaleId, returnSale.getId()));
    omsOrderReturnApplies.forEach(omsOrderReturnApply -> {
      omsOrderReturnApply.setStatus(returnSale.getStatus());
      omsOrderReturnApplyMapper.updateById(omsOrderReturnApply);
    });
    OmsOrder omsOrder = omsOrderMapper.selectById(oldOmsOrderReturnSale.getOrderId());
    //售后装填
    returnSale.setHandleMan(UserUtils.getCurrentMember().getUsername());
    returnSale.setUpdateTime(new Date());
    iSysMatcherStatisticsService.refreshMatcherStatisticsByOrder(omsOrder);
    return omsOrderReturnSaleMapper.updateById(returnSale);
  }

  @Override
  public Page<OmsOrderReturnSale> listByPage(OmsOrderSaleParam queryParam) {
    Page<OmsOrderReturnSale> page = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());
    return omsOrderReturnSaleMapper.listByPage(page, queryParam);
  }

  @Override
  public Page<OmsOrderReturnSaleVO> listVoByPage(OmsOrderSaleParam queryParam) {
    Page<OmsOrderReturnSale> page = listByPage(queryParam);
    List<OmsOrderReturnSaleVO> omsOrderReturnSaleVOS = page.getRecords().stream().map(sale -> {
      OmsOrderReturnSaleVO omsOrderSaleParamVO = new OmsOrderReturnSaleVO();
      omsOrderSaleParamVO.setOmsOrderReturnSale(sale);
      List<OmsOrderReturnApply> omsOrderReturnApplyList = omsOrderReturnApplyMapper
        .selectList(new QueryWrapper<OmsOrderReturnApply>().lambda().eq(OmsOrderReturnApply::getSaleId, sale.getId()));
      omsOrderSaleParamVO.setOmsOrderReturnApplyList(omsOrderReturnApplyList);
      return omsOrderSaleParamVO;
    }).collect(Collectors.toList());
    Page<OmsOrderReturnSaleVO> omsOrderReturnSaleVOPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
    omsOrderReturnSaleVOPage.setRecords(omsOrderReturnSaleVOS);
    return omsOrderReturnSaleVOPage;
  }
}
