package com.zscat.mallplus.manage.service.pms.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.manage.service.cms.ICmsPrefrenceAreaProductRelationService;
import com.zscat.mallplus.manage.service.cms.ICmsSubjectProductRelationService;
import com.zscat.mallplus.manage.service.pms.*;
import com.zscat.mallplus.manage.utils.DateUtils;
import com.zscat.mallplus.manage.utils.UserUtils;
import com.zscat.mallplus.mbg.marking.entity.SmsGroup;
import com.zscat.mallplus.mbg.marking.entity.SmsGroupMember;
import com.zscat.mallplus.mbg.marking.mapper.SmsGroupMapper;
import com.zscat.mallplus.mbg.marking.mapper.SmsGroupMemberMapper;
import com.zscat.mallplus.mbg.pms.entity.*;
import com.zscat.mallplus.mbg.pms.mapper.*;
import com.zscat.mallplus.mbg.pms.vo.PmsProductAndGroup;
import com.zscat.mallplus.mbg.pms.vo.PmsProductParam;
import com.zscat.mallplus.mbg.pms.vo.PmsProductQueryParam;
import com.zscat.mallplus.mbg.pms.vo.PmsProductResult;
import com.zscat.mallplus.mbg.pms.vo.PmsProductVo;
import com.zscat.mallplus.mbg.utils.constant.MagicConstant;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Slf4j
@Service
public class PmsProductServiceImpl extends ServiceImpl<PmsProductMapper, PmsProduct> implements IPmsProductService {

    @Resource
    private PmsProductMapper pmsProductMapper;
    @Resource
    private IPmsMemberPriceService memberPriceDao;
    @Resource
    private PmsMemberPriceMapper memberPriceMapper;
    @Resource
    private IPmsProductLadderService productLadderDao;
    @Resource
    private PmsProductLadderMapper productLadderMapper;
    @Resource
    private IPmsProductFullReductionService productFullReductionDao;
    @Resource
    private PmsProductFullReductionMapper productFullReductionMapper;
    @Resource
    private IPmsSkuStockService skuStockDao;
    @Resource
    private PmsSkuStockMapper skuStockMapper;
    @Resource
    private IPmsProductAttributeValueService productAttributeValueDao;
    @Resource
    private PmsProductAttributeValueMapper productAttributeValueMapper;
    @Resource
    private ICmsSubjectProductRelationService subjectProductRelationDao;
    @Resource
    private CmsSubjectProductRelationMapper subjectProductRelationMapper;
    @Resource
    private ICmsPrefrenceAreaProductRelationService prefrenceAreaProductRelationDao;
    @Resource
    private CmsPrefrenceAreaProductRelationMapper prefrenceAreaProductRelationMapper;
    @Resource
    private PmsProductVertifyRecordMapper productVertifyRecordMapper;
    @Resource
    private SmsGroupMapper groupMapper;
    @Resource
    private SmsGroupMemberMapper groupMemberMapper;
    @Autowired
    private PmsProductCommissionMapper pmsProductCommissionMapper;

    @Override
    public Long create(PmsProductParam productParam) {
        //创建商品
        PmsProduct product = productParam;
        product.setDeleteStatus(0);
        product.setUnit("件");
        product.setNewStatus(0);
        product.setVerifyStatus(0);
        product.setCreateTime(new Date());
        pmsProductMapper.insert(product);
        //根据促销类型设置价格：、阶梯价格、满减价格
        Long productId = product.getId();
        //会员价格
        relateAndInsertList(memberPriceDao, productParam.getMemberPriceList(), productId);
        //阶梯价格
        relateAndInsertList(productLadderDao, productParam.getProductLadderList(), productId);
        //满减价格
        relateAndInsertList(productFullReductionDao, productParam.getProductFullReductionList(), productId);
        //处理sku的编码
        handleSkuStockCode(productParam.getSkuStockList(), productId);
        //添加sku库存信息
        relateAndInsertList(skuStockDao, productParam.getSkuStockList(), productId);
        //添加商品参数,添加自定义商品规格
        relateAndInsertList(productAttributeValueDao, productParam.getProductAttributeValueList(), productId);
        //关联专题
        relateAndInsertList(subjectProductRelationDao, productParam.getSubjectProductRelationList(), productId);
        //关联优选
        relateAndInsertList(prefrenceAreaProductRelationDao, productParam.getPrefrenceAreaProductRelationList(), productId);
        return productId;
    }

