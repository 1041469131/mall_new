package com.zscat.mallplus.mbg.marking.mapper;

import com.zscat.mallplus.mbg.marking.entity.SmsCouponHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zscat.mallplus.mbg.marking.vo.SmsCouponHistoryDetail;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 优惠券使用、领取历史表 Mapper 接口
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface SmsCouponHistoryMapper extends BaseMapper<SmsCouponHistory> {

    List<SmsCouponHistoryDetail> getDetailList(Map<String,Object> paramMap);

    /**
     * 获取优惠券的总的金额
     * @param recommendedId
     * @return
     */
    BigDecimal getTotalAmout(Long recommendedId);
}
