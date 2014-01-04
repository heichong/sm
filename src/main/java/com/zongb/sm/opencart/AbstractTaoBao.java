package com.zongb.sm.opencart;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;

import com.zongb.sm.entity.Desc;
import com.zongb.sm.entity.OcParameter;
import com.zongb.sm.entity.Product;
import com.zongb.sm.entity.ProductCategory;
import com.zongb.sm.entity.ProductDesc;
import com.zongb.sm.entity.ProductImage;
import com.zongb.sm.entity.TaoBaoBean;
import com.zongb.sm.support.utils.CsvBeanConveter;
import com.zongb.sm.support.utils.CsvUtil;

/**
 * 处理淘宝数据包
 * @author zongb
 *
 */
public abstract class AbstractTaoBao {

	protected Logger log = Logger.getLogger(AbstractTaoBao.class) ;
	
	protected int SEQ_COUNT = 1;
	/**
	 * 淘宝数据文件的文件名后缀
	 */
	public static final String TAOBAO_CSV_FILE_SUFFIX = "CSV" ;
	//参数
	private OcParameter params ;
	//当前正在处理的产品对象
	protected Product product ; 
	
	public AbstractTaoBao(OcParameter params) {
		// TODO Auto-generated constructor stub
		log.debug("---------开始初始化AbstractTaoBao-------------");
		this.params = params ;
		if(!params.getUrlPath().endsWith(File.separator)){
			params.setUrlPath(params.getUrlPath() + File.separator);
		}
		this.SEQ_COUNT = this.params.getProductSeqStart() ;
		log.debug("---------初始化AbstractTaoBao结束-------------");
	}

	/**
	 * 获取指定参数的文件夹下所有的产品信息
	 * @return
	 * @throws Exception
	 */
	public  List<Product> getProductList() throws Exception {
		log.debug("---------开始搜索产品列表-------------");
		//自定义文件过滤器,只处理特殊的文件
		FilenameFilter  fileFilter = new FilenameFilter(){
			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				return name.toUpperCase().endsWith(TAOBAO_CSV_FILE_SUFFIX);
			}
			
		} ;
		
		List<Product> products = new ArrayList<Product>() ;
		
		File floder = new File(params.getUrlPath());  
		//列出所有符合过滤条件的文件
		File[] files = floder.listFiles(fileFilter);  
		//循环处理所有文件
		for(File cvs : files){
			log.debug("---------产品文件：["+cvs.getName()+"]开始处理-------------");
			products.addAll(getProductListForSingleCVS(cvs)) ;
			log.debug("---------产品文件：["+cvs.getName()+"]处理结束-------------");
		}
		
