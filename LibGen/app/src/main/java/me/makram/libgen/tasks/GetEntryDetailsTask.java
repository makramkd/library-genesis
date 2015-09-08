package me.makram.libgen.tasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import me.makram.libgen.BuildConfig;
import me.makram.libgen.R;
import me.makram.libgen.activities.DetailsActivity;
import me.makram.libgen.LibGen;
import me.makram.libgen.activities.ResultsActivity;
import me.makram.libgen.data.Entry;

/**
 * The task that is used in order to get the details of the entry.
 * Created by admin on 9/7/15.
 */
public class GetEntryDetailsTask extends AsyncTask<Entry, Void, String> {

    public static final String LIBGEN_IO_GET_PHP = "libgen.io/get.php";
    public static final String HREF = "href";
    public static final String ENTRY_KEY = "entryObjectKey";
    public static final String DOWNLOAD_LINK_KEY = "entryDownloadLinkKey";

    ResultsActivity resultsActivity;
    Entry entry;

    public GetEntryDetailsTask(ResultsActivity activity) {
        resultsActivity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (BuildConfig.DEBUG) {
            Toast.makeText(resultsActivity, "Getting book details...", Toast.LENGTH_SHORT).show();
        }

        resultsActivity.getProgressDialog().show();
    }

    @Override
    protected void onPostExecute(String downloadLink) {
        super.onPostExecute(downloadLink);

        resultsActivity.getProgressDialog().hide();

        if (downloadLink != null) {
            Toast.makeText(resultsActivity, "Finished getting book details!",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(resultsActivity, DetailsActivity.class);
            Gson gson = new Gson();
            String entryJson = gson.toJson(entry, Entry.class);
            intent.putExtra(ENTRY_KEY, entryJson);
            intent.putExtra(DOWNLOAD_LINK_KEY, downloadLink);

            resultsActivity.startActivity(intent);
        } else {
            Toast.makeText(resultsActivity,
                    resultsActivity.getResources().getString(R.string.errorOpeningDetails),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected String doInBackground(Entry... params) {
        entry = params[0];
        String link = null;
        try {
            Response response = ((LibGen)resultsActivity.getApplication()).getClient()
                    .newCall(new Request.Builder()
                    .url(entry.linkToPage)
                    .build())
                    .execute();

            Document document = Jsoup.parse(response.body().string());

            response.body().close();

            Elements links = document.getElementsByAttributeValueContaining(HREF,
                    LIBGEN_IO_GET_PHP);

            // it's possible that one of them returns the image; all we really need is the
            // direct download link for now
            link = links.first().attr(HREF);

        } catch (Exception e) {
            Log.v("GEDT", "Exception in doInBackground: " + e.getMessage());
        }

        return link;
    }
}
