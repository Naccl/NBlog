package top.naccl.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.entity.ScheduleJob;
import top.naccl.entity.ScheduleJobLog;
import top.naccl.model.vo.Result;
import top.naccl.service.ScheduleJobService;
import top.naccl.util.common.ValidatorUtils;

import java.util.Date;

/**
 * @Description: 定时任务动态管理
 * @Author: Naccl
 * @Date: 2020-11-01
 */
@RestController
@RequestMapping("/admin")
public class ScheduleJobController {
	@Autowired
	private ScheduleJobService scheduleJobService;

	/**
	 * 分页查询定时任务列表
	 *
	 * @param pageNum  页码
	 * @param pageSize 每页条数
	 * @return
	 */
	@GetMapping("/jobs")
	public Result jobs(@RequestParam(defaultValue = "1") Integer pageNum,
	                   @RequestParam(defaultValue = "10") Integer pageSize) {
		String orderBy = "create_time desc";
		PageHelper.startPage(pageNum, pageSize, orderBy);
		PageInfo<ScheduleJob> pageInfo = new PageInfo<>(scheduleJobService.getJobList());
		return Result.ok("请求成功", pageInfo);
	}

	/**
	 * 根据id查询定时任务信息
	 *
	 * @param jobId 任务id
	 * @return
	 */
	@GetMapping("/job")
	public Result job(@RequestParam Long jobId) {
		ScheduleJob schedule = scheduleJobService.getJobById(jobId);
		return Result.ok("请求成功", schedule);
	}

	/**
	 * 新建定时任务
	 *
	 * @param scheduleJob
	 * @return
	 */
	@PostMapping("/job")
	public Result saveJob(@RequestBody ScheduleJob scheduleJob) {
		ValidatorUtils.validateEntity(scheduleJob);
		scheduleJobService.saveJob(scheduleJob);
		return Result.ok("添加成功");
	}

	/**
	 * 修改定时任务
	 *
	 * @param scheduleJob
	 * @return
	 */
	@PutMapping("/job")
	public Result updateJob(@RequestBody ScheduleJob scheduleJob) {
		ValidatorUtils.validateEntity(scheduleJob);
		scheduleJobService.updateJob(scheduleJob);
		return Result.ok("修改成功");
	}

	/**
	 * 删除定时任务
	 *
	 * @param jobId 任务id
	 * @return
	 */
	@DeleteMapping("/job")
	public Result deleteJob(@RequestParam Long jobId) {
		scheduleJobService.deleteJobById(jobId);
		return Result.ok("删除成功");
	}

	/**
	 * 立即执行任务
	 *
	 * @param jobId 任务id
	 * @return
	 */
	@PostMapping("/job/run")
	public Result runJob(@RequestParam Long jobId) {
		scheduleJobService.runJobById(jobId);
		return Result.ok("提交执行");
	}

	/**
	 * 更新任务状态：暂停或恢复
	 *
	 * @param jobId  任务id
	 * @param status 状态
	 * @return
	 */
	@PutMapping("/job/status")
	public Result updateJobStatus(@RequestParam Long jobId, @RequestParam Boolean status) {
		scheduleJobService.updateJobStatusById(jobId, status);
		return Result.ok("更新成功");
	}

	/**
	 * 分页查询定时任务日志列表
	 *
	 * @param pageNum  页码
	 * @param pageSize 每页条数
	 * @return
	 */
	@GetMapping("/job/logs")
	public Result logs(@RequestParam(defaultValue = "1") Integer pageNum,
	                   @RequestParam(defaultValue = "10") Integer pageSize) {
		String orderBy = "create_time desc";
		PageHelper.startPage(pageNum, pageSize, orderBy);
		PageInfo<ScheduleJobLog> pageInfo = new PageInfo<>(scheduleJobService.getJobLogList());
		return Result.ok("请求成功", pageInfo);
	}

	/**
	 * 根据id查询定时任务日志信息
	 *
	 * @param jobLogId 任务日志id
	 * @return
	 */
	@GetMapping("/job/log")
	public Result log(@RequestParam Long jobLogId) {
		return Result.ok("请求成功", scheduleJobService.getJobLogById(jobLogId));
	}

	public static ScheduleJob createJob() {
		ScheduleJob jobEntity = new ScheduleJob();
		jobEntity.setBeanName("redisSyncScheduleTask");
		jobEntity.setMethodName("syncBlogViewsToDatabase");
		jobEntity.setParams(null);
		jobEntity.setCron("0/10 * * * * ?");
		jobEntity.setStatus(true);
		jobEntity.setRemark("测试该定时任务的执行");
		jobEntity.setCreateTime(new Date());
		return jobEntity;
	}
}
