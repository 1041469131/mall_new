package com.zscat.mallplus.mbg.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnSale;
import com.zscat.mallplus.mbg.oms.vo.OmsOrderSaleParam;
import com.zscat.mallplus.mbg.pms.vo.PmsProductVo;
import org.apache.ibatis.annotations.Param;

/**
 * 售后表对应的mapper
 */
public interface OmsOrderReturnSaleMapper extends BaseMapper<OmsOrderReturnSale> {

  Page<OmsOrderReturnSale> listByPage(Page<OmsOrderReturnSale> pmsProductPage,@Param("queryParam") OmsOrderSaleParam queryParam);
}