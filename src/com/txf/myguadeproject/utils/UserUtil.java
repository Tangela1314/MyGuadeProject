package com.txf.myguadeproject.utils;

import java.io.File;
import java.util.HashMap;
import java.util.TreeMap;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.txf.myguadeproject.entity.User;

public class UserUtil {

	private static UserUtil instance;

	private UserUtil() {
	}

	public static UserUtil getInstance() {
		if (null == instance) {
			instance = new UserUtil();
		}
		return instance;
	}

	public static User loginUser;

	/**
	 * 用户登录的方法
	 * 
	 * @param userName
	 * @param psw
	 * @return
	 */
	public int login(String userName, String psw) {
		TreeMap<String, String> map = new TreeMap<String, String>();
		map.put("name", userName);
		map.put("psw", psw);
		String jsonString = HttpUtils.postMsg(Common.URL_LOGIN, map);
		
		Log.e("m_tag", "登录结果:" + jsonString);
		if (jsonString.startsWith("{result:")) {
			return 0;
		} else {
			loginUser = JSON.parseObject(jsonString, User.class);
//			int id = loginUser.getQuestionId();
//			String question = getQuestion(id);
//			loginUser.setQuestion(question);
//			Log.e("m_tag", "登录结果�? + loginUser.getId());
//			String url = Common.URL_UPDATE_USER_INFO + "?userId="+loginUser.getId();
//			String json = HttpUtils.getMsg(url);
//			if (json.startsWith("{result:}")) {
//				return 0;
//			}else {
//				saveUser = JSON.parseObject(json, UserInfo.class);
//				Log.e("m_tag", "登录结果�? + saveUser.getUserId());
//			}
			return 1;
		}
	}

	/**
	 * 注册用户
	 * 
	 * @param userName
	 * @param psw
	 * @param iconPath
	 * @return
	 */
	public int regist(String userName, String psw,String sex,String iconPath) {
		TreeMap<String, String> params = new TreeMap<String, String>();
		params.put("name", userName);
		params.put("psw", psw);
		params.put("sex", sex);
		HashMap<String, File> files = null;
		if (null != iconPath) {
			File file = new File(iconPath);
			files = new HashMap<String, File>();
			files.put(userName, file);
		}
		String jsonStr = HttpUtils.postMsg(Common.URL_REGIST, params,files);
		if (jsonStr.startsWith("{result:}")) {
			String s = jsonStr.substring(8, jsonStr.length() - 1);
			int result = Integer.parseInt(s);
			return result;
		}
		return 1;
	}
	
	
	/**
	 * 注销用户
	 */
	public void logout() {
		loginUser = null;
		instance = null;
	}
}
