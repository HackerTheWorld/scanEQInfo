package com.wgm.scaneqinfo.view;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.wgm.scaneqinfo.R;

public class MainActivity extends BaseActivity implements OnClickListener{
    private Button btn_scanInStock;
    private Button btn_scanOutStock;
    private Button btn_countStock;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        btn_scanInStock = (Button)this.findViewById(R.id.scanInStockBtn);
        btn_scanOutStock = (Button)this.findViewById(R.id.scanOutStockBtn);
        btn_countStock = (Button)this.findViewById(R.id.scanCountStockBtn);
        btn_scanInStock.setOnClickListener(this);
        btn_scanOutStock.setOnClickListener(this);
        btn_countStock.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //入库
        if(btn_scanInStock == v){
            //Intent intent = new Intent(this,ScanInStockActivity.class);
            //startActivity(intent);
        }
        //出库
        if(btn_scanOutStock == v){
            //Intent intent = new Intent(this,ScanOutStockActivity.class);
            //startActivity(intent);
        }
        //查询，盘点
        if(btn_countStock == v){
            //Intent intent = new Intent(this,ScanRepairActivity.class);
            //startActivity(intent);
        }
    }
}
