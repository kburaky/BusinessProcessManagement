package com.kburaky.admin;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kburaky.pojo.IsSureci;
import com.kburaky.pojo.Kullanici;
import com.kburaky.yetenek.DB;
import com.kburaky.yetenek.Yetenekler;

import ozellikler.isSurecleriListesi;

@Controller
public class SurecYonetimiController {
	
	SessionFactory sf = new Configuration().configure().buildSessionFactory(); // connection
	Session sesi = sf.openSession(); // statement 

	// aÃ§Ä±lÄ±ÅŸ yapÄ±sÄ±
	@RequestMapping(value = "/surecYonetimi", method = RequestMethod.GET)
	public String surecYonetimi(Model model, HttpServletRequest req) {
		// data aktarÄ±mÄ± -> model
		List<isSurecleriListesi> isl = new ArrayList<isSurecleriListesi>();
		DB db = new DB();
		String seviye =  ""+req.getSession().getAttribute("seviye");
		String kid =  ""+req.getSession().getAttribute("kid");
		try {
			String q = "";
			if (seviye.equalsIgnoreCase("0") || seviye.equalsIgnoreCase("1"))
				q = "CALL isSurecPro()";
			else
				q = "CALL isSurecKulPro('"+kid+"')";
			
			ResultSet rs = db.baglan().executeQuery(q);
			while(rs.next()) {
				isSurecleriListesi is = new isSurecleriListesi();
				
				// iÅŸ süreci datalarÄ±
				is.setSid(rs.getString(1));
				is.setKid(rs.getString(2));
				is.setPid(rs.getString(3));
				is.setsBaslik(rs.getString(4));
				is.setsAciklama(rs.getString(5));
				is.setBaslamaTarihi(rs.getString(6));
				is.setBitisTarihi(rs.getString(7));
				is.setsDurum(rs.getString(8));
				
				// atama yapan personel datalarÄ±
				is.setAkid(rs.getString(9));
				is.setAkUnvan(rs.getString(10));
				is.setAkSeviye(rs.getString(11));
				is.setAkAdi(rs.getString(12));
				is.setAkSoyadi(rs.getString(13));
				is.setAkMail(rs.getString(14));
				is.setAkTelefon(rs.getString(16));
				is.setAkAdres(rs.getString(17));
				is.setAkTarih(rs.getString(18));
				
				// iÅŸi alan personel
				is.setCkid(rs.getString(19));
				is.setCkUnvan(rs.getString(20));
				is.setCkSeviye(rs.getString(21));
				is.setCkAdi(rs.getString(22));
				is.setCkSoyadi(rs.getString(23));
				is.setCkMail(rs.getString(24));
				is.setCkTelefon(rs.getString(26));
				is.setCkAdres(rs.getString(27));
				is.setCkTarih(rs.getString(28));

				isl.add(is);
			}
			model.addAttribute("isl", isl);
		} catch (Exception e) {
			System.err.println("Data Getirme Hatasý : " + e);
		}finally {
			db.kapat();
		}
		
		
		DB dbb = new DB();
		String id = ""+req.getSession().getAttribute("kid");
		try {
			ResultSet rs = dbb.baglan().executeQuery("select *from kullanici where kid = '"+id+"'");
			if(rs.next()) {
				Kullanici kl = new Kullanici();
				kl.setKAdi(rs.getString("kAdi"));
				kl.setKSoyadi(rs.getString("kSoyadi"));
				model.addAttribute("kll", kl);
			}
		} catch (Exception e) {
			System.err.println("id getirme hatasý : " + e);
		}finally {
			dbb.kapat();
		}

		
		DB dbtk = new DB();
		List<Kullanici> klls = new ArrayList<Kullanici>();
		try {
			ResultSet rs = dbb.baglan().executeQuery("select *from kullanici where kid != '"+id+"' ");
			while(rs.next()) {
				Kullanici kl = new Kullanici();
				kl.setKAdi(rs.getString("kAdi"));
				kl.setKSoyadi(rs.getString("kSoyadi"));
				kl.setKUnvan(rs.getString("kUnvan"));
				kl.setKid(rs.getInt("kid"));
				klls.add(kl);
			}
			model.addAttribute("klls", klls);
		} catch (Exception e) {
			System.err.println("dbtk hatasý : " + e);
		}finally {
			dbtk.kapat();
		}
		
		
		return "admin/surecYonetimi";
	}
	
	
	// iÅŸ süreÃ§ durum deÄŸiÅŸtir
	@RequestMapping(value = "/isDurumDegistir/{sid}/{sDurum}", method = RequestMethod.GET)
	public String isDurumDegistir(@PathVariable(value = "sid") String sid, @PathVariable(value = "sDurum") String sDurum) {
		sesi = sf.openSession(); // statement
		// eski tarih bilgilerini al
		try {
			Transaction tr = sesi.beginTransaction();
			Query query = sesi.createQuery("update IsSureci set sDurum = '"+sDurum+"'" +
					" where sid = '"+sid+"'");
			int updateSonuc = query.executeUpdate();
			tr.commit();
			System.out.println("updateSonuc : " + updateSonuc);
		} catch (Exception e) {
			System.err.println("Hata : " + e);
		}finally {
			sesi.close();
		}
		sesi.close();
		return "redirect:/surecYonetimi";
	}
	
	
	// yeni iÅŸ süreci ekle
	@RequestMapping(value = "/surecEkle", method = RequestMethod.POST)
	public String surecEkle(Model model, HttpServletRequest req) {
		
		String kidD = ""+req.getSession().getAttribute("kid");
		boolean pidD = req.getParameter("pid") != null;
		boolean sBaslikD = req.getParameter("sBaslik") != null;
		boolean sAciklamaD = req.getParameter("sAciklama") != null;
		boolean tarihD = req.getParameter("tarih") != null;
		
		if(pidD && sBaslikD && sAciklamaD && tarihD) {
			// yazma iÅŸlemini baÅŸlat
			
			String pid =  req.getParameter("pid");
			DB dbb = new DB();
			String kmail = "";
			try {
				ResultSet rs = dbb.baglan().executeQuery("select kMail from kullanici where kid = '"+pid+"'");
				if(rs.next()) {
					kmail = rs.getString("kMail");
				}
			} catch (Exception e) {
				System.err.println("Mail Getirme Hatasý : " + e);
			}finally {
				dbb.kapat();
			}
			
			String sBaslik =  req.getParameter("sBaslik");
			String sAciklama =  req.getParameter("sAciklama");
			String tarih =  req.getParameter("tarih");
			String baslamaTarihi = tarih.split(" - ")[0];
			String bitisTarihi = tarih.split(" - ")[1];
			
			DB yaz = new DB();
			try {
				int sonuc = yaz.baglan().executeUpdate("insert into isSureci values(null, '"+kidD+"', '"+pid+"', '"+sBaslik+"', '"+sAciklama+"', '"+baslamaTarihi+"', '"+bitisTarihi+"', '0')");
				if (sonuc > 0) {
					Yetenekler.sendEmail(kmail, sBaslik, sAciklama);
					model.addAttribute("basarili", "basarili");
				}
			} catch (Exception e) {
				System.err.println("Yazma hatasý : " + e);
			}finally {
				yaz.kapat();
			}
			
		}else {
			model.addAttribute("basarisiz", "basarisiz");
		}
		return "redirect:/surecYonetimi"; 
	}
	
	
	
