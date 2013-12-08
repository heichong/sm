package com.zongb.sm.service.imp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zongb.sm.dao.ProductMapper;
import com.zongb.sm.entity.Product;
import com.zongb.sm.entity.ProductCategory;
import com.zongb.sm.entity.ProductDesc;
import com.zongb.sm.entity.ProductImage;
import com.zongb.sm.service.ProductService;


@Service("productService")
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductMapper productMapper ;
	
	/**
	 * 添加用户
	 * @param user
	 */
	@Transactional(readOnly = false,propagation=Propagation.REQUIRED,  rollbackFor = Exception.class)  
	public void saveProduct(Product product){

		int i = productMapper.saveProduct(product) ;
		int z = productMapper.saveProductStore(product) ;
		System.out.println("插入产品主信息成功！");
		
		List<ProductDesc> descList = product.getDescList() ;
		for(ProductDesc desc: descList){
			desc.setProductId(product.getProductId());
			int j = productMapper.saveProductDesc(desc) ;
			
		}
		System.out.println("插入产品描述信息成功！");
		
		List<ProductImage> imageList = product.getImageList() ;
		if(imageList != null){
			for(ProductImage image: imageList){
				image.setProductId(product.getProductId());
				int j = productMapper.saveProductImage(image) ;
				
			}
			System.out.println("插入产品图片成功！");
		}
		
		List<ProductCategory> categoryList = product.getCategoryList() ;
		for(ProductCategory category: categoryList){
			category.setProductId(product.getProductId());
			int j = productMapper.saveProductCategory(category) ;
			
		}
		System.out.println("插入产品目录成功！");
		
	}

	/**
	 * 获取此类商品的最大编号modeid
	 * @param modelPre 商品编号的前缀 【日期+商品类型编码】
	 */
	@Transactional(readOnly = true)  
	public String getMaxModeByType(String modelPre){
		return productMapper.getMaxModeByType(modelPre) ;
	}

	/**
	 * 获取此类商品的语言id
	 */
	@Transactional(readOnly = true)  
	public String getLanguageIdByCode(String code){
		return productMapper.getLanguageIdByCode(code) ;
	}
	/**
	 * 获取所有的品牌信息
	 */
	@Transactional(readOnly = true)  
	public List<Map<String,Object>> getAllManufacturer(){
		return productMapper.getAllManufacturer() ;
	}
	/**
	 * 获取所有的产品目录信息
	 */
	@Transactional(readOnly = true)  
	public List<Map<String,Object>> getAllCategory(){
		return productMapper.getAllCategory() ;
	}
}
