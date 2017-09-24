package com.wgm.scaneqinfo.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.wgm.scaneqinfo.entity.User;

public class ClientStoreUtil {
	public static final String STORE = "user";
	public static User getUser(Context context){
		SharedPreferences sp = context.getSharedPreferences(STORE, Context.MODE_PRIVATE);
		User user = new User();
		user.setOpenId(sp.getInt("openId", -1));
		user.setMobile(sp.getString("mobile", ""));
		user.setUsername(sp.getString("username", ""));
		user.setPwd(sp.getString("pwd", ""));
		return user;
	}
	public static void setUser(Context context,User user){
		SharedPreferences.Editor sp = context.
				getSharedPreferences(STORE, Context.MODE_PRIVATE).edit();
		sp.putInt("openId", user.getOpenId());
		sp.putString("mobile", user.getMobile());
		sp.putString("username", user.getUsername());
		sp.putString("pwd", user.getPwd());
		sp.commit();
	}
	public static void deleteUser(Context context){
		SharedPreferences.Editor sp = context.
				getSharedPreferences(STORE, Context.MODE_PRIVATE).edit();
		sp.remove("pwd");
		sp.remove("openid");
		sp.remove("username");
		sp.commit();
	}
	public static void setUrl(Context context,String url){
		SharedPreferences.Editor sp = context.getSharedPreferences(STORE, Context.MODE_PRIVATE).edit();
		sp.putString("ip_url", url);
		sp.commit();
	}
	public static String getUrl(Context context){
		SharedPreferences sp = context.getSharedPreferences(STORE, Context.MODE_PRIVATE);
		String url = sp.getString("ip_url", "");
		return url;
	}
}
