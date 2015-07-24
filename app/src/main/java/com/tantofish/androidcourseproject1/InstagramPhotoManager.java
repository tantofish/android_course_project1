package com.tantofish.androidcourseproject1;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yutu on 7/24/15.
 */
public class InstagramPhotoManager {

    static final String CLIENT_ID = "2dd2f932e83f4333bfb15b08c34c908e";


    private ArrayList<InstagramPhoto> popularPhotos;
    private Context mContext;

    public InstagramPhotoManager(Context context) {
        this.mContext = context;
        popularPhotos = new ArrayList<>();
    }

    public ArrayList<InstagramPhoto> getPopularPhotos() {
        return popularPhotos;
    }

    //send request to instagram API
    public void fetchPopularPhoto(){
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {

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
                        photo.userPhotoUrl= photoJson.getJSONObject("user").getString("profile_picture");
                        if (photoJson.optJSONObject("caption") != null)
                            photo.caption     = photoJson.getJSONObject("caption").getString("text");
                        photo.imageUrl    = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.imageWidth  = photoJson.getJSONObject("images").getJSONObject("standard_resolution").getInt("width");
                        photo.likeCount   = photoJson.getJSONObject("likes").getInt("count");
                        photo.createdTime = photoJson.getString("created_time");
                        photo.location    = photoJson.optString("location");

                        popularPhotos.add(photo);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }

                ((MainActivity)mContext).update();
            }
        });
    }
}
