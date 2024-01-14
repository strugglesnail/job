package com.frontier.job.core.quartz.scheduler;

import com.frontier.job.core.exception.TaskException;
import com.frontier.job.core.quartz.job.QuartzDisallowConcurrentExecution;
import com.frontier.job.core.quartz.job.QuartzJobExecution;
import com.frontier.job.core.quartz.model.JobInfo;
import com.frontier.job.core.util.CronUtils;
import org.quartz.*;

import java.util.List;

/**
 * 定时任务工具类
 *
 * @author fendou
 */
public class JobScheduler {


    // 创建定时任务
    public static void createScheduleJob(Scheduler scheduler, JobInfo jobInfo) throws SchedulerException, TaskException {

        String cronExpression = jobInfo.getCronExpression();
        String misfirePolicy = jobInfo.getMisfirePolicy();
        String concurrent = jobInfo.getConcurrent();
        JobKey jobKey = getJobKey(jobInfo);

        // 如果存在，则删除, 防止创建时存在数据问题 先移除，然后在执行创建操作
        if (scheduler.checkExists(jobKey)) {
            scheduler.deleteJob(jobKey);
        }

        // 任务调度方式
        Class<? extends Job> jobClass = getQuartzJobClass(concurrent);

        // 构建job信息
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobKey).build();

        // 表达式调度构建器
        CronScheduleBuilder cronScheduleBuilder =  handleCronScheduleMisfirePolicy(misfirePolicy, cronExpression);

        // 按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity(getTriggerKey(jobInfo))
                .withSchedule(cronScheduleBuilder).build();

        // 放入参数，运行时的方法可以获取
        jobDetail.getJobDataMap().put(ScheduleConstants.TASK_PROPERTIES, jobInfo);

        // 判断任务是否过期, 执行调度任务
        if (CronUtils.getNextExecution(cronExpression) != null) {
            scheduler.scheduleJob(jobDetail, trigger);
        }

        // 暂停任务
        if (ScheduleConstants.Status.PAUSE.getValue().equals(jobInfo.getStatus())) {
            scheduler.pauseJob(jobKey);
        }
    }

    // 触发一次调度
    public static void triggerJob(Scheduler scheduler, JobInfo jobInfo) {
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, jobInfo);
        JobKey jobKey = getJobKey(jobInfo);
        try {
            createScheduleJob(scheduler, jobInfo);
            scheduler.triggerJob(jobKey, dataMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 更新调度任务
    public static void updateJob(Scheduler scheduler, JobInfo jobInfo) {
        try {
            createScheduleJob(scheduler, jobInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 删除调度任务
    public static void deleteJob(Scheduler scheduler, JobInfo jobInfo) {
        JobKey jobKey = getJobKey(jobInfo);
        try {
            if (scheduler.checkExists(jobKey)) {
                scheduler.deleteJob(jobKey);
            }
//            Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 暂停调度任务
    public static void pauseJob(Scheduler scheduler, JobInfo jobInfo) {
        JobKey jobKey = getJobKey(jobInfo);
        try {
            scheduler.pauseJob(jobKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 恢复调度任务
    public static void resumeJob(Scheduler scheduler, JobInfo jobInfo) {
        JobKey jobKey = getJobKey(jobInfo);
        try {
            scheduler.resumeJob(jobKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 获取所有任务的状态
    public static void jobStatusList(Scheduler scheduler, JobInfo jobInfo) {
        try {
            List<JobExecutionContext> currentlyExecutingJobs = scheduler.getCurrentlyExecutingJobs();
            for (JobExecutionContext executingJob : currentlyExecutingJobs) {
                JobKey jobKey = executingJob.getTrigger().getJobKey();
                System.out.println(jobKey.getName().concat("_").concat(jobKey.getGroup()));
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置定时任务策略
     */
    public static CronScheduleBuilder handleCronScheduleMisfirePolicy(String misfirePolicy, String cronExpression) throws TaskException {
        CronScheduleBuilder cb = CronScheduleBuilder.cronSchedule(cronExpression);
        switch (misfirePolicy) {
            case ScheduleConstants.MISFIRE_IGNORE_MISFIRES:
                return cb.withMisfireHandlingInstructionIgnoreMisfires();
            case ScheduleConstants.MISFIRE_FIRE_AND_PROCEED:
                return cb.withMisfireHandlingInstructionFireAndProceed();
            case ScheduleConstants.MISFIRE_DO_NOTHING:
                return cb.withMisfireHandlingInstructionDoNothing();
            default:
                return cb;
        }
    }

    /**
     * 得到quartz任务类
     *
     * @param concurrent 并发状态
     * @return 具体执行任务类
     */
    private static Class<? extends Job> getQuartzJobClass(String concurrent) {
        boolean isConcurrent = "0".equals(concurrent);
        return isConcurrent ? QuartzJobExecution.class : QuartzDisallowConcurrentExecution.class;
    }

    /**
     * 构建任务触发对象
     */
    public static TriggerKey getTriggerKey(JobInfo jobInfo) {
        int jobId = jobInfo.getJobId();
        String jobGroup = jobInfo.getJobGroup();
        return TriggerKey.triggerKey(ScheduleConstants.TASK_CLASS_NAME + jobId, jobGroup);
    }

    /**
     * 构建任务键对象
     */
    public static JobKey getJobKey(JobInfo jobInfo) {
        int jobId = jobInfo.getJobId();
        String jobGroup = jobInfo.getJobGroup();
        return JobKey.jobKey(ScheduleConstants.TASK_CLASS_NAME + jobId, jobGroup);
    }

}
