package com.wgm.scaneqinfo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;

public class DownlaodFileThread {
	private ExecutorService mService;
	private static DownlaodFileThread downloadFileThread;
	//线程个数定义
	private static final int THREAD_COUNT = 5;
	//socket超时时间定义
	private static final int TIME_OUT = 1000*20;
	private static final int BUFFER_SIZE = 1024;
	private DownlaodFileThread(){
		if(mService == null){
			mService = Executors.newFixedThreadPool(THREAD_COUNT);
		}
	}
	//单例
	public static DownlaodFileThread getInstance(){
		if(downloadFileThread == null)
			downloadFileThread = new DownlaodFileThread();
		return downloadFileThread;
	}

	public void fetchData(DownlaodApkThread downlaod){
		mService.submit(downlaod);
	}

	public static class DownlaodApkThread implements Runnable{
		/**
		 * 已下载大小
		 */
		private int hasDownload = 0;
		/**
		 * 从流中读取的长度
		 */
		private int len = -1;
		/**
		 * 文件大小
		 */
		private int size = 0;
		/**
		 * 文件下载的百分比
		 */
		private int rate = 0;
		private String path;
		private String url;
		private Context context;
		private URL mUrl;
		private DownloadCallBack downloadCallBack;
		public void setCallBack(DownloadCallBack downloadCallBack){
			this.downloadCallBack = downloadCallBack;
		}
		public void setUrl(Context context,String url,String path){
			this.context = context;
			this.url = url;
			this.path = path;
			try {
				mUrl = new URL(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			if(mUrl == null){
				throw new IllegalArgumentException("URL must be init!");
			}
			hasDownload = 0;
			len = -1;
			rate = 0;
			File f = new File(path);
			if(!f.exists())
				f.mkdirs();
			//组装文件路径
			String targetFileName = path+url.substring(url.lastIndexOf("/")+1);
			File downloadFile = new File(targetFileName);

			FileOutputStream fos = null;
			HttpURLConnection conn = null;
			InputStream is = null;
			byte[] buffer = new byte[BUFFER_SIZE];
			int size = 0;
			try {
				conn = (HttpURLConnection)mUrl.openConnection();
				conn.setConnectTimeout(TIME_OUT);
				conn.setReadTimeout(TIME_OUT);
				conn.connect();
				size = conn.getContentLength();
				System.out.println("DownloadFile > "+size+"  =="+downloadFile.length());
				if(size == downloadFile.length()){
					//System.out.println("download complete!");
					downloadCallBack.callBack(targetFileName);
					return;
				}
				else
					downloadFile.delete();
				is = conn.getInputStream();
				if(FileManager.getInitialize().isSdCardAvailable()){
					fos = new FileOutputStream(downloadFile);
				}
				else{
					fos = context.openFileOutput(url.substring(url.lastIndexOf("/")+1), Context.MODE_WORLD_READABLE);
				}
				//1 设置进度条为0
				downloadCallBack.setProgressBar(0);
				Thread.sleep(10L);

				while((len = is.read(buffer))!=-1){
					fos.write(buffer,0,len);
					hasDownload += len;
					if(hasDownload <size && hasDownload*100/size>rate){
						rate = hasDownload*100/size;
						if(rate < 100){
							//System.out.println("通知进度条更新，更新进度");
							downloadCallBack.setProgressBar(rate);
						}
					}
				}
				if(hasDownload == size){
					//System.out.println("通知进度条更新，说明下载完毕");
					downloadCallBack.callBack(targetFileName);
				}
				fos.flush();
				fos.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally{
				try{
					if(fos != null){
						fos.close();
					}
					if(is != null)
						is.close();
					if(conn != null)
						conn.disconnect();
					if(f != null)
						f = null;
				}
				catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}

	public static interface DownloadCallBack{
		/**
		 * 下载完成后回调方法
		 * @param path 保存apk的路径
		 */
		public void callBack(String path);
		/**
		 * 下载过程中通知显示进度条
		 * @param rate
		 */
		public void setProgressBar(int rate);
	}
}