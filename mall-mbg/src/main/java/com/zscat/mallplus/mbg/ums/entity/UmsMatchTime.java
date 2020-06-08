package com.zscat.mallplus.mbg.ums.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@ApiModel("搭配时间线")
@Data
public class UmsMatchTime implements Serializable {
    private Long id;


    @TableField("status")
    @ApiModelProperty(value = "-1代表未来状态 0空状态 1代表不搭配  2已经搭配 3代表需搭配 4急需搭配")
    private Integer status;

    /**搭配时间*/
    @TableField("match_time")
    @ApiModelProperty(value = "搭配时间")
    private Date matchTime;

    /**会员id*/
    @TableField("member_id")
    @ApiModelProperty(value = "会员id")
    private Long memberId;

    /**搭配师id*/
    @TableField("match_user_id")
    @ApiModelProperty(value = "搭配师id")
    private Long matchUserId;

    @ApiModelProperty("搭配推送时间间隔")
    @TableField("dress_freq_month")
    private Integer dressFreqMonth;

    private static final long serialVersionUID = 1L;
}