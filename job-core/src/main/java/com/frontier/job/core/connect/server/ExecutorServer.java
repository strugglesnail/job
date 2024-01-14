package com.frontier.job.core.connect.server;

import com.frontier.job.core.connect.ServerClient;
import com.frontier.job.core.model.IdleBeatParam;
import com.frontier.job.core.model.KillParam;
import com.frontier.job.core.model.ReturnT;
import com.frontier.job.core.model.TriggerParam;
import com.frontier.job.core.quartz.model.JobInfo;
import com.frontier.job.core.quartz.scheduler.JobScheduler;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author wang_tengfei
 */
public class ExecutorServer implements ServerClient {

    private static Logger logger = LoggerFactory.getLogger(ExecutorServer.class);

    private final Scheduler scheduler;

    public ExecutorServer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    // 心跳：判断此服务是否运行
    @Override
    public ReturnT<String> beat() {
        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<String> idleBeat(IdleBeatParam idleBeatParam) {
        // 获取所有任务的状态
        JobScheduler.jobStatusList(scheduler, null);
        // isRunningOrHasQueue
//        boolean isRunningOrHasQueue = false;
//        JobThread jobThread = XxlJobExecutor.loadJobThread(idleBeatParam.getJobId());
//        if (jobThread != null && jobThread.isRunningOrHasQueue()) {
//            isRunningOrHasQueue = true;
//        }
//
//        if (isRunningOrHasQueue) {
//            return new ReturnT<>(ReturnT.FAIL_CODE, "job thread is running or has trigger queue.");
//        }
        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<String> run(TriggerParam triggerParam) {
        JobInfo jobInfo = getJobInfo(triggerParam);
        System.out.println(jobInfo);
        JobScheduler.triggerJob(scheduler, jobInfo);
        return ReturnT.SUCCESS;
    }



    @Override
    public ReturnT<String> update(TriggerParam triggerParam) {
        JobInfo jobInfo = getJobInfo(triggerParam);
        JobScheduler.updateJob(scheduler, jobInfo);
        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<String> pause(TriggerParam triggerParam) {
        JobInfo jobInfo = getJobInfo(triggerParam);
        JobScheduler.pauseJob(scheduler, jobInfo);
        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<String> resume(TriggerParam triggerParam) {
        JobInfo jobInfo = getJobInfo(triggerParam);
        JobScheduler.resumeJob(scheduler, jobInfo);
        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<String> kill(KillParam killParam) {
//        // kill handlerThread, and create new one
//        JobThread jobThread = XxlJobExecutor.loadJobThread(killParam.getJobId());
//        if (jobThread != null) {
//            XxlJobExecutor.removeJobThread(killParam.getJobId(), "scheduling center kill job.");
//            return ReturnT.SUCCESS;
//        }

        return new ReturnT<>(ReturnT.SUCCESS_CODE, "job already killed.");
    }

    @Override
    public ReturnT<String> add(TriggerParam triggerParam) {
        JobInfo jobInfo = new JobInfo();
        jobInfo.setJobId(triggerParam.getJobId());
        jobInfo.setJobGroup(triggerParam.getJobGroup());
        jobInfo.setJobName(triggerParam.getJobName());
        jobInfo.setStatus(triggerParam.getStatus());
        jobInfo.setConcurrent(triggerParam.getConcurrent());
        jobInfo.setCronExpression(triggerParam.getCronExpression());
        try {
            JobScheduler.createScheduleJob(scheduler, jobInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }

    //    @Override
//    public ReturnT<LogResult> log(LogParam logParam) {
//        // log filename: logPath/yyyy-MM-dd/9999.log
//        String logFileName = XxlJobFileAppender.makeLogFileName(new Date(logParam.getLogDateTim()), logParam.getLogId());
//
//        LogResult logResult = XxlJobFileAppender.readLog(logFileName, logParam.getFromLineNum());
//        return new ReturnT<LogResult>(logResult);
//    }

    private static JobInfo getJobInfo(TriggerParam triggerParam) {
        JobInfo jobInfo = new JobInfo();
        jobInfo.setJobId(triggerParam.getJobId());
        jobInfo.setJobGroup(triggerParam.getJobGroup());
        jobInfo.setJobName(triggerParam.getJobName());
        jobInfo.setExecutorHandler(triggerParam.getExecutorHandler());
        jobInfo.setCronExpression(triggerParam.getCronExpression());
        jobInfo.setMisfirePolicy(triggerParam.getMisfirePolicy());
        jobInfo.setConcurrent(triggerParam.getConcurrent());
        jobInfo.setStatus(triggerParam.getStatus());
        return jobInfo;
    }
}
