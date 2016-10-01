package com.example.motasemhamed.actualreality;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by motasemhamed on 9/8/16.
 */
public class GihpyActivity extends AppCompatActivity {

    private ImageView imageView;
    public static final String TAG = GihpyActivity.class.getSimpleName();
    private static ArrayList<GiphyGifData> gifImages;
    private  GiphyAdapter horizontalAdapter;
    private RecyclerView horizontal_recycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giphy_activity);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageView = (ImageView) findViewById(R.id.imageViewGif);

        horizontal_recycler_view= (RecyclerView) findViewById(R.id.horizontal_recycler_view);

        horizontalAdapter=new GiphyAdapter(this, gifImages);

        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(GihpyActivity.this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManagaer);

        horizontal_recycler_view.setAdapter(horizontalAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
    private void refresh() {
        getGiphy();
 //       loadRandomDrawable();
    }

//    private void loadRandomDrawable() {
//        TypedArray images = getResources().obtainTypedArray(R.array.cat_images);
//        int randImage = (int) (Math.random() * images.length());
//        Log.i(TAG, "Random Image: " + randImage);
//
//        int image = images.getResourceId(randImage, R.drawable.cutecat001);
//        gifImageView.setImageResource(image);
//        images.recycle();
//    }

    private GiphyGifData getGif(String jsonData) throws JSONException {
        JSONObject giphy = new JSONObject(jsonData);
        JSONObject data = giphy.getJSONObject("data");

        GiphyGifData gif = new GiphyGifData();
        //gif.setUrl(data.getString("image_url"));
        Log.i(TAG, "Gif JSON Data - GIF URL: " + gif);

        return gif;
    }

    private void updateDisplay() {
//        //String gifUrl = giphyGifData.getUrl();
//        Log.i(TAG, "updateDisplay GIF URL: " + gifUrl);
//
////        Picasso.with(GihpyActivity.this).load(gifUrl)
////                .placeholder(R.drawable.one)
////                .error(R.drawable.two)
////                .into(imageView);
//
//        Glide.with(GihpyActivity.this)
//                .load(gifUrl)
//                .thumbnail(0.1f)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .placeholder(R.drawable.progress_animation)
//                .into(new GlideDrawableImageViewTarget(imageView) {
//                    @Override
//                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
//                        super.onResourceReady(resource, animation);
//                        //check isRefreshing
//                    }
//                });


    }

    private void getGiphy () {
        //URL Format: http://api.giphy.com/v1/gifs/search?q=cute+cat&api_key=dc6zaTOxFJmzC&limit=1&offset=0
        //Random Search URL: http://api.giphy.com/v1/gifs/random?api_key=dc6zaTOxFJmzC&tag=cute+funny+cat+kitten
        String apiKey = "dc6zaTOxFJmzC"; //Giphy's Public API Key



//        String giphyUrl =
//                "http://api.giphy.com/v1/gifs/random" +
//                        "?api_key=" +
//                        apiKey +
//                        "&tag=" +
//                        "cat";

        String giphyUrl = "http://api.giphy.com/v1/gifs/trending?api_key=dc6zaTOxFJmzC&limit=10";

        if (isNetworkAvailable()) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(giphyUrl)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            toggleRefresh();
                        }
                    });
                    Log.i(TAG, "Request Failure");
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                    try {
                        final String jsonData = response.body().string();
                        if (response.isSuccessful()) {

                            //giphyGifData = getGif(jsonData);
                           // Log.v(TAG, "Giphy Gif Data from Response: " + giphyGifData);
                            //JSONObject giphy = new JSONObject(jsonData);
                            //JSONObject data = giphy.getJSONObject("data");
                            //Log.v(TAG, "Giphy Gif Data from Response: " + data);


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //updateDisplay();
                                    try {
                                        setImage(jsonData);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        } else {
                            Log.i("Erorrr", "sssss");
                            Log.i(TAG, "Response Unsuccessful");
                        }
                    }
                    catch (IOException e) {
                        Log.i("Erorrr", "sssss");
                        Log.e(TAG, "Exception Caught: ", e);
                    }
                }
            });
        } else {
            Toast.makeText(this, "Network is not available!", Toast.LENGTH_LONG).show();
        }

    }

    String URL;
    private void setImage(String jsonData) throws JSONException {

        JSONObject jsonResponse;

        jsonResponse = new JSONObject(jsonData);
        JSONArray jsonMainNode = jsonResponse.optJSONArray("data");
        Log.v(TAG, "Giphy Gif Data from Response: " + jsonMainNode);
        int lengthJsonArr = jsonMainNode.length();
        Log.v(TAG, "data count : " + lengthJsonArr);

        gifImages = new ArrayList<GiphyGifData>();

        for (int i = 0; i < lengthJsonArr; i++) {

            GiphyGifData item = new GiphyGifData();
            item.setFixedHeightDownsampledUrl(jsonMainNode.getJSONObject(i).getJSONObject("images").getJSONObject("fixed_height_downsampled").optString("url"));
            item.setFixedHeightDownsampledWidth(jsonMainNode.getJSONObject(i).getJSONObject("images").getJSONObject("fixed_height").optString("width"));
            item.setFixedWidthUrl(jsonMainNode.getJSONObject(i).getJSONObject("images").getJSONObject("fixed_width").optString("url"));

//            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
//            JSONObject workArray = jsonChildNode.getJSONObject("images");
//            JSONObject workArray2 = workArray.getJSONObject("fixed_height");
//            String url = workArray2.optString("url").toString();
//            Log.v(TAG, "fixed height data : " + workArray);
//            Log.v(TAG, "fixed height data two : " + url);
//
//            URL = jsonMainNode.getJSONObject(i).getJSONObject("images").getJSONObject("fixed_height_downsampled").optString("url").toString();
//            Log.v(TAG, "getting url : " + URL);
//            String WIDTH = jsonMainNode.getJSONObject(i).getJSONObject("images").getJSONObject("fixed_height").optString("width").toString();
//            Log.v(TAG, "getting url : " + WIDTH);

            gifImages.add(item);
        }

        horizontalAdapter.notifyDataSetChanged();

        Glide.with(GihpyActivity.this)
                .load(URL)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new GlideDrawableImageViewTarget(imageView) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        //check isRefreshing
                    }
                });

        imageView.getLayoutParams().width = 1000;
        imageView.getLayoutParams().height = 1000;
        imageView.requestLayout();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void doSearchForGiphy(String data)throws Exception{
        String searchUrl = "http://api.giphy.com/v1/gifs/search?q=" + data + "&api_key=dc6zaTOxFJmzC&limit=20";
        // doSearch using Volley

        OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormEncodingBuilder()
                    .add("email", "Jurassic@Park.com")
                    .add("tel", "90301171XX")
                    .build();
            Request request = new Request.Builder()
                    .url("https://en.wikipedia.org/w/index.php")
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            System.out.println(response.body().string());

    }

    OkHttpClient client = new OkHttpClient();

    // get method
    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
