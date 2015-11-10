package com.txf.myguadeproject.activity;


import com.txf.myguadeproject.R;
import com.txf.myguadeproject.task.UserTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
	public static final String SETTING_INFO = "SETTING_Infos";
	public static final String NAME = "NAME";
	public static final String PASSWORD = "PASSWORD";

	private EditText etName, etPsw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		etName = (EditText) findViewById(R.id.et_name);
		etPsw = (EditText) findViewById(R.id.et_psw);
		findViewById(R.id.btn_login).setOnClickListener(this);
		findViewById(R.id.btn_regist).setOnClickListener(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		SharedPreferences settings = getSharedPreferences(SETTING_INFO, 0);
		settings.edit().putString(NAME, etName.getText().toString())
				.putString(PASSWORD, etPsw.getText().toString()).commit();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		SharedPreferences settings = getSharedPreferences(SETTING_INFO, 0);
		String name = settings.getString(NAME, "");
		String password = settings.getString(PASSWORD, "");
		etName.setText(name);
		etPsw.setText(password);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_login:
			// 将登录的用户名、密码发送到服务端
			String userName = etName.getText().toString();
			String psw = etPsw.getText().toString();
			new UserTask(UserTask.OPT_LOGIN, mHandler).execute(userName, psw);
			break;
		case R.id.btn_regist:
			Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
			startActivity(intent);
			break;
		}
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				// 登录成功，跳到主页
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
				break;
			case 0:
				Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG)
						.show();
				break;
			}
		}

	};

}
