package com.frontier.job.admin.controller;

import com.frontier.job.admin.service.JobInfoService;
import com.frontier.job.core.model.ReturnT;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * index controller
 * @author 
 */
@Controller
public class IndexController {

	@Resource
	private JobInfoService xxlJobService;
//	@Resource
//	private LoginService loginService;


	@RequestMapping("/")
	public String index(Model model) {

		Map<String, Object> dashboardMap = xxlJobService.dashboardInfo();
		model.addAllAttributes(dashboardMap);

		return "index";
	}

    @GetMapping("/chartInfo")
	@ResponseBody
	public ReturnT<Map<String, Object>> chartInfo(Date startDate, Date endDate) {
        ReturnT<Map<String, Object>> chartInfo = xxlJobService.chartInfo(startDate, endDate);
        return chartInfo;
    }
	
//	@RequestMapping("/toLogin")
//	public ModelAndView toLogin(HttpServletRequest request, HttpServletResponse response,ModelAndView modelAndView) {
//		if (loginService.ifLogin(request, response) != null) {
//			modelAndView.setView(new RedirectView("/",true,false));
//			return modelAndView;
//		}
//		return new ModelAndView("login");
//	}
	
//	@GetMapping(value="login")
//	@ResponseBody
//	public ReturnT<String> loginDo(HttpServletRequest request, HttpServletResponse response, String userName, String password, String ifRemember){
//		boolean ifRem = (ifRemember!=null && ifRemember.trim().length()>0 && "on".equals(ifRemember))?true:false;
//		return loginService.login(request, response, userName, password, ifRem);
//	}
	
//	@RequestMapping(value="logout", method=RequestMethod.POST)
//	@ResponseBody
//	public ReturnT<String> logout(HttpServletRequest request, HttpServletResponse response){
//		return loginService.logout(request, response);
//	}
	
	@RequestMapping("/help")
	public String help() {
		return "help";
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
}
