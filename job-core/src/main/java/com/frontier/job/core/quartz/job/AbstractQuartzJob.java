package com.frontier.job.core.quartz.job;


import com.frontier.job.core.executor.SimpleExecutor;
import com.frontier.job.core.handler.JobHandler;
import com.frontier.job.core.quartz.model.JobInfo;
import com.frontier.job.core.quartz.scheduler.ScheduleConstants;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 抽象quartz调用
 *
 * @author
 */
public abstract class AbstractQuartzJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(AbstractQuartzJob.class);

    /**
     * 线程本地变量
     */
    private static ThreadLocal<Date> threadLocal = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobInfo jobInfo = (JobInfo)context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES);
//        BeanUtils.copyBeanProp(sysJob, );
        try {
//            before(context, sysJob);
//            if (sysJob != null) {
                doExecute(jobInfo);
//            }
//            after(context, sysJob, null);
        } catch (Exception e) {
            log.error("任务执行异常  - ：", e);
//            after(context, sysJob, e);
        }
    }

    /**
     * 执行前
     *
     * @param context 工作执行上下文对象
     * @param sysJob 系统计划任务
     */
//    protected void before(JobExecutionContext context, SysJob sysJob) {
//        threadLocal.set(new Date());
//    }

    /**
     * 执行后
     *
     * @param context 工作执行上下文对象
     * @param sysJob 系统计划任务
     */
//    protected void after(JobExecutionContext context, SysJob sysJob, Exception e) {
//        Date startTime = threadLocal.get();
//        threadLocal.remove();
//
//        final SysJobLog sysJobLog = new SysJobLog();
//        sysJobLog.setJobName(sysJob.getJobName());
//        sysJobLog.setJobGroup(sysJob.getJobGroup());
//        sysJobLog.setInvokeTarget(sysJob.getInvokeTarget());
//        sysJobLog.setStartTime(startTime);
//        sysJobLog.setStopTime(new Date());
//        double runMs = (sysJobLog.getStopTime().getTime() - sysJobLog.getStartTime().getTime()) / 1000;
//        sysJobLog.setJobMessage(sysJobLog.getJobName() + " 总共耗时：" + runMs + "秒");
//        if (e != null) {
//            sysJobLog.setStatus(Constants.FAIL);
//            String errorMsg = StringUtils.substring(ExceptionUtil.getExceptionMessage(e), 0, 20000);
//            sysJobLog.setExceptionInfo(errorMsg);
//        } else {
//            sysJobLog.setStatus(Constants.SUCCESS);
//        }
//
//        // 写入数据库当中
//        SpringUtils.getBean(ISysJobLogService.class).addJobLog(sysJobLog);
//    }

    /**
     * 执行方法，由子类重载
     *
//     * @param context 工作执行上下文对象
     * @param jobInfo 系统计划任务
     * @throws Exception 执行过程中的异常
     */
    protected void doExecute(JobInfo jobInfo) throws Exception {
        JobHandler jobHandler = SimpleExecutor.getJobHandler(jobInfo.getExecutorHandler());
        jobHandler.execute();
    }
}
