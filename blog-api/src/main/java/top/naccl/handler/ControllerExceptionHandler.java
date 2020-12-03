package top.naccl.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.naccl.exception.NotFoundException;
import top.naccl.exception.PersistenceException;
import top.naccl.model.vo.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 对Controller层全局异常处理
 * @RestControllerAdvice 捕获异常后，返回json数据类型
 * @Author: Naccl
 * @Date: 2020-08-14
 */
@RestControllerAdvice
public class ControllerExceptionHandler {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 捕获自定义的404异常
	 *
	 * @param request 请求
	 * @param e       自定义抛出的异常信息
	 * @return
	 */
	@ExceptionHandler(NotFoundException.class)
	public Result notFoundExceptionHandler(HttpServletRequest request, NotFoundException e) {
		logger.error("Request URL : {}, Exception :", request.getRequestURL(), e);
		return Result.create(404, e.getMessage());
	}

	/**
	 * 捕获自定义的持久化异常
	 *
	 * @param request 请求
	 * @param e       自定义抛出的异常信息
	 * @return
	 */
	@ExceptionHandler(PersistenceException.class)
	public Result persistenceExceptionHandler(HttpServletRequest request, PersistenceException e) {
		logger.error("Request URL : {}, Exception :", request.getRequestURL(), e);
		return Result.create(500, e.getMessage());
	}

	/**
	 * 捕获自定义的登录失败异常
	 *
	 * @param request 请求
	 * @param e       自定义抛出的异常信息
	 * @return
	 */
	@ExceptionHandler(UsernameNotFoundException.class)
	public Result usernameNotFoundExceptionHandler(HttpServletRequest request, UsernameNotFoundException e) {
		logger.error("Request URL : {}, Exception :", request.getRequestURL(), e);
		return Result.create(401, "用户名或密码错误！");
	}

	/**
	 * 捕获其它异常
	 *
	 * @param request 请求
	 * @param e       异常信息
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public Result exceptionHandler(HttpServletRequest request, Exception e) {
		logger.error("Request URL : {}, Exception :", request.getRequestURL(), e);
		return Result.create(500, "异常错误");
	}
}
