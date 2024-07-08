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
@RequestMapping("/loginUser")
public class Login {
    @Autowired
	@Value("${JDBCURL}")
	String JdbcURL;
	@Value("${SQLUSER}")
	String Use;
	@Value("${SQLPASSWORD}")
	String pass;
    public int loginUser(String username, String password) {
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
			if(rs.next()) {
				String pword = rs.getString("password");
				if(!pword.equals(password)) {
					userID = -2;
				}
				else {
					userID = rs.getInt("ID");
				}
			}
			
		}catch(SQLException sqle) {
			System.out.println("SQLException in login user");
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
    public String validate(@RequestParam("username") String username, @RequestParam("password") String password){
		Gson gson = new Gson();
		int userID = 0;
		if(username==null||username.isBlank()||password==null||password.isBlank()) {
			userID = -3;
			
			return gson.toJson(userID);
		}
		userID = loginUser(username, password);
		return gson.toJson(userID);
    }


}
