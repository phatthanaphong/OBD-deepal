package com.evdiag.app;

import android.app.Activity;
import android.webkit.JavascriptInterface;

/**
 * JavaScript bridge — lets the HTML call AndroidApp.exit() to close the activity.
 * The ✕ button in the HTML already calls window.close() / history.back(),
 * but on Android WebView those don't work — so we wire it here.
 */
public class AppInterface {
    private final Activity activity;

    public AppInterface(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public void exit() {
        activity.runOnUiThread(() -> activity.finish());
    }

    @JavascriptInterface
    public String getVersion() {
        return "1.0";
    }
}
