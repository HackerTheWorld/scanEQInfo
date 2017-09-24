package com.wgm.scaneqinfo.view;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
//import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.wgm.scaneqinfo.common.SysUtil;
import com.wgm.scaneqinfo.conf.Api;
import com.wgm.scaneqinfo.conf.App;
import com.wgm.scaneqinfo.entity.User;
import com.wgm.scaneqinfo.operate.UserOperate;
import com.wgm.scaneqinfo.operate.VersionInfoOperate;
import com.wgm.scaneqinfo.util.AppManager;
import com.wgm.scaneqinfo.util.ClientStoreUtil;
import com.wgm.scaneqinfo.util.DialogUtil;
import com.wgm.scaneqinfo.util.DownlaodFileThread;
import com.wgm.scaneqinfo.util.DownlaodFileThread.DownlaodApkThread;
import com.wgm.scaneqinfo.util.FileManager;
import com.wgm.scaneqinfo.util.LoadingDialog;
import com.wgm.scaneqinfo.R;

public class IndexActivity extends BaseActivity{
	private VersionInfoOperate versionOperate = new VersionInfoOperate();
	private UserOperate userOperate = new UserOperate();
	private int type;
	private static final int WAIT_SECOND = 2000;
	private LoadingDialog laodDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_pic);
		String url = ClientStoreUtil.getUrl(getApplicationContext());
		Api.BASE_URL = url;
		checkVersion();
	}
	private void checkVersion(){
		PackageManager packageManager = this.getPackageManager();
		String version = "";
		try {
			PackageInfo packInfo = packageManager.getPackageInfo(
					this.getPackageName(), 0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		Map<String,String> params = new HashMap<String, String>();
		params.put("version", version);
		versionOperate.asyncRequest(params, new VersionInfoOperate.AsyncRequestCallBack() {
			@Override
			public void callBack() {
				if(versionOperate == null ||
						!versionOperate.checkResponseAndShowMsg(IndexActivity.this)){
					//进入主界面
					goToActivity();
					return;
				}
				final String url = versionOperate.getUrl();
				if(!SysUtil.isBlankString(url)){
					DialogUtil.showAlertMsg(IndexActivity.this, "检测到新版本",
							"是否进行升级？", "升级",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									//下载APK
									downloadApk(url);
								}
							}, "取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									goToActivity();
								}
							});
				}
			}
		});
	}
	private void goToActivity(){
		App app = (App)getApplication();
		if(app != null && app.getUser()!=null &&
				!SysUtil.isBlankString(app.getUser().getMobile())
				&& !SysUtil.isBlankString(app.getUser().getPwd())
				&& app.getUser().getOpenId() > 0 ){
			//表明用户登录过而且记住了用户
			User user = app.getUser();
			//模拟登陆
			doLogin(user.getMobile(),user.getPwd());
			//type = 1;		//跳转到MainActivity
		}
		else{
			//type = 2;		//跳转到登陆界面
			Intent intent = new Intent(IndexActivity.this,LoginActivity.class);
			startActivity(intent);
		}
		//显示启动界面
		showInitPic();
	}
	//执行模拟登录
	private void doLogin(String username,String pwd){
		Map<String,String> params = new HashMap<String,String>();
		params.put("method", "doLogin");
		params.put("username", username);
		params.put("pwd", pwd);
		userOperate.asyncRequest(params, new UserOperate.AsyncRequestCallBack() {
			@Override
			public void callBack() {
				if(!userOperate.getSuccess()){	//登陆失败
					type = 2;
					User user = new User();
					ClientStoreUtil.setUser(IndexActivity.this, user);
				}
				if(userOperate.getSuccess()){
					type = 1;
				}
				showInitPic();
				//等待X秒进行自动跳转
				new Thread(){
					@Override
					public void run() {
						super.run();
						try {
							this.sleep(WAIT_SECOND);
							Message message = showHander.obtainMessage(1);
							showHander.sendMessage(message);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				}.start();
			}
		});

	}

	private Handler showHander = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			finish();
			if(type == 1){
				Intent intent = new Intent(IndexActivity.this,MainActivity.class);
				startActivity(intent);
			}
			if(type == 2){
				Intent intent = new Intent(IndexActivity.this,LoginActivity.class);
				startActivity(intent);
			}
		}

	};

	private void showInitPic(){
		FrameLayout layout = (FrameLayout)findViewById(R.id.fl_login);
		RelativeLayout relativeLayout = new RelativeLayout(this);
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT
		);
		rlp.addRule(RelativeLayout.CENTER_IN_PARENT);
		relativeLayout.setBackgroundResource(R.drawable.welcome);
		layout.addView(relativeLayout);
	}
	public void downloadApk(String url){
		String path = "";
		if(FileManager.getInitialize().isSdCardAvailable()){
			path = FileManager.getInitialize().getSDOrCache(this, "/wgm/scanyiqi/apk/");
		}
		else
			path = FileManager.getInitialize().getSDOrCache(this, "");

		//这里用2种进度条方式进行展示

		//1  用ProgressDialog进行下载进度条提示
		/*final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setTitle("正在下载");
		dialog.setCancelable(true);
		dialog.setMax(100);
		dialog.show();*/

		//2 用自定义进度条进行模拟进度条（模拟支付宝支付进度效果）
		laodDialog = new LoadingDialog(this);
		laodDialog.show();

		DownlaodFileThread download = DownlaodFileThread.getInstance();
		DownlaodApkThread thread = new DownlaodApkThread();
		thread.setUrl(this, url, path);
		thread.setCallBack(new DownlaodFileThread.DownloadCallBack() {
			@Override
			public void setProgressBar(int rate) {
				//如果要控制百分比，则需要这行代码
				//dialog.setProgress(rate);
			}
			@Override
			public void callBack(String path) {
				//if(dialog != null){
				//	dialog.dismiss();
				//}
				handler.sendMessage(handler.obtainMessage(1000, path));
			}
		});
		download.fetchData(thread);
	}
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(final Message msg){
			final String ppath = (String)msg.obj;
			//安装
			if(msg.what == 1000){
				laodDialog.success(new LoadingDialog.CallBack() {
					@Override
					public void callback() {
						AppManager.getAppManager().finishAllActivity();
						installApk(ppath);
					}
				});

			}
		}
	};
	private void installApk(String path){
		File file = new File(path);
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		startActivity(intent);
	}
}
