package com.frontier.demo.jobhandler;

import com.frontier.job.core.handler.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Job开发示例（Bean模式）
 *
 * 开发步骤：
 *      1、任务开发：在Spring Bean实例中，开发Job方法；
 *      2、注解配置：为Job方法添加注解 "@Job(value="自定义jobhandler名称", init = "JobHandler初始化方法", destroy = "JobHandler销毁方法")"，注解value值对应的是调度中心新建任务的JobHandler属性的值。
 *
 * @author fendou
 */
public class SampleJob {
    private static Logger logger = LoggerFactory.getLogger(SampleJob.class);


    /**
     * 1、简单任务示例（Bean模式）
     */
    @Job("demoJobHandler")
    public void demoJobHandler() throws Exception {
        System.out.println("我是示例^_^");
        // default success
    }





}
