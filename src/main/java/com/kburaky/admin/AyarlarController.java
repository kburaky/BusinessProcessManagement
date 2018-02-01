package com.kburaky.admin;

import java.util.List;

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
public class AyarlarController {
	
	SessionFactory sf = new Configuration().configure().buildSessionFactory();
	Session sesi = sf.openSession();
	
	@RequestMapping(value = "/ayarlar", method = RequestMethod.GET)
	public String ayarAc(Model model, HttpServletRequest req) {		
		String kid = ""+req.getSession().getAttribute("kid");
		sesi = sf.openSession();
		List<Kullanici> ls = sesi.createQuery("from Kullanici where kid = '"+kid+"' ").list();
		model.addAttribute("kullanici", ls.get(0));
		
		return "admin/ayarlar";
	}
	
	
	@RequestMapping(value = "/ayarlarDuzenle", method = RequestMethod.POST)
	public String personelKayitDuzenle(HttpServletRequest req, Model model) {
		
		String kAdi = req.getParameter("kAdi");
		String kSoyadi = req.getParameter("kSoyadi");
		String kMail = req.getParameter("kMail");
		String kTelefon = req.getParameter("kTelefon");
		String kAdres = req.getParameter("kAdres");
		String kid = req.getParameter("kid");
		
		DB db = new DB();
		try {
			int duzenle = db.baglan().executeUpdate("update kullanici set kAdi = '"+kAdi+"', kSoyadi = '"+kSoyadi+"', kMail = '"+kMail+"' , kTelefon = '"+kTelefon+"' , kAdres = '"+kAdres+"' where kid = '"+kid+"' ");
			if (duzenle > 0) {
				
			}
		} catch (Exception e) {
			System.err.println("perfonel duzenleme hatasi " + e);
		}finally {
			db.kapat();
		}
		
		return "redirect:/ayarlar";
	}
	
	

}