		return products ;
	}    

	public  List<Product> getProductListForSingleCVS(File cvs) throws Exception {
		//当前处理的文件名
		final String fileName = cvs.getName() ;
		log.debug("---------开始读取产品文件的产品信息，并转化成TaoBaoBean列表-------------");
		//读取整个csv文件获取文件信息
		List<TaoBaoBean> csvList = CsvUtil.getBeanList(cvs.getAbsolutePath(), CsvUtil.CSV_SPLIT_COMMA, false, new CsvBeanConveter<TaoBaoBean>(){
			int i = 0 ;
			/**
			 * 行转化成product
			 */
			@Override
			public TaoBaoBean rowToBean(String[] records){
				//i++ < 3  前三行不处理
				if (records == null || records.length == 0 || i++ < 3) {  
					return null;  
				}
				TaoBaoBean bean = new TaoBaoBean() ;
				bean.setCsvParentPath(params.getUrlPath());
				bean.setCsvFileName(fileName);
				
				bean.setName(records[0]);//名称
				bean.setCategoryId(records[1]);//品类
				bean.setPrice(NumberUtils.isNumber(records[7])?Double.valueOf(records[7]).doubleValue():0);//价格
				bean.setDescHtml(records[20]);//商品描述
				bean.setImgsStr(records[28]);//图片字符串
				
				return bean ;
			}
		}) ;
		log.debug("---------产品信息读取完成，共抽取有效产品【"+csvList.size()+"】个-------------");
		log.debug("---------开始解析产品信息-------------");
		//解析tabaobean 到product对象
		List<Product> pList = new ArrayList<Product>() ;
		for(TaoBaoBean bean : csvList){
			log.debug("-------------------正在解析产品："+bean.getName()+"-------------");
			pList.add(parseProduct(bean)) ;
			log.debug("-------------------完成解析产品："+bean.getName()+"-------------");
		}
		log.debug("---------产品信息解析完成，共解析产品【"+pList.size()+"】个--------------");
		
		return pList ;
	}
	
	/**
	 * 解析淘宝bean到product
	 * @param bean
	 * @return
	 */
	private Product parseProduct(TaoBaoBean bean){
		product = new Product() ;
		//组成产品描述字符串
		Desc d = new Desc() ;
		
		String modelCode = Helper.getCurrentDateStr()
				+params.getProductTypeCode()
				+String.format("%04d", SEQ_COUNT++) ;
		//商店id
		product.setStoreId(params.getStoreId());
		//产品型号
		product.setModel(modelCode);
		product.setSku("");//编码
		product.setUpc("");//UPC条码
		product.setEan("");// EAN码
		product.setJan("");//JAN码
		product.setIsbn("");//ISBN码
		product.setMpn("");//MPN码
		product.setLocation("");//组装地
		
		product.setQuantity(99999);//数量
		product.setStockStatusId(7);//库存状况编码 （5 - Out Of Stock|6 - 2 - 3 Days|7 - In Stock|8 - Pre-Order)
		//product.setImage("data/2013-09/.jpg");//此图片放在添加图片列表到产品的动作中
		product.setManufacturerId(params.getBrandId());//品牌的id
		product.setShipping(1);//需要配送 1配送；0-不配送
		//价格，单位美元
		product.setPrice(Helper.getNewPrice(bean.getPrice()));

		product.setPoints(0);//本产品回馈红利点数
		product.setTaxClassId(0);//税率种类0-无；9-Taxable Goods；10-Downloadable Products
		product.setDateAvailable(new Date());//上架日期
		
		//重量
		product.setWeight(getWeight(bean))  ;

		//长度
		product.setLength(getLength(bean))  ;

		//宽度
		product.setWidth(getWidth(bean)) ;

		//高度
		product.setHeight(getHeight(bean)) ;

		//直径
		product.setDiameter(getDiameter(bean));
		
		product.setWeightClassId(params.unitMap.get("克"));// 重量单位id--参考系统管理-》本地化
		product.setLengthClassId(params.unitMap.get("CM"));//长度单位id--参考系统管理-》本地化

		product.setSubtract(0);//减去库存？ 0-否；1-是

		product.setMinimum(params.getMinNumDiff()<1?1:params.getMinNumDiff());//起订量
		product.setSortOrder(SEQ_COUNT);//排序号
		product.setStatus(1);//0-停用 ；1-启用
		product.setDateAdded(new Date());//添加时间
		product.setDateModified(new Date());//修改时间
		product.setViewed(0);//点击量
		product.setOriginalUrl(getProductUrl(bean));//查询当前产品的url
		
		//[修改]--组织描述信息
		d.setOtherHtml(params.getOtherHtml());
		d.setItemType(params.getDesItemType());
		d.setModelNumber(modelCode);

		d.setStyle(params.getStyle());
		d.setBraceletType(params.getBraceletType());
		//材质
		d.setMainMaterial(getMainMaterial(bean));
		//长宽高直径全部整合到size中
		d.setSize(product);
		product.setDesc(d);//设置产品的描述类
		//下载图片
		if(params.getIsDownloadImage()){
			log.debug("------------------------开始解析产品图片-------------");
			product.setImageList(getImageList(bean));
			log.debug("------------------------完成解析产品图片-------------");
		}
		//获取产品的中英文描述
		product.setDescList(getProductDescList(bean));
		
		//产品所属的品类
		product.setCategoryList(getCategorys());
		return product ;
	}
	/**
	 * 获取当前产品的列表，同时copy图片到指定的路径下
	 * @param bean
	 * @return
	 */
	private List<ProductImage> getImageList(TaoBaoBean bean){
		List<ProductImage> imageList = new ArrayList<ProductImage>() ;
		String imgStr = bean.getImgsStr() ;
		if(imgStr==null || "".equals(imgStr)){
			product.addError("当前产品没有解析到图片！");
			return imageList ;
		}
		int i = 0 ;
		String [] imgs = imgStr.split("\\|") ;
		for(String img : imgs){//解析产品的图片字符串

			log.debug("---------------------------img="+img+"-------------");
			if(img==null || "".equals(img)){
				continue ;
			}
			//可能包含两个图片字符串
			String [] im = img.split(";") ;
			if(im==null || im.length == 0){
				continue ;
			}
			String path = getImageAbsolutelyFullPath() ;
			String name0 = getImgName(im[0]) ;
			//通过网络下载图片
			if(im[0].toUpperCase().startsWith("HTTP")){//http开头的图片会与文件夹下的图片重复，所以不做处理
//				try{
//					//下载图片
//					HttpGetImage.getImages(im[0], path+name0);
//
//					//保存图片信息
//					ProductImage image = new ProductImage() ;
//					image.setImage("data/" + Helper.getCurrentYear() + "/" +Helper.getCurrentMonth() + "/" + name0);
//					image.setSortOrder(i++);
//					imageList.add(image) ;
//				}catch(Exception e){
//					product.addError("当前图片下载失败.["+im[0]+"]");
//				}
				
			}else if(!"".equals(im[0])){//拷贝文件
				File srcFile = new File(bean.getCsvParentPath() + bean.getCsvFileNameNoSuffix(),Helper.getMatcher("(\\w+):", im[0], 1) + ".tbi") ;
				if(srcFile.exists()){
					try {
						FileUtils.copyFile(srcFile, new File(path+name0));
						//保存图片信息
						ProductImage image = new ProductImage() ;
						image.setImage("data/" + Helper.getCurrentYearMonth() + "/" + name0);
						image.setSortOrder(i++);
						imageList.add(image) ;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						product.addError("拷贝当前图片失败.src=["+srcFile.getAbsolutePath() + srcFile.getName()+"].dest=["+path+name0+"]");
					}
				}
				
			}
			
						
			if(im.length > 1 && null != im[1] && !"".equals(im[1])){
				String name1 = getImgName(im[1]) ;
				File srcFile = new File(bean.getCsvParentPath() + bean.getCsvFileNameNoSuffix(),Helper.getMatcher("(\\w+):", im[1], 1) + ".tbi") ;
				if(srcFile.exists()){
					try {
						FileUtils.copyFile(srcFile, new File(path+name1));
						//保存图片信息
						ProductImage image = new ProductImage() ;
						image.setImage("data/" + Helper.getCurrentYearMonth() + "/" + name1);
						image.setSortOrder(i++);
						imageList.add(image) ;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						product.addError("拷贝当前图片失败.src=["+srcFile.getAbsolutePath() + srcFile.getName()+"].dest=["+path+name1+"]");
					}
				}
			}
			
		}
		
		return imageList ;
	}
	
	/**
	 * 获取图片文件的绝对路径c:/path/
	 * @return
	 */
	private String getImageAbsolutelyFullPath(){
		
		//图片的顶级文件路径
		String pdir = params.getImageDataPath() ;
		
		if(!pdir.endsWith("\\") && !pdir.endsWith("/")){
			pdir += "/" ;
		}
		//图片的路径
		String path = pdir + Helper.getCurrentYearMonth() + "/"  ;

		File dir = new File(path) ;
		if(!dir.exists()){
			dir.mkdirs() ;
		}
		
		return path  ;
		
	}
	/**
	 * 获取图片的新文件名，如果是http的文件，则使用图片的原后缀名，否则使用.jpg
	 * @param img
	 * @return
	 */
	private String getImgName(String img){
		//图片的后缀名
		String prefix = ".jpg" ;
//		if(img.toUpperCase().startsWith("HTTP")){
//			prefix = img.substring(img.lastIndexOf(".")) ;
//		}
		return product.getModel() + "_" + System.currentTimeMillis() + prefix ;
	}
	

	/**
	 * 根据页面传递的目录id参数，组成当前产品对应的目录对象列表
	 * 一个产品可以位于多个目录
	 * @param doc html文档对象
	 * @throws Exception
	 */
	protected List<ProductCategory> getCategorys()  {
		
		List<ProductCategory> categoryList = new ArrayList<ProductCategory>();
		//用户传递的目录id以逗号进行分割
		String [] categorys = params.getCategorys();
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
	

	/**
	 * 获取产品的中英文描述
	 * @param doc html文档对象
	 * @throws Exception
	 */
	protected List<ProductDesc> getProductDescList(TaoBaoBean bean)  {
		
		List<ProductDesc> descList =new ArrayList<ProductDesc>();
		//-------------------英文描述--------START------------------------------
		ProductDesc desc = new ProductDesc() ;
		//产品名称
		desc.setName(bean.getName());
		//产品描述
		desc.setDescription(product.getDesc().toString());
		desc.setMetaDescription("");
		desc.setMetaKeyword("");
		desc.setTag(params.getDescTags());
		desc.setLanguageId(params.getLanguageEn());
		//-------------------英文描述--------END------------------------------
		
		descList.add(desc) ;
		//-------------------中文描述--------START------------------------------
		ProductDesc descCn = new ProductDesc() ;
		descCn.setName(desc.getName());
		descCn.setDescription(product.getOriginalUrl() + desc.getDescription());
		descCn.setMetaDescription(desc.getMetaDescription());
		descCn.setMetaKeyword(desc.getMetaKeyword());
		descCn.setTag(desc.getTag());
		descCn.setLanguageId(params.getLanguageCn());
		descList.add(descCn) ;
		//-------------------中文描述--------START------------------------------
		
		product.setDescList(descList);
		return descList ;
		
	}

	/**
	 * 获取产品的直径
	 * @param product
	 * @return
	 */
	protected abstract Double getDiameter(TaoBaoBean bean) ;

	/**
	 * 获取产品的长度
	 * @param bean
	 * @return
	 */
	protected abstract Double getLength(TaoBaoBean bean)  ;

	/**
	 * 获取产品的宽度
	 * @param bean
	 * @return
	 */
	protected abstract Double getWidth(TaoBaoBean bean)  ;

	/**
	 * 获取产品的高度
	 * @param bean
	 * @return
	 */
	protected abstract Double getHeight(TaoBaoBean bean)  ;
	
	
	
	/**
	 * 获取产品的重量
	 * @param bean
	 * @return
	 */
	protected abstract Double getWeight(TaoBaoBean bean) ;

	/**
	 * 获取产品的主要材料
	 * @param bean
	 * @return
	 */
	protected abstract String getMainMaterial(TaoBaoBean bean)  ;

	/**
	 * 获取产品的url
	 * @param product
	 * @return
	 */
	protected abstract String getProductUrl(TaoBaoBean bean)  ;
	
}
