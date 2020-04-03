package com.zscat.mallplus.mbg.ums.vo;

import com.zscat.mallplus.mbg.ums.entity.UmsMemberTag;

public class UmsMemberTagVo extends UmsMemberTag{

    /**粉丝数量*/
    private Integer fanCount;

    public Integer getFanCount() {
        return fanCount;
    }

    public void setFanCount(Integer fanCount) {
        this.fanCount = fanCount;
    }
}
