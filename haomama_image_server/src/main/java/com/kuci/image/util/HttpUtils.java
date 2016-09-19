package com.kuci.image.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kuci.common.GlobalBase;

/**
 * Http 工具类
 * @author walkersing at 2011-4-21 下午02:44:56
 *
 */
public class HttpUtils extends GlobalBase{
	
	private static Log log = LogFactory.getLog(HttpUtils.class);
	
	
	/**
     * @param url
     * @param charset
     * @param poststrings
     * @return
     */
	 public static String postStrToUrl(String url, String charset,String[] poststrings) {
		 String result = null;
		 int i=0;
		 while(i<5){
			 result = _postStrToUrl(url,charset,poststrings);
			 if(result!=null){
				 break;
			 }
			 i++;
		 }
		 if(result == null){
			 log.error("postStrToUrl retry faild! url="+url);
		 }
		 return result;
	 }
	
	/**

     * @param url
     * @param charset
     * @param poststrings  a=a&b=b类型
     * @return
     */
    public static String _postStrToUrl(String url, String charset,String[] poststrings) {
        //URL url1 = new URL(url);//internal site
        //InputStream inr = null;
        HttpURLConnection huc=null;
        try {
            URLConnection UrlConnObj1 = new URL(url).openConnection();
            huc = (HttpURLConnection) UrlConnObj1;
            huc.setRequestMethod("POST");
            huc.setDoOutput(true);
            huc.setDoInput(true);
            huc.setUseCaches(false);
            huc.setDefaultUseCaches(false);
            BufferedOutputStream out = new BufferedOutputStream(huc.getOutputStream());
            for (int i = 0; i < poststrings.length; i++) {
                out.write(poststrings[i].getBytes(charset));
                if (i<poststrings.length-1)
                	out.write('&');
            }
            out.flush();
            out.close();
            
            InputStream inr = huc.getInputStream();
            char[] buf = new char[1000];
            int length = -1;
            //String result="";
            StringBuffer res = new StringBuffer();
            InputStreamReader reader = new InputStreamReader(inr, charset);
            while ((length = reader.read(buf)) != -1) {
                res.append(buf, 0, length);
            }
            reader.close();
            //inr.close();
            return res.toString();
            
        } catch (Exception e) {
        	System.out.println(e.getMessage()+",url:"+url);
            return null;
        }
        finally{
            if (huc!=null) huc.disconnect();
        }
        
    }
    

    
    
	/**
     * @param url
     * @param charset
     * @param poststrings
     * @return
     */
	 public static String postBinaryToUrl(String url, String charset,byte[] binary) {
		 String result = null;
		 int i=0;
		 while(i<5){
			 result = _postBinaryToUrl(url,charset,binary);
			 if(result!=null){
				 break;
			 }
			 i++;
		 }
		 if(result == null){
			 log.error("postBinaryToUrl retry faild! url="+url);
		 }
		 return result;
	 }
    private static String _postBinaryToUrl(String url, String charset,byte[] binary) {
        //URL url1 = new URL(url);//internal site
        //InputStream inr = null;
        HttpURLConnection huc=null;
        try {
            URLConnection UrlConnObj1 = new URL(url).openConnection();
            huc = (HttpURLConnection) UrlConnObj1;
            huc.setReadTimeout(10000);
            huc.setConnectTimeout(5000);
            huc.setRequestMethod("POST");
            huc.setRequestProperty("content-type","binarry/data");
            huc.setDoOutput(true);
            huc.setDoInput(true);
            huc.setUseCaches(false);
            huc.setDefaultUseCaches(false);
            BufferedOutputStream out = new BufferedOutputStream(huc.getOutputStream());
            out.write(binary);
            out.flush();
            out.close();

            InputStream inr = huc.getInputStream();
            char[] buf = new char[1000];
            int length = -1;
            //String result="";
            StringBuffer res = new StringBuffer();
            InputStreamReader reader = new InputStreamReader(inr, charset);
            while ((length = reader.read(buf)) != -1) {
                res.append(buf, 0, length);
            }
            reader.close();
            //inr.close();
            return res.toString();

        } catch (Exception e) {
        	log.error(e.getMessage()+",url:"+url);
            return null;
        }
        finally{
            if (huc!=null) huc.disconnect();
        }

    }
    
    
    public static  boolean openUrl(String url){
		boolean flag = false;
		try {
			HttpURLConnection conn = (java.net.HttpURLConnection) (new java.net.URL(url)).openConnection();
            conn.setReadTimeout(50000);
            conn.setConnectTimeout(50000);
            conn.setRequestMethod("HEAD");

            flag = conn.getResponseCode()==200;
            
		} catch (Exception e) {
			log.error("openUrl error url="+url+"\t error message="+e.getMessage());
		}
		return flag;
	}
    
    
    public static String getUrlContent(String url) {
    	String re = null;
    	for(int retry=0;retry<5;retry++){
    		re = getUrlContent(url, "UTF-8");
    		if(re!=null){
    			break;
    		}
    	}
    	if(re==null){
    		log.error("getUrlContent failed! url="+url);
    	}
        return re;
    }
    
    
    public static String getUrlContent(String newurl, String charset) {
        java.io.InputStream inr = null;
        java.net.HttpURLConnection conn = null;
        try {
            conn = (java.net.HttpURLConnection) (new java.net.URL(newurl)).openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            inr = conn.getInputStream();
            char[] buf = new char[300];
            int length = -1;
            //String result="";
            StringBuffer res = new StringBuffer(3000);
            InputStreamReader reader = new InputStreamReader(inr, charset);
            while ((length = reader.read(buf)) != -1) {
                res.append(buf, 0, length);
            }
            reader.close();
            return res.toString();
        } catch (Exception e) {
            log.error(e.getMessage()+",url:"+newurl);
            return null;
        } finally {
            try {
                if (inr != null)
                    inr.close();
                if (conn != null)
                    conn.disconnect();
            } catch (IOException e) {
            }
        }
    }


