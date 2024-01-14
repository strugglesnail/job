package com.frontier.job.core.executor;


import com.frontier.job.core.model.RegistryParam;
import com.frontier.job.core.connect.AdminClient;
import com.frontier.job.core.connect.client.HttpAdminClient;
import com.frontier.job.core.exception.JobException;
import com.frontier.job.core.handler.Job;
import com.frontier.job.core.handler.JobHandler;
import com.frontier.job.core.handler.MethodJobHandler;
import com.frontier.job.core.model.ReturnT;
import com.frontier.job.core.server.EmbedServer;
import com.frontier.job.core.util.IpUtil;
import com.frontier.job.core.util.NetUtil;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * 任务执行器模板
 */
public abstract class AbstractExecutor implements Executor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractExecutor.class);


    // 客户端地址
    private String adminAddresses;

    private String accessToken;

    // 服务名称：调度器服务名称
    private String appName;

    // 服务器地址
    private String address;

    // 服务Ip
    private String ip;

    // 服务端口
    private int port;

    private EmbedServer embedServer = null;

    // 调度器
    protected Scheduler scheduler;

    // 任务处理器仓库
    public static ConcurrentMap<String, JobHandler> jobHandlerRepository = new ConcurrentHashMap();


    @Override
    public void start() {
        // 1. 注册该服务到客户端
        initServer(adminAddresses, accessToken);

        // 2. callback 需要从客户端获取调度任务信息
        // 定时获取


        // 3. 启动Netty服务：接收客户端请求主要用于调度业务定时任务
        startServer();
    }

    // 连接客户端：回调客户端，由客户端加入任务调度
    private void initServer(String adminAddresses, String accessToken) {
        List<String> jobHandlerNames = jobHandlerRepository.entrySet().stream().map(job -> job.getKey()).collect(Collectors.toList());
        String serverAddress = "http://" + ip + ":" + port;
        if (adminAddresses != null && adminAddresses.trim().length() > 0) {
            for (String address : adminAddresses.trim().split(",")) {
                if (StringUtils.isNotBlank(address)) {
                    // 1. 注册当前服务、调度任务
                    RegistryParam registryParam = new RegistryParam(appName, serverAddress, jobHandlerNames);
                    AdminClient adminClient = new HttpAdminClient(address, accessToken);
                    ReturnT<String> returnT = adminClient.registry(registryParam);
                    if (returnT.getCode() == ReturnT.SUCCESS_CODE) {
                        logger.info(appName + ": 注册成功！");
                    } else {
                        logger.error(appName + ": 注册失败！");
                    }
                }
            }
        }
    }

    private void startServer() {
        // fill ip port
        port = port > 0 ? port : NetUtil.findAvailablePort(9999);
        ip = (ip != null && ip.trim().length() > 0) ? ip : IpUtil.getIp();

        // generate address
        if (address == null || address.trim().length() == 0) {
            String ip_port_address = IpUtil.getIpPort(ip, port);   // registry-address：default use address to registry , otherwise use ip:port if address is null
            address = "http://{ip_port}/".replace("{ip_port}", ip_port_address);
        }

        // accessToken
        if (accessToken == null || accessToken.trim().length() == 0) {
            logger.warn("job accessToken is empty. To ensure system security, please set the accessToken.");
        }

        // start
        embedServer = new EmbedServer();
        embedServer.start(address, port, appName, accessToken, getScheduler());
    }


    // 以@Job注解的方式注册
    protected void addJobHandler(Object bean) {
        Class<?> clazz = bean.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Job job = method.getDeclaredAnnotation(Job.class);
            if (job == null) {
                continue;
            }

            String jobHandlerName = null;
            if (StringUtils.isNotBlank(job.value())) {
                jobHandlerName = job.value();
            }

            if (getJobHandler(jobHandlerName) != null) {
                throw new JobException("job jobHandler[" + jobHandlerName + "] naming conflicts.");
            }
            MethodJobHandler methodJobHandler = new MethodJobHandler(bean, method);
            registerJobHandler(jobHandlerName, methodJobHandler);
        }
    }

    public Scheduler getScheduler() {
        return this.scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public static JobHandler registerJobHandler(String name, JobHandler jobHandler) {
        logger.info("job register jobHandler success, name:{}, jobHandler:{}", name, jobHandler);
        return jobHandlerRepository.put(name, jobHandler);
    }

    public static JobHandler getJobHandler(String name) {
        return jobHandlerRepository.get(name);
    }

    @Override
    public void destroy() {

    }

    public String getAdminAddresses() {
        return adminAddresses;
    }

    public void setAdminAddresses(String adminAddresses) {
        this.adminAddresses = adminAddresses;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
