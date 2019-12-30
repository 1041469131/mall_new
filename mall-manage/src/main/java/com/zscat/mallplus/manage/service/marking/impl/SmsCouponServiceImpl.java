package com.zscat.mallplus.manage.service.marking.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.marking.ISmsCouponProductCategoryRelationService;
import com.zscat.mallplus.manage.service.marking.ISmsCouponProductRelationService;
import com.zscat.mallplus.manage.service.marking.ISmsCouponService;
import com.zscat.mallplus.manage.service.ums.IUmsMemberService;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.mbg.marking.entity.SmsCoupon;
import com.zscat.mallplus.mbg.marking.entity.SmsCouponHistory;
import com.zscat.mallplus.mbg.marking.entity.SmsCouponProductCategoryRelation;
import com.zscat.mallplus.mbg.marking.entity.SmsCouponProductRelation;
import com.zscat.mallplus.mbg.marking.mapper.SmsCouponHistoryMapper;
import com.zscat.mallplus.mbg.marking.mapper.SmsCouponMapper;
import com.zscat.mallplus.mbg.marking.mapper.SmsCouponProductCategoryRelationMapper;
import com.zscat.mallplus.mbg.marking.mapper.SmsCouponProductRelationMapper;
import com.zscat.mallplus.mbg.marking.vo.SmsCouponHistoryDetail;
import com.zscat.mallplus.mbg.marking.vo.SmsCouponParam;
import com.zscat.mallplus.mbg.oms.vo.CartPromotionItem;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import com.zscat.mallplus.mbg.utils.CommonResult;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * <p>
 * 优惠卷表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Service
public class SmsCouponServiceImpl extends ServiceImpl<SmsCouponMapper, SmsCoupon> implements ISmsCouponService {

    @Resource
    private SmsCouponMapper couponMapper;
    @Resource
    private SmsCouponProductRelationMapper productRelationMapper;
    @Resource
    private SmsCouponProductCategoryRelationMapper productCategoryRelationMapper;
    @Resource
    private ISmsCouponProductRelationService productRelationDao;
    @Resource
    private ISmsCouponProductCategoryRelationService productCategoryRelationDao;
    @Resource
    private IUmsMemberService memberService;
    @Resource
    private SmsCouponHistoryMapper couponHistoryMapper;

    @Override
    public boolean saves(SmsCouponParam couponParam) {
        couponParam.setCount(couponParam.getPublishCount());
        couponParam.setUseCount(0);
        couponParam.setReceiveCount(0);
        //插入优惠券表
        couponMapper.insert(couponParam);
        //插入优惠券和商品关系表
        if (couponParam.getUseType().equals(MagicConstant.COUPON_USE_TYPE_PRODUCT)) {
            for (SmsCouponProductRelation productRelation : couponParam.getProductRelationList()) {
                productRelation.setCouponId(couponParam.getId());
            }
            productRelationDao.saveBatch(couponParam.getProductRelationList());
        }
        //插入优惠券和商品分类关系表
        if (couponParam.getUseType().equals(MagicConstant.COUPON_USE_TYPE_PRODUCT_CATEGORY)) {
            for (SmsCouponProductCategoryRelation couponProductCategoryRelation : couponParam.getProductCategoryRelationList()) {
                couponProductCategoryRelation.setCouponId(couponParam.getId());
            }
            productCategoryRelationDao.saveBatch(couponParam.getProductCategoryRelationList());
        }
        return true;
    }

    @Override
    public boolean updateByIds(SmsCouponParam couponParam) {
        couponParam.setId(couponParam.getId());
        int count = couponMapper.updateById(couponParam);
        //删除后插入优惠券和商品关系表
        if (couponParam.getUseType().equals(MagicConstant.COUPON_USE_TYPE_PRODUCT)) {
            for (SmsCouponProductRelation productRelation : couponParam.getProductRelationList()) {
                productRelation.setCouponId(couponParam.getId());
            }
            deleteProductRelation(couponParam.getId());
            productRelationDao.saveBatch(couponParam.getProductRelationList());
        }
        //删除后插入优惠券和商品分类关系表
        if (couponParam.getUseType().equals(MagicConstant.COUPON_USE_TYPE_PRODUCT_CATEGORY)) {
            for (SmsCouponProductCategoryRelation couponProductCategoryRelation : couponParam.getProductCategoryRelationList()) {
                couponProductCategoryRelation.setCouponId(couponParam.getId());
            }
            deleteProductCategoryRelation(couponParam.getId());
            productCategoryRelationDao.saveBatch(couponParam.getProductCategoryRelationList());
        }
        return true;
    }

