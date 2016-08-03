package com.turborep.turbotracker.user.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class UserInterceptor extends HandlerInterceptorAdapter 
{
	private String welcome;
	
	private Logger logger = Logger.getLogger(UserInterceptor.class);
	
	public void setWelcome(String welcome) {
		this.welcome = welcome;
	}

	@Override
    public boolean preHandle(HttpServletRequest request,  HttpServletResponse response, Object handler) throws Exception 
    {
		//logger.debug("Interceptor Pre Handler.....");
		//System.out.println(request.getRequestURL());
		
        return super.preHandle(request, response, handler);
	}
	
	@Override
	public void postHandle(
			HttpServletRequest request,
			HttpServletResponse response,
			Object handler,
			ModelAndView modelAndView ) throws Exception 
	{
		//logger.debug("Interceptor Post Handler");
		HttpSession session = request.getSession(true);
		//System.out.println("Session New?: " + session.isNew());
		/**if (!session.isNew() && session.getAttribute(SessionConstants.USER) == null /&& session.getAttribute("username") == null/) {
			logger.debug("App context path: " + request.getContextPath());
			modelAndView.addObject("user-message", "Session expired");*/
			
			//modelAndView.setViewName(welcome);
			//logger.debug("View1 is: " + modelAndView.getView());
		/**} else {
			session.removeAttribute("username");
		}*/
		super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request,
								HttpServletResponse response, 
								Object handler, 
								Exception excep) throws Exception 
	{
		//logger.debug("Interceptor after completion ....");
		super.afterCompletion(request, response, handler, excep);
	}
}