	// iÅŸ süreÃ§ sil
	@RequestMapping(value = "/surecSil/{sid}", method = RequestMethod.GET )
	public String surecSil(@PathVariable(value = "sid") String sid) {
		sesi = sf.openSession(); // statement
		Transaction tr = sesi.beginTransaction();
		IsSureci isr = new IsSureci(Integer.valueOf(sid));
		sesi.delete(isr);
		tr.commit();
		sesi.close();
		return "redirect:/surecYonetimi";
	}
	
	
	
	// süreÃ§ düzenle
	@RequestMapping(value = "/surecDuzenle/{sid}", method = RequestMethod.GET )
	public String surecDuzenle(@PathVariable(value = "sid") String sid, Model model, HttpServletRequest req) {
		sesi = sf.openSession(); // statement
		List<IsSureci> isls = sesi.createQuery("from IsSureci where sid = '"+sid+"'").getResultList();
		model.addAttribute("isls", isls.get(0));
		
		DB dbb = new DB();
		String id = ""+req.getSession().getAttribute("kid");
		try {
			ResultSet rs = dbb.baglan().executeQuery("select *from kullanici where kid = '"+id+"'");
			if(rs.next()) {
				Kullanici kl = new Kullanici();
				kl.setKAdi(rs.getString("kAdi"));
				kl.setKSoyadi(rs.getString("kSoyadi"));
				model.addAttribute("kll", kl);
			}
		} catch (Exception e) {
			System.err.println("id getirme hatasý : " + e);
		}finally {
			dbb.kapat();
		}
		
		

		
		DB dbtk = new DB();
		List<Kullanici> klls = new ArrayList<Kullanici>();
		try {
			ResultSet rs = dbtk.baglan().executeQuery("select *from kullanici where kid != '"+id+"' ");
			while(rs.next()) {
				Kullanici kl = new Kullanici();
				kl.setKAdi(rs.getString("kAdi"));
				kl.setKSoyadi(rs.getString("kSoyadi"));
				kl.setKUnvan(rs.getString("kUnvan"));
				kl.setKid(rs.getInt("kid"));
				klls.add(kl);
			}
			model.addAttribute("klls", klls);
		} catch (Exception e) {
			System.err.println("dbtk hatasý : " + e);
		}finally {
			dbtk.kapat();
		}
		
		
		
		return "admin/surecDuzenle";
	}
	
	
	
