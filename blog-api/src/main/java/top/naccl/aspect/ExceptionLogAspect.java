package top.naccl.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.naccl.entity.ExceptionLog;
import top.naccl.service.ExceptionLogService;
import top.naccl.util.AopUtils;
import top.naccl.util.IpAddressUtils;
import top.naccl.util.JacksonUtils;
import top.naccl.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description: AOP记录异常日志
 * @Author: Naccl
 * @Date: 2020-12-03
 */
@Component
@Aspect
public class ExceptionLogAspect {
	@Autowired
	ExceptionLogService exceptionLogService;

	/**
	 * 配置切入点
	 */
	@Pointcut("execution(* top.naccl.controller..*.*(..))")
	public void logPointcut() {
	}

	@AfterThrowing(value = "logPointcut()", throwing = "e")
	public void logAfterThrowing(JoinPoint joinPoint, Exception e) {
		ExceptionLog log = handleLog(joinPoint, e);
		exceptionLogService.saveExceptionLog(log);
	}

	/**
	 * 设置ExceptionLog对象属性
	 *
	 * @return
	 */
	private ExceptionLog handleLog(JoinPoint joinPoint, Exception e) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String uri = request.getRequestURI();
		String method = request.getMethod();
		String ip = IpAddressUtils.getIpAddress(request);
		String userAgent = request.getHeader("User-Agent");
		//todo 使用swagger后，可以直接使用注解上的内容作为 ExceptionLog 的 description
		String error = StringUtils.getStackTrace(e);
		ExceptionLog log = new ExceptionLog(uri, method, error, ip, userAgent);
		Map<String, Object> requestParams = AopUtils.getRequestParams(joinPoint);
		log.setParam(StringUtils.substring(JacksonUtils.writeValueAsString(requestParams), 0, 2000));
		return log;
	}
}