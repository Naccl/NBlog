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
import top.naccl.constant.RedisKeyConstants;
import top.naccl.entity.VisitLog;
import top.naccl.entity.Visitor;
import top.naccl.enums.VisitBehavior;
import top.naccl.model.dto.VisitLogRemark;
import top.naccl.model.vo.BlogDetail;
import top.naccl.model.vo.Result;
import top.naccl.service.RedisService;
import top.naccl.service.VisitLogService;
import top.naccl.service.VisitorService;
import top.naccl.util.AopUtils;
import top.naccl.util.IpAddressUtils;
import top.naccl.util.JacksonUtils;
import top.naccl.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

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
	@Autowired
	VisitorService visitorService;
	@Autowired
	RedisService redisService;

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
		Result result = (Result) joinPoint.proceed();
		int times = (int) (System.currentTimeMillis() - currentTime.get());
		currentTime.remove();
		//获取请求对象
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		//校验访客标识码
		String identification = checkIdentification(request);
		//记录访问日志
		VisitLog visitLog = handleLog(joinPoint, visitLogger, request, result, times, identification);
		visitLogService.saveVisitLog(visitLog);
		return result;
	}

	/**
	 * 校验访客标识码
	 *
	 * @param request
	 * @return
	 */
	private String checkIdentification(HttpServletRequest request) {
		String identification = request.getHeader("identification");
		if (identification == null) {
			//请求头没有uuid，签发uuid并保存到数据库和Redis
			identification = saveUUID(request);
		} else {
			//校验Redis中是否存在uuid
			boolean redisHas = redisService.hasValueInSet(RedisKeyConstants.IDENTIFICATION_SET, identification);
			//Redis中不存在uuid
			if (!redisHas) {
				//校验数据库中是否存在uuid
				boolean mysqlHas = visitorService.hasUUID(identification);
				if (mysqlHas) {
					//数据库存在，保存至Redis
					redisService.saveValueToSet(RedisKeyConstants.IDENTIFICATION_SET, identification);
				} else {
					//数据库不存在，签发新的uuid
					identification = saveUUID(request);
				}
			}
		}
		return identification;
	}

	/**
	 * 签发UUID，并保存至数据库和Redis
	 *
	 * @param request
	 * @return
	 */
	private String saveUUID(HttpServletRequest request) {
		//获取响应对象
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		//获取当前时间戳，精确到小时，防刷访客数据
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		String timestamp = Long.toString(calendar.getTimeInMillis() / 1000);
		//获取访问者基本信息
		String ip = IpAddressUtils.getIpAddress(request);
		String userAgent = request.getHeader("User-Agent");
		//根据时间戳、ip、userAgent生成UUID
		String nameUUID = timestamp + ip + userAgent;
		String uuid = UUID.nameUUIDFromBytes(nameUUID.getBytes()).toString();
		//添加访客标识码UUID至响应头
		response.addHeader("identification", uuid);
		//暴露自定义header供页面资源使用
		response.addHeader("Access-Control-Expose-Headers", "identification");
		//校验Redis中是否存在uuid
		boolean redisHas = redisService.hasValueInSet(RedisKeyConstants.IDENTIFICATION_SET, uuid);
		if (!redisHas) {
			//保存至Redis
			redisService.saveValueToSet(RedisKeyConstants.IDENTIFICATION_SET, uuid);
			//保存至数据库
			Visitor visitor = new Visitor(uuid, ip, userAgent);
			visitorService.saveVisitor(visitor);
		}
		return uuid;
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
	private VisitLog handleLog(ProceedingJoinPoint joinPoint, VisitLogger visitLogger, HttpServletRequest request, Result result,
	                           int times, String identification) {
		String uri = request.getRequestURI();
		String method = request.getMethod();
		String ip = IpAddressUtils.getIpAddress(request);
		String userAgent = request.getHeader("User-Agent");
		Map<String, Object> requestParams = AopUtils.getRequestParams(joinPoint);
		VisitLogRemark visitLogRemark = judgeBehavior(visitLogger.value(), requestParams, result);
		VisitLog log = new VisitLog(identification, uri, method, visitLogger.value().getBehavior(),
				visitLogRemark.getContent(), visitLogRemark.getRemark(), ip, times, userAgent);
		log.setParam(StringUtils.substring(JacksonUtils.writeValueAsString(requestParams), 0, 2000));
		return log;
	}

	/**
	 * 根据访问行为，设置对应的访问内容或备注
	 *
	 * @param behavior
	 * @param requestParams
	 * @param result
	 * @return
	 */
	private VisitLogRemark judgeBehavior(VisitBehavior behavior, Map<String, Object> requestParams, Result result) {
		String remark = "";
		String content = behavior.getContent();
		switch (behavior) {
			case INDEX:
			case MOMENT:
				remark = "第" + requestParams.get("pageNum") + "页";
				break;
			case BLOG:
				if (result.getCode() == 200) {
					BlogDetail blog = (BlogDetail) result.getData();
					String title = blog.getTitle();
					content = title;
					remark = "文章标题：" + title;
				}
				break;
			case SEARCH:
				if (result.getCode() == 200) {
					String query = (String) requestParams.get("query");
					content = query;
					remark = "搜索内容：" + query;
				}
				break;
			case CATEGORY:
				String categoryName = (String) requestParams.get("categoryName");
				content = categoryName;
				remark = "分类名称：" + categoryName + "，第" + requestParams.get("pageNum") + "页";
				break;
			case TAG:
				String tagName = (String) requestParams.get("tagName");
				content = tagName;
				remark = "标签名称：" + tagName + "，第" + requestParams.get("pageNum") + "页";
				break;
			case CLICK_FRIEND:
				String nickname = (String) requestParams.get("nickname");
				content = nickname;
				remark = "友链名称：" + nickname;
				break;
		}
		return new VisitLogRemark(content, remark);
	}
}