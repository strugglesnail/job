package com.frontier.demo.config;


import com.frontier.demo.jobhandler.SampleJob;
import com.frontier.job.core.executor.SimpleExecutor;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author fendou
 */
@Component
public class JobConfig implements InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(JobConfig.class);


    private static JobConfig instance = new JobConfig();

    public static JobConfig getInstance() {
        return instance;
    }

    @Autowired
    private Scheduler scheduler;


    private SimpleExecutor jobExecutor = null;



    @Override
    public void afterPropertiesSet() throws Exception {
        initJobExecutor();
    }


    /**
     * init
     */
    public void initJobExecutor() {
        if (scheduler == null) {
            throw new IllegalArgumentException("scheduler do not init！");
        }
        // load executor prop
        Properties jobProp = loadProperties("job-executor.properties");

        // init executor
        jobExecutor = new SimpleExecutor();
        jobExecutor.setScheduler(scheduler);
        jobExecutor.setAdminAddresses(jobProp.getProperty("job.admin.addresses"));
        jobExecutor.setAccessToken(jobProp.getProperty("job.accessToken"));
        jobExecutor.setAppName(jobProp.getProperty("job.executor.appName"));
        jobExecutor.setAddress(jobProp.getProperty("job.executor.address"));
        jobExecutor.setIp(jobProp.getProperty("job.executor.ip"));
        jobExecutor.setPort(Integer.valueOf(jobProp.getProperty("job.executor.port")));

        // registry job bean
        jobExecutor.setJobBeanList(Arrays.asList(new SampleJob()));

        // start executor
        try {
            jobExecutor.start();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }



    /**
     * destroy
     */
    public void destroyJobExecutor() {
        if (jobExecutor != null) {
            jobExecutor.destroy();
        }
    }


    public static Properties loadProperties(String propertyFileName) {
        InputStreamReader in = null;
        try {
            ClassLoader loder = Thread.currentThread().getContextClassLoader();

            in = new InputStreamReader(loder.getResourceAsStream(propertyFileName), "UTF-8");;
            if (in != null) {
                Properties prop = new Properties();
                prop.load(in);
                return prop;
            }
        } catch (IOException e) {
            logger.error("load {} error!", propertyFileName);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("close {} error!", propertyFileName);
                }
            }
        }
        return null;
    }

}
