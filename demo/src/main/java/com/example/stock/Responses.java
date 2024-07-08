package com.example.stock;

import java.util.Vector;

public class Responses {
    
}

class ProfResp{
	String country;
	String currency;
	String estimateCurrency;
	String exchange;
	String finnhubIndustry;
	String ipo;
	String logo;
	String marketCapitalization;
	String name;
	String phone;
	String shareOutstanding;
	String ticker;
	String weburl;
	public ProfResp(String country, String currency, String estimateCurrency, String exchange, String finnhubIndustry, String ipo, String logo, String marketCapitalization, String name, String phone, String weburl, String shareOutstanding, String ticker) {
		this.country = country;
		this.currency = currency;
		this.estimateCurrency = estimateCurrency;
		this.exchange = exchange;
		this.finnhubIndustry = finnhubIndustry;
		this.ipo = ipo;
		this.logo = logo;
		this.marketCapitalization = marketCapitalization;
		this.name = name;
		this.phone = phone;
		this.shareOutstanding = shareOutstanding;
		this.ticker = ticker;
		this.weburl = weburl;
	}
	
}

class OverallResp{
	float c;
	float d;
	float dp;
	float h;
	float l;
	float o;
	float pc;
	long t;
	String country;
	String currency;
	String estimateCurrency;
	String exchange;
	String finnhubIndustry;
	String ipo;
	String logo;
	String marketCapitalization;
	String name;
	String phone;
	String shareOutstanding;
	String ticker;
	String weburl;
	public OverallResp(JsonResp j, ProfResp p) {
		this.country = p.country;
		this.currency = p.currency;
		this.estimateCurrency = p.estimateCurrency;
		this.exchange = p.exchange;
		this.finnhubIndustry = p.finnhubIndustry;
		this.ipo = p.ipo;
		this.logo = p.logo;
		this.marketCapitalization = p.marketCapitalization;
		this.name = p.name;
		this.phone = p.phone;
		this.shareOutstanding = p.shareOutstanding;
		this.ticker = p.ticker;
		this.weburl = p.weburl;
		
		this.c = j.c;
		this.d = j.d;
		this.dp = j.dp;
		this.h = j.h;
		this.l = j.l;
		this.o = j.o;
		this.pc = j.pc;
		this.t = j.t;
	}
	
	public OverallResp() {
		this.country = "";
		this.currency = "";
		this.estimateCurrency = "";
		this.exchange = "";
		this.finnhubIndustry = "";
		this.ipo = "";
		this.logo = "";
		this.name = "";
		this.marketCapitalization = "";
		this.phone = "";
		this.shareOutstanding = "";
		this.ticker = "";
		this.weburl = "";
		
		this.c = 0;
		this.d = 0;
		this.dp = 0;
		this.h = 0;
		this.l = 0;
		this.o = 0;
		this.pc = 0;
		this.t = 0;
	}
	
	
}

class TradeHolder{
	String ticker;
	int quantity;
	int userID;
	public TradeHolder(String ticker, int quantity, int userID) {
		this.ticker = ticker;
		this.quantity = quantity;
		this.userID = userID;
	}
	
}

class JsonResp{
	float c;
	float d;
	float dp;
	float h;
	float l;
	float o;
	float pc;
	long t;
	JsonResp(float c, float d, float dp, float h, float l, float o, float pc, long t){
		this.c = c;
		this.d = d;
		this.dp = dp;
		this.h = h;
		this.l = l;
		this.o = o;
		this.pc = pc;
		this.t = t;
	}
	JsonResp(){
		this.c = 0;
		this.d = 0;
		this.dp = 0;
		this.h = 0;
		this.l = 0;
		this.o = 0;
		this.pc = 0;
		this.t = 0;
	}
}

class TradeResp{
	float price;
	int quantity;
	boolean success;
	TradeResp(float price, int quantity){
		this.price = price;
		this.quantity = quantity;
		this.success = true;
	}
	TradeResp(){
		this.price = 0;
		this.quantity = 0;
		this.success = false;
	}
}

class LoginCredentials{
	String username;
	String password;
	public LoginCredentials(String username, String password) {
		this.username = username;
		this.password = password;
	}
}

class PortResp{
	float bal;
	float accval;
	Vector<PortItem> ptitems;
	PortResp(float bal, float accval, Vector<PortItem> ptitems){
		this.bal = bal;
		this.accval = accval;
		this.ptitems = ptitems;
	}
	PortResp(){
		this.bal = 0;
		this.accval = 0;
		this.ptitems = new Vector<PortItem>();
	}
}

class PortItem{
	int qty;
	float avg;
	float total;
	float change;
	float curr;
	float mkt;
	String ticker;
	String name;
	PortItem(int qty, float avg, float total, float change, float curr, float mkt, String ticker, String name){
		this.qty = qty;
		this.avg = avg;
		this.total = total;
		this.change = change;
		this.curr = curr;
		this.mkt = mkt;
		this.ticker = ticker;
		this.name = name;
	}
	PortItem(){
		this.qty = 0;
		this.avg = 0;
		this.total = 0;
		this.change = 0;
		this.curr = 0;
		this.mkt = 0;
	}
}

class User{
	String username;
	String password;
	String email;
	String confirm;
	
	
	public User(String username, String password, String email, String confirm) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.confirm = confirm;
	}
}