package com.fifaqater.cup;

import android.app.Application;

import com.huawei.hms.ads.HwAds;

public class HuaveiAds extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the HUAWEI Ads SDK.
        HwAds.init(this);
    }

}
