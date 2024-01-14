package com.frontier.job.core.executor;

import org.quartz.Scheduler;

import java.lang.reflect.Method;
import java.util.List;

public class SimpleExecutor extends AbstractExecutor {


    private List<Object> jobBeanList;


    @Override
    public void start() {
        initJobHandlerMethodRepository(jobBeanList);
        super.start();
    }



    private void initJobHandlerMethodRepository(List<Object> jobBeanList) {
        if (jobBeanList == null || jobBeanList.size() == 0) {
            return;
        }

        // init job handler from method
        for (Object bean: jobBeanList) {
            // method
            Method[] methods = bean.getClass().getDeclaredMethods();
            if (methods.length == 0) {
                continue;
            }

            addJobHandler(bean);
        }
    }

    public List<Object> getJobBeanList() {
        return jobBeanList;
    }

    public void setJobBeanList(List<Object> jobBeanList) {
        this.jobBeanList = jobBeanList;
    }

    @Override
    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }


}
