package com.kbibazaar.kbi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private WebView myWebView;
    private WebSettings webSettings;
    private ProgressBar progress;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED , WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        progress = (ProgressBar)findViewById(R.id.progress);

        onNewIntent(getIntent());

        myWebView = (WebView)findViewById(R.id.webView);

        if (myWebView != null) {
            webSettings = myWebView.getSettings();
        }

        webSettings.setJavaScriptEnabled(true);

        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        //webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        myWebView.setLayerType(View.LAYER_TYPE_HARDWARE , null);
       // webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);


   /*     myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.getSettings().setSupportZoom(true);
        myWebView.getSettings().setDisplayZoomControls(false);
        myWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        myWebView.setLayerType(View.LAYER_TYPE_HARDWARE , null);
*/




        //override the web client to open all links in the same webview
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.setWebChromeClient(new MyWebChromeClient());


        myWebView.addJavascriptInterface(new JavaScriptInterface(), "Android");

        //load the home page URL
        myWebView.loadUrl("http://kbibazaar.com/tbx/");

    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            myWebView.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progress.setVisibility(View.GONE);
        }
    }

    private class MyWebChromeClient extends WebChromeClient {


        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progress.setProgress(newProgress);

            if (newProgress<100)
            {
                progress.setVisibility(View.VISIBLE);


            }
            if (newProgress == 100)
            {
                progress.setVisibility(View.GONE);
            }


        }

        //display alert message in Web View
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.d("asdasdasd", message);
            new AlertDialog.Builder(view.getContext())
                    .setMessage(message).setCancelable(true).show();
            result.confirm();
            return true;
        }

    }

    private class JavaScriptInterface {

        // Instantiate the interface and set the context
        JavaScriptInterface() {
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

        if (myWebView.canGoBack())
        {
            myWebView.goBack();
        }
        else
        {
            super.onBackPressed();
        }

    }
}
