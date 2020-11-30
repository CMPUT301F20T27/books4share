package com.example.books4share;

import android.app.Application;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * This class is used to initialize the QR scan tools that we import from ZXingLibrary
 * ZXingLibrary is an open-source, 1D/2D barcode image processing library implemented in Java
 * https://github.com/yipianfengye/android-zxingLibrary
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ZXingLibrary.initDisplayOpinion(this);
    }
}
