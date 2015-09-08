package me.makram.libgen;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import me.makram.libgen.data.Entry;

/**
 * Created by admin on 9/7/15.
 */
public class EntryAdapter extends BaseAdapter {

    private static final String TAG = "EntryAdapter";

    int itemLayoutId;
    Context context;
    List<Entry> data;
    LayoutInflater layoutInflater;
    private class ViewHolder {
        TextView titleTextView;
        TextView authorTextView;
    }

    public EntryAdapter(Context context, int itemLayoutId, Collection<Entry> data) {
        Log.v(TAG, "Constructing EntryAdapter");

        this.context = context;
        this.itemLayoutId = itemLayoutId;
        this.data = new ArrayList<>();
        this.data.addAll(data);
        layoutInflater = LayoutInflater.from(context);

        Log.v(TAG, "Finished Constructing EntryAdapter");
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        Log.v(TAG, "In getView for position " + position + ", convertView is " +
                ((convertView == null) ? "null" : "being recycled"));

        if (convertView == null) {
            convertView = layoutInflater.inflate(itemLayoutId, null);
            holder = new ViewHolder();
            holder.authorTextView = (TextView) convertView.findViewById(R.id.authorTextView);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Entry entry = data.get(position);
        String title = entry.title, author = entry.author;

        // i think this is unnecessary: just show the whole thing, it's
        // not a big deal.
//        if (entry.title.length() >= 60) {
//            title = entry.title.substring(0, 60);
//        }
//        if (entry.author.length() >= 70) {
//            author = entry.author.substring(0, 70);
//        }
        
        holder.titleTextView.setText(title);
        holder.authorTextView.setText(author);

        return convertView;
    }

    public void addEntries(Collection<Entry> entries) {
        data.addAll(entries);
    }

    public Collection<Entry> getData() {
        return data;
    }

    public void setData(List<Entry> entries) {
        data = entries;
        notifyDataSetChanged();
    }
}
