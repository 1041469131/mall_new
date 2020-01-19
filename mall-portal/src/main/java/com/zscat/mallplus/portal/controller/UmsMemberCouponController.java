package com.zscat.mallplus.portal.controller;


import com.zscat.mallplus.manage.service.marking.ISmsCouponHistoryService;
import com.zscat.mallplus.manage.service.marking.ISmsCouponService;
import com.zscat.mallplus.manage.service.oms.IOmsCartItemService;
import com.zscat.mallplus.manage.service.ums.IUmsMemberService;
import com.zscat.mallplus.mbg.annotation.IgnoreAuth;
import com.zscat.mallplus.mbg.marking.entity.SmsCoupon;
import com.zscat.mallplus.mbg.marking.entity.SmsCouponHistory;
import com.zscat.mallplus.mbg.marking.vo.SmsCouponHistoryDetail;
import com.zscat.mallplus.mbg.oms.vo.CartPromotionItem;
import com.zscat.mallplus.mbg.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户优惠券管理Controller
 * https://github.com/shenzhuan/mallplus on 2018/8/29.
 */
@Controller
@Api(tags = "UmsMemberCouponController", description = "用户优惠券管理")
@RequestMapping("/api/member/coupon")
public class UmsMemberCouponController {

    @Autowired
    private IUmsMemberService memberService;

    @Autowired
    private ISmsCouponService couponService;

    @Autowired
    private IOmsCartItemService cartItemService;

    @Autowired
    private ISmsCouponHistoryService iSmsCouponHistoryService;

    @ApiOperation("领取指定优惠券")
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add( Long couponId) {
        return couponService.getCouponById(couponId);
    }

    @ApiOperation("获取用户优惠券列表")
    @ApiImplicitParam(name = "useStatus", value = "优惠券筛选类型:0->未使用；1->已使用；2->已过期",
            allowableValues = "0,1,2", paramType = "query", dataType = "integer")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @IgnoreAuth
    public Object list(@RequestParam(value = "useStatus", required = false) Integer useStatus) {
        iSmsCouponHistoryService.updateCouponStatus();
        List<SmsCouponHistoryDetail> couponHistoryDetails = iSmsCouponHistoryService.listByStatus(useStatus);
        return new CommonResult().success(couponHistoryDetails);
    }

    /**
     * 所有可领取的优惠券
     * @return
     */
    @ApiOperation("所有可领取的优惠券")
    @RequestMapping(value = "/alllist", method = RequestMethod.GET)
    @ResponseBody
    public Object alllist() {
        List<SmsCoupon> couponList = couponList = couponService.selectAllCoupon();
        return new CommonResult().success(couponList);
    }


    @ApiOperation("获取登录会员购物车的相关优惠券")
    @ApiImplicitParam(name = "type", value = "使用可用:0->不可用；1->可用",
            defaultValue = "1", allowableValues = "0,1", paramType = "query", dataType = "integer")
    @RequestMapping(value = "/list/cart/{type}", method = RequestMethod.GET)
    @ResponseBody
    public Object listCart(@PathVariable Integer type) {
        List<CartPromotionItem> cartPromotionItemList = cartItemService.listPromotion(memberService.getCurrentMember().getId(),null);
        List<SmsCouponHistoryDetail> couponHistoryList = couponService.getCouponHistoryDetailByCart(cartPromotionItemList, type);
        return new CommonResult().success(couponHistoryList);
    }

}
