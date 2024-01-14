package com.frontier.job.core.model;

import java.io.Serializable;

/**
 * Created by x
 */
public class TriggerParam implements Serializable {
    private static final long serialVersionUID = 42L;

    private int jobId;

    //任务名称
    private String jobName;

    //任务组名
    private String jobGroup;

    //执行器任务handler
    private String executorHandler;

    //执行器任务参数
    private String executorParams;

    //cron执行表达式
    private String cronExpression;

    //计划执行错误策略（1立即执行 2执行一次 3放弃执行）
    private String misfirePolicy;

    //是否并发执行（0允许 1禁止）
    private String concurrent;

    //状态（0正常 1暂停）
    private String status;

    //    private String executorBlockStrategy;
    private int executorTimeout;

//    private long logId;
//    private long logDateTime;
//
//    private String glueType;
//    private String glueSource;
//    private long glueUpdatetime;

//    private int broadcastIndex;
//    private int broadcastTotal;


    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getExecutorHandler() {
        return executorHandler;
    }

    public void setExecutorHandler(String executorHandler) {
        this.executorHandler = executorHandler;
    }

    public String getExecutorParams() {
        return executorParams;
    }

    public void setExecutorParams(String executorParams) {
        this.executorParams = executorParams;
    }

    public int getExecutorTimeout() {
        return executorTimeout;
    }

    public void setExecutorTimeout(int executorTimeout) {
        this.executorTimeout = executorTimeout;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getMisfirePolicy() {
        return misfirePolicy;
    }

    public void setMisfirePolicy(String misfirePolicy) {
        this.misfirePolicy = misfirePolicy;
    }

    public String getConcurrent() {
        return concurrent;
    }

    public void setConcurrent(String concurrent) {
        this.concurrent = concurrent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TriggerParam{" +
                "jobId=" + jobId +
                ", executorHandler='" + executorHandler + '\'' +
                ", executorParams='" + executorParams + '\'' +
                ", executorTimeout=" + executorTimeout +
                '}';
    }

}
