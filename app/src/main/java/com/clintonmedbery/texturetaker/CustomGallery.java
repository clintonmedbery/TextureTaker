package com.clintonmedbery.texturetaker;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by clintonmedbery on 4/6/15.
 */
public class CustomGallery extends ArrayAdapter {
    private Context context;
    private int pickerType;



    private File[] fileList;
    ArrayList<String> filePaths;

    public CustomGallery(Context context, int pickerType){
        super(context, 0);
        this.context = context;
        filePaths = new ArrayList<String>();
        getFilePaths();
        this.pickerType = pickerType;

    }

    private void getFilePaths(){
        File directory = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES + "/TextureTaker");
        fileList = directory.listFiles();
        Log.d("CUSTOMGALL GETFILEPATH", String.valueOf(fileList.length));
        for (File file: fileList){

            filePaths.add(file.getAbsolutePath());

            //ImageButton newImageButton = new ImageButton(this);
            //Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            //newImageButton.setImageBitmap(bitmap);
        }
    }

    public int getCount(){
        return filePaths.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;

        if (row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.grid_row, parent, false);

            final ImageButton newImageButton = (ImageButton) row.findViewById(R.id.imageButton);
            final int index = position;
            Bitmap bitmap = BitmapFactory.decodeFile(filePaths.get(position));
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, 128 ,128);
            newImageButton.setImageBitmap(bitmap);

            newImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("CUSTOM GALLERY", filePaths.get(index));
                    final String currentFilePath = filePaths.get(index);
                    if(pickerType == MainActivity.GALLERY_TO_RENDER){
                        Intent renderIntent = new Intent(context, RenderActivity.class);
                        renderIntent.putExtra("ImagePath", filePaths.get(index));

                        context.startActivity(renderIntent);
                    } else if(pickerType == MainActivity.GALLERY_TO_EMAIL){
                        Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
                        //email.setType("message/rfc822");
                        email.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        email.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        email.setType("image/jpeg");

                        File file = new File(currentFilePath);
                        email.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

                        try {
                            context.startActivity(Intent.createChooser(email, "Choose an email client..."));
                        } catch (ActivityNotFoundException error) {
                            Toast.makeText(context, "No email client installed.", Toast.LENGTH_LONG).show();
                        }

                    }

                }
            });
        }
        return row;
    }
}
