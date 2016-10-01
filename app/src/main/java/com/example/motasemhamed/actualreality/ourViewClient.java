package com.example.motasemhamed.actualreality;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by motasemhamed on 4/25/16.
 */
public class ourViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView v, String url){
        v.loadUrl(url);
        return true;
    }
}
