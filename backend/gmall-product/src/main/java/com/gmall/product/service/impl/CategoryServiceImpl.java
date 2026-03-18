package com.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gmall.product.dto.CategoryVO;
import com.gmall.product.entity.Category;
import com.gmall.product.mapper.CategoryMapper;
import com.gmall.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分类服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryVO> getTree() {
        log.info("查询分类树");

        // 查询所有一级分类
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getParentId, 0)
                .eq(Category::getStatus, 1)
                .orderByAsc(Category::getSort);
        List<Category> rootCategories = categoryMapper.selectList(wrapper);

        // 转换为 VO 并加载子分类
        return rootCategories.stream()
                .map(this::convertToVOWithChildren)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryVO getCategoryById(Long categoryId) {
        log.info("查询分类详情，分类 ID: {}", categoryId);

        Category category = categoryMapper.selectById(categoryId);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }

        return convertToVO(category);
    }

    @Override
    public List<CategoryVO> listByParentId(Long parentId) {
        log.info("查询子分类列表，父分类 ID: {}", parentId);

        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getParentId, parentId)
                .eq(Category::getStatus, 1)
                .orderByAsc(Category::getSort);
        List<Category> categories = categoryMapper.selectList(wrapper);

        return categories.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 转换为 VO
     */
    private CategoryVO convertToVO(Category category) {
        CategoryVO vo = new CategoryVO();
        BeanUtils.copyProperties(category, vo);
        return vo;
    }

    /**
     * 转换为 VO 并加载子分类 (简化版，暂不实现多级)
     */
    private CategoryVO convertToVOWithChildren(Category category) {
        CategoryVO vo = convertToVO(category);
        // TODO: 未来可以实现加载子分类逻辑
        return vo;
    }
}
