package com.kburaky.pojo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the isSureci database table.
 * 
 */
@Entity
@NamedQuery(name="IsSureci.findAll", query="SELECT i FROM IsSureci i")
public class IsSureci implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int sid;

	@Temporal(TemporalType.TIMESTAMP)
	private Date baslamaTarihi;

	@Temporal(TemporalType.TIMESTAMP)
	private Date bitisTarihi;

	private int kid;

	private int pid;

	private String sBaslik;
	
	private String sAciklama;

	private int sDurum;

	public IsSureci() {
	}

	public int getSid() {
		return this.sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public Date getBaslamaTarihi() {
		return this.baslamaTarihi;
	}

	public void setBaslamaTarihi(Date baslamaTarihi) {
		this.baslamaTarihi = baslamaTarihi;
	}

	public Date getBitisTarihi() {
		return this.bitisTarihi;
	}

	public void setBitisTarihi(Date bitisTarihi) {
		this.bitisTarihi = bitisTarihi;
	}

	public int getKid() {
		return this.kid;
	}

	public void setKid(int kid) {
		this.kid = kid;
	}

	public int getPid() {
		return this.pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getSBaslik() {
		return this.sBaslik;
	}

	public void setSBaslik(String sBaslik) {
		this.sBaslik = sBaslik;
	}

	
	public String getSAciklama() {
		return this.sAciklama;
	}

	public void setSAciklama(String sAciklama) {
		this.sAciklama = sAciklama;
	}
	
	public int getSDurum() {
		return this.sDurum;
	}

	public void setSDurum(int sDurum) {
		this.sDurum = sDurum;
	}
	
	public IsSureci(int sid) {
		this.sid = sid;
	}
	

}