package com.asus.glview;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.asus.glview.mesh.Mesh;
import com.asus.glview.utilities.ShaderUtils;
import com.asus.glview.geometry.World;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by rod on 8/4/16.
 */

public class GLES20Renderer implements GLSurfaceView.Renderer {

    private final float[] mMatrixProjection = new float[16];
    private final float[] mMatrixView = new float[16];
    private final float[] mMatrixProjectionAndView = new float[16];
    private int mScreenWidth;
    private int mScreenHeight;

    final World world = new World();

    public GLES20Renderer() {
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        ShaderUtils.setupShader();
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glDisable(GLES20.GL_DITHER);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {

        GLES20.glClearColor(0f, 0f, 0f, 0f);
        for (int i = 0; i < 16; i++) {
            mMatrixProjection[i] = 0.0f;
            mMatrixView[i] = 0.0f;
            mMatrixProjectionAndView[i] = 0.0f;
        }
        mScreenWidth = width;
        mScreenHeight = height;
        GLES20.glViewport(0, 0, mScreenWidth, mScreenHeight);
        float ratio = (float) width / (float) height;
        Matrix.frustumM(mMatrixProjection, 0, -ratio, ratio, -1, 1, 1, 100);
        Mesh.setLightPos(new float[]{-15f, 15f, 15f});
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT
                | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        Matrix.setLookAtM(mMatrixView, 0, 0, 0, 3f, 0, 0,
                0f, 0, 1, 0.0f);

        world.draw(mMatrixView, mMatrixProjection);
    }

    public World getWorld() {
        return world;
    }

    public void draw() {
        world.draw(mMatrixView, mMatrixProjection);
    }
}