	public static Map<String, String> getRequestHeaders(
			HttpServletRequest request) {
		HashMap<String, String> res = new HashMap<String, String>();
		Enumeration<String> emu = request.getHeaderNames();
		while (emu.hasMoreElements()) {
			String key = emu.nextElement();
			String value = request.getHeader(key);
			res.put(key, value);
		}
		return res;
	}

	public static String getSessionString(HttpServletRequest request,
			final String name,String defaultValue) {
		String str = defaultValue;
		Object value = request.getSession().getAttribute(name);
		if (value != null) {
			str = (String)value;
		}
		return str;
	}


//	public static String getSessionVerifyCode(HttpServletRequest request,
//			final String name,String defaultValue) {
//		String code = getSessionString(request, name, defaultValue);
//		request.getSession().removeAttribute(name);
//		return code;
//	}

	public static String getRequestString(HttpServletRequest request,
			final String name) {
		return getRequestString(request, name, null);
	}

	public static String getRequestString(HttpServletRequest request,
			final String name, final String defaultValue) {
		String value = request.getParameter(name);
		if (value == null) {
			value =  defaultValue;
		}
		return value;
	}

	public static int getRequestInt(HttpServletRequest request,
			final String name) {
		return getRequestInt(request, name, 0);
	}

	public static int getRequestInt(HttpServletRequest request,
			final String name, int defaultValue) {
		String value = getRequestString(request, name, null);
		if (value == null)
			return defaultValue;

		try {
			return Integer.parseInt(value.trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}


	public static String getCookie(HttpServletRequest request, String cname) {
		Cookie[] cookies = request.getCookies();
		if(cookies!=null){
			for(Cookie c :cookies){
			    if(c.getName().equalsIgnoreCase(cname)){
			    	return c.getValue();
			    }
			}
		}
		return null;
	}

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}


	/*
	 * 写cookie
	*/
	public static void setCookie(HttpServletResponse response,String name,String value){
		Cookie cookie = new Cookie(name,value);
		//生命周期
		cookie.setMaxAge(60*60*24*365);
		cookie.setPath("/");
		response.addCookie(cookie);
	}



	//test
	public static String getRequestInfo(HttpServletRequest req) {
		StringBuffer out = new StringBuffer();
		out.append("Cookies:\n");
		Cookie[] cookies = req.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				out.append(cookies[i].getName() + "=" + cookies[i].getValue()+"\n");
				out.append("---\n");
			}
		}
		return out.toString();
	}
	
	public static String replaceUUIDFromURL(String url){
		if(StringUtils.isEmpty(url)){
			return url;
		}
		if(url.indexOf("?")==-1){
			return url;
		}
		if(url.indexOf("uuid=")==-1){
			return url;
		}
		
		if( url.indexOf("&")==-1){
			return url.substring(0, url.indexOf("?"));
		}
		String _url = url.substring(0, url.indexOf("?")+1);
		String para =  url.substring( url.indexOf("?")+1,url.length());
		String[] obj = para.split("&");
		for(String s:obj){
			if(s.split("=").length==2){
				String[] ss = s.split("=");
				if(ss[0].equals("uuid")){
					continue;
				}
				_url+=ss[0]+"="+ss[1]+"&";
			}
		}
		if(_url.lastIndexOf("&")+1 ==_url.length()){
			_url = _url.substring(0, _url.length()-1);
		}
		return _url;
	}
	
	
	/**
	 * 引当前请求的来源
	 * @param request
	 * @return
	 */
	public static String getRefer(HttpServletRequest request) {
		String referer = null;
		String rURI = null;
		String rQuery = null;
		rURI = request.getRequestURI();
		rQuery = request.getQueryString();

		rURI = request.getScheme()
				+ "://"
				+ request.getServerName()
				+ (request.getServerPort() == 80 ? "" : (":" + request
						.getServerPort())) + rURI;

		if (rQuery != null && !"".equals(rQuery)) {
			referer = rURI + "?" + rQuery;
		} else {
			referer = rURI;
		}

		return referer;
	}

