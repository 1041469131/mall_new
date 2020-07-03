package com.zscat.mallplus.mbg.ums.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;

/**
 * <p>
 * 会员积分成长规则表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@TableName("ums_member_rule_setting")
@Data
public class UmsMemberRuleSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 连续签到天数
     */
    @TableField("continue_sign_day")
    private Integer continueSignDay;

    /**
     * 连续签到赠送数量
     */
    @TableField("continue_sign_point")
    private Integer continueSignPoint;

    /**
     * 每消费多少元获取1个点
     */
    @TableField("consume_per_point")
    private BigDecimal consumePerPoint;

    /**
     * 最低获取点数的订单金额
     */
    @TableField("low_order_amount")
    private BigDecimal lowOrderAmount;

    /**
     * 每笔订单最高获取点数
     */
    @TableField("max_point_per_order")
    private Integer maxPointPerOrder;

    /**
     * 类型：0->积分规则；1->成长值规则
     */
    private Integer type;
}
