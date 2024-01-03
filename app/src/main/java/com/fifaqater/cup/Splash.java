package com.fifaqater.cup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Splash extends AppCompatActivity {
    private ProgressBar mProgress;
    private AlertDialog dialog;
    String apk, app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mProgress = findViewById(R.id.progress);

        // Check for internet connectivity
        if (isNetworkAvailable()) {
            // Create an instance of DoWorkTask and execute it
            new DoWorkTask().execute();
        } else {
            // Show a toast message indicating no internet connection
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class DoWorkTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            doWork();
            checkForUpdate();
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            mProgress.setProgress(progress);
        }

        @Override
        protected void onPostExecute(Void result) {
            // This method will be called after the background task finishes
            // Start the app after the update check is done
            // startApp();
        }

        private void doWork() {
            for (int progress = 0; progress < 1; progress += 1) {
                try {
                    Thread.sleep(2);
                    publishProgress(progress);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void checkForUpdate() {
            try {
                Document document = Jsoup.connect("https://docs.google.com/spreadsheets/d/e/2PACX-1vSLk6i7J_eU91sOwsLK3JKZp_CpEkx6UA0tlLU9pjQWdIWeewPRHfE4NNIkwB5cnzfHEMTAUndQ-10G/pubhtml").get();
                Elements data = document.select("tbody").select("tr");

                String latestVersionName = data.select("tr").eq(0).select("td").eq(1).text();
                String latestVersionCode = data.select("tr").eq(1).select("td").eq(1).text();
                apk = data.select("tr").eq(2).select("td").select("a").attr("href");
                app = data.select("tr").eq(3).select("td").select("a").attr("href");
                Log.e("latest_version_name", latestVersionName);
                Log.e("latest_version_code", latestVersionCode);
                Log.e("apk", apk);
                PackageManager packageManager = getPackageManager();
                String packageName = getPackageName();

                PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
                String installedVersionName = packageInfo.versionName;
                int installedVersionCode = packageInfo.versionCode;

                Log.e("installed_version_code", String.valueOf(installedVersionCode));
                Log.e("installed_version_name", installedVersionName);
                int versionComparison = compareVersions(latestVersionCode, String.valueOf(installedVersionCode));
                if (!(versionComparison == 0)) {
                    // Newer version is available. Show the update dialog on the main UI thread.
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showUpdateDialog();
                        }
                    });
                } else if (versionComparison == 0) {
                    // Versions are equal. Proceed to start the app as usual.
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startApp();
                        }
                    });
                }

            } catch (Exception e) {
                Log.e("Update Check", "Exception: " + e.getMessage());
            }
        }
    }

    private int compareVersions(String version1, String version2) {
        String[] version1Parts = version1.split("\\.");
        String[] version2Parts = version2.split("\\.");

        int length = Math.max(version1Parts.length, version2Parts.length);
        for (int i = 0; i < length; i++) {
            int v1 = (i < version1Parts.length) ? Integer.parseInt(version1Parts[i]) : 0;
            int v2 = (i < version2Parts.length) ? Integer.parseInt(version2Parts[i]) : 0;

            if (v1 < v2) {
                return -1;
            } else if (v1 > v2) {
                return 1;
            }
        }

        return 0;
    }

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.update_dialog_layout, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        Button btnUpdateApp = dialogView.findViewById(R.id.UpdateApp);
        Button btnNextTime = dialogView.findViewById(R.id.cancel);
        Button btnapk = dialogView.findViewById(R.id.apk);
        btnUpdateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(app)));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(app)));
                }
            }
        });

        btnNextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss the dialog and proceed to start the app as usual.
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                startApp();
            }
        });

        btnapk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle "View Details" button click here.
                // For example, you can show a toast message with the new version details.
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(apk)));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(apk)));
                }
            }
        });

        dialog = builder.create();
        dialog.show();
    }

    private void startApp() {
        Intent intent = new Intent(Splash.this, MainActivity.class);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();  // Finish the Splash activity to prevent going back to it
    }

    @Override
    public void onBackPressed() {
        // Exit the app when the back button is pressed
        finishAffinity();
    }
}
