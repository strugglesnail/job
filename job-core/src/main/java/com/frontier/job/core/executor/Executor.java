package com.frontier.job.core.executor;

/**
 * 任务执行器：1.注册客户端 2.启动Netty服务
 */
public interface Executor {

    // 开始
    void start() throws Exception;

    // 结束
    void destroy();

}
