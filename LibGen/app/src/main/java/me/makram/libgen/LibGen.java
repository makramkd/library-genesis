package me.makram.libgen;

import android.app.Application;

import com.squareup.okhttp.OkHttpClient;

/**
 * Created by admin on 9/7/15.
 */
public class LibGen extends Application {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * What page number we are on when querying genesis.
     */
    private int pageNumber = 1;

    /**
     * The user's search query.
     */
    private String query;

    public OkHttpClient getClient() {
        return client;
    }

    public int incrementAndGetPageNumber() {
        ++pageNumber;
        return pageNumber;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int setPageNumber(int newNum) {
        pageNumber = newNum;
        return pageNumber;
    }

    public void setQuery(String q) {
        query = q;
    }

    public String getQuery() {
        return query;
    }
}
