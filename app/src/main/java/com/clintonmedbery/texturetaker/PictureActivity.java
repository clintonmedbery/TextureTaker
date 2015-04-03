package com.clintonmedbery.texturetaker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_picture, menu);
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


    public void saveTexture(View view){
        Log.d("Debug", "Save Texture");
        imageView.buildDrawingCache();
        Bitmap newBitmap = imageView.getDrawingCache();
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
