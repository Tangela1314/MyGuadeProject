package com.txf.myguadeproject.app;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.txf.myguadeproject.utils.RequestManager;
import com.txf.myguadeproject.utils.UserUtil;


import android.app.Application;
import android.widget.Toast;

public class MyGuadeProjectApp extends Application {

	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		RequestManager.init(this);
	}

	public void executeRequest(Request<?> request) {
		RequestManager.addRequest(request, this);
	}

	public Response.ErrorListener errorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(MyGuadeProjectApp.this, error.getMessage(),
						Toast.LENGTH_LONG).show();
			}
		};
	}

	public void stop() {
		RequestManager.cancelAll(this);
	}

	/**
	 * 判断用户是否登录，登录则可以进行操作，没有登录则需要登录
	 */
	public static Boolean checkIsLogin(){
		if (null !=UserUtil.loginUser) {
			int id = UserUtil.loginUser.getId();
			return true;
		}else {				
			return false;
		}
	}

}
