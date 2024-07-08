package com.example.stock;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;


@RestController
@RequestMapping("/registerUser")
public class Register {
    @Autowired
	@Value("${JDBCURL}")
	String JdbcURL;
	@Value("${SQLUSER}")
	String Use;
	@Value("${SQLPASSWORD}")
	String pass;
    public int registerUser(String username, String password, String email, String confirm) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		
		int userID = -1;
		
		try {
			conn=DriverManager.getConnection(JdbcURL, Use, pass);	
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM assignment4.users WHERE username='" + username + "'");
			
			if(!rs.next()) {
				st = conn.createStatement();
				rs = st.executeQuery("SELECT * FROM assignment4.users WHERE email='" + email + "'");
				if(!rs.next()) {
					rs.close();
					if(password.equals(confirm)) {
						st.execute("INSERT INTO assignment4.users (username, password, email, balance) VALUES ('" + username + "', '" + password + "', '" + email + "', '50000')");
						rs = st.executeQuery("SELECT LAST_INSERT_ID()");
						rs.next();
						userID = rs.getInt(1);
					}
					else {
						userID = -3;
					}
					
				}
				else {
					userID = -2;
				}
			}
		}catch(SQLException sqle) {
			System.out.println("SQLException in register user");
			sqle.printStackTrace();
		}finally {
			try {
				if(rs!=null) {
					rs.close();		
				}
				if(st!=null) {
					st.close();
				}
				if(conn!=null) {
					conn.close();
				}
			}catch(SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
		return userID;
	}

    @GetMapping
    @CrossOrigin
    public String validate(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("email") String email, @RequestParam("confirm") String confirm){

		int userID = registerUser(username, password, email, confirm);
		Gson gson = new Gson();
		return gson.toJson(userID);
		
    }
	
}
