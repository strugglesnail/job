package com.frontier.job.core.model;

import java.io.Serializable;
import java.util.List;

/**
 * 注册的服务参数
 */
public class RegistryParam implements Serializable {

    // 服务名称: demo
    private String serverName;

    // 服务地址：http://localhost:9999
    private String serverAddress;

    // 任务列表
    private List<String> jobHandlerNameList;

    public RegistryParam() {

    }

    public RegistryParam(String serverName, String serverAddress, List<String> jobHandlerNameList) {
        this.serverName = serverName;
        this.serverAddress = serverAddress;
        this.jobHandlerNameList = jobHandlerNameList;
    }


    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public List<String> getJobHandlerNameList() {
        return jobHandlerNameList;
    }

    public void setJobHandlerNameList(List<String> jobHandlerNameList) {
        this.jobHandlerNameList = jobHandlerNameList;
    }
}
