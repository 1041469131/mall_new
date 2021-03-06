package com.zscat.mallplus.manage.service.oms;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnReason;

import java.util.List;

/**
 * <p>
 * 退货原因表 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
public interface IOmsOrderReturnReasonService extends IService<OmsOrderReturnReason> {
    /**
     * 批量修改退货原因状态
     */
    int updateStatus(List<Long> ids, Integer status);
}
