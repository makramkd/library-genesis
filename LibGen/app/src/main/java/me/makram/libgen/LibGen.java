package me.makram.libgen;

import android.app.Application;
import android.content.Context;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;

/**
 * Created by admin on 9/7/15.
 */
public class LibGen extends Application {
    private OkHttpClient client;
    private Cache cache;

    /**
     * What page number we are on when querying genesis.
     */
    private int pageNumber = 1;

    /**
     * The user's search query.
     */
    private String query;

    @Override
    public void onCreate() {
        super.onCreate();

        cache = new Cache(getCacheDir(), 10 * 1024 * 1024);
        client = new OkHttpClient();
        client.setCache(cache);
    }

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
