package me.makram.libgen;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.makram.libgen.data.Entry;

/**
 * Created by admin on 9/6/15.
 */
class GetPageTask extends AsyncTask<Request, Void, Collection<Entry>> {

    public static final String ENTRIES_ID = "entries";

    private MainActivity mainActivity;

    boolean succeeded = false;

    public static final Type COLLECTION_ENTRY_TYPE = new TypeToken<Collection<Entry>>(){}.getType();

    public GetPageTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Toast.makeText(mainActivity.getApplicationContext(), "Downloading page", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Collection<Entry> doInBackground(Request... params) {
        List<Entry> entries = new ArrayList<>();

        try {
            OkHttpClient client = ((LibGen)mainActivity.getApplication()).getClient();
            Response response = client.newCall(params[0]).execute();
            Document document = Jsoup.parse(response.body().string());

            Elements table = document.getElementsByClass("c");
            Element child = table.first();
            Elements children = child.children();
            Element first = children.first();
            Elements theChildren = first.children();

            for (int i = 1; i < theChildren.size(); ++i) {
                Element tr = theChildren.get(i);
                Elements childrenOfTr = tr.children();

                entries.add(getEntryObject(childrenOfTr));
            }

            succeeded = true;
        } catch (Exception e) {
            Log.d("MA", "Exception in AsyncTask#GetPage: " + e.getMessage());
        }

        return entries;
    }

    /**
     * Create and return an Entry POJO from a table row in the libgen resulting table.
     * @param childrenOfTr the children of the row in the table
     * @return an Entry object with the data from the data in the given row.
     */
    public static Entry getEntryObject(Elements childrenOfTr) {
        Entry entry = new Entry();

        Element id = childrenOfTr.get(0);
        Element author = childrenOfTr.get(1);
        Element title = childrenOfTr.get(2);
        Element publisher = childrenOfTr.get(3);
        Element years = childrenOfTr.get(4);
        Element pageCount = childrenOfTr.get(5);
        Element language = childrenOfTr.get(6);
        Element size = childrenOfTr.get(7);
        Element extension = childrenOfTr.get(8);

        Elements allAuthorChildren = author.getAllElements();
        StringBuilder authorBuilder = new StringBuilder();
        authorBuilder.append(allAuthorChildren.first().text());
        entry.author = authorBuilder.toString();

        Elements titleChildren = title.getAllElements();
        StringBuilder titleBuilder = new StringBuilder();
        titleBuilder.append(titleChildren.first().text());
        entry.title = titleBuilder.toString();

        Elements hrefs = title.getElementsByAttributeValueContaining("href", "md5");
        entry.linkToPage = MainActivity.BASE_URL + hrefs.first().attr("href");

        // could be empty
        entry.publisher = publisher.text();

        // could be empty as well
        entry.year = Integer.valueOf(years.text());
        entry.pages = pageCount.text();

        entry.id = Integer.valueOf(id.text());
        entry.extension = extension.text();
        entry.size = size.text();
        entry.language = language.text();

        return entry;
    }

    @Override
    protected void onPostExecute(Collection<Entry> entries) {
        super.onPostExecute(entries);

        if (succeeded) {
            Toast.makeText(mainActivity.getApplicationContext(), "Finished downloading and parsing",
                    Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(mainActivity, ResultsActivity.class);
        Gson gson = new Gson();
        String entriesJson = gson.toJson(entries, COLLECTION_ENTRY_TYPE);
        intent.putExtra(ENTRIES_ID, entriesJson);

        mainActivity.startActivity(intent);
    }
}
