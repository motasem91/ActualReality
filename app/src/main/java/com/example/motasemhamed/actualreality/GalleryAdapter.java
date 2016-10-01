package com.example.motasemhamed.actualreality;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by motasemhamed on 9/6/16.
 */
public class GalleryAdapter extends BaseAdapter {
    int GalItemBg;
    private Context cont;

    // Adding images.
    private Integer[] Imgid = {
            R.drawable.one, R.drawable.two, R.drawable.one, R.drawable.two, R.drawable.one, R.drawable.two, R.drawable.one
    };
    LayoutInflater inflater;

    public GalleryAdapter(Context c) {
        cont = c;
        inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//        TypedArray typArray = obtainStyledAttributes(R.styleable.GalleryTheme);
//        GalItemBg = typArray.getResourceId(R.styleable.GalleryTheme_android_galleryItemBackground, 0);
//        typArray.recycle();
    }
    ArrayList<String> places ;

    public GalleryAdapter(Context c, ArrayList<String> places) {
        cont = c;
        this.places = places;
        inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return places.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View container = inflater.inflate(R.layout.layout_gallery, parent, false);
        ImageView image = (ImageView) container.findViewById(R.id.image);
        //image.setImageResource(Imgid[position]);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(places.get(position),bmOptions);
        //bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);
        image.setImageBitmap(bitmap);

        Button text = (Button)container.findViewById(R.id.buttonOne);
        text.setText("whatever");

        return container;
    }

}