//
//	/**
//	 * 根据url得到网页内容
//	 * 
//	 * @param url
//	 * @return
//	 * @throws HttpException
//	 * @throws IOException
//	 */
//	public static String getContentByUrl(String url) throws Exception {
//		HttpClient client = new HttpClient();
//		
//		//定义代理的ip和端口号
////	    HostConfiguration hcf = new HostConfiguration();
////	    hcf.setProxy("69.86.0.230",8090);
////	    client.setHostConfiguration(hcf);
//	    
//	    HttpConnectionManagerParams managerParams = client .getHttpConnectionManager().getParams(); 
//		// 设置连接超时时间(单位毫秒) 
//		managerParams.setConnectionTimeout(30000); 
//		// 设置读数据超时时间(单位毫秒) 
//		managerParams.setSoTimeout(120000); 
//	    
//		HttpMethod getmethod = new GetMethod(url);
//		getmethod.setRequestHeader(
//						"User-Agent",
//						"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.1.6) Gecko/20091201 Firefox/3.5.6");
////		getmethod.setRequestHeader("Referer", "	http://www.qisuu.com/");
//		client.executeMethod(getmethod);
//		Header h = getmethod.getResponseHeader("Content-Type");
//		String code = null;
//		String content = null;
//		if (h != null) {
//			String reg = "charset\\s*?=\\s*?(.*?)\\s*?$";
//			code = getResult(reg, h.getValue());
//			if (code != null) {// 从header中得到编码
//				code = code.trim();
//				code = code.substring(code.indexOf("charset=") + 8, code
//						.length());
//				content = ClassUtils.convertStreamToString(getmethod
//						.getResponseBodyAsStream(), code);
//			} else {
//				content = ClassUtils.convertStreamToString(getmethod
//						.getResponseBodyAsStream(), "UTF-8");
//				reg = "<\\s*?meta\\s*?http-equiv\\s*?=\\s*?\"Content-Type\"\\s*?content\\s*?=\\s*?\".*?charset\\s*?=\\s*?(.*?)\"\\s*?/?>";
//				code = getResult(reg, content);
//				if (code != null) {// 从页面找到编码
//					code = code.trim();
//					if (!code.equalsIgnoreCase("UTF-8")) {
//						client.executeMethod(getmethod);
//						code = code.substring(code.indexOf("charset=") + 8,
//								code.length() - 2);
//						code = code.replaceAll("\"", "").trim();
//						content = ClassUtils.convertStreamToString(getmethod
//								.getResponseBodyAsStream(), code);
//					}
//				} else {
//					client.executeMethod(getmethod);
//					content = ClassUtils.convertStreamToString(getmethod
//							.getResponseBodyAsStream());
//				}
//			}
//		} else {
//			content = ClassUtils.convertStreamToString(getmethod
//					.getResponseBodyAsStream());
//		}
//		getmethod.releaseConnection();
//		return content;
//	}

	public static String getContentByUrl2(String urlStr,String encode,boolean isCompress,String referer,
			boolean isSetUA,String cookie,boolean isProxy) throws Exception {
		 String result = null;
		 int i=0;
		 while(i<2){
			 result = _getContentByUrl2(urlStr,encode,isCompress,referer,isSetUA,cookie,isProxy);
			 if(result!=null){
				 break;
			 }
			 i++;
		 }
		 
		 if(result == null){
			 log.error("getContentByUrl2 retry faild! url="+urlStr);
		 }
		 return result;
	}
	
	/**
	 * 得url的html
	 * @param urlStr url
	 * @param encode 编码
	 * @param isCompress 是否压缩
	 * @param referer 是否设置referer
	 * @param isSetUA 是否隐藏为百度spider
	 * @return
	 * @throws Exception 
	 */
	public static String _getContentByUrl2(String urlStr,String encode,boolean isCompress,String referer,
			boolean isSetUA,String cookie,boolean isProxy) throws Exception {
		if(StringUtils.isEmpty(encode))
			encode = "gbk";
		
		URL url;
		StringBuffer sb = new StringBuffer();
		HttpURLConnection conn =null;
		Properties prop = System.getProperties();
		try {
			url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(25000);
			conn.setReadTimeout(25000);
			
//			if(isProxy || urlStr.indexOf("yintai") !=-1){
//				//http://www.adminym.com/ip/list_1_226.html
//				//219.141.216.88
//				//222.141.199.150
//				//211.155.128.44
//				prop.setProperty("http.proxyHost","221.194.177.162");     //设置http访问要使用的代理服务器的地址     
//				prop.setProperty("http.proxyPort","81"); //设置http访问要使用的代理服务器的端口
//			}
			
			
			if(StringUtils.isNotEmpty(cookie))
				conn.setRequestProperty("Cookie", cookie);
			
			conn.setRequestProperty("User-agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.3) Gecko/20100401 Toolbar/alxf-1.54 Firefox/3.6.3 ( .NET CLR 3.5.30729)");
			
			//if(isSetUA)
			//	conn.setRequestProperty("User-agent","Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)");
			
			if(referer !=null){
				conn.setRequestProperty("Referer", referer);
			}else{
				conn.setRequestProperty("Referer", "www.google.com");
			}
			
			if(conn.getResponseCode() == 302 || conn.getResponseCode() == 404){
				return conn.getURL().toString();
			}
			
			BufferedReader in = null;
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(),encode));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine);
				sb.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(conn != null)
				conn.disconnect();
			if(prop != null)
			    prop.clear();
		}
		if(isCompress)
			return minimizeHtml(sb.toString());
		else
			return sb.toString();
	}

	private static String minimizeHtml(String html){
		return html.replaceAll("\t", "").replace("\r\n", "").replaceAll("\n", "").replaceAll("\r","").replaceAll("  ", "");
	}
	

	/**
	 * 判断两个字符串是否类似
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean isSimilar(String str1, String str2) {
		if (str1 != null && str2 != null) {
			str1 = str1.replaceAll("\\s", "");
			if (str1.length() > 10) {
				str1 = str1.substring(0, 10);
			}
			str2 = str2.replaceAll("\\s", "");
			if (str2.length() > 10) {
				str2 = str2.substring(0, 10);
			}
			return str1.equalsIgnoreCase(str2);
		}
		return false;
	}
	
    public static  boolean checkImageUrl(String url){
		boolean flag = false;
		try {
			HttpURLConnection conn = (java.net.HttpURLConnection) (new java.net.URL(url)).openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("HEAD");
            if(conn.getContentLength() > 0 && conn.getResponseCode()==200 && ("image/jpeg".equals(conn.getContentType()) ||"image/png".equals(conn.getContentType()) ||"image/gif".equals(conn.getContentType()))){
               flag = true;
            }
            
		} catch (Exception e) {
			log.error("openUrl error url="+url+"\t error message="+e.getMessage());
		}
		return flag;
	}

}
