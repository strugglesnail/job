package com.frontier.job.admin.controller;

import com.frontier.job.admin.model.JobInfo;
import com.frontier.job.admin.model.JobInfoRequest;
import com.frontier.job.admin.service.JobInfoService;
import com.frontier.job.admin.trigger.JobTrigger;
import com.frontier.job.core.model.ReturnT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Map;

/**
 * index controller
 * @author 
 */
@Controller
@RequestMapping("/jobinfo")
public class JobInfoController {
	private static Logger logger = LoggerFactory.getLogger(JobInfoController.class);

	// 任务1： 改造状态，操作按钮 编辑 删除 更多
	// 任务2：服务端实时推送消息

	@Resource
	private JobInfoService jobInfoService;
//	@Resource
//	private XxlJobService xxlJobService;
	
	@RequestMapping
	public String index(JobInfoRequest request, Model model, @RequestParam(required = false, defaultValue = "-1") int jobGroup) {

		// 枚举-字典
//		model.addAttribute("ExecutorRouteStrategyEnum", ExecutorRouteStrategyEnum.values());	    // 路由策略-列表
//		model.addAttribute("GlueTypeEnum", GlueTypeEnum.values());								// Glue类型-字典
//		model.addAttribute("ExecutorBlockStrategyEnum", ExecutorBlockStrategyEnum.values());	    // 阻塞处理策略-字典
//		model.addAttribute("ScheduleTypeEnum", ScheduleTypeEnum.values());	    				// 调度类型
//		model.addAttribute("MisfireStrategyEnum", MisfireStrategyEnum.values());	    			// 调度过期策略

		// 执行器列表
		Map<String, Object> pageList = jobInfoService.pageList(request);


		model.addAttribute("JobGroupList", new ArrayList<>());
		model.addAttribute("jobGroup", jobGroup);

		return "jobinfo/jobinfo.index";
	}



	@RequestMapping("/pageList")
	@ResponseBody
	public Map<String, Object> pageList(JobInfoRequest request) {
		return jobInfoService.pageList(request);
	}

	@RequestMapping("/add")
	@ResponseBody
	public ReturnT<String> add(JobInfo jobInfo) {
		return jobInfoService.add(jobInfo);
	}

	@RequestMapping("/update")
	@ResponseBody
	public ReturnT<String> update(JobInfo jobInfo) {
		return jobInfoService.update(jobInfo);
	}
//
//	@RequestMapping("/remove")
//	@ResponseBody
//	public ReturnT<String> remove(int id) {
//		return xxlJobService.remove(id);
//	}
//
//	@RequestMapping("/stop")
//	@ResponseBody
//	public ReturnT<String> pause(int id) {
//		return xxlJobService.stop(id);
//	}
//
//	@RequestMapping("/start")
//	@ResponseBody
//	public ReturnT<String> start(int id) {
//		return xxlJobService.start(id);
//	}
	
	@RequestMapping("/trigger")
	@ResponseBody
	public ReturnT<String> triggerJob(int id, String executorParam, String addressList) {
		// force cover job param
		if (executorParam == null) {
			executorParam = "";
		}

		JobTrigger.trigger(id, executorParam, addressList);
		return ReturnT.SUCCESS;
	}

	
}