    private void deleteProductCategoryRelation(Long id) {
        productCategoryRelationMapper.delete(new QueryWrapper<>(new SmsCouponProductCategoryRelation()).eq("coupon_id",id));
    }

    private void deleteProductRelation(Long id) {
        productRelationMapper.delete(new QueryWrapper<>(new SmsCouponProductRelation()).eq("coupon_id",id));
    }

    @Override
    public int delete(Long id) {
        //删除优惠券
        int count = couponMapper.deleteById(id);
        //删除商品关联
        deleteProductRelation(id);
        //删除商品分类关联
        deleteProductCategoryRelation(id);
        return count;
    }

    @Override
    public SmsCouponParam getItem(Long id) {
        return couponMapper.getItem(id);
    }

    @Override
    public List<SmsCoupon> selectAllCoupon(Long memberId){
        return couponMapper.selectNotRecive(memberId);
    }

    @Override
    public List<SmsCoupon> selectRecive(Long memberId){
        return  couponMapper.selectRecive(memberId);
    }

    @Override
    public List<SmsCoupon> selectAllCoupon(){
        SmsCoupon coupon = new SmsCoupon();
        coupon.setType(MagicConstant.COUPON_TYPE_ALL);
        return couponMapper.selectList(new QueryWrapper<>(coupon).gt("end_time",new Date()));
    }

    @Override
    public CommonResult getCouponById(Long couponId) {
        UmsMember currentMember = UserUtils.getCurrentUmsMember();
        //获取优惠券信息，判断数量
        SmsCoupon coupon = couponMapper.selectById(couponId);
        if (coupon == null) {
            return new CommonResult().failed("优惠券不存在");
        }
        if (coupon.getCount() <= 0) {
            return new CommonResult().failed("优惠券已经领完了");
        }
        Date now = new Date();
        if (now.after(coupon.getEndTime())) {
            return new CommonResult().failed("优惠券已过期");
        }
        //判断用户领取的优惠券数量是否超过限制
        SmsCouponHistory queryH = new SmsCouponHistory();
        queryH.setMemberId(currentMember.getId());
        queryH.setCouponId(couponId);

        int count = couponHistoryMapper.selectCount(new QueryWrapper<>(queryH));
        if (count >= coupon.getPerLimit()) {
            return new CommonResult().failed("您已经领取过该优惠券");
        }
        //生成领取优惠券历史
        SmsCouponHistory couponHistory = new SmsCouponHistory();
        couponHistory.setCouponId(couponId);
        couponHistory.setCouponCode(generateCouponCode(currentMember.getId()));
        couponHistory.setCreateTime(now);
        couponHistory.setMemberId(currentMember.getId());
        couponHistory.setMemberNickname(currentMember.getNickname());
        //主动领取
        couponHistory.setGetType(1);
        //未使用
        couponHistory.setUseStatus(0);
        couponHistory.setStartTime(coupon.getStartTime());
        couponHistory.setEndTime(coupon.getEndTime());
        couponHistory.setNote(coupon.getName()+":满"+coupon.getMinPoint()+"减"+ coupon.getAmount());
        couponHistoryMapper.insert(couponHistory);
        //修改优惠券表的数量、领取数量
        coupon.setCount(coupon.getCount() - 1);
        coupon.setReceiveCount(coupon.getReceiveCount() == null ? 1 : coupon.getReceiveCount() + 1);
        couponMapper.updateById(coupon);
        return new CommonResult().success("领取成功", null);
    }

    /**
     * 16位优惠码生成：时间戳后8位+4位随机数+用户id后4位
     */
    private String generateCouponCode(Long memberId) {
        StringBuilder sb = new StringBuilder();
        Long currentTimeMillis = System.currentTimeMillis();
        String timeMillisStr = currentTimeMillis.toString();
        sb.append(timeMillisStr.substring(timeMillisStr.length() - 8));
        for (int i = 0; i < 4; i++) {
            sb.append(new Random().nextInt(10));
        }
        String memberIdStr = memberId.toString();
        if (memberIdStr.length() <= 4) {
            sb.append(String.format("%04d", memberId));
        } else {
            sb.append(memberIdStr.substring(memberIdStr.length() - 4));
        }
        return sb.toString();
    }


