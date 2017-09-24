package com.wgm.scaneqinfo.util;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

/**
 * ����Ӧ��APP��Activity
 * @author user
 *
 */
public class AppManager {
	private static Stack<Activity> activityStack;
	private static AppManager instance;
	private AppManager(){
		
	}
	public static AppManager getAppManager(){
		if(instance == null){
			instance = new AppManager();
		}
		return instance;
	}
	/**
	 * ���Activity��ջ
	 */
	public void addActivity(Activity activity){
		if(activityStack == null)
			activityStack = new Stack<Activity>();
		activityStack.add(activity);
	}
	/**
	 * ��ȡ��ǰ��Activity����ջ�����һ��ѹ��Ķ���
	 * @return
	 */
	public Activity currentActivity(){
		Activity activity = activityStack.lastElement();
		return activity;
	}
	/**
	 * ������ǰActivity
	 */
	public void finishActivity(){
		Activity activity = activityStack.lastElement();
		if(activity != null){
			activity.finish();
			activity = null;
		}
	}
	/**
	 * ����ָ����Activity
	 * @param activity
	 */
	public void finishActivity(Activity activity){
		if(activity != null){
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}
	public void finishAllActivity(){
		for(int i=0,size=activityStack.size();i<size;i++){
			if(null != activityStack.get(i)){
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}
	public void appExit(Context context){
		finishAllActivity();
		/*ActivityManager activityMgr = (ActivityManager)context.getSystemService(
				Context.ACTIVITY_SERVICE);
		activityMgr.restartPackage(context.getPackageName());*/
		System.exit(0);
	}
}
