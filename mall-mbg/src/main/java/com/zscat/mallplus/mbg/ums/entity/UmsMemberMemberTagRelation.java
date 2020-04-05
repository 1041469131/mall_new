package com.zscat.mallplus.mbg.ums.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 用户和标签关系表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@TableName("ums_member_member_tag_relation")
public class UmsMemberMemberTagRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("member_id")
    @ApiModelProperty("用户id")
    private Long memberId;

    @TableField("tag_id")
    @ApiModelProperty("标签id，前端传入的数据不需要管这个字段")
    private Long tagId;

    @TableField("create_time")
    private Date createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "UmsMemberMemberTagRelation{" +
        ", id=" + id +
        ", memberId=" + memberId +
        ", tagId=" + tagId +
        ", createTime=" + createTime +
        "}";
    }
}
