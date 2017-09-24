package com.wgm.scaneqinfo.operate;

import com.wgm.scaneqinfo.conf.Api;
import com.wgm.scaneqinfo.entity.Product;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by funny on 16/12/22.
 */

public class ProductOperate extends BaseOperate{
    private Product product;
    @Override
    public void asyncRequest(Map<String, String> params,
                             AsyncRequestCallBack callBack) {
        String method = params.get("method");
        if("product.scan".equals(method))
            super.asyncRequest(params, Api.BASE_URL, callBack);
        if("product.upload".equals(method)){
            super.asyncRequest(params, Api.BASE_URL, callBack);
        }
    }
    @Override
    protected void handleResponse(JSONObject response) {
        super.handleResponse(response);
        parseProduct(response);
    }
    private Product parseProduct(JSONObject response){
        return null;
    }
}
