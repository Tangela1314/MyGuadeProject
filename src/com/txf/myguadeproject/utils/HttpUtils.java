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
 * �������ӹ���
 * 
 * @author Whunf
 * 
 */
public class HttpUtils {

	/**
	 * �ύ��ͨ�ı�
	 * 
	 * @param url�ύ�ķ�������ַ
	 * @param params�ύ�Ĳ���
	 * @return
	 */
	public static String postMsg(String url, TreeMap<String, String> params) {
		// ʹ��NameValuePair������Ҫ���ݵ�Post����
		List<NameValuePair> pars = new ArrayList<NameValuePair>();
		// ���Ҫ���ݵĲ���
		for (Map.Entry<String, String> entry : params.entrySet()) {
			pars.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		try {
			// �����ַ���
			HttpEntity httpentity = new UrlEncodedFormEntity(pars, "UTF-8");
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			// ����httpRequest(Ҫ�ύ������)
			httppost.setEntity(httpentity);
			// ִ�в���ȡ���
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
	 * ����Ϣ��post��ʽ�ϴ��ļ�������ˣ��÷���ֻ����ע�ᡢ�����û���Ϣ��ʹ�ã�
	 * 
	 * @param url
	 * @param params�ύ�Ĳ���
	 * @param files�ϴ����ļ�
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String postMsg(String url, TreeMap<String, String> params,
			Map<String, File> files) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		try {
			// �ύ�����ݶ���
			MultipartEntity mulEntity = new MultipartEntity();
			// �ϴ����ļ�
			if (null != files) {
				// �����ļ���������ļ�
				for (Map.Entry<String, File> entry : files.entrySet()) {
					FileBody fb = new FileBody(entry.getValue());
					mulEntity.addPart(entry.getKey(), fb);
				}
			}
			// ����ı������б�
			if (null != params) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					mulEntity.addPart(
							entry.getKey(),
							new StringBody(entry.getValue(), Charset
									.forName("UTF-8")));
				}
			}
			httppost.setEntity(mulEntity);

			// ִ�в���ȡ���
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
	 * ��get��ʽ�ӷ�������ȡ����
	 * 
	 * @param url
	 * @return
	 */
	public static String getMsg(String url) {
		// ������get��ʽ���������
		HttpGet get = new HttpGet(url);
		// ִ�����ӵĿͻ��˶���
		HttpClient client = new DefaultHttpClient();
		try {
			// ִ�����ӻ�ȡ����˵ķ��ؽ��
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
	 * �ӷ���˻�ȡ�ļ������ֵ�����
	 * 
	 * @param url���صĵ�ַ
	 * @param savePath
	 *            �����ڱ��صĵ�ַ:/sdcard/timeblog/image/abc.jpg
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
