package com.kburaky.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SideBarController {
	
	
	@RequestMapping(value = "/sideBar", method = RequestMethod.GET)
	public String sideBar( HttpServletRequest req, Model model) {
		
		return "admin/inc/sideBar";
	}
	
	
}
