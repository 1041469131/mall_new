package com.zscat.mallplus.mbg.ums.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

public class UmsRecommendRelation implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**推荐人id*/
    @TableField("recommend_id")
    @ApiModelProperty(value = "推荐人id")
    private Long recommendId;

    /**被推荐人id*/
    @TableField("recommended_id")
    @ApiModelProperty(value = "被推荐人id")
    private Long recommendedId;

    /**推荐的状态 0-无效 1-有效*/
    @TableField("status")
    @ApiModelProperty(value = "推荐的状态 0-无效 1-有效")
    private String status;

    /**修改时间*/
    @TableField("update_time")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**创建时间*/
    @TableField("create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecommendId() {
        return recommendId;
    }

    public void setRecommendId(Long recommendId) {
        this.recommendId = recommendId;
    }

    public Long getRecommendedId() {
        return recommendedId;
    }

    public void setRecommendedId(Long recommendedId) {
        this.recommendedId = recommendedId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", recommendId=").append(recommendId);
        sb.append(", recommendedId=").append(recommendedId);
        sb.append(", status=").append(status);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}