package com.zscat.mallplus.mbg.ums.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import lombok.Data;

/**
 * <p>
 * 用户和标签关系表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@TableName("ums_member_member_tag_relation")
@Data
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
}
