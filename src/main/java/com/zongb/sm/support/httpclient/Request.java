
package com.zongb.sm.support.httpclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 * 发送请求
 * @author Legend、
 *
 */

public class Request {
	
	public static BasicCookieStore cookieStore = new BasicCookieStore(); 
	
	private final static String HTTP_SSL_PREFIX = "HTTPS://" ;
	
	//这是模拟get请求
	public static Result sendGet(String url,Map<String,String> headers,Map<String,String>  params) throws ClientProtocolException, IOException{
		
		CloseableHttpClient client = createHttpClient(url.toUpperCase().startsWith(HTTP_SSL_PREFIX));
		//如果有参数的就拼装起来
		url = getRealUrl(url,params);
		System.out.println("getUrl="+url);
		//这是实例化一个get请求
		HttpGet hp = new HttpGet(url);
		//如果需要头部就组装起来
		if(null!=headers){
			hp.setHeaders(assemblyHeader(headers));
		}

		//实行请求并返回
		return client.execute(hp,getResponseHandler());
	}
	
	//这是模拟post请求
	public static Result sendPost(String url,Map<String,String> headers,Map<String,String>  params,String encoding) throws ClientProtocolException, IOException{
		//实例化一个Httpclient的
		CloseableHttpClient client = createHttpClient(url.toUpperCase().startsWith(HTTP_SSL_PREFIX));
		//实例化一个post请求
		HttpPost post = new HttpPost(url);
		
		//设置需要提交的参数
		List<NameValuePair> list  = new ArrayList<NameValuePair>();
		for (String temp : params.keySet()) {
			list.add(new BasicNameValuePair(temp,params.get(temp)));
		}
		post.setEntity(new UrlEncodedFormEntity(list,encoding));
		
		//设置头部
		if(null!=headers){
			post.setHeaders(assemblyHeader(headers));
		}

		//实行请求并返回
		return client.execute(post,getResponseHandler());
		
	}
	
	/**
	 * 获取response处理类
	 * @return
	 */
	public static ResponseHandler<Result> getResponseHandler(){
		return new ResponseHandler<Result>() {
			//处理方法
            public Result handleResponse(
                    final HttpResponse response) throws ClientProtocolException, IOException {
            	//封装返回的参数
                Result result = new Result();
                //设置返回状态代码
                result.setStatusCode(response.getStatusLine().getStatusCode());
                //设置返回的头部信息
                result.setHeaders(response.getAllHeaders());
        		
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    //设置返回到信息
            		result.setHttpEntity(entity);
                    result.setBody( entity != null ? EntityUtils.toString(entity) : null) ;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
                
                return result ;
            }

        };
	}
	
	/**
	 * 创建httpclient
	 * @param isSSL
	 * @return
	 */
	public static CloseableHttpClient createHttpClient(boolean isSSL){
		CloseableHttpClient httpclient ;
		
		if(isSSL){
			 SSLContext sslcontext = SSLContexts.createDefault() ;
		     SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,
	                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		        
		     httpclient = HttpClients.custom()
					.setDefaultCookieStore(cookieStore)
	                .setSSLSocketFactory(sslsf)
	                .build();
		}else{
			httpclient = HttpClients.custom()
					.setDefaultCookieStore(cookieStore)
					.build();
		}
		
		return httpclient ;
		
	}
	
	//这是组装头部
	private static Header[] assemblyHeader(Map<String,String> headers){
		Header[] allHeader= new BasicHeader[headers.size()];
		int i  = 0;
		for (String str :headers.keySet()) {
			allHeader[i] = new BasicHeader(str,headers.get(str));
			i++;
		}
		return allHeader;
	}
	
	//这是组装cookie
	private static String assemblyCookie(List<Cookie> cookies){
		StringBuffer sbu = new StringBuffer();
		for (Cookie cookie : cookies) {
			sbu.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
		}
		if(sbu.length()>0)sbu.deleteCharAt(sbu.length()-1);
		return sbu.toString();
	}
	//这是组装参数
	private static String getRealUrl(String url,Map<String,String> parameters){
		if(parameters == null){
			return url ;
		}
		String para = url;
		if(!url.endsWith("?") && url.indexOf("?")>0){
			 para += "&";
		}else{
			 para += "?";
		}
		for (String str :parameters.keySet()) {
			para+=str+"="+parameters.get(str)+"&";
		}
		return para.substring(0,para.length()-1);
	}
	
}
