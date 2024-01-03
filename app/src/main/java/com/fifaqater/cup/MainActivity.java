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
import com.fifaqater.cup.adapter.ParseItemAdapter;
import com.fifaqater.cup.model.ParseItemModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.InterstitialAd;
import com.huawei.hms.ads.banner.BannerView;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNav;
    private RecyclerView recyclerView;
    private TextView tvNoItems;
    private LinearLayout loadingLl;
    private int selectedButtonIndex = 0;
    private ParseItemAdapter parseItemAdapter;
    private final List<ParseItemModel> parseItemModelList = new ArrayList<>();
    private BannerView bannerView1;
    private BannerView bannerView2;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingLl = findViewById(R.id.llLoading2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        tvNoItems = findViewById(R.id.tvNoItems);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Live Events");
        toolbarTitle.setTextColor(Color.WHITE);

        overridePendingTransition(0, 0);

        HwAds.init(this);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(() -> recreate());

        bannerView1 = findViewById(R.id.hw_banner_view1);
        bannerView2 = findViewById(R.id.hw_banner_view);
        loadBannerAd(bannerView1, "testw6vs28auh3");
        loadBannerAd(bannerView2, "testw6vs28auh3");

        recyclerView = findViewById(R.id.recyclerView_main);
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
        mBottomNav.setSelectedItemId(R.id.nav_home);

        mBottomNav.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    return true;
                case R.id.nav_video:
                    startActivity(new Intent(getApplicationContext(), Video.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.nav_upcoming:
                    startActivity(new Intent(getApplicationContext(), Upcoming.class));
                    overridePendingTransition(0, 0);
                    return true;
                default:
                    return false;
            }
        });
    }

    private void loadBannerAd(BannerView bannerView, String adUnitId) {
        bannerView.setAdId(adUnitId);
        bannerView.setBannerAdSize(BannerAdSize.BANNER_SIZE_360_57);
        AdParam adParam = new AdParam.Builder().build();
        bannerView.loadAd(adParam);
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

            parseItemAdapter = new ParseItemAdapter(parseItemModelList, MainActivity.this);
            recyclerView.setAdapter(parseItemAdapter);
            parseItemAdapter.notifyDataSetChanged();
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
                        runOnUiThread(() -> new AlertDialog.Builder(MainActivity.this)
                                .setMessage("No Internet Connection")
                                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show());
                        return null;
                    }
                }

                String link = "https://hq.livesoccer.sx/?type=football";
                Connection.Response response = Jsoup.connect(link)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.102 Safari/537.36")
                        .header("Referer", "https://my.livesoccer.sx/")
                        .header("Accept-Language", "en-US,en;q=0.9")
                        .execute();
                org.jsoup.nodes.Document document = response.parse();

                org.jsoup.select.Elements articleHTML = document.select("section")
                        .select("ul.item-list.striped")
                        .select("li.item.itemhov, li.bahamas");

                Element itemhov = null;
                Element bahamas = null;
                for (Element element : articleHTML) {
                    if (element.hasClass("item itemhov")) {
                        itemhov = element;
                    } else if (element.hasClass("bahamas")) {
                        bahamas = element;
                    }

                    if (itemhov != null && bahamas != null) {
                        String imageA = itemhov.select("div.matchname table tbody tr td:eq(0) img").attr("src");
                        if (!imageA.startsWith("//") && !imageA.startsWith("http")) {
                            imageA = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRIbmZsUVUAo1Yv_oLUPPH_YlOIHjWo6G6Xpk-6VuSghBS5BWbWDSG4K3onPN0bRpnBxjQ&usqp=CAU";
                        } else if (imageA.startsWith("//")) {
                            imageA = "https://my.livesoccer.sx" + imageA;
                        }

                        String imageB = itemhov.select("div.matchname table tbody tr td:eq(2) img").attr("src");
                        if (!imageB.startsWith("//") && !imageB.startsWith("http")) {
                            imageB = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRIbmZsUVUAo1Yv_oLUPPH_YlOIHjWo6G6Xpk-6VuSghBS5BWbWDSG4K3onPN0bRpnBxjQ&usqp=CAU";
                        } else if (imageB.startsWith("//")) {
                            imageB = "https://my.livesoccer.sx" + imageB;
                        }

                        String teama = itemhov.select("div.matchname table tbody tr td:eq(0)").text();
                        String teamb = itemhov.select("div.matchname table tbody tr td:eq(2)").text();
                        String league = itemhov.select("div.col-xl-12 a span").text();
                        String time = itemhov.select("div.item-col.pull-left.item-col-time span").text();
                        String gif2 = itemhov.select("div.item-col.fixed.pull-left.item-col-stream a").text();
                        String gif = "";
                        if (gif2.startsWith("Live")) {
                            gif = "https://eobjx2.com/GroupGreets/blog/wp-content/uploads/2016/08/live.gif";
                        }

                        List<String> postLinkList = new ArrayList<>();
                        List<String> titleLinkList = new ArrayList<>();
                        org.jsoup.select.Elements tooltipItems = bahamas.select("span.tooltip-item a");
                        for (Element tooltipItem : tooltipItems) {
                            String hrefLink = tooltipItem.attr("href");
                            postLinkList.add(hrefLink);
                        }

                        org.jsoup.select.Elements titleLinks = bahamas.select("span.tooltip-item");
                        for (Element titleLink : titleLinks) {
                            String titleLinkText = titleLink.select("a").text();
                            titleLinkList.add(titleLinkText);
                        }

                        if (!teama.isEmpty() && !teamb.isEmpty()) {
                            parseItemModelList.add(new ParseItemModel(imageA, imageB, teama, teamb, league, time, postLinkList, titleLinkList, gif));
                        }

                        itemhov = null;
                        bahamas = null;
                    }
                }
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
