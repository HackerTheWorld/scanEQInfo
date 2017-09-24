package com.wgm.scaneqinfo.conf;

import com.wgm.scaneqinfo.entity.User;
import com.wgm.scaneqinfo.util.ClientStoreUtil;

import android.app.Application;
import android.util.Log;

public class App extends Application{
	private User user;
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(Setting.TAG, "App onCreate()");
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.d(Setting.TAG, "App OnTerminate()");
	}

	public User getUser() {
		if(user == null){
			user = ClientStoreUtil.getUser(getApplicationContext());
		}
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
