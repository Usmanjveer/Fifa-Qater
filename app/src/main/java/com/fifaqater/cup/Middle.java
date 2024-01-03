package com.fifaqater.cup;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.fifaqater.cup.model.mdel1;
import org.jsoup.Jsoup;

public class Middle extends AppCompatActivity {
    private final mdel1 link = new mdel1();
    private LinearLayout loadingLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        String title2 = getIntent().getStringExtra("title");
        Log.e(ContentValues.TAG, "Get Data: " + title2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(title2);
        toolbarTitle.setTextColor(Color.WHITE);

        loadingLl = findViewById(R.id.llLoading1);
        String sec = getIntent().getStringExtra("postLink");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    org.jsoup.nodes.Document document = Jsoup.connect(sec).get();
                    org.jsoup.select.Elements data = document.select("article");
                    Log.e("Player", data.toString());

                    String iframe = data.select("iframe").attr("src");
                    String url2 = "";

                    if (!iframe.startsWith("http") || !iframe.startsWith("//")) {
                        url2 = data.select("iframe").attr("data-src");
                    } else {
                        url2 = iframe;
                    }

                    if (url2.startsWith("//")) {
                        url2 = "https:" + url2;
                    } else {
                        url2 = "https://istorm.live/flash1";
                    }
                    Log.e("Middle_url2", url2);
                    link.setUrl(url2);
                    Log.e("url2", url2.toString());
                } catch (Exception ex) {
                    // Handle exceptions
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (link.getUrl() != null && link.getUrl().startsWith("http")) {
                            Intent intent = new Intent(Middle.this, Middleweb.class);
                            intent.putExtra("postlink", link.getUrl());
                            intent.putExtra("title", title2);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(0, 0);
                        } else {
                            Intent intent = new Intent(Middle.this, MainActivity.class);
                            Toast.makeText(Middle.this, "Link Will Be Live On Match Time'", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                            overridePendingTransition(0, 0);
                        }
                    }
                });
            }
        }).start();
    }
}
