package top.naccl.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import top.naccl.annotation.AccessLimit;
import top.naccl.model.vo.Result;
import top.naccl.service.RedisService;
import top.naccl.util.IpAddressUtils;
import top.naccl.util.JacksonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @Description: 访问控制拦截器
 * @Author: Naccl
 * @Date: 2021-04-04
 */
@Component
public class AccessLimitInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	RedisService redisService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
			//方法上没有访问控制的注解，直接通过
			if (accessLimit == null) {
				return true;
			}
			int seconds = accessLimit.seconds();
			int maxCount = accessLimit.maxCount();
			String ip = IpAddressUtils.getIpAddress(request);
			String method = request.getMethod();
			String requestURI = request.getRequestURI();
			String redisKey = ip + ":" + method + ":" + requestURI;
			Integer count = redisService.getObjectByValue(redisKey, Integer.class);
			if (count == null) {
				//在规定周期内第一次访问，存入redis
				redisService.incrementByKey(redisKey, 1);
				redisService.expire(redisKey, seconds);
			} else {
				if (count >= maxCount) {
					//超出访问限制次数
					response.setContentType("application/json;charset=utf-8");
					PrintWriter out = response.getWriter();
					Result result = Result.create(403, accessLimit.msg());
					out.write(JacksonUtils.writeValueAsString(result));
					out.flush();
					out.close();
					return false;
				} else {
					//没超出访问限制次数
					redisService.incrementByKey(redisKey, 1);
				}
			}
		}
		return true;
	}
}
