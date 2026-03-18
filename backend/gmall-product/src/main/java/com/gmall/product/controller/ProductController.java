package com.gmall.product.controller;

import com.gmall.Result;
import com.gmall.common.PageResult;
import com.gmall.product.dto.CategoryVO;
import com.gmall.product.dto.ProductQueryRequest;
import com.gmall.product.dto.ProductVO;
import com.gmall.product.entity.Product;
import com.gmall.product.service.CategoryService;
import com.gmall.product.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品控制器
 */
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Api(tags = "商品管理")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/list")
    @ApiOperation("分页查询商品列表")
    public Result<PageResult<List<ProductVO>>> listProducts(ProductQueryRequest request) {
        PageResult<List<ProductVO>> result = productService.listProducts(request);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据 ID 获取商品详情")
    public Result<ProductVO> getProductById(@PathVariable Long id) {
        ProductVO productVO = productService.getProductById(id);
        return Result.success(productVO);
    }

    @PostMapping("/create")
    @ApiOperation("创建商品")
    public Result<Long> createProduct(@RequestBody Product product) {
        Long productId = productService.createProduct(product);
        return Result.success("创建成功", productId);
    }

    @PutMapping("/update")
    @ApiOperation("更新商品信息")
    public Result<Void> updateProduct(@RequestBody Product product) {
        productService.updateProduct(product);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除商品")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return Result.success("删除成功");
    }

    @PutMapping("/{id}/status")
    @ApiOperation("上架/下架商品")
    public Result<Void> updateProductStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        productService.updateProductStatus(id, status);
        return Result.success("操作成功");
    }

    @GetMapping("/category/tree")
    @ApiOperation("获取分类树形结构")
    public Result<List<CategoryVO>> getCategoryTree() {
        List<CategoryVO> tree = categoryService.getTree();
        return Result.success(tree);
    }

    @GetMapping("/category/{id}")
    @ApiOperation("根据 ID 获取分类详情")
    public Result<CategoryVO> getCategoryById(@PathVariable Long id) {
        CategoryVO categoryVO = categoryService.getCategoryById(id);
        return Result.success(categoryVO);
    }

    @GetMapping("/category/parent/{parentId}")
    @ApiOperation("根据父分类 ID 获取子分类列表")
    public Result<List<CategoryVO>> listByParentId(@PathVariable Long parentId) {
        List<CategoryVO> list = categoryService.listByParentId(parentId);
        return Result.success(list);
    }
}
