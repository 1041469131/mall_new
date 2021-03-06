package com.zscat.mallplus.mbg.ums.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@TableName("ums_member_register_param")
@ApiModel("用户注册参数")
@Data
public class UmsMemberRegisterParam implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**参数名称*/
    @ApiModelProperty(value = "参数名称")
    private String name;

    /**参数名称*/
    @ApiModelProperty(value = "属性名对应的code")
    private String code;

    /**参数对应的图片地址*/
    @ApiModelProperty(value = "参数对应的图片地址")
    private String url;

    /**类型*/
    @ApiModelProperty(value = "类型(skin-皮肤,head-头部，neck-脖子，shoulder-肩型，chest-胸型，arm-臂长，belly-腹型，hip-臀部，" +
            "legtype-腿型，leglength-腿长，dressStyle-穿衣风格，dressColour-穿搭色系，neverDressStyle-永远都不会穿的图案，rightLining-合适面料，" +
            "shirt-衬衣，shorts-短裤，jeans-裤子，skuBudget-单品预算)")
    private String type;

    /**参数类型描述*/
    @ApiModelProperty(value = "参数类型描述")
    private String typeDescr;

    /**0-单选，1-多选，2-输入框，3-复选框*/
    @ApiModelProperty(value = "0-单选，1-多选，2-输入框，3-复选框")
    private String typeInput;

    /**更新时间*/
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**创建时间*/
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**排序序号*/
    @ApiModelProperty(value = "排序序号")
    private Integer orderNum;

    /**父id*/
    @ApiModelProperty(value = "父id")
    private Long parentId;

    private static final long serialVersionUID = 1L;
}