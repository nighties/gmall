package com.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gmall.common.PageResult;
import com.gmall.product.dto.ProductQueryRequest;
import com.gmall.product.dto.ProductVO;
import com.gmall.product.entity.Category;
import com.gmall.product.entity.Product;
import com.gmall.product.entity.Shop;
import com.gmall.product.mapper.CategoryMapper;
import com.gmall.product.mapper.ProductMapper;
import com.gmall.product.mapper.ShopMapper;
import com.gmall.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final ShopMapper shopMapper;

    @Override
    public PageResult<ProductVO> listProducts(ProductQueryRequest request) {
        log.info("查询商品列表，请求：{}", request);

        // 创建分页对象
        Page<Product> page = new Page<>(request.getPageNum(), request.getPageSize());

        // 创建查询条件
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1); // 只查询上架商品

        // 分类筛选
        if (request.getCategoryId() != null) {
            wrapper.eq(Product::getCategoryId, request.getCategoryId());
        }

        // 商家筛选
        if (request.getShopId() != null) {
            wrapper.eq(Product::getShopId, request.getShopId());
        }

        // 价格区间筛选
        if (request.getMinPrice() != null) {
            wrapper.ge(Product::getPrice, request.getMinPrice());
        }
        if (request.getMaxPrice() != null) {
            wrapper.le(Product::getPrice, request.getMaxPrice());
        }

        // 关键词搜索
        if (StringUtils.hasText(request.getKeyword())) {
            wrapper.and(w -> w
                    .like(Product::getName, request.getKeyword())
                    .or()
                    .like(Product::getDescription, request.getKeyword()));
        }

        // 排序
        if (StringUtils.hasText(request.getSortBy())) {
            String sortBy = request.getSortBy();
            boolean isAsc = "asc".equalsIgnoreCase(request.getSortOrder());
            if ("price".equals(sortBy)) {
                wrapper.orderBy(true, isAsc, Product::getPrice);
            } else if ("sales".equals(sortBy)) {
                wrapper.orderBy(true, isAsc, Product::getSales);
            } else if ("create_time".equals(sortBy)) {
                wrapper.orderBy(true, isAsc, Product::getCreateTime);
            }
        } else {
            // 默认按创建时间降序
            wrapper.orderByDesc(Product::getCreateTime);
        }

        // 执行查询
        Page<Product> productPage = productMapper.selectPage(page, wrapper);

        // 转换为 VO
        List<ProductVO> productVOList = productPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        log.info("查询商品列表成功，总数：{}", productPage.getTotal());

        return new PageResult<ProductVO>(
                productPage.getCurrent(),
                productPage.getSize(),
                productPage.getTotal(),
                productVOList
        );
    }

    @Override
    public ProductVO getProductById(Long productId) {
        log.info("查询商品详情，商品 ID: {}", productId);

        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }

        return convertToVO(product);
    }

    @Override
    public Long createProduct(Product product) {
        log.info("创建商品，商品名称：{}", product.getName());

        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());
        productMapper.insert(product);

        log.info("创建商品成功，商品 ID: {}", product.getId());
        return product.getId();
    }

    @Override
    public void updateProduct(Product product) {
        log.info("更新商品信息，商品 ID: {}", product.getId());

        Product existProduct = productMapper.selectById(product.getId());
        if (existProduct == null) {
            throw new RuntimeException("商品不存在");
        }

        product.setUpdateTime(LocalDateTime.now());
        productMapper.updateById(product);

        log.info("更新商品成功，商品 ID: {}", product.getId());
    }

    @Override
    public void deleteProduct(Long productId) {
        log.info("删除商品，商品 ID: {}", productId);

        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }

        productMapper.deleteById(productId);

        log.info("删除商品成功，商品 ID: {}", productId);
    }

    @Override
    public void updateProductStatus(Long productId, Integer status) {
        log.info("更新商品状态，商品 ID: {}, 状态：{}", productId, status);

        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }

        product.setStatus(status);
        product.setUpdateTime(LocalDateTime.now());
        productMapper.updateById(product);

        log.info("更新商品状态成功，商品 ID: {}", productId);
    }

    /**
     * 转换为 VO
     */
    private ProductVO convertToVO(Product product) {
        ProductVO vo = new ProductVO();
        BeanUtils.copyProperties(product, vo);

        // 查询分类名称
        if (product.getCategoryId() != null) {
            Category category = categoryMapper.selectById(product.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getName());
            }
        }

        // 查询商家名称
        if (product.getShopId() != null) {
            Shop shop = shopMapper.selectById(product.getShopId());
            if (shop != null) {
                vo.setShopName(shop.getName());
            }
        }

        return vo;
    }
}
