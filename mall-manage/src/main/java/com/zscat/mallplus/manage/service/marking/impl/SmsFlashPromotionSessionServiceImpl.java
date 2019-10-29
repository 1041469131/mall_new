package com.zscat.mallplus.manage.service.marking.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.marking.ISmsFlashPromotionProductRelationService;
import com.zscat.mallplus.manage.service.marking.ISmsFlashPromotionSessionService;
import com.zscat.mallplus.mbg.marking.entity.SmsFlashPromotionSession;
import com.zscat.mallplus.mbg.marking.mapper.SmsFlashPromotionSessionMapper;
import com.zscat.mallplus.mbg.marking.vo.SmsFlashPromotionSessionDetail;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 限时购场次表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Service
public class SmsFlashPromotionSessionServiceImpl extends ServiceImpl<SmsFlashPromotionSessionMapper, SmsFlashPromotionSession> implements ISmsFlashPromotionSessionService {

    @Resource
    private SmsFlashPromotionSessionMapper promotionSessionMapper;
    @Resource
    private ISmsFlashPromotionProductRelationService relationService;
    @Override
    public List<SmsFlashPromotionSessionDetail> selectList(Long flashPromotionId) {
        List<SmsFlashPromotionSessionDetail> result = new ArrayList<>();

        List<SmsFlashPromotionSession> list = promotionSessionMapper.selectList(new QueryWrapper<>(new SmsFlashPromotionSession()).eq("status",1));
        for (SmsFlashPromotionSession promotionSession : list) {
            SmsFlashPromotionSessionDetail detail = new SmsFlashPromotionSessionDetail();
            BeanUtils.copyProperties(promotionSession, detail);
            int count = relationService.getCount(flashPromotionId, promotionSession.getId());
            detail.setProductCount(count);
            result.add(detail);
        }
        return result;
    }

    @Override
    public int updateStatus(Long id, Integer status) {
        SmsFlashPromotionSession promotionSession = new SmsFlashPromotionSession();
        promotionSession.setId(id);
        promotionSession.setStatus(status);
        return promotionSessionMapper.updateById(promotionSession);
    }
}
