package com.txf.myguadeproject.activity;

import java.io.FileNotFoundException;

import com.txf.myguadeproject.R;
import com.txf.myguadeproject.task.UserTask;
import com.txf.myguadeproject.utils.BitmapUtil;
import com.txf.myguadeproject.utils.Common;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class RegistActivity extends Activity implements OnClickListener {
	// 拍照之后的地址
	private Uri picUri = Uri.parse("file:///mnt/sdcard/temp00.jpg");
	// 裁减之后的地址
	private Uri imageUri = Uri.parse("file:///mnt/sdcard/temp.jpg");

	private String uploadIconPath;

	private static final int CHOOSE_BIG_PICTURE = 1;
	private static final int TACK_PICTURE = 2;
	private ImageView mIcon;
	private EditText etName, etPsw, etRePsw;
	private RadioGroup rgSex;
	private RadioButton boy, girl, rb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		mIcon = (ImageView) findViewById(R.id.m_icon);
		etName = (EditText) findViewById(R.id.et_name);
		etPsw = (EditText) findViewById(R.id.et_psw);
		etRePsw = (EditText) findViewById(R.id.et_re_psw);
		rgSex = (RadioGroup) findViewById(R.id.rg_sex);
		boy = (RadioButton) findViewById(R.id.rbtn_sex_nan);
		girl = (RadioButton) findViewById(R.id.rbtn_sex_nv);
		mIcon.setOnClickListener(this);
		findViewById(R.id.btn_regist).setOnClickListener(this);
		findViewById(R.id.btn_cancle).setOnClickListener(this);
		rgSex.setOnCheckedChangeListener(groupCheckChange);

		Bitmap defaultIcon = BitmapFactory.decodeStream(getResources()
				.openRawResource(R.drawable.user));
		mIcon.setImageBitmap(BitmapUtil.getRoundBitmap(defaultIcon));	
	}

	// 监听性别是否被选中
	private RadioGroup.OnCheckedChangeListener groupCheckChange = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.m_icon:
			showGetPictureDialog();
			break;
		case R.id.btn_regist:
			String userName = etName.getText().toString().trim();
			System.out.println(userName);
			if (null != userName && !"".equals(userName)) {
				String psw = etPsw.getText().toString();
				if (null != psw && !"".equals(psw)) {
					if (psw.equals(etRePsw.getText().toString())) {
						String sex = ((RadioButton) findViewById(rgSex
								.getCheckedRadioButtonId())).getText()
								.toString();
						System.out.println(sex);
						UserTask task = new UserTask(UserTask.OPT_REGIST,
								mHandler);
						if (null != uploadIconPath) {
							task.execute(userName, psw, uploadIconPath);
						} else {
							task.execute(userName, psw, sex);
						}

					} else {
						Common.showMsg(RegistActivity.this, "两次输入密码不匹配");
					}
				} else {
					Common.showMsg(RegistActivity.this, "未输入密码");
				}
			} else {
				Common.showMsg(RegistActivity.this, "未输入用户名");
			}
			break;
		case R.id.btn_cancle:
			finish();
			break;
		case R.id.btn_get_from_local:
			// 从图库获取图像
			dialog.dismiss();
			getFromPictureHome();
			break;
		case R.id.btn_get_from_camera:
			// 从相机获取图像
			dialog.dismiss();
			startCamera();
			break;
		}
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Common.showMsg(RegistActivity.this, "注册失败");
				break;
			case 1:
				Common.showMsg(RegistActivity.this, "注册成功");
				finish();
				break;
			}
		}

	};

	private Dialog dialog;

	// 显示获取图像弹出框
	public void showGetPictureDialog() {
		if (null == dialog) {
			View layout = getLayoutInflater().inflate(
					R.layout.dialog_get_picture_layout, null);
			layout.findViewById(R.id.btn_get_from_local).setOnClickListener(
					this);
			layout.findViewById(R.id.btn_get_from_camera).setOnClickListener(
					this);
			dialog = new Dialog(this, R.style.MyDialog);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(layout);
			Window window = dialog.getWindow();
			window.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
			WindowManager.LayoutParams lp = window.getAttributes();
			lp.width = window.getWindowManager().getDefaultDisplay().getWidth();
			window.setAttributes(lp);
		}
		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case TACK_PICTURE:
			// 将拍照之后的图像进行裁剪，这里启动裁剪的Activity
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(picUri, "image/*");
			intent.putExtra("crop", "true");
			// // aspectX aspectY 是宽高的比例
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			intent.putExtra("outputFormat", "JPEG");
			intent.putExtra("return-data", false);
			startActivityForResult(intent, CHOOSE_BIG_PICTURE);
			break;
		case CHOOSE_BIG_PICTURE:
			if (imageUri != null) {
				Bitmap b = BitmapUtil
						.getRoundBitmap(decodeUriAsBitmap(imageUri));
				mIcon.setImageBitmap(b);
				uploadIconPath = imageUri.getPath();
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void startCamera() {
		// 调用系统的拍照功能
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
		intent.putExtra("noFaceDetection", false);
		startActivityForResult(intent, TACK_PICTURE);
	}

	// 从图库获取并裁减
	private void getFromPictureHome() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra("outputFormat", "JPEG");
		startActivityForResult(intent, CHOOSE_BIG_PICTURE);
	}

	private Bitmap decodeUriAsBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}
}
