package com.frontier.job.admin.trigger;

import com.frontier.job.admin.config.JobAdminConfig;
import com.frontier.job.admin.model.JobInfo;
import com.frontier.job.admin.model.JobServer;
import com.frontier.job.admin.scheduler.JobScheduler;
import com.frontier.job.admin.util.I18nUtil;
import com.frontier.job.core.connect.ClientFactory;
import com.frontier.job.core.connect.client.HttpServerClient;
import com.frontier.job.core.connect.ServerClient;
import com.frontier.job.core.model.ReturnT;
import com.frontier.job.core.model.TriggerParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * xxl-job trigger
 * Created by x.
 */
public class JobTrigger {
    private static Logger logger = LoggerFactory.getLogger(JobTrigger.class);

    /**
     * trigger job
     *
     * @param jobId
     * 			>=0: use this param
     * 			<0: use param from job info config
     * @param executorParam
     *          null: use job param
     *          not null: cover job param
     * @param addressList
     *          null: use executor addressList
     *          not null: cover
     */
    // 统一规范化调度
    public static ReturnT<String> trigger(int jobId, String executorParam, String addressList) {

        // load data
        JobInfo jobInfo = JobAdminConfig.getAdminConfig().getJobInfoMapper().getById((long) jobId);
        if (jobInfo == null) {
            logger.warn("trigger fail, jobId invalid，jobId={}", jobId);
            return null;
        }
        if (executorParam != null) {
            jobInfo.setExecutorParam(executorParam);
        }

        JobServer jobServer = new JobServer();
        jobServer.setId(jobInfo.getJobServerId());
        jobServer.setAppName(jobInfo.getJobGroup());
        JobServer group = JobAdminConfig.getAdminConfig().getJobServerMapper().getOne(jobServer);

        // cover addressList
        if (addressList!=null && addressList.trim().length()>0) {
//            group.setAddressType(1);
            group.setAddressList(addressList.trim());
        }

        return processTrigger(group, jobInfo);

    }


    /**
     * @param group                     job group, registry list may be empty
     * @param jobInfo
     */
    private static ReturnT<String> processTrigger(JobServer group, JobInfo jobInfo){

        // param

        // 2、init trigger-param
        TriggerParam triggerParam = new TriggerParam();
        triggerParam.setJobId(jobInfo.getId());
        triggerParam.setJobName(jobInfo.getJobName());
        triggerParam.setJobGroup(jobInfo.getJobGroup());
        triggerParam.setExecutorHandler(jobInfo.getExecutorHandler());
        triggerParam.setExecutorParams(jobInfo.getExecutorParam());
        triggerParam.setCronExpression(jobInfo.getCronExpression());
        triggerParam.setMisfirePolicy(jobInfo.getMisfirePolicy());
        triggerParam.setStatus(jobInfo.getStatus());
        triggerParam.setConcurrent(jobInfo.getConcurrent());
//        triggerParam.setExecutorTimeout(jobInfo.getExecutorTimeout());

        // 3、init address
        String address = null;
        if (group.getAddressList()!=null && !group.getAddressList().isEmpty()) {
            address = group.getAddressList();
        }

        // 4、trigger remote executor
        ReturnT<String> triggerResult = null;
        if (address != null) {
            runExecutor(triggerParam, address);
        } else {
            triggerResult = new ReturnT<>(ReturnT.FAIL_CODE, null);
        }

        return triggerResult;
        // 5、collection trigger info

    }

    /**
     * run executor
     * @param triggerParam
     * @param address
     * @return
     */
    public static ReturnT<String> runExecutor(TriggerParam triggerParam, String address){
        ReturnT<String> runResult;
        try {
            ServerClient serverClient = ClientFactory.getServerClient(address, JobAdminConfig.getAdminConfig().getAccessToken());
            runResult = serverClient.run(triggerParam);
        } catch (Exception e) {
            logger.error("job trigger error, please check if the executor[{}] is running.", address, e);
            runResult = new ReturnT<>(ReturnT.FAIL_CODE, e.toString());
        }

        StringBuffer runResultSB = new StringBuffer(I18nUtil.getString("jobconf_trigger_run") + "：");
        runResultSB.append("<br>address：").append(address);
        runResultSB.append("<br>code：").append(runResult.getCode());
        runResultSB.append("<br>msg：").append(runResult.getMsg());

        runResult.setMsg(runResultSB.toString());
        return runResult;
    }



}
