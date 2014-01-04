package com.zongb.sm.support.httpclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zongb.sm.opencart.Helper;

public class T {

	public T() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String [] args) throws Exception{
		String orgUrl = "http://www.ass007.com/?gallery--n,欧美复古夸张朋克哥特 韦雪 权志龙GD同款克罗心骷髅头戒指 指环男女 R782-grid.html" ;
		orgUrl = Helper.getEncodeUrl(orgUrl);
		String url = "" ;
		Document doc;
		try {
			doc = Jsoup.connect(orgUrl).timeout(1000000).get();

			System.out.println(doc.toString());
			Elements items = doc.select(".items-gallery") ;
			if(null != items && items.size() > 0){
				Element e = items.first() ;
				url = e.select(".goodpic a").first().absUrl("href") ;
			}
			System.out.println(url);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	private static String getToken(String str){
		String regex = "\"token\":\"(\\w+)\"}" ;
		return Helper.getMatcher(regex, str, 1) ;
	}
	
}
