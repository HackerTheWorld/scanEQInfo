package com.wgm.scaneqinfo.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.wgm.scaneqinfo.common.SysUtil;
import com.wgm.scaneqinfo.conf.App;
import com.wgm.scaneqinfo.entity.User;
import com.wgm.scaneqinfo.operate.UserOperate;
import com.wgm.scaneqinfo.util.ClientStoreUtil;
import com.wgm.scaneqinfo.util.DialogUtil;
import com.wgm.scaneqinfo.R;

public class LoginActivity extends BaseActivity implements OnClickListener{
	private EditText et_username;
	private EditText et_pwd;
	private Button btn_login;
	private Button btn_setting;
	private UserOperate userOperate = new UserOperate();

	private List<String> datas = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login);
		initView();
	}
	private void initView(){
		et_username = (EditText)this.findViewById(R.id.userId);
		et_pwd = (EditText)this.findViewById(R.id.pwdId);
		btn_login = (Button)this.findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		btn_setting=(Button)this.findViewById(R.id.btn_setting);
		btn_setting.setOnClickListener(this);
		App app = (App)getApplication();
		User user = app.getUser();
		if(user !=null && !SysUtil.isBlankString(user.getMobile())){
			et_username.setText(user.getMobile());
		}
	}

	@Override
	public void onClick(View v) {
		if(v == btn_login) {
			doLogin();
		}
		if(v== btn_setting){
			Intent intent = new Intent(LoginActivity.this,SetconnectActivity.class);
			startActivity(intent);
		}
	}
	//执行登录
	private void doLogin(){
		final String username = et_username.getText().toString();
		final String pwd = et_pwd.getText().toString();
		if(SysUtil.isBlankString(username)){

			et_username.requestFocus();
			return;
		}
		if(SysUtil.isBlankString(pwd)){

			et_pwd.requestFocus();
			return;
		}

		Map<String,String> params = new HashMap<String,String>();
		params.put("method", "user.login");
		params.put("username", username);
		params.put("password", pwd);
		userOperate.asyncRequest(params, new UserOperate.AsyncRequestCallBack() {
			@Override
			public void callBack() {
				if(!userOperate.getSuccess()){
					DialogUtil.showAlertMsg(LoginActivity.this, "用户名或密码错误！");
					return;
				}
				else {
					//DialogUtil.showAlertMsg(LoginActivity.this, "登录成功！");
					Intent intent = new Intent(LoginActivity.this,MainActivity.class);
					startActivity(intent);
					//userService.insertMobile(username);
					App app = (App)getApplication();
					User user = new User();
					user.setUsername(username);
					//登录成功，保存用户信息到SharedPreferences
					ClientStoreUtil.setUser(getApplicationContext(),user);
				}
			}
		});


	}

}
