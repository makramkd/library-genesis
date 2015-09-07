package me.makram.libgen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.Collection;

import me.makram.libgen.data.Entry;

public class ResultsActivity extends AppCompatActivity {

    ListView resultsListView;
    EntryAdapter entryAdapter;
    EndlessScrollListener endlessScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String json = extras.getString(GetPageTask.ENTRIES_ID);
        Gson gson = new Gson();
        Collection<Entry> entries = gson.fromJson(json, GetPageTask.COLLECTION_ENTRY_TYPE);

        entryAdapter = new EntryAdapter(this, R.layout.list_item, entries);
        endlessScrollListener = new EndlessScrollListener(this, entryAdapter, 5);

        resultsListView = (ListView) findViewById(R.id.resultsListView);
        resultsListView.setAdapter(entryAdapter);
        resultsListView.setOnScrollListener(endlessScrollListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
        return true;
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
