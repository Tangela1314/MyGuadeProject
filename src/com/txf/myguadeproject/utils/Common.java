package com.txf.myguadeproject.utils;

import android.content.Context;
import android.widget.Toast;

public class Common {
	public static final String BASE_URL = "http://192.168.0.173:8080/MyPlayer/";
	// 登录地址
	public static final String URL_LOGIN = BASE_URL + "login";
	// 注册地址
	public static final String URL_REGIST = BASE_URL + "regist";
	// 获取用户详情
	public static final String URL_USER_INFO = BASE_URL + "user_info";
	// 获取问题
	public static final String URL_QUESTION = BASE_URL + "question";
	// 更新用户详情
	public static final String URL_UPDATE_USER_INFO = BASE_URL + "update_info";

	// 本地已下载的头像缓存
	public static final String PHOTO_CACHE_PATH = "/sdcard/blog/photo/";

	public static void showMsg(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
}
