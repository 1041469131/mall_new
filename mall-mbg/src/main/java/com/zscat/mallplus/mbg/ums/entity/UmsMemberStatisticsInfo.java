package com.zscat.mallplus.mbg.ums.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import lombok.Data;

/**
 * <p>
 * 会员统计信息
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@TableName("ums_member_statistics_info")
@Data
public class UmsMemberStatisticsInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("member_id")
    private Long memberId;

    /**
     * 累计消费金额
     */
    @TableField("consume_amount")
    private BigDecimal consumeAmount;

    /**
     * 订单数量
     */
    @TableField("order_count")
    private Integer orderCount;

    /**
     * 优惠券数量
     */
    @TableField("coupon_count")
    private Integer couponCount;

    /**
     * 评价数
     */
    @TableField("comment_count")
    private Integer commentCount;

    /**
     * 退货数量
     */
    @TableField("return_order_count")
    private Integer returnOrderCount;

    /**
     * 登录次数
     */
    @TableField("login_count")
    private Integer loginCount;

    /**
     * 关注数量
     */
    @TableField("attend_count")
    private Integer attendCount;

    /**
     * 粉丝数量
     */
    @TableField("fans_count")
    private Integer fansCount;

    @TableField("collect_product_count")
    private Integer collectProductCount;

    @TableField("collect_subject_count")
    private Integer collectSubjectCount;

    @TableField("collect_topic_count")
    private Integer collectTopicCount;

    @TableField("collect_comment_count")
    private Integer collectCommentCount;

    @TableField("invite_friend_count")
    private Integer inviteFriendCount;

    /**
     * 最后一次下订单时间
     */
    @TableField("recent_order_time")
    private Date recentOrderTime;

    @ApiModelProperty("推荐日期")
    private Date recomendDate;

    @ApiModelProperty("推荐日期的时间戳")
    private Long recomendTime;

    @ApiModelProperty("创建日期")
    private Date createDate;

    @ApiModelProperty("推荐状态")
    private String recommendStatus;

    @ApiModelProperty("推荐数量")
    private Integer recommendCount;

    @ApiModelProperty("用户标签")
    private String tagName;
}
