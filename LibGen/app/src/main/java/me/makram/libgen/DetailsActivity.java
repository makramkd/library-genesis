package me.makram.libgen;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import me.makram.libgen.data.Entry;

public class DetailsActivity extends AppCompatActivity {

    public static final String ENTRY_DOWNLOADID_KEY = "entryDownloadId";
    /**
     * The download link of this entry.
     */
    private String downloadLink;

    /**
     * The views associated with this activity.
     */
    private Button downloadButton;
    private TextView entryTitleTextView;
    private TextView authorTextView;
    private TextView extensionTextView;
    private TextView sizeTextView;
    private TextView publisherTextView;
    private TextView yearTextView;
    private TextView languageTextView;

    /**
     * Entry object that holds the details.
     */
    private Entry entry;

    DownloadBroadcastReceiver downloadReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Gson gson = new Gson();
        entry = gson.fromJson(extras.getString(GetEntryDetailsTask.ENTRY_KEY), Entry.class);

        downloadLink = extras.getString(GetEntryDetailsTask.DOWNLOAD_LINK_KEY);

        downloadButton = (Button) findViewById(R.id.downloadButton);
        entryTitleTextView = (TextView) findViewById(R.id.entryTitleTextView);
        authorTextView = (TextView) findViewById(R.id.authorsTextView);
        extensionTextView = (TextView) findViewById(R.id.extensionTextView);
        sizeTextView = (TextView) findViewById(R.id.sizeTextView);
        publisherTextView = (TextView) findViewById(R.id.publisherTextView);
        yearTextView = (TextView) findViewById(R.id.yearTextView);
        languageTextView = (TextView) findViewById(R.id.languageTextView);

        downloadReceiver = new DownloadBroadcastReceiver(this);

        registerReceiver(downloadReceiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        fillInData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    public void fillInData() {
        entryTitleTextView.setText(entry.title);
        authorTextView.setText(entry.author);
        extensionTextView.setText(entry.extension);
        sizeTextView.setText(entry.size);
        publisherTextView.setText(entry.publisher);
        yearTextView.setText(entry.year);
        languageTextView.setText(entry.language);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void downloadButtonClicked(View view) {
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadLink));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle(entry.title);
        request.setDescription("Please wait...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.allowScanningByMediaScanner();

        long id = downloadManager.enqueue(request);
        SharedPreferences preferences = getSharedPreferences(DetailsActivity.class.getName(),
                MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(ENTRY_DOWNLOADID_KEY, id);
        editor.apply();
    }
}
