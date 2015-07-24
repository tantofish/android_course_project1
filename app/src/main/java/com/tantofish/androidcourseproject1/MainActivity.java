package com.tantofish.androidcourseproject1;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    static final String CLIENT_ID = "2dd2f932e83f4333bfb15b08c34c908e";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotoAdapter ipAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        photos = new ArrayList<>();
        ipAdapter = new InstagramPhotoAdapter(this, photos);

        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(ipAdapter);

        //send request to instagram API
        fetchPopularPhoto();


    }

    public void fetchPopularPhoto(){
        // Popular: https://api.instagram.com/v1/media/popular?access_token=ACCESS-TOKEN
        // Type: { "data" => [x] => "type" } (image or video)
        // URL: { "data" => [x] => "images" => "standard_resolution" => "url"}
        // Caption: { "data" => [x] => "caption" => "text"}
        // Author: { "data" => [x] => "user" => "username"}

        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            // onSuccess(worked, 200)

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);

                JSONArray photosJson = null;
                try {
                    photosJson = response.getJSONArray("data");
                    for( int i = 0 ; i < photosJson.length() ; i++){
                        JSONObject photoJson = photosJson.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username    = photoJson.getJSONObject("user").getString("username");
                        photo.caption     = photoJson.getJSONObject("caption").getString("text");
                        photo.imageUrl    = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.imageWidth  = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getInt("width");
                        photo.likeCount   = photoJson.getJSONObject("likes").getInt("count");

                        photos.add(photo);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }

                ipAdapter.notifyDataSetChanged();
            }


            // onFailure()
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
