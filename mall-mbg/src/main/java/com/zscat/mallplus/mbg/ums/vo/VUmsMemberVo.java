package com.zscat.mallplus.mbg.ums.vo;

import com.zscat.mallplus.mbg.ums.entity.VUmsMember;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel("粉丝和平台管理扩展对象")
public class VUmsMemberVo extends VUmsMember {

    @ApiModelProperty("推荐数量")
    private Integer recomendCount;

    @ApiModelProperty("处理的状态 0-待处理 1-急需处理 2-已处理")
    private String status;

    public Integer getRecomendCount() {
        return recomendCount;
    }

    public void setRecomendCount(Integer recomendCount) {
        this.recomendCount = recomendCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
