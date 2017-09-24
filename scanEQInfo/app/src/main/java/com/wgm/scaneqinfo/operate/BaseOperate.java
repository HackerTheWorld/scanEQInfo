package com.wgm.scaneqinfo.operate;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.wgm.scaneqinfo.conf.Setting;
import com.wgm.scaneqinfo.util.DialogUtil;

public class BaseOperate {
	private JSONObject response;

	private Handler handler;
	private String msg = null;
	private static String JSESSIONID;
	/**
	 * 返回操作完成后的返回结果json
	 * @return
	 */
	public JSONObject getResponse() {
		return response;
	}
	/**
	 * 操作是否成功
	 * 成果返回true；失败返回false
	 * @return
	 */
	public boolean checkResponse(){
		return msg == null?true:false;
	}
	/**
	 * 操作是否成功
	 * 成功返回true,失败返回false同时显示失败消息
	 * @param context
	 * @return
	 */
	public boolean checkResponseAndShowMsg(Context context){
		if(!checkResponse()){
			DialogUtil.showToastMsg(context, msg);
			return false;
		}
		return true;
	}
	public String getErrorMsg(){
		return msg != null?msg:"操作未成功，请稍后重试";
	}
	public void setMsg(String msg){
		this.msg = msg;
	}
	/**
	 * 覆盖此方法，然后在重写方法中调用request(Map<String,String> params,String url);
	 * @param params
	 */
	public void request(Map<String,String> params){
		throw new RuntimeException("unimplements method");
	}
	//增加一些日志输出，便于项目调试
	protected void request(Map<String,String> params,String url){
		HttpURLConnection urlConnection = null;
		try {
			URL u = new URL(url);
			urlConnection = (HttpURLConnection)u.openConnection();
			//设置超时
			urlConnection.setConnectTimeout(30000);
			urlConnection.setReadTimeout(30000);
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			//urlConnection.setRequestProperty("Content-type", "application/json;charset=utf-8");
			StringBuffer request = new StringBuffer();
			request.append(url).append("?");
			StringBuffer body = new StringBuffer();
			/*DataOutputStream dos = new DataOutputStream(urlConnection.getOutputStream());
			dos.writeBytes(body.toString());
			dos.flush();
			dos.close();*/
			if(params != null && !params.isEmpty()){
				Set<Entry<String,String>> entites = params.entrySet();
				boolean isFirst = true;
				//List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
				for(Entry<String, String> entry : entites){
					//BasicNameValuePair pair = new BasicNameValuePair(entry.getKey(),
					//		entry.getValue());
					//pairs.add(pair);
					if(!isFirst){
						body.append("&");
						request.append("&");
					}
					body.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(),"UTF-8"));
					request.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(),"UTF-8"));
					isFirst = false;
				}
				DataOutputStream dos = new DataOutputStream(urlConnection.getOutputStream());
				dos.writeBytes(body.toString());
				dos.flush();
				dos.close();
			}
			if(null != JSESSIONID){
				urlConnection.setRequestProperty("Cookie", "JSESSIONID="+JSESSIONID);
				//req.setHeader("Cookie","JSESSIONID="+JSESSIONID);
			}
			Log.d(Setting.TAG, request.toString());
			if(urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK){
				msg = "服务器繁忙，请稍后重试！";
			}
			else{
				InputStream is = urlConnection.getInputStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[4096];
				int ss = 0;
				while((ss = is.read(buffer))!=-1){
					baos.write(buffer,0,ss);
				}
				/*String key = null;
				String cookieVal = null;
				Map<String,List<String>> map = urlConnection.getHeaderFields();
				for (int i=1;(key = urlConnection.getHeaderFieldKey(i))!=null;i++) {
					if (key!=null && key.equalsIgnoreCase("Set-Cookie")) {
						cookieVal = urlConnection.getHeaderField(i);
						String cookieVal1 = cookieVal.substring(0,cookieVal.indexOf(";"));
						if("JSESSIONID".equals(cookieVal.substring((start, end))){

						}
					}
				}*/
				String responseBody = new String(baos.toByteArray(),"UTF-8");
				response = new JSONObject(responseBody);
				handleResponse(response);

			}
			urlConnection.disconnect();
			//Inputs urlConnection.getInputStream();
			//int responseCode = resp.getStatusLine().getStatusCode();
			//String body = EntityUtils.toString(resp.getEntity(),"utf-8");
			//if(responseCode != 200){
			//	msg = "服务器繁忙，请稍后重试！";
			//}
			//else{
			//	CookieStore cookieStore = ((DefaultHttpClient)client).getCookieStore();
			//	List<Cookie> cookies = cookieStore.getCookies();
			//	for(int i = 0;i<cookies.size();i++){
			//		if("JSESSIONID".equals(cookies.get(i).getName())){
			//			JSESSIONID = cookies.get(i).getValue();
			//			break;
			//		}
			//	}
			//	response = new JSONObject(body);
			//	handleResponse(response);
			//}
			Log.d(Setting.TAG, "response: "+response);
			//resp.getEntity().consumeContent();
		} catch(JSONException ex){
			Log.d(Setting.TAG, "",ex);
			msg = "内容解析异常！";
		} catch (MalformedURLException e) {
			Log.d(Setting.TAG, "",e);
			msg = "无法连接到服务器，请确认当前已连接的网络！";
		} catch (IOException e) {
			Log.d(Setting.TAG, "",e);
			msg = "连接服务器超时！";
		}
		catch(Exception ex){
			Log.d(Setting.TAG, "",ex);
			msg = "操作失败！";
		}


	}

	public void asyncRequest(Map<String,String> params,AsyncRequestCallBack callBack){
		throw new RuntimeException("unimplements method");
	}

	public void asyncRequest(final Map<String,String> params,final String url,
							 final AsyncRequestCallBack callBack){
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(callBack != null)
					callBack.callBack();
			}
		};
		new Thread(){
			@Override
			public void run() {
				request(params,url);
				handler.sendEmptyMessage(0);
			}

		}.start();
	}

	/**
	 * 操作完成后的回调函数，子类需要重写此方法。只有操作成果时才执行此方法
	 * @param response
	 */
	protected void handleResponse(JSONObject response){
		//操作成功时的回调函数
	}

	//定义回调接口
	public interface AsyncRequestCallBack{
		public void callBack();
	}
}
