package com.frontier.job.core.quartz.job;

import com.frontier.job.core.quartz.model.JobInfo;
import org.quartz.DisallowConcurrentExecution;

/**
 * 定时任务处理（禁止并发执行）
 * 
 * @author
 *
 */
@DisallowConcurrentExecution
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobInfo jobInfo) throws Exception {
        super.doExecute(jobInfo);
        System.out.println("禁用并发了哦");
    }
}
