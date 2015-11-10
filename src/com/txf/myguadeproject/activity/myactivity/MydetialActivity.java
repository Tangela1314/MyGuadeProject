package com.txf.myguadeproject.activity.myactivity;


import com.txf.myguadeproject.R;
import com.txf.myguadeproject.app.MyGuadeProjectApp;
import com.txf.myguadeproject.utils.UserUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MydetialActivity extends Activity implements OnClickListener {
	private static final int REQUEST_CODE = 1;
	private TextView nick, name, sex, tel, qq, email, level, sign;
	public boolean isLogin = MyGuadeProjectApp.checkIsLogin();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mydetialinfo);
		init();
	}

	public void init() {
		nick = (TextView) findViewById(R.id.tv_name);
		name = (TextView) findViewById(R.id.tv_realname);
		sex = (TextView) findViewById(R.id.tv_sex);
		tel = (TextView) findViewById(R.id.tv_tel);
		qq = (TextView) findViewById(R.id.tv_qq);
		email = (TextView) findViewById(R.id.tv_email);
		level = (TextView) findViewById(R.id.tv_level);
		findViewById(R.id.btn_setdetail).setOnClickListener(this);
		if (isLogin) {
			nick.setText(UserUtil.loginUser.getNick());
			sex.setText(UserUtil.loginUser.getSex());

		} else {
			nick.setText(null);
			name.setText(null);
			sex.setText(null);
			tel.setText(null);
			qq.setText(null);
			email.setText(null);
			level.setText(null);
		}
	}
	
	public void getData(){
		nick.setText(UserUtil.loginUser.getNick());
		sex.setText(UserUtil.loginUser.getSex());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btn_setdetail) {
			Intent intent = new Intent(MydetialActivity.this,
					UpdateDetailActivity.class);
			intent.putExtra("nick", nick.getText());
			intent.putExtra("name", name.getText());
			intent.putExtra("sex", sex.getText());
			intent.putExtra("tel", tel.getText());
			intent.putExtra("qq", qq.getText());
			intent.putExtra("email", email.getText());
			intent.putExtra("level", level.getText());
			startActivityForResult(intent, REQUEST_CODE);
		}
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				Toast.makeText(MydetialActivity.this, "加载数据成功",
						Toast.LENGTH_LONG).show();
				finish();
				break;
			case 0:
				Toast.makeText(MydetialActivity.this, "加载数据失败",
						Toast.LENGTH_LONG).show();
				break;
			}
		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE) {
			if (null != data) {
				String n = data.getStringExtra("result_nick");
				String re = data.getStringExtra("result_name");
				// String se = data.getStringExtra("result_sex");
				String s = data.getStringExtra("result_sign");
				String t = data.getStringExtra("result_tel");
				String q = data.getStringExtra("result_qq");
				String e = data.getStringExtra("result_email");
				String l = data.getStringExtra("result_level");
				nick.setText(n);
				name.setText(re);
				// sex.setText(se);
				tel.setText(t);
				qq.setText(q);
				email.setText(e);
				level.setText(l);
			}
		}
	}

}
