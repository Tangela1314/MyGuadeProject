package com.txf.myguadeproject.activity;

import io.vov.a.c;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.lg.lrcview_master.DefaultLrcParser;
import com.lg.lrcview_master.LrcRow;
import com.lg.lrcview_master.LrcView;
import com.lg.lrcview_master.LrcView.OnLrcClickListener;
import com.lg.lrcview_master.LrcView.OnSeekToListener;
import com.txf.myguadeproject.R;
import com.txf.myguadeproject.adapter.MusicAdapter;

import android.app.ListActivity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MusicPlayerActivity extends ListActivity {
	/** 控制歌词字体大小的SeekBar ***/
//	private SeekBar mLrcSeekBar;
	private LrcView mLrcView;
	private Toast mPlayerToast;
	private Toast mLrcToast;
	private String allpath = null;
	private int current = 0;
	
	private MusicAdapter musicAdapter;

	// 定义一个播放位置获取的消息
	private static final int MSG_PLAY_POS = 1;

	/* 几个操作按钮 */
	private ImageButton mFrontImageButton = null;
	private ImageButton mStopImageButton = null;
	private ImageButton mStartImageButton = null;
	private ImageButton mPauseImageButton = null;
	private ImageButton mNextImageButton = null;
	// 播放音乐的进度
	private SeekBar mSeekBar;

	/* MediaPlayer对象 */
	public MediaPlayer mMediaPlayer = null;

	/* 播放列表 */
	private List<String> mMusicList = new ArrayList<String>();

	/* 当前播放歌曲的索引 */
	private int currentListItme = 0;

	/* 音乐的路径 */
	private static final String MUSIC_PATH = new String("/sdcard/");

	private static final String[] data = new String[] { "buzaijian.lrc",
			"我们说好的.lrc","给我一个理由忘记.lrc" };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playmusic);
		/* 更新显示播放列表 */
		musicList();
		init();
		
		mFrontImageButton = (ImageButton) findViewById(R.id.LastImageButton);
		mStopImageButton = (ImageButton) findViewById(R.id.StopImageButton);
		mStartImageButton = (ImageButton) findViewById(R.id.StartImageButton);
		mPauseImageButton = (ImageButton) findViewById(R.id.PauseImageButton);
		mNextImageButton = (ImageButton) findViewById(R.id.NextImageButton);
		musicAdapter= new MusicAdapter(this);
		// 播放进度控制
		mSeekBar = (SeekBar) findViewById(R.id.m_seekbar);
		mSeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);

		// 停止按钮
		mStopImageButton.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View v) {
				/* 是否正在播放 */
				if (mMediaPlayer != null) {
					mHandler.removeMessages(MSG_PLAY_POS);
					// 重置MediaPlayer到初始状态
					mMediaPlayer.stop();
					// 释放资源
					mMediaPlayer.release();
					mMediaPlayer = null;
				}
			}
		});

		// 开始按钮
		mStartImageButton.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mMediaPlayer && !mMediaPlayer.isPlaying()) {
					mMediaPlayer.start();
//					setAdapter();
					// 发送消息
					mHandler.sendEmptyMessage(MSG_PLAY_POS);
				} else {
					if (null == mMediaPlayer) {
						playMusic(MUSIC_PATH + mMusicList.get(currentListItme));
						mMediaPlayer
								.setOnCompletionListener(onCompletionListener);
//						setAdapter();
					}
				}
			}
		});

		// 暂停
		mPauseImageButton.setOnClickListener(new ImageButton.OnClickListener() {
			public void onClick(View view) {
				// mMediaPlayer.isPlaying()判断是否正在播放
				if (null != mMediaPlayer && mMediaPlayer.isPlaying()) {
					// 移除消息，停止循环获取当前进度
					mHandler.removeMessages(MSG_PLAY_POS);
					/* 暂停 */
					mMediaPlayer.pause();
				}
			}
		});

		// 下一首
		mNextImageButton.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				nextMusic();
//				setAdapter();
				if (current==data.length-1) {
					current=0;
					mLrcView.setLrcRows(getLrcRows());
				}else {
					current++;
					mLrcView.setLrcRows(getLrcRows());
				}
			}
		});
		// 上一首
		mFrontImageButton.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				frontMusic();
