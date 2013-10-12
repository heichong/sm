package com.zongb.sm.opencart.pj;


import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zongb.sm.entity.Desc;
import com.zongb.sm.entity.OcParameter;
import com.zongb.sm.entity.Product;
import com.zongb.sm.entity.ProductCategory;
import com.zongb.sm.entity.ProductDesc;
import com.zongb.sm.entity.ProductImage;
import com.zongb.sm.opencart.Helper;
import com.zongb.sm.opencart.HttpGetImage;

public class HttpPJ {
	private int SEQ_COUNT = 1;
	private int count = 0 ;
	
	private OcParameter param ;
	
	/**
	 * 根据某一个分页页面的url，循环其后面的所有分页
	 * @param urlPath
	 * @return
	 * @throws Exception
	 */
	public List<Product> getPagesProductList(OcParameter params) throws Exception{
		this.param = params ;
		this.SEQ_COUNT = this.param.getProductSeqStart() ;
		List<Product> pageProductList = new ArrayList<Product>() ;
		//从当前页开始循环
		String currentPageUrl = param.getUrlPath() ;
		Document doc = Jsoup.connect(param.getUrlPath()).timeout(1000000).get();
		int i = 0 ;
		do{
			i++ ;
			//获取当前页的doc结构
			doc = Jsoup.connect(currentPageUrl).timeout(1000000).get();
			//处理当前页中的所有产品
			pageProductList.addAll(getCurrentPageProductList(doc)) ;
			//获取下一个链接
			currentPageUrl = getNextPage(doc) ;
		}while(currentPageUrl != null) ;
		
		System.err.println("本次共处理："+i+"页，包含产品："+pageProductList.size()+"个"); 
		return pageProductList ;
	}
	/**
	 * 判断当前页面是否有下一个[修改]
	 * @param url
	 * @return
	 */
	protected String getNextPage(Document doc){
		if(param.getOnlyGetCurrentPage()){//只抽取当前页面
			return null ;
		}
		//获取当前位置
		Element current = doc.select("#pagediv .pagelist b").first() ;
		if(current == null ){
			return null ;
		}
		//获取当前页的下一个页的链接对象
		Element next = current.nextElementSibling() ;
		//如果下一个链接对象为null或者不是http链接，那说明没有下一个
		String nextHref = null ;
		if(next != null) {
			if("下一页".equals(next.text())){//如果下一个页面元素的内容为：下一页，不处理
				return null ;
			}
			nextHref = next.absUrl("href") ;//获取下一页链接
			
		}
		
		return nextHref ;
	}

