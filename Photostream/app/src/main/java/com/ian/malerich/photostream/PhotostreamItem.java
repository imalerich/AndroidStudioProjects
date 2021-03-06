package com.ian.malerich.photostream;

import android.provider.Contacts;

import org.json.JSONObject;

/**
 * Created by wdmalerich on 12/8/15.
 */
public class PhotostreamItem {
    public String imgUrl;
    public String title;
    public String descr;
    public String author;

    public PhotostreamItem(JSONObject obj) {
        try {
            imgUrl = obj.getString("image_medium");
            title = obj.getString("title");
            descr = obj.getString("description");

            JSONObject submittedBy = obj.getJSONObject("submitted_by");
            author = submittedBy.getString("name");
        } catch (Exception e) {
            // fuck exceptions
        }
    }
}
