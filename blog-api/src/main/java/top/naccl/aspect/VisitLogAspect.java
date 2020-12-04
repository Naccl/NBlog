package top.naccl.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.naccl.annotation.VisitLogger;
import top.naccl.entity.VisitLog;
import top.naccl.model.vo.BlogDetail;
import top.naccl.model.vo.Result;
import top.naccl.service.VisitLogService;
import top.naccl.util.AopUtils;
import top.naccl.util.IpAddressUtils;
import top.naccl.util.JacksonUtils;
import top.naccl.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: AOP记录访问日志
 * @Author: Naccl
 * @Date: 2020-12-04
 */
@Component
@Aspect
public class VisitLogAspect {
	@Autowired
	VisitLogService visitLogService;

	ThreadLocal<Long> currentTime = new ThreadLocal<>();

	/**
	 * 配置切入点
	 */
	@Pointcut("@annotation(visitLogger)")
	public void logPointcut(VisitLogger visitLogger) {
	}

	/**
	 * 配置环绕通知
	 *
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("logPointcut(visitLogger)")
	public Object logAround(ProceedingJoinPoint joinPoint, VisitLogger visitLogger) throws Throwable {
		currentTime.set(System.currentTimeMillis());
		Object result = joinPoint.proceed();
		int times = (int) (System.currentTimeMillis() - currentTime.get());
		currentTime.remove();
		VisitLog visitLog = handleLog(joinPoint, visitLogger, result, times);
		visitLogService.saveVisitLog(visitLog);
		return result;
	}

	/**
	 * 设置VisitLogger对象属性
	 *
	 * @param joinPoint
	 * @param visitLogger
	 * @param result
	 * @param times
	 * @return
	 */
	private VisitLog handleLog(ProceedingJoinPoint joinPoint, VisitLogger visitLogger, Object result, int times) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String uri = request.getRequestURI();
		String method = request.getMethod();
		String behavior = visitLogger.behavior();
		String content = visitLogger.content();
		String ip = IpAddressUtils.getIpAddress(request);
		String userAgent = request.getHeader("User-Agent");
		Map<String, Object> requestParams = AopUtils.getRequestParams(joinPoint);
		Map<String, String> map = judgeBehavior(behavior, content, requestParams, result);
		VisitLog log = new VisitLog(uri, method, behavior, map.get("content"), map.get("remark"), ip, times, userAgent);
		log.setParam(StringUtils.substring(JacksonUtils.writeValueAsString(requestParams), 0, 2000));
		return log;
	}

	/**
	 * 根据访问行为，设置对应的访问内容或备注
	 *
	 * @param behavior
	 * @param content
	 * @param requestParams
	 * @param result
	 * @return
	 */
	private Map<String, String> judgeBehavior(String behavior, String content, Map<String, Object> requestParams, Object result) {
		Map<String, String> map = new HashMap<>();
		String remark = "";
		if (behavior.equals("访问页面") && (content.equals("首页") || content.equals("动态"))) {
			int pageNum = (int) requestParams.get("pageNum");
			remark = "第" + pageNum + "页";
		} else if (behavior.equals("查看博客")) {
			Result res = (Result) result;
			if (res.getCode() == 200) {
				BlogDetail blog = (BlogDetail) res.getData();
				String title = blog.getTitle();
				content = title;
				remark = "文章标题：" + title;
			}
		} else if (behavior.equals("搜索博客")) {
			Result res = (Result) result;
			if (res.getCode() == 200) {
				String query = (String) requestParams.get("query");
				content = query;
				remark = "搜索内容：" + query;
			}
		} else if (behavior.equals("查看分类")) {
			String categoryName = (String) requestParams.get("categoryName");
			int pageNum = (int) requestParams.get("pageNum");
			content = categoryName;
			remark = "分类名称：" + categoryName + "，第" + pageNum + "页";
		} else if (behavior.equals("查看标签")) {
			String tagName = (String) requestParams.get("tagName");
			int pageNum = (int) requestParams.get("pageNum");
			content = tagName;
			remark = "标签名称：" + tagName + "，第" + pageNum + "页";
		} else if (behavior.equals("点击友链")) {
			String nickname = (String) requestParams.get("nickname");
			content = nickname;
			remark = "友链名称：" + nickname;
		}
		map.put("remark", remark);
		map.put("content", content);
		return map;
	}
}