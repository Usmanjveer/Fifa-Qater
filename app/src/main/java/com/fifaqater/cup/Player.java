package com.fifaqater.cup;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.InterstitialAd;
import com.huawei.hms.ads.banner.BannerView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Player extends AppCompatActivity {
    private static final String TAG = "Player";
    private WebView webView;
    private String check;
    private String title;
    private ArrayList<String> iframeLinks;
    private ArrayList<String> iframeTitles;
    private InterstitialAd interstitialAd;
    private LinearLayout loadingLl;
    private Button lastClickedButton;
    private CustomWebChromeClient customWebChromeClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        HwAds.init(this);

        BannerView bannerView = findViewById(R.id.hw_banner_viewpl);
        bannerView.setAdId("testw6vs28auh3"); // Replace with your actual ad unit ID
        bannerView.setBannerAdSize(BannerAdSize.BANNER_SIZE_360_57);
        bannerView.setBannerRefresh(60);
        AdParam adParam = new AdParam.Builder().build();
        bannerView.loadAd(adParam);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdId("testb4znbuh3n2"); // Replace with your actual ad unit ID
        loadInterstitialAd();
        title = getIntent().getStringExtra("title");

        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new Player.MyBrowser());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(title);
        toolbarTitle.setTextColor(Color.WHITE);
        loadingLl = findViewById(R.id.llLoading);
        iframeTitles = getIntent().getStringArrayListExtra("iframeTitles");
        iframeLinks = getIntent().getStringArrayListExtra("iframeLinks");

        if (iframeLinks != null && !iframeLinks.isEmpty()) {
            createButtons();
        }

        webView.setWebChromeClient(customWebChromeClient = new CustomWebChromeClient());
        check = iframeLinks.get(0);
        Log.e(TAG, "check: " + check);
        webView.loadUrl(check);
    }

    private void createButtons() {
        LinearLayout buttonContainer = findViewById(R.id.buttonContainer);

        for (int i = 0; i < iframeLinks.size(); i++) {
            Button button = new Button(this);
            // Set button text to the corresponding title from iframeTitles
            button.setText(iframeTitles.get(i));

            // Styling the button
            button.setBackgroundResource(R.drawable.button_normal);

            // Set padding for the button (adjust values as needed)
            int paddingInDp = 8;  // specify padding in dp
            float scale = getResources().getDisplayMetrics().density;
            int paddingInPx = (int) (paddingInDp * scale + 0.5f);
            button.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx);

            final int finalI = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonClicked(button, iframeLinks.get(finalI));
                    loadingLl.setVisibility(View.VISIBLE);
                }
            });

            buttonContainer.addView(button);
        }

        // Set the background of the first button initially
        if (!iframeLinks.isEmpty()) {
            Button firstButton = (Button) buttonContainer.getChildAt(0);
            onButtonClicked(firstButton, iframeLinks.get(0));
        }
    }

    private void onButtonClicked(Button clickedButton, String iframeLink) {
        // Reset the color of the last clicked button
        if (lastClickedButton != null) {
            lastClickedButton.setBackgroundResource(R.drawable.button_normal);
        }

        // Load the iframe link
        webView.loadUrl(iframeLink);

        // Update the last clicked button
        lastClickedButton = clickedButton;

        // Set the background of the clicked button to button_clicked
        clickedButton.setBackgroundResource(R.drawable.button_clicked);
    }

    private void loadInterstitialAd() {
        AdParam adParam = new AdParam.Builder().build();
        interstitialAd.loadAd(adParam);
    }

    private void showInterstitialAd() {
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            interstitialAd.show(this);
        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.equals(check)) {
                view.loadUrl(url);
            }
            return true;
        }


        @Override
        public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
            try {
                webView.stopLoading();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (webView.canGoBack()) {
                webView.goBack();
            }

            webView.loadUrl("about:blank");
            Log.e("WebViewError", "Error loading URL: " + failingUrl + "\nDescription: " + description);
            showErrorMessage();
            super.onReceivedError(webView, errorCode, description, failingUrl);
        }

        private void showErrorMessage() {
            AlertDialog alertDialog = new AlertDialog.Builder(Player.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Check your internet connection and try again.");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Try Again", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    startActivity(getIntent());
                }
            });
            alertDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName('notranslate')[0].style.display='none'; " +
                    "document.getElementById('floatLayer1').style.display='none';})()");

            loadingLl.setVisibility(View.GONE);
            showInterstitialAd();
        }

        @SuppressWarnings("deprecation")
        @Override
        public WebResourceResponse shouldInterceptRequest(final WebView view, String url) {
            if (url.contains("/blaste.js")) {
                return getCssWebResourceResponseFromAsset();
            } else {
                return super.shouldInterceptRequest(view, url);
            }
        }

        /**
         * Return WebResourceResponse with CSS markup from an asset (e.g. "assets/style.css").
         */
        private WebResourceResponse getCssWebResourceResponseFromAsset() {
            try {
                return getUtf8EncodedCssWebResourceResponse(new ByteArrayInputStream("// script blocked".getBytes("UTF-8")));
            } catch (IOException e) {
                return null;
            }
        }

        private WebResourceResponse getUtf8EncodedCssWebResourceResponse(InputStream data) {
            return new WebResourceResponse("text/css", "UTF-8", data);
        }
    }

    class CustomWebChromeClient extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        private int mOriginalOrientation;

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }

            mCustomView = view;
            mOriginalOrientation = getResources().getConfiguration().orientation;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            FrameLayout decor = (FrameLayout) getWindow().getDecorView();
            decor.addView(mCustomView, new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER));
            mCustomViewCallback = callback;
            super.onShowCustomView(view, callback);
        }

        @Override
        public void onHideCustomView() {
            if (mCustomView == null) {
                return;
            }

            setRequestedOrientation(mOriginalOrientation);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            FrameLayout decor = (FrameLayout) getWindow().getDecorView();
            decor.removeView(mCustomView);
            mCustomView = null;
            mCustomViewCallback.onCustomViewHidden();
            mCustomViewCallback = null;

            super.onHideCustomView();
        }

        // Add this method to handle orientation change
        public void onOrientationChanged(int newOrientation) {
            // Handle orientation change here
            if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                // Landscape mode
                if (mCustomView != null) {
                    mCustomView.setLayoutParams(new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            Gravity.CENTER));
                }
            } else if (newOrientation == Configuration.ORIENTATION_PORTRAIT) {
                // Portrait mode
                if (mCustomView != null) {
                    mCustomView.setLayoutParams(new FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            Gravity.CENTER));
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Handle configuration changes
        if (customWebChromeClient != null) {
            customWebChromeClient.onOrientationChanged(newConfig.orientation);
        }
    }

    @Override
    public void onBackPressed() {
        webView.onPause();
        super.onPause();
        showInterstitialAd();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}
