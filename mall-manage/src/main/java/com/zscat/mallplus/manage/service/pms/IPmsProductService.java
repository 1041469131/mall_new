package com.zscat.mallplus.manage.service.pms;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.pms.entity.PmsProduct;
import com.zscat.mallplus.mbg.pms.entity.PmsProductVertifyRecord;
import com.zscat.mallplus.mbg.pms.vo.PmsProductAndGroup;
import com.zscat.mallplus.mbg.pms.vo.PmsProductParam;
import com.zscat.mallplus.mbg.pms.vo.PmsProductQueryParam;
import com.zscat.mallplus.mbg.pms.vo.PmsProductResult;
import com.zscat.mallplus.mbg.pms.vo.PmsProductVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品信息 服务类
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
public interface IPmsProductService extends IService<PmsProduct> {

    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
    Long create(PmsProductParam productParam);

    /**
     * 根据商品编号获取更新信息
     */
    PmsProductResult getUpdateInfo(Long id);

    List<PmsProductResult> getProductResults(List<Long> ids);

    /**
     * 更新商品
     */
    @Transactional
    int update(Long id, PmsProductParam productParam);
    /**
     * 批量修改审核状态
     *
     * @param ids          产品id
     * @param verifyStatus 审核状态
     * @param detail       审核详情
     */
    @Transactional
    int updateVerifyStatus(Long ids, Integer verifyStatus, String detail);

    /**
     * 批量修改商品上架状态
     */
    int updatePublishStatus(List<Long> ids, Integer publishStatus);

    /**
     * 批量修改商品推荐状态
     */
    int updateRecommendStatus(List<Long> ids, Integer recommendStatus);

    /**
     * 批量修改新品状态
     */
    int updateNewStatus(List<Long> ids, Integer newStatus);

    /**
     * 批量删除商品
     */
    int updateDeleteStatus(List<Long> ids, Integer deleteStatus);

    /**
     * 根据商品名称或者货号模糊查询
     */
    List<PmsProduct> list(String keyword);

    List<PmsProductVertifyRecord> getProductVertifyRecord(Long id);


    String deleteProduct(Long productId);

    PmsProductAndGroup getProductAndGroup(Long id);

    Page<PmsProductVo> listPmsProductByPage(PmsProductQueryParam queryParam);

    Page<PmsProduct> listProductsByPage(PmsProductQueryParam queryParam);

    Page<PmsProductVo> listPmsProductCollectByPage(PmsProductQueryParam queryParam);
}
