package com.silladus.stock;

import android.app.Application;

/**
 *
 * @author :        silladus
 * @date :          2019/12/5 18:02
 * @desc :
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(new SimpleActivityLifecycleCallbacks());
    }
}
