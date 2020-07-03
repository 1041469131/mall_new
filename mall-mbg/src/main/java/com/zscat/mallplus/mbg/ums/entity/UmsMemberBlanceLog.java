package com.zscat.mallplus.mbg.ums.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@TableName("ums_member_blance_log")
@Data
public class UmsMemberBlanceLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("member_id")
    private Long memberId;

    private BigDecimal price;

    private Integer type;

    private String note;

    @TableField("create_time")
    private Date createTime;
}
