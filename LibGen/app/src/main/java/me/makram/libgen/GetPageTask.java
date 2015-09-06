package me.makram.libgen;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by admin on 9/6/15.
 */
class GetPageTask extends AsyncTask<Request, Void, Void> {

    private MainActivity mainActivity;
    
    boolean succeeded = false;

    public GetPageTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Toast.makeText(mainActivity.getApplicationContext(), "Downloading page", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(Request... params) {
        try {
            Response response = mainActivity.client.newCall(params[0]).execute();
            Document document = Jsoup.parse(response.body().string());
            succeeded = true;
        } catch (Exception e) {
            Log.d("MA", "Exception in AsyncTask#GetPage: " + e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (succeeded) {
            Toast.makeText(mainActivity.getApplicationContext(), "Finished downloading and parsing",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
