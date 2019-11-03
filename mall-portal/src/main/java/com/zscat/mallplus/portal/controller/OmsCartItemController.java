package com.zscat.mallplus.portal.controller;


import com.zscat.mallplus.manage.service.oms.IOmsCartItemService;
import com.zscat.mallplus.manage.service.pms.IPmsSkuStockService;
import com.zscat.mallplus.manage.service.ums.IUmsMemberService;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.mbg.oms.entity.OmsCartItem;
import com.zscat.mallplus.mbg.oms.vo.CartProduct;
import com.zscat.mallplus.mbg.oms.vo.CartPromotionItem;
import com.zscat.mallplus.mbg.pms.entity.PmsSkuStock;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import com.zscat.mallplus.mbg.utils.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 购物车管理Controller
 * https://github.com/shenzhuan/mallplus on 2018/8/2.
 */
@Controller
@Api(tags = "OmsCartItemController", description = "购物车管理")
@RequestMapping("/api/cart")
public class OmsCartItemController {

    @Autowired
    private IOmsCartItemService cartItemService;

    @Autowired
    private IPmsSkuStockService pmsSkuStockService;


    @ApiOperation("添加商品到购物车")
    @RequestMapping(value = "/addCart")
    @ResponseBody
    public Object addCart(@RequestParam(value = "id", defaultValue = "0") Long id,
                          @RequestParam(value = "count", defaultValue = "1") Integer count) {
        UmsMember umsMember = UserUtils.getCurrentUmsMember();
        PmsSkuStock pmsSkuStock = pmsSkuStockService.getById(id);
        if (pmsSkuStock != null && (pmsSkuStock.getStock()-pmsSkuStock.getLockStock()) > 0) {
            OmsCartItem cartItem = new OmsCartItem();
            cartItem.setPrice(pmsSkuStock.getPrice());
            cartItem.setProductId(pmsSkuStock.getProductId());
            cartItem.setProductSkuCode(pmsSkuStock.getSkuCode());
            cartItem.setQuantity(count);
            cartItem.setProductSkuId(id);
            cartItem.setProductAttr(pmsSkuStock.getMeno());
            cartItem.setProductPic(pmsSkuStock.getPic());
            cartItem.setSp1(pmsSkuStock.getSp1());
            cartItem.setSp2(pmsSkuStock.getSp2());
            cartItem.setSp3(pmsSkuStock.getSp3());
            cartItem.setMemberId(UserUtils.getCurrentUmsMember().getId());
            OmsCartItem omsCartItem = cartItemService.addCart(cartItem);
            return new CommonResult().success(omsCartItem.getId());

        }
        return new CommonResult().failed("暂无库存");
    }

    @ApiOperation("获取某个会员的购物车列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Map<String,List<OmsCartItem>>> list() {
        UmsMember umsMember = UserUtils.getCurrentUmsMember();
        if (umsMember != null && umsMember.getId() != null) {
            Map<String,List<OmsCartItem>> cartMap = cartItemService.queryCartMap(umsMember.getId());
            return new CommonResult().success(cartMap);
        }
        return new CommonResult<>().failed("查询商品购物车列表失败");
    }

    @ApiOperation("获取某个会员的购物车列表,包括促销信息")
    @RequestMapping(value = "/list/promotion", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Map<String,List<CartPromotionItem>>> listPromotion() {
        Map<String,List<CartPromotionItem>> cartPromotionItemMap = cartItemService.mapCartPromotionItem(UserUtils.getCurrentUmsMember().getId());
        return new CommonResult().success(cartPromotionItemMap);
    }

    @ApiOperation("修改购物车中某个商品的数量")
    @RequestMapping(value = "/update/quantity", method = RequestMethod.GET)
    @ResponseBody
    public Object updateQuantity(@RequestParam Long id,
                                 @RequestParam Integer quantity) {
        OmsCartItem omsCartItem = cartItemService.getById(id);
        if(omsCartItem != null){
            PmsSkuStock pmsSkuStock = pmsSkuStockService.getById(omsCartItem.getProductSkuId());
            if(pmsSkuStock.getStock() < quantity){
                return new CommonResult<>().failed("库存数量不足");
            }
            int count = cartItemService.updateQuantity(id, UserUtils.getCurrentUmsMember().getId(), quantity);
            if (count > 0) {
                return new CommonResult().success("修改数量成功");
            }
        }
        return new CommonResult().failed();
    }

    @ApiOperation("获取购物车中某个商品的规格,用于重选规格")
    @RequestMapping(value = "/getProduct/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public Object getCartProduct(@PathVariable Long productId) {
        CartProduct cartProduct = cartItemService.getCartProduct(productId);
        return new CommonResult().success(cartProduct);
    }

    @ApiOperation("修改购物车中商品的规格")
    @RequestMapping(value = "/update/attr", method = RequestMethod.POST)
    @ResponseBody
    public Object updateAttr(@RequestBody OmsCartItem cartItem) {
        int count = cartItemService.updateAttr(cartItem);
        if (count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("删除购物车中的某个商品")
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(String cart_id_list) {
        if (StringUtils.isEmpty(cart_id_list)){
            return new CommonResult().failed("参数为空");
        }
        List<Long> resultList = new ArrayList<>(cart_id_list.split(",").length);
        for (String s : cart_id_list.split(",")) {
            resultList.add(Long.valueOf(s));
        }
        int count = cartItemService.delete(UserUtils.getCurrentUmsMember().getId(), resultList);
        if (count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }

    @ApiOperation("清空购物车")
    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    @ResponseBody
    public Object clear() {
        int count = cartItemService.clear(UserUtils.getCurrentUmsMember().getId());
        if (count > 0) {
            return new CommonResult().success(count);
        }
        return new CommonResult().failed();
    }


}
