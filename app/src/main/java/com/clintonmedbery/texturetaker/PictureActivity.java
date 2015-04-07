package com.clintonmedbery.texturetaker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PictureActivity extends ActionBarActivity {

    private Bitmap bitmap;
    public ImageView imageView;
    private Bitmap picture;
    private String imagePath;

    private int[] pixels;
    private int imageHeight;
    private int imageWidth;
    private int screenWidth;
    private int screenHeight;
    public SeekBar cropSeek;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        imageView = (ImageView) findViewById(R.id.imageView);
        cropSeek = (SeekBar) findViewById(R.id.cropBar);

        Intent receiveIntent = getIntent();
        imagePath = receiveIntent.getStringExtra("ImagePath");
        picture = BitmapFactory.decodeFile(imagePath);

        imageHeight = picture.getHeight();
        imageWidth = picture.getWidth();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;


        if(imageWidth > imageHeight){
            picture = rotateBitmap(picture, 90);
            imageHeight = picture.getHeight();
            imageWidth = picture.getWidth();
        }

        Log.d("Debug", "Image Height: " + String.valueOf(imageHeight));
        Log.d("Debug", "Image Width: " + String.valueOf(imageWidth));
        Log.d("Debug", "Screen Height: " + String.valueOf(screenHeight));
        Log.d("Debug", "Screen Width: " + String.valueOf(screenWidth));

        pixels = new int[imageHeight * imageWidth];
        picture.getPixels(pixels, 0, imageWidth, 0, screenHeight/4, imageWidth, imageWidth);
        bitmap = Bitmap.createBitmap(pixels, imageWidth, imageWidth, Config.ARGB_8888);
        imageView.getLayoutParams().height = screenWidth;
        imageView.getLayoutParams().width = screenWidth;
        imageView.setImageBitmap(bitmap);

        cropSeek.setMax(imageHeight/2);
        cropSeek.setProgress(imageHeight/4);
        cropSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progressChanged = progress;
                setCrop(progress);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Log.d("Debug", "Image View Height is: " + imageView.getLayoutParams().height);
        Log.d("Debug", "Image View Width is: " + imageView.getLayoutParams().width);

    }

    public static Bitmap rotateBitmap(Bitmap source, float angle)
    {

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public void saveTexture(View view){
        Log.d("Debug", "Save Texture");
        imageView.buildDrawingCache();
        Bitmap newBitmap = imageView.getDrawingCache();
        newBitmap.setHeight(imageView.getLayoutParams().height);
        Log.d("Debug", "Image View Height is: " + imageView.getLayoutParams().height);
        Log.d("Debug", "Image View Width is: " + imageView.getLayoutParams().width);
        Log.d("Debug", "NEW BITMAP Height is: " + newBitmap.getHeight());
        Log.d("Debug", "NEW BITMAP Width is: " + newBitmap.getWidth());


        File file = new File(imagePath);
        try{
            FileOutputStream fOut = new FileOutputStream(file);

            newBitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            try{
                fOut.flush();
                fOut.close();
            } catch(java.io.IOException error){
                Log.d("Debug", "IOException");
            }

        } catch (FileNotFoundException e ){
            Log.d("Debug", "FileNotFound");

        }

        Intent viewIntent = new Intent(this, RenderActivity.class);
        viewIntent.putExtra("ImagePath", imagePath);

        startActivity(viewIntent);

    }

    public void setCrop(int y){
        Log.d("Debug", "Y is: " + String.valueOf(y));
        Log.d("Debug", "ImageHeight/2 is: " + String.valueOf(imageHeight/2));
        Log.d("Debug", "Imageview is: " + String.valueOf(imageView.getHeight()));
        try{
            picture.getPixels(pixels, 0, imageWidth, 0, y, imageWidth, imageWidth);

        } catch(IllegalArgumentException e){

        }
        bitmap = Bitmap.createBitmap(pixels, imageWidth, imageWidth, Config.ARGB_8888);
        imageView.setImageBitmap(bitmap);
    }
}
