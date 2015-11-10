package com.txf.myguadeproject.activity;

import android.app.Activity;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.VitamioInstaller.VitamioNotCompatibleException;
import io.vov.vitamio.VitamioInstaller.VitamioNotFoundException;
import io.vov.vitamio.widget.MediaController;
import java.io.IOException;

import com.txf.myguadeproject.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;

public class PlayerActivity extends Activity {
	MediaPlayer mPlayer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	setContentView(R.layout.play_page);
	MediaController controller = new MediaController(this);//创建控制对象
	this.addContentView(controller, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	String path = "mms://ting.mop.com/mopradio";//猫扑电台地址,这里可以添加自己的喜欢的电台地址,mms协议的
	try {
	mPlayer = new MediaPlayer(this);//播放流媒体的对象
	mPlayer.setDataSource(path);//设置流媒体的数据源
	mPlayer.prepare();
	} catch (VitamioNotCompatibleException e) {
	e.printStackTrace();
	} catch (VitamioNotFoundException e) {
	e.printStackTrace();
	} catch (IllegalArgumentException e) {
	e.printStackTrace();
	} catch (IllegalStateException e) {
	e.printStackTrace();
	} catch (IOException e) {
	e.printStackTrace();
	}
	super.onCreate(savedInstanceState);
	}
	public void doStart(View view){
	mPlayer.start();//开始播放
	}
	public void doStop(View view){
	mPlayer.stop();//停止播放
	}

}
