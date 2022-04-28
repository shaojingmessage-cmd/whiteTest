package com.java.jacoco.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

	public static void writeFile(String FilePath, String str) throws IOException {
		BufferedWriter writer = null;
		try {
//			FileWriter writer = new FileWriter(FilePath);
//			
//			writer.write(str);
//			writer.close();
			
			File file = new File(FilePath);
			FileOutputStream writerStream = new FileOutputStream(file);
			writer = new BufferedWriter(new OutputStreamWriter(writerStream, "UTF-8"));

			StringBuilder strBuild = new StringBuilder();
			strBuild.append(str);

			writer.write(strBuild.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			writer.flush();
			writer.close();
		}
	}

	// read file content into a string
	public static String readFileToString(String filePath) throws IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));

		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			// System.out.println(numRead);
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}

		reader.close();

		return fileData.toString();
	}

	public static void mkDir(String dir) {
		File file = new File(dir);
		if (!file.exists()) {// 如果文件夹不存在
			file.mkdir();// 创建文件夹
		}
	}

	public static List<String> getAllFile(String directoryPath, boolean isAddDirectory) {
		List<String> list = new ArrayList<String>();
		File baseFile = new File(directoryPath);
		if (baseFile.isFile() || !baseFile.exists()) {
			return list;
		}
		File[] files = baseFile.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				if (isAddDirectory) {
					list.add(file.getAbsolutePath());
				}
				list.addAll(getAllFile(file.getAbsolutePath(), isAddDirectory));
			} else {
				list.add(file.getAbsolutePath());
			}
		}
		return list;
	}

	/**
	 * ———————————————— 版权声明：本文为CSDN博主「瞌睡先生想睡觉」的原创文章，遵循CC 4.0
	 * BY-SA版权协议，转载请附上原文出处链接及本声明。
	 * 原文链接：https://blog.csdn.net/w18756901575/article/details/70238234
	 **/
	public  static String getStringByEnter(int length, String string) throws Exception {
		for (int i = 1; i <= string.length(); i++) {
			if (string.substring(0, i).getBytes("UTF-8").length > length) {
				return string.substring(0, i - 1) + "\n" + getStringByEnter(length, string.substring(i - 1));
			}
		}
		return string;
	}
}
