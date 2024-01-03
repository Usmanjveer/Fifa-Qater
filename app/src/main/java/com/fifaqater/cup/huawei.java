package com.fifaqater.cup;


import android.app.Application;

import com.huawei.hms.ads.HwAds;

public class huawei extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the Petal Ads SDK.
        HwAds.init(this);
    }
}