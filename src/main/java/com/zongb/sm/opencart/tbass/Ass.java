package com.zongb.sm.opencart.tbass;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zongb.sm.entity.OcParameter;
import com.zongb.sm.entity.TaoBaoBean;
import com.zongb.sm.opencart.AbstractTaoBao;
import com.zongb.sm.opencart.Helper;
/**
 * 淘宝数据包解析类：爱时尚www.ass007.com
 * @author zongb
 *
 */
public class Ass extends AbstractTaoBao {

	public Ass(OcParameter params) {
		super(params);
		// TODO Auto-generated constructor stub
	}


	@Override
	protected Double getDiameter(TaoBaoBean bean) {
		Double diameter = null ;
		Element e = getDocment(bean).select("tr:eq(2) td:eq(1)").first();
		if(e != null){
			String str = StringUtils.trim(e.text()) ;
			String d = "" ;
			if(!StringUtils.isBlank(str)){
				str = str.replaceAll("\\s", "") ;//去除空格
				//直径约：1.85 cm
				d = Helper.getMatcher("[直|内]径(约)?(：)?(\\d+([.]\\d+)?)cm", str, 3) ;
				
			}
			
			diameter = "".equals(d)?null:NumberUtils.createDouble(d) ;
		}
		
		return diameter;
	}


	@Override
	protected Double getLength(TaoBaoBean bean) {
		Double length = null ;
		Element e = getDocment(bean).select("tr:eq(2) td:eq(1)").first();
		if(e != null){
			String str = StringUtils.trim(e.text()) ;
			String l = "" ;
			if(!StringUtils.isBlank(str)){
				str = str.replaceAll("\\s", "") ;//去除空格
				//长：9 cm             宽：7 cm
				l = Helper.getMatcher("长(约)?(：)?(\\d+([.]\\d+)?)cm", str, 3) ;
				if(StringUtils.isBlank(l)){
					//吊坠 ：12.2 * 6  cm
					l = Helper.getMatcher("(\\d+([.]\\d+)?)\\*", str, 1) ;
				}
			}
			
			length = "".equals(l)?null:NumberUtils.createDouble(l) ;
		}
		if(null == length){
			super.product.addError("产品长度未找到！");
			length = 0d ;
		}
		return length ;
	}


	@Override
	protected Double getWidth(TaoBaoBean bean) {
		Double width = null ;
		Element e = getDocment(bean).select("tr:eq(2) td:eq(1)").first();
		if(e != null){
			String str = StringUtils.trim(e.text()) ;
			String w = "" ;
			if(!StringUtils.isBlank(str)){
				str = str.replaceAll("\\s", "") ;//去除空格
				//长：9 cm             宽：7 cm
				w = Helper.getMatcher("宽(约)?(：)?(\\d+([.]\\d+)?)cm", str, 3) ;
				if(StringUtils.isBlank(w)){
					//吊坠 ：12.2 * 6  cm
					w = Helper.getMatcher("\\*(\\d+([.]\\d+)?)cm", str, 1) ;
				}
			}
			
			width = "".equals(w)?null:NumberUtils.createDouble(w) ;
		}

		if(null == width){
			super.product.addError("产品宽度未找到！");
			width = 0d ;
		}
		return width;
	}

	@Override
	protected Double getHeight(TaoBaoBean bean) {
		// TODO Auto-generated method stub
		return 0d;
	}

	@Override
	protected Double getWeight(TaoBaoBean bean) {
		Double weight = null ;
		Element e = getDocment(bean).select("tr:eq(1) td:eq(3) span").first();
		if(e != null){
			String weightStr = StringUtils.trim(e.text()) ;
			String w = Helper.getMatcher("(\\d+([.]\\d+)?)", weightStr, 1) ;
			weight = "".equals(w)?null:NumberUtils.createDouble(w) ;
		}

		if(null == weight){
			super.product.addError("产品重量未找到！");
			weight = 0d ;
		}
		return weight;
	}


	@Override
	protected String getMainMaterial(TaoBaoBean bean) {
		String material = "" ;
		Element e = getDocment(bean).select("tr:eq(1) td:eq(1) span").first();
		if(e != null){
			material = StringUtils.trim(e.text()) ;
		}
		return "".equals(material)?null:material;
	}
	
	
	@Override
	protected String getProductUrl(TaoBaoBean bean) {
		// TODO Auto-generated method stub
		String orgUrl = "http://www.ass007.com/?gallery--n,"+bean.getName()+"-grid.html" ;
		orgUrl = Helper.getEncodeUrl(orgUrl);
		String url = "" ;
		Document doc;
		try {
			doc = Jsoup.connect(orgUrl).timeout(1000000).get();

			Elements items = doc.select(".items-gallery") ;
			if(null != items && items.size() > 0){
				Element e = items.first() ;
				url = e.select(".goodpic a").first().absUrl("href") ;
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return url ;
	}

	/**
	 * 获取jsoup解析html后的的document
	 * @param bean
	 * @return
	 */
	private Document getDocment(TaoBaoBean bean){
		return Jsoup.parse(bean.getDescHtml()) ;
	}

	

}
