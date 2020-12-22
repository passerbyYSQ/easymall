package net.ysq.easymall.component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.ysq.easymall.common.ResultModel;
import net.ysq.easymall.common.StatusCode;
import net.ysq.easymall.po.User;

/**
 * 登录拦截器
 * @author	passerbyYSQ
 * @date	2020-11-18 8:29:50
 */
public class LoginInterceptor implements HandlerInterceptor {
	
	@Autowired
    private ObjectMapper objectMapper;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		User self = (User) session.getAttribute("user");
		
		if (ObjectUtils.isEmpty(self)) {
			//response.sendRedirect("/account/login");
			
			String requestType = request.getHeader("X-Requested-With");
			if (!StringUtils.isEmpty(requestType) && "XMLHttpRequest".equals(requestType)) {
				// 异步请求
				ResultModel<Void> resModel = ResultModel.error(StatusCode.USER_NOT_LOGIN);
				String jsonResModel = objectMapper.writeValueAsString(resModel);
				
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json;charset=UTF-8");
				
				response.getWriter().print(jsonResModel);
			} else {
				// 同步请求
				
				// 获取源路径，方便登录成功后重定向
				String redirectUrl = request.getHeader("referer");
				if (StringUtils.isEmpty(redirectUrl)) {
					redirectUrl = "/product/list";
				}
				
				// 重定向到登录页面
				response.sendRedirect("/account/login?redirect_url=" + redirectUrl);
			}
			
			// 拦截，不往下执行
			return false; 
		} 
		return true;
	}	

}
