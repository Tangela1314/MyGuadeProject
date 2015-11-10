package com.txf.myguadeproject.activity;

import com.txf.myguadeproject.R;
import com.txf.myguadeproject.utils.Diary;
import com.txf.myguadeproject.utils.Diary.DiaryColumns;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class ActivityDiaryEditor extends Activity {
	public static final String EDIT_DIARY_ACTION = "com.txf.myguadeproject.activity.ActivityDiaryEditor.EDIT_DIARY";
	public static final String INSERT_DIARY_ACTION = "com.txf.myguadeproject.activity.ActivityDiaryEditor.action.INSERT_DIARY";

	private static final int STATE_EDIT = 0;
	private static final int STATE_INSERT = 1;

	/**
	 * 查询cursor时候，感兴趣的那些条例。
	 */
	private static final String[] PROJECTION = new String[] { DiaryColumns._ID,
			DiaryColumns.TITLE, DiaryColumns.BODY, };

	private int mState;

	private Uri mUri;

	private EditText mTitleText;
	private EditText mBodyText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Light);
		setContentView(R.layout.diary_edit);

		mTitleText = (EditText) findViewById(R.id.title);
		mBodyText = (EditText) findViewById(R.id.body);
		//获取跳转过来的Intent对象
		Intent intent = getIntent();
		//得到该对象中的动作（编辑日记或者插入数据）
		String action = intent.getAction();
		
		//获取操作uri
		mUri = intent.getData();

		if (EDIT_DIARY_ACTION.equals(action)) {// 编辑日记
			mState = STATE_EDIT;
			Cursor mCursor = managedQuery(mUri, PROJECTION, null, null, null);
			mCursor.moveToFirst();
			String title = mCursor.getString(mCursor.getColumnIndex(DiaryColumns.TITLE));
			mTitleText.setTextKeepState(title);
			String body = mCursor.getString(mCursor.getColumnIndex(DiaryColumns.BODY));
			mBodyText.setTextKeepState(body);
			setTitle("编辑日记");
		} else if (INSERT_DIARY_ACTION.equals(action)) {// 新建日记
			mState = STATE_INSERT;
			setTitle("新建日记");
		} else {
			finish();
			return;
		}

		findViewById(R.id.confirm).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View view) {
						if (mState == STATE_INSERT) {
							insertDiary();
						} else {
							updateDiary();
						}
						Intent mIntent = new Intent();
						setResult(RESULT_OK, mIntent);
						finish();
					}
				});

		findViewById(R.id.cancel).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						setResult(RESULT_CANCELED);
						finish();
					}
				});

	}

	private void insertDiary() {
		String title = mTitleText.getText().toString();
		String body = mBodyText.getText().toString();
		ContentValues values = new ContentValues();
		values.put(Diary.DiaryColumns.CREATED,
				DiaryContentProvider.getFormateCreatedDate());
		values.put(Diary.DiaryColumns.TITLE, title);
		values.put(Diary.DiaryColumns.BODY, body);
		//使用ContentResolve对象插入数据
		getContentResolver().insert(Diary.DiaryColumns.CONTENT_URI, values);

	}

	private void updateDiary() {
		String title = mTitleText.getText().toString();
		String body = mBodyText.getText().toString();
		ContentValues values = new ContentValues();
		values.put(Diary.DiaryColumns.CREATED,
				DiaryContentProvider.getFormateCreatedDate());
		values.put(Diary.DiaryColumns.TITLE, title);
		values.put(Diary.DiaryColumns.BODY, body);
		//使用ContentResolve对象更新数据
		getContentResolver().update(mUri, values, null, null);
	}
}
