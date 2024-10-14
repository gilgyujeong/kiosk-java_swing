package kiosk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBsetrting {

	Connection conn;
	Statement stmt;
	
	public DBsetrting() {
		dbcon();
		try {
			stmt.execute("set global local_infile='on'");
			stmt.execute("drop database if exists kiosk");
			stmt.execute("create database kiosk");
			stmt.execute("use kiosk");
//			stmt.execute("create table menu(m_no int primary key not null auto_increment, m_category varchar(10), m_name varchar(30), m_amount int)");
			stmt.execute("load data local infile './DataFiles/menu.txt' into table menu ignore 1 lines");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void dbcon() {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306?allowLoadLocalInfile=true&serverTimezone=UTC", "root", "#mysql123");
			stmt = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		new DBsetrting();

	}

}
