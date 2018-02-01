package com.kburaky.admin;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.kburaky.pojo.Kullanici;
import com.kburaky.yetenek.Yetenekler;

@Controller
public class PersonelController {
	
	SessionFactory sf = new Configuration().configure().buildSessionFactory();
	Session sesi = sf.openSession();
	
	@RequestMapping(value = "/personelYonetimi", method = RequestMethod.GET)
	public String personelYonetimi(Model model, HttpServletRequest req) {
		sesi = sf.openSession();
		String aid = String.valueOf(req.getSession().getAttribute("kid"));
		List<Kullanici> ls = sesi.createQuery("from Kullanici where kSeviye != 0 ").list();

		model.addAttribute("personelListesi", ls);
		for (Kullanici k : ls) {
			System.out.println("ünvan : " + k.getKUnvan());
		}
		boolean basarili = req.getParameter("basarili") != null;
		boolean basarisiz = req.getParameter("basarisiz") != null;
		if(basarili) {
			
			model.addAttribute("basarili", "basarili");
			System.out.println("basarili çalıştı");
		}
		if(basarisiz){
			
			model.addAttribute("basarisiz", "basarisiz");
			System.out.println("basarisiz çalıştı");
		} 
		sesi.close();
		return "admin/personelYonetimi";
		
	}
	
	
	
	// yeni personel kayıt işlemi
	@RequestMapping(value = "/personelKayit", method = RequestMethod.POST)
	public String personelKayit(Model model, HttpServletRequest req) {
		
		String kUnvan = req.getParameter("kUnvan");
		String kSeviye = req.getParameter("kSeviye");
		String kAdi = req.getParameter("kAdi");
		String kSoyadi = req.getParameter("kSoyadi");
		String kMail = req.getParameter("kMail");
		String kSifre = Yetenekler.MD5(req.getParameter("kSifre"));
		String kTelefon = req.getParameter("kTelefon");
		String kAdres = req.getParameter("kAdres");
		
		sesi = sf.openSession();
		Transaction tr = sesi.beginTransaction();
		Kullanici kl = new Kullanici();
		kl.setKid(Integer.MAX_VALUE);
		kl.setKUnvan(kUnvan);
		kl.setKSeviye(Integer.valueOf(kSeviye));
		kl.setKAdi(kAdi);
		kl.setKSoyadi(kSoyadi);
		kl.setKMail(kMail);
		kl.setKSifre(kSifre);
		kl.setKTelefon(kTelefon);
		kl.setKAdres(kAdres);
		kl.setKTarih(new Date());
		
		try {
			sesi.save(kl);
			tr.commit();
			model.addAttribute("basarili", "basarili");
		} catch (Exception e) {
			model.addAttribute("basarisiz", "basarisiz");
		}finally {
			sesi.close();
		}
		return "redirect:/personelYonetimi"; 
	}	
	
	
	// personel silme
	@RequestMapping(value = "/personelSil/{kid}", method = RequestMethod.GET)
	public String personelSil(@PathVariable(value="kid") int kid, Model model) {
		sesi = sf.openSession();
		Transaction tr = sesi.beginTransaction();
		Kullanici kl = new Kullanici(kid);
		sesi.delete(kl);
		tr.commit();
		sesi.close();
		return "redirect:/personelYonetimi";  
	}
	
	
	
	// personel data düzenle
	@RequestMapping(value = "/personelDuzenle/{kid}", method = RequestMethod.GET)
	public String personelDuzenle(@PathVariable(value="kid") int kid, Model model) {
		
		sesi = sf.openSession();
		List<Kullanici> ls = sesi.createQuery("from Kullanici where kid = '"+kid+"' ").list();
		model.addAttribute("kullanici", ls.get(0));
		return "admin/personelDuzenle";
	}
	
	
	@RequestMapping(value = "/personelKayitDuzenle", method = RequestMethod.POST)
	public String personelKayitDuzenle(HttpServletRequest req, Model model) {
		String kUnvan = req.getParameter("kUnvan");
		String kSeviye = req.getParameter("kSeviye");
		String kAdi = req.getParameter("kAdi");
		String kSoyadi = req.getParameter("kSoyadi");
		String kMail = req.getParameter("kMail");
		String kTelefon = req.getParameter("kTelefon");
		String kAdres = req.getParameter("kAdres");
		String kid = req.getParameter("kid");
		
		Kullanici kl = new Kullanici(Integer.valueOf(kid));
		sesi = sf.openSession();
		Session sesiup = sf.openSession();
		Transaction tr = sesi.beginTransaction();
		kl.setKUnvan(kUnvan);
		kl.setKSeviye(Integer.valueOf(kSeviye));
		kl.setKAdi(kAdi);
		kl.setKSoyadi(kSoyadi);
		kl.setKMail(kMail);
		kl.setKTelefon(kTelefon);
		kl.setKAdres(kAdres);
		
		
		// eski şifreyi yaz
		List<Kullanici> ls = sesiup.createQuery("from Kullanici where kid = '"+kid+"' ").list();
		kl.setKSifre(ls.get(0).getKSifre());
		kl.setKTarih(ls.get(0).getKTarih());
		
		sesi.update(kl);
		tr.commit();
		sesi.close();
		
		return "redirect:/personelYonetimi";
	}
	
	

}









