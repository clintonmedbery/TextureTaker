package com.clintonmedbery.texturetaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.MotionEvent;

import org.rajawali3d.Object3D;
import org.rajawali3d.lights.PointLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Cube;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.renderer.RajawaliRenderer;


/**
 * Created by clintonmedbery on 4/5/15.
 */
public class Renderer extends RajawaliRenderer {

    public PointLight light;
    public Cube cube;
    public Sphere sphere;
    public Plane plane;
    public Context context;
    public Object3D currentObject;
    private Material material;
    private Texture texture;

    private float currentXRotation;
    private float currentYRotation;

    private Bitmap picture;

    public Renderer(Context context, Bitmap bitmap) {
        super(context);
        this.context = context;
        setFrameRate(60);
        this.picture = bitmap;
    }

    public Renderer(Context context) {
        super(context);
        this.context = context;
        setFrameRate(60);
    }

    public void initScene() {
        light = new PointLight();
        light.setColor(1.0f, 1.0f, 1.0f);
        light.setPower(12);
        getCurrentScene().addLight(light);

        cube = new Cube(2.0F);
        sphere = new Sphere(1, 24, 24);
        plane = new Plane(3, 3, 3, 3, 2);


        material = new Material();
        material.setColor(0);


        material.enableLighting(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());

        texture = new Texture("Texture", picture);
        try {
            material.addTexture(texture);
        } catch (ATexture.TextureException e){
            Log.d("Debug", "TEXTURE EXCEPTION");
        }

        getCurrentScene().addChild(cube);
        getCurrentScene().addChild(sphere);
        getCurrentScene().addChild(plane);

        sphere.setVisible(false);
        plane.setVisible(false);



        cube.setRotY(cube.getRotY() + 45);
        cube.setRotY(cube.getRotX() + 45);

        plane.setRotX(plane.getRotX() + 190);
        plane.setRotY(plane.getRotY() + 190);

        cube.setMaterial(material);
        plane.setMaterial(material);
        sphere.setMaterial(material);

        currentObject = cube;

        getCurrentCamera().setZ(4.2f);
    }

    @Override
    public void onRender(final long elapsedTime, final double deltaTime) {
        super.onRender(elapsedTime, deltaTime);

        if(currentXRotation > 0){
            currentObject.rotate(Vector3.Axis.X, 1);
            currentXRotation -= .5f;
            if(currentXRotation < 5.0f){
                currentXRotation = 0;
            }
        }

        if(currentXRotation < 0){
            currentObject.rotate(Vector3.Axis.X, -1);
            currentXRotation += .5f;
            if(currentXRotation > 5.0f){
                currentXRotation = 0;
            }
        }

        if(currentYRotation < 0){
            currentObject.rotate(Vector3.Axis.Y, 1);
            currentYRotation += .5f;
            if(currentYRotation > 5.0f){
                currentYRotation = 0;
            }
        }

        if(currentYRotation > 0){
            currentObject.rotate(Vector3.Axis.Y, -1);
            currentYRotation -= .5f;
            if(currentYRotation < 5.0f){
                currentYRotation = 0;
            }
        }

        //Log.d("DEBUG", "CURRENTXROTATION: " + currentXRotation);
        //Log.d("DEBUG", "CURRENTYROTATION: " + currentYRotation);


    }

    public void onTouchEvent(MotionEvent event){

    }

    public void onOffsetsChanged(float x, float y, float z, float w, int i, int j){

    }

    public void rotateCurrentObject(float x1, float x2, float y1, float y2){
        if(x1 < x2){
            //Left to right swipe
            currentXRotation = x1 - x2;
        }

        if (x1 > x2){
            //Right to left swipe
            currentXRotation = x1 - x2;
        }
        if( y1 < y2){
            //Up to down swipe
            currentYRotation = y1 - y2;
        }
        if(y1 > y2){
            //Down to up swipe
            currentYRotation = y1 - y2;
        }
    }

    public void scrollCurrentObject(float distanceX, float distanceY){
        if(Math.abs(distanceX) > Math.abs(distanceY)){
            currentYRotation = distanceX;
        } else {
            currentXRotation = distanceY;

        }

    }

    public void switchToPlane(){
        sphere.setVisible(false);
        cube.setVisible(false);
        plane.setVisible(true);
        currentObject = plane;
    }

    public void switchToCube(){
        sphere.setVisible(false);
        cube.setVisible(true);
        plane.setVisible(false);
        currentObject = cube;
    }

    public void switchToSphere(){
        sphere.setVisible(true);
        cube.setVisible(false);
        plane.setVisible(false);
        currentObject = sphere;
    }



}
