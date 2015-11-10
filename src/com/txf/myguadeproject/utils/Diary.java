package com.txf.myguadeproject.utils;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Diary {
	// 这里的 AUTHORITY 要求是唯一，而且和Manifest当中provider标签的AUTHORITY内容一致
	public static final String AUTHORITY = "com.txf.myguadeproject.activity.diarycontentprovider";

	private Diary() {
	}

	/**
	 * Notes table
	 *内部静态类
	 */
	public static final class DiaryColumns implements BaseColumns {
		// This class cannot be instantiated
		private DiaryColumns() {
		}

		//定义主Uri,访问入口
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/diaries");
		//自定义一个全表操作类型
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.diary";
		//定义一个单条数据操作类型
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.diary";
		//默认排序方式，按时间逆序
		public static final String DEFAULT_SORT_ORDER = "created DESC";
		//定义标题字段
		public static final String TITLE = "title";
		//定义内容字段
		public static final String BODY = "body";
		//定义时间字段
		public static final String CREATED = "created";
	}
}
