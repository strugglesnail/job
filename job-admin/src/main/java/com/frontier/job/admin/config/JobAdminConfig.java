package com.frontier.job.admin.config;

import com.frontier.job.admin.mapper.JobInfoMapper;
import com.frontier.job.admin.mapper.JobServerMapper;
import com.frontier.job.admin.scheduler.JobScheduler;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;


/**
 * xxl-job config
 *
 */

@Component
public class JobAdminConfig implements InitializingBean, DisposableBean {

    private static JobAdminConfig adminConfig = null;
    public static JobAdminConfig getAdminConfig() {
        return adminConfig;
    }

    // conf
    @Value("${xxl.job.accessToken}")
    private String accessToken;
    @Value("${xxl.job.i18n}")
    private String i18n;

    @Resource
    private JobInfoMapper xxlJobInfoDao;
    @Resource
    private JobServerMapper xxlJobGroupDao;


    // ---------------------- JobScheduler ----------------------

    private JobScheduler jobScheduler;

    @Override
    public void afterPropertiesSet() throws Exception {
        adminConfig = this;

        jobScheduler = new JobScheduler();
        jobScheduler.init();
    }

    public String getI18n() {
        if (!Arrays.asList("zh_CN", "zh_TC", "en").contains(i18n)) {
            return "zh_CN";
        }
        return i18n;
    }

    @Override
    public void destroy() throws Exception {
        jobScheduler.destroy();
    }


    // ---------------------- XxlJobScheduler ----------------------



    public String getAccessToken() {
        return accessToken;
    }


    public JobInfoMapper getJobInfoMapper() {
        return xxlJobInfoDao;
    }

    public JobServerMapper getJobServerMapper() {
        return xxlJobGroupDao;
    }



}
