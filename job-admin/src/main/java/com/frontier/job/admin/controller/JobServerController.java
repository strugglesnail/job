package com.frontier.job.admin.controller;

import com.frontier.job.admin.model.JobServer;
import com.frontier.job.admin.model.JobServerRequest;
import com.frontier.job.admin.service.JobInfoService;
import com.frontier.job.admin.service.JobServerService;
import com.frontier.job.admin.util.I18nUtil;
import com.frontier.job.core.model.ReturnT;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * job group controller
 * @author xuxueli 2016-10-02 20:52:56
 */
@Controller
@RequestMapping("/jobgroup")
public class JobServerController {

	@Resource
	public JobInfoService jobInfoService;
	@Resource
	public JobServerService jobServerService;
//	@Resource
//	private XxlJobRegistryDao xxlJobRegistryDao;

	@RequestMapping
	public String index(Model model) {
		return "jobgroup/jobgroup.index";
	}

	@RequestMapping("/pageList")
	@ResponseBody
	public Map<String, Object> pageList(JobServerRequest request) {

		// page query
		Map<String, Object> maps = jobServerService.pageList(request);

		// package result
//		Map<String, Object> maps = new HashMap<String, Object>();
//		maps.put("recordsTotal", list_count);		// 总记录数
//		maps.put("recordsFiltered", list_count);	// 过滤后的总记录数
//		maps.put("data", list);  					// 分页列表
		return maps;
	}

	@RequestMapping("/save")
	@ResponseBody
	public ReturnT<String> save(JobServer jobServer){
		String appName = jobServer.getAppName();
		String appDesc = jobServer.getAppDesc();
		// valid
		if (appName==null || appName.trim().length()==0) {
			return new ReturnT<String>(500, (I18nUtil.getString("system_please_input")+"AppName") );
		}
		if (appName.length()<4 || appName.length()>64) {
			return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_appname_length") );
		}
		if (appName.contains(">") || appName.contains("<")) {
			return new ReturnT<String>(500, "AppName"+I18nUtil.getString("system_unvalid") );
		}
		if (jobServer.getAppDesc()==null || jobServer.getAppDesc().trim().length()==0) {
			return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title")) );
		}
		if (appDesc.contains(">") || appDesc.contains("<")) {
			return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_title")+I18nUtil.getString("system_unvalid") );
		}
//		if (jobServer.getAddressType()!=0) {
//			if (jobServer.getAddressList()==null || jobServer.getAddressList().trim().length()==0) {
//				return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_addressType_limit") );
//			}
//			if (jobServer.getAddressList().contains(">") || jobServer.getAddressList().contains("<")) {
//				return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_registryList")+I18nUtil.getString("system_unvalid") );
//			}
//
//			String[] addresss = jobServer.getAddressList().split(",");
//			for (String item: addresss) {
//				if (item==null || item.trim().length()==0) {
//					return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_registryList_unvalid") );
//				}
//			}
//		}

		// process
		jobServer.setUpdateTime(new Date());

		return jobServerService.add(jobServer);
	}

	@RequestMapping("/update")
	@ResponseBody
	public ReturnT<String> update(JobServer jobServer){
//		// valid
//		if (jobServer.getAppname()==null || jobServer.getAppname().trim().length()==0) {
//			return new ReturnT<String>(500, (I18nUtil.getString("system_please_input")+"AppName") );
//		}
//		if (jobServer.getAppname().length()<4 || jobServer.getAppname().length()>64) {
//			return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_appname_length") );
//		}
//		if (jobServer.getTitle()==null || jobServer.getTitle().trim().length()==0) {
//			return new ReturnT<String>(500, (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title")) );
//		}
//		if (jobServer.getAddressType() == 0) {
//			// 0=自动注册
//			List<String> registryList = findRegistryByAppName(jobServer.getAppname());
//			String addressListStr = null;
//			if (registryList!=null && !registryList.isEmpty()) {
//				Collections.sort(registryList);
//				addressListStr = "";
//				for (String item:registryList) {
//					addressListStr += item + ",";
//				}
//				addressListStr = addressListStr.substring(0, addressListStr.length()-1);
//			}
//			jobServer.setAddressList(addressListStr);
//		} else {
//			// 1=手动录入
//			if (jobServer.getAddressList()==null || jobServer.getAddressList().trim().length()==0) {
//				return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_addressType_limit") );
//			}
//			String[] addresss = jobServer.getAddressList().split(",");
//			for (String item: addresss) {
//				if (item==null || item.trim().length()==0) {
//					return new ReturnT<String>(500, I18nUtil.getString("jobgroup_field_registryList_unvalid") );
//				}
//			}
//		}

		// process
		jobServer.setUpdateTime(new Date());

		return jobServerService.update(jobServer);
//		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}


	@RequestMapping("/remove")
	@ResponseBody
	public ReturnT<String> remove(int id){

//		// valid
//		int count = xxlJobInfoDao.pageListCount(0, 10, id, -1,  null, null, null);
//		if (count > 0) {
//			return new ReturnT<String>(500, I18nUtil.getString("jobgroup_del_limit_0") );
//		}
//
//		List<XxlJobGroup> allList = xxlJobGroupDao.findAll();
//		if (allList.size() == 1) {
//			return new ReturnT<String>(500, I18nUtil.getString("jobgroup_del_limit_1") );
//		}
//
		int ret = 0;// xxlJobGroupDao.remove(id);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/loadById")
	@ResponseBody
	public ReturnT<JobServer> loadById(int id){
		JobServer jobGroup = jobServerService.loadById(id);
		return jobGroup!=null ? new ReturnT<>(jobGroup) : new ReturnT<>(ReturnT.FAIL_CODE, null);
	}

}
