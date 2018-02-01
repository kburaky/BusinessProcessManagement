package com.kburaky.yetenek;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DB {
	
	
	private String dbName = "surecyonetimi?useUnicode=true&characterEncoding=UTF-8";
	private String dbUserName = "root";
	private String dbPass = "root";

	private String url = "jdbc:mysql://localhost:3306/";
	private String driver = "com.mysql.jdbc.Driver";
	
	private Connection conn = null;
	private Statement st = null;
	
	public Statement baglan(){
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url+dbName, dbUserName, dbPass);
			st = (Statement) conn.createStatement();
			System.out.println("Baglanti Basarili");
		} catch (Exception e) {
			System.err.println("Baglanti hatasi: " + e);
		}
		return st;
	}
	
	// db kapat
	public void kapat() {
		try {
			if (conn != null ) {
				conn.close();
			}
			if (st != null) {
				st.close();
			}
		} catch (Exception e) {
			System.err.println("Kapatma Hatası : " + e);
		}
	}
	
	
}
