package com.clintonmedbery.texturetaker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int MEDIA_TYPE_IMAGE = 2;
    static final int SELECT_PHOTO = 3;
    public Uri imageUri;


    public Bitmap picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }




    public void dispatchTakePictureIntent(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){

            Log.d("Debug", imageUri.getPath());

            Intent editIntent = new Intent(this, PictureActivity.class);
            editIntent.putExtra("ImagePath", imageUri.getPath());

            startActivity(editIntent);
        }

        //if(requestCode == SELECT_PHOTO && resultCode == RESULT_OK){
            //Log.d("Debug", imageUri.getPath());

            //Intent editIntent = new Intent(this, PictureActivity.class);
            //editIntent.putExtra("ImagePath", imageUri.getPath());
            //Log.d("Debug", imageUri.getPath());
            //startActivity(editIntent);
        //}

    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type){


        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "TextureTaker");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("TextureTaker", "failed to create directory");
                return null;
            }
        }
        Log.d("TextureTaker", mediaStorageDir.toString());
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }


    public void pickImage(View view){
        //Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        //photoPickerIntent.setType("image/*");

        //startActivityForResult(photoPickerIntent, SELECT_PHOTO);

        Intent pickerIntent = new Intent(this, ImagePickerActivity.class);


        startActivity(pickerIntent);
    }
}
