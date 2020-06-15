package com.zscat.mallplus.manage.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.oms.IOmsMatcherCommissionItemService;
import com.zscat.mallplus.manage.service.sys.ISysMatcherStatisticsService;
import com.zscat.mallplus.mbg.oms.entity.OmsMatcherCommission;
import com.zscat.mallplus.mbg.oms.entity.OmsMatcherCommissionItem;
import com.zscat.mallplus.mbg.oms.entity.OmsOrder;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderItem;
import com.zscat.mallplus.mbg.oms.entity.OmsOrderReturnSale;
import com.zscat.mallplus.mbg.oms.mapper.OmsMatcherCommissionMapper;
import com.zscat.mallplus.mbg.oms.mapper.OmsOrderItemMapper;
import com.zscat.mallplus.mbg.oms.mapper.OmsOrderReturnSaleMapper;
import com.zscat.mallplus.mbg.pms.entity.PmsProductCommission;
import com.zscat.mallplus.mbg.pms.mapper.PmsProductCommissionMapper;
import com.zscat.mallplus.mbg.sys.entity.SysMatcherAccount;
import com.zscat.mallplus.mbg.sys.entity.SysMatcherStatistics;
import com.zscat.mallplus.mbg.sys.entity.SysUser;
import com.zscat.mallplus.mbg.sys.mapper.SysMatcherAccountMapper;
import com.zscat.mallplus.mbg.sys.mapper.SysMatcherStatisticsMapper;
import com.zscat.mallplus.mbg.sys.mapper.SysUserMapper;
import com.zscat.mallplus.mbg.sys.vo.SysMatcherStatisticsVo;
import com.zscat.mallplus.mbg.ums.entity.UmsApplyMatcher;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import com.zscat.mallplus.mbg.ums.mapper.UmsApplyMatcherMapper;
import com.zscat.mallplus.mbg.ums.mapper.UmsMemberMapper;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
public class SysMatcherStatisticsServiceImpl extends ServiceImpl<SysMatcherStatisticsMapper,SysMatcherStatistics> implements ISysMatcherStatisticsService{

    @Autowired
    private SysMatcherStatisticsMapper sysMatcherStatisticsMapper;

    @Autowired
    private UmsMemberMapper umsMemberMapper;

    @Autowired
    private OmsOrderReturnSaleMapper omsOrderReturnSaleMapper;

    @Autowired
    private UmsApplyMatcherMapper umsApplyMatcherMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private OmsMatcherCommissionMapper omsMatcherCommissionMapper;

    @Autowired
    private OmsOrderItemMapper omsOrderItemMapper;

    @Autowired
    private PmsProductCommissionMapper pmsProductCommissionMapper;

    @Autowired
    private SysMatcherAccountMapper sysMatcherAccountMapper;

    @Autowired
    private IOmsMatcherCommissionItemService omsMatcherCommissionItemService;

    @Override
    public Page<SysMatcherStatisticsVo> pageMatherStatistics(SysMatcherStatisticsVo sysMatcherStatisticsVo) {
        Page<SysMatcherStatisticsVo> sysMatcherStatisticsVoPage = sysMatcherStatisticsMapper.pageMatherStatistics(new Page<>(sysMatcherStatisticsVo.getPageNum(),sysMatcherStatisticsVo.getPageSize()),sysMatcherStatisticsVo);
        if(!CollectionUtils.isEmpty(sysMatcherStatisticsVoPage.getRecords())){
            List<SysMatcherStatisticsVo> sysMatcherStatisticsVos = sysMatcherStatisticsVoPage.getRecords();
            for(SysMatcherStatisticsVo sysMatcherStatisticsVo1 : sysMatcherStatisticsVos){
                Long matcherUserId = sysMatcherStatisticsVo1.getId();
                List<SysMatcherAccount> sysMatcherAccounts = sysMatcherAccountMapper.selectList(new QueryWrapper<SysMatcherAccount>().eq("matcher_id",matcherUserId).eq("status","1"));
                sysMatcherStatisticsVo1.setSysMatcherAccounts(sysMatcherAccounts);
            }
            sysMatcherStatisticsVoPage.setRecords(sysMatcherStatisticsVos);
        }
        return sysMatcherStatisticsVoPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refreshMatcherStatisticsByOrder(OmsOrder omsOrder) {
        Long memberId = omsOrder.getMemberId();
        UmsMember umsMember = umsMemberMapper.selectById(memberId);
        if(umsMember != null){
            Long matcherId = umsMember.getMatchUserId();
            SysUser sysUser = sysUserMapper.selectById(matcherId);
            //商品分佣的搭配师统计
            saveOrUpdateOmsMatcherCommission(omsOrder,sysUser,"0");
            accountMatcherStatics(sysUser);
            //邀请奖励搭配师统计
            UmsApplyMatcher umsApplyMatcher = umsApplyMatcherMapper.selectOne(new QueryWrapper<UmsApplyMatcher>().eq("phone",sysUser.getPhone()).eq("audit_status",MagicConstant.AUDIT_STATUS_PASSED));
            if(umsApplyMatcher != null){
                if(!StringUtils.isEmpty(umsApplyMatcher.getInvitePhone())){
                    SysUser inviteSysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("phone",umsApplyMatcher.getInvitePhone()));
                    //邀请搭配师的比例
                    if(inviteSysUser!=null) {
                        saveOrUpdateOmsMatcherCommission(omsOrder, inviteSysUser, "1");
                        accountMatcherStatics(inviteSysUser);
                    }
                }
            }
        }

    }

