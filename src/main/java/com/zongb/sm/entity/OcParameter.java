package com.zongb.sm.entity;

import java.util.HashMap;
import java.util.Map;

public class OcParameter {

	private String urlPath ;
	
	private int languageEn ;
	private int languageCn ;

	private String imageDataPath ;//opencart的image的全路径<br/>路径截止到data文件夹
	private String productTypeCode ;//您为当前商品自定义的编码
	private int productSeqStart = 1 ;//商品序列的初始值
	private int brandId ;//商品所属的品牌id<br/>参考[商品->链接->品牌]
	private int minNumDiff ;//商品描述的默认tag标签
	private float productPriceTime ;//商品价格与实际价格的倍数
	private String desItemType ;//商品描述的Item Type
	private String descTags ;//商品描述的默认tag标签
	private String [] categorys ;//所属目录id
	
	private String style ;//其他描述html
	private String braceletType ;//其他描述html
	
	private String otherHtml ;//其他描述html

	private Integer storeId = 0 ;//商店id

	private Boolean isDownloadImage = false ;//是否下载产品图片
	private Boolean onlyGetCurrentPage = false ;//是否只抽取当前页面的产品
	
	
	/**
	 * 常用计量单位名称与opencart系统中计量单位id的对应关系
	 */
	public static Map<String,Integer>  unitMap = null ;
	
	static{
		unitMap = new HashMap<String,Integer>() ;
		unitMap.put("CM",1) ;//厘米
		unitMap.put("MM",2) ;//毫米
		

		unitMap.put("G",2) ;//克
		unitMap.put("克",2) ;//克
		
		unitMap.put("KG",1) ;//千克
		unitMap.put("千克",1) ;//千克
	}
	
	
	public Boolean getOnlyGetCurrentPage() {
		return onlyGetCurrentPage;
	}
	public void setOnlyGetCurrentPage(Boolean onlyGetCurrentPage) {
		this.onlyGetCurrentPage = onlyGetCurrentPage;
	}
	public Boolean getIsDownloadImage() {
		return isDownloadImage;
	}
	public void setIsDownloadImage(Boolean isDownloadImage) {
		this.isDownloadImage = isDownloadImage;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getBraceletType() {
		return braceletType;
	}
	public void setBraceletType(String braceletType) {
		this.braceletType = braceletType;
	}
	public static Map<String, Integer> getUnitMap() {
		return unitMap;
	}
	public static void setUnitMap(Map<String, Integer> unitMap) {
		OcParameter.unitMap = unitMap;
	}
	public Integer getStoreId() {
		return storeId;
	}
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
	public String getOtherHtml() {
		return otherHtml;
	}
	public void setOtherHtml(String otherHtml) {
		this.otherHtml = otherHtml;
	}
	public String getUrlPath() {
		return urlPath;
	}
	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}
	public int getLanguageEn() {
		return languageEn;
	}
	public void setLanguageEn(int languageEn) {
		this.languageEn = languageEn;
	}
	public int getLanguageCn() {
		return languageCn;
	}
	public void setLanguageCn(int languageCn) {
		this.languageCn = languageCn;
	}
	public String getImageDataPath() {
		return imageDataPath;
	}
	public void setImageDataPath(String imageDataPath) {
		this.imageDataPath = imageDataPath;
	}
	public String getProductTypeCode() {
		return productTypeCode;
	}
	public void setProductTypeCode(String productTypeCode) {
		this.productTypeCode = productTypeCode;
	}
	public int getProductSeqStart() {
		return productSeqStart;
	}
	public void setProductSeqStart(int productSeqStart) {
		this.productSeqStart = productSeqStart;
	}
	public int getBrandId() {
		return brandId;
	}
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}
	public int getMinNumDiff() {
		return minNumDiff;
	}
	public void setMinNumDiff(int minNumDiff) {
		this.minNumDiff = minNumDiff;
	}
	public float getProductPriceTime() {
		return productPriceTime;
	}
	public void setProductPriceTime(float productPriceTime) {
		this.productPriceTime = productPriceTime;
	}
	public String getDesItemType() {
		return desItemType;
	}
	public void setDesItemType(String desItemType) {
		this.desItemType = desItemType;
	}
	public String getDescTags() {
		return descTags;
	}
	public void setDescTags(String descTags) {
		this.descTags = descTags;
	}
	public String[] getCategorys() {
		return categorys;
	}
	public void setCategorys(String[] categorys) {
		this.categorys = categorys;
	}
	
	
	
}
