package top.naccl.service;

import top.naccl.entity.ScheduleJob;
import top.naccl.entity.ScheduleJobLog;

import java.util.List;

public interface ScheduleJobService {
	List<ScheduleJob> getJobList();

	void saveJob(ScheduleJob scheduleJob);

	void updateJob(ScheduleJob scheduleJob);

	void deleteJobById(Long jobId);

	void runJobById(Long jobId);

	void updateJobStatusById(Long jobId, Boolean status);

	List<ScheduleJobLog> getJobLogList();

	void saveJobLog(ScheduleJobLog log);

	void deleteJobLogByLogId(Long logId);
}
