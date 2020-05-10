package com.zscat.mallplus.manage.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.sys.ISysMatcherStatisticsService;
import com.zscat.mallplus.mbg.oms.entity.OmsMatcherCommission;
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
    @Transactional
    public void refreshMatcherStatisticsByOrder(OmsOrder omsOrder) {
        Long memberId = omsOrder.getMemberId();
        UmsMember umsMember = umsMemberMapper.selectById(memberId);
        if(umsMember != null){
            Long matcherId = umsMember.getMatchUserId();
            SysUser sysUser = sysUserMapper.selectById(matcherId);
            //商品分佣的搭配师统计
            saveOrupdateOmsMatcherCommission(omsOrder,sysUser,"0");
            accountMatcherStatics(sysUser);
            //邀请奖励搭配师统计
            UmsApplyMatcher umsApplyMatcher = umsApplyMatcherMapper.selectOne(new QueryWrapper<UmsApplyMatcher>().eq("phone",sysUser.getPhone()).eq("audit_status",MagicConstant.AUDIT_STATUS_PASSED));
            if(umsApplyMatcher != null){
                if(!StringUtils.isEmpty(umsApplyMatcher.getInvitePhone())){
                    SysUser inviteSysUser = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("phone",umsApplyMatcher.getInvitePhone()));
                    //邀请搭配师的比例
                    saveOrupdateOmsMatcherCommission(omsOrder,inviteSysUser,"1");
                    accountMatcherStatics(inviteSysUser);
                }
            }
        }

    }

    /**
     * 统计搭配师的统计数据
     * @param matcherUser
     */
    private void accountMatcherStatics(SysUser matcherUser) {
        SysMatcherStatistics sysMatcherStatistics = sysMatcherStatisticsMapper.selectOne(new QueryWrapper<SysMatcherStatistics>().eq("matcher_id",matcherUser.getId()));
        if(sysMatcherStatistics == null){
            sysMatcherStatistics = new SysMatcherStatistics();
            UmsMember umsMember = umsMemberMapper.selectOne(new QueryWrapper<UmsMember>().eq("phone",matcherUser.getPhone()));
            sysMatcherStatistics.setMatcherId(matcherUser.getId());
            sysMatcherStatistics.setMemberId(umsMember.getId());
            sysMatcherStatistics.setCreateDate(new Date());
            sysMatcherStatistics.setCreateTime(new Date().getTime());
        }
        BigDecimal totalSaleAmount = BigDecimal.ZERO;//总销售额
        BigDecimal totalProfit = BigDecimal.ZERO;//总收益
        BigDecimal unSettleTotalProfit = BigDecimal.ZERO;//未结算的总收益
        BigDecimal productCommission = BigDecimal.ZERO;//商品佣金
        BigDecimal inviteCommission = BigDecimal.ZERO;//邀请佣金
        //计算未结算的订单
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("matcherUserId",matcherUser.getId());
        List<String> settleStatus = new ArrayList<>();
        settleStatus.add(MagicConstant.SETTLE_STAUTS_WAITE);
        settleStatus.add(MagicConstant.SETTLE_STAUTS_WAITE_PARTREFUND);
        paramMap.put("statusList",settleStatus);
        paramMap.put("profitType","0");
        HashMap<String, Object> unSettledomsResultMap = sysMatcherStatisticsMapper.getAmount(paramMap);//未结算
        BigDecimal pmsOrderAmount =  unSettledomsResultMap.get("ORDER_AMOUNT") == null ? BigDecimal.ZERO:(BigDecimal)unSettledomsResultMap.get("ORDER_AMOUNT");
        BigDecimal pmsProfit =  unSettledomsResultMap.get("PROFIT") == null ? BigDecimal.ZERO:(BigDecimal)unSettledomsResultMap.get("PROFIT");
        totalSaleAmount = totalSaleAmount.add(pmsOrderAmount);
        totalProfit = totalProfit.add(pmsProfit);
        productCommission = productCommission.add(pmsProfit);
        unSettleTotalProfit = unSettleTotalProfit.add(pmsProfit);
        sysMatcherStatistics.setProductUnsettleAmount(pmsOrderAmount);
        sysMatcherStatistics.setProductUnsettleCommission(pmsProfit);

        paramMap.put("profitType","1");
        HashMap<String, Object> unSettledomsInviteResultMap = sysMatcherStatisticsMapper.getAmount(paramMap);//未结算
        BigDecimal unSettleInviteOrderAmount =  unSettledomsInviteResultMap.get("ORDER_AMOUNT") == null ? BigDecimal.ZERO:(BigDecimal)unSettledomsInviteResultMap.get("ORDER_AMOUNT");
        BigDecimal unSettleInvitePmsProfit =  unSettledomsInviteResultMap.get("PROFIT") == null ? BigDecimal.ZERO:(BigDecimal)unSettledomsInviteResultMap.get("PROFIT");
        totalSaleAmount = totalSaleAmount.add(unSettleInviteOrderAmount);
        inviteCommission = inviteCommission.add(unSettleInvitePmsProfit);
        totalProfit = totalProfit.add(unSettleInvitePmsProfit);
        sysMatcherStatistics.setInviteUnsettleAmount(unSettleInviteOrderAmount);
        sysMatcherStatistics.setInviteUnsettleCommission(unSettleInvitePmsProfit);

        //计算已结算的订单
        List<String> settledStatus = new ArrayList<>();
        settledStatus.add(MagicConstant.SETTLE_STAUTS_SETTLED);
        settledStatus.add(MagicConstant.SETTLE_STAUTS_SETTLED_PARTREFUND);
        paramMap.put("statusList",settledStatus);
        paramMap.put("profitType","0");
        HashMap<String, Object> settledomsResultMap = sysMatcherStatisticsMapper.getAmount(paramMap);//未结算
        BigDecimal settledOmsOrderAmount =  settledomsResultMap.get("ORDER_AMOUNT") == null ? BigDecimal.ZERO:(BigDecimal)settledomsResultMap.get("ORDER_AMOUNT");
        BigDecimal settledPmsProfit =  settledomsResultMap.get("PROFIT") == null ? BigDecimal.ZERO:(BigDecimal)settledomsResultMap.get("PROFIT");
        totalSaleAmount = totalSaleAmount.add(settledOmsOrderAmount);
        totalProfit = totalProfit.add(settledPmsProfit);
        productCommission = productCommission.add(settledPmsProfit);
        unSettleTotalProfit = unSettleTotalProfit.add(settledPmsProfit);
        sysMatcherStatistics.setProductSettleAmount(settledOmsOrderAmount);
        sysMatcherStatistics.setProductSettleCommission(settledPmsProfit);

        paramMap.put("profitType","1");
        HashMap<String, Object> settledomsInviteResultMap = sysMatcherStatisticsMapper.getAmount(paramMap);//未结算
        BigDecimal settleInviteOrderAmount =  settledomsInviteResultMap.get("ORDER_AMOUNT") == null ? BigDecimal.ZERO:(BigDecimal)settledomsInviteResultMap.get("ORDER_AMOUNT");
        BigDecimal settleInvitePmsProfit =  settledomsInviteResultMap.get("PROFIT") == null ? BigDecimal.ZERO:(BigDecimal)settledomsInviteResultMap.get("PROFIT");
        totalSaleAmount = totalSaleAmount.add(settleInviteOrderAmount);
        totalProfit = totalProfit.add(settleInvitePmsProfit);
        inviteCommission = inviteCommission.add(settleInvitePmsProfit);
        sysMatcherStatistics.setInviteSettleAmount(settleInviteOrderAmount);
        sysMatcherStatistics.setInviteSettleCommission(settleInvitePmsProfit);

        sysMatcherStatistics.setTotalSaleAmount(totalSaleAmount);//总得销售额
        sysMatcherStatistics.setTotalProfit(totalProfit);//总得收益
        sysMatcherStatistics.setUnsettleProfit(unSettleTotalProfit);//未结算金额
        sysMatcherStatistics.setProductCommission(productCommission);//商品佣金
        sysMatcherStatistics.setInviteCommission(inviteCommission);//邀请奖励
        int fanCount = sysMatcherStatisticsMapper.getFanCount(matcherUser.getId());
        int inviteCount = sysMatcherStatisticsMapper.getInviteCount(matcherUser.getPhone());
        sysMatcherStatistics.setFanCount(fanCount);
        sysMatcherStatistics.setInviteCount(inviteCount);
        sysMatcherStatistics.setUpdateDate(new Date());
        sysMatcherStatistics.setUpdateTime(new Date().getTime());
        if(sysMatcherStatistics.getId() == null){
            sysMatcherStatisticsMapper.insert(sysMatcherStatistics);
        }else {
            sysMatcherStatisticsMapper.updateById(sysMatcherStatistics);
        }
    }

    /**
     * 更新订单搭配比例
     * @param omsOrder
     * @param sysUser
     */
    private OmsMatcherCommission saveOrupdateOmsMatcherCommission(OmsOrder omsOrder, SysUser sysUser, String profitType) {
        OmsMatcherCommission omsMatcherCommission = omsMatcherCommissionMapper.selectOne(new QueryWrapper<OmsMatcherCommission>().eq("order_id",omsOrder.getId()).eq("profit_type",profitType).
                eq("matcher_user_id",sysUser.getId()));
        if(omsMatcherCommission == null ){
            omsMatcherCommission = new OmsMatcherCommission();
            omsMatcherCommission.setProfitType(profitType);
            omsMatcherCommission.setCreateDate(new Date());
            omsMatcherCommission.setCreateTime(new Date().getTime());
            omsMatcherCommission.setOrderId(omsOrder.getId());
            omsMatcherCommission.setMatcherUserId(sysUser.getId());
        }
        OmsOrderReturnSale omsOrderReturnSale = omsOrderReturnSaleMapper.selectOne(new QueryWrapper<OmsOrderReturnSale>().eq("order_id",omsOrder.getId()).notIn("status",new Object[]{MagicConstant.RETURN_STATUS_FINISHED,MagicConstant.RETURN_STATUS_CANCEL,
                MagicConstant.RETURN_STATUS_REFUSE}));
        if(MagicConstant.ORDER_STATUS_YET_DONE.equals(omsOrder.getStatus()) || MagicConstant.ORDER_STATUS_WAIT_SEND.equals(omsOrder.getStatus()) ||
                MagicConstant.ORDER_STATUS_YET_SEND.equals(omsOrder.getStatus())){
            if(omsOrderReturnSale != null){
                omsMatcherCommission.setStatus(MagicConstant.SETTLE_STAUTS_WAITE_PARTREFUND);
            }else{
                omsMatcherCommission.setStatus(MagicConstant.SETTLE_STAUTS_WAITE);
            }
        }
        omsMatcherCommission.setUpdateDate(new Date());
        omsMatcherCommission.setUpdateTime(new Date().getTime());
        BigDecimal profit = calcProfit(omsOrder,omsMatcherCommission.getProfitType(),sysUser);
        omsMatcherCommission.setProfit(profit);
        if(omsMatcherCommission.getId() == null){
            omsMatcherCommissionMapper.insert(omsMatcherCommission);
        }else{
            omsMatcherCommissionMapper.updateById(omsMatcherCommission);
        }

        return omsMatcherCommission;
    }

    /**
     * 计算订单的收益
     * @param omsOrder
     * @return
     */
    private BigDecimal calcProfit(OmsOrder omsOrder, String profitType, SysUser matcherUser) {
        List<OmsOrderItem> omsOrderItems = omsOrderItemMapper.selectList(new QueryWrapper<OmsOrderItem>().eq("order_id",omsOrder.getId()));
        BigDecimal profit = BigDecimal.ZERO;//收益
        //查询订单下面的商品列表
        if(!CollectionUtils.isEmpty(omsOrderItems)){
            for (OmsOrderItem omsOrderItem : omsOrderItems){
                Long productId = omsOrderItem.getProductId();
                Integer productCount = omsOrderItem.getProductQuantity();
                BigDecimal price = omsOrderItem.getProductPrice();
                PmsProductCommission pmsProductCommission = pmsProductCommissionMapper.selectOne(new QueryWrapper<PmsProductCommission>().eq("product_id",productId).
                        eq("matcher_level",matcherUser.getLevel()));
                //商品的分佣查询为空或者不为空但是商品的分佣类型是不分佣
                if(pmsProductCommission == null ||(pmsProductCommission != null && "1".equals(pmsProductCommission.getCommissionType())) ){
                    return BigDecimal.ZERO;
                }
                BigDecimal proportion = BigDecimal.ZERO;
                if("0".equals(profitType)){//商品佣金
                    proportion = pmsProductCommission.getCommissionProportion();//分佣比例
                }else{
                    proportion = pmsProductCommission.getInviteProportion();//邀请奖励
                }
                profit = profit.add(proportion.divide(new BigDecimal(100)).multiply(price).multiply(new BigDecimal(productCount)));
            }
        }
        return profit;
    }

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
