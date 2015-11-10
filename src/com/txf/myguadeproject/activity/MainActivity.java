package com.txf.myguadeproject.activity;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.nineoldandroids.view.ViewHelper;
import com.txf.myguadeproject.R;
import com.txf.myguadeproject.fragment.MusicPlayerFragment;
import com.txf.myguadeproject.fragment.RadioFragment;
import com.txf.myguadeproject.fragment.RememberFragment;
import com.txf.myguadeproject.view.DragLayout;
import com.txf.myguadeproject.view.DragLayout.DragListener;

public class MainActivity extends FragmentActivity implements OnClickListener  {
	private DragLayout dl;
	private ImageView iv_icon;
	private Fragment[] fragments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fragments = new Fragment[3];
		initDragLayout();
		initView();
		
		//默认显示问吧碎片
		showFragment(1);
	}

	// 初始化界面控件
	private void initView() {
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		iv_icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dl.open();
			}
		});
		findViewById(R.id.tab_ask).setOnClickListener(this);
		findViewById(R.id.tab_found).setOnClickListener(this);
		findViewById(R.id.tab_picture).setOnClickListener(this);
	}

	// 初始化拖动框架
	private void initDragLayout() {
		dl = (DragLayout) findViewById(R.id.dl);
		dl.setDragListener(new DragListener() {
			@Override
			public void onOpen() {
				// lv.smoothScrollToPosition(new Random().nextInt(30));
			}

			@Override
			public void onClose() {
				shake();
			}

			@Override
			public void onDrag(float percent) {
				ViewHelper.setAlpha(iv_icon, 1 - percent);
			}
		});
	}

	// 主页头像图标抖动
	private void shake() {
		iv_icon.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tab_found:
			showFragment(0);
			break;
		case R.id.tab_ask:
			showFragment(1);
			break;
		case R.id.tab_picture:
			showFragment(2);
			break;
		}
	}

	// 当前显示的下标
	private int currentIndex = -1;

	// 显示碎片内容
	private void showFragment(int index) {
		if (currentIndex != index) {
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			if (currentIndex >= 0 && fragments[currentIndex] != null) {
				ft.hide(fragments[currentIndex]);
			}
			if (fragments[index] != null) {
				ft.show(fragments[index]);
			} else {
				ft.add(R.id.content, createFragment(index));
			}
			ft.commit();
			currentIndex = index;
		}
	}

	//根据下标创建碎片
	private Fragment createFragment(int index) {
		switch (index) {
		case 0:
			fragments[0] = new RememberFragment();
			break;
		case 1:
			fragments[1] = new MusicPlayerFragment();
			break;
		case 2:
			fragments[2] = new RadioFragment();
			break;
		}
		return fragments[index];
	}


}
