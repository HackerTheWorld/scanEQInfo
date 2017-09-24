package com.wgm.scaneqinfo.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.wgm.scaneqinfo.conf.App;
import com.wgm.scaneqinfo.entity.User;
import com.wgm.scaneqinfo.util.AppManager;
import com.wgm.scaneqinfo.util.DialogUtil;
import com.wgm.scaneqinfo.util.PublicWay;

public class BaseActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//registerReceiver(broadcastReceiver, new IntentFilter("app.exit"));
		AppManager.getAppManager().addActivity(this);
	}
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		PublicWay.exit(this);
	}
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
		}
	};
	
}