    /**
     * 统计搭配师的统计数据
     * @param matcherUser
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void accountMatcherStatics(SysUser matcherUser) {
        SysMatcherStatistics sysMatcherStatistics = sysMatcherStatisticsMapper.selectOne(new QueryWrapper<SysMatcherStatistics>().eq("matcher_id",matcherUser.getId()));
        if(sysMatcherStatistics == null){
            sysMatcherStatistics = new SysMatcherStatistics();
            SysUser umsMember = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("phone",matcherUser.getPhone()));
            sysMatcherStatistics.setMatcherId(matcherUser.getId());
            sysMatcherStatistics.setMemberId(umsMember.getId());
            sysMatcherStatistics.setCreateDate(new Date());
            sysMatcherStatistics.setCreateTime(System.currentTimeMillis());
        }
        //总销售额
        BigDecimal totalSaleAmount = BigDecimal.ZERO;
        //总收益
        BigDecimal totalProfit = BigDecimal.ZERO;
        //未结算的总收益
        BigDecimal unSettleTotalProfit = BigDecimal.ZERO;
        //商品佣金
        BigDecimal productCommission = BigDecimal.ZERO;

        //计算未结算的订单
        Map<String,Object> paramMap = new HashMap<>(4);
        paramMap.put("matcherUserId",matcherUser.getId());
        List<String> settleStatus = new ArrayList<>();
        settleStatus.add(MagicConstant.SETTLE_STAUTS_WAITE);
        settleStatus.add(MagicConstant.SETTLE_STAUTS_WAITE_PARTREFUND);
        paramMap.put("statusList",settleStatus);
        //分佣
        paramMap.put("profitType","0");
        //未结算
        HashMap<String, Object> unSettledomsResultMap = sysMatcherStatisticsMapper.getAmount(paramMap);
        unSettledomsResultMap=unSettledomsResultMap==null?new HashMap<>():unSettledomsResultMap;
        BigDecimal pmsOrderAmount = unSettledomsResultMap.get("ORDER_AMOUNT") == null? BigDecimal.ZERO:(BigDecimal)unSettledomsResultMap.get("ORDER_AMOUNT");
        BigDecimal pmsProfit =  unSettledomsResultMap.get("PROFIT") == null ? BigDecimal.ZERO:(BigDecimal)unSettledomsResultMap.get("PROFIT");
        totalSaleAmount = totalSaleAmount.add(pmsOrderAmount);
        totalProfit = totalProfit.add(pmsProfit);
        productCommission = productCommission.add(pmsProfit);
        unSettleTotalProfit = unSettleTotalProfit.add(pmsProfit);
        //未结算订单金额
        sysMatcherStatistics.setProductUnsettleAmount(pmsOrderAmount);
        //未结算商品分佣
        sysMatcherStatistics.setProductUnsettleCommission(pmsProfit);


        //邀请状态
        paramMap.put("profitType","1");
        BigDecimal inviteCommission = BigDecimal.ZERO;//邀请佣金
        //未结算
        HashMap<String, Object> unSettledomsInviteResultMap = sysMatcherStatisticsMapper.getAmount(paramMap);
        unSettledomsInviteResultMap=unSettledomsInviteResultMap==null?new HashMap<>():unSettledomsInviteResultMap;
        //
        BigDecimal unSettleInviteOrderAmount = unSettledomsInviteResultMap.get("ORDER_AMOUNT") == null ? BigDecimal.ZERO:(BigDecimal)unSettledomsInviteResultMap.get("ORDER_AMOUNT");
        //未结算邀请奖励
        BigDecimal unSettleInvitePmsProfit = unSettledomsInviteResultMap.get("PROFIT") == null? BigDecimal.ZERO:(BigDecimal)unSettledomsInviteResultMap.get("PROFIT");
        //totalSaleAmount = totalSaleAmount.add(unSettleInviteOrderAmount);
        inviteCommission = inviteCommission.add(unSettleInvitePmsProfit);
        totalProfit = totalProfit.add(unSettleInvitePmsProfit);
        unSettleTotalProfit = unSettleTotalProfit.add(unSettleInvitePmsProfit);
        //未结算订单金额
        sysMatcherStatistics.setInviteUnsettleAmount(unSettleInviteOrderAmount);
        //未结算邀请奖励
        sysMatcherStatistics.setInviteUnsettleCommission(unSettleInvitePmsProfit);

        //计算已结算的订单
        List<String> settledStatus = new ArrayList<>();
        settledStatus.add(MagicConstant.SETTLE_STAUTS_SETTLED);
        settledStatus.add(MagicConstant.SETTLE_STAUTS_SETTLED_PARTREFUND);
        paramMap.put("statusList",settledStatus);
        paramMap.put("profitType","0");
        HashMap<String, Object> settledomsResultMap = sysMatcherStatisticsMapper.getAmount(paramMap);//未结算
        settledomsResultMap=settledomsResultMap==null?new HashMap<>():settledomsResultMap;
        //已结算订单金额
        BigDecimal settledOmsOrderAmount =  settledomsResultMap.get("ORDER_AMOUNT") == null ? BigDecimal.ZERO:(BigDecimal)settledomsResultMap.get("ORDER_AMOUNT");
        //已结算分佣金额
        BigDecimal settledPmsProfit =  settledomsResultMap.get("PROFIT") == null ? BigDecimal.ZERO:(BigDecimal)settledomsResultMap.get("PROFIT");
        totalSaleAmount = totalSaleAmount.add(settledOmsOrderAmount);
        totalProfit = totalProfit.add(settledPmsProfit);
        productCommission = productCommission.add(settledPmsProfit);
        //unSettleTotalProfit = unSettleTotalProfit.add(settledPmsProfit);
        //已结算的订单金额（商品分佣部分）
        sysMatcherStatistics.setProductSettleAmount(settledOmsOrderAmount);
        //已结算的商品分佣
        sysMatcherStatistics.setProductSettleCommission(settledPmsProfit);

        //邀请状态 以结算
        paramMap.put("profitType","1");
        HashMap<String, Object> settledomsInviteResultMap = sysMatcherStatisticsMapper.getAmount(paramMap);//未结算
        settledomsInviteResultMap=settledomsInviteResultMap==null?new HashMap<>():settledomsInviteResultMap;
        BigDecimal settleInviteOrderAmount =  settledomsInviteResultMap.get("ORDER_AMOUNT") == null ? BigDecimal.ZERO:(BigDecimal)settledomsInviteResultMap.get("ORDER_AMOUNT");
        BigDecimal settleInvitePmsProfit =  settledomsInviteResultMap.get("PROFIT") == null ? BigDecimal.ZERO:(BigDecimal)settledomsInviteResultMap.get("PROFIT");
      //  totalSaleAmount = totalSaleAmount.add(settleInviteOrderAmount);
        totalProfit = totalProfit.add(settleInvitePmsProfit);
        inviteCommission = inviteCommission.add(settleInvitePmsProfit);
        sysMatcherStatistics.setInviteSettleAmount(settleInviteOrderAmount);
        sysMatcherStatistics.setInviteSettleCommission(settleInvitePmsProfit);
        //总得销售额
        sysMatcherStatistics.setTotalSaleAmount(totalSaleAmount);
        //总得收益
        sysMatcherStatistics.setTotalProfit(totalProfit);
        //未结算金额
        sysMatcherStatistics.setUnsettleProfit(unSettleTotalProfit);
        //商品佣金
        sysMatcherStatistics.setProductCommission(productCommission);
        //邀请奖励
        sysMatcherStatistics.setInviteCommission(inviteCommission);
        int fanCount = sysMatcherStatisticsMapper.getFanCount(matcherUser.getId());
        int inviteCount = sysMatcherStatisticsMapper.getInviteCount(matcherUser.getPhone());
        sysMatcherStatistics.setFanCount(fanCount);
        sysMatcherStatistics.setInviteCount(inviteCount);
        sysMatcherStatistics.setUpdateDate(new Date());
        sysMatcherStatistics.setUpdateTime(System.currentTimeMillis());
        if(sysMatcherStatistics.getId() == null){
            sysMatcherStatisticsMapper.insert(sysMatcherStatistics);
        }else {
            sysMatcherStatisticsMapper.updateById(sysMatcherStatistics);
        }
    }

    /**
     * 更新订单搭配比例
     */
    private OmsMatcherCommission saveOrUpdateOmsMatcherCommission(OmsOrder omsOrder, SysUser sysUser, String profitType) {
        OmsMatcherCommission omsMatcherCommission = omsMatcherCommissionMapper.selectOne(new QueryWrapper<OmsMatcherCommission>().eq("order_id",omsOrder.getId()).eq("profit_type",profitType).
                eq("matcher_user_id",sysUser.getId()));
        List<OmsOrderItem> omsOrderItems = omsOrderItemMapper.selectList(new QueryWrapper<OmsOrderItem>().eq("order_id",omsOrder.getId()));
        if(omsMatcherCommission == null ){
            omsMatcherCommission = new OmsMatcherCommission();
            omsMatcherCommission.setProfitType(profitType);
            omsMatcherCommission.setCreateDate(new Date());
            omsMatcherCommission.setCreateTime(System.currentTimeMillis());
            omsMatcherCommission.setOrderId(omsOrder.getId());
            omsMatcherCommission.setMatcherUserId(sysUser.getId());
        }
        List<OmsMatcherCommissionItem> omsMatcherCommissionItems = omsMatcherCommissionItemService
          .list(new QueryWrapper<OmsMatcherCommissionItem>().eq("order_id", omsOrder.getId()).eq("profit_type",profitType));
        if(CollectionUtils.isEmpty(omsMatcherCommissionItems)) {
            omsMatcherCommissionItems=saveOrupdateOmsMatcherCommissionItems(omsOrderItems,omsMatcherCommission,sysUser.getLevel());
        }
        OmsOrderReturnSale omsOrderReturnSale = omsOrderReturnSaleMapper.selectOne(new QueryWrapper<OmsOrderReturnSale>().eq("order_id",omsOrder.getId()).notIn("status",MagicConstant.RETURN_STATUS_FINISHED,MagicConstant.RETURN_STATUS_CANCEL,
                MagicConstant.RETURN_STATUS_REFUSE));
        if(MagicConstant.ORDER_STATUS_YET_DONE.equals(omsOrder.getStatus()) || MagicConstant.ORDER_STATUS_WAIT_SEND.equals(omsOrder.getStatus()) ||
                MagicConstant.ORDER_STATUS_YET_SEND.equals(omsOrder.getStatus())){
            if(omsOrderReturnSale != null){
                omsMatcherCommission.setStatus(MagicConstant.SETTLE_STAUTS_WAITE_PARTREFUND);
            }else{
                omsMatcherCommission.setStatus(MagicConstant.SETTLE_STAUTS_WAITE);
            }
        }
        omsMatcherCommission.setUpdateDate(new Date());
        omsMatcherCommission.setUpdateTime(System.currentTimeMillis());
        BigDecimal profit = omsMatcherCommissionItems.stream().map(OmsMatcherCommissionItem::getProfit).reduce(BigDecimal.ZERO,BigDecimal::add);
        //BigDecimal profit = calcProfit(omsOrderItems,omsMatcherCommission.getProfitType(),sysUser);
        omsMatcherCommission.setProfit(profit);
        if(omsMatcherCommission.getId() == null){
            omsMatcherCommissionMapper.insert(omsMatcherCommission);
        }else{
            omsMatcherCommissionMapper.updateById(omsMatcherCommission);
        }
        return omsMatcherCommission;
    }


