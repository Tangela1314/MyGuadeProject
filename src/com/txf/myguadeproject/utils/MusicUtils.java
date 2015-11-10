package com.txf.myguadeproject.utils;

import java.text.DecimalFormat;

public class MusicUtils {
	/**
	 * 获取文件后缀名
	 * 
	 * @param fileName
	 * @return 文件后缀名
	 */
	public static String getFileType(String fileName) {
		if (fileName != null) {
			int typeIndex = fileName.lastIndexOf(".");
			if (typeIndex != -1) {
				String fileType = fileName.substring(typeIndex + 1)
						.toLowerCase();
				return fileType;
			}
		}
		return "";
	}

	/**
	 * 根据后缀名判断是否是音频文件
	 * 
	 * @param type
	 * @return
	 */
	public static boolean isAudio(String type) {
		if (type.equals("m4a") || type.equals("mp3") || type.equals("mid")
				|| type.equals("xmf") || type.equals("ogg")
				|| type.equals("wav") || type.equals("arm")
				|| type.equals("aac") || type.equals("wma")
				|| type.equals("ape") || type.equals("midi")
				|| type.equals("ra") || type.equals("rmx")
				|| type.equals("amr") || type.equals("flac")
				|| type.equals("dat") || type.equals("au")
				|| type.equals("mpga") || type.equals("mp2")
				|| type.equals("aiff") || type.equals("af")
				|| type.equals("m3u") || type.equals("rmm")
				|| type.equals("ram")) {
			return true;
		}
		return false;
	}

	/**
	 * 计算文件的大小
	 * 
	 * @param fileSize
	 * @return 返回显示的文件的大小信息
	 */
	public static String compSize(long fileSize) {
		DecimalFormat df = new DecimalFormat("0.00");
		if (fileSize >= 1073741824) {
			return df.format(fileSize / 1073741824) + "GB";
		} else if (fileSize >= 1048576) {
			return df.format(fileSize / 1048576) + "MB";
		} else if (fileSize >= 1024) {
			return df.format(fileSize / 1024) + "KB";
		} else {
			return df.format(fileSize) + "B";
		}
	}
}
