package com.zongb.sm.entity;

/**
 * 产品基本（一般）信息
 * @author zongb
 *
 */
public class ProductDesc {

	private Integer productId ;//产品id
	private Integer languageId ;//语言id english=1
	private String name ;//产品名称
	private String description ;//产品描述
	private String metaDescription ;//SEO关键內容
	private String metaKeyword ;//SEO关键字
	private String tag ;//标签（用逗号隔开）
	
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getLanguageId() {
		return languageId;
	}
	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMetaDescription() {
		return metaDescription;
	}
	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}
	public String getMetaKeyword() {
		return metaKeyword;
	}
	public void setMetaKeyword(String metaKeyword) {
		this.metaKeyword = metaKeyword;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	
	
}
