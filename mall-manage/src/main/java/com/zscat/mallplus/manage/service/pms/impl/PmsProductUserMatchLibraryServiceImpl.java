package com.zscat.mallplus.manage.service.pms.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.config.WxAppletProperties;
import com.zscat.mallplus.manage.helper.CalcRecommendStatus;
import com.zscat.mallplus.manage.service.pms.IPmsProductUserMatchLibraryService;
import com.zscat.mallplus.manage.service.sys.ISysUserService;
import com.zscat.mallplus.manage.service.ums.IUmsMemberService;
import com.zscat.mallplus.manage.service.wechat.WechatApiService;
import com.zscat.mallplus.manage.utils.DateUtils;
import com.zscat.mallplus.manage.utils.applet.TemplateData;
import com.zscat.mallplus.manage.utils.applet.WX_TemplateMsgUtil;
import com.zscat.mallplus.mbg.pms.entity.PmsProductUserMatchLibrary;
import com.zscat.mallplus.mbg.pms.mapper.PmsProductUserMatchLibraryMapper;
import com.zscat.mallplus.mbg.pms.vo.PmsProductQueryParam;
import com.zscat.mallplus.mbg.sys.entity.SysUser;
import com.zscat.mallplus.mbg.ums.entity.UmsMember;
import com.zscat.mallplus.mbg.ums.entity.UmsMemberStatisticsInfo;
import com.zscat.mallplus.mbg.ums.mapper.UmsMemberMapper;
import com.zscat.mallplus.mbg.ums.mapper.UmsMemberStatisticsInfoMapper;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import java.util.stream.Collectors;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Logger logger= LoggerFactory.getLogger(PmsProductUserMatchLibraryServiceImpl.class);
    @Autowired
    private UmsMemberStatisticsInfoMapper umsMemberStatisticsInfoMapper;
    @Autowired
    private UmsMemberMapper umsMemberMapper;
    @Autowired
    private PmsProductUserMatchLibraryMapper pmsProductUserMatchLibraryMapper;
    @Autowired
    private WechatApiService wechatApiService;
    @Autowired
    private WxAppletProperties wxAppletProperties;
    @Autowired
    private IUmsMemberService umsMemberService;
    @Autowired
    private ISysUserService sysUserService;

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

    @Override
    public void sendMatchLibraryMsg(List<PmsProductUserMatchLibrary> pmsProductMatchLibrarys) {
        if(CollectionUtils.isEmpty(pmsProductMatchLibrarys)){
            return;
        }
        Long userId = pmsProductMatchLibrarys.get(0).getUserId();
        UmsMember umsMember = umsMemberService.getById(userId);
        List<String> pmsProductUserIds = pmsProductMatchLibrarys.stream().map(p->p.getId().toString()).collect(Collectors.toList());
        String formId = String.join(",", pmsProductUserIds);
        SysUser sysUser = sysUserService.getById(pmsProductMatchLibrarys.get(0).getMatchUserId());
        push(umsMember,sysUser,null,formId);
    }

    public void push(UmsMember umsMember,  SysUser sysUser, String page, String formId) {
        logger.info("发送模版消息：userId=" + umsMember.getId() + ",formId=" + formId);
        try {
            String accessToken = wechatApiService.getAccessToken();
            String templateId = wxAppletProperties.getServiceCompleteTemplateId();
            Map<String, TemplateData> param = new HashMap<>(4);
            param.put("thing1", new TemplateData("穿搭专属推荐", "#EE0000"));
            param.put("time2", new TemplateData(DateUtils.format(new Date(), "yyyy/MM/dd"), "#EE0000"));
            String memo="您的订阅的1对1穿搭推荐服务，已推送至个搭小程序主页，请及时选购。如有疑问，请及时联系您的专属搭配师"+sysUser.getName();
            param.put("thing4", new TemplateData(memo, "#EE0000"));
            JSONObject jsonObject = JSONObject.fromObject(param);
            //调用发送微信消息给用户的接口    ********这里写自己在微信公众平台拿到的模板ID
            WX_TemplateMsgUtil.sendWechatMsgToUser(umsMember.getWeixinOpenid(), templateId, page,formId, jsonObject, accessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
