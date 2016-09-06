package com.route876.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.route876.R;

/**
 * Created by Howard on 1/15/2016.
 */
public class WebViewActivity extends AppCompatActivity {

    private String newsUrl;
    private String newsTitle;

    private TextView textViewTitle;
    private TextView textViewLink;
    private ProgressBar progressBar;

    private WebView webview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Bundle bundle = this.getIntent().getExtras();
        newsUrl = bundle.getString("newsUrl");
        newsTitle = bundle.getString("newsTitle");

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setMax(100);

        LinearLayout titleBarButton = (LinearLayout) findViewById(R.id.title_bar_button);
        titleBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayout shareButton = (LinearLayout) findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, newsUrl);
                startActivity(Intent.createChooser(intent, "Share News Article"));
            }
        });

        textViewTitle = (TextView) findViewById(R.id.title_bar_title);
        textViewLink = (TextView) findViewById(R.id.title_bar_link);

        webview = (WebView) findViewById(R.id.web_view);
        webview.setWebViewClient(new RssWebClient());
        webview.loadUrl(newsUrl);
        WebViewActivity.this.setProgress(0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setValue(int progress) {
        progressBar.setProgress(progress);
    }

    private class RssWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setProgress(100);
            textViewTitle.setText(newsTitle);
            textViewLink.setText(newsUrl);
            progressBar.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }
    }
}
