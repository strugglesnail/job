package com.frontier.job.admin.service;

import com.frontier.job.admin.config.JobAdminConfig;
import com.frontier.job.admin.mapper.JobInfoMapper;
import com.frontier.job.admin.mapper.JobServerMapper;
import com.frontier.job.admin.model.JobInfo;
import com.frontier.job.admin.model.JobInfoRequest;
import com.frontier.job.admin.model.JobServer;
import com.frontier.job.core.connect.ClientFactory;
import com.frontier.job.core.connect.ServerClient;
import com.frontier.job.core.model.ReturnT;
import com.frontier.job.core.model.TriggerParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * core job action
 * @author xuxueli
 */
@Service
public class JobInfoService {
	private static Logger logger = LoggerFactory.getLogger(JobInfoService.class);

	@Resource
	private JobServerMapper jobServerMapper;
	@Resource
	private JobInfoMapper jobInfoMapper;



	public Map<String, Object> pageList(JobInfoRequest request) {
		JobInfo jobInfo = new JobInfo();
		BeanUtils.copyProperties(request, jobInfo);
		// page list
		List<JobInfo> dataList = jobInfoMapper.listLikePage(jobInfo, request.getRowBounds());
		Long count = jobInfoMapper.countLike(jobInfo);
//
		// package result
		Map<String, Object> maps = new HashMap<>();
	    maps.put("recordsTotal", count);		// 总记录数
	    maps.put("recordsFiltered", 0);				// 过滤后的总记录数
	    maps.put("data", dataList);  					// 分页列表
		return maps;
	}

	
	public ReturnT<String> add(JobInfo jobInfo) {
		JobServer jobServer = new JobServer();
		jobServer.setAppName(jobInfo.getJobGroup());

		// valid base
		JobServer server = jobServerMapper.getOne(jobServer);
		jobInfo.setJobServerId(server.getId());
		jobInfoMapper.save(jobInfo);
		return new ReturnT<>(String.valueOf(jobInfo.getId()));
	}



	
	public ReturnT<String> update(JobInfo jobInfo) {
		int update = jobInfoMapper.updateById(jobInfo);
		if (update > 0) {

			JobServer jobServer = jobServerMapper.getById(jobInfo.getJobServerId());
			String addressList = jobServer.getAddressList();
			ServerClient serverClient = ClientFactory.getServerClient(addressList, JobAdminConfig.getAdminConfig().getAccessToken());
			TriggerParam triggerParam = getTriggerParam(jobInfo);
			ReturnT<String> updateR = serverClient.update(triggerParam);
			if (updateR.getCode() == ReturnT.SUCCESS_CODE) {
				logger.info("远程调用成功：" + addressList);
			} else {
				return new ReturnT<>(ReturnT.FAIL_CODE, "远程调用失败！");
			}
		}
		return ReturnT.SUCCESS;
	}

	private static TriggerParam getTriggerParam(JobInfo jobInfo) {
		TriggerParam triggerParam = new TriggerParam();
		triggerParam.setJobId(jobInfo.getId());
		triggerParam.setJobName(jobInfo.getJobName());
		triggerParam.setJobGroup(jobInfo.getJobGroup());
		triggerParam.setCronExpression(jobInfo.getCronExpression());
		triggerParam.setMisfirePolicy(jobInfo.getMisfirePolicy());
		triggerParam.setExecutorHandler(jobInfo.getExecutorHandler());
		triggerParam.setStatus(jobInfo.getStatus());
		return triggerParam;
	}


