package me.spectrallabs.libgen.listeners;

import android.view.View;
import android.widget.AdapterView;

import me.spectrallabs.libgen.EntryAdapter;
import me.spectrallabs.libgen.activities.ResultsActivity;
import me.spectrallabs.libgen.data.Entry;
import me.spectrallabs.libgen.tasks.GetEntryDetailsTask;

/**
 * Created by admin on 9/7/15.
 */
public class ListEntryClickedListener implements AdapterView.OnItemClickListener {

    private EntryAdapter entryAdapter;
    private ResultsActivity resultsActivity;

    public ListEntryClickedListener(EntryAdapter adapter, ResultsActivity activity) {
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
