package com.zongb.sm.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 产品主要信息类
 * @author zongb
 *
 */
public class Product {

	private Integer storeId ;//商店id
	
	private Integer productId ;//产品id
	
	private String model ;//产品型号
	private String sku ;//编码
	private String upc ;//UPC条码
	private String ean ;// EAN码
	private String jan ;//JAN码
	private String isbn ;//ISBN码
	private String mpn ;//MPN码
	private String location ;//组装地

	private Integer quantity ;//数量
	private Integer stockStatusId ;//库存状况编码 （5 - Out Of Stock|6 - 2 - 3 Days|7 - In Stock|8 - Pre-Order)
	private String image ;//产品的主图片,如data/demo/canon_eos_5d_3.jpg
	private Integer manufacturerId ;//品牌的id
	private Integer shipping ;//需要配送 1配送；0-不配送
	private Double price ;//价格
	private Integer points ;//本产品回馈红利点数
	private Integer taxClassId ;//税率种类0-无；9-Taxable Goods；10-Downloadable Products
	private Date dateAvailable ;//上架日期
	private Double weight ;//重量
	private Integer weightClassId ;//重量单位id--参考系统管理-》本地化
	private Double length ;//长度
	private Double width ;//宽度
	private Double height ;//高度
	private Integer lengthClassId ;//长度单位id--参考系统管理-》本地化
	private Integer subtract ;//减去库存？ 0-否；1-是
	private Integer minimum ;//起订量
	private Integer sortOrder ;//排序号
	private Integer status ;//0-停用 ；1-启用
	private Date dateAdded ;//添加时间
	private Date dateModified ;//修改时间
	private Integer viewed ;//点击量
	
	private String originalUrl ;//原始url
	
	//--------属性类--------------------
	private List<ProductDesc> descList ;
	private List<ProductImage> imageList ;
	private List<ProductCategory> categoryList ;

	//--------临时--------------------
	private Desc desc ;
	private List<String> errList ;//错误列表
	
	/**
	 * 是否有错误
	 * @return
	 */
	public boolean hasError(){
		return (errList != null) && (errList.size() > 0) ;
	}
	/**
	 * 添加错误消息
	 * @param error
	 */
	public void addError(String error){
		if(null == errList){
			errList = new ArrayList<String>() ;
		}
		errList.add(error) ;
	}
	
	
	public String getOriginalUrl() {
		return originalUrl;
	}
	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}
	public List<String> getErrList() {
		return errList;
	}
	public Desc getDesc() {
		return desc;
	}
	public void setDesc(Desc desc) {
		this.desc = desc;
	}
	
	public List<ProductCategory> getCategoryList() {
		return categoryList;
	}
	public void setCategoryList(List<ProductCategory> categoryList) {
		this.categoryList = categoryList;
	}
	public List<ProductImage> getImageList() {
		return imageList;
	}
	public void setImageList(List<ProductImage> imageList) {
		this.imageList = imageList;
		this.image = imageList.get(0).getImage() ;
	}
	public List<ProductDesc> getDescList() {
		return descList;
	}
	public void setDescList(List<ProductDesc> descList) {
		this.descList = descList;
	}
	
	
	public Integer getStoreId() {
		return storeId;
	}
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getUpc() {
		return upc;
	}
	public void setUpc(String upc) {
		this.upc = upc;
	}
	public String getEan() {
		return ean;
	}
	public void setEan(String ean) {
		this.ean = ean;
	}
	public String getJan() {
		return jan;
	}
	public void setJan(String jan) {
		this.jan = jan;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getMpn() {
		return mpn;
	}
	public void setMpn(String mpn) {
		this.mpn = mpn;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getStockStatusId() {
		return stockStatusId;
	}
	public void setStockStatusId(Integer stockStatusId) {
		this.stockStatusId = stockStatusId;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Integer getManufacturerId() {
		return manufacturerId;
	}
	public void setManufacturerId(Integer manufacturerId) {
		this.manufacturerId = manufacturerId;
	}
	public Integer getShipping() {
		return shipping;
	}
	public void setShipping(Integer shipping) {
		this.shipping = shipping;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getPoints() {
		return points;
	}
	public void setPoints(Integer points) {
		this.points = points;
	}
	public Integer getTaxClassId() {
		return taxClassId;
	}
	public void setTaxClassId(Integer taxClassId) {
		this.taxClassId = taxClassId;
	}
	public Date getDateAvailable() {
		return dateAvailable;
	}
	public void setDateAvailable(Date dateAvailable) {
		this.dateAvailable = dateAvailable;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Integer getWeightClassId() {
		return weightClassId;
	}
	public void setWeightClassId(Integer weightClassId) {
		this.weightClassId = weightClassId;
	}
	public Double getLength() {
		return length;
	}
	public void setLength(Double length) {
		this.length = length;
	}
	public Double getWidth() {
		return width;
	}
	public void setWidth(Double width) {
		this.width = width;
	}
	public Double getHeight() {
		return height;
	}
	public void setHeight(Double height) {
		this.height = height;
	}
	public Integer getLengthClassId() {
		return lengthClassId;
	}
	public void setLengthClassId(Integer lengthClassId) {
		this.lengthClassId = lengthClassId;
	}
	public Integer getSubtract() {
		return subtract;
	}
	public void setSubtract(Integer subtract) {
		this.subtract = subtract;
	}
	public Integer getMinimum() {
		return minimum;
	}
	public void setMinimum(Integer minimum) {
		this.minimum = minimum;
	}
	public Integer getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
	public Date getDateModified() {
		return dateModified;
	}
	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}
	public Integer getViewed() {
		return viewed;
	}
	public void setViewed(Integer viewed) {
		this.viewed = viewed;
	}
	
	
	
}
