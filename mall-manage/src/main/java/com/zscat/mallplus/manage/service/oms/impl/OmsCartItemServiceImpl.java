package com.zscat.mallplus.manage.service.oms.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.oms.IOmsCartItemService;
import com.zscat.mallplus.manage.service.ums.IUmsMemberService;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.mbg.oms.entity.OmsCartItem;
import com.zscat.mallplus.mbg.oms.mapper.OmsCartItemMapper;
import com.zscat.mallplus.mbg.oms.vo.CartProduct;
import com.zscat.mallplus.mbg.oms.vo.CartPromotionItem;
import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import com.zscat.mallplus.mbg.pms.entity.PmsProductFullReduction;
import com.zscat.mallplus.mbg.pms.entity.PmsProductLadder;
import com.zscat.mallplus.mbg.pms.entity.PmsSkuStock;
import com.zscat.mallplus.mbg.pms.mapper.PmsProductMapper;
import com.zscat.mallplus.mbg.pms.vo.PromotionProduct;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * <p>
 * 购物车表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
@Service
public class OmsCartItemServiceImpl extends ServiceImpl<OmsCartItemMapper, OmsCartItem> implements IOmsCartItemService {

  @Autowired
  private OmsCartItemMapper cartItemMapper;
  @Autowired
  private IUmsMemberService memberService;
  @Autowired
  private PmsProductMapper pmsProductMapper;

  @Override
  public OmsCartItem add(OmsCartItem cartItem) {
    UmsMember currentMember = UserUtils.getCurrentUmsMember();
    cartItem.setMemberId(currentMember.getId());
    cartItem.setMemberNickname(currentMember.getNickname());
    cartItem.setDeleteStatus(MagicConstant.DELETE_NOT);
    PmsProduct pmsProduct = pmsProductMapper.selectById(cartItem.getProductId());
    if (org.apache.commons.lang.StringUtils.isBlank(cartItem.getProductPic())) {
      cartItem.setProductPic(pmsProduct.getPic());
    }
    cartItem.setProductBrand(pmsProduct.getBrandName());
    cartItem.setProductName(pmsProduct.getName());
    cartItem.setProductSn(pmsProduct.getProductSn());
    cartItem.setProductSubTitle(pmsProduct.getSubTitle());
    cartItem.setProductCategoryId(pmsProduct.getProductCategoryId());
    OmsCartItem existCartItem = getCartItem(cartItem);
    if (existCartItem == null) {
      cartItem.setCreateDate(new Date());
      cartItemMapper.insert(cartItem);
    } else {
      cartItem.setModifyDate(new Date());
      existCartItem.setQuantity(existCartItem.getQuantity() + cartItem.getQuantity());
      existCartItem.setProductSkuId(cartItem.getProductSkuId());
      existCartItem.setProductSkuCode(cartItem.getProductSkuCode());
      cartItemMapper.updateById(existCartItem);
      return existCartItem;
    }
    return cartItem;
  }

  /**
   * 根据会员id,商品id和规格获取购物车中商品
   */
  private OmsCartItem getCartItem(OmsCartItem cartItem) {
    OmsCartItem example = new OmsCartItem();
    example.setProductId(cartItem.getProductId());
    example.setDeleteStatus(MagicConstant.DELETE_NOT);
    example.setMemberId(cartItem.getMemberId());

    if (!StringUtils.isEmpty(cartItem.getSp1())) {
      example.setSp1(cartItem.getSp1());
    }
    if (!StringUtils.isEmpty(cartItem.getSp2())) {
      example.setSp2(cartItem.getSp2());
    }
    if (!StringUtils.isEmpty(cartItem.getSp3())) {
      example.setSp3(cartItem.getSp3());
    }
    List<OmsCartItem> cartItemList = cartItemMapper.selectList(new QueryWrapper<>(example));
    if (!CollectionUtils.isEmpty(cartItemList)) {
      return cartItemList.get(0);
    }
    return null;
  }