	/**
	 * 循环当前页面中的所有产品，并获取对应的product对象列表
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public List<Product> getCurrentPageProductList(Document doc) throws Exception{
		List<Product> pageProductList = new ArrayList<Product>() ;
		
		//查找当前页面中所有的产品url
		Elements links = doc.select(".prolist_pic .pic a") ;
		//循环所有产品页面
		for(int i=0 ,len = links.size() ; i<len ; i++){
			String href = links.get(i).absUrl("href") ;
			if("".equals(href)){
				continue ;
			}
			System.out.println("=====================================================================");
			System.out.println("正在处理的产品序号： " + (++count));
			System.out.println("链接： " + href);
			Document productDoc = Jsoup.connect(href).timeout(1000000).get();
			Product p = getProductAllInfo(productDoc,href) ;
			pageProductList.add(p) ;
		}
		
		return pageProductList ;
	}
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
	
	/**
	 * 解析当前html文档对象,获取产品的主要信息
	 * @param doc html文档对象
	 * @throws Exception
	 */
	private Product getProduct(Document doc) throws Exception {
		
		Product product = new Product() ;
		//组成产品描述字符串
		Desc d = new Desc() ;
		
		String modelCode = Helper.getCurrentDateStr()
				+param.getProductTypeCode()
				+String.format("%04d", SEQ_COUNT++) ;
		//商店id
		product.setStoreId(param.getStoreId());
		//产品型号
		product.setModel(modelCode);
		//product.setSku("");//编码
		product.setUpc("");//UPC条码
		product.setEan("");// EAN码
		product.setJan("");//JAN码
		product.setIsbn("");//ISBN码
		product.setMpn("");//MPN码
		product.setLocation("");//组装地
		
		product.setQuantity(99999);//数量
		product.setStockStatusId(7);//库存状况编码 （5 - Out Of Stock|6 - 2 - 3 Days|7 - In Stock|8 - Pre-Order)
		//product.setImage("data/2013-09/.jpg");//此图片放在添加图片列表到产品的动作中
		product.setManufacturerId(param.getBrandId());//品牌的id
		product.setShipping(1);//需要配送 1配送；0-不配送
		

		//-------------------1.价格--------START------------------------------
		Element e = doc.select("#selectm font[color=#D03420]").first() ;
		//原价
		double oPrice = Double.valueOf(Helper.getMatcher("(\\d+([.]\\d+)?)", e.text(),1)).doubleValue() ;
		//本网站显示的价格=原价*倍数/6
		double nPrice = new BigDecimal(param.getProductPriceTime() * oPrice/6).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ;
		
		//System.out.println("oprice=["+ oPrice + "]    nprice=["+ nPrice + "]    ");
		product.setPrice(nPrice);//价格
		product.setSku(Helper.getMatcher("/(.)+", e.text(),0));//编码--当前实际存储的是价格的单位
		//-------------------1.价格--------END------------------------------
		
		product.setPoints(0);//本产品回馈红利点数
		product.setTaxClassId(0);//税率种类0-无；9-Taxable Goods；10-Downloadable Products
		product.setDateAvailable(new Date());//上架日期
		
		//-------------------2.重量、长度、宽度、高度、直径--------START------------------------------
		
		double weight = 20d ;
		double length = 0d ;
		double width = 0d ;
		double height = 0d ;
		double diameter = 0d ;
		
		//重量
		product.setWeight(weight)  ;

		//长度
		product.setLength(length)  ;

		//宽度
		product.setWidth(width) ;

		//高度
		product.setHeight(height) ;

		//直径
		
		//[修改]
		product.setWeightClassId(param.unitMap.get("克"));// 重量单位id--参考系统管理-》本地化
		product.setLengthClassId(param.unitMap.get("CM"));//长度单位id--参考系统管理-》本地化
		
		
		//[修改]--组织描述信息
		d.setOtherHtml(param.getOtherHtml());
		d.setItemType(param.getDesItemType());
		d.setModelNumber(modelCode);
		//长度与直径在描述中不显示
		d.setLength(null);
		d.setDiameter(null);
		
		e = doc.select("#selectm hr").first().nextElementSibling() ;
		String size = e.text() ;
		if("".equals(size)){//如果hr后面的第一个元素内容为空，则跳过此元素，继续后面的元素
			e = e.nextElementSibling() ;
			size = e.text() ;
		}
		if(size.indexOf("尺寸") <= 0){
			size = e.nextElementSibling().text() ;
		}
		size = Helper.getMatcher("尺寸：((.)+cm)", e.text(),1) ;
		d.setSize(size);
		d.setStyle(param.getStyle());
		d.setBraceletType(param.getBraceletType());
		
		//材质
		d.setMainMaterial(null);
		//-------------------2.重量、长度、宽度、高度、直径--------END------------------------------
		
		
		product.setSubtract(0);//减去库存？ 0-否；1-是
		
		int minNum = 0 ;//最低起订量=页面设置的起订量
		
		product.setMinimum(minNum +param.getMinNumDiff());//起订量
		product.setSortOrder(SEQ_COUNT);//排序号
		product.setStatus(1);//0-停用 ；1-启用
		product.setDateAdded(new Date());//添加时间
		product.setDateModified(new Date());//点击量
		product.setViewed(0);//点击量
		
		//临时保存组成的描述对象
		product.setDesc(d);
		return product ;

	}


	/**
	 * 解析当前html文档对象,获取产品的描述信息
	 * @param doc html文档对象
	 * @throws Exception
	 */
	private void getProductDesc(Document doc,Product product) throws Exception {
		//-------------------英文描述--------START------------------------------
		ProductDesc desc = new ProductDesc() ;

		//产品名称
		Element e = doc.select("#selectm hr").first().nextElementSibling() ;
		String name = e.text() ;
		if("".equals(name)){//如果hr后面的第一个元素内容为空，则跳过此元素，继续后面的元素
			e = e.nextElementSibling() ;
			name = e.text() ;
		}
		if(name.indexOf("尺寸") <= 0){
			name += e.nextElementSibling().text() ;
		}

		desc.setName(name);
		
		//产品描述
		desc.setDescription(product.getDesc().toString());
		
		desc.setMetaDescription("");
		desc.setMetaKeyword("");
		desc.setTag(param.getDescTags());
		
		desc.setLanguageId(param.getLanguageEn());
		//-------------------英文描述--------END------------------------------
		
		List<ProductDesc> descList =new ArrayList<ProductDesc>();
		descList.add(desc) ;
		//-------------------中文描述--------START------------------------------
		ProductDesc descCn = new ProductDesc() ;
		descCn.setLanguageId(param.getLanguageCn());
		descCn.setName(desc.getName());
		descCn.setDescription(product.getOriginalUrl() + desc.getDescription());
		descCn.setMetaDescription(desc.getMetaDescription());
		descCn.setMetaKeyword(desc.getMetaKeyword());
		descCn.setTag(desc.getTag());
		descList.add(descCn) ;
		//-------------------中文描述--------START------------------------------
		
		product.setDescList(descList);

	}

