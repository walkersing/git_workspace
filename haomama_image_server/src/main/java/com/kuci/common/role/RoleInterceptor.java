package com.kuci.common.role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.kuci.common.GlobalBase;
import com.kuci.common.annotation.ROLE;
import com.kuci.common.annotation.VisitorRole;
import com.kuci.image.util.Utils;

/**
 * 权限拦截器
 * 
 * @author walkersing at 2010-9-16 上午10:31:51
 * 
 *         desc：
 */
public class RoleInterceptor extends GlobalBase implements HandlerInterceptor  {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		response.setHeader("Access-Control-Allow-Credentials", "false");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "*");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
		// 找不到RequestMapping 非法操作
		if (!(handler instanceof HandlerMethod)) {
			return false;
		}
		HandlerMethod method = (HandlerMethod) handler;

		VisitorRole methodRoles = method.getMethodAnnotation(VisitorRole.class);
		VisitorRole classRoles = method.getBean().getClass().getAnnotation(VisitorRole.class);
		
		// 如果没有标识权限,可随意访问
		if (methodRoles == null && classRoles == null) {
			logger.info("No role control.");
			return true;
		}

		ROLE role = (methodRoles != null) ? methodRoles.value() : classRoles.value();
		// 如果权限限制任意,也可随意访问
		if (role == ROLE.ANYONE) {
			logger.info("Any role control.");
			return true;
		}

		if (role == ROLE.ONLY_IP && !Utils.isValidIp(request)) {
			logger.info(Utils.getHeader(request));
			logger.info("no role ip request:" + Utils.getIpAddr(request));
			logger.info("request method:"+method.getMethod().getName());
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		response.setHeader("Access-Control-Allow-Credentials", "false");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "*");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		response.setHeader("Access-Control-Allow-Credentials", "false");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "*");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
	}
}
