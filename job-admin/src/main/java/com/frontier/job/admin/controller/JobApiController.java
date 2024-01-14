package com.frontier.job.admin.controller;

import com.frontier.job.core.connect.AdminClient;
import com.frontier.job.core.model.RegistryParam;
import com.frontier.job.core.model.ReturnT;
import com.frontier.job.core.util.GsonTool;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 */
@RestController
@RequestMapping("/api")
public class JobApiController {

    @Resource
    private AdminClient adminBiz;

    /**
     * api
     *
     * @param uri
     * @param data
     * @return
     */
    @RequestMapping("/{uri}")
    public ReturnT<String> api(HttpServletRequest request, @PathVariable("uri") String uri, @RequestBody(required = false) String data) {

        // valid
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "invalid request, HttpMethod not support.");
        }
        if (uri==null || uri.trim().length()==0) {
            return new ReturnT<>(ReturnT.FAIL_CODE, "invalid request, uri-mapping empty.");
        }
//        if (XxlJobAdminConfig.getAdminConfig().getAccessToken()!=null
//                && XxlJobAdminConfig.getAdminConfig().getAccessToken().trim().length()>0
//                && !XxlJobAdminConfig.getAdminConfig().getAccessToken().equals(request.getHeader(JobRemotingUtil.JOB_ACCESS_TOKEN))) {
//            return new ReturnT<>(ReturnT.FAIL_CODE, "The access token is wrong.");
//        }

        // services mapping
//        if ("callback".equals(uri)) {
//            List<HandleCallbackParam> callbackParamList = GsonTool.fromJson(data, List.class, HandleCallbackParam.class);
//            return adminBiz.callback(callbackParamList);
//        } else
        if ("registry".equals(uri)) {
            RegistryParam registryParam = GsonTool.fromJson(data, RegistryParam.class);
            return adminBiz.registry(registryParam);
        } else if ("registryRemove".equals(uri)) {
            RegistryParam registryParam = GsonTool.fromJson(data, RegistryParam.class);
            return adminBiz.registryRemove(registryParam);
        } else {
            return new ReturnT<>(ReturnT.FAIL_CODE, "invalid request, uri-mapping(" + uri + ") not found.");
        }

    }

}
