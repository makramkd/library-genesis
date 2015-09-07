package me.makram.libgen;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import me.makram.libgen.data.Entry;

public class DetailsActivity extends AppCompatActivity {

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
    private TextView descriptionTextView;

    /**
     * Entry object that holds the details.
     */
    private Entry entry;

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
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);

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
}
