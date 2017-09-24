package com.wgm.scaneqinfo.operate;

import java.util.Map;

import org.json.JSONObject;

import com.wgm.scaneqinfo.conf.Api;

public class VersionInfoOperate extends BaseOperate{
	private String url;
	@Override
	public void asyncRequest(Map<String, String> params,
			AsyncRequestCallBack callBack) {
		super.asyncRequest(params, Api.API00001,callBack);
	}

	@Override
	protected void handleResponse(JSONObject response) {
		super.handleResponse(response);
		String ss = response.optString("s");
		if("-1".equals(ss)){
			setMsg("没传入版本号");
			return;
		}
		if("1".equals(ss)){
			setMsg("没有新版本可以更新");
			return;
		}
		if("2".equals(ss)){
			JSONObject obj = response.optJSONObject("version");
			if(obj != null){
				url = obj.optString("url");
			}
		}
	}

	public String getUrl() {
		return url;
	}
	
}
