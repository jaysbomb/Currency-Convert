package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class historyMainActivity extends AppCompatActivity {
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_main);

        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.loadUrl("http://www.xe.com/zh-HK/currencycharts/?from=HKD&to=USD&view=1D");

        mWebView.setWebViewClient(new WebViewClient());
    }

    @Override
    public boolean
    onKeyDown(int keyCode, KeyEvent event) {
        if
                ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
