package com.frontier.job.core.quartz.model;

import java.util.Date;

public class JobInfo {
    //任务ID
    private int jobId;

    //服务主键ID
    private Integer jobServerId;

    //任务名称
    private String jobName;

    //任务组名
    private String jobGroup;

    //执行器任务handler
    private String executorHandler;

    //执行器任务参数
    private String executorParam;

    //cron执行表达式
    private String cronExpression;

    //计划执行错误策略（1立即执行 2执行一次 3放弃执行）
    private String misfirePolicy;

    //是否并发执行（0允许 1禁止）
    private String concurrent;

    //状态（0正常 1暂停）
    private String status;

    //创建者
    private String createBy;

    //创建时间
    private Date createTime;

    //更新者
    private String updateBy;

    //更新时间
    private Date updateTime;

    //备注信息
    private String remark;


    /**
     * 获取服务主键ID
     *
     * @return job_server_id - 服务主键ID
     */
    public Integer getJobServerId() {
        return jobServerId;
    }

    /**
     * 设置服务主键ID
     *
     * @param jobServerId 服务主键ID
     */
    public void setJobServerId(Integer jobServerId) {
        this.jobServerId = jobServerId;
    }

    /**
     * 获取任务名称
     *
     * @return job_name - 任务名称
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * 设置任务名称
     *
     * @param jobName 任务名称
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * 获取任务组名
     *
     * @return job_group - 任务组名
     */
    public String getJobGroup() {
        return jobGroup;
    }

    /**
     * 设置任务组名
     *
     * @param jobGroup 任务组名
     */
    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    /**
     * 获取执行器任务handler
     *
     * @return executor_handler - 执行器任务handler
     */
    public String getExecutorHandler() {
        return executorHandler;
    }

    /**
     * 设置执行器任务handler
     *
     * @param executorHandler 执行器任务handler
     */
    public void setExecutorHandler(String executorHandler) {
        this.executorHandler = executorHandler;
    }

    /**
     * 获取执行器任务参数
     *
     * @return executor_param - 执行器任务参数
     */
    public String getExecutorParam() {
        return executorParam;
    }

    /**
     * 设置执行器任务参数
     *
     * @param executorParam 执行器任务参数
     */
    public void setExecutorParam(String executorParam) {
        this.executorParam = executorParam;
    }

    /**
     * 获取cron执行表达式
     *
     * @return cron_expression - cron执行表达式
     */
    public String getCronExpression() {
        return cronExpression;
    }

    /**
     * 设置cron执行表达式
     *
     * @param cronExpression cron执行表达式
     */
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    /**
     * 获取计划执行错误策略（1立即执行 2执行一次 3放弃执行）
     *
     * @return misfire_policy - 计划执行错误策略（1立即执行 2执行一次 3放弃执行）
     */
    public String getMisfirePolicy() {
        return misfirePolicy;
    }

    /**
     * 设置计划执行错误策略（1立即执行 2执行一次 3放弃执行）
     *
     * @param misfirePolicy 计划执行错误策略（1立即执行 2执行一次 3放弃执行）
     */
    public void setMisfirePolicy(String misfirePolicy) {
        this.misfirePolicy = misfirePolicy;
    }

    /**
     * 获取是否并发执行（0允许 1禁止）
     *
     * @return concurrent - 是否并发执行（0允许 1禁止）
     */
    public String getConcurrent() {
        return concurrent;
    }

    /**
     * 设置是否并发执行（0允许 1禁止）
     *
     * @param concurrent 是否并发执行（0允许 1禁止）
     */
    public void setConcurrent(String concurrent) {
        this.concurrent = concurrent;
    }

    /**
     * 获取状态（0正常 1暂停）
     *
     * @return status - 状态（0正常 1暂停）
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态（0正常 1暂停）
     *
     * @param status 状态（0正常 1暂停）
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取创建者
     *
     * @return create_by - 创建者
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置创建者
     *
     * @param createBy 创建者
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新者
     *
     * @return update_by - 更新者
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置更新者
     *
     * @param updateBy 更新者
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取备注信息
     *
     * @return remark - 备注信息
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注信息
     *
     * @param remark 备注信息
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    @Override
    public String toString() {
        return "JobInfo{" +
                "jobId=" + jobId +
                ", jobServerId=" + jobServerId +
                ", jobName='" + jobName + '\'' +
                ", jobGroup='" + jobGroup + '\'' +
                ", executorHandler='" + executorHandler + '\'' +
                ", executorParam='" + executorParam + '\'' +
                ", cronExpression='" + cronExpression + '\'' +
                ", misfirePolicy='" + misfirePolicy + '\'' +
                ", concurrent='" + concurrent + '\'' +
                ", status='" + status + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createTime=" + createTime +
                ", updateBy='" + updateBy + '\'' +
                ", updateTime=" + updateTime +
                ", remark='" + remark + '\'' +
                '}';
    }
}