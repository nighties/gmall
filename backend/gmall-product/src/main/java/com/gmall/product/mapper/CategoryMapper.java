package com.gmall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gmall.product.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分类 Mapper 接口
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}
