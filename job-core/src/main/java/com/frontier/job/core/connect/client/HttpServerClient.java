package com.frontier.job.core.connect.client;


import com.frontier.job.core.connect.ServerClient;
import com.frontier.job.core.model.IdleBeatParam;
import com.frontier.job.core.model.KillParam;
import com.frontier.job.core.model.ReturnT;
import com.frontier.job.core.model.TriggerParam;
import com.frontier.job.core.util.JobRemotingUtil;

/**
 * admin api test
 *
 *
 */
public class HttpServerClient implements ServerClient {

    public HttpServerClient() {
    }
    public HttpServerClient(String addressUrl, String accessToken) {
        this.addressUrl = addressUrl;
        this.accessToken = accessToken;

        // valid
        if (!this.addressUrl.endsWith("/")) {
            this.addressUrl = this.addressUrl + "/";
        }
    }

    private String addressUrl ;
    private String accessToken;
    private int timeout = 3;


    @Override
    public ReturnT<String> beat() {
        return JobRemotingUtil.postBody(addressUrl+"beat", accessToken, timeout, "", String.class);
    }

    @Override
    public ReturnT<String> idleBeat(IdleBeatParam idleBeatParam) {
        return JobRemotingUtil.postBody(addressUrl+"idleBeat", accessToken, timeout, idleBeatParam, String.class);
    }

    @Override
    public ReturnT<String> run(TriggerParam triggerParam) {
        return JobRemotingUtil.postBody(addressUrl + "run", accessToken, timeout, triggerParam, String.class);
    }

    @Override
    public ReturnT<String> add(TriggerParam killParam) {
        return JobRemotingUtil.postBody(addressUrl + "add", accessToken, timeout, killParam, String.class);
    }

    @Override
    public ReturnT<String> update(TriggerParam triggerParam) {
        return JobRemotingUtil.postBody(addressUrl + "update", accessToken, timeout, triggerParam, String.class);
    }

    @Override
    public ReturnT<String> pause(TriggerParam triggerParam) {
        return JobRemotingUtil.postBody(addressUrl + "pause", accessToken, timeout, triggerParam, String.class);
    }

    @Override
    public ReturnT<String> resume(TriggerParam triggerParam) {
        return JobRemotingUtil.postBody(addressUrl + "resume", accessToken, timeout, triggerParam, String.class);
    }

    @Override
    public ReturnT<String> kill(KillParam killParam) {
        return JobRemotingUtil.postBody(addressUrl + "kill", accessToken, timeout, killParam, String.class);
    }



//    @Override
//    public ReturnT<LogResult> log(LogParam logParam) {
//        return XxlJobRemotingUtil.postBody(addressUrl + "log", accessToken, timeout, logParam, LogResult.class);
//    }

}