  @Override
  public List<OmsCartItem> list(Long memberId, List<Long> ids) {

    OmsCartItem example = new OmsCartItem();
    example.setMemberId(memberId);
    example.setDeleteStatus(0);
    if (ids != null && ids.size() > 0) {
      return cartItemMapper.selectList(new QueryWrapper<>(example).in("id", ids));
    }
    return cartItemMapper.selectList(new QueryWrapper<>(example));
  }

  @Override
  public OmsCartItem selectById(Long id) {
    return cartItemMapper.selectById(id);
  }

  @Override
  public List<CartPromotionItem> listPromotion(Long memberId, List<Long> ids) {
    //购物车列表
    List<OmsCartItem> cartItemList = list(memberId, ids);
    List<CartPromotionItem> cartPromotionItemList = new ArrayList<>();
    if (!CollectionUtils.isEmpty(cartItemList)) {
      cartPromotionItemList = this.calcCartPromotion(cartItemList);
    }
    return cartPromotionItemList;
  }

  @Override
  public int updateQuantity(Long id, Long memberId, Integer quantity) {
    OmsCartItem cartItem = new OmsCartItem();
    cartItem.setQuantity(quantity);
    QueryWrapper example = new QueryWrapper();
    example.eq("delete_status", 0);
    example.eq("member_id", memberId);
    example.eq("id", id);

    return cartItemMapper.update(cartItem, example);
  }

  @Override
  public int delete(Long memberId, List<Long> ids) {
    OmsCartItem record = new OmsCartItem();
    record.setDeleteStatus(MagicConstant.DELETE_YET);
    QueryWrapper<OmsCartItem> example = new QueryWrapper<OmsCartItem>();
    example.in("id", ids).eq("member_id", memberId);
    return cartItemMapper.update(record, example);
  }

  @Override
  public CartProduct getCartProduct(Long productId) {
    return pmsProductMapper.getCartProduct(productId);
  }

  @Override
  public int updateAttr(OmsCartItem cartItem) {
    //删除原购物车信息
    OmsCartItem updateCart = new OmsCartItem();
    updateCart.setId(cartItem.getId());
    updateCart.setModifyDate(new Date());
    updateCart.setDeleteStatus(MagicConstant.DELETE_YET);
    cartItemMapper.updateById(updateCart);
    add(cartItem);
    return 1;
  }

  @Override
  public int clear(Long memberId) {
    OmsCartItem record = new OmsCartItem();
    record.setDeleteStatus(MagicConstant.DELETE_YET);
    QueryWrapper<OmsCartItem> example = new QueryWrapper<OmsCartItem>();
    example.eq("memberId", memberId);
    return cartItemMapper.update(record, example);
  }

  @Override
  public OmsCartItem addCart(OmsCartItem cartItem) {
    UmsMember currentMember = UserUtils.getCurrentUmsMember();
    cartItem.setMemberId(currentMember.getId());
    cartItem.setMemberNickname(currentMember.getNickname());
    cartItem.setDeleteStatus(0);
    PmsProduct pmsProduct = pmsProductMapper.selectById(cartItem.getProductId());
    if (org.apache.commons.lang.StringUtils.isBlank(cartItem.getProductPic())) {
      cartItem.setProductPic(pmsProduct.getPic());
    }
    cartItem.setProductBrand(pmsProduct.getBrandName());
    cartItem.setProductName(pmsProduct.getName());
    cartItem.setProductSn(pmsProduct.getProductSn());
    cartItem.setProductSubTitle(pmsProduct.getSubTitle());
    cartItem.setProductCategoryId(pmsProduct.getProductCategoryId());
    OmsCartItem existCartItem = getCartItem(cartItem);
    if (existCartItem == null) {
      cartItem.setCreateDate(new Date());
      cartItemMapper.insert(cartItem);
    } else {
      cartItem.setModifyDate(new Date());
      existCartItem.setQuantity(existCartItem.getQuantity() + cartItem.getQuantity());
      cartItemMapper.updateById(existCartItem);
      return existCartItem;
    }
    return cartItem;
  }


