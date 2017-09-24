package com.wgm.scaneqinfo.util;

import android.content.Context;
import android.content.Intent;

public class PublicWay {
	
	//������������ϴ�����Ƭ��
	public static int MAXPICCOUNT = 9;
	
	private static long clickTime = 0;
	public static void exit(Context context){
		if(System.currentTimeMillis()-clickTime>2000){
			DialogUtil.showToastMsg(context, "�ٰ�һ�κ��˼��˳�����");
			clickTime = System.currentTimeMillis();
		}
		else{
			//ִ���˳�
			System.out.println("����ִ���˳�");
			//Intent intent = new Intent("app.exit");
			//context.sendBroadcast(intent);
			AppManager.getAppManager().appExit(context);
		}
	}
}
