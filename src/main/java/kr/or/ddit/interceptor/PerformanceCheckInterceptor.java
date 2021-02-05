package kr.or.ddit.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class PerformanceCheckInterceptor extends HandlerInterceptorAdapter{

	private static final Logger logger = LoggerFactory.getLogger(PerformanceCheckInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// ���۽ð��� ���
		long startTime = System.nanoTime();
		request.setAttribute("startTime", startTime);
		
		// ��û�� ���� interceptor or controller���� �������� ���θ� ��ȯ
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		// controller �޼ҵ尡 ����� ���� ����Ǵ� ����
		long endTime = System.nanoTime();
		long startTime = (long)request.getAttribute("startTime");
		
		logger.debug("uri : {} , duration : {}", request.getRequestURI() , endTime-startTime );
		
	}
	
}
