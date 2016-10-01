package com.example.motasemhamed.actualreality;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Environment;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


import android.content.res.AssetManager;
import android.widget.Button;
import android.widget.ImageView;
import android.view.View;
import android.view.View.OnClickListener;
import android.graphics.*;
import android.media.FaceDetector;
import android.widget.Toast;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.server.converter.StringToIntConverter;

/**
 * Created by motasemhamed on 9/6/16.
 */
public class AssestActivity extends AppCompatActivity {

    private Gallery gallery;
    private RecyclerView horizontal_recycler_view;
    private ArrayList<String> horizontalList,verticalList;
    private HorizontalAdapter horizontalAdapter;

    private android.util.LruCache<String, Bitmap> mLruCache;


    Context mContext;
    private Integer[] Imgid = {
            R.drawable.one, R.drawable.two, R.drawable.one, R.drawable.two, R.drawable.one, R.drawable.two, R.drawable.one
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private static final int WRITE_PERMISSION = 0x01;

    List<GridViewItem> gridItems;

    private void setGridAdapter(String path) {
        // Create a new grid adapter
        gridItems = createGridItems(path);
        //MyGridAdapter adapter = new MyGridAdapter(this, gridItems);

        // Set the grid adapter
//        GridView gridView = (GridView) findViewById(R.id.gridView);
//        gridView.setAdapter(adapter);

        // Set the onClickListener
 //       gridView.setOnItemClickListener(this);
    }

    private List<GridViewItem> createGridItems(String directoryPath) {
        List<GridViewItem> items = new ArrayList<GridViewItem>();

        // List all the items within the folder.
        File[] files = new File(directoryPath).listFiles(new ImageFileFilter());
        for (File file : files) {

            // Add the directories containing images or sub-directories
            if (file.isDirectory() && file.listFiles(new ImageFileFilter()).length > 0) {

                setGridAdapter(file.getAbsolutePath());

                items.add(new GridViewItem(file.getAbsolutePath(), true, null));
                Log.d(file.getName(), file.getAbsolutePath());

                myGallery.addView(insertPhoto(file.getAbsolutePath()));

            }
            // Add the images
            else {
                Bitmap image = BitmapHelper.decodeBitmapFromFile(file.getAbsolutePath(), 50, 50);
                items.add(new GridViewItem(file.getAbsolutePath(), false, image));
                Log.d("one ", file.getAbsolutePath());
                myGallery.addView(insertPhoto(file.getAbsolutePath()));

            }
        }

        return items;
    }


    LinearLayout myGallery;

    View insertPhoto(String path){
        Bitmap bm = decodeSampledBitmapFromUri(path, 500, 500);

        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setLayoutParams(new LayoutParams(500, 500));
        layout.setGravity(Gravity.CENTER);

        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setLayoutParams(new LayoutParams(500, 500));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(bm);

        layout.addView(imageView);
        return layout;
    }

    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {
        Bitmap bm = null;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }

    public int calculateInSampleSize(

            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }

        return inSampleSize;
    }


    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mLruCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mLruCache.get(key);
    }

    public class DownloadImagesTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public DownloadImagesTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }
        @Override
        protected Bitmap doInBackground(String... urls) {
            return download_Image(urls[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //mImageView.setImageBitmap(bitmap);              // how do I pass a reference to mChart here ?

            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = (ImageView)imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }

        }


        private Bitmap download_Image(String url) {
            //---------------------------------------------------
            Bitmap bm = null;
            try {
                URL aURL = new URL(url);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
                addBitmapToMemoryCache("image_data", bm);

            } catch (IOException e) {
                Log.e("Hub","Error getting the image from server : " + e.getMessage().toString());
            }
            return bm;
            //---------------------------------------------------
        }


    }

    ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(getApplicationContext());

        setContentView(R.layout.assest_activity);
        Button buttonLoadImage = (Button) findViewById(R.id.loadimage);
        mImageView = (ImageView) findViewById(R.id.cachImage);

        //Find out maximum memory available to application
        //1024 is used because LruCache constructor takes int in kilobytes
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/4th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 4;

        mLruCache = new android.util.LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes
                return bitmap.getByteCount() / 1024;
            }
        };

        Bitmap thumbnailImage = getBitmapFromMemCache("image_data");

        if (thumbnailImage == null){
            // if asked thumbnail is not present it will be put into cache
            DownloadImagesTask task = new DownloadImagesTask(mImageView);
            task.execute("http://kingofwallpapers.com/girl/girl-015.jpg");
        }

        textTargetUri = (TextView) findViewById(R.id.targeturi);
        targetImage = (ImageView) findViewById(R.id.targetimage);
        requestWritePermission();
        myGallery = (LinearLayout)findViewById(R.id.mygallery);

        gallery = (Gallery) findViewById(R.id.examplegallery);
        //gallery.setAdapter(new GalleryAdapter(this));
        //etGridAdapter("/storage/sdcard/DCIM/");

        mContext = getApplicationContext();


