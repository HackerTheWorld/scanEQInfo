package com.wgm.scaneqinfo.operate;

import android.widget.EditText;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by apple on 2016/12/23.
 */

public class SetconnectOperate {

    private boolean test=false;

    public boolean testconnect(String url) {
                try {
                    URL realurl = new URL(url);
                    realurl.openConnection();
                    test=true;
                } catch (IOException e) {
                    test=false;
                }
        return test;
    }
}


