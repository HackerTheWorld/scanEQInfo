package com.wgm.scaneqinfo.view;

/**
 * Created by apple on 2016/12/23.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wgm.scaneqinfo.R;
import com.wgm.scaneqinfo.common.SysUtil;
import com.wgm.scaneqinfo.conf.Api;
import com.wgm.scaneqinfo.conf.App;
import com.wgm.scaneqinfo.entity.User;
import com.wgm.scaneqinfo.operate.SetconnectOperate;
import com.wgm.scaneqinfo.operate.UserOperate;
import com.wgm.scaneqinfo.util.ClientStoreUtil;
import com.wgm.scaneqinfo.util.DialogUtil;

import java.util.HashMap;
import java.util.Map;

import mexxen.mx5010.barcode.BarcodeEvent;
import mexxen.mx5010.barcode.BarcodeListener;
import mexxen.mx5010.barcode.BarcodeManager;

public class SetconnectActivity extends BaseActivity implements OnClickListener {

    private Button setconnect;
    private EditText url;
    private TextView testinfrared;
    private SetconnectOperate setconnectOperate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_setting);
        initView();
    }
    private void initView(){
        url=(EditText)this.findViewById(R.id.set_url);
        setconnect=(Button)this.findViewById(R.id.btn_setconnect);
        testinfrared=(TextView)this.findViewById(R.id.Infrared);
        setconnect.setOnClickListener(this);

//        final BarcodeManager bm=new BarcodeManager(this);
//          bm.addListener(new BarcodeListener() {
//              @Override
//              public void barcodeEvent(BarcodeEvent barcodeEvent) {
//                  if(barcodeEvent.getOrder().equals("SCANNER_READ")){
//                      String str=bm.getBarcode();
//                      testinfrared.setText(str);
//                  }
//              }
//          });
    }

    @Override
    public void onClick(View v) {

        String u=url.getText().toString();
        setconnectOperate=new SetconnectOperate();



    }

    public boolean testconnect(String url){
        Map<String,String> params = new HashMap<String,String>();
        params.put("method", "user.login");
        params.put("code","static-code");
        setconnectOperate.setUrl(url);
        setconnectOperate.asyncRequest(params, new SetconnectOperate.AsyncRequestCallBack() {
            @Override
            public void callBack() {
                if(!setconnectOperate.getSuccess()){
                    DialogUtil.showAlertMsg(SetconnectActivity.this,setconnectOperate.getMsg());
                    return;
                }
                else {
                    //DialogUtil.showAlertMsg(LoginActivity.this, "登录成功！");
                    Intent intent = new Intent(SetconnectActivity.this,LoginActivity.class);
                    startActivity(intent);
                    Api.BASE_URL=setconnectOperate.getUrl();
                }
            }
        });
        return false;
    }

}
