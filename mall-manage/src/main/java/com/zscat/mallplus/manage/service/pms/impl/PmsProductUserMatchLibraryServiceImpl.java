package com.zscat.mallplus.manage.service.pms.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.helper.CalcRecommendStatus;
import com.zscat.mallplus.manage.service.pms.IPmsProductUserMatchLibraryService;
import com.zscat.mallplus.manage.utils.DateUtils;
import com.zscat.mallplus.mbg.pms.entity.PmsProductUserMatchLibrary;
import com.zscat.mallplus.mbg.pms.mapper.PmsProductUserMatchLibraryMapper;
import com.zscat.mallplus.mbg.pms.vo.PmsProductQueryParam;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import com.zscat.mallplus.mbg.ums.entity.UmsMemberStatisticsInfo;
import com.zscat.mallplus.mbg.ums.mapper.UmsMemberMapper;
import com.zscat.mallplus.mbg.ums.mapper.UmsMemberStatisticsInfoMapper;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 用户搭配服务实现类
 * @Date: 2019/10/19
 * @Description
 */
@Service
public class PmsProductUserMatchLibraryServiceImpl extends ServiceImpl<PmsProductUserMatchLibraryMapper,PmsProductUserMatchLibrary> implements IPmsProductUserMatchLibraryService {

    @Autowired
    private UmsMemberStatisticsInfoMapper umsMemberStatisticsInfoMapper;

    @Autowired
    private UmsMemberMapper umsMemberMapper;

    @Autowired
    private PmsProductUserMatchLibraryMapper pmsProductUserMatchLibraryMapper;

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
                UmsMember umsMember = umsMemberMapper.selectById(pmsProductUserMatchLibrary.getUserId());
                Integer recommendCount = getRecommendCount(umsMember.getId());//推荐搭配的数量
                String recommendStatus = CalcRecommendStatus.getRecommendStatus(new Date(),umsMember.getDressFreq());
                if(umsMemberStatisticsInfo != null){
                    umsMemberStatisticsInfo.setRecomendDate(new Date());
                    umsMemberStatisticsInfo.setRecommendStatus(recommendStatus);
                    umsMemberStatisticsInfo.setRecommendCount(recommendCount);
                    umsMemberStatisticsInfoMapper.updateById(umsMemberStatisticsInfo);
                }else{
                    umsMemberStatisticsInfo = new UmsMemberStatisticsInfo();
                    umsMemberStatisticsInfo.setMemberId(pmsProductUserMatchLibrary.getUserId());
                    umsMemberStatisticsInfo.setRecommendStatus(recommendStatus);
                    umsMemberStatisticsInfo.setRecommendCount(recommendCount);
                    umsMemberStatisticsInfo.setRecomendDate(new Date());
                    umsMemberStatisticsInfo.setCreateDate(new Date());
                    umsMemberStatisticsInfoMapper.insert(umsMemberStatisticsInfo);
                }
            }
        }
        if(!CollectionUtils.isEmpty(pmsProductUserMatchLibraries)){
            return this.saveOrUpdateBatch(pmsProductUserMatchLibraries);
        }
        return false;
    }

    @Override
    public Page<PmsProductUserMatchLibrary> listByPage(PmsProductQueryParam queryParam) {
        return pmsProductUserMatchLibraryMapper.listByPage(new Page<>(queryParam.getPageNum(),queryParam.getPageSize()),queryParam);
    }

    private Integer getRecommendCount(Long memberId) {
        //根据用户的id查询该用户推荐了多少sku的信息，然后把sku的量给返回
        List<PmsProductUserMatchLibrary> pmsProductUserMatchLibraries = pmsProductUserMatchLibraryMapper.selectList(new QueryWrapper<PmsProductUserMatchLibrary>().
                eq("user_id", memberId).eq("recommend_type", "1").orderByDesc("update_time"));
        if (!CollectionUtils.isEmpty(pmsProductUserMatchLibraries)) {
            Map<String, Object> skuMap = new HashMap<>();
            for (PmsProductUserMatchLibrary pmsProductUserMatchLibrary : pmsProductUserMatchLibraries) {
                String[] skus = pmsProductUserMatchLibrary.getSkuIds().split(",");
                for (String sku : skus) {
                    skuMap.put(sku, sku);
                }
            }
            return skuMap.keySet().size();
        } else {
            return 0;
        }
    }
}
