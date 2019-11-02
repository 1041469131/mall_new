package com.zscat.mallplus.manage.assemble;

import com.zscat.mallplus.manage.service.pms.IPmsProductService;
import com.zscat.mallplus.manage.service.pms.IPmsSkuStockService;
import com.zscat.mallplus.manage.utils.SpringContextHolder;
import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import com.zscat.mallplus.mbg.pms.entity.PmsProductMatchLibrary;
import com.zscat.mallplus.mbg.pms.entity.PmsProductUserMatchLibrary;
import com.zscat.mallplus.mbg.pms.entity.PmsSkuStock;
import com.zscat.mallplus.mbg.pms.vo.PmsProductMatchLibraryVo;
import com.zscat.mallplus.mbg.pms.vo.PmsProductResult;
import com.zscat.mallplus.mbg.pms.vo.PmsSkuStockVo;
import com.zscat.mallplus.mbg.utils.IdGeneratorUtil;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 匹配组合类
 * @Date: 2019/10/30
 * @Description
 */
public class MatchLibraryAssemble {

    static IPmsSkuStockService iPmsSkuStockService;

    static IPmsProductService iPmsProductService;

    static {
        iPmsSkuStockService = SpringContextHolder.getBean(IPmsSkuStockService.class);
        iPmsProductService = SpringContextHolder.getBean(IPmsProductService.class);
    }

    /**
     * 组装用户搭配
     * @param pmsProductUserMatchLibraries
     * @return
     */
    public static List<PmsProductMatchLibraryVo> assembleUserMatchLibrary(List<PmsProductUserMatchLibrary> pmsProductUserMatchLibraries){
        List<PmsProductMatchLibraryVo> pmsProductMatchLibraryVos = null;
        if(!CollectionUtils.isEmpty(pmsProductUserMatchLibraries)){
            pmsProductMatchLibraryVos = new ArrayList<>();
            for(PmsProductUserMatchLibrary pmsProductUserMatchLibrary : pmsProductUserMatchLibraries){
                    PmsProductMatchLibraryVo pmsProductMatchLibraryVo = assembleSingleUserMatchLibrary(pmsProductUserMatchLibrary);
                    pmsProductMatchLibraryVos.add(pmsProductMatchLibraryVo);
                }
        }
        return pmsProductMatchLibraryVos;
    }

    /**
     * 组装单个用户匹配
     * @param pmsProductUserMatchLibraries
     * @return
     */
    public static PmsProductMatchLibraryVo assembleSingleUserMatchLibrary(PmsProductUserMatchLibrary pmsProductUserMatchLibrary){
        PmsProductMatchLibraryVo pmsProductMatchLibraryVo = new PmsProductMatchLibraryVo();
        String skuIds = pmsProductUserMatchLibrary.getSkuIds();
        if(!StringUtils.isEmpty(skuIds)){
            String[] skuIdArrays = skuIds.split(",");
            List<Long> skuIdList = new ArrayList<>();;
            for (String skuId:skuIdArrays){
                skuIdList.add(Long.valueOf(skuId));
            }
            List<PmsSkuStock> pmsSkuStocks = (List<PmsSkuStock>)iPmsSkuStockService.listByIds(skuIdList);
            List<PmsSkuStockVo> pmsSkuStockVos = null;
            if(!CollectionUtils.isEmpty(pmsSkuStocks)){
                pmsSkuStockVos = new ArrayList<>();
                for(PmsSkuStock pmsSkuStock:pmsSkuStocks){
                    PmsSkuStockVo pmsSkuStockVo = new PmsSkuStockVo();
                    BeanUtils.copyProperties(pmsSkuStock, pmsSkuStockVo);
                    Long productId = pmsSkuStock.getProductId();
                    PmsProductResult pmsProductResult = iPmsProductService.getUpdateInfo(productId);
                    pmsSkuStockVo.setPmsProductResult(pmsProductResult);
                    pmsSkuStockVos.add(pmsSkuStockVo);
                }
            }
            pmsProductMatchLibraryVo.setPmsSkuStockVos(pmsSkuStockVos);
            pmsProductMatchLibraryVo.setPmsProductUserMatchLibrary(pmsProductUserMatchLibrary);
        }
        return pmsProductMatchLibraryVo;
    }

