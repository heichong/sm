package com.zongb.sm.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.zongb.sm.entity.Product;
import com.zongb.sm.entity.ProductCategory;
import com.zongb.sm.entity.ProductDesc;
import com.zongb.sm.entity.ProductImage;
/**
 * 此接口无需实现类
 * @author Administrator
 *
 */
@Repository
public interface ProductMapper {

	/**
	 * 获取此类商品的最大编号modeid
	 * @param modelPre 商品编号的前缀 【日期+商品类型编码】
	 */
	public String getMaxModeByType(@Param("modelPre") String modelPre);
	
	/**
	 * 添加产品主表
	 * @param product
	 */
	public int saveProduct(Product product);

	/**
	 * 添加产品描述表
	 * @param product
	 */
	public int saveProductDesc(ProductDesc pDesc);

	/**
	 * 添加产品图片
	 * @param product
	 */
	public int saveProductImage(ProductImage image);

	/**
	 * 添加产品目录
	 * @param product
	 */
	public int saveProductCategory(ProductCategory pc);

	/**
	 * 添加产品商店
	 * @param product
	 */
	public int saveProductStore(Product product);
}
