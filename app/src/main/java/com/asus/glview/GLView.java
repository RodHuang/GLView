package com.asus.glview;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.asus.glview.geometry.World;

/**
 * Created by root on 8/12/16.
 */

public class GLView extends GLSurfaceView {
    private GLES20Renderer mRenderer;
    private float mTouchDownX;
    private float mTouchDownY;
    private TouchHelper mTouchHelper;

    public GLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRenderer = new GLES20Renderer();
        setEGLContextClientVersion(2);
        setPreserveEGLContextOnPause(true);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setZOrderOnTop(true);
        setRenderer(mRenderer);
        getHolder().setFormat(PixelFormat.RGBA_8888);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        mTouchHelper = new TouchHelper(this);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Log.d("QQ", "onTouchEvent action: " + action);
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mTouchDownX = x;
                mTouchDownY = y;
                Log.d("QQ", "ACTION_DOWN: (" + x + ", " + y + ")");
                mTouchHelper.refreshHiddenBuffer();
                mTouchHelper.readPixalAt((int) x, (int) y);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("QQ", "ACTION_MOVE: (" + (x - mTouchDownX) + ", " + (y - mTouchDownY) + ")");
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.d("QQ", "ACTION_UP: (" + x + ", " + y + ")");
                break;
        }
        return true;
    }

    public World getWorld() {
        return mRenderer.getWorld();
    }

    public void draw() {
        mRenderer.draw();
    }
}