    /**
     * 组装搭配库信息
     */
    public static PmsProductMatchLibrary assembleMatchLibrary(PmsProductUserMatchLibrary pmsProductUserMatchLibrary) {
        PmsProductMatchLibrary pmsProductMatchLibrary = new PmsProductMatchLibrary();
        pmsProductMatchLibrary.setCreateTime(new Date());
        pmsProductMatchLibrary.setUpdateTime(new Date());
        pmsProductMatchLibrary.setMatchType(pmsProductUserMatchLibrary.getMatchType());
        pmsProductMatchLibrary.setCollectStatus(MagicConstant.COLLECT_STATUS_YES);
        pmsProductMatchLibrary.setMatchOwer(MagicConstant.MATCH_OWER_PERSON);
        pmsProductMatchLibrary.setId(IdGeneratorUtil.getIdGeneratorUtil().nextId());
        pmsProductMatchLibrary.setOperateId(pmsProductUserMatchLibrary.getMatchUserId());
        String skuIds = pmsProductUserMatchLibrary.getSkuIds();
        StringBuffer sb = new StringBuffer();//商品id组合
        String[] skuIdArrays = skuIds.split(",");
        List<Long> skuIdList = new ArrayList<>();;
        for (String skuId:skuIdArrays){
            skuIdList.add(Long.valueOf(skuId));
        }
        List<PmsSkuStock> pmsSkuStocks = (List<PmsSkuStock>)iPmsSkuStockService.listByIds(skuIdList);
        if(org.springframework.util.CollectionUtils.isEmpty(pmsSkuStocks)){
            for(PmsSkuStock pmsProduct:pmsSkuStocks){
                sb.append(pmsProduct.getProductId()).append(",");
            }
        }
        if(!org.springframework.util.StringUtils.isEmpty(sb.toString())){
            sb.deleteCharAt(sb.length() - 1);
        }
        pmsProductMatchLibrary.setProductIds(sb.toString());
        pmsProductMatchLibrary.setTitle(pmsProductUserMatchLibrary.getTitle());
        pmsProductMatchLibrary.setTitleDescr(pmsProductUserMatchLibrary.getTitleDescr());
        pmsProductMatchLibrary.setTitlePicUrl(pmsProductUserMatchLibrary.getTitlePicUrl());
        return pmsProductMatchLibrary;
    }

    /**
     * 组装用户的搭配信息
     */
    public static PmsProductUserMatchLibrary assembleUserMatchLibrary(Long memberId,PmsProductMatchLibrary pmsProductMatchLibrary) {
        PmsProductUserMatchLibrary pmsProductUserMatchLibrary = new PmsProductUserMatchLibrary();
        pmsProductUserMatchLibrary.setId(IdGeneratorUtil.getIdGeneratorUtil().nextId());
        pmsProductUserMatchLibrary.setCreateTime(new Date());
        pmsProductUserMatchLibrary.setUpdateTime(new Date());
        if(org.springframework.util.StringUtils.isEmpty(pmsProductUserMatchLibrary.getMatchType())){
            pmsProductUserMatchLibrary.setMatchType(MagicConstant.MATCH_TYPE_COMBIN);
        }
        pmsProductUserMatchLibrary.setMatchId(pmsProductMatchLibrary.getId());
        pmsProductUserMatchLibrary.setMatchUserId(pmsProductMatchLibrary.getOperateId());
        if(org.springframework.util.StringUtils.isEmpty(pmsProductUserMatchLibrary.getRecommendType())){
            pmsProductUserMatchLibrary.setRecommendType(MagicConstant.RECOMMEND_TYPE_NO);
        }
        pmsProductUserMatchLibrary.setTitle(pmsProductMatchLibrary.getTitle());
        pmsProductUserMatchLibrary.setTitleDescr(pmsProductMatchLibrary.getTitleDescr());
        pmsProductUserMatchLibrary.setTitlePicUrl(pmsProductMatchLibrary.getTitlePicUrl());
        pmsProductUserMatchLibrary.setUserId(memberId);
        return pmsProductUserMatchLibrary;
    }
}
