package com.wgm.scaneqinfo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import android.content.Context;
import android.os.Environment;

/**
 * <br/>文件管理类
 * <br/>FileManager.java
 * @author funny
 * <br/>
 * 2012-7-15
 *
 */
public class FileManager {

	private static FileManager manager;

	/**
	 * 有效缓存时间是3天
	 */
	private static final long maxCacheTime = 1000*60*60*24*15;

	/**
	 * 构造函数
	 * @param context
	 */
	public FileManager(){
	}
	/**
	 * 单例，获取FileManager实例
	 * @param context
	 * @return
	 */
	public static FileManager getInitialize(){
		if(manager == null)
			manager = new FileManager();
		return manager;
	}
	/**
	 * 获取缓存cache目录路径
	 * @param context	上下文
	 * @return
	 */
	public String getCacheDir(Context context, String path){
		StringBuffer dir = new StringBuffer();
		dir.append(context.getFilesDir().getAbsolutePath());
		if(!path.startsWith("/"))
			dir.append("/");
		dir.append(path);
		File file = new File(dir.toString());
		if(!file.exists())
			file.mkdirs();
		return dir.toString();
	}

	/**
	 * 获取sd卡缓存路径
	 *
	 * @param path		路径
	 * @return
	 * 			有sd卡的返回sd上的路径
	 * 			没有sd卡返回null
	 */
	public String getSDDir(String path){
		if(!isSdCardAvailable())return null;
		StringBuffer dir = new StringBuffer();
		dir.append(Environment.getExternalStorageDirectory());
		if(!path.startsWith("/"))
			dir.append("/");
		dir.append(path);
		File file = new File(dir.toString());
		if(!file.exists())
			file.mkdirs();
		return dir.toString();
	}

	/**
	 * 获取缓存文件的路径
	 * @param path	缓存文件的路径
	 * @param url	保存的文件的url
	 * @return
	 */
	public String getCacheFileUrl(Context context,String path, String url){
		return getSDOrCache(context,path)+formatPath(url);
	}

	/**
	 * 获取缓存路径
	 * @param context	上下文
	 * @param path		路径
	 * @return
	 * 			有sd卡返回sd上的路径
	 * 			没有sd卡返回cach中的路径
	 */
	public String getSDOrCache(Context context, String path){
		String url = null;
		if(isSdCardAvailable())
			url = getSDDir(path);
		else
			url = getCacheDir(context, path);
		return url;
	}
	/**
	 * 检测内存卡是否可用
	 * @return  可用则返回 true
	 */
	public boolean isSdCardAvailable(){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			return true;
		}
		return false;
	}
	/**
	 * 格式化url地址
	 * 	以便根据url来命名文件
	 * @param name	图片url
	 * @return
	 */
	public String formatPath(String name){
		String path = name;
		path = path.replace(":", "_");
		path = path.replace("/", "_");
		return path;
	}
	/**
	 * 判断该路径的文件是否存在
	 * @param path	文件路径（完全路径）
	 * @return
	 */
	public boolean isExists(String path){
		File file = new File(path);
		if(file.exists())
			return true;
		return false;
	}

	/**
	 * 判断当前目录是否存在文件
	 * @param path
	 * @return
	 */
	public boolean hasMoreFile(String path){
		File file = new File(path);
		if(!file.exists())
			return false;
		String[] files = file.list();
		if(files == null || files.length == 0)
			return false;
		return true;
	}
	/**
	 * 清除sd卡中过期的缓存
	 *
	 * @param basePath	路径(要清楚文件的路径)
	 */
	public void cleanInvalidCache(String basePath){
		File file = new File(basePath);
		if(!file.exists()) return;
		File[] caches = file.listFiles();
		if(caches == null || caches.length == 0) return;
		long now = System.currentTimeMillis();
		try {
			for(int i=0;i<caches.length;i++){
				if((now - caches[i].lastModified()) < maxCacheTime){
					continue;
				}
				caches[i].delete();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
					//每删除一张图片后停留10毫秒，防止cpu占用率过高，造成程序无响应
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存保存输入流
	 * @param path	要保存的完全路径
	 * @param is	网络获取的inputstream流
	 */
	public void saveInputStream(String path,InputStream is){
		if(!isExists(path)){
			File file = new File(path);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				int size = 0;
				byte[] buffer = new byte[1024];
				while((size = is.read(buffer)) != -1)
					fos.write(buffer, 0, size);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally{
				try {
					if(fos != null){
						fos.close();
						fos = null;
					}
					if(file != null)
						file = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 从文件中读取流
	 * @param path	文件路径
	 * @return
	 */
	public InputStream loadInputStream(String path){
		InputStream is = null;
		if(isExists(path)){
			File file = new File(path);
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}
		return is;
	}


	/**
	 * 把对象写入文件
	 *
	 * @throws Exception
	 */
	public void WriteObject(Object obj,Context context,String path) throws Exception {
		FileOutputStream fis = context.openFileOutput(path, Context.MODE_PRIVATE);
		ObjectOutputStream oos = new ObjectOutputStream(fis);
		oos.writeObject(obj);
		oos.close();
		fis.close();
	}

	/**
	 * 从文件读取对象
	 *
	 * @return
	 * @throws Exception
	 */
	public Object readObject(Context context,String path) throws Exception {
		Object obj = new Object();
		FileInputStream fis = context.openFileInput(path);
		ObjectInputStream oos = new ObjectInputStream(fis);
		obj = oos.readObject();
		oos.close();
		fis.close();
		return obj;
	}

	/**
	 * 删除保存文件
	 * @param path  文件的完整路径
	 */
	public void delete(String path){
		File file = new File(path);
		if(file.exists())
			file.delete();
	}
}
