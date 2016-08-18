package com.asus.glview;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by root on 8/17/16.
 */

public class TouchHelper {

    private static final int FRAMEBUFFER_SIZE = 500;
    private GLView mGLView;
    private int mFrameBuffer = -1;
    private ByteBuffer mPixelBuffer = ByteBuffer.allocateDirect(4);

    public TouchHelper(GLView glView) {
        mGLView = glView;
        generateFrameBuffer();
    }

    public void readPixalAt(final int x, final int y) {
        mGLView.queueEvent(new Runnable() {
            @Override
            public void run() {
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffer);

                mPixelBuffer.order(ByteOrder.nativeOrder());
                mPixelBuffer.position(0);
                GLES20.glReadPixels(x, mGLView.getHeight() - y,
                        1, 1, GLES20
                                .GL_RGBA, GLES20.GL_UNSIGNED_BYTE, mPixelBuffer);
                mPixelBuffer.rewind();
                int r = mPixelBuffer.get(0) & 0xff;
                int g = mPixelBuffer.get(1) & 0xff;
                int b = mPixelBuffer.get(2) & 0xff;
                int a = mPixelBuffer.get(3) & 0xff;
                Log.d("QQ", r + " " + g + " " +
                        b + " " + a);

                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
            }
        });
    }

    public void refreshHiddenBuffer() {

        mGLView.queueEvent(new Runnable() {
            @Override
            public void run() {
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffer);

                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
                mGLView.draw();

                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
            }
        });
    }

    private void generateFrameBuffer() {
        mGLView.queueEvent(new Runnable() {
            @Override
            public void run() {
                int[] renderBuffer = new int[1];
                GLES20.glGenRenderbuffers(renderBuffer.length, renderBuffer, 0);
                GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, renderBuffer[0]);
                GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_RGB,
                        FRAMEBUFFER_SIZE, FRAMEBUFFER_SIZE);
                GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, 0);

                int[] frameBuffer = new int[1];
                GLES20.glGenFramebuffers(frameBuffer.length, frameBuffer, 0);
                mFrameBuffer = frameBuffer[0];
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffer);
                GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20
                        .GL_COLOR_ATTACHMENT0, GLES20.GL_RENDERBUFFER, renderBuffer[0]);
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
            }
        });

    }
}
