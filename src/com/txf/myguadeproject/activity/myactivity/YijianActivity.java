package com.txf.myguadeproject.activity.myactivity;


import com.txf.myguadeproject.R;
import com.txf.myguadeproject.activity.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class YijianActivity extends Activity implements OnClickListener{
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_yijian);
		findViewById(R.id.btn_cancle).setOnClickListener(this);
		findViewById(R.id.btn_confirm).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_confirm:
			Toast.makeText(YijianActivity.this, "十分感谢您提供宝贵的意见", 3000);
			Intent intent = new Intent(YijianActivity.this,MainActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.btn_cancle:
			Intent i = new Intent(YijianActivity.this,MainActivity.class);
			startActivity(i);
			finish();
			break;

		
		}
	}

}
