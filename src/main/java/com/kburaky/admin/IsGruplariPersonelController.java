package com.kburaky.admin;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.kburaky.pojo.IsGruplari;
import com.kburaky.pojo.IsGruplariAtama;
import com.kburaky.pojo.Kullanici;
import com.kburaky.yetenek.DB;

import ozellikler.isGrupListesi;

@Controller
public class IsGruplariPersonelController {

	SessionFactory sf = new Configuration().configure().buildSessionFactory(); // connection
	Session sesi = sf.openSession(); // statement 
	
	
	@RequestMapping(value = "/isGruplariPersonel", method = RequestMethod.GET)
	public String isGruplariPersonel (Model model) {
		
		sesi = sf.openSession(); // statement 
		List<Kullanici> personel = sesi.createQuery("from Kullanici where kSeviye = 2").getResultList();
		List<IsGruplari> isgruplari = sesi.createQuery("from IsGruplari").getResultList();
		sesi.close();
		
		List<isGrupListesi> igls = new ArrayList<isGrupListesi>();
		DB db = new DB();
		try {
			ResultSet rs = db.baglan().executeQuery("select *FROM isGruplariAtama as iga left join kullanici as kl on iga.pid = kl.kid left join isGruplari as ig on iga.iid = ig.iid");
			while(rs.next()) {
				isGrupListesi ig = new isGrupListesi();

				ig.setGid(rs.getString("gid"));
				ig.setIid(rs.getString("iid"));
				ig.setPid(rs.getString("pid"));
				ig.setiTarih(rs.getString("iTarih"));
				ig.setKid(rs.getString("kid"));
				ig.setkUnvan(rs.getString("kUnvan"));
				ig.setkSeviye(rs.getString("kSeviye"));
				ig.setkAdi(rs.getString("kAdi"));
				ig.setkSoyadi(rs.getString("kSoyadi"));
				ig.setkMail(rs.getString("kMail"));
				ig.setkTelefon(rs.getString("kTelefon"));
				ig.setkAdres(rs.getString("kAdres"));
				ig.setkTarih(rs.getString("kTarih"));
				ig.setiAdi(rs.getString("iAdi"));
				ig.setgTarih(rs.getString("gTarih"));
				igls.add(ig);
			}
			model.addAttribute("igls", igls);
		} catch (Exception e) {
			System.err.println("DB Okuma Hatası : " + e);
		}finally {
			db.kapat();
		}
		
		// dataları gönder
		model.addAttribute("personel", personel);
		model.addAttribute("isgruplari", isgruplari);

		return "admin/isGruplariPersonel";
	}
	
	
	// isGrupKayit
	@RequestMapping(value = "/isGrupKayit", method = RequestMethod.POST)
	public String isGrupKayit(Model model, HttpServletRequest req) {
		
		boolean personelN = req.getParameter("personel") != null;
		boolean isGrupN = req.getParameter("isGrup[]") != null;
		sesi = sf.openSession(); // statement
		
		
		
		if(personelN && isGrupN) {
			String personelid = req.getParameter("personel");
			String[] gDizi = req.getParameterValues("isGrup[]");
			for (String item : gDizi) {
				Transaction tr = sesi.beginTransaction();
				IsGruplariAtama ig = new IsGruplariAtama();
				ig.setGid(Integer.MAX_VALUE);
				ig.setIid(Integer.valueOf(item));
				ig.setPid(Integer.valueOf(personelid));
				ig.setITarih(new Date());
				sesi.save(ig);
				tr.commit();
			}
		}
		
		sesi.close();
		
		/*
		System.out.println("personel : " + personel);
		for (String item : isGrup) {
			System.out.println("item : " + item);
		}
		*/
		
		return "redirect:isGruplariPersonel";
	}
	
	
}