  /**
   * 如果该商品有优惠价格，则在优惠价格的基础上进行计算商品的价格，如果没有优惠就按照原价进行商品的计算
   */
  @Override
  public List<CartPromotionItem> calcCartPromotion(List<OmsCartItem> cartItemList) {
    //1.先根据productId对CartItem进行分组，以spu为单位进行计算优惠
    Map<Long, List<OmsCartItem>> productCartMap = cartItemList.stream().collect(Collectors.groupingBy(OmsCartItem::getProductId));
    //2.查询所有商品的优惠相关信息
    Map<Long, PromotionProduct> promotionProductMap = pmsProductMapper
      .getPromotionProductList(cartItemList.stream().map(OmsCartItem::getProductId).collect(Collectors.toList())).stream()
      .collect(Collectors.toMap(PromotionProduct::getId,
        Function.identity()));
    //3.根据商品促销类型计算商品促销优惠价格
    List<CartPromotionItem> cartPromotionItemList = new ArrayList<>();
    for (Map.Entry<Long, List<OmsCartItem>> entry : productCartMap.entrySet()) {
      Long productId = entry.getKey();
      //商品信息
      PromotionProduct promotionProduct = promotionProductMap.get(productId);
      List<OmsCartItem> itemList = entry.getValue();
      Integer promotionType = promotionProduct.getPromotionType();
      if (Objects.equals(promotionType, MagicConstant.PROMOTION_TYPE_SALES)) {
        //单品促销
        for (OmsCartItem item : itemList) {
          CartPromotionItem cartPromotionItem = new CartPromotionItem();
          BeanUtils.copyProperties(item, cartPromotionItem);
          cartPromotionItem.setPromotionMessage("单品促销");
          //商品原价-促销价
          PmsSkuStock skuStock = getOriginalPrice(promotionProduct, item.getProductSkuId());
          BigDecimal originalPrice = skuStock.getPrice();
          cartPromotionItem.setReduceAmount(originalPrice.subtract(skuStock.getPromotionPrice()));
          cartPromotionItem.setRealStock(skuStock.getStock() - skuStock.getLockStock());
          cartPromotionItem.setIntegration(promotionProduct.getGiftPoint());
          cartPromotionItem.setGrowth(promotionProduct.getGiftGrowth());
          cartPromotionItemList.add(cartPromotionItem);
        }
      } else if (Objects.equals(promotionType, MagicConstant.PROMOTION_TYPE_LADDER)) {
        //使用阶梯价格
        int count = getCartItemCount(itemList);
        PmsProductLadder ladder = getProductLadder(count, promotionProduct.getProductLadderList());
        if (ladder != null) {
          for (OmsCartItem item : itemList) {
            CartPromotionItem cartPromotionItem = new CartPromotionItem();
            BeanUtils.copyProperties(item, cartPromotionItem);
            String message = getLadderPromotionMessage(ladder);
            cartPromotionItem.setPromotionMessage(message);
            //商品原价-折扣金额*商品原价
            PmsSkuStock skuStock = getOriginalPrice(promotionProduct, item.getProductSkuId());
            BigDecimal originalPrice = skuStock.getPrice();
            BigDecimal reduceAmount = originalPrice.subtract(ladder.getDiscount().multiply(originalPrice));
            cartPromotionItem.setReduceAmount(reduceAmount);
            cartPromotionItem.setRealStock(skuStock.getStock() - skuStock.getLockStock());
            cartPromotionItem.setIntegration(promotionProduct.getGiftPoint());
            cartPromotionItem.setGrowth(promotionProduct.getGiftGrowth());
            cartPromotionItemList.add(cartPromotionItem);
          }
        } else {
          handleNoReduce(cartPromotionItemList, itemList, promotionProduct);
        }
      } else if (Objects.equals(promotionType, MagicConstant.PROMOTION_TYPE_FULL_REDUCTION)) {
        //满减
        BigDecimal totalAmount = getCartItemAmount(itemList, promotionProductMap);
        PmsProductFullReduction fullReduction = getProductFullReduction(totalAmount, promotionProduct.getProductFullReductionList());
        if (fullReduction != null) {
          for (OmsCartItem item : itemList) {
            CartPromotionItem cartPromotionItem = new CartPromotionItem();
            BeanUtils.copyProperties(item, cartPromotionItem);
            String message = getFullReductionPromotionMessage(fullReduction);
            cartPromotionItem.setPromotionMessage(message);
            //(商品原价/总价)*满减金额
            PmsSkuStock skuStock = getOriginalPrice(promotionProduct, item.getProductSkuId());
            BigDecimal originalPrice = skuStock.getPrice();
            BigDecimal reduceAmount = originalPrice.divide(totalAmount, RoundingMode.HALF_EVEN).multiply(fullReduction.getReducePrice());
            cartPromotionItem.setReduceAmount(reduceAmount);
            cartPromotionItem.setRealStock(skuStock.getStock() - skuStock.getLockStock());
            cartPromotionItem.setIntegration(promotionProduct.getGiftPoint());
            cartPromotionItem.setGrowth(promotionProduct.getGiftGrowth());
            cartPromotionItemList.add(cartPromotionItem);
          }
        } else {
          handleNoReduce(cartPromotionItemList, itemList, promotionProduct);
        }
      } else {
        //无优惠
        handleNoReduce(cartPromotionItemList, itemList, promotionProduct);
      }
    }
    return cartPromotionItemList;
  }

