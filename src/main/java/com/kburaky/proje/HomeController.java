package com.kburaky.proje;

import java.sql.SQLException;
import java.util.Date;
import java.util.Locale;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kburaky.pojo.IsGruplari;
import com.kburaky.yetenek.DB;


/*
 * SessionFactory sf = new Configuration().configure().buildSessionFactory(); // connection
	Session sesi = sf.openSession(); // statement 
 * */


@Controller
public class HomeController {
	
	SessionFactory sf = new Configuration().configure().buildSessionFactory();
	Session sesi = sf.openSession();
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		
		/*
		Transaction tr = sesi.beginTransaction();
		IsGruplari ig = new IsGruplari();
		ig.setIid(Integer.MAX_VALUE);
		ig.setIadi("ÜĞİŞÇÖüğişçö");
		ig.setGTarih(new Date());
		
		sesi.save(ig);
		tr.commit();
		
		
		try {
			new DB().baglan().executeUpdate("insert into isGruplari values(null, 'Cağrı Bilsin', now() )");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/

		return "home";
	}
	
}
