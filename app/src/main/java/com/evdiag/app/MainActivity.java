package com.evdiag.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.ByteArrayInputStream;

public class MainActivity extends Activity {

    private WebView webView;
    private StringBuilder errorLog = new StringBuilder();

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webview);

        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);
        s.setBuiltInZoomControls(false);
        s.setDisplayZoomControls(false);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);
        s.setCacheMode(WebSettings.LOAD_NO_CACHE);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        // จับ console.error และ JS errors แสดงบนหน้าจอ
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage msg) {
                if (msg.messageLevel() == ConsoleMessage.MessageLevel.ERROR) {
                    errorLog.append("JS ERROR: ").append(msg.message())
                            .append(" (line ").append(msg.lineNumber()).append(")\n");
                }
                return true;
            }
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                result.confirm();
                return true;
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest req) {
                String url = req.getUrl().toString();
                if (!url.startsWith("file://") && !url.startsWith("about:")) {
                    // Block external URLs — return empty response
                    return new WebResourceResponse("text/plain", "utf-8",
                            new ByteArrayInputStream("".getBytes()));
                }
                return super.shouldInterceptRequest(view, req);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError err) {
                String msg = "onReceivedError: " + err.getDescription()
                        + " | url=" + req.getUrl().toString()
                        + " | code=" + err.getErrorCode();
                errorLog.append(msg).append("\n");
                showError(msg);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // หลังโหลดเสร็จ inject JS เพื่อจับ runtime errors
                view.evaluateJavascript(
                    "window.onerror = function(msg,src,line,col,err) {" +
                    "  AndroidApp.reportError('JS: '+msg+' @ line '+line);" +
                    "  return true;" +
                    "};", null);
            }
        });

        webView.addJavascriptInterface(new AppInterface(this) {
            @Override
            public void reportError(String msg) {
                runOnUiThread(() -> showError(msg));
            }
        }, "AndroidApp");

        webView.loadUrl("file:///android_asset/ev_diagnostic_web.html");
    }

    private void showError(final String msg) {
        runOnUiThread(() -> {
            try {
                new AlertDialog.Builder(MainActivity.this)
                    .setTitle("DEBUG ERROR")
                    .setMessage(msg + "\n\n---LOG---\n" + errorLog.toString())
                    .setPositiveButton("OK", null)
                    .show();
            } catch (Exception ignored) {}
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) webView.goBack();
            else finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override protected void onPause() { super.onPause(); webView.onPause(); }
    @Override protected void onResume() { super.onResume(); webView.onResume(); }
    @Override protected void onDestroy() { webView.destroy(); super.onDestroy(); }
}