	// surecKayitDuzenle iÅŸlemi
	@RequestMapping(value = "/surecKayitDuzenle", method = RequestMethod.POST)
	public String surecKayitDuzenle(Model model, HttpServletRequest req) {
		
		String kidD = ""+req.getSession().getAttribute("kid");
		boolean pidD = req.getParameter("pid") != null;
		boolean sBaslikD = req.getParameter("sBaslik") != null;
		boolean sAciklamaD = req.getParameter("sAciklama") != null;
		boolean tarihD = req.getParameter("tarih") != null;
		
		if(pidD && sBaslikD && sAciklamaD && tarihD) {
			String pid =  req.getParameter("pid");
			String sid =  req.getParameter("sid");
			String sBaslik =  req.getParameter("sBaslik");
			String sAciklama =  req.getParameter("sAciklama");
			String tarih =  req.getParameter("tarih");
			String baslamaTarihi = tarih.split(" - ")[0];
			String bitisTarihi = tarih.split(" - ")[1];
			
			DB db = new DB();
			try {
				int sonuc = db.baglan().executeUpdate("update isSureci set kid = '"+kidD+"', pid = '"+pid+"' , sBaslik = '"+sBaslik+"',  sAciklama = '"+sAciklama+"', baslamaTarihi = '"+baslamaTarihi+"', bitisTarihi = '"+bitisTarihi+"' where sid = '"+sid+"'");
				if(sonuc > 0) {
					model.addAttribute("basarili", "basarili");
				}else {
					model.addAttribute("basarisiz", "basarisiz");
				}
			} catch (Exception e) {
				System.err.println("Düzenleme hatasý : " + e);
				model.addAttribute("basarisiz", "basarisiz");
			}finally {
				db.kapat();
			}
		}
		
		return "redirect:/surecYonetimi";
	}
	
	
	
	
}
