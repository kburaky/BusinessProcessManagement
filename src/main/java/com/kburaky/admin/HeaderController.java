package com.kburaky.admin;

import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kburaky.pojo.Kullanici;
import com.kburaky.yetenek.DB;

@Controller
public class HeaderController {
	SessionFactory sf = new Configuration().configure().buildSessionFactory();
	Session sesi = sf.openSession();
	
	@RequestMapping(value = "/header", method = RequestMethod.GET)
	public String headerAc(Model model, HttpServletRequest req) {
		
		DB db = new DB();
		try {
			String id = ""+req.getSession().getAttribute("kid");
			ResultSet rs = db.baglan().executeQuery("select *, (SELECT COUNT(sid) FROM issureci WHERE pid = '"+id+"' and sDurum = 0) as toplanIs from kullanici where kid = '"+id+"' ");
			if (rs.next()) {
				Kullanici kl = new Kullanici();
				kl.setKUnvan(rs.getString("kUnvan"));
				kl.setKSeviye(rs.getInt("kSeviye"));
				kl.setKAdi(rs.getString("kAdi"));
				kl.setKSoyadi(rs.getString("kSoyadi"));	
				kl.setKMail(rs.getString("kMail"));
				kl.setKTelefon(rs.getString("kTelefon"));
				model.addAttribute("kl", kl);
				model.addAttribute("toplanIs", rs.getString("toplanIs"));
			}
		} catch (Exception e) {
			System.err.println("id getirme hatası : " + e);
		}finally {
			db.kapat();
		}

		
		return "admin/inc/header";
	}

}
