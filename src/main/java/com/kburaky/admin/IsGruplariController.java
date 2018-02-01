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
import com.kburaky.pojo.IsGruplari;

@Controller
public class IsGruplariController {

	SessionFactory sf = new Configuration().configure().buildSessionFactory();
	Session sesi = sf.openSession();

	@RequestMapping(value = "/isGruplari", method = RequestMethod.GET)
	public String isGruplari(Model model, HttpServletRequest req) {
		sesi = sf.openSession();
		List<IsGruplari> ls = sesi.createQuery("from IsGruplari").list();
		model.addAttribute("isGruplistesi", ls);
		sesi.close();
		boolean basarili = req.getParameter("basarili") != null;
		boolean basarisiz = req.getParameter("basarisiz") != null;
		if (basarili) {

			model.addAttribute("basarili", "basarili");
			System.out.println("basarili çalıştı");
		}
		if (basarisiz) {

			model.addAttribute("basarisiz", "basarisiz");
			System.out.println("basarisiz çalıştı");
		}

		return "admin/isGruplari";
	}

	@RequestMapping(value = "/isGrubuKayit", method = RequestMethod.POST)
	public String isGrubuKayit(HttpServletRequest req, Model model) {
		boolean adiDurum = req.getParameter("isGrubuAdi") != null;
		if (adiDurum) {
			sesi = sf.openSession();
			Transaction tr = sesi.beginTransaction();
			String adi = req.getParameter("isGrubuAdi");
			IsGruplari ig = new IsGruplari();
			ig.setIid(Integer.MAX_VALUE);
			ig.setIadi(adi);
			ig.setGTarih(new Date());

			try {
				sesi.save(ig);
				tr.commit();
				model.addAttribute("basarili", "basarili");
			} catch (Exception e) {
				model.addAttribute("basarisiz", "basarisiz");
			} finally {
				sesi.close();
			}
		} else {
			model.addAttribute("basarisiz", "basarisiz");
		}
		return "redirect:/isGruplari";
	}

	// personel silme
	@RequestMapping(value = "/isGrubuSil/{igid}", method = RequestMethod.GET)
	public String personelSil(@PathVariable(value = "igid") int igid, Model model) {
		sesi = sf.openSession();
		Transaction tr = sesi.beginTransaction();
		IsGruplari kl = new IsGruplari(igid);
		sesi.delete(kl);
		tr.commit();
		sesi.close();
		return "redirect:/isGruplari";
	}

	// düzenle aç
	@RequestMapping(value = "/isGrubuDuzenle/{igid}", method = RequestMethod.GET)
	public String isGrubuDuzenle(Model model, @PathVariable(value = "igid") int igid) {
		sesi = sf.openSession();
		List<IsGruplari> ls = sesi.createQuery("from IsGruplari where iid = '" + igid + "'").list();
		model.addAttribute("isGurubu", ls.get(0));
		return "admin/isGrubuDuzenle";
	}

	
	// düzenle kayıt
	@RequestMapping(value = "/isGurubuKayitDuzenle", method = RequestMethod.POST)
	public String isGurubuKayitDuzenle(Model model, HttpServletRequest req) {

		sesi = sf.openSession();
		Session sesiup = sf.openSession();
		
		Transaction tr = sesi.beginTransaction();
		String id = req.getParameter("igid");
		String adi = req.getParameter("iadi");
		
		IsGruplari ig = new IsGruplari(Integer.valueOf(id));
		ig.setIadi(adi);
		List<IsGruplari> ls = sesiup.createQuery("from IsGruplari where iid = '" + id + "'").list();
		ig.setGTarih(ls.get(0).getGTarih());
		
		sesi.update(ig);
		tr.commit();
		sesi.close();
		sesiup.close();
		
		return "redirect:/isGruplari";
	}

}
