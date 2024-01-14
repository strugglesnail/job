package com.frontier.job.admin.thread;


import com.frontier.job.admin.config.JobAdminConfig;
import com.frontier.job.admin.mapper.JobServerMapper;
import com.frontier.job.admin.model.JobServer;
import com.frontier.job.core.connect.ServerClient;
import com.frontier.job.core.connect.client.HttpServerClient;
import com.frontier.job.core.model.ReturnT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

// 任务检测器
public class JobMonitorHelper {

    private static Logger logger = LoggerFactory.getLogger(JobMonitorHelper.class);

    private static JobMonitorHelper instance = new JobMonitorHelper();
    public static JobMonitorHelper getInstance(){
        return instance;
    }

    private Thread monitorThread;
    private volatile boolean toStop = false;

    public void start() {
        JobServerMapper jobServerMapper = JobAdminConfig.getAdminConfig().getJobServerMapper();
        monitorThread = new Thread(() -> {
            while (!toStop) {
                List<JobServer> list = JobAdminConfig.getAdminConfig().getJobServerMapper().list(new JobServer());
                for (JobServer server : list) {
                    String addressList = server.getAddressList();
                    ServerClient serverClient = new HttpServerClient(addressList, null);
                    // 心跳
                    beat(jobServerMapper, server, addressList, serverClient);
                    // 任务心跳
                    idleBeat(jobServerMapper, server, addressList, serverClient);

                }


                // 2. 更新服务端信息

                // 睡眠时长
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (Exception e) {
                    if (!toStop) {
                        logger.error(e.getMessage(), e);
                    }
                }

//                logger.info("job monitor thread stop");
            }
        });

        monitorThread.setDaemon(true);
        monitorThread.setName("admin JobMonitorHelper");
        monitorThread.start();
    }

    private void idleBeat(JobServerMapper jobServerMapper, JobServer server, String addressList, ServerClient serverClient) {
        try {
            ReturnT<String> idleBeatResult = serverClient.idleBeat(null);
            if (idleBeatResult.getCode() == ReturnT.SUCCESS_CODE) {
                // 实时更新任务状态
                logger.info("job server " + addressList + " status.");
            }
        } catch (Exception e) {
            if (!toStop) {

            }
        }
    }

    private void beat(JobServerMapper jobServerMapper, JobServer server, String addressList, ServerClient serverClient) {
        try {
            // 一些心跳
            // 1. 判断服务端是否还活着
            ReturnT<String> beatResult = serverClient.beat();
            if (beatResult.getCode() == ReturnT.SUCCESS_CODE) {
                logger.info("job server " + addressList + " running.");
                jobServerMapper.updateStatus(server.getId(), (byte) 1);
            } else {
                // 更新服务状态
                jobServerMapper.updateStatus(server.getId(), (byte) 0);
            }
        } catch (Exception e) {
            if (!toStop) {
                logger.error("job server " + addressList + " shutdown:{}", e);
                // 更新服务状态
                jobServerMapper.updateStatus(server.getId(), (byte) 0);
            }
        }
    }

    public void toStop() {
        toStop = true;
        // interrupt and wait
        monitorThread.interrupt();
        try {
            monitorThread.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
