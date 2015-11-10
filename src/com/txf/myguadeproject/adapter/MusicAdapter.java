package com.txf.myguadeproject.adapter;

import java.util.ArrayList;

import com.txf.myguadeproject.R;
import com.txf.myguadeproject.utils.FileBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicAdapter extends BaseAdapter {
	private ArrayList<FileBean> list;

	private int selectPos = -1;

	private LayoutInflater mInflater;

	public MusicAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
	}

	public void setFileList(ArrayList<FileBean> list) {
		this.list = list;
	}

	public void addFileBean(FileBean bean) {
		if (null == list) {
			list = new ArrayList<FileBean>();
		}
		list.add(bean);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null == list ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// TODO Auto-generated method stub
		ViewHolder holder;
		if (null == convertView) {
//			convertView = mInflater.inflate(R.layout.music_list_item, null);
			convertView = mInflater.inflate(R.layout.musicitme, null);
			holder = new ViewHolder();
//			holder.arrow = (ImageView) convertView
//					.findViewById(R.id.music_play_arrow);
//			holder.icon = (ImageView) convertView.findViewById(R.id.music_icon);
			holder.title = (TextView) convertView
					.findViewById(R.id.music_title);
//			holder.lenght = (TextView) convertView
//					.findViewById(R.id.music_lenght);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		FileBean bean = list.get(position);
		holder.icon.setImageResource(bean.getIconRes());
		holder.title.setText(bean.getFileName());
		holder.lenght.setText(bean.getFileLenght());

		if (selectPos == position) {
			holder.arrow.setVisibility(View.VISIBLE);
			holder.title.setTextSize(20);
			holder.title.setTextColor(0xff00ff00);
			holder.lenght.setTextColor(0xff00ff00);
		} else {
			holder.arrow.setVisibility(View.INVISIBLE);
			holder.title.setTextSize(16);
			holder.title.setTextColor(0xffffffff);
			holder.lenght.setTextColor(0xffffffff);
		}
		return convertView;

	}

	class ViewHolder {
		ImageView arrow;
		ImageView icon;
		TextView title;
		TextView lenght;
	}

	public int getSelectPos() {
		return selectPos;
	}

	public void setSelectPos(int selectPos) {
		this.selectPos = selectPos;
	}

}
