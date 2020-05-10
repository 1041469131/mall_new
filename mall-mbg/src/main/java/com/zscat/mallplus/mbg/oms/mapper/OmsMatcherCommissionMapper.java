package com.zscat.mallplus.mbg.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zscat.mallplus.mbg.oms.entity.OmsMatcherCommission;
import com.zscat.mallplus.mbg.oms.vo.OmsMatcherCommissionVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OmsMatcherCommissionMapper extends BaseMapper<OmsMatcherCommission>{

    Page<OmsMatcherCommissionVo> pageOmsMathcerCommissions(Page<OmsMatcherCommissionVo> omsMatcherCommissionVoPage, @Param("matcherCommission") OmsMatcherCommissionVo omsMatcherCommissionVo);

    List<OmsMatcherCommissionVo> listOmsMathcerCommissions(@Param("matcherCommission")OmsMatcherCommissionVo omsMatcherCommissionVo);
}