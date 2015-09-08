package me.makram.libgen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import me.makram.libgen.data.Entry;
import me.makram.libgen.listeners.EndlessScrollListener;
import me.makram.libgen.listeners.ListEntryClickedListener;
import me.makram.libgen.tasks.GetPageTask;

public class ResultsActivity extends AppCompatActivity {

    public static final String ENTRY_LIST_KEY = "previousEntryList";
    ListView resultsListView;
    EntryAdapter entryAdapter;
    EndlessScrollListener endlessScrollListener;
    ListEntryClickedListener listEntryClickedListener;

    public static final Type LIST_ENTRY_TYPE =
            new TypeToken<List<Entry>>(){}.getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        Collection<Entry> entries = null;
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (!extras.getString(GetPageTask.ACTIVITY_SOURCE_KEY, "").equals(
                    MainActivity.class.toString()
            ) && BuildConfig.DEBUG) {
                throw new AssertionError("Bundle is from the wrong activity");
            }
            String json = extras.getString(GetPageTask.ENTRIES_ID);
            Gson gson = new Gson();
            entries = gson.fromJson(json, GetPageTask.COLLECTION_ENTRY_TYPE);
        } else {
            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
            String jsonList = preferences.getString(ENTRY_LIST_KEY, "");
            if (BuildConfig.DEBUG && jsonList.isEmpty()) {
                throw new AssertionError("Json list is empty");
            }
            entries = (new Gson()).fromJson(jsonList, GetPageTask.COLLECTION_ENTRY_TYPE);
        }

        entryAdapter = new EntryAdapter(this, R.layout.list_item, entries);
        endlessScrollListener = new EndlessScrollListener(this, entryAdapter, 5);
        listEntryClickedListener = new ListEntryClickedListener(entryAdapter, this);

        resultsListView = (ListView) findViewById(R.id.resultsListView);
        resultsListView.setAdapter(entryAdapter);
        resultsListView.setOnScrollListener(endlessScrollListener);
        resultsListView.setOnItemClickListener(listEntryClickedListener);
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

        switch (id)
        {
//            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
//                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ENTRY_LIST_KEY, (new Gson()).toJson(entryAdapter.getData(),
                GetPageTask.COLLECTION_ENTRY_TYPE));
        editor.apply();
    }
}
