package com.txf.myguadeproject.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 * 网络连接工具
 * 
 * @author Whunf
 * 
 */
public class HttpUtils {

	/**
	 * 提交普通文本
	 * 
	 * @param url提交的服务器地址
	 * @param params提交的参数
	 * @return
	 */
	public static String postMsg(String url, TreeMap<String, String> params) {
		// 使用NameValuePair来保存要传递的Post参数
		List<NameValuePair> pars = new ArrayList<NameValuePair>();
		// 添加要传递的参数
		for (Map.Entry<String, String> entry : params.entrySet()) {
			pars.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		try {
			// 设置字符集
			HttpEntity httpentity = new UrlEncodedFormEntity(pars, "UTF-8");
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			// 请求httpRequest(要提交的内容)
			httppost.setEntity(httpentity);
			// 执行并获取结果
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity resEntity = response.getEntity();
			if (response.getStatusLine().getStatusCode() == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						resEntity.getContent()));
				StringBuilder sb = new StringBuilder();
				String s;
				while ((s = br.readLine()) != null) {
					sb.append(s);
				}
				httpclient.getConnectionManager().shutdown();
				return sb.toString();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将信息以post方式上传文件到服务端（该方法只能在注册、更新用户信息中使用）
	 * 
	 * @param url
	 * @param params提交的参数
	 * @param files上传的文件
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String postMsg(String url, TreeMap<String, String> params,
			Map<String, File> files) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		try {
			// 提交的内容对象
			MultipartEntity mulEntity = new MultipartEntity();
			// 上传的文件
			if (null != files) {
				// 遍历文件集合添加文件
				for (Map.Entry<String, File> entry : files.entrySet()) {
					FileBody fb = new FileBody(entry.getValue());
					mulEntity.addPart(entry.getKey(), fb);
				}
			}
			// 添加文本参数列表
			if (null != params) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					mulEntity.addPart(
							entry.getKey(),
							new StringBody(entry.getValue(), Charset
									.forName("UTF-8")));
				}
			}
			httppost.setEntity(mulEntity);

			// 执行并获取结果
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity resEntity = response.getEntity();
			if (response.getStatusLine().getStatusCode() == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						resEntity.getContent()));
				StringBuilder sb = new StringBuilder();
				String s;
				while ((s = br.readLine()) != null) {
					sb.append(s);
				}
				httpclient.getConnectionManager().shutdown();
				return sb.toString();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 以get方式从服务器获取数据
	 * 
	 * @param url
	 * @return
	 */
	public static String getMsg(String url) {
		// 创建了get方式请求的连接
		HttpGet get = new HttpGet(url);
		// 执行连接的客户端对象
		HttpClient client = new DefaultHttpClient();
		try {
			// 执行连接获取服务端的返回结果
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream in = response.getEntity().getContent();
				BufferedReader br = new BufferedReader(
						new InputStreamReader(in));
				StringBuilder sb = new StringBuilder();
				String s;
				while ((s = br.readLine()) != null) {
					sb.append(s);
				}
				client.getConnectionManager().shutdown();
				return sb.toString();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从服务端获取文件并保持到本地
	 * 
	 * @param url下载的地址
	 * @param savePath
	 *            保存在本地的地址:/sdcard/timeblog/image/abc.jpg
	 * @return
	 */
	public static boolean download(String url, String savePath) {
		HttpGet get = new HttpGet(url);
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse respon = client.execute(get);
			if (respon.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream in = respon.getEntity().getContent();
				FileOutputStream out = new FileOutputStream(savePath);
				byte[] buffer = new byte[1024];
				int b;
				while ((b = in.read(buffer)) != -1) {
					out.write(buffer, 0, b);
				}
				out.flush();
				out.close();
				in.close();
				client.getConnectionManager().shutdown();
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
