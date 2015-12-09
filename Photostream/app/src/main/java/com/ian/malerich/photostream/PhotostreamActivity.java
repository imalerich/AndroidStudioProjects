package com.ian.malerich.photostream;

import android.app.Activity;
import android.app.ListActivity;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PhotostreamActivity extends Activity {
    private RequestQueue queue;
    private ArrayList<String> images = new ArrayList<>();
    private SimpleCursorAdapter cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photostream);

        // create a Volley request for the text view
        StringRequest strreq = new StringRequest(Request.Method.GET, "http://photostream.iastate.edu/api/photo?key=c88ba8aca62dd5ccb60d", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray arr = new JSONArray(response);

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        images.add(obj.getString("image_medium"));
                    }

                     displayImages();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //
            }
        });

        queue.add(strreq);
    }

    public void displayImages() {
        UrlImageAdapter adapter = new UrlImageAdapter(this, R.layout.image_cell, images, queue);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photostream, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
