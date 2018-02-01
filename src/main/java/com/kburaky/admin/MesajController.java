package com.kburaky.admin;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.kburaky.yetenek.DB;

import ozellikler.isSurecleriListesi;
import ozellikler.mesajOzellik;

@Controller
public class MesajController {
	
	DB db = new DB();
	@RequestMapping(value = "/mesaj/{surecID}", method = RequestMethod.GET)
	public String mesajAc(Model model, @PathVariable(value = "surecID") String surecID, HttpServletRequest req) {
		try {
			String quer = "SELECT * FROM issureci as isu LEFT JOIN kullanici as kl on isu.pid = kl.kid WHERE isu.sid = '"+surecID+"'";
			ResultSet rs = db.baglan().executeQuery(quer);
			List<isSurecleriListesi> ls = new ArrayList<isSurecleriListesi>();
			if(rs.next()) {
				isSurecleriListesi is = new isSurecleriListesi();
				is.setSid(rs.getString("sid"));
				is.setsBaslik(rs.getString("sBaslik"));
				is.setsAciklama(rs.getString("sAciklama"));
				is.setBaslamaTarihi(rs.getString("baslamaTarihi"));
				is.setBitisTarihi(rs.getString("bitisTarihi"));
				is.setCkUnvan(rs.getString("kUnvan"));
				is.setCkAdi(rs.getString("kAdi"));
				is.setCkSoyadi(rs.getString("kSoyadi"));
				is.setPid(rs.getString("pid"));
				is.setKid(rs.getString("kid"));
				is.setsDurum(rs.getString("sDurum"));
				ls.add(is);
				}
			model.addAttribute("surecData", ls.get(0));
		} catch (Exception e) {
			System.err.println("Mesaj Getirme Hatasý : " + e);
		}finally {
			db.kapat();
		}
		
		try {
			db = new DB();
			String qq = "SELECT sm.sid, sm.gonderenID, sm.mesajText, kl.kAdi as gAdi, kl.kSoyadi as gSoyadi, kll.kAdi as aAdi, kll.kSoyadi as aSoyadi, sm.mTarih FROM surecmesajlari as sm   LEFT JOIN kullanici as kl on kl.kid = sm.gonderenID LEFT JOIN kullanici as kll on kll.kid = sm.aliciID  WHERE sm.sid = '"+surecID+"' ORDER BY sm.mTarih DESC ";
			System.out.println("sorgu : " + qq);
			ResultSet rs = db.baglan().executeQuery(qq);
			List<mesajOzellik> ms = new ArrayList<mesajOzellik>();
			while(rs.next()) {
				mesajOzellik mo = new mesajOzellik();
				mo.setSid(rs.getString(1));
				mo.setGonderenID(rs.getString(2));
				mo.setMesajText(rs.getString(3));
				mo.setgAdi(rs.getString(4));
				mo.setgSoyadi(rs.getString(5));
				mo.setaAdi(rs.getString(6));
				mo.setaSoyadi(rs.getString(7));
				mo.setmTarih(rs.getString(8));
				mo.setSesiID(String.valueOf(req.getSession().getAttribute("kid")));
				ms.add(mo);
			}
			
			model.addAttribute("ms", ms);
		} catch (Exception e) {
			System.err.println("Mesaj getirme hatasý : " + e);
		}finally {
			db.kapat();
		}
		
		
		return "admin/mesaj";
	}

	
	// mesaj kaydetme bölümü
	@RequestMapping(value = "/mesajKaydet", method = RequestMethod.POST)
	public String mesajKaydet(Model model ,@RequestParam String kid, @RequestParam String sid, @RequestParam String msj, @RequestParam String pid, HttpServletRequest req) {
		
		String gonderenID = String.valueOf(req.getSession().getAttribute("kid"));
		String seviye = String.valueOf(req.getSession().getAttribute("seviye"));
		String aliciID = pid;
		String aliciAdminID = kid;
		
		String g = "";
		String a = "";
		if(seviye.equals("0")) {
			// gonderenID
			// aliciID
			g = gonderenID;
			a = aliciID;
		}

		if(seviye.equals("2")) {
			// gonderenID
			// aliciAdminID
			g = gonderenID;
			a = aliciAdminID;
		}
		DB db = new DB();
		try {
			int mesajYazsonuc =  db.baglan().executeUpdate("insert into surecmesajlari values( null, '"+sid+"', '"+g+"', '"+a+"', '"+msj+"', '', '0', now() )");
			if(mesajYazsonuc >0) {
				model.addAttribute("basarili", "basarili");
			}
		} catch (Exception e) {
			System.err.println("Mesaj yazma hatasý : " + e);
		}finally {
			db.kapat();
		}
		
		return "redirect:/mesaj/"+sid;
	}
	
	// surecý tamamla
	@RequestMapping(value = "/surecTamamla/{sid}")
	public String surecTamamla(@PathVariable(value="sid") String sid ) {
		DB db = new DB();
		try {
			int sonuc = db.baglan().executeUpdate("update issureci set sDurum = 1 where sid = '"+sid+"'");
		} catch (Exception e) {
			System.err.println("Surec Tamamlama Hatasi : " + e);
		}finally {
			db.kapat();
		}
		return "redirect:/mesaj/"+sid;
	} 
	
	
	
}
