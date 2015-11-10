package com.txf.myguadeproject.activity.myactivity;


import com.txf.myguadeproject.R;
import com.txf.myguadeproject.activity.MainActivity;
import com.txf.myguadeproject.utils.UserUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivitym extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_setting);
		findViewById(R.id.tv_back).setOnClickListener(this);
		findViewById(R.id.ly_m_user).setOnClickListener(this);
		findViewById(R.id.ly_p).setOnClickListener(this);
		findViewById(R.id.ly_about).setOnClickListener(this);
		findViewById(R.id.ly_yijian).setOnClickListener(this);
		findViewById(R.id.ly_exit).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_back:
			Intent i = new Intent(SettingActivitym.this,MainActivity.class);
			startActivity(i);
			break;
		case R.id.ly_m_user:
			
			break;
		case R.id.ly_p:
			
			break;

		case R.id.ly_about:
			Intent in = new Intent(SettingActivitym.this,AboutmeActivity.class);
			startActivity(in);
			break;
		case R.id.ly_yijian:
			Intent intent = new Intent(SettingActivitym.this,YijianActivity.class);
			startActivity(intent);
			break;
		case R.id.ly_exit:
			UserUtil.getInstance().logout();
			Intent e = new Intent(SettingActivitym.this,MainActivity.class);
			startActivity(e);
			break;
		}
	}

	

}
