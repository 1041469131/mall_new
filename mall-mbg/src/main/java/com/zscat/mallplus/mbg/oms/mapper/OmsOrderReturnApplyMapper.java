package com.zscat.mallplus.mbg.oms.mapper;

import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnApply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.mbg.oms.vo.OmsOrderReturnApplyVO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 订单退货申请 Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
public interface OmsOrderReturnApplyMapper extends BaseMapper<OmsOrderReturnApply> {
   List<OmsOrderReturnApplyVO> listBySaleId(@Param("saleId") Long saleId);
}
