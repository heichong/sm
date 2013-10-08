package com.zongb.sm.opencart;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpGetImage {

	/**
	 * @param urlPath
	 *            图片路径
	 * @throws Exception
	 */
	public static void getImages(String urlPath, String fileName)
			throws Exception {
		URL url = new URL(urlPath);// ：获取的路径
		// :http协议连接对象
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setReadTimeout(6 * 10000);
		if (conn.getResponseCode() < 10000) {
			InputStream inputStream = conn.getInputStream();
			byte[] data = readStream(inputStream);
			if (data.length > (1024 * 10)) {
				FileOutputStream outputStream = new FileOutputStream(fileName);
				outputStream.write(data);
				//System.err.println("[" + fileName + "]图片下载成功");
				outputStream.close();
			}
		}

	}

	/**
	 * 读取url中数据，并以字节的形式返回
	 * 
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	private static byte[] readStream(InputStream inputStream) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, len);
		}
		outputStream.close();
		inputStream.close();
		return outputStream.toByteArray();
	}

}
