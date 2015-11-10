package com.txf.myguadeproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.txf.myguadeproject.R;
import com.txf.myguadeproject.activity.LoginActivity;
import com.txf.myguadeproject.activity.myactivity.MydetialActivity;
import com.txf.myguadeproject.activity.myactivity.SettingActivitym;
import com.txf.myguadeproject.app.MyGuadeProjectApp;
import com.txf.myguadeproject.utils.RequestManager;
import com.txf.myguadeproject.utils.UserUtil;

/**
 * 我
 * 
 * @author Administrator
 * 
 */
public class MeFragment extends Fragment implements OnClickListener {

	private NetworkImageView iv_bottom;
	private TextView name, sex;
	private TextView tvdetailInfo, tvSetting;// 详细信息，设置

	private boolean isLogin = MyGuadeProjectApp.checkIsLogin();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View layout = inflater.inflate(R.layout.fragment_me, null);
		iv_bottom = (NetworkImageView) layout.findViewById(R.id.iv_bottom);
		name = (TextView) layout.findViewById(R.id.et_name);
		sex = (TextView) layout.findViewById(R.id.rg_sex);
		tvdetailInfo = (TextView) layout.findViewById(R.id.tv_detail);
		tvSetting = (TextView) layout.findViewById(R.id.tv_setting);

		tvdetailInfo.setOnClickListener(this);
		tvSetting.setOnClickListener(this);

		if (!isLogin) {
			iv_bottom.setDefaultImageResId(0);
			name.setText("匿名张三");
			sex.setText("未知");
		} else {
			iv_bottom.setDefaultImageResId(UserUtil.loginUser.getId());
			name.setText(UserUtil.loginUser.getNick());
			sex.setText(UserUtil.loginUser.getSex());
			Log.e("m_tag", "name  " + UserUtil.loginUser.getNick());
		}
		return layout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loadUserIcon();
	}

	private void loadUserIcon() {
		ImageLoader imageLoader = RequestManager.getImageLoader();
		if (isLogin) {
			iv_bottom.setImageUrl(UserUtil.loginUser.getPhoto(), imageLoader);
		} else {
			iv_bottom
					.setImageUrl(
							"http://192.168.0.146:8080/AskMeServer/photo/1339049989.jpg",
							imageLoader);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_detail:
			if (isLogin) {
				// 跳转到详细信息页面
				Intent intent = new Intent(getActivity(),
						MydetialActivity.class);
				startActivity(intent);
			} else {
				// 没有登录，需登录
				Intent a = new Intent(getActivity(), LoginActivity.class);
				startActivity(a);
			}
			break;
		case R.id.tv_setting:
			if (isLogin) {
				// 跳转到设置页面
				Intent s = new Intent(getActivity(), SettingActivitym.class);
				startActivity(s);

			} else {

				// 没有登录，需登录
				Intent a = new Intent(getActivity(), LoginActivity.class);
				startActivity(a);

			}
			break;
		}
	}

}
