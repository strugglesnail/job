package com.frontier.job.admin.model;

import java.util.Date;

public class JobServerRequest extends PageRequest {


    private Integer id;

    //服务名称
    private String appName;

    //服务描述
    private String appDesc;

    private Date updateTime;

    //服务地址列表，多地址逗号分隔
    private String addressList;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取服务名称
     *
     * @return app_name - 服务名称
     */
    public String getAppName() {
        return appName;
    }

    /**
     * 设置服务名称
     *
     * @param appName 服务名称
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * 获取服务描述
     *
     * @return app_desc - 服务描述
     */
    public String getAppDesc() {
        return appDesc;
    }

    /**
     * 设置服务描述
     *
     * @param appDesc 服务描述
     */
    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取服务地址列表，多地址逗号分隔
     *
     * @return address_list - 服务地址列表，多地址逗号分隔
     */
    public String getAddressList() {
        return addressList;
    }

    /**
     * 设置服务地址列表，多地址逗号分隔
     *
     * @param addressList 服务地址列表，多地址逗号分隔
     */
    public void setAddressList(String addressList) {
        this.addressList = addressList;
    }
}