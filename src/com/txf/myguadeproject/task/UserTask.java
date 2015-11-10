package com.txf.myguadeproject.task;

import com.txf.myguadeproject.utils.UserUtil;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class UserTask extends AsyncTask<Object, Void, Integer> {
	// 登录
	public static final int OPT_LOGIN = 1;
	// 注册
	public static final int OPT_REGIST = 2;
	// 获取用户信息
	public static final int OPT_GETINFO = 3;

	public static final int C_UPDATE_USER_INFO = 4;// 更新用户信息
	// 更改用户信息
	public static final int OPT_UPDATEUSERINFO = 5;

	private int opt;

	// 处理结果的返回
	private Handler handler;

	public UserTask(int opt, Handler handler) {
		this.opt = opt;
		this.handler = handler;
	}

	@Override
	protected Integer doInBackground(Object... params) {
		// TODO Auto-generated method stub
		switch (opt) {
		case OPT_LOGIN:
			// 处理登录
			if (params.length == 2) {
				String userName = (String) params[0];
				String psw = (String) params[1];
				return UserUtil.getInstance().login(userName, psw);
			}
			break;
		case OPT_GETINFO:
			// 处理获取用户信息
			break;

		case OPT_UPDATEUSERINFO:
			// 更改用户信息
			break;
		case OPT_REGIST:
			// 处理注册
			String userName = (String) params[0];
			String psw = (String) params[1];
			String sexr = (String) params[2];
			String iconPath = null;
			if (params.length == 4) {
				iconPath = (String) params[3];

			}
			return UserUtil.getInstance().regist(userName, psw, sexr, iconPath);
		}
		return null;

	}

	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		if (null != handler) {
			Message msg = null;
			if (result == 1) {
				msg = handler.obtainMessage(1);
			} else {
				msg = handler.obtainMessage(0);
			}
			msg.sendToTarget();
		}
	}

}
