package me.makram.libgen;

import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by admin on 9/7/15.
 */
public class GetEntryDetailsTask extends AsyncTask<String, Void, Void> {

    ResultsActivity resultsActivity;

    public GetEntryDetailsTask(ResultsActivity activity) {
        resultsActivity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Toast.makeText(resultsActivity, "Getting book details...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        Toast.makeText(resultsActivity, "Finished getting book details!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(String... params) {
        return null;
    }
}
