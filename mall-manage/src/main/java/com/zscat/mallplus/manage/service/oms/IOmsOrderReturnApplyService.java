package com.zscat.mallplus.manage.service.oms;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnApply;
import com.zscat.mallplus.mbg.oms.vo.OmsOrderReturnApplyVO;
import java.util.List;

/**
 * <p>
 * 订单退货申请 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
public interface IOmsOrderReturnApplyService extends IService<OmsOrderReturnApply> {

  List<OmsOrderReturnApplyVO>  listBySaleId(Long saleId);
}
