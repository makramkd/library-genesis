package me.spectrallabs.libgen.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.spectrallabs.libgen.BuildConfig;
import me.spectrallabs.libgen.EntryAdapter;
import me.spectrallabs.libgen.LibGen;
import me.spectrallabs.libgen.activities.MainActivity;
import me.spectrallabs.libgen.data.Entry;

/**
 * Created by admin on 9/7/15.
 */
public class LoadMoreEntriesTask extends AsyncTask<Void, Void, Collection<Entry>> {

    private Context context;
    private EntryAdapter entryAdapter;

    public LoadMoreEntriesTask(Context context, EntryAdapter adapter) {
        this.context = context;
        this.entryAdapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (BuildConfig.DEBUG) {
            Toast.makeText(context, "Loading more entries", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected Collection<Entry> doInBackground(Void... params) {
        List<Entry> entries = new ArrayList<>();

        try {
            LibGen application = (LibGen) context.getApplicationContext();
            OkHttpClient client = application.getClient();
            application.incrementAndGetPageNumber();
            Request request = new Request.Builder()
                    .url(String.format(MainActivity.SEARCH_URL,
                            application.getQuery(), application.getPageNumber()))
                    .build();

            Response response = client.newCall(request).execute();
            Document document = Jsoup.parse(response.body().string());

            Elements table = document.getElementsByClass("c");
            Element child = table.first();
            Elements children = child.children();
            Element first = children.first();
            Elements theChildren = first.children();

            for (int i = 1; i < theChildren.size(); ++i) {
                Element tr = theChildren.get(i);
                Elements childrenOfTr = tr.children();

                entries.add(GetPageTask.getEntryObject(childrenOfTr));
            }
        } catch (Exception e) {
            Log.d("LMET", "Exception in LoadMoreEntriesTask: " + e.getMessage());
        }

        // don't unnecessarily add entries
        if (entries.size() > 0) {
            entryAdapter.addEntries(entries);
        }

        return entries;
    }

    @Override
    protected void onPostExecute(Collection<Entry> entries) {
        super.onPostExecute(entries);

        if (entries.size() > 0) {
            Toast.makeText(context, "Loaded " + entries.size() + " more entries."
                    , Toast.LENGTH_SHORT).show();
            entryAdapter.notifyDataSetChanged();
        }
    }
}
