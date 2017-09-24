package com.wgm.scaneqinfo.operate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.wgm.scaneqinfo.conf.Api;
import com.wgm.scaneqinfo.entity.User;

public class UserOperate extends BaseOperate{
	private boolean isSuccess;
	@Override
	public void asyncRequest(Map<String, String> params,
			AsyncRequestCallBack callBack) {
		String method = params.get("method");
		if("user.login".equals(method))
			super.asyncRequest(params, Api.BASE_URL, callBack);
	}

	@Override
	protected void handleResponse(JSONObject response) {
		super.handleResponse(response);
		isSuccess = response.optBoolean("success",false);
	}

	public boolean getSuccess(){
		return isSuccess;
	}
	
}