	public ReturnT<String> remove(int id) {
//		XxlJobInfo xxlJobInfo = xxlJobInfoDao.loadById(id);
//		if (xxlJobInfo == null) {
//			return ReturnT.SUCCESS;
//		}

//		xxlJobInfoDao.delete(id);
//		xxlJobLogDao.delete(id);
		return ReturnT.SUCCESS;
	}

	
	public ReturnT<String> start(int id) {
//		XxlJobInfo xxlJobInfo = xxlJobInfoDao.loadById(id);
//
//		// valid
//		ScheduleTypeEnum scheduleTypeEnum = ScheduleTypeEnum.match(xxlJobInfo.getScheduleType(), ScheduleTypeEnum.NONE);
//		if (ScheduleTypeEnum.NONE == scheduleTypeEnum) {
//			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("schedule_type_none_limit_start")) );
//		}
//
//		// next trigger time (5s后生效，避开预读周期)
//		long nextTriggerTime = 0;
//		try {
//			Date nextValidTime = JobScheduleHelper.generateNextValidTime(xxlJobInfo, new Date(System.currentTimeMillis() + JobScheduleHelper.PRE_READ_MS));
//			if (nextValidTime == null) {
//				return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("schedule_type")+I18nUtil.getString("system_unvalid")) );
//			}
//			nextTriggerTime = nextValidTime.getTime();
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//			return new ReturnT<String>(ReturnT.FAIL_CODE, (I18nUtil.getString("schedule_type")+I18nUtil.getString("system_unvalid")) );
//		}
//
//		xxlJobInfo.setTriggerStatus(1);
//		xxlJobInfo.setTriggerLastTime(0);
//		xxlJobInfo.setTriggerNextTime(nextTriggerTime);
//
//		xxlJobInfo.setUpdateTime(new Date());
//		xxlJobInfoDao.update(xxlJobInfo);
		return ReturnT.SUCCESS;
	}

	
	public ReturnT<String> stop(int id) {
//        XxlJobInfo xxlJobInfo = xxlJobInfoDao.loadById(id);
//
//		xxlJobInfo.setTriggerStatus(0);
//		xxlJobInfo.setTriggerLastTime(0);
//		xxlJobInfo.setTriggerNextTime(0);
//
//		xxlJobInfo.setUpdateTime(new Date());
//		xxlJobInfoDao.update(xxlJobInfo);
		return ReturnT.SUCCESS;
	}

	
	public Map<String, Object> dashboardInfo() {

		int jobInfoCount = 0;//xxlJobInfoDao.findAllCount();
		int jobLogCount = 0;
		int jobLogSuccessCount = 0;
//		XxlJobLogReport xxlJobLogReport = xxlJobLogReportDao.queryLogReportTotal();
//		if (xxlJobLogReport != null) {
//			jobLogCount = xxlJobLogReport.getRunningCount() + xxlJobLogReport.getSucCount() + xxlJobLogReport.getFailCount();
//			jobLogSuccessCount = xxlJobLogReport.getSucCount();
//		}

		// executor count
		Set<String> executorAddressSet = new HashSet<String>();
		List<JobServer> groupList = new ArrayList<>();//xxlJobGroupDao.findAll();

		if (groupList!=null && !groupList.isEmpty()) {
//			for (XxlJobGroup group: groupList) {
//				if (group.getRegistryList()!=null && !group.getRegistryList().isEmpty()) {
//					executorAddressSet.addAll(group.getRegistryList());
//				}
//			}
		}

		int executorCount = executorAddressSet.size();

		Map<String, Object> dashboardMap = new HashMap<>();
		dashboardMap.put("jobInfoCount", jobInfoCount);
		dashboardMap.put("jobLogCount", jobLogCount);
		dashboardMap.put("jobLogSuccessCount", jobLogSuccessCount);
		dashboardMap.put("executorCount", executorCount);
		return dashboardMap;
	}

	
	public ReturnT<Map<String, Object>> chartInfo(Date startDate, Date endDate) {

		// process
		List<String> triggerDayList = new ArrayList<String>();
		List<Integer> triggerDayCountRunningList = new ArrayList<Integer>();
		List<Integer> triggerDayCountSucList = new ArrayList<Integer>();
		List<Integer> triggerDayCountFailList = new ArrayList<Integer>();
		int triggerCountRunningTotal = 0;
		int triggerCountSucTotal = 0;
		int triggerCountFailTotal = 0;

//		List<XxlJobLogReport> logReportList = xxlJobLogReportDao.queryLogReport(startDate, endDate);
//
//		if (logReportList!=null && logReportList.size()>0) {
//			for (XxlJobLogReport item: logReportList) {
//				String day = DateUtil.formatDate(item.getTriggerDay());
//				int triggerDayCountRunning = item.getRunningCount();
//				int triggerDayCountSuc = item.getSucCount();
//				int triggerDayCountFail = item.getFailCount();
//
//				triggerDayList.add(day);
//				triggerDayCountRunningList.add(triggerDayCountRunning);
//				triggerDayCountSucList.add(triggerDayCountSuc);
//				triggerDayCountFailList.add(triggerDayCountFail);
//
//				triggerCountRunningTotal += triggerDayCountRunning;
//				triggerCountSucTotal += triggerDayCountSuc;
//				triggerCountFailTotal += triggerDayCountFail;
//			}
//		} else {
//			for (int i = -6; i <= 0; i++) {
//				triggerDayList.add(DateUtil.formatDate(DateUtil.addDays(new Date(), i)));
//				triggerDayCountRunningList.add(0);
//				triggerDayCountSucList.add(0);
//				triggerDayCountFailList.add(0);
//			}
//		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("triggerDayList", triggerDayList);
		result.put("triggerDayCountRunningList", triggerDayCountRunningList);
		result.put("triggerDayCountSucList", triggerDayCountSucList);
		result.put("triggerDayCountFailList", triggerDayCountFailList);

		result.put("triggerCountRunningTotal", triggerCountRunningTotal);
		result.put("triggerCountSucTotal", triggerCountSucTotal);
		result.put("triggerCountFailTotal", triggerCountFailTotal);

		return new ReturnT<>(result);
	}

}
