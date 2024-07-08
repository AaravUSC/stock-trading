package com.example.stock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;



@RestController
@RequestMapping("/makeTrade")
public class Trade {
	@Value("${JDBCURL}")
	String JdbcURL;
	@Value("${SQLUSER}")
	String Use;
	@Value("${SQLPASSWORD}")
	String pass;
	@Value("${FINNHUBAPI}")
	String API_KEY;
    public TradeResp makeTrade(int userID, String ticker, int quantity, String name) {
		
		float price = 0;
		Gson gson = new Gson();
		int trade_success = -1;
		String link = "https://finnhub.io/api/v1/quote?symbol="+ticker+"&token="+API_KEY;
		TradeResp ret = new TradeResp();
		try {
			URL url = new URL(link);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));
			StringBuilder sb = new StringBuilder();
		    String line;
		    while ((line = br.readLine()) != null) {
		        sb.append(line);
		    }
		    JsonElement jsonObject = JsonParser.parseString(sb.toString());
		    JsonResp resp = gson.fromJson(jsonObject, JsonResp.class);
		    price = resp.c;
		    
		}
		catch(MalformedURLException ex) {
			ex.printStackTrace();
			trade_success = -2;
		}
		catch(UnsupportedEncodingException ex) {
			ex.printStackTrace();
			trade_success = -2;
		}
		catch(ProtocolException pe) {
			pe.printStackTrace();
			trade_success = -2;
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
			trade_success = -2;
		}
		catch(JsonSyntaxException formaterr) {
			formaterr.printStackTrace();
			trade_success = -2;
		}
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
			
		}
		
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;

		try {
			
			//Connection conn = null;
			
				
			conn=DriverManager.getConnection(this.JdbcURL, this.Use, this.pass);
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM assignment4.users WHERE ID=" + String.valueOf(userID) + "");
			rs.next();
			float balance = rs.getFloat("balance");
			if(quantity >= 0 && (price*quantity)<=balance) {
				st.execute("INSERT INTO assignment4.trades (ticker, quantity, price, userID, name) VALUES ('" + ticker + "', " + String.valueOf(quantity) + ", " + String.valueOf(price) + ", " + String.valueOf(userID)+", '"+name+"')");
				ret = new TradeResp(price, quantity);
				float new_balance = balance - (price*quantity);
				
				st.execute("UPDATE assignment4.users SET balance = "+String.valueOf(new_balance)+" WHERE ID = " + String.valueOf(userID));
			}
			else {
				int holding_qty = 0;
				rs = st.executeQuery("SELECT * FROM assignment4.trades WHERE userID=" + userID + " AND ticker='" + ticker+"'");
				while(rs.next()) {
					holding_qty += rs.getInt("quantity");
				}
				if((-1*quantity) <= holding_qty) {
					st.execute("INSERT INTO assignment4.trades (ticker, quantity, price, userID, name) VALUES ('" + ticker + "', " + String.valueOf(quantity) + ", " + String.valueOf(price) + ", " + String.valueOf(userID)+", '"+name+"')");
					ret = new TradeResp(price, (-1*quantity));
					float new_balance = balance - (price*quantity);
					st.execute("UPDATE assignment4.users SET balance = "+String.valueOf(new_balance)+" WHERE ID = " + String.valueOf(userID));
				}
			}
			
		}catch(SQLException sqle) {
			System.out.println("SQLException in fetching user");
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
		return ret;
	}
	
    @GetMapping
    @CrossOrigin
    public String validate(@RequestParam("ticker") String ticker, @RequestParam("quantity") String qt, @RequestParam("userID") String us, @RequestParam("name") String name){
		int quantity = Integer.parseInt(qt);
		int userID = Integer.parseInt(us);
		TradeResp tr = makeTrade(userID, ticker, quantity, name);
		Gson gson = new Gson();
		return gson.toJson(tr);
    }
}
