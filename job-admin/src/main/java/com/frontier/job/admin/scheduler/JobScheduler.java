package com.frontier.job.admin.scheduler;

import com.frontier.job.admin.thread.JobMonitorHelper;
import com.frontier.job.admin.thread.JobRegistryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */

public class JobScheduler {
    private static final Logger logger = LoggerFactory.getLogger(JobScheduler.class);


    public void init() throws Exception {
        // init i18n

        // admin trigger pool start
//        JobTriggerPoolHelper.toStart();

        // admin registry monitor run
        JobRegistryHelper.getInstance().start();

        // admin monitor run
        JobMonitorHelper.getInstance().start();

        // admin lose-monitor run ( depend on JobTriggerPoolHelper )
//        JobCompleteHelper.getInstance().start();

        // admin log report start
//        JobLogReportHelper.getInstance().start();

        // start-schedule  ( depend on JobTriggerPoolHelper )
//        JobScheduleHelper.getInstance().start();

        logger.info("init job admin success.");
    }


    public void destroy() throws Exception {

//        // stop-schedule
//        JobScheduleHelper.getInstance().toStop();
//
//        // admin log report stop
//        JobLogReportHelper.getInstance().toStop();
//
//        // admin lose-monitor stop
//        JobCompleteHelper.getInstance().toStop();
//
//        // admin fail-monitor stop
        JobMonitorHelper.getInstance().toStop();
//
//        // admin registry stop
//        JobRegistryHelper.getInstance().toStop();
//
//        // admin trigger pool stop
//        JobTriggerPoolHelper.toStop();

    }

    // ---------------------- executor-client ----------------------
//    private static ConcurrentMap<String, ServerClient> serverClientRepository = new ConcurrentHashMap<String, ServerClient>();
//    public static ServerClient getServerClient(String address) throws Exception {
//        // valid
//        if (address==null || address.trim().length()==0) {
//            return null;
//        }
//
//        // load-cache
//        address = address.trim();
//        ServerClient executorBiz = serverClientRepository.get(address);
//        if (executorBiz != null) {
//            return executorBiz;
//        }
//
//        // set-cache
//        executorBiz = new HttpServerClient(address, JobAdminConfig.getAdminConfig().getAccessToken());
//
//        serverClientRepository.put(address, executorBiz);
//        return executorBiz;
//    }

}
