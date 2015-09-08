package me.makram.libgen.listeners;

import android.content.Context;
import android.widget.AbsListView;

import me.makram.libgen.EntryAdapter;
import me.makram.libgen.tasks.LoadMoreEntriesTask;

/**
 * Created by admin on 9/7/15.
 */
public class EndlessScrollListener implements AbsListView.OnScrollListener {

    private int visibleThreshold;
    private int currentPage;
    private int previousTotal;
    private boolean loading;
    private Context context;
    private EntryAdapter entryAdapter;

    public EndlessScrollListener(Context context, EntryAdapter adapter, int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
        currentPage = 1;
        previousTotal = 0;
        loading = true;
        this.context = context;
        this.entryAdapter = adapter;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
                currentPage++;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            // use async task to load more entries
            new LoadMoreEntriesTask(context, entryAdapter).execute();
            loading = true;
        }
    }
}
