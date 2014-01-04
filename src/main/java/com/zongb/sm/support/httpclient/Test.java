package com.zongb.sm.support.httpclient;


import java.util.HashMap;
import java.util.Map;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.zongb.sm.opencart.Helper;

public class Test {

	private static Logger loger = Logger.getLogger(Test.class) ;
	
	public final static void main(String[] args) throws Exception {

		try{
			//登录
			loginAliexpress();
			//
	        Result rs = Request.sendGet("http://cn.ae.aliexpress.com/"
	        		, getAliexpressHeader()
	        		, null) ;
	
//	        loger.debug(rs.getBody()) ;
            
	        Document doc = Jsoup.parse(rs.getBody()) ;
	        Element info = doc.select("div.welcome_info").first() ;
            if(null != info){
            	loger.debug(info.text()) ;
            }
            
            
            
	        
        }catch(Exception e){
        	e.printStackTrace();
        }
	}
	
	public static void loginAliexpress(){
		try {

            loger.debug("----1.开始登录步骤1--------------------------------");
            Result rs = Request.sendGet("https://login.aliexpress.com/seller.htm"
            		, getAliexpressHeader()
            		, null) ;
//            loger.debug(rs.getBody());
            loger.debug("----1.开始登录步骤1结束--------------------------------");
            
            String loginUrl = "https://login.aliexpress.com/xman/xlogin.js" ;
            Map<String,String> loginParams = new HashMap<String,String>() ;
            loginParams.put("pd", "aliexpress") ;
            loginParams.put("xloginPassport", "zzbeads1@gmail.com") ;
            loginParams.put("xloginPassword", "20052161") ;

            loger.debug("----2.开始登录步骤2--------------------------------");
            rs = Request.sendGet(loginUrl
            		, getAliexpressHeader()
            		, loginParams) ;

//            loger.debug(rs.getBody()) ;
            loger.debug("----2.开始登录步骤2结束--------------------------------");
            

            String myUrl2 = "https://passport.alipay.com/mini_apply_st.js?site=4&callback=window.xmanDealTokenCallback&token=";
            myUrl2 += getToken(rs.getBody()) ;

            loger.debug("----3.开始登录步骤3--------------------------------");
            rs = Request.sendGet(myUrl2
            		, getAliexpressHeader()
            		, null) ;

//            loger.debug(rs.getBody()) ;
            loger.debug("----3.开始登录步骤3结束--------------------------------");
            

            String myUrl3 = "https://login.aliexpress.com/validateST.htm";
            Map<String,String> loginParams3 = new HashMap<String,String>() ;
            loginParams3.put("st", getST(rs.getBody())) ;
            loginParams3.put("pd", "aliexpress") ;
            loginParams3.put("xloginPassport", "zzbeads1@gmail.com") ;
            loginParams3.put("xloginPassword", "20052161") ;

            loger.debug("----4.开始登录步骤4--------------------------------");
            rs = Request.sendGet(myUrl3
            		, getAliexpressHeader()
            		, loginParams3) ;

//            loger.debug(rs.getBody()) ;
            loger.debug("----4.开始登录步骤4结束--------------------------------");
            
//            String myUrl = "https://login.aliexpress.com/xloginCallBackForRisk.do";
//
//            Request.sendPost(myUrl
//            		, getAliexpressHeader()
//            		, loginParams
//            		, HTTP.UTF_8) ;
//
//            loger.debug("----5--------------------------------");
//
            String myUrl4 = getUrl(rs.getBody())+"&moduleKey=common.xman.SetCookie";

            loger.debug("----5.开始登录步骤5--------------------------------");
            rs = Request.sendGet(myUrl4
            		, getAliexpressHeader()
            		, null) ;

//            loger.debug(rs.getBody()) ;
            loger.debug("----5.开始登录步骤5结束--------------------------------");
            
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Map<String,String> getAliexpressHeader(){
		Map<String,String> headers = new HashMap<String,String>() ;
		headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,en-US;q=0.4");
        headers.put("Accept-Encoding", "gzip,deflate,sdch");
        headers.put("Host", "cn.ae.aliexpress.com");
		return headers ;
	}

	private static String getToken(String str){
		String regex = "\"token\":\"(\\w+)\"" ;
		return Helper.getMatcher(regex, str, 1) ;
	}
	private static String getST(String str){
		String regex = "\"st\":\"(\\w+)\"}" ;
		return Helper.getMatcher(regex, str, 1) ;
	}

	private static String getUrl(String str){
		String regex = ",\"xlogin_urls\":\\[\"(\\S*?)\"\\]" ;
		return Helper.getMatcher(regex, str, 1) ;
	}
	private static Cookie []  getCookies(String str){
		//List<Cookie> cookies = new ArrayList<Cookie>() ;
		String [] cookie = str.split(";") ;
		Cookie [] cookies = new Cookie[cookie.length] ;
		int i=0 ;
		for(String c : cookie){
			String [] t = c.split("=") ;
			cookies[i++] = new BasicClientCookie2(t[0],t[1]);
		}
		
		return cookies ;
	}
	
}
