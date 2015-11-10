package com.txf.myguadeproject.activity;

import com.txf.myguadeproject.R;

import io.vov.vitamio.VitamioInstaller;
import io.vov.vitamio.VitamioInstaller.VitamioNotCompatibleException;
import io.vov.vitamio.VitamioInstaller.VitamioNotFoundException;
import android.app.Activity;
import android.content.Intent; 
import android.os.Bundle; 
import android.util.Log; 
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;


public class NetRadioDemoActivity extends Activity {
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_radio);
		intent = new Intent(this, PlayerActivity.class);
		TextView tvCheck = new TextView(this);
		tvCheck.setText("使用前请检查是否安装了Vitamio插件:");
		this.addContentView(tvCheck, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		Button btnCheck = new Button(this);
		btnCheck.setText("检查");
		btnCheck.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View arg0) {

		try {
		String isInstallerString = VitamioInstaller.checkVitamioInstallation(NetRadioDemoActivity.this);//检查插件是否安装成功,这里是一个费时操作,应该启新线程处理,作为一个demo我就不做了
		Log.i("tag",isInstallerString); //插件安装成功后,Log中显示插件名称
		if(isInstallerString!=null){
		Toast.makeText(NetRadioDemoActivity.this, "已安装正确版本Vitamio!", Toast.LENGTH_LONG).show();        
		startActivity(intent);//开启收听界面
		}else{
		Toast.makeText(NetRadioDemoActivity.this, "没有匹配的Vitamio!", Toast.LENGTH_LONG).show();
		finish();//没有插件安装失败,则结束程序
		}
		} catch (VitamioNotCompatibleException e) {
		e.printStackTrace(); 
		} catch (VitamioNotFoundException e) {
		e.printStackTrace(); 
		} 
		}
		}); 
		this.addContentView(btnCheck, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}

	

}
