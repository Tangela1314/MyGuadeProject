package com.txf.myguadeproject.activity;

import com.txf.myguadeproject.R;
import com.txf.myguadeproject.utils.Diary;
import com.txf.myguadeproject.utils.Diary.DiaryColumns;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class RememberActivity extends ListActivity {


	// 插入一条新纪录
	public static final int MENU_ITEM_INSERT = Menu.FIRST;

	private static final String[] PROJECTION = new String[] { DiaryColumns._ID,
			DiaryColumns.TITLE, DiaryColumns.CREATED };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remember);

		renderListView();
		getListView().setOnItemLongClickListener(itemLongClickListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_ITEM_INSERT, 0, R.string.menu_insert);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// 插入一条数据
		case MENU_ITEM_INSERT:
			Intent intent0 = new Intent(this, ActivityDiaryEditor.class);
			// 给Intent设置一个动作
			intent0.setAction(ActivityDiaryEditor.INSERT_DIARY_ACTION);
			intent0.setData(DiaryColumns.CONTENT_URI);
			startActivityForResult(intent0,1);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		lookAtAndEdit(id);
	}

	private OnItemLongClickListener itemLongClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			showOperationDialog(id);
			return false;
		}
	};

	private void lookAtAndEdit(long id) {
		Uri uri = Uri.parse("content://" + Diary.AUTHORITY + "/diaries" + "/"
				+ id);
		startActivityForResult(new Intent(ActivityDiaryEditor.EDIT_DIARY_ACTION, uri),1);
	}

	/**
	 * 显示操作弹出框
	 * 
	 * @param id
	 */
	private void showOperationDialog(final long id) {
		new AlertDialog.Builder(this)
				.setTitle(getResources().getString(R.string.chioce))
				.setItems(
						new String[] { getString(R.string.look_at),
								getString(R.string.delete) },
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								switch (which) {
								case 0:
									// 查看并编辑
									lookAtAndEdit(id);
									break;
								case 1:
									// 删除
									deleteItem(id);
									break;
								}
							}
						})
				.setNegativeButton(getResources().getString(R.string.cancel),
						null).create().show();
	}

	private void deleteItem(final long id) {
		new AlertDialog.Builder(this)
				.setMessage(R.string.confirm_to_delete)
				.setNegativeButton(getString(R.string.cancel), null)
				.setPositiveButton(getString(R.string.confirm),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								Uri uri = ContentUris.withAppendedId(
										Diary.DiaryColumns.CONTENT_URI, id);
								getContentResolver().delete(uri, null, null);
								renderListView();
							}
						}).create().show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK) {
			renderListView();
		}
	}

	private void renderListView() {
		Cursor cursor = getContentResolver().query(
				Diary.DiaryColumns.CONTENT_URI, PROJECTION, null, null, null);

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.diary_row, cursor, new String[] { DiaryColumns.TITLE,
						DiaryColumns.CREATED }, new int[] { R.id.text1,
						R.id.created });
		setListAdapter(adapter);
	}

}
