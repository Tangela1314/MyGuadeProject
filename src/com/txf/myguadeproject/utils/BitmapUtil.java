package com.txf.myguadeproject.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import com.txf.myguadeproject.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.widget.ImageView;

public class BitmapUtil {

	// 图像缓存
	private static Map<String, SoftReference<Bitmap>> cache = new HashMap<String, SoftReference<Bitmap>>();

	/**
	 * 获取圆形显示的Bitmap
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Bitmap out = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(out);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		canvas.drawCircle(width / 2, height / 2, width / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		Rect rect = new Rect(0, 0, width, height);
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return out;
	}

	/**
	 * 根据用户信息获取头像，并设置头像到ImageView中(该方法直接获取的图像来源本地或者应用的默认头像)
	 * 
	 * @param context
	 * @param name用户名
	 * @param isHasIconInRemote服务器上是否有可下载的头像
	 * @param iv
	 *            显示头像的ImageView
	 * @return 0:表示需要下载 1:表示无需下载并已设置成功
	 */
	public static int getBitmap(Context context, String name,
			boolean isHasIconInRemote, ImageView iv) {
		if (!cache.containsKey(name)) {
			// 缓存中没有图像
			// 如果用户有头像可供下载(说明可能已经下载，可能还未下载)
			if (isHasIconInRemote) {
				// 先检测本地是否有缓存的头像，有则加载
				InputStream in = getLocalIcon(name);
				if (in != null) {
					iv.setImageBitmap(saveIntoCache(in, name));
				} else {
					// 本地没有缓存，需要从网络上下载
					return 0;
				}
			} else {
				// 用户没有头像可供下载，缓存默认头像
				iv.setImageBitmap(saveIntoCache(context.getResources()
						.openRawResource(R.drawable.user), "default"));
			}
		} else {
			// 缓存中曾经有图像记录
			Bitmap b = null;
			// 服务器上没有头像，所以只能用默认头像
			if (!isHasIconInRemote) {
				b = cache.get("default").get(); // 获取默认头像
			} else {
				b = cache.get(name).get();
			}
			if (b != null) {
				// 得到缓存的图像对象
				iv.setImageBitmap(b);
			} else {
				// 图像被回收了，移除记录然后重新获取
				cache.remove(name);
				getBitmap(context, name, isHasIconInRemote, iv);
			}
		}
		return 1;
	}

	// 将图像转换并保存到缓存中
	private static Bitmap saveIntoCache(InputStream in, String name) {
		Bitmap b = BitmapFactory.decodeStream(in);
		Bitmap bitmap;
		if ("default".equals(name)) { //默认的不处理
			bitmap = b;
		} else {
			bitmap = BitmapUtil.getRoundBitmap(b);
		}
		cache.put(name, new SoftReference<Bitmap>(bitmap));
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}

	// 获取本地缓存的头像
	private static InputStream getLocalIcon(String name) {
		File file = new File(Common.PHOTO_CACHE_PATH + name);
		if (file.exists()) {
			InputStream in = null;
			try {
				in = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return in;
		}
		return null;
	}


}
