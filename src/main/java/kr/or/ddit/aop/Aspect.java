package kr.or.ddit.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@org.aspectj.lang.annotation.Aspect
public class Aspect {
	
	private static final Logger logger = LoggerFactory.getLogger(Aspect.class);
	
	@Pointcut("execution(* kr.or.ddit.user.service.*.*(..))")
	public void dummy() {}
	
	// 특정 메소드가 실행되기전에 실행되어야할 공통의 관심사항
	@Before("dummy()")
	public void beforeMethod(JoinPoint joinPoint) {
		logger.debug("Aspect.beforeMethod");
	}
	
	// around : 특정 메소드 실행 전후
	// 메소드 실행 전 - 공통 관심사항
	// 메소드의 원래 로직
	// 메소드 실행 후 - 공통 관심사항
	@Around("dummy()")
	public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {

		// 메소드 본 로직 실행전 
		long startTime = System.nanoTime();
		
		String className = joinPoint.getTarget().getClass().getSimpleName();
		String methodName = joinPoint.getSignature().getName();

		
		// 원래 로직을 실행하는 부분
		Object ret = joinPoint.proceed(joinPoint.getArgs());

		
		// 메소드 본 로직 실행 후
		long endTime = System.nanoTime();
		
		logger.debug("{} {} : duration : {} ", className, methodName, endTime-startTime);
		
		return ret;
	}
}

