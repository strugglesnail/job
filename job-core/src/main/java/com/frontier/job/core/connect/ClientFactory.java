package com.frontier.job.core.connect;


import com.frontier.job.core.connect.client.HttpAdminClient;
import com.frontier.job.core.connect.client.HttpServerClient;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ClientFactory {


    private static ConcurrentMap<String, AdminClient> adminClientRepository = new ConcurrentHashMap<>();
    private static ConcurrentMap<String, ServerClient> serverClientRepository = new ConcurrentHashMap<>();



    public static AdminClient getAdminClient(String address, String accessToken) {
        // valid
        if (address==null || address.trim().length()==0) {
            return null;
        }

        AdminClient adminClient;
        // load-cache
        address = address.trim();
        if (adminClientRepository.containsKey(address)) {
            adminClient = adminClientRepository.get(address);
        } else {
            // set-cache
            adminClient = new HttpAdminClient(address, accessToken);
            adminClientRepository.put(address, adminClient);
        }
        return adminClient;
    }



    public static ServerClient getServerClient(String address, String accessToken) {
        // valid
        if (address==null || address.trim().length()==0) {
            return null;
        }

        // load-cache
        address = address.trim();
        ServerClient serverClient;
        if (serverClientRepository.containsKey(address)) {
            serverClient = serverClientRepository.get(address);
        } else {
            // set-cache
            serverClient = new HttpServerClient(address, accessToken);
            serverClientRepository.put(address, serverClient);
        }
        return serverClient;
    }
}
