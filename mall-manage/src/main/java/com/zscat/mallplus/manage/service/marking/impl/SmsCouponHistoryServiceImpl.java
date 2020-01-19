package com.zscat.mallplus.manage.service.marking.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.marking.ISmsCouponHistoryService;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.mbg.marking.entity.SmsCouponHistory;
import com.zscat.mallplus.mbg.marking.mapper.SmsCouponHistoryMapper;
import com.zscat.mallplus.mbg.marking.vo.SmsCouponHistoryDetail;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 优惠券使用、领取历史表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Service
public class SmsCouponHistoryServiceImpl extends ServiceImpl<SmsCouponHistoryMapper, SmsCouponHistory> implements ISmsCouponHistoryService {

    @Autowired
    private SmsCouponHistoryMapper smsCouponHistoryMapper;

    @Override
    public List<SmsCouponHistoryDetail> listByStatus(Integer useStatus) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("useStatus", useStatus);
        paramMap.put("memberId", UserUtils.getCurrentUmsMember().getId());
        return smsCouponHistoryMapper.getDetailList(paramMap);
    }

    @Override
    @Transactional
    public void updateCouponStatus() {
        Long memberId = UserUtils.getCurrentUmsMember().getId();
        List<SmsCouponHistory> smsCouponHistories = smsCouponHistoryMapper.selectList(new QueryWrapper<SmsCouponHistory>().eq("member_id", memberId));
        if(!CollectionUtils.isEmpty(smsCouponHistories)){
            for (SmsCouponHistory smsCouponHistory : smsCouponHistories){
                if(smsCouponHistory.getEndTime().before(new Date()) && MagicConstant.COUPON_USE_STATUS_4_NO == smsCouponHistory.getUseStatus()){
                    smsCouponHistory.setUseStatus(MagicConstant.COUPON_USE_STATUS_4_EXPIR);
                    smsCouponHistoryMapper.updateById(smsCouponHistory);
                }
            }
        }
    }
}
