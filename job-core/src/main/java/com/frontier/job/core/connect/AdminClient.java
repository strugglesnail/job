package com.frontier.job.core.connect;

import com.frontier.job.core.model.RegistryParam;
import com.frontier.job.core.model.ReturnT;

/**
 * 请求客户端的接口
 */
public interface AdminClient {


//    ReturnT<String> callback(List<HandleCallbackParam> callbackParamList);

    // 注册服务
    ReturnT<String> registry(RegistryParam registryParam);

    // 移除服务
    ReturnT<String> registryRemove(RegistryParam registryParam);



}