  /**
   * 获取满减促销消息
   */
  private String getFullReductionPromotionMessage(PmsProductFullReduction fullReduction) {
    StringBuilder sb = new StringBuilder();
    sb.append("满减优惠：");
    sb.append("满");
    sb.append(fullReduction.getFullPrice());
    sb.append("元，");
    sb.append("减");
    sb.append(fullReduction.getReducePrice());
    sb.append("元");
    return sb.toString();
  }

  /**
   * 对没满足优惠条件的商品进行处理
   */
  private void handleNoReduce(List<CartPromotionItem> cartPromotionItemList, List<OmsCartItem> itemList,
    PromotionProduct promotionProduct) {
    for (OmsCartItem item : itemList) {
      CartPromotionItem cartPromotionItem = new CartPromotionItem();
      BeanUtils.copyProperties(item, cartPromotionItem);
      cartPromotionItem.setPromotionMessage("无优惠");
      cartPromotionItem.setReduceAmount(new BigDecimal(0));
      PmsSkuStock skuStock = getOriginalPrice(promotionProduct, item.getProductSkuId());
      cartPromotionItem.setRealStock(skuStock.getStock() - skuStock.getLockStock());
      cartPromotionItem.setIntegration(promotionProduct.getGiftPoint());
      cartPromotionItem.setGrowth(promotionProduct.getGiftGrowth());
      cartPromotionItemList.add(cartPromotionItem);
    }
  }

  private PmsProductFullReduction getProductFullReduction(BigDecimal totalAmount, List<PmsProductFullReduction> fullReductionList) {
    //按条件从高到低排序
    fullReductionList.sort((o1, o2) -> o2.getFullPrice().subtract(o1.getFullPrice()).intValue());
    for (PmsProductFullReduction fullReduction : fullReductionList) {
      if (totalAmount.subtract(fullReduction.getFullPrice()).intValue() >= 0) {
        return fullReduction;
      }
    }
    return null;
  }

  /**
   * 获取打折优惠的促销信息
   */
  private String getLadderPromotionMessage(PmsProductLadder ladder) {
    StringBuilder sb = new StringBuilder();
    sb.append("打折优惠：");
    sb.append("满");
    sb.append(ladder.getCount());
    sb.append("件，");
    sb.append("打");
    sb.append(ladder.getDiscount().multiply(new BigDecimal(10)));
    sb.append("折");
    return sb.toString();
  }

  /**
   * 根据购买商品数量获取满足条件的打折优惠策略
   */
  private PmsProductLadder getProductLadder(int count, List<PmsProductLadder> productLadderList) {
    //按数量从大到小排序
    productLadderList.sort((o1, o2) -> o2.getCount() - o1.getCount());
    return productLadderList.stream().filter(p->count>=p.getCount()).findFirst().orElse(null);
  }

  /**
   * 获取购物车中指定商品的数量
   */
  private int getCartItemCount(List<OmsCartItem> itemList) {
    int count = 0;
    for (OmsCartItem item : itemList) {
      count += item.getQuantity();
    }
    return count;
  }

