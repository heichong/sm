package com.zongb.sm.opencart;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;

import com.zongb.sm.entity.OcParameter;
import com.zongb.sm.entity.Product;
import com.zongb.sm.entity.ProductCategory;

public abstract class AbstractHttp {
	protected int SEQ_COUNT = 1;
	protected int count = 0 ;
	protected OcParameter param ;
	
	public AbstractHttp() {
		// TODO Auto-generated constructor stub
	}

	public abstract List<Product> getPagesProductList(OcParameter params) throws Exception ;
	/**
	 * 解析当前html文档对象内的所有产品信息
	 * @param doc html文档对象
	 * @throws Exception
	 */
	public Product getProductAllInfo(Document doc,String href) throws Exception {
		//1.获取产品的主要信息
		//System.out.println("产品主要信息[start]");
		Product p = getProduct(doc) ;
		p.setOriginalUrl(href);
		//System.out.println("产品主要信息[end]");
		//2.获取产品相关描述信息
		//System.out.println("产品相关描述[start]");
		getProductDesc(doc,p) ;
		//System.out.println("产品相关描述[end]");
		
		//3.下载所有的产品图片
		//System.out.println("产品图片[start]");
		if(param.getIsDownloadImage()){
			getImages(doc,p) ;
		}
		//System.out.println("产品图片结束[end]");
		//4.获取产品目录
		p.setCategoryList(getCategorys());
		
		return p ;
	}
	


	protected abstract Product getProduct(Document doc) throws Exception ;
	protected abstract void getProductDesc(Document doc, Product p) throws Exception ;
	protected abstract void getImages(Document doc, Product p) throws Exception ;

	/**
	 * 根据页面传递的目录id参数，组成当前产品对应的目录对象列表
	 * 一个产品可以位于多个目录
	 * @param doc html文档对象
	 * @throws Exception
	 */
	protected List<ProductCategory> getCategorys() throws Exception {
		
		List<ProductCategory> categoryList = new ArrayList<ProductCategory>();
		//用户传递的目录id以逗号进行分割
		String [] categorys = param.getCategorys();
		for(int i=0,len=categorys.length ; i<len ; i++){
			if("".equals(categorys[i])){//目录id不能为空
				continue ;
			}
			ProductCategory pc = new ProductCategory() ;
			pc.setCategoryId(Integer.valueOf(categorys[i]).intValue()) ;
			categoryList.add(pc) ;
		}
		return categoryList ;
		
	}
}
