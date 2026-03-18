package com.gmall.product.dto;

import lombok.Data;

/**
 * 分类信息响应 DTO
 */
@Data
public class CategoryVO {

    /**
     * 分类 ID
     */
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 父分类 ID
     */
    private Long parentId;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 图标
     */
    private String icon;
}
