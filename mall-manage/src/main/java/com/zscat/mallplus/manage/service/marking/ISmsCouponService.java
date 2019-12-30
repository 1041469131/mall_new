package com.zscat.mallplus.manage.service.marking;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.marking.entity.SmsCoupon;
import com.zscat.mallplus.mbg.marking.entity.SmsCouponHistory;
import com.zscat.mallplus.mbg.marking.vo.SmsCouponHistoryDetail;
import com.zscat.mallplus.mbg.marking.vo.SmsCouponParam;
import com.zscat.mallplus.mbg.oms.vo.CartPromotionItem;
import com.zscat.mallplus.mbg.utils.CommonResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 优惠卷表 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface ISmsCouponService extends IService<SmsCoupon> {

    boolean saves(SmsCouponParam entity);

    boolean updateByIds(SmsCouponParam entity);

    /**
     * 获取优惠券详情
     *
     * @param id 优惠券表id
     */
    SmsCouponParam getItem(Long id);

    /**
     * 根据优惠券id删除优惠券
     */
    @Transactional
    int delete(Long id);

    /**
     * 会员添加优惠券
     */
    @Transactional
    CommonResult getCouponById(Long couponId);

    /**
     * 获取优惠券列表
     *
     * @param useStatus 优惠券的使用状态
     */
    List<SmsCouponHistory> getCouponByUserStatus(Integer useStatus);

    /**
     * 根据购物车信息获取可用优惠券
     * @param cartItemList
     * @param type(是否可用 1-表示可用)
     * @return
     */
    List<SmsCouponHistoryDetail> getCouponHistoryDetailByCart(List<CartPromotionItem> cartItemList, Integer type);


    List<SmsCoupon> selectAllCoupon(Long memberId);
    List<SmsCoupon> selectRecive(Long memberId);

    List<SmsCoupon> selectAllCoupon();

}
