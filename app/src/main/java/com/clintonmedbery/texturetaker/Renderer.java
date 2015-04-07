package com.clintonmedbery.texturetaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.MotionEvent;

import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Cube;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.renderer.RajawaliRenderer;


/**
 * Created by clintonmedbery on 4/5/15.
 */
public class Renderer extends RajawaliRenderer {

    public DirectionalLight light;
    public Cube cube;
    public Sphere sphere;
    public Context context;

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
        light = new DirectionalLight(1f, 0.2f, -1.0f); // set the direction
        light.setColor(1.0f, 1.0f, 1.0f);
        light.setPower(2);
        getCurrentScene().addLight(light);

        cube = new Cube(2.0F);
        //sphere = new Sphere(1, 24, 24);


        Material material = new Material();
        material.setColor(0);


        material.enableLighting(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());

        Texture texture = new Texture("Texture", picture);
        try {
            material.addTexture(texture);
        } catch (ATexture.TextureException e){
            Log.d("Debug", "TEXTURE EXCEPTION");
        }
        getCurrentScene().addChild(cube);
        cube.setRotY(cube.getRotY() + 45);
        cube.setRotY(cube.getRotX() + 45);
        cube.setMaterial(material);


        getCurrentCamera().setZ(4.2f);
    }

    @Override
    public void onRender(final long elapsedTime, final double deltaTime) {
        super.onRender(elapsedTime, deltaTime);
        cube.rotate(Vector3.Axis.Y, 0.5);
    }

    public void onTouchEvent(MotionEvent event){


    }

    public void onOffsetsChanged(float x, float y, float z, float w, int i, int j){

    }


}
