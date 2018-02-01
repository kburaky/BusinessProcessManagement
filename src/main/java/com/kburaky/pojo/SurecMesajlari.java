package com.kburaky.pojo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the surecMesajlari database table.
 * 
 */
@Entity
@NamedQuery(name="SurecMesajlari.findAll", query="SELECT s FROM SurecMesajlari s")
public class SurecMesajlari implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int mid;

	private int aliciID;

	private int gonderenID;

	private String mesajDosya;

	@Lob
	private String mesajText;

	@Temporal(TemporalType.TIMESTAMP)
	private Date mTarih;

	private int okunduDurum;

	private int sid;

	public SurecMesajlari() {
	}

	public int getMid() {
		return this.mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public int getAliciID() {
		return this.aliciID;
	}

	public void setAliciID(int aliciID) {
		this.aliciID = aliciID;
	}

	public int getGonderenID() {
		return this.gonderenID;
	}

	public void setGonderenID(int gonderenID) {
		this.gonderenID = gonderenID;
	}

	public String getMesajDosya() {
		return this.mesajDosya;
	}

	public void setMesajDosya(String mesajDosya) {
		this.mesajDosya = mesajDosya;
	}

	public String getMesajText() {
		return this.mesajText;
	}

	public void setMesajText(String mesajText) {
		this.mesajText = mesajText;
	}

	public Date getMTarih() {
		return this.mTarih;
	}

	public void setMTarih(Date mTarih) {
		this.mTarih = mTarih;
	}

	public int getOkunduDurum() {
		return this.okunduDurum;
	}

	public void setOkunduDurum(int okunduDurum) {
		this.okunduDurum = okunduDurum;
	}

	public int getSid() {
		return this.sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

}