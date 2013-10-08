package com.zongb.sm.service;

import java.util.Map;

import com.zongb.sm.entity.Product;

public interface ProductService {

	/**
	 * 添加产品
	 * @param user
	 */
	public void saveProduct(Product product);
	/**
	 * 获取此类商品的最大编号modeid
	 * @param modelPre 商品编号的前缀 【日期+商品类型编码】
	 */
	public String getMaxModeByType(String modelPre);
}