    private void handleSkuStockCode(List<PmsSkuStock> skuStockList, Long productId) {
        if (CollectionUtils.isEmpty(skuStockList)) {
            return;
        }
        for (int i = 0; i < skuStockList.size(); i++) {
            PmsSkuStock skuStock = skuStockList.get(i);
            if (StringUtils.isEmpty(skuStock.getSkuCode())) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                StringBuilder sb = new StringBuilder();
                //日期
                sb.append(sdf.format(new Date()));
                //四位商品id
                sb.append(String.format("%04d", productId));
                //三位索引id
                sb.append(String.format("%03d", i + 1));
                skuStock.setSkuCode(sb.toString());
            }
        }
    }

    @Override
    public PmsProductResult getUpdateInfo(Long id) {
        return pmsProductMapper.getUpdateInfo(id);
    }

    @Override
    public List<PmsProductResult> getProductResults(List<Long> ids) {
        return pmsProductMapper.getProductResults(ids);
    }

    @Override
    public int update(Long id, PmsProductParam productParam) {
        //更新商品信息
        PmsProduct product = productParam;
        product.setId(id);
        pmsProductMapper.updateById(product);
        //会员价格
       // memberPriceMapper.delete(new QueryWrapper<PmsMemberPrice>().eq("product_id",id));
        relateAndInsertList(memberPriceDao, productParam.getMemberPriceList(), id);
        //阶梯价格
       // productLadderMapper.delete(new QueryWrapper<PmsProductLadder>().eq("product_id",id));
        relateAndInsertList(productLadderDao, productParam.getProductLadderList(), id);
        //满减价格
      //  productFullReductionMapper.delete(new QueryWrapper<PmsProductFullReduction>().eq("product_id",id));
        relateAndInsertList(productFullReductionDao, productParam.getProductFullReductionList(), id);
        //修改sku库存信息
        //skuStockMapper.delete(new QueryWrapper<PmsSkuStock>().eq("product_id",id));
        handleSkuStockCode(productParam.getSkuStockList(), id);
        relateAndInsertList(skuStockDao, productParam.getSkuStockList(), id);
        //修改商品参数,添加自定义商品规格
        //productAttributeValueMapper.delete(new QueryWrapper<PmsProductAttributeValue>().eq("product_id",id));
        relateAndInsertList(productAttributeValueDao, productParam.getProductAttributeValueList(), id);
        //关联专题
        //subjectProductRelationMapper.delete(new QueryWrapper<CmsSubjectProductRelation>().eq("product_id",id));
        relateAndInsertList(subjectProductRelationDao, productParam.getSubjectProductRelationList(), id);
        //关联优选
       // prefrenceAreaProductRelationMapper.delete(new QueryWrapper<CmsPrefrenceAreaProductRelation>().eq("product_id",id));
        relateAndInsertList(prefrenceAreaProductRelationDao, productParam.getPrefrenceAreaProductRelationList(), id);
        return 1;
    }



    @Override
    public int updateVerifyStatus(Long ids, Integer verifyStatus, String detail) {
        PmsProduct product = new PmsProduct();
        product.setVerifyStatus(verifyStatus);
        int count = pmsProductMapper.update(product, new QueryWrapper<PmsProduct>().in("id",ids) );
        //修改完审核状态后插入审核记录

        PmsProductVertifyRecord record = new PmsProductVertifyRecord();
        record.setProductId(ids);
        record.setCreateTime(new Date());
        record.setDetail(detail);
        record.setStatus(verifyStatus);
        record.setVertifyMan(UserUtils.getCurrentMember().getUsername());
        productVertifyRecordMapper.insert(record);
        return count;
    }

    @Override
    public int updatePublishStatus(List<Long> ids, Integer publishStatus) {
        PmsProduct record = new PmsProduct();
        record.setPublishStatus(publishStatus);

        return pmsProductMapper.update(record, new QueryWrapper<PmsProduct>().in("id",ids));
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        PmsProduct record = new PmsProduct();
        record.setRecommandStatus(recommendStatus);

        return pmsProductMapper.update(record, new QueryWrapper<PmsProduct>().in("id",ids));
    }

    @Override
    public int updateNewStatus(List<Long> ids, Integer newStatus) {
        PmsProduct record = new PmsProduct();
        record.setNewStatus(newStatus);

        return pmsProductMapper.update(record, new QueryWrapper<PmsProduct>().in("id",ids));
    }

    @Override
    public int updateDeleteStatus(List<Long> ids, Integer deleteStatus) {
        PmsProduct record = new PmsProduct();
        record.setDeleteStatus(deleteStatus);
        return pmsProductMapper.update(record, new QueryWrapper<PmsProduct>().in("id",ids));
    }

    @Override
    public List<PmsProduct> list(String keyword) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("delete_status",0);

        if (!StringUtils.isEmpty(keyword)) {
            queryWrapper.like("name",keyword);

        }
        return pmsProductMapper.selectList(queryWrapper);
    }

    @Override
    public List<PmsProductVertifyRecord> getProductVertifyRecord(Long id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("product_id",id);

        return productVertifyRecordMapper.selectList(queryWrapper);
    }


    /**
     * 建立和插入关系表操作
     *
     * @param dao       可以操作的dao
     * @param dataList  要插入的数据
     * @param productId 建立关系的id
     */
    private void relateAndInsertList(Object dao, List dataList, Long productId) {
        try {
            if (CollectionUtils.isEmpty(dataList)) {
                return;
            }
            for (Object item : dataList) {
                Method setProductId = item.getClass().getMethod("setProductId", Long.class);
                setProductId.invoke(item, productId);
            }
            Method insertList = dao.getClass().getMethod("saveOrUpdateBatch", Collection.class);
            insertList.invoke(dao, dataList);
        } catch (Exception e) {
            log.warn("创建产品出错:{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String deleteProduct(Long productId) {
        StringBuffer errMsg = new StringBuffer();
        PmsProduct pmsProduct = pmsProductMapper.selectById(productId);
        int deleteStatus = pmsProduct.getDeleteStatus();//删除状态
        int pushStatus = pmsProduct.getPublishStatus();//上架状态
        int verifyStatus = pmsProduct.getVerifyStatus();//审核状态
        if(MagicConstant.DELETE_YET == deleteStatus){
            errMsg.append(" 已删除");
        }
        if(MagicConstant.PUBLISH_STATUS_UP == pushStatus){
            errMsg.append(" 已上架状态");
        }
        if(MagicConstant.VERIFY_STATUS_VERIFYED == verifyStatus){
            errMsg.append(" 已审核");
        }
        if(!StringUtils.isEmpty(errMsg.toString())){
            errMsg.insert(0, "该条记录");
        }
        pmsProduct.setDeleteStatus(MagicConstant.DEFAULT_STATUS_YES);
        pmsProductMapper.updateById(pmsProduct);
        return errMsg.toString();
    }

    @Override
    public PmsProductAndGroup getProductAndGroup(Long id) {
        PmsProduct goods = pmsProductMapper.selectById(id);
        PmsProductAndGroup vo = new PmsProductAndGroup();
        try {
            BeanUtils.copyProperties(goods, vo);
            SmsGroup queryG = new SmsGroup();
            queryG.setGoodsId(id);
            SmsGroup group = groupMapper.selectOne(new QueryWrapper<>(queryG));
            SmsGroupMember newG = new SmsGroupMember();
            newG.setGoodsId(id);
            List<SmsGroupMember> list = groupMemberMapper.selectList(new QueryWrapper<>(newG));
            if (group != null) {
                Map<String, List<SmsGroupMember>> map = groupMemberByMainId(list, group);
                vo.setMap(map);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return vo;
    }

    /**
     * 按照异常批次号对已开单数据进行分组
     *
     * @param billingList
     * @return
     * @throws Exception
     */
    private Map<String, List<SmsGroupMember>> groupMemberByMainId(List<SmsGroupMember> billingList, SmsGroup group) throws Exception {
        Map<String, List<SmsGroupMember>> resultMap = new HashMap<String, List<SmsGroupMember>>();
        Map<String, List<SmsGroupMember>> map = new HashMap<String, List<SmsGroupMember>>();
        try {
            List<Long> ids = new ArrayList<>();
            for (SmsGroupMember tmExcpNew : billingList) {
                if (tmExcpNew.getMemberId().equals(tmExcpNew.getMainId())) {
                    Date cr = tmExcpNew.getCreateTime();
                    Long nowT = System.currentTimeMillis();
                    Date endTime = DateUtils.convertStringToDate(DateUtils.addHours(cr, group.getHours()));
                    if (nowT <= endTime.getTime()) {
                        ids.add(tmExcpNew.getMainId());
                    }
                }
                if (resultMap.containsKey(tmExcpNew.getMainId()+"")) {//map中异常批次已存在，将该数据存放到同一个key（key存放的是异常批次）的map中
                    resultMap.get(tmExcpNew.getMainId()+"").add(tmExcpNew);
                } else {//map中不存在，新建key，用来存放数据
                    List<SmsGroupMember> tmpList = new ArrayList<SmsGroupMember>();
                    tmpList.add(tmExcpNew);
                    resultMap.put(tmExcpNew.getMainId() + "", tmpList);
                }
            }
            for (Long id : ids) {
                if (resultMap.get(id + "") != null) {
                    map.put(id + "", resultMap.get(id + ""));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("按照异常批次号对已开单数据进行分组时出现异常", e);
        }

        return map;
    }

    @Override
    public Page<PmsProductVo> listPmsProductByPage(PmsProductQueryParam queryParam) {
        Page<PmsProductVo> page = new Page<>(queryParam.getPageNum(),queryParam.getPageSize());
        Page<PmsProductVo> pmsProductVoPage = pmsProductMapper.listPmsProductByPage(page,queryParam);
        List<PmsProductVo> pmsProductVos = pmsProductVoPage.getRecords();
        buildSkuAndCommission1(pmsProductVos);
        pmsProductVoPage.setRecords(pmsProductVos);
        return pmsProductVoPage;
    }

    @Override
    public Page<PmsProduct> listProductsByPage(PmsProductQueryParam queryParam) {
        Page<PmsProductVo> page = new Page<>(queryParam.getPageNum(),queryParam.getPageSize());
        Page<PmsProductVo> pmsProductVoPage = pmsProductMapper.listPmsProductByPage(page,queryParam);
        List<PmsProduct> pmsProducts = pmsProductVoPage.getRecords().stream().map(pmsProductVo -> (PmsProduct) pmsProductVo)
          .collect(Collectors.toList());
        Page<PmsProduct> pmsProductPage = new Page<>(pmsProductVoPage.getCurrent(), pmsProductVoPage.getSize(),
          pmsProductVoPage.getTotal());
        pmsProductPage.setRecords(pmsProducts);
        return pmsProductPage;
    }

    private void buildSkuAndCommission(List<PmsProductVo> pmsProductVos) {
        if(!CollectionUtils.isEmpty(pmsProductVos)){
            for(PmsProductVo productVo : pmsProductVos){
                List<PmsProductCommission> pmsProductCommissions = pmsProductCommissionMapper.selectList(new QueryWrapper<PmsProductCommission>().eq("product_id",productVo.getId()));
                productVo.setPmsProductCommissions(pmsProductCommissions);
                List<PmsSkuStock> pmsSkuStocks = skuStockMapper
                  .selectList(new QueryWrapper<PmsSkuStock>().eq("product_id", productVo.getId()));
                productVo.setPmsSkuStocks(pmsSkuStocks);
            }
        }
    }

    private void buildSkuAndCommission1(List<PmsProductVo> pmsProductVos) {
        if(!CollectionUtils.isEmpty(pmsProductVos)){
            List<Long> productIds = pmsProductVos.stream().map(PmsProductVo::getId).distinct().collect(Collectors.toList());
            Map<Long, PmsProductResult> pmsProductResultMap = pmsProductMapper.getProductResults(productIds).stream()
              .collect(Collectors.toMap(PmsProductResult::getId, Function.identity()));
            for(PmsProductVo productVo : pmsProductVos){
                PmsProductResult pmsProductResult = pmsProductResultMap.get(productVo.getId());
                productVo.setPmsProductCommissions(pmsProductResult.getPmsProductCommissions());
                productVo.setPmsSkuStocks(pmsProductResult.getSkuStockList());
                productVo.setFavoriteType(pmsProductResult.getFavoriteType());
            }
        }
    }

    @Override
    public Page<PmsProductVo> listPmsProductCollectByPage(PmsProductQueryParam queryParam) {
        Page<PmsProductVo> page = new Page<>(queryParam.getPageNum(),queryParam.getPageSize());
        Page<PmsProductVo> pmsProductVoPage = pmsProductMapper.listPmsProductCollectByPage(page,queryParam);
        List<PmsProductVo> pmsProductVos = pmsProductVoPage.getRecords();
        buildSkuAndCommission1(pmsProductVos);
        pmsProductVoPage.setRecords(pmsProductVos);

        return pmsProductVoPage;
    }
}


