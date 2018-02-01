package com.kburaky.admin;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.kburaky.pojo.Kullanici;
import com.kburaky.yetenek.DB;
import com.kburaky.yetenek.Yetenekler;

import ozellikler.IstatistikOzellik;

@Controller
public class GirisController {
	
	SessionFactory sf = new Configuration().configure().buildSessionFactory(); // connection
	Session sesi = sf.openSession(); // statement 

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String giris(HttpServletResponse res, HttpServletRequest req) {
		
		boolean cDiziN = req.getCookies() != null;
		if (cDiziN) {
		Cookie[] cdizi = req.getCookies();
		for (int i = 0; i < cdizi.length; i++) {
			Cookie cookie = cdizi[i];
			if(cookie.getName().equals("beni_hatirla")) {
				String cData = cookie.getValue();
				String[] bol = cData.split(".");
				req.getSession().setAttribute("kid", bol[0]);
				req.getSession().setAttribute("seviye", bol[1]);
				return "redirect:panel";
			}
		}
		}
		
		return "admin/giris";
	}
	
	
	@RequestMapping(value = "/giris", method = RequestMethod.POST)	
	public String girisYap(Model model, @RequestParam String email, @RequestParam String sifre, HttpServletRequest req, HttpServletResponse res) {
		System.out.println("Geldim");
		boolean durum = false;
		String md5 = Yetenekler.MD5(sifre);
		List<Kullanici> ls = sesi.createQuery("from Kullanici where kMail = '"+email+"' and kSifre = '"+md5+"'").list();
		for (Kullanici item : ls) {
			
			durum = true;
			// oturum aciliyor
			req.getSession().setAttribute("kid", item.getKid());
			req.getSession().setAttribute("seviye", item.getKSeviye());
			
			// beni hatirla kontrolu saglaniyor
			boolean hatirla_durum =  req.getParameter("beni_hatirla") != null;
			if (hatirla_durum) {
				String cerezYaz = item.getKid() + "." + item.getKSeviye();
				Cookie cerez = new Cookie("beni_hatirla", cerezYaz);
				cerez.setMaxAge(60 * 60 * 24);
				res.addCookie(cerez);
			}
			
			// sayfayi yonlendir.
			return "redirect:panel";
		}
		model.addAttribute("durum", durum);
		return "admin/giris";
	}
	
	
	// admin sayfasini ac
	@RequestMapping(value = "/panel", method = RequestMethod.GET)
	public String adminAc(Model model, HttpServletRequest req) {

		DB db = new DB();
		String seviye =  ""+req.getSession().getAttribute("seviye");
		String kid =  ""+req.getSession().getAttribute("kid");
		// istatistikPr
		List<IstatistikOzellik> lsistatis = new ArrayList<IstatistikOzellik>();
		try {
			String q = "";
			if (seviye.equalsIgnoreCase("0") || seviye.equalsIgnoreCase("1"))
				q = "CALL istatistikPro()";
			else
				q = "CALL istatistikKulPro('"+kid+"')";
			
			ResultSet rs = db.baglan().executeQuery(q);
			if(rs.next()) {
				IstatistikOzellik isn = new IstatistikOzellik();
				isn.setAktifisToplam(rs.getString("aktifisToplam"));
				isn.setBitirilenisToplam(rs.getString("bitirilenisToplam"));
				isn.setGecenisToplam(rs.getString("gecenisToplam"));
				isn.setMesajToplam(rs.getString("mesajToplam"));
				lsistatis.add(isn);
			}
			model.addAttribute("ista", lsistatis.get(0));
		} catch (Exception e) {
			System.err.println("Istatistik Getirme Hatas� : " + e);
		}finally {
			db.kapat();
		}
		
		
		return "admin/panel";
	}
	
	
	// Cikis yap sayfasi
	@RequestMapping(value = "/cikis", method = RequestMethod.GET)
	public String cikisYap( HttpServletRequest req , HttpServletResponse res) {
		// bütün oturumlari kapat
		req.getSession().invalidate();
		
		// Cerezleri sil
		Cookie cerez = new Cookie("beni_hatirla", "");
		cerez.setMaxAge(0);
		res.addCookie(cerez);
		
		return "admin/giris";
	}	
	
	
	
	// session - oturum denetimi yap
	@RequestMapping(value = "/headerCSS", method = RequestMethod.GET)
	public String sessionKontrolAc(Model model, HttpServletRequest req) {
		// session denetimi
		boolean sessionDurum = req.getSession().getAttribute("kid") == null;
		if (sessionDurum) {
			model.addAttribute("oturumDurum", 0);
		}
		return "admin/inc/headerCSS";
	}
	
	
	// boş sayfasını aç
		@RequestMapping(value = "/bos", method = RequestMethod.GET)
		public String bosAc(HttpServletRequest req, Model model) {
			boolean basarili = req.getParameter("basarili") != null;
			boolean basarisiz = req.getParameter("basarisiz") != null;
			if (basarili == true) 
				model.addAttribute("basarili", basarili);
			if (basarisiz == true) 
				model.addAttribute("basarisiz", basarisiz);
			return "admin/bos";
		}
		
		// boş form kayıt
		@RequestMapping(value = "/bosKayit", method = RequestMethod.POST)
		public String bosKayit(@RequestParam String mail,@RequestParam String sifre, Model model ) {
			
			if(mail.equals("ali@ali.com") && sifre.equals("12345")) {
				model.addAttribute("basarili", true);
			}else {
				model.addAttribute("basarisiz", true);
			}
			
			return "redirect:/bos";
		}
	
		
		
}
