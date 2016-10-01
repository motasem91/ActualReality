package com.example.motasemhamed.actualreality;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;

import java.io.File;

/**
 * Created by motasemhamed on 8/14/16.
 */
public class WebViewActivity extends AppCompatActivity {


    boolean fromOpen = false;
    MyHorizontalLayout myHorizontalLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview);

//        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/open?id=17NVEwFrSuMDbgicGdegNd-M216w&usp=sharing"));
//        startActivity(myIntent);

//        WebView browser = (WebView) findViewById(R.id.webViewData);
//        browser.loadUrl("https://drive.google.com/open?id=17NVEwFrSuMDbgicGdegNd-M216w&usp=sharing");

//        fromOpen = true;
//        if (fromOpen)
//            finish();
        WebView webView = (WebView) findViewById(R.id.webViewData);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.setWebViewClient(new ourViewClient());
        try {
            webView.clearHistory();
            webView.loadUrl("https://drive.google.com/open?id=17NVEwFrSuMDbgicGdegNd-M216w&usp=sharing");
        } catch (Exception e) {
            e.printStackTrace();
        }

        RangeBar rb = (RangeBar) findViewById(R.id.rangebar);
        assert rb != null;
        rb.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {
                Log.i("min", leftPinValue);
                Log.i("max", rightPinValue);

            }
        });

        rb.setRangePinsByIndices(10, 50);


        myHorizontalLayout = (MyHorizontalLayout)findViewById(R.id.mygallery);

        String ExternalStorageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();

        String targetPath = ExternalStorageDirectoryPath + "/test/";

        Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG).show();
        File targetDirector = new File(targetPath);

        File[] files = targetDirector.listFiles();
        for (File file : files){
            //myHorizontalLayout.add(file.getAbsolutePath());
            Log.d("file path:", file.getAbsolutePath().toString());
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
