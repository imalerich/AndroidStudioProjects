package com.ian.malerich.photostream;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.ArrayList;

/**
 * Created by wdmalerich on 12/8/15.
 */
public class UrlImageAdapter extends ArrayAdapter<PhotostreamItem> {
    private RequestQueue queue;
    private ArrayList<PhotostreamItem> strings;

    public UrlImageAdapter(Context context, int textViewResourceId, ArrayList<PhotostreamItem> Items, RequestQueue Queue) {
        super(context, textViewResourceId, Items);
        strings = Items;
        queue = Queue;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater li = LayoutInflater.from(this.getContext());
            v = li.inflate(R.layout.image_cell, null);
        }

        PhotostreamItem current = strings.get(position);

        final ImageView imageView = (ImageView) v.findViewById(R.id.image_view);
        setImageViewWithURL(current.imgUrl, imageView);

        final TextView titleView = (TextView) v.findViewById(R.id.title_view);
        titleView.setText(current.title);

        final TextView authorView = (TextView) v.findViewById(R.id.author_view);
        authorView.setText(current.author);

        return v;
    }

    private void setImageViewWithURL(String url, final ImageView imageView) {
        // load the image for the image view
        ImageRequest imgreq = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //
                    }
                });

        queue.add(imgreq);
    }
}
