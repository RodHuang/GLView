package com.example.rod.glview;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.asus.glview.GLView;
import com.asus.glview.geometry.Geometry;
import com.asus.glview.geometry.World;
import com.asus.glview.mesh.Sphere;
import com.asus.glview.utilities.TextureUtils;

public class MainActivity extends AppCompatActivity {

    GLView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Rod", "onCreate");
        super.onCreate(savedInstanceState);
        if (hasGLES20()) {
        } else {
            // Time to get a new phone, OpenGL ES 2.0 not
            // supported.
        }
//        setContentView(mGLView);
        setContentView(R.layout.activity_main);
        mGLView = (GLView) findViewById(R.id.container);
        final View v = findViewById(R.id.textt);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawScene(view);
            }
        });
        final View vv = findViewById(R.id.texttt);
        vv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawScene(view);
            }
        });
    }

    @Override
    protected void onPause() {
        Log.d("Rod", "onPause");
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        Log.d("Rod", "onResume");
        super.onResume();
        mGLView.onResume();
    }

    private boolean hasGLES20() {
        ActivityManager am = (ActivityManager)
                getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        return info.reqGlEsVersion >= 0x20000;
    }

    public void drawScene(final View view) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                World world = mGLView.getWorld();
                Sphere s = new Sphere(1f, 48, 24);
                s.setTexture(TextureUtils.generateTextureFromView(new View[]{view})[0]);
                Log.d("Rod", "Texture: " + s.getTexture());
                s.setUseTexture(true);
                Geometry g = new Geometry(s);
                g.identityMatrixModel();
                g.scale(.1f, .1f, .1f);
//                g.rotateX((float) Math.random() * 360f);
                g.translate(((float) Math.random() - .5f) * 3f, ((float) Math.random() - .5f) * 3f, ((float) Math.random() - .5f) * 3f);
                world.add(g);
                mGLView.requestRender();
            }
        };
        runOnGLThread(r);
    }

    public void runOnGLThread(final Runnable r) {
        mGLView.queueEvent(r);
    }
}
