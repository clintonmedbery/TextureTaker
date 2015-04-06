package com.clintonmedbery.texturetaker;

import android.content.Context;
import android.view.MotionEvent;

import org.rajawali3d.Camera;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.primitives.Cube;
import org.rajawali3d.renderer.RajawaliRenderer;


/**
 * Created by clintonmedbery on 4/5/15.
 */
public class Renderer extends RajawaliRenderer {

    public DirectionalLight light;
    public Cube cube;
    public Context context;
    public Camera camera;

    public Renderer(Context context) {
        super(context);
        this.context = context;
        setFrameRate(60);
    }

    public void initScene() {
        light = new DirectionalLight(1f, 0.2f, -1.0f); // set the direction
        light.setColor(1.0f, 1.0f, 1.0f);
        light.setPower(2);
        getCurrentScene().addLight(light);
        cube = new Cube(1.0F);
        Material material = new Material();
        cube.setMaterial(material);

        material.enableLighting(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());

        getCurrentScene().addChild(cube);
        cube.setRotY(cube.getRotY() + 45);
        cube.setRotY(cube.getRotX() + 45);

        getCurrentCamera().setZ(4.2f);
    }



    public void onTouchEvent(MotionEvent event){

    }

    public void onOffsetsChanged(float x, float y, float z, float w, int i, int j){

    }


}