//        assert buttonLoadImage != null;
//        buttonLoadImage.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent(Intent.ACTION_PICK,
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, 0);
//            }
//        });
//

        ArrayList<String> places = new ArrayList<String>();

        String name;
        File CameraDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString());
        File[] files = CameraDirectory.listFiles();
        for (File CurFile : files) {
            if (CurFile.isDirectory()) {
                name = CurFile.getName();

                final String CompleteCameraFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/" + name;

                File sdcardPath = new File(CompleteCameraFolder);

                int imageCount = sdcardPath.listFiles().length;
                for (int count = 0; count < imageCount - 1; count++) {
                    Log.d(name, sdcardPath.listFiles()[count].getAbsolutePath());
                    //myGallery.addView(insertPhoto(sdcardPath.listFiles()[count].getAbsolutePath()));
                    places.add(sdcardPath.listFiles()[count].getAbsolutePath());

                }
            }else {
                Log.d("one ", CurFile.getAbsolutePath());

                //myGallery.addView(insertPhoto(CurFile.getAbsolutePath()));

            }
        }

        horizontal_recycler_view= (RecyclerView) findViewById(R.id.horizontal_recycler_view);
        horizontalList=new ArrayList<>();
        horizontalList.add("horizontal 1");
        horizontalList.add("horizontal 2");
        horizontalList.add("horizontal 3");
        horizontalList.add("horizontal 4");
        horizontalList.add("horizontal 5");
        horizontalList.add("horizontal 6");
        horizontalList.add("horizontal 7");
        horizontalList.add("horizontal 8");
        horizontalList.add("horizontal 9");
        horizontalList.add("horizontal 10");

        horizontalAdapter=new HorizontalAdapter(places);

        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(AssestActivity.this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManagaer);
        horizontal_recycler_view.setAdapter(horizontalAdapter);

        //gallery.setAdapter(new GalleryAdapter(this, places));


