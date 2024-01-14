package com.frontier.job.admin.thread;

import com.frontier.job.admin.mapper.JobInfoMapper;
import com.frontier.job.admin.mapper.JobServerMapper;
import com.frontier.job.admin.model.JobInfo;
import com.frontier.job.admin.model.JobServer;
import com.frontier.job.core.model.RegistryParam;
import com.frontier.job.core.model.ReturnT;
import com.frontier.job.admin.config.JobAdminConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.*;

/**
 * job registry instance
 */
public class JobRegistryHelper {

    private static Logger logger = LoggerFactory.getLogger(JobRegistryHelper.class);

    private static JobRegistryHelper instance = new JobRegistryHelper();

    public static JobRegistryHelper getInstance() { return instance; }

    private ThreadPoolExecutor registryOrRemoveThreadPool = null;

//    private Thread registryMonitorThread;

//    private volatile boolean toStop = false;
    // 初始化注册器线程池
    public void start() {

        // for registry or remove
        registryOrRemoveThreadPool = new ThreadPoolExecutor(
                2,
                10,
                30L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(2000),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "job, admin JobRegistryMonitorHelper-registryOrRemoveThreadPool-" + r.hashCode());
                    }
                },
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        r.run();
                        logger.warn("job, registry or remove too fast, match threadPool rejected handler(run now).");
                    }
                });

        // for monitor
    }

    // 对应服务端请求实现：注册指定服务端的信息及任务信息
    public ReturnT<String> registry(RegistryParam registryParam) {
        // valid
        if (!StringUtils.hasText(registryParam.getServerName()) || !StringUtils.hasText(registryParam.getServerAddress())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "Illegal Argument.");
        }

        // async execute
        registryOrRemoveThreadPool.execute(() -> {
            JobServerMapper jobServerMapper = JobAdminConfig.getAdminConfig().getJobServerMapper();
            JobInfoMapper jobInfoMapper = JobAdminConfig.getAdminConfig().getJobInfoMapper();
            JobServer server = new JobServer();
            server.setAppName(registryParam.getServerName());
            server.setAddressList(registryParam.getServerAddress());

            System.out.println(server);
            // 更新服务信息
            int ret = jobServerMapper.update(server);
            if (ret < 1) {
                jobServerMapper.save(server);
            } else {
                server = jobServerMapper.getOne(server);
            }
            // 更新任务信息
            List<String> jobHandlerNameList = registryParam.getJobHandlerNameList();
            if (jobHandlerNameList != null && jobHandlerNameList.size() > 0) {
                for (String jobHandlerName : jobHandlerNameList) {
                    JobInfo jobInfo = new JobInfo();
                    jobInfo.setJobServerId(server.getId());
                    jobInfo.setJobGroup(registryParam.getServerName());
                    jobInfo.setExecutorHandler(jobHandlerName);


                    int up = jobInfoMapper.update(jobInfo);
                    if (up < 1) {
                        jobInfo.setJobName(null);
                        jobInfo.setConcurrent("0");
                        jobInfo.setCronExpression("*/5 * * * * ? ");
                        jobInfoMapper.save(jobInfo);
                    }

                }
            }
        });
        return ReturnT.SUCCESS;
    }

	public ReturnT<String> registryRemove(RegistryParam registryParam) {

		// valid
//		if (!StringUtils.hasText(registryParam.getServerName())
//				|| !StringUtils.hasText(registryParam.getServerAddress())) {
//			return new ReturnT<String>(ReturnT.FAIL_CODE, "Illegal Argument.");
//		}
//
//		// async execute
//		registryOrRemoveThreadPool.execute(new Runnable() {
//			@Override
//			public void run() {
//				int ret = JobAdminConfig.getAdminConfig().getJobServerMapper().delete(registryParam.getServerAddress());
//				if (ret > 0) {
//					// fresh
////					freshGroupRegistryInfo(registryParam);
//				}
//			}
//		});

		return ReturnT.SUCCESS;
	}


}