    private List<OmsMatcherCommissionItem> saveOrupdateOmsMatcherCommissionItems( List<OmsOrderItem> omsOrderItems,OmsMatcherCommission omsMatcherCommission,String level){
        List<OmsMatcherCommissionItem> collect = omsOrderItems.stream().map(omsOrderItem -> {
            OmsMatcherCommissionItem omsMatcherCommissionItem = new OmsMatcherCommissionItem();
            omsMatcherCommissionItem.setCreateDate(new Date());
            omsMatcherCommissionItem.setMatcherUserId(omsMatcherCommission.getMatcherUserId());
            omsMatcherCommissionItem.setOrderId(omsMatcherCommission.getOrderId());
            omsMatcherCommissionItem.setOrderItemId(omsOrderItem.getId());
            omsMatcherCommissionItem.setProductId(omsOrderItem.getProductId());
            omsMatcherCommissionItem.setProfitType(omsMatcherCommission.getProfitType());
            omsMatcherCommissionItem.setStatus(omsMatcherCommission.getStatus());
            omsMatcherCommissionItem.setUpdateDate(new Date());
            Long productId = omsOrderItem.getProductId();
            Integer productCount = omsOrderItem.getProductQuantity();
            BigDecimal price = omsOrderItem.getProductPrice();
            PmsProductCommission pmsProductCommission = pmsProductCommissionMapper
              .selectOne(new QueryWrapper<PmsProductCommission>().eq("product_id", productId).
                eq("matcher_level", level));
            //商品的分佣查询为空或者不为空但是商品的分佣类型是不分佣
            if (pmsProductCommission == null || "1".equals(pmsProductCommission.getPromoteType())) {
                omsMatcherCommissionItem.setProfit(BigDecimal.ZERO);
                return null;
            }
            BigDecimal proportion;
            //商品佣金
            if ("0".equals(omsMatcherCommissionItem.getProfitType())) {
                //分佣比例
                proportion = pmsProductCommission.getCommissionProportion();
            } else {
                //邀请奖励
                proportion = pmsProductCommission.getInviteProportion();
            }
            BigDecimal profit = proportion.divide(new BigDecimal(100)).multiply(price).multiply(new BigDecimal(productCount));
            omsMatcherCommissionItem.setProfit(profit);
            return omsMatcherCommissionItem;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(collect)) {
            omsMatcherCommissionItemService.saveOrUpdateBatch(collect);
        }
        return collect;
    }
//    /**
//     * 计算订单的收益
//     * @param
//     * @return
//     */
//    private BigDecimal calcProfit(List<OmsOrderItem> omsOrderItems , String profitType, SysUser matcherUser) {
//       BigDecimal profit = BigDecimal.ZERO;//收益
//        //查询订单下面的商品列表
//        if(!CollectionUtils.isEmpty(omsOrderItems)){
//            for (OmsOrderItem omsOrderItem : omsOrderItems){
//                Long productId = omsOrderItem.getProductId();
//                Integer productCount = omsOrderItem.getProductQuantity();
//                BigDecimal price = omsOrderItem.getProductPrice();
//                PmsProductCommission pmsProductCommission = pmsProductCommissionMapper.selectOne(new QueryWrapper<PmsProductCommission>().eq("product_id",productId).
//                        eq("matcher_level",matcherUser.getLevel()));
//                //商品的分佣查询为空或者不为空但是商品的分佣类型是不分佣
//                if(pmsProductCommission == null ||(pmsProductCommission != null && "1".equals(pmsProductCommission.getPromoteType())) ){
//                    return BigDecimal.ZERO;
//                }
//                BigDecimal proportion = BigDecimal.ZERO;
//                if("0".equals(profitType)){//商品佣金
//                    proportion = pmsProductCommission.getCommissionProportion();//分佣比例
//                }else{
//                    proportion = pmsProductCommission.getInviteProportion();//邀请奖励
//                }
//                profit = profit.add(proportion.divide(new BigDecimal(100)).multiply(price).multiply(new BigDecimal(productCount)));
//            }
//        }
//        return profit;
//    }

    @Override
    public void timingMatcherStatics() {

    }

    @Override
    public void updateMatcherStaticsByMatcherId(Long matcherId) {

    }

    @Override
    public SysMatcherStatisticsVo querySysMatcherStatistics(Long matcherUserId) {
        SysMatcherStatisticsVo sysMatcherStatisticsVoPage = sysMatcherStatisticsMapper.querySysMatcherStatistics(matcherUserId);
        if(sysMatcherStatisticsVoPage != null){
                List<SysMatcherAccount> sysMatcherAccounts = sysMatcherAccountMapper.selectList(new QueryWrapper<SysMatcherAccount>().eq("matcher_id",matcherUserId).eq("status","1"));
            sysMatcherStatisticsVoPage.setSysMatcherAccounts(sysMatcherAccounts);
        }
        return sysMatcherStatisticsVoPage;
    }
}