  /**
   * 获取购物车中指定商品的总价
   */
  private BigDecimal getCartItemAmount(List<OmsCartItem> itemList,  Map<Long, PromotionProduct> promotionProductMap) {
    BigDecimal amount = new BigDecimal(0);
    for (OmsCartItem item : itemList) {
      //计算出商品原价
      PromotionProduct promotionProduct = promotionProductMap.get(item.getProductId());
      PmsSkuStock skuStock = getOriginalPrice(promotionProduct, item.getProductSkuId());
      amount = amount.add(skuStock.getPrice().multiply(new BigDecimal(item.getQuantity())));
    }
    return amount;
  }

  /**
   * 获取商品的原价
   */
  private PmsSkuStock getOriginalPrice(PromotionProduct promotionProduct, Long productSkuId) {
    return promotionProduct.getSkuStockList().stream().filter(s -> s.getId().equals(productSkuId)).findFirst()
      .orElse(null);
  }

  /**
   * 根据商品id获取商品的促销信息
   */
  private PromotionProduct getPromotionProductById(Long productId, List<PromotionProduct> promotionProductList) {
    for (PromotionProduct promotionProduct : promotionProductList) {
      if (productId.equals(promotionProduct.getId())) {
        return promotionProduct;
      }
    }
    return null;
  }

  /**********************新增的实现类*****************************/
  @Override
  public Map<String, List<OmsCartItem>> queryCartMap(Long memberId) {
    List<OmsCartItem> omsCartItems = list(memberId, null);
    Map<String, List<OmsCartItem>> cartItemMap = null;
    if (!CollectionUtils.isEmpty(omsCartItems)) {
      cartItemMap = new HashMap<>();
      for (OmsCartItem omsCartItem : omsCartItems) {
        String brandName = omsCartItem.getProductBrand();
        List<OmsCartItem> omsCartItemList = cartItemMap.get(brandName);
        if (omsCartItemList == null) {
          omsCartItemList = new ArrayList<>();
          omsCartItemList.add(omsCartItem);
          cartItemMap.put(brandName, omsCartItemList);
        } else {
          omsCartItemList.add(omsCartItem);
        }
      }
    }
    return cartItemMap;
  }

  @Override
  public Map<String, List<CartPromotionItem>> mapCartPromotionItem(Long memberId) {
    List<CartPromotionItem> cartPromotionItems = listPromotion(memberId, null);
    Map<String, List<CartPromotionItem>> cartPromotionItemMap = null;
    if (!CollectionUtils.isEmpty(cartPromotionItems)) {
      cartPromotionItemMap = new HashMap<>();
      for (CartPromotionItem cartPromotionItem : cartPromotionItems) {
        String brandName = cartPromotionItem.getProductBrand();
        List<CartPromotionItem> cartPromotionItemList = cartPromotionItemMap.get(brandName);
        if (cartPromotionItemList == null) {
          cartPromotionItemList = new ArrayList<>();
          cartPromotionItemList.add(cartPromotionItem);
          cartPromotionItemMap.put(brandName, cartPromotionItemList);
        }
        cartPromotionItemList.add(cartPromotionItem);
      }
    }
    return cartPromotionItemMap;
  }

  @Override
  public int updateAttr4Product(Long cartItemId, Map<String, Object> attrMap) {
    OmsCartItem oldOmsCartItem = cartItemMapper.selectById(cartItemId);
    OmsCartItem cartItem = oldOmsCartItem;
    if (oldOmsCartItem != null) {
      oldOmsCartItem.setId(oldOmsCartItem.getId());
      oldOmsCartItem.setModifyDate(new Date());
      oldOmsCartItem.setDeleteStatus(MagicConstant.DELETE_YET);
      cartItemMapper.updateById(oldOmsCartItem);
      if (attrMap != null) {
        cartItem.setId(null);
        String sp1 = (String) attrMap.get("sp1");
        String sp2 = (String) attrMap.get("sp2");
        cartItem.setSp1(sp1);
        cartItem.setSp1(sp2);
        add(cartItem);
        return 1;
      }

    }
    return 0;
  }
}
