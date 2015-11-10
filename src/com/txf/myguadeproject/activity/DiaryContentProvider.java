package com.txf.myguadeproject.activity;

import java.util.Calendar;

import com.txf.myguadeproject.utils.Diary;
import com.txf.myguadeproject.utils.Diary.DiaryColumns;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class DiaryContentProvider extends ContentProvider {

	private static final String DATABASE_NAME = "database";
	private static final int DATABASE_VERSION = 1;
	private static final String DIARY_TABLE_NAME = "diary";

	// 定义一个针对全表操作的操作码
	private static final int DIARIES = 1;
	// 定义一个针对单行操作的操作码
	private static final int DIARY_ID = 2;

	private static final UriMatcher sUriMatcher;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = "CREATE TABLE " + DIARY_TABLE_NAME + " ("
					+ DiaryColumns._ID + " INTEGER PRIMARY KEY,"
					+ DiaryColumns.TITLE + " TEXT," + DiaryColumns.BODY
					+ " TEXT," + DiaryColumns.CREATED + " TEXT" + ");";

			db.execSQL(sql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS diary");
			onCreate(db);
		}
	}

	private DatabaseHelper mOpenHelper;

	static {
		// 通过UriMatcher.NO_MATCH参数构造UriMatcher对象
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		// 注册URI，下面这里相当于注册了content://com.mct.diary.diarycontentprovider/diaries，对应的操作码为DIARIES
		sUriMatcher.addURI(Diary.AUTHORITY, "diaries", DIARIES);
		// 注册content://com.mct.diary.diarycontentprovider/diaries/#，对应的操作码为DIARY_ID
		sUriMatcher.addURI(Diary.AUTHORITY, "diaries/#", DIARY_ID);
	}

	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// 数据库查询对象
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (sUriMatcher.match(uri)) {
		case DIARIES:
			qb.setTables(DIARY_TABLE_NAME);
			break;

		case DIARY_ID:
			qb.setTables(DIARY_TABLE_NAME);
			// content://com.mct.diary.diarycontentprovider/diaries/1
			qb.appendWhere(DiaryColumns._ID + "="
					+ uri.getPathSegments().get(1));
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = Diary.DiaryColumns.DEFAULT_SORT_ORDER;
		} else {
			orderBy = sortOrder;
		}

		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, orderBy);
		return c;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case DIARIES:
			return DiaryColumns.CONTENT_TYPE;

		case DIARY_ID:
			return DiaryColumns.CONTENT_ITEM_TYPE;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		if (sUriMatcher.match(uri) != DIARIES) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		if (!values.containsKey(Diary.DiaryColumns.CREATED)) {
			values.put(Diary.DiaryColumns.CREATED, getFormateCreatedDate());
		}

		if (!values.containsKey(Diary.DiaryColumns.TITLE)) {
			Resources r = Resources.getSystem();
			values.put(Diary.DiaryColumns.TITLE,
					r.getString(android.R.string.untitled));
		}

		if (!values.containsKey(Diary.DiaryColumns.BODY)) {
			values.put(Diary.DiaryColumns.BODY, "");
		}

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = db.insert(DIARY_TABLE_NAME, DiaryColumns.BODY, values);
		if (rowId > 0) {
			//组装一个URI
			//假如rowID==5，那么diaryUri为：content://com.mct.diary.diarycontentprovider/diaries/5
			Uri diaryUri = ContentUris.withAppendedId(
					Diary.DiaryColumns.CONTENT_URI, rowId);
			return diaryUri;
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		if (sUriMatcher.match(uri) != DIARY_ID) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		//获取uri中第一个#的内容
		String rowId = uri.getPathSegments().get(1);
		return db
				.delete(DIARY_TABLE_NAME, DiaryColumns._ID + "=" + rowId, null);
	}

	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		if (sUriMatcher.match(uri) != DIARY_ID) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		String rowId = uri.getPathSegments().get(1);
		return db.update(DIARY_TABLE_NAME, values, DiaryColumns._ID + "="
				+ rowId, null);
	}

	public static String getFormateCreatedDate() {
		Calendar calendar = Calendar.getInstance();
		String created = calendar.get(Calendar.YEAR) + "年"
				+ calendar.get(Calendar.MONTH) + "月"
				+ calendar.get(Calendar.DAY_OF_MONTH) + "日"
				+ calendar.get(Calendar.HOUR_OF_DAY) + "时"
				+ calendar.get(Calendar.MINUTE) + "分";
		return created;
	}
}
