package com.kburaky.admin;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.kburaky.yetenek.DB;

import ozellikler.RaporOzellik;

@Controller
public class RaporlamaController {
	
	
	@RequestMapping(value = "/raporlama", method = RequestMethod.GET)
	public String raporlama(HttpServletRequest req, Model model) {
		boolean tarihDurum = req.getParameter("tarih") != null;
		if (tarihDurum) {
			String tarih = req.getParameter("tarih");
			String baslamaTarihi = tarih.split(" - ")[0];
			String bitisTarihi = tarih.split(" - ")[1];
			List<RaporOzellik> rls = new ArrayList<RaporOzellik>();
			DB db = new DB();
			try {
				String q = "SELECT isu.sid, isu.sBaslik, isu.baslamaTarihi, isu.bitisTarihi, isu.sDurum , kl.kAdi as aAdi, kl.kSoyadi as aSoyadi, pkl.kAdi as pAdi, pkl.kSoyadi as pSoyadi FROM issureci as isu LEFT JOIN kullanici as kl ON kl.kid = isu.kid LEFT JOIN kullanici as pkl ON isu.pid = pkl.kid WHERE isu.baslamaTarihi BETWEEN '"+baslamaTarihi+"' and '"+bitisTarihi+"'";
				ResultSet rs = db.baglan().executeQuery(q);
				while(rs.next()) {
					RaporOzellik rp = new RaporOzellik();
					rp.setSid(rs.getString("sid"));
					rp.setsBaslik(rs.getString("sBaslik"));
					rp.setBaslamaTarihi(rs.getString("baslamaTarihi"));
					rp.setBitisTarihi(rs.getString("bitisTarihi"));
					rp.setsDurum(rs.getString("sDurum"));
					rp.setaAdi(rs.getString("aAdi"));
					rp.setaSoyadi(rs.getString("aSoyadi"));
					rp.setpAdi(rs.getString("pAdi"));
					rp.setpSoyadi(rs.getString("pSoyadi"));
					rls.add(rp);
				}
				model.addAttribute("rapor", rls);
			} catch (Exception e) {
				System.err.println("raporlama hatasi " + e);
			}finally {
				db.kapat();
			}
			
			
		}
		return "admin/raporlama";
	}

	@RequestMapping(value = "/raporAra", method = RequestMethod.POST)
	public String raporAra(Model model, @RequestParam String tarih) {
		model.addAttribute("tarih", tarih);
		return "redirect:/raporlama";
	}

	
	
}
