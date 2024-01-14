package com.frontier.job.admin.service;

import com.frontier.job.admin.thread.JobRegistryHelper;
import com.frontier.job.core.connect.AdminClient;
import com.frontier.job.core.model.RegistryParam;
import com.frontier.job.core.model.ReturnT;
import org.springframework.stereotype.Component;

@Component
public class AdminClientImpl implements AdminClient {


    // 调度服务及任务列表入库
    @Override
    public ReturnT<String> registry(RegistryParam registryParam) {
        return JobRegistryHelper.getInstance().registry(registryParam);
    }

    @Override
    public ReturnT<String> registryRemove(RegistryParam registryParam) {
        return JobRegistryHelper.getInstance().registryRemove(registryParam);
    }
}