//				setAdapter();
				if (current==0) {
					current=data.length-1;
					mLrcView.setLrcRows(getLrcRows());
				}else {
					
					current--;
					mLrcView.setLrcRows(getLrcRows());
				}
			}
		});

	}

	private void init() {
		mLrcView = (LrcView) findViewById(R.id.lrcView);
		mLrcView.setOnSeekToListener(onSeekToListener);
		mLrcView.setOnLrcClickListener(onLrcClickListener);
//		mLrcSeekBar = (SeekBar) findViewById(R.id.include_lrc_seekbar);
//		mLrcSeekBar.setMax(100);
		// 为seekbar设置当前的progress
//		mLrcSeekBar
//				.setProgress((int) ((mLrcView.getmCurScalingFactor() - LrcView.MIN_SCALING_FACTOR)
//						/ (LrcView.MAX_SCALING_FACTOR - LrcView.MIN_SCALING_FACTOR) * 100));
//		mLrcSeekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
		if (current==0) {
			mLrcView.setLrcRows(getLrcRows());
		}
	}
	
//	private void setAdapter(){
//		musicAdapter.setSelectPos(currentListItme);
//		musicAdapter.notifyDataSetChanged();
//	}

	// mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
	OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			int currentPos = seekBar.getProgress();
			// 设置播放位置
			mMediaPlayer.seekTo(currentPos);
			if (mMediaPlayer.isPlaying()) {
				mHandler.sendEmptyMessage(MSG_PLAY_POS);
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			if (null != mMediaPlayer && mMediaPlayer.isPlaying()) {
				mHandler.removeMessages(MSG_PLAY_POS);
			}
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			if (seekBar == mSeekBar) {
				mLrcView.seekTo(progress, true, fromUser);
				if (fromUser) {
					showPlayerToast(formatTimeFromProgress(progress));
				}
			} else /*if (seekBar == mLrcSeekBar) */{
				float scalingFactor = LrcView.MIN_SCALING_FACTOR
						+ progress
						* (LrcView.MAX_SCALING_FACTOR - LrcView.MIN_SCALING_FACTOR)
						/ 100;
				mLrcView.setLrcScalingFactor(scalingFactor);
				showLrcToast((int) (scalingFactor * 100) + "%");
			}

		}
	};

	OnCompletionListener onCompletionListener = new OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {
			mLrcView.reset();
			mLrcView.setLrcRows(getLrcRows());
			mHandler.removeMessages(0);
			mSeekBar.setProgress(0);
		}
	};

	OnLrcClickListener onLrcClickListener = new OnLrcClickListener() {

		@Override
		public void onClick() {
			Toast.makeText(getApplicationContext(), "歌词被点击啦",
					Toast.LENGTH_SHORT).show();
		}
	};
	OnSeekToListener onSeekToListener = new OnSeekToListener() {

		@Override
		public void onSeekTo(int progress) {
			mMediaPlayer.seekTo(progress);

		}
	};

	/**
	 * 获取歌词List集合
	 * 
	 * @return
	 */
	private List<LrcRow> getLrcRows() {
		List<LrcRow> rows = null;

		// InputStream is = getResources().openRawResource(R.raw.buzaijian);
		// BufferedReader br = new BufferedReader(new InputStreamReader(is));

		// String filename = "buzaijian.lrc";// 闺蜜 - 一起老去.krc
		// allpath = MUSIC_PATH + filename;
		String filename = data[current];
		allpath = MUSIC_PATH + filename;
			StringBuffer sb = new StringBuffer();
			File file = new File(allpath);
			String line;
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				System.out.println(sb.toString());
				rows = DefaultLrcParser.getIstance().getLrcRows(sb.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return rows;

	}

	/**
	 * 将播放进度的毫米数转换成时间格式 如 3000 --> 00:03
	 * 
	 * @param progress
	 * @return
	 */
	private String formatTimeFromProgress(int progress) {
		// 总的秒数
		int msecTotal = progress / 1000;
		int min = msecTotal / 60;
		int msec = msecTotal % 60;
		String minStr = min < 10 ? "0" + min : "" + min;
		String msecStr = msec < 10 ? "0" + msec : "" + msec;
		return minStr + ":" + msecStr;
	}

	private TextView mPlayerToastTv;

	private void showPlayerToast(String text) {
		if (mPlayerToast == null) {
			mPlayerToast = new Toast(this);
			mPlayerToastTv = (TextView) LayoutInflater.from(this).inflate(
					R.layout.toast, null);
			mPlayerToast.setView(mPlayerToastTv);
			mPlayerToast.setDuration(Toast.LENGTH_SHORT);
		}
		mPlayerToastTv.setText(text);
		mPlayerToast.show();
	}

	private TextView mLrcToastTv;

	private void showLrcToast(String text) {
		if (mLrcToast == null) {
			mLrcToast = new Toast(this);
			mLrcToastTv = (TextView) LayoutInflater.from(this).inflate(
					R.layout.toast, null);
			mLrcToast.setView(mLrcToastTv);
			mLrcToast.setDuration(Toast.LENGTH_SHORT);
		}
		mLrcToastTv.setText(text);
		mLrcToast.show();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mMediaPlayer != null) {
				mHandler.removeMessages(MSG_PLAY_POS);
				mMediaPlayer.stop();
				mMediaPlayer.release();
				mMediaPlayer = null;
			}
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	/* 当我们点击列表时，播放被点击的音乐 */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		currentListItme = position;
		current = position;
		// mMusicList存储文件名称列表，参数是文件路径：/sdcard/abc.mp3
		playMusic(MUSIC_PATH + mMusicList.get(position));
		mLrcView.setLrcRows(getLrcRows());
//		setAdapter();
	}
	
	/* 播放列表 */
	public void musicList() {
		// 取得指定位置的文件设置显示到播放列表（遍历MUSIC_PATH下所有.mp3结尾的文件）
		File home = new File(MUSIC_PATH);
		File[] files = home.listFiles(new MusicFilter());
		Log.e("m_tag   files.length", files.length + "");
		if (files.length > 0) {
			for (File file : files) {
				mMusicList.add(file.getName());
			}
			ArrayAdapter<String> musicList = new ArrayAdapter<String>(this,
					R.layout.musicitme, mMusicList);
			setListAdapter(musicList);
		}
	}

	/**
	 * 按照文件路径播放文件
	 * 
	 * @param path
	 */
	private void playMusic(String path) {
		try {
			if (null == mMediaPlayer) {
				/* 构建MediaPlayer对象 */
				mMediaPlayer = new MediaPlayer();
			} else {
				/* 重置MediaPlayer */
				mMediaPlayer.reset();
			}// 1、处于idle状态

			/* 设置要播放的文件的路径 */
			mMediaPlayer.setDataSource(path);// 2、处于initialized状态

			// 设置准备监听
			mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					// 3、进入准备完毕状态
					// mp.getDuration()获取播放的音乐的总时间然后设置到SeekBar的最大值
					mSeekBar.setMax(mp.getDuration());
					/* 开始播放 */
					mp.start();
					// 发送消息监听整个播放过程的播放位置
					mHandler.sendEmptyMessage(MSG_PLAY_POS);
				}
			});
			// 设置播放完成的监听
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {
					// 移除播放位置消息监听
					mHandler.removeMessages(MSG_PLAY_POS);
					// 播放完成一首之后进行下一首
					nextMusic();
				}
			});
			mMediaPlayer.setOnErrorListener(new OnErrorListener() {

				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					// TODO Auto-generated method stub
					return false;
				}
			});
			/* 准备播放 */
			mMediaPlayer.prepareAsync();
		} catch (IOException e) {
		}
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case MSG_PLAY_POS:
				if (null != mMediaPlayer) {
					// 获取当前播放位置
					int pos = mMediaPlayer.getCurrentPosition();
					// 设置当前位置
					mSeekBar.setProgress(pos);
					// 延迟发送
					mHandler.sendEmptyMessageDelayed(MSG_PLAY_POS, 100);
				}
				break;
			}
			super.handleMessage(msg);
		}

	};

	/* 下一首 */
	private void nextMusic() {
		if (currentListItme >= mMusicList.size() - 1) {
			currentListItme = 0;
		} else {
			currentListItme++;
		}
		playMusic(MUSIC_PATH + mMusicList.get(currentListItme));
	}

	/* 上一首 */
	private void frontMusic() {
		if (currentListItme <= 0) {
			currentListItme = mMusicList.size();
		}
		currentListItme--;
		playMusic(MUSIC_PATH + mMusicList.get(currentListItme));
	}

//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		mHandler.removeMessages(0);
//		mMediaPlayer.stop();
//		mMediaPlayer.release();
//		mMediaPlayer = null;
//		mLrcView.reset();
//	}
}

/* 过滤文件类型 */
class MusicFilter implements FilenameFilter {
	public boolean accept(File dir, String name) {
		// 这里还可以设置其他格式的音乐文件
		return name.endsWith(".mp3");
	}

}