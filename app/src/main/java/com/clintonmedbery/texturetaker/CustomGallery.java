package com.clintonmedbery.texturetaker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by clintonmedbery on 4/6/15.
 */
public class CustomGallery extends ArrayAdapter {
    private Context context;
    private int galleryCount;


    private File[] fileList;
    ArrayList<String> filePaths;

    public CustomGallery(Context context){
        super(context, 0);
        this.context = context;
        filePaths = new ArrayList<String>();
        getFilePaths();

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
            newImageButton.setImageBitmap(bitmap);

            newImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("CUSTOM GALLERY", filePaths.get(index));
                    Intent renderIntent = new Intent(context, RenderActivity.class);
                    renderIntent.putExtra("ImagePath", filePaths.get(index));

                    context.startActivity(renderIntent);
                }
            });
        }
        return row;
    }
}
