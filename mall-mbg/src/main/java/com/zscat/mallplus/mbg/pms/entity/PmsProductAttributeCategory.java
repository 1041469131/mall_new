package com.zscat.mallplus.mbg.pms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 产品属性分类表
 * </p>
 *
 * @author zscat
 * @since 2019-04-19
 */
@Data
@TableName("pms_product_attribute_category")
public class PmsProductAttributeCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    /**
     * 属性数量
     */
    @TableField("attribute_count")
    private Integer attributeCount;

    /**
     * 参数数量
     */
    @TableField("param_count")
    private Integer paramCount;
    @TableField(exist = false)
    List<PmsProduct> goodsList;

    @TableField("param_count")
    @ApiModelProperty("商品分类id")
    private Long categoryId;

    public Long getId() {
        return id;
    }

    public PmsProductAttributeCategory setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public PmsProductAttributeCategory setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getAttributeCount() {
        return attributeCount;
    }

    public PmsProductAttributeCategory setAttributeCount(Integer attributeCount) {
        this.attributeCount = attributeCount;
        return this;
    }

    public Integer getParamCount() {
        return paramCount;
    }

    public PmsProductAttributeCategory setParamCount(Integer paramCount) {
        this.paramCount = paramCount;
        return this;
    }

    public List<PmsProduct> getGoodsList() {
        return goodsList;
    }

    public PmsProductAttributeCategory setGoodsList(List<PmsProduct> goodsList) {
        this.goodsList = goodsList;
        return this;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public PmsProductAttributeCategory setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
        return this;
    }
}
