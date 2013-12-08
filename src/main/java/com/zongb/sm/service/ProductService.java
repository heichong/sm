package com.zongb.sm.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

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

	/**
	 * 获取此类商品的语言id
	 * @param code 语言的code
	 */
	public String getLanguageIdByCode(String code);


	/**
	 * 获取所有的品牌信息
	 */
	public List<Map<String,Object>> getAllManufacturer();

	/**
	 * 获取所有的产品目录信息
	 */
	public List<Map<String,Object>> getAllCategory();
	
	
}
