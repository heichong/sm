package com.zongb.sm.entity;

/**
 * 产品目录类
 * @author zongb
 *
 */
public class ProductCategory {

	private Integer productId ;//产品id
	private Integer categoryId ;//目录id
	
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	
	
}
