package com.zscat.mallplus.mbg.ums.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;

/**
 * <p>
 * 积分变化历史记录表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@TableName("ums_integration_change_history")
@Data
public class UmsIntegrationChangeHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("member_id")
    private Long memberId;

    @TableField("create_time")
    private Date createTime;

    /**
     * 改变类型：0->增加；1->减少
     */
    @TableField("change_type")
    private Integer changeType;

    /**
     * 积分改变数量
     */
    @TableField("change_count")
    private Integer changeCount;

    /**
     * 操作人员
     */
    @TableField("operate_man")
    private String operateMan;

    /**
     * 操作备注
     */
    @TableField("operate_note")
    private String operateNote;

    /**
     * 积分来源：0->购物；1->管理员修改
     */
    @TableField("source_type")
    private Integer sourceType;
}
