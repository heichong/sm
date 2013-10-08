package com.zongb.sm.entity;

/**
 * 记录产品图片
 * @author zongb
 *
 */
public class ProductImage {

	private Integer productImageId ;//产品图片id
	private Integer productId ;//产品id
	private String image ;//图片路径
	private Integer sortOrder ;//图片排序
	
	public Integer getProductImageId() {
		return productImageId;
	}
	public void setProductImageId(Integer productImageId) {
		this.productImageId = productImageId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Integer getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	
	
}
