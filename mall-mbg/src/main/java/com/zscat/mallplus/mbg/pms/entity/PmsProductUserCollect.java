package com.zscat.mallplus.mbg.pms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

@ApiModel("单品收藏")
@Data
public class PmsProductUserCollect implements Serializable {
    private Long id;
    /**产品id*/
    @TableField("product_id")
    @ApiModelProperty(value = "产品id")
    private Long productId;
    @ApiModelProperty("搭配师id")
    private Long matchUserId;
    private static final long serialVersionUID = 1L;

}