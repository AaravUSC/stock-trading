package com.example.stock;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.net.ProtocolException;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

@RestController
@RequestMapping("/fetch")
public class Fetch {
    @Autowired
    public static OverallResp getInfo(String ticker) {
		String API_KEY = "cnv1umhr01qub9j07570cnv1umhr01qub9j0757g";
		String link = "https://finnhub.io/api/v1/quote?symbol="+ticker+"&token="+API_KEY;
		String proflink = "https://finnhub.io/api/v1/stock/profile2?symbol="+ticker+"&token="+API_KEY;
		OverallResp resp = new OverallResp();
		
		try {
			URL url = new URL(link);
			URL url_ = new URL(proflink);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			HttpURLConnection con_ = (HttpURLConnection) url_.openConnection();
			con.setRequestMethod("GET");
			BufferedReader br = new BufferedReader(new InputStreamReader( con.getInputStream(),"utf-8"));
			StringBuilder sb = new StringBuilder();
		    String line;
		    while ((line = br.readLine()) != null) {
		        sb.append(line);
		    }
		    Gson gson = new Gson();
		    JsonElement jsonObject = JsonParser.parseString(sb.toString());
		    JsonResp res_ = gson.fromJson(jsonObject, JsonResp.class);
		    
		    br = new BufferedReader(new InputStreamReader( con_.getInputStream(),"utf-8"));
			sb = new StringBuilder();
		    while ((line = br.readLine()) != null) {
		        sb.append(line);
		    }
		    jsonObject = JsonParser.parseString(sb.toString());
		    ProfResp res = gson.fromJson(jsonObject, ProfResp.class);
		    
		    OverallResp test = new OverallResp(res_, res);
		    resp = test;
  
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
		return resp;
    }

    @GetMapping
    @CrossOrigin
    public String getStock(@RequestParam("ticker") String ticker){
		OverallResp resp = getInfo(ticker);
		Gson gson = new Gson();
		return gson.toJson(resp);
    }
    
}




