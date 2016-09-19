/**
 * 
 */
package com.kuci.image.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * @author Joshuabrightsc
 *
 */
public class Utils {

	
	/**
	 * 获取ip地址
	 * @param request
	 * @return
	 */
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
	
	/**
	 * 如果请求的IP在ALLOW_IP的列表中就返回true
	 * 其它情况全部返回false
	 * @param request
	 * @return
	 */
	public static boolean isValidIp(HttpServletRequest request){
		String urlStr = SysConfig.getInstance().getConfig("allow_ip");
		String requestIp = Utils.getIpAddr(request);
		if(StringUtils.isNotEmpty(requestIp) && requestIp.indexOf(",")!=-1){
			requestIp = requestIp.substring(0, requestIp.indexOf(","));
		}
		if(urlStr.indexOf(requestIp)!=-1){
			return true;
		}
		System.out.println("ip验证不通过!!!");
		return false;
	}
	
	public static String getHeader(HttpServletRequest request){
		StringBuffer sb = new StringBuffer();
		Enumeration<String>  names = request.getHeaderNames(); 
		while(names.hasMoreElements()){ 
		    String name = (String)names.nextElement(); 
		    sb.append(name + ":" + request.getHeader(name) + ",");
		}
		return sb.toString();
	}
}
