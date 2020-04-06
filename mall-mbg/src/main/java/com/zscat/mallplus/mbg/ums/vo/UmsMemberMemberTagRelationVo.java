package com.zscat.mallplus.mbg.ums.vo;

import com.zscat.mallplus.mbg.ums.entity.UmsMemberMemberTagRelation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Date: 2020/4/5
 * @Description
 */
@ApiModel("标签用户关联关系表")
public class UmsMemberMemberTagRelationVo extends UmsMemberMemberTagRelation {

    /**标签id列表*/
    @ApiModelProperty("给用户打标签的id，多个用户用逗号隔开")
    private String tagIds;

    @ApiModelProperty("用户标签的名称，多个名称用逗号隔开")
    private String tagNames;

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public String getTagNames() {
        return tagNames;
    }

    public void setTagNames(String tagNames) {
        this.tagNames = tagNames;
    }
}
