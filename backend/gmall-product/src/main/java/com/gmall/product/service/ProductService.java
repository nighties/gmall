package com.gmall.product.service;

import com.gmall.common.PageResult;
import com.gmall.product.dto.ProductQueryRequest;
import com.gmall.product.dto.ProductVO;
import com.gmall.product.entity.Product;

import java.util.List;

/**
 * 商品服务接口
 */
public interface ProductService {

    /**
     * 分页查询商品列表
     *
     * @param request 查询请求
     * @return 商品列表
     */
    PageResult<ProductVO> listProducts(ProductQueryRequest request);

    /**
     * 根据 ID 获取商品详情
     *
     * @param productId 商品 ID
     * @return 商品详情
     */
    ProductVO getProductById(Long productId);

    /**
     * 创建商品
     *
     * @param product 商品信息
     * @return 商品 ID
     */
    Long createProduct(Product product);

    /**
     * 更新商品信息
     *
     * @param product 商品信息
     */
    void updateProduct(Product product);

    /**
     * 删除商品
     *
     * @param productId 商品 ID
     */
    void deleteProduct(Long productId);

    /**
     * 上架/下架商品
     *
     * @param productId 商品 ID
     * @param status 状态
     */
    void updateProductStatus(Long productId, Integer status);
}
