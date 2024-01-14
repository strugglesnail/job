package com.frontier.job.core.quartz.job;

import com.frontier.job.core.quartz.model.JobInfo;

/**
 * 定时任务处理（允许并发执行）
 * 
 * @author wang_tengfei
 *
 */
public class QuartzJobExecution extends AbstractQuartzJob {

    @Override
    protected void doExecute(JobInfo jobInfo) throws Exception {
//        JobInvokeUtil.invokeMethod(jobInfo);
        super.doExecute(jobInfo);
        System.out.println("完成了");
    }
}
