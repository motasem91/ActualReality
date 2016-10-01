package com.example.motasemhamed.actualreality;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by motasemhamed on 9/8/16.
 */
public class GiphyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

private static String TAG = GiphyAdapter.class.getSimpleName();

private String userId;
private int SELF = 100;
private static String today;

private Context mContext;
private ArrayList<GiphyGifData> messageArrayList;



    public class ViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;

    public ViewHolder(View view) {
        super(view);
        imageView = (ImageView) itemView.findViewById(R.id.imageViewGiphy);
    }
}


    public GiphyAdapter(Context mContext, ArrayList<GiphyGifData> gifImages) {
        this.mContext = mContext;
        this.messageArrayList = gifImages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.giphy_adapter, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        GiphyGifData data = messageArrayList.get(position);

        Glide.with(mContext)
                .load(data.getFixedHeightDownsampledUrl())
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new GlideDrawableImageViewTarget(((ViewHolder) holder).imageView) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        //check isRefreshing
                    }
                });

        ((ViewHolder) holder).imageView.getLayoutParams().width = 1000;
        ((ViewHolder) holder).imageView.getLayoutParams().height = 1000;
        ((ViewHolder) holder).imageView.requestLayout();
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

}
