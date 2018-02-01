package com.kburaky.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SolMenuController {

	
	String seviye = "-1";
	@RequestMapping(value = "/solMenu", method = RequestMethod.GET)
	public String solMenuAc(HttpServletRequest req, Model model) {
		seviye = String.valueOf(req.getSession().getAttribute("seviye"));
		String seviyeDurum = "";
		String konum = "";
		
		if(seviye.equals("0")) {
			seviyeDurum = "sirket";
			konum = "Þirket";
		}
		
		if(seviye.equals("1")) {
			seviyeDurum = "mudur";
			konum = "Müdür";
		}
		
		if(seviye.equals("2")) {
			seviyeDurum = "calisan";
			konum = "Çalýþan";
		}
		model.addAttribute(seviyeDurum,seviye);
		model.addAttribute("seviyeKonum",konum);

		return "admin/inc/solMenu";
	}
	
	
}
