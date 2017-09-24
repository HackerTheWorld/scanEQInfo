package com.wgm.scaneqinfo.util;

import com.wgm.scaneqinfo.common.SysUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class DialogUtil {
	/**
	 * 简单提示框,只有一个确定按钮
	 * @param context
	 * @param title
	 * @param msg
	 */
	public static void showAlertMsg(Context context,String title,String msg){
		showAlertMsg(context, title,msg,"确定",null,null,null);
	}
	/**
	 * 简单提示框，只有一个确定按钮，标题为固定字符串   提示
	 * @param context
	 * @param msg
	 */
	public static void showAlertMsg(Context context,String msg){
		showAlertMsg(context,"提示",msg);
	}
	public static void showAlertMsg(Context context,String title,String msg,
									DialogInterface.OnClickListener positiveButton){
		showAlertMsg(context, title,msg,"确定",positiveButton,null,null);
	}
	/**
	 * 简单提示框，包含确定，取消按钮以及事件
	 * @param context
	 * @param title
	 * @param msg
	 * @param positiveText
	 * @param positiveButton
	 * @param cancelText
	 * @param cancelButton
	 */
	public static void showAlertMsg(Context context,String title,String msg,String positiveText,
									final DialogInterface.OnClickListener positiveButton,String cancelText,
									final DialogInterface.OnClickListener cancelButton){
		if(context instanceof Activity){
			if(((Activity)context).isFinishing())
				return;
		}
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.setTitle(title);
		dialog.setMessage(msg);
		dialog.setButton(positiveText, positiveButton);
		dialog.setButton2(cancelText, cancelButton);
		dialog.show();
	}

	public static void showToastMsg(Context context,String msg){
		if(!SysUtil.isBlankString(msg)){
			Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
		}
	}

	public static void showToastMsgShort(Context context,String msg){
		if(!SysUtil.isBlankString(msg))
			Toast.makeText(context, msg, Toast.LENGTH_SHORT);
	}
}
