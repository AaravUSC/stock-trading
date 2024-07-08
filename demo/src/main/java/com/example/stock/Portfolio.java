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
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

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
@RequestMapping("/portfolio")
public class Portfolio {
	@Value("${JDBCURL}")
	String JdbcURL;
	@Value("${SQLUSER}")
	String Use;
	@Value("${SQLPASSWORD}")
	String pass;
	@Value("${FINNHUBAPI}")
	String API_KEY;
    protected PortResp getPort(String userID) {
		PortResp ret = new PortResp();
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		Map<String, Integer> tickToQ = new HashMap<>();
		Map<String, Integer> tickToQP = new HashMap<>();
		Map<String, Float> tickToT = new HashMap<>();
		Map<String, String> tickToName = new HashMap<>();
		try {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				
			}catch(ClassNotFoundException e) {
				e.printStackTrace();
				
			}

			conn=DriverManager.getConnection(JdbcURL, Use, pass);
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM assignment4.users WHERE ID=" + userID);
			float balance = 0;
			while(rs.next()) {
				balance = rs.getFloat("balance");
			}
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM assignment4.trades WHERE userID=" + userID);
			while(rs.next()) {
				String tick = rs.getString("ticker");
				String name = rs.getString("name");
				int qty = rs.getInt("quantity");
				float price = rs.getFloat("price");
				if(!tickToQ.containsKey(tick)) {
					tickToQ.put(tick, 0);
					tickToT.put(tick, (float)0);
					tickToQP.put(tick, 0);
					tickToName.put(tick, name);
				}
				tickToQ.put(tick, tickToQ.get(tick)+qty);
				
				if(qty > 0) {
					tickToQP.put(tick, tickToQP.get(tick)+qty);
					tickToT.put(tick, tickToT.get(tick)+(qty*price));
				}
			}
			float stock_val = 0;
			Vector<PortItem> pitems = new Vector<PortItem>();
			for (Map.Entry<String, Integer> entry : tickToQ.entrySet()) {
	            String ticker = entry.getKey();
	            String link = "https://finnhub.io/api/v1/quote?symbol="+ticker+"&token="+API_KEY;
	            String name = tickToName.get(ticker);
	            float price = 0;
	            float change = 0;
	            try {
	            	Gson gson = new Gson();
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
				    change = resp.d;
				    if(tickToQ.get(ticker) > 0) {
				    	float avg = tickToT.get(ticker)/tickToQP.get(ticker);
					    float mkt = (tickToQ.get(ticker)*price);
					    stock_val += mkt;
					    pitems.add(new PortItem(tickToQ.get(ticker), avg, (tickToT.get(ticker)*tickToQ.get(ticker))/tickToQP.get(ticker), change, price, mkt, ticker, name));
				    }
				    
				    
				    
				}
				catch(MalformedURLException ex) {
					ex.printStackTrace();
				}
				catch(UnsupportedEncodingException ex) {
					ex.printStackTrace();
				}
				catch(ProtocolException pe) {
					pe.printStackTrace();
				}
				catch(IOException ioe) {
					ioe.printStackTrace();
				}
				catch(JsonSyntaxException formaterr) {
					formaterr.printStackTrace();
				}
	            
	        }
			float acc_val = balance + stock_val;
			ret = new PortResp(balance, acc_val, pitems);
			
			
		}catch(SQLException sqle) {
			
			System.out.println("SQL error in portfolio");
			sqle.printStackTrace();
		}
		return ret;
		
	}
	@GetMapping
	@CrossOrigin
    public String loadPortfolio(@RequestParam("userID") String userID){
		PortResp pr = getPort(userID);
		Gson gson = new Gson();
		return gson.toJson(pr);
    }
	
}
