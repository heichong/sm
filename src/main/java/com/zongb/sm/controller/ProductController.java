package com.zongb.sm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zongb.sm.entity.OcParameter;
import com.zongb.sm.entity.Product;
import com.zongb.sm.opencart.AbstractHttp;
import com.zongb.sm.opencart.Helper;
import com.zongb.sm.opencart.nimi.HttpNIMI;
import com.zongb.sm.opencart.pj.HttpPJ;
import com.zongb.sm.service.ProductService;

@Controller
@RequestMapping("/product")
public class ProductController {
	@Autowired
	private ProductService productService ;
	

	@RequestMapping(value="/nimibus")
	public String nimibusInit(Model model) throws Exception {
		OcParameter oc = new OcParameter() ;
		List list = productService.getAllManufacturer(); 
		oc.setLanguageEn(Integer.valueOf(productService.getLanguageIdByCode("en")));
		oc.setLanguageCn(Integer.valueOf(productService.getLanguageIdByCode("cn")));
		model.addAttribute("param", oc) ;
		model.addAttribute("manuList", productService.getAllManufacturer()) ;//所有品牌列表
		model.addAttribute("categoryList", productService.getAllCategory()) ;//所有产品目录列表
		
		return "oc/nimibus" ;
	}
	
	
	/**
	 * 根据参数从网络中抽取产品信息
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/nimibus/save")
	@ResponseBody 
	public Map<String,Object> nimibusSaveProducts(@ModelAttribute("param") OcParameter param) throws Exception {
		
		//获取此类商品的最大编号
		String modelPre = "'" + Helper.getCurrentDateStr() + param.getProductTypeCode() + "%'";
		String maxModel = productService.getMaxModeByType(modelPre) ;
		if(maxModel==null || "".equals(maxModel)){
			param.setProductSeqStart(1);
		}else{
			maxModel = maxModel.substring(maxModel.length() - 4) ;
			param.setProductSeqStart(Integer.valueOf(maxModel) + 1);
		}

		System.out.println("=========此产品已存在的最大编号为："+param.getProductSeqStart());
		AbstractHttp http = new HttpNIMI() ;
		//开始时间
		long start = System.currentTimeMillis() ;
		List<Product>  products = http.getPagesProductList(param) ;
		
		//输出错误日志
		List<Map<String,String>> errorList =  Helper.getErrorList(products);
		
		System.out.println("----------开始插入数据库---------------");
		for(Product product : products){
			productService.saveProduct(product) ;
		}

		//结束时间
		long end = System.currentTimeMillis() ;
		//花费时间-秒
		long time = (end - start)/1000 ;
		
		Map<String,Object> map = new HashMap<String,Object>() ;
		map.put("success", true) ;
		map.put("count", products.size()) ;
		map.put("time", time) ;
		map.put("errorList", errorList) ;
		map.put("errorCount", errorList.size()) ;
				
		return map ;
	}

	@RequestMapping(value="/pj")
	public String pjInit(Model model) throws Exception {
		
		model.addAttribute("param", new OcParameter()) ;
		
		return "oc/pj" ;
	}
	
	
	/**
	 * 根据参数从网络中抽取产品信息
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/pj/save")
	@ResponseBody 
	public Map<String,Object> pjSaveProducts(@ModelAttribute("param") OcParameter param) throws Exception {
		
		//获取此类商品的最大编号
		String modelPre = "'" + Helper.getCurrentDateStr() + param.getProductTypeCode() + "%'";
		String maxModel = productService.getMaxModeByType(modelPre) ;
		if(maxModel==null || "".equals(maxModel)){
			param.setProductSeqStart(1);
		}else{
			maxModel = maxModel.substring(maxModel.length() - 4) ;
			param.setProductSeqStart(Integer.valueOf(maxModel) + 1);
		}

		System.out.println("=========此产品已存在的最大编号为："+param.getProductSeqStart()+",最大model为："+maxModel);
		AbstractHttp http = new HttpPJ() ;
		//开始时间
		long start = System.currentTimeMillis() ;
		List<Product>  products = http.getPagesProductList(param) ;
		
		//输出错误日志
		List<Map<String,String>> errorList =  Helper.getErrorList(products);
				
		System.out.println("----------开始插入数据库---------------");
		for(Product product : products){
			productService.saveProduct(product) ;
		}
		
		//结束时间
		long end = System.currentTimeMillis() ;
		//花费时间-秒
		long time = (end - start)/1000 ;
		
		Map<String,Object> map = new HashMap<String,Object>() ;
		map.put("success", true) ;
		map.put("count", products.size()) ;
		map.put("time", time) ;
		map.put("errorList", errorList) ;
		map.put("errorCount", errorList.size()) ;
				
		return map ;
	}
}