//        final String CompleteCameraFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/" + CameraFolder;
//
//
//
//        File sdcardPath = new File(Environment.getExternalStorageDirectory()
//                .getPath() + "/DCIM/Camera");
//        int imageCount = sdcardPath.listFiles().length;
//        for (int count = 0; count < imageCount - 1; count++) {
//            ImageView eachImageView= new ImageView(this);
//            Log.d("tag one", sdcardPath.listFiles()[count].getAbsolutePath());
//            Bitmap bmp = BitmapFactory.decodeFile(sdcardPath.listFiles()[10].getAbsolutePath());
//            eachImageView.setImageBitmap(bmp);
//            targetImage.setImageBitmap(bmp);
//
//        }


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    TextView textTargetUri;
    ImageView targetImage;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri targetUri = data.getData();
            textTargetUri.setText(targetUri.toString());
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                targetImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Assest Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.motasemhamed.actualreality/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Assest Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.motasemhamed.actualreality/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == WRITE_PERMISSION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You must allow permission write external storage to your mobile device.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestWritePermission(){
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_PERMISSION);
        }
    }

    /**
     * This can be used to filter files.
     */
    private class ImageFileFilter implements FileFilter {

        @Override
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            }
            else if (isImageFile(file.getAbsolutePath())) {
                return true;
            }
            return false;
        }
    }
    /**
     * Checks the file to see if it has a compatible extension.
     */
    private boolean isImageFile(String filePath) {
        if (filePath.endsWith(".jpg") || filePath.endsWith(".png"))
        // Add other formats as desired
        {
            return true;
        }
        return false;
    }



    public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

        private List<String> horizontalList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView txtView;
            public ImageView imageView;
            public SimpleDraweeView draweeView;

            public MyViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.image);
                draweeView = (SimpleDraweeView) view.findViewById(R.id.my_image_view);
            }
        }


        public HorizontalAdapter(List<String> horizontalList) {
            this.horizontalList = horizontalList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_gallery, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            Log.d("position", position+"");
            //holder.txtView.setText(horizontalList.get(position));
//            Log.i("ttttt",horizontalList.get(position) );
//            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//            Bitmap bitmap = BitmapFactory.decodeFile(horizontalList.get(position),bmOptions);
//            //bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);
//            holder.imageView.setImageBitmap(scaleCenterCrop(bitmap, 200, 200));
//
//            holder.imageView.setImageResource(Imgid[position]);
//


//
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(horizontalList.get(position),bmOptions);
//            int imageHeight = options.outHeight;
//            int imageWidth = options.outWidth;
//            String imageType = options.outMimeType;
//
//            holder.imageView.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.id.myimage, 100, 100));

//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeResource(getResources(), Imgid[0], options);
//            int imageHeight = options.outHeight;
//            int imageWidth = options.outWidth;
//            String imageType = options.outMimeType;


//            ImagePipeline imagePipeline = Fresco.getImagePipeline();
//
//            ImageRequest imageRequest = ImageRequestBuilder
//                    .newBuilderWithSource(Uri.parse(horizontalList.get(0)))
//                    .setRequestPriority(Priority.HIGH)
//                    .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
//                    .build();
//            DataSource<CloseableReference<CloseableImage>> dataSource =
//                    imagePipeline.fetchDecodedImage(imageRequest, mContext);
//
//            try {
//                dataSource.subscribe(new BaseBitmapDataSubscriber() {
//                    @Override
//                    public void onNewResultImpl(@Nullable Bitmap bitmap) {
//                        if (bitmap == null) {
//                            Log.d("", "Bitmap data source returned success, but bitmap null.");
//                            return;
//                        }
//                        holder.imageView.setImageBitmap(bitmap);
//
//                        // The bitmap provided to this method is only guaranteed to be around
//                        // for the lifespan of this method. The image pipeline frees the
//                        // bitmap's memory after this method has completed.
//                        //
//                        // This is fine when passing the bitmap to a system process as
//                        // Android automatically creates a copy.
//                        //
//                        // If you need to keep the bitmap around, look into using a
//                        // BaseDataSubscriber instead of a BaseBitmapDataSubscriber.
//
//                    }
//
//                    @Override
//                    public void onFailureImpl(DataSource dataSource) {
//                        // No cleanup required here
//                    }
//                }, CallerThreadExecutor.getInstance());
//            } finally {
//                if (dataSource != null) {
//                    dataSource.close();
//                }
//            }

//            if (position %2 == 0){
//                holder.imageView.setImageBitmap(decodeSampledBitmapFromResource(getResources(), horizontalList.get(position), 500, 500));
//
//            }else {
//                holder.imageView.setImageBitmap(decodeSampledBitmapFromResource(getResources(), horizontalList.get(position), 500, 500));
//
//            }
            holder.imageView.setTag(horizontalList.get(position));//tag of imageView == path to image
            //new LoadImage().execute(holder.imageView);
            //new LoadImage(holder.imageView).execute();
            new LoadImage(holder.imageView).execute();

//
            Uri imageUri = Uri.parse("https://i.imgur.com/tGbaZCY.jpg");
            //holder.draweeView.setImageURI(imageUri);
           //holder.draweeView.setImageBitmap(decodeSampledBitmapFromResource(getResources(), Imgid[0], 100, 100));
//            holder.imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                   // Toast.makeText(AssestActivity.this,holder.txtView.getText().toString(),Toast.LENGTH_SHORT).show();
//                }
//            });
        }

        @Override
        public int getItemCount() {
            return 100;//horizontalList.size();
        }


        class LoadImage extends AsyncTask<Object, Void, Bitmap>{

            private ImageView imv;
            private String path;

            public LoadImage(ImageView imv) {
                this.imv = imv;
                this.path = imv.getTag().toString();
            }

            @Override
            protected Bitmap doInBackground(Object... params) {
                Bitmap bitmap = null;
//                File file = new File(
//                        Environment.getExternalStorageDirectory().getAbsolutePath() + path);
//
//                if(file.exists()){
//                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                }
                bitmap = decodeSampledBitmapFromResource(getResources(), path, 500, 500);
                return bitmap;
            }
            @Override
            protected void onPostExecute(Bitmap result) {
                if (!imv.getTag().toString().equals(path)) {
               /* The path is not same. This means that this
                  image view is handled by some other async task.
                  We don't do anything and return. */
                    return;
                }

                if(result != null && imv != null){
                    imv.setVisibility(View.VISIBLE);
                    imv.setImageBitmap(result);
                }else{
                    imv.setVisibility(View.GONE);
                }
            }

        }