	/**
	 * 解析当前html文档对象内的产品图片并下载到指定目录
	 * @param doc html文档对象
	 * @throws Exception
	 */
	private void getImages(Document doc,Product product) throws Exception {
		//查找到页面中所有产品的缩略图的url
		Elements simgLinks = doc.select(".pro_sm").first().previousElementSibling().select("img");
		if(null == simgLinks){
			product.addError("未找到产品的图片");
		}
		List<ProductImage> imageList = new ArrayList<ProductImage>();
		int i = 0 ;
		for (Element simg : simgLinks) {
			
			String url = simg.attr("abs:src") ;
			
			//图片的后缀名
			String prefix = url.substring(url.lastIndexOf(".")) ;
			//图片的新名称
			String name = product.getModel() + "_" + System.currentTimeMillis() + prefix ;
			
			String pdir = param.getImageDataPath() ;
			if(!pdir.endsWith("\\") && !pdir.endsWith("/")){
				pdir += "/" ;
			}
			
			String path = pdir + param.getImageDirName() + "/" ;
			File dir = new File(path) ;
			if(!dir.exists()){
				dir.mkdirs() ;
			}
			try{
				//下载图片
				HttpGetImage.getImages(url, path + name);
			}catch(Exception e){
				product.addError("当前图片下载失败.["+url+"]");
			}
			
			//保存图片信息
			ProductImage image = new ProductImage() ;
			image.setImage("data/" + param.getImageDirName() + "/" + name);
			image.setSortOrder(i++);
			imageList.add(image) ;
		}
		System.out.println("当前产品的图片数量："+i);
		
		product.setImageList(imageList);

	}


	/**
	 * 根据页面传递的目录id参数，组成当前产品对应的目录对象列表
	 * 一个产品可以位于多个目录
	 * @param doc html文档对象
	 * @throws Exception
	 */
	private List<ProductCategory> getCategorys() throws Exception {
		
		List<ProductCategory> categoryList = new ArrayList<ProductCategory>();
		//用户传递的目录id以逗号进行分割
		String [] categorys = param.getCategorys().split(",") ;
		for(int i=0,len=categorys.length ; i<len ; i++){
			if("".equals(categorys[i])){//目录id不能为空
				continue ;
			}
			int categoryId = Integer.valueOf(categorys[i]).intValue() ;
			ProductCategory pc = new ProductCategory() ;
			pc.setCategoryId(categoryId) ;
			categoryList.add(pc) ;
		}
		return categoryList ;
		
	}

	public static void main(String[] args) {
		try {
			
			String urlPath = "http://www.pjspw.com/product-48174.html" ;
			//获取html文档结构
			Document doc = Jsoup.connect(urlPath).timeout(1000000).get();
			Element div = doc.select("#selectm hr").first().nextElementSibling() ;
			
			System.out.println(Helper.getMatcher("尺寸：((.)+cm)", div.nextElementSibling().text(),1));
			System.out.println();
			//HttpPJ http = new HttpPJ() ;
			
			//System.out.println(http.getNextPage(doc));
			//Product product = getProductAllInfo(doc) ;
			
			// List<Product>  products = getPagesProductList(urlPath) ;
			
			/**
			String html = "<div>"
					+ "<span style=\"color: #800000; font-size: 12pt;\">"
					+ "<strong>【产品规格】</strong><a href=""></>"
					+ "	直径7*长19*宽1.8CM&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
					+ "<strong>【产品重量】</strong>40.123克 12条一打60 240一打 4打一公斤</span></div>";
			Document doc = Jsoup.parse(html);//解析HTML字符串返回一个Document实现
			
			
			Element link = doc.select(":matchesOwn(【产品规格】)").first();//查找第一个a元素

			System.out.println(link.nextElementSibling().text()); 
			System.out.println(link.nextSibling().outerHtml()); 
			System.out.println(link.nextSibling().toString()); 
			**/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
