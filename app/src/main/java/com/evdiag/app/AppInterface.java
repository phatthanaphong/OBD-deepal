package com.evdiag.app;

import android.app.Activity;
import android.webkit.JavascriptInterface;

public class AppInterface {
    protected final Activity activity;

    public AppInterface(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public void exit() {
        activity.runOnUiThread(() -> activity.finish());
    }

    @JavascriptInterface
    public String getVersion() { return "1.0"; }

    @JavascriptInterface
    public void reportError(String msg) {
        // override in MainActivity debug build
    }
}