//        class LoadImage extends AsyncTask<Object, Void, Bitmap>{
//
//            private ImageView imv;
//            private String path;
//
//            public LoadImage(ImageView imageView) {
//                imv = imageView;
//            }
//
//
//            @Override
//            protected Bitmap doInBackground(Object... params) {
//
//
//                path = horizontalList.get(0);
//
//                Bitmap bitmap = null;
//                File file = new File(
//                        Environment.getExternalStorageDirectory().getAbsolutePath() + path);
//
//                if(file.exists()){
//                    bitmap = decodeSampledBitmapFromResource(getResources(), path, 500, 500);
//                }
//
//                return bitmap;
//            }
//            @Override
//            protected void onPostExecute(Bitmap result) {
//                Log.d("data", path);
//                if(result != null && imv != null){
//
//                    if(imv.getTag().equals(path)){
//                        imv.setVisibility(View.VISIBLE);
//                        imv.setImageBitmap(result);
//                    }else{
//                        imv.setVisibility(View.GONE);
//                    }
//
//                }else{
//                    imv.setVisibility(View.GONE);
//                }
//            }
//        }


        public Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
            int sourceWidth = source.getWidth();
            int sourceHeight = source.getHeight();

            // Compute the scaling factors to fit the new height and width, respectively.
            // To cover the final image, the final scaling will be the bigger
            // of these two.
            float xScale = (float) newWidth / sourceWidth;
            float yScale = (float) newHeight / sourceHeight;
            float scale = Math.max(xScale, yScale);

            // Now get the size of the source bitmap when scaled
            float scaledWidth = scale * sourceWidth;
            float scaledHeight = scale * sourceHeight;

            // Let's find out the upper left coordinates if the scaled bitmap
            // should be centered in the new size give by the parameters
            float left = (newWidth - scaledWidth) / 2;
            float top = (newHeight - scaledHeight) / 2;

            // The target rectangle for the new, scaled version of the source bitmap will now
            // be
            RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

            // Finally, we create a new bitmap of the specified size and draw our new,
            // scaled bitmap onto it.
            Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
            Canvas canvas = new Canvas(dest);
            canvas.drawBitmap(source, null, targetRect, null);

            return dest;
        }

        public  Bitmap decodeSampledBitmapFromResource(Resources res, String resId,
                                                             int reqWidth, int reqHeight) {

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            //BitmapFactory.decodeResource(res, resId, options);
            BitmapFactory.decodeFile(resId,options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            //return BitmapFactory.decodeResource(res, resId, options);
            return BitmapFactory.decodeFile(resId,options);

        }

        public  int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) >= reqHeight
                        && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }

        public Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
            return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
        }

        public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
            int width = image.getWidth();
            int height = image.getHeight();

            float bitmapRatio = (float) width / (float) height;
            if (bitmapRatio > 1) {
                width = maxSize;
                height = (int) (width / bitmapRatio);
            } else {
                height = maxSize;
                width = (int) (height * bitmapRatio);
            }

            return Bitmap.createScaledBitmap(image, width, height, true);
        }
    }

}

