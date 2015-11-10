package com.txf.myguadeproject.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.txf.myguadeproject.R;
import com.txf.myguadeproject.activity.MusicPlayerActivity;

/**
 * 音乐播放器
 * 
 * @author Administrator
 * 
 */
public class MusicPlayerFragment extends Fragment implements OnClickListener {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View layout = inflater.inflate(R.layout.fragment_music_player, null);
		layout.findViewById(R.id.btn_toplay).setOnClickListener(this);
		return layout;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btn_toplay) {
			Intent intent = new Intent(getActivity(), MusicPlayerActivity.class);
			startActivity(intent);
		}

	}
}
