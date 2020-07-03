package com.zscat.mallplus.mbg.ums.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class UmsRecommendRelation implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**推荐人id*/
    @TableField("recommend_id")
    @ApiModelProperty(value = "推荐人id")
    private Long recommendId;

    /**被推荐人id*/
    @TableField("recommended_id")
    @ApiModelProperty(value = "被推荐人id")
    private Long recommendedId;

    /**推荐的状态 0-无效 1-有效*/
    @TableField("status")
    @ApiModelProperty(value = "推荐的状态 0-无效 1-有效")
    private String status;

    /**修改时间*/
    @TableField("update_time")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**创建时间*/
    @TableField("create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}