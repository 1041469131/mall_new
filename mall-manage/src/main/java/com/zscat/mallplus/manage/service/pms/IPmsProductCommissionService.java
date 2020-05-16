package com.zscat.mallplus.manage.service.pms;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.mbg.pms.entity.PmsProductCommission;
import com.zscat.mallplus.mbg.pms.vo.PmsProductCommissionVo;

/**
 * 商品佣金设置
 */
public interface IPmsProductCommissionService extends IService<PmsProductCommission>{

    void batchUpdateCommission(PmsProductCommissionVo pmsProductCommissionVo);

    void deletePmsCommissionByProductId(Long productId);
}
