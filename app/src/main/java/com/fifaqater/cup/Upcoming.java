package com.fifaqater.cup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.fifaqater.cup.adapter.ParseItemAdapter1;
import com.fifaqater.cup.model.ParseItemModel1;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.InterstitialAd;
import com.huawei.hms.ads.banner.BannerView;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import java.util.ArrayList;
import java.util.List;

public class Upcoming extends AppCompatActivity {
    private static final String TAG = "";
    private BottomNavigationView mBottomNav;
    private RecyclerView recyclerView;
    private TextView tvNoItems;
    private LinearLayout loadingLl;
    private ParseItemAdapter1 parseItemAdapter1;
    private final List<ParseItemModel1> parseItemModelList = new ArrayList<>();
    private BannerView bannerView;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        loadingLl = findViewById(R.id.llLoading1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        tvNoItems = findViewById(R.id.tvNoItems);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Videos");
        toolbarTitle.setTextColor(Color.WHITE);

        overridePendingTransition(0, 0);

        HwAds.init(this);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(() -> recreate());

        bannerView = findViewById(R.id.hw_banner_viewc);
        bannerView.setAdId("testw6vs28auh3");
        bannerView.setBannerAdSize(BannerAdSize.BANNER_SIZE_360_57);
        AdParam adParam = new AdParam.Builder().build();
        bannerView.loadAd(adParam);

        recyclerView = findViewById(R.id.recyclerView_up);
        tvNoItems = findViewById(R.id.tvNoItems);

        Content content = new Content();
        content.execute();

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdId("testb4znbuh3n2");
        loadInterstitialAd();
        // Find the Rate and Share ImageViews
        ImageView rateImageView = findViewById(R.id.rate);
        ImageView shareImageView = findViewById(R.id.share);

        // Set click listener for the Rate button
        rateImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://appgallery.huawei.com/app/C106860949")));
                } catch (android.content.ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://appgallery.huawei.com/app/C106860949")));
                }

            }
        });

        // Set click listener for the Share button
        shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Football Tv");
                intent.putExtra(Intent.EXTRA_TEXT,
                        "Watch All football mactches, Live Streaming and highlights only on Live football tv Click here to Download https://appgallery.huawei.com/app/C106860949");
                startActivity(Intent.createChooser(intent, "Share via"));

            }
        });
        mBottomNav = findViewById(R.id.bottom_nav);

        mBottomNav.setSelectedItemId(R.id.nav_upcoming);

        mBottomNav.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.nav_upcoming:
                    return true;
                case R.id.nav_home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    overridePendingTransition(0, 0);
                    return true;
                case R.id.nav_video:
                    startActivity(new Intent(getApplicationContext(), Video.class));

                    overridePendingTransition(0, 0);
                    return true;
                default:
                    return false;
            }
        });
    }

    private void loadInterstitialAd() {
        AdParam adParam = new AdParam.Builder().build();
        interstitialAd.loadAd(adParam);
    }

    private void showInterstitialAd() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show(this);
        }
    }

    private final AdListener adListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            showInterstitialAd();
        }

        @Override
        public void onAdFailed(int errorCode) {
            loadInterstitialAd();
            showInterstitialAd();
        }

        @Override
        public void onAdClosed() {
            // Called when an ad is closed.
        }

        @Override
        public void onAdClicked() {
            // Called when an ad is clicked.
        }

        @Override
        public void onAdLeave() {
            // Called when an ad leaves an app.
        }

        @Override
        public void onAdOpened() {
            // Called when an ad is opened.
        }

        @Override
        public void onAdImpression() {
            // Called when an ad impression occurs.
        }
    };



    private class Content extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            recyclerView.setVisibility(View.VISIBLE);

            parseItemAdapter1 = new ParseItemAdapter1(parseItemModelList, Upcoming.this);
            recyclerView.setAdapter(parseItemAdapter1);
            parseItemAdapter1.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);

            if (parseItemModelList.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                tvNoItems.setVisibility(View.VISIBLE);
            }
            showInterstitialAd();
            loadingLl.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... p0) {
            try {
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager != null) {
                    if (connectivityManager.getActiveNetworkInfo() == null ||
                            !connectivityManager.getActiveNetworkInfo().isConnected()) {
                        runOnUiThread(() -> new AlertDialog.Builder(Upcoming.this)
                                .setMessage("No Internet Connection")
                                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show());
                        return null;
                    }
                }

                String link = "https://www.fasthighlights.net/tag/top-goals-saves/";
                Connection.Response response = Jsoup.connect(link)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.102 Safari/537.36")
                        .header("Referer", "https://www.fasthighlights.net/")
                        .header("Accept-Language", "en-US,en;q=0.9")
                        .execute();
                org.jsoup.nodes.Document document = response.parse();

                org.jsoup.select.Elements articleHTML = document.getElementById("main").select("article");
                Log.e(TAG, "articleHTML: " + articleHTML);
                articleHTML.forEach(item -> {
                    String title = item.select("div.read-title").select("h4").select("a").text();
                    Log.e(TAG, "title: " + title);
                    String imageLink = "";
                    String imageLink2 = item.select("div.read-img.pos-rel.read-bg-img").select("img").attr("src");
                    if (imageLink2.startsWith("http")) {
                        imageLink = imageLink2;
                    } else {
                        imageLink = item.select("div.read-img.pos-rel.read-bg-img").select("img").attr("data-src");
                    }

                    Log.e(TAG, "imageLink: " + imageLink);
                    String postLink = item.select("div.read-img.pos-rel.read-bg-img").select("a").attr("href");
                    Log.e(TAG, "postLink: " + postLink);

                    if (!title.isEmpty()) {
                        parseItemModelList.add(new ParseItemModel1(title, imageLink, postLink));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    @Override
    public void onBackPressed() {
        if (interstitialAd.isLoaded()) {
            showInterstitialAd();
            interstitialAd.show();
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            overridePendingTransition(0, 0);
            startActivity(intent);
        }
    }
}
