package com.kburaky.pojo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the isGruplariAtama database table.
 * 
 */
@Entity
@NamedQuery(name="IsGruplariAtama.findAll", query="SELECT i FROM IsGruplariAtama i")
public class IsGruplariAtama implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int gid;

	private int iid;

	@Temporal(TemporalType.TIMESTAMP)
	private Date iTarih;

	private int pid;

	public IsGruplariAtama() {
	}

	public int getGid() {
		return this.gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public int getIid() {
		return this.iid;
	}

	public void setIid(int iid) {
		this.iid = iid;
	}

	public Date getITarih() {
		return this.iTarih;
	}

	public void setITarih(Date iTarih) {
		this.iTarih = iTarih;
	}

	public int getPid() {
		return this.pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

}