    @Override
    public List<SmsCouponHistory> getCouponByUserStatus(Integer useStatus) {
        UmsMember currentMember = UserUtils.getCurrentUmsMember();
        SmsCouponHistory couponHistory = new SmsCouponHistory();
        couponHistory.setMemberId(currentMember.getId());

        if (useStatus != null) {
            couponHistory.setUseStatus(useStatus);
        }
        return couponHistoryMapper.selectList(new QueryWrapper<>(couponHistory));
    }

    @Override
    public List<SmsCouponHistoryDetail> getCouponHistoryDetailByCart(List<CartPromotionItem> cartItemList, Integer type) {
        UmsMember currentMember = memberService.getCurrentMember();
        Date now = new Date();
        //获取该用户所有优惠券
        List<SmsCouponHistoryDetail> allList = couponHistoryMapper.getDetailList(currentMember.getId());
        //根据优惠券使用类型来判断优惠券是否可用
        List<SmsCouponHistoryDetail> enableList = new ArrayList<>();
        List<SmsCouponHistoryDetail> disableList = new ArrayList<>();
        for (SmsCouponHistoryDetail couponHistoryDetail : allList) {
            Integer useType = couponHistoryDetail.getCoupon().getUseType();//使用类型：0->全场通用；1->指定分类；2->指定商品
            BigDecimal minPoint = couponHistoryDetail.getCoupon().getMinPoint();//使用门槛；0表示无门槛
            Date endTime = couponHistoryDetail.getCoupon().getEndTime();
            if (useType.equals(MagicConstant.COUPON_USE_TYPE_ALL)) {
                //0->全场通用
                //判断是否满足优惠起点
                //计算购物车商品的总价
                BigDecimal totalAmount = calcTotalAmount(cartItemList);
                if (now.before(endTime) && totalAmount.subtract(minPoint).intValue() >= 0) {
                    enableList.add(couponHistoryDetail);
                } else {
                    disableList.add(couponHistoryDetail);
                }
            } else if (useType.equals(MagicConstant.COUPON_USE_TYPE_PRODUCT_CATEGORY)) {
                //1->指定分类
                //计算指定分类商品的总价
                List<Long> productCategoryIds = new ArrayList<>();
                for (SmsCouponProductCategoryRelation categoryRelation : couponHistoryDetail.getCategoryRelationList()) {
                    productCategoryIds.add(categoryRelation.getProductCategoryId());
                }
                BigDecimal totalAmount = calcTotalAmountByproductCategoryId(cartItemList, productCategoryIds);
                if (now.before(endTime) && totalAmount.intValue() > 0 && totalAmount.subtract(minPoint).intValue() >= 0) {
                    enableList.add(couponHistoryDetail);
                } else {
                    disableList.add(couponHistoryDetail);
                }
            } else if (useType.equals(MagicConstant.COUPON_USE_TYPE_PRODUCT)) {
                //2->指定商品
                //计算指定商品的总价
                List<Long> productIds = new ArrayList<>();
                for (SmsCouponProductRelation productRelation : couponHistoryDetail.getProductRelationList()) {
                    productIds.add(productRelation.getProductId());
                }
                BigDecimal totalAmount = calcTotalAmountByProductId(cartItemList, productIds);
                if (now.before(endTime) && totalAmount.intValue() > 0 && totalAmount.subtract(minPoint).intValue() >= 0) {
                    enableList.add(couponHistoryDetail);
                } else {
                    disableList.add(couponHistoryDetail);
                }
            }
        }
        if (type.equals(1)) {
            return enableList;
        } else {
            return disableList;
        }
    }

    private BigDecimal calcTotalAmount(List<CartPromotionItem> cartItemList) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
            total = total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
        }
        return total;
    }

    private BigDecimal calcTotalAmountByproductCategoryId(List<CartPromotionItem> cartItemList, List<Long> productCategoryIds) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            if (productCategoryIds.contains(item.getProductCategoryId())) {
                BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
                total = total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
            }
        }
        return total;
    }

    private BigDecimal calcTotalAmountByProductId(List<CartPromotionItem> cartItemList, List<Long> productIds) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            if (productIds.contains(item.getProductId())) {
                BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
                total = total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
            }
        }
        return total;
    }
}
