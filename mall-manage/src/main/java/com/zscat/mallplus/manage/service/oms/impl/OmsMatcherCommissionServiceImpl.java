package com.zscat.mallplus.manage.service.oms.impl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.oms.IOmsMatcherCommissionService;
import com.zscat.mallplus.manage.service.sys.ISysMatcherStatisticsService;
import com.zscat.mallplus.mbg.oms.entity.OmsMatcherCommission;
import com.zscat.mallplus.mbg.oms.mapper.OmsMatcherCommissionMapper;
import com.zscat.mallplus.mbg.oms.vo.OmsMatcherCommissionVo;
import com.zscat.mallplus.mbg.sys.entity.SysUser;
import com.zscat.mallplus.mbg.sys.mapper.SysUserMapper;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.util.Date;
import java.util.List;

@Service
public class OmsMatcherCommissionServiceImpl extends ServiceImpl<OmsMatcherCommissionMapper,OmsMatcherCommission> implements IOmsMatcherCommissionService{

    @Autowired
    private OmsMatcherCommissionMapper omsMatcherCommissionMapper;

    @Autowired
    private ISysMatcherStatisticsService sysMatcherStatisticsService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public Page<OmsMatcherCommissionVo> pageOmsMathcerCommissions(OmsMatcherCommissionVo omsMatcherCommissionVo) {
        Page<OmsMatcherCommissionVo> omsMatcherCommissionVoPage = new Page<>(omsMatcherCommissionVo.getPageNum(),omsMatcherCommissionVo.getPageSize());
        return omsMatcherCommissionMapper.pageOmsMathcerCommissions(omsMatcherCommissionVoPage,omsMatcherCommissionVo);
    }

    @Override
    public List<OmsMatcherCommissionVo> listOmsMathcerCommissions(OmsMatcherCommissionVo omsMatcherCommissionVo) {
        String settleStatus = omsMatcherCommissionVo.getSettleStatuses();
        if(!StringUtils.isEmpty(settleStatus)){
            String[] statusList = settleStatus.split(",");
            omsMatcherCommissionVo.setStatusList(statusList);
        }
        return omsMatcherCommissionMapper.listOmsMathcerCommissions(omsMatcherCommissionVo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSettleStatus(List<OmsMatcherCommission> omsMatcherCommissions) {
        //根据老的订单的状态进行结算状态的变更
        if(!CollectionUtils.isEmpty(omsMatcherCommissions)){
            List<OmsMatcherCommission> omsMatcherCommissionList=new ArrayList<>();
            for(OmsMatcherCommission omsMatcherCommission : omsMatcherCommissions){
                omsMatcherCommission.setUpdateTime(System.currentTimeMillis());
                omsMatcherCommission.setUpdateDate(new Date());
                if(MagicConstant.SETTLE_STAUTS_WAITE.equals(omsMatcherCommission.getStatus())){
                    omsMatcherCommission.setStatus(MagicConstant.SETTLE_STAUTS_SETTLED);
                    omsMatcherCommissionList.add(omsMatcherCommission);
                }else if(MagicConstant.SETTLE_STAUTS_WAITE_PARTREFUND.equals(omsMatcherCommission.getStatus())){
                    omsMatcherCommission.setStatus(MagicConstant.SETTLE_STAUTS_SETTLED_PARTREFUND);
                    omsMatcherCommissionList.add(omsMatcherCommission);
                }
            }
            this.updateBatchById(omsMatcherCommissions);
            Set<Long> collect = omsMatcherCommissionList.stream().map(OmsMatcherCommission::getMatcherUserId).collect(Collectors.toSet());
            List<SysUser> sysUsers = sysUserMapper.selectBatchIds(collect);
            sysUsers.forEach(sysUser -> sysMatcherStatisticsService.accountMatcherStatics(sysUser));
        }
    }
}
