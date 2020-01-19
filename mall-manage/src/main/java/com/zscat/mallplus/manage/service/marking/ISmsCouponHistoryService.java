package com.zscat.mallplus.manage.service.marking;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.marking.entity.SmsCouponHistory;
import com.zscat.mallplus.mbg.marking.vo.SmsCouponHistoryDetail;

import java.util.List;

/**
 * <p>
 * 优惠券使用、领取历史表 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface ISmsCouponHistoryService extends IService<SmsCouponHistory> {

    /**
     * 根据用户的优惠券状态进行优惠券的过滤
     * @param useStatus
     * @return
     */
    List<SmsCouponHistoryDetail> listByStatus(Integer useStatus);

    void updateCouponStatus();
}
