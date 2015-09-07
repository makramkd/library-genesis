package me.makram.libgen;

import android.view.View;
import android.widget.AdapterView;

import me.makram.libgen.data.Entry;

/**
 * Created by admin on 9/7/15.
 */
public class ListEntryClickedListener implements AdapterView.OnItemClickListener {

    private EntryAdapter entryAdapter;
    private ResultsActivity resultsActivity;

    ListEntryClickedListener(EntryAdapter adapter, ResultsActivity activity) {
        entryAdapter = adapter;
        resultsActivity = activity;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Entry entry = (Entry) entryAdapter.getItem(position);

        // async task that will load the page and create the intent
        new GetEntryDetailsTask(resultsActivity).execute(entry);
    }
}
