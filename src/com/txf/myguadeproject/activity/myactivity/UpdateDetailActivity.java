package com.txf.myguadeproject.activity.myactivity;


import com.txf.myguadeproject.R;
import com.txf.myguadeproject.app.MyGuadeProjectApp;
import com.txf.myguadeproject.utils.UserUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class UpdateDetailActivity extends Activity implements OnClickListener {
	private EditText nick, realname, sign, tel, qq, email, level;
	private RadioGroup radioSex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_update_detailinfo);
		init();
	}

	public void init() {
		nick = (EditText) findViewById(R.id.et_name);
		realname = (EditText) findViewById(R.id.et_realname);
		tel = (EditText) findViewById(R.id.et_tel);
		qq = (EditText) findViewById(R.id.et_qq);
		email = (EditText) findViewById(R.id.et_email);
		level = (EditText) findViewById(R.id.et_level);
		radioSex = (RadioGroup) findViewById(R.id.rg_sex);
		radioSex.setOnCheckedChangeListener(onCheckedChangeListener);
		findViewById(R.id.btn_confirm).setOnClickListener(this);
		findViewById(R.id.btn_cancle).setOnClickListener(this);
		if (MyGuadeProjectApp.checkIsLogin()) {
			nick.setText(UserUtil.loginUser.getNick());
			radioSex.check(radioSex.getCheckedRadioButtonId());
		} else {
			nick.setText(null);
			realname.setText(null);
			radioSex.check(R.id.rbtn_sex_nan);
			sign.setText(null);
			tel.setText(null);
			qq.setText(null);
			email.setText(null);
			level.setText(null);
		}
	}

	RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int CheckedId) {
			// TODO Auto-generated method stub
			if (CheckedId == R.id.rbtn_sex_nan) {

			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_confirm:
			Intent intent = new Intent();
			intent.putExtra("result_nick", nick.getText().toString());
			intent.putExtra("result_name", realname.getText().toString());
			intent.putExtra("result_sex", ((RadioButton) findViewById(radioSex
					.getCheckedRadioButtonId())).getText().toString());
			intent.putExtra("result_tel", tel.getText().toString());
			intent.putExtra("result_qq", qq.getText().toString());
			intent.putExtra("result_email", email.getText().toString());
			intent.putExtra("result_level", level.getText().toString());
			setResult(RESULT_OK, intent);
			finish();
			break;

		case R.id.btn_cancle:

			break;
		}
	}

}
