package com.zscat.mallplus.manage.service.pms.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.pms.IPmsProductUserMatchLibraryService;
import com.zscat.mallplus.manage.utils.DateUtils;
import com.zscat.mallplus.mbg.pms.entity.PmsProductUserMatchLibrary;
import com.zscat.mallplus.mbg.pms.mapper.PmsProductUserMatchLibraryMapper;
import com.zscat.mallplus.mbg.ums.entity.UmsMemberStatisticsInfo;
import com.zscat.mallplus.mbg.ums.mapper.UmsMemberStatisticsInfoMapper;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 用户搭配服务实现类
 * @Date: 2019/10/19
 * @Description
 */
@Service
public class PmsProductUserMatchLibraryServiceImpl extends ServiceImpl<PmsProductUserMatchLibraryMapper,PmsProductUserMatchLibrary> implements IPmsProductUserMatchLibraryService {

    @Autowired
    private UmsMemberStatisticsInfoMapper umsMemberStatisticsInfoMapper;

    @Override
    @Transactional
    public boolean saveProductUserMatch(String matchIdParam, String recommType) {
        String[] matchIdArray = matchIdParam.split(",");
        List<String> matchIds = Arrays.asList(matchIdArray);
        List<PmsProductUserMatchLibrary> pmsProductUserMatchLibraries = new ArrayList<>();
        for(String matchId : matchIds){
            PmsProductUserMatchLibrary pmsProductUserMatchLibrary = this.getById(Long.valueOf(matchId));
            pmsProductUserMatchLibrary.setUpdateTime(new Date());
            pmsProductUserMatchLibrary.setRecommendType(recommType);
            pmsProductUserMatchLibraries.add(pmsProductUserMatchLibrary);
            if(MagicConstant.RECOMMEND_TYPE_YES.equals(recommType)){
                UmsMemberStatisticsInfo umsMemberStatisticsInfo = umsMemberStatisticsInfoMapper.selectOne(new QueryWrapper<UmsMemberStatisticsInfo>().
                        eq("member_id",pmsProductUserMatchLibrary.getUserId()));
                if(umsMemberStatisticsInfo != null){
                    umsMemberStatisticsInfo.setRecomendDate(DateUtils.format(new Date(),"yyyy-MM-dd"));
                    umsMemberStatisticsInfo.setRecomendTime(DateUtils.format(new Date(),"HH:mm:ss"));
                    umsMemberStatisticsInfoMapper.updateById(umsMemberStatisticsInfo);
                }else{
                    umsMemberStatisticsInfo.setMemberId(pmsProductUserMatchLibrary.getUserId());
                    umsMemberStatisticsInfo.setRecomendDate(DateUtils.format(new Date(),"yyyy-MM-dd"));
                    umsMemberStatisticsInfo.setRecomendTime(DateUtils.format(new Date(),"HH:mm:ss"));
                    umsMemberStatisticsInfo.setCreateDate(DateUtils.format(new Date(),"yyyy-MM-dd"));
                    umsMemberStatisticsInfo.setCreateTime(DateUtils.format(new Date(),"HH:mm:ss"));
                    umsMemberStatisticsInfoMapper.insert(umsMemberStatisticsInfo);
                }
            }
        }
        if(!CollectionUtils.isEmpty(pmsProductUserMatchLibraries)){
            return this.saveBatch(pmsProductUserMatchLibraries);
        }
        return false;
    }
}
