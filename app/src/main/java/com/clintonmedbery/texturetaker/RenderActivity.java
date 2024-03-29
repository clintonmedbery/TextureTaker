package com.clintonmedbery.texturetaker;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.rajawali3d.surface.IRajawaliSurface;
import org.rajawali3d.surface.RajawaliSurfaceView;


public class RenderActivity extends ActionBarActivity implements GestureDetector.OnGestureListener {

    Renderer renderer;
    public RelativeLayout relativeLayout;
    private int screenWidth;
    private int screenHeight;

    private Bitmap picture;
    private String imagePath;


    private GestureDetectorCompat detector;
    private static final String DEBUG_TAG = "Gestures";

    float x1;
    float x2;
    float y1;
    float y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_render);

        relativeLayout = (RelativeLayout) findViewById(R.id.renderLayout);

        detector = new GestureDetectorCompat(this, this);

        Intent receiveIntent = getIntent();
        imagePath = receiveIntent.getStringExtra("ImagePath");
        picture = BitmapFactory.decodeFile(imagePath);

        final RajawaliSurfaceView surface= new RajawaliSurfaceView(this);
        surface.setFrameRate(60.0);
        surface.setRenderMode(IRajawaliSurface.RENDERMODE_WHEN_DIRTY);

        // Add surface to your root view
        addContentView(surface, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT));

        renderer = new Renderer(this, picture);
        surface.setSurfaceRenderer(renderer);


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;

        ViewGroup.LayoutParams layoutParams=surface.getLayoutParams();
        layoutParams.width=screenWidth;
        layoutParams.height=screenWidth;
        surface.setLayoutParams(layoutParams);
    }


    public void setPlane(View view){
        renderer.switchToPlane();
    }

    public void setCube(View view){
        renderer.switchToCube();
    }

    public void setSphere(View view){
        renderer.switchToSphere();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

       this.detector.onTouchEvent(event);

        return  super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        Log.d(DEBUG_TAG, "onDown: " + event.toString());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        Log.d(DEBUG_TAG, "onFling: " + event1.toString()+event2.toString());



        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onLongPress: " + event.toString());
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        //Log.d(DEBUG_TAG, "onScroll: " + e1.toString()+e2.toString());
        Log.d(DEBUG_TAG, "Distance X: " + String.valueOf(distanceX) + "Distance Y: " + String.valueOf(distanceY));

        renderer.scrollCurrentObject(distanceX, distanceY);
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
        return true;
    }
}
