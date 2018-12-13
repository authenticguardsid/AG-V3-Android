package com.agreader.screen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.agreader.R;

public class TermEndService extends AppCompatActivity {
    public String URL_NEWS = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_end_service);
        URL_NEWS = getIntent().getStringExtra("EXTRA_SESSION_ID");
        WebView webView = (WebView) findViewById(R.id.NewsWebview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.clearCache(true);
        webView.loadUrl(URL_NEWS);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progressProfile);
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        }, 3000);
    }
}
