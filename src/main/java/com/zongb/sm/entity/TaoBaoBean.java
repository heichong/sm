package com.zongb.sm.entity;
/**
 * 淘宝数据包中cvs所对应的实体bean
 * cvs的每一列对应一个属性
 * @author zongb
 *
 */
public class TaoBaoBean {

	public TaoBaoBean() {
		// TODO Auto-generated constructor stub
	}
	//csv文件路径
	private String csvParentPath ;
	//csv文件名
	private String csvFileName ;
	
	//宝贝名称[0]
	private String name ;
	//宝贝类目,所属类别id[1]
	private String categoryId ;
	//价格[7]
	private double price ;
	//描述html[20]
	private String descHtml ;
	//图片字符串[28]
	private String imgsStr ;
	
	
	public String getCsvParentPath() {
		return csvParentPath;
	}
	public void setCsvParentPath(String csvParentPath) {
		this.csvParentPath = csvParentPath;
	}
	/**
	 * return 20130905头饰.csv
	 * @return
	 */
	public String getCsvFileName() {
		return csvFileName;
	}

	/**
	 * return 20130905头饰
	 * @return
	 */
	public String getCsvFileNameNoSuffix(){
		return csvFileName==null?null:csvFileName.substring(0, csvFileName.lastIndexOf(".")) ;
	}
	public void setCsvFileName(String csvFileName) {
		this.csvFileName = csvFileName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getDescHtml() {
		return descHtml;
	}
	public void setDescHtml(String descHtml) {
		this.descHtml = descHtml;
	}
	public String getImgsStr() {
		return imgsStr;
	}
	public void setImgsStr(String imgsStr) {
		this.imgsStr = imgsStr;
	}
	
	
}
