package com.txf.myguadeproject.utils;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class FileBean implements Parcelable {
	private int iconRes;

	private Bitmap iconBitmap;

	private String fileName;

	private String fileLenght;

	private String filePath;

	private boolean isDirectory;

	public int getIconRes() {
		return iconRes;
	}

	public FileBean setIconRes(int iconRes) {
		this.iconRes = iconRes;
		return this;
	}

	public String getFileName() {
		return fileName;
	}

	public FileBean setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public String getFilePath() {
		return filePath;
	}

	public FileBean setFilePath(String filePath) {
		this.filePath = filePath;
		return this;
	}

	public FileBean() {
	}

	public FileBean(int iconRes, String fileName, String filePath) {
		super();
		this.iconRes = iconRes;
		this.fileName = fileName;
		this.filePath = filePath;
	}

	public String getFileLenght() {
		return fileLenght;
	}

	public FileBean setFileLenght(String fileLenght) {
		this.fileLenght = fileLenght;
		return this;
	}

	public FileBean setFileLenght(long fileLenght) {
		return this.setFileLenght(MusicUtils.compSize(fileLenght));
	}

	public boolean isDirectory() {
		return isDirectory;
	}

	public FileBean setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
		return this;
	}

	public Bitmap getIconBitmap() {
		return iconBitmap;
	}

	public FileBean setIconBitmap(Bitmap iconBitmap) {
		this.iconBitmap = iconBitmap;
		return this;
	}

	public FileBean(Bitmap iconBitmap, String fileName, String fileLenght,
			String filePath, boolean isDirectory) {
		super();
		this.iconBitmap = iconBitmap;
		this.fileName = fileName;
		this.fileLenght = fileLenght;
		this.filePath = filePath;
		this.isDirectory = isDirectory;
	}
	
	public static final Parcelable.Creator<FileBean> CREATOR = new Creator<FileBean>() {
		
		@Override
		public FileBean[] newArray(int size) {
			// TODO Auto-generated method stub
			return new FileBean[size];
		}
		
		@Override
		public FileBean createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new FileBean(source);
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(iconRes);
		dest.writeParcelable(iconBitmap, flags);
		dest.writeString(fileName);
		dest.writeString(fileLenght);
		dest.writeString(filePath);
		dest.writeBooleanArray(new boolean[] { isDirectory });
	}

	public FileBean(Parcel dest) {
		iconRes = dest.readInt();
		iconBitmap = dest.readParcelable(getClass().getClassLoader());
		fileName = dest.readString();
		fileLenght = dest.readString();
		filePath = dest.readString();
		boolean[] ary = new boolean[1];
		dest.readBooleanArray(ary);
		isDirectory = ary[0];
	}

}
