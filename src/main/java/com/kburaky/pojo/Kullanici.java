package com.kburaky.pojo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the kullanici database table.
 * 
 */
@Entity
@NamedQuery(name="Kullanici.findAll", query="SELECT k FROM Kullanici k")
public class Kullanici implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int kid;

	private String kAdi;

	@Lob
	private String kAdres;

	private String kMail;

	private int kSeviye;

	private String kSifre;

	private String kSoyadi;

	@Temporal(TemporalType.TIMESTAMP)
	private Date kTarih;

	private String kTelefon;

	private String kUnvan;

	public Kullanici() {
	}

	public int getKid() {
		return this.kid;
	}

	public void setKid(int kid) {
		this.kid = kid;
	}

	public String getKAdi() {
		return this.kAdi;
	}

	public void setKAdi(String kAdi) {
		this.kAdi = kAdi;
	}

	public String getKAdres() {
		return this.kAdres;
	}

	public void setKAdres(String kAdres) {
		this.kAdres = kAdres;
	}

	public String getKMail() {
		return this.kMail;
	}

	public void setKMail(String kMail) {
		this.kMail = kMail;
	}

	public int getKSeviye() {
		return this.kSeviye;
	}

	public void setKSeviye(int kSeviye) {
		this.kSeviye = kSeviye;
	}

	public String getKSifre() {
		return this.kSifre;
	}

	public void setKSifre(String kSifre) {
		this.kSifre = kSifre;
	}

	public String getKSoyadi() {
		return this.kSoyadi;
	}

	public void setKSoyadi(String kSoyadi) {
		this.kSoyadi = kSoyadi;
	}

	public Date getKTarih() {
		return this.kTarih;
	}

	public void setKTarih(Date kTarih) {
		this.kTarih = kTarih;
	}

	public String getKTelefon() {
		return this.kTelefon;
	}

	public void setKTelefon(String kTelefon) {
		this.kTelefon = kTelefon;
	}

	public String getKUnvan() {
		return this.kUnvan;
	}

	public void setKUnvan(String kUnvan) {
		this.kUnvan = kUnvan;
	}
	
	public Kullanici(int kid) {
		this.kid = kid;
	}
	
	
	

}