package com.frontier.job.core.connect.client;

import com.frontier.job.core.connect.AdminClient;
import com.frontier.job.core.model.RegistryParam;
import com.frontier.job.core.model.ReturnT;
import com.frontier.job.core.util.JobRemotingUtil;

public class HttpAdminClient implements AdminClient {


    private String addressUrl ;
    private String accessToken;
    private int timeout = 3;

    public HttpAdminClient(String addressUrl, String accessToken) {
        this.addressUrl = addressUrl;
        this.accessToken = accessToken;
    }

    @Override
    public ReturnT<String> registry(RegistryParam registryParam) {
        ReturnT returnT = JobRemotingUtil.postBody(addressUrl +"/api/registry", accessToken, timeout, registryParam, String.class);
        return returnT;
    }

    @Override
    public ReturnT<String> registryRemove(RegistryParam registryParam) {
        return null;
    }
}
