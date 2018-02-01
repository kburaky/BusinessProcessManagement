package com.kburaky.pojo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

// http://download.eclipse.org/datatools/1.14.0.201701131441/repository/

/**
 * The persistent class for the isGruplari database table.
 * 
 */
@Entity
@NamedQuery(name="IsGruplari.findAll", query="SELECT i FROM IsGruplari i")
public class IsGruplari implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int iid;

	@Temporal(TemporalType.TIMESTAMP)
	private Date gTarih;

	private String iadi;

	public IsGruplari() {
	}

	public int getIid() {
		return this.iid;
	}

	public void setIid(int iid) {
		this.iid = iid;
	}

	public Date getGTarih() {
		return this.gTarih;
	}

	public void setGTarih(Date gTarih) {
		this.gTarih = gTarih;
	}

	public String getIadi() {
		return this.iadi;
	}

	public void setIadi(String iadi) {
		this.iadi = iadi;
	}
	
	public IsGruplari(int iid) {
		this.iid = iid;
	}

}