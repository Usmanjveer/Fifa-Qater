/*
package com.fifaqater.cup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class BlogActivity extends AppCompatActivity {
    TextView title_txt, admin_txt, date_txt, tag_txt;
    ImageView imageView;
    String title, admin, date, tag, image, link;
    WebView webView;
    String blogDoc;
    Elements data;
    Document document;
    private InterstitialAd interstitialAd;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        title_txt = findViewById(R.id.title_txt_id);
        imageView = findViewById(R.id.imageView);
      //  admin_txt = findViewById(R.id.admin_txt_id);
    //    date_txt = findViewById(R.id.date_txt_id);
        tag_txt = findViewById(R.id.tag_txt_id);
        webView = findViewById(R.id.webView_id);

        title = getIntent().getStringExtra("title");
        admin = getIntent().getStringExtra("admin");
        date = getIntent().getStringExtra("date");
        image = getIntent().getStringExtra("image");
        link = getIntent().getStringExtra("postlink");

        title_txt.setText(title);
      //  admin_txt.setText(admin);
//        date_txt.setText(date);
        Picasso.get().load(image).into(imageView);

        Content content = new Content();
        content.execute();
        interstitialAd = new InterstitialAd(this);
        // "testb4znbuh3n2" is a dedicated test ad unit ID. Before releasing your app, replace the test ad unit ID with the formal one.
        interstitialAd.setAdId("l397oskyxd");
        loadInterstitialAd();
    }
    private void loadInterstitialAd() {
        // Load an interstitial ad.
        AdParam adParam = new AdParam.Builder().build();
        interstitialAd.loadAd(adParam);
    }
    private void showInterstitialAd() {
        // Display the ad.
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            interstitialAd.show(this);
        }
    }

    private class Content extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            tag_txt.setText(tag);
            webView.setWebChromeClient(new WebChromeClient());
            webView.loadDataWithBaseURL(null,blogDoc,"text/html","utf-8",null);
            webView.requestFocus();
            progressDialog.hide();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                document = Jsoup.connect(link).get();
                data = document.select("section.article__body");

                int i = 0;
                blogDoc = data.select("div.ciam-article-f365").eq(i).html();


            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.hide();
            }
            try {
                document = Jsoup.connect(link).get();
                data = document.select("header.article__header");

                int i = 0;
                tag = data.select("header.article__header").eq(i).select("p").text();

                Log.e("TAG", "doInBackground: " + tag);

            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.hide();
            }


            return null;
        }


    }
    @Override
    public void onBackPressed() {
        showInterstitialAd();
        super.onBackPressed();
    }
}*/
