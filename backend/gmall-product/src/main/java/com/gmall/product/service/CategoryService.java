package com.gmall.product.service;

import com.gmall.product.dto.CategoryVO;

import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService {

    /**
     * 获取分类树形结构
     *
     * @return 分类树
     */
    List<CategoryVO> getTree();

    /**
     * 根据 ID 获取分类
     *
     * @param categoryId 分类 ID
     * @return 分类信息
     */
    CategoryVO getCategoryById(Long categoryId);

    /**
     * 根据父分类 ID 获取子分类列表
     *
     * @param parentId 父分类 ID
     * @return 分类列表
     */
    List<CategoryVO> listByParentId(Long parentId);
}
