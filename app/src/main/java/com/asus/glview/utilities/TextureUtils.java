package com.asus.glview.utilities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.asus.glview.mesh.Mesh;

/**
 * Created by root on 8/9/16.
 */

public class TextureUtils {

    public static int[] generateTextureFromView(View[] views) {

        Log.d("Rod", "generateTextureFromView");
        Mesh.resetBoundTexture();
        int count = views.length;
        int[] textures = new int[count];
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glGenTextures(count, textures, 0);
        for (int i = 0; i < count; i++) {
            Log.d("Rod", "gen texture: " + textures[i]);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[i]);
            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

            // Set wrapping mode
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

            // Load the bitmap into the bound texture.
            Bitmap b = loadBitmapFromView(views[i]);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, b, 0);
            b.recycle();
        }
        return textures;
    }

    public static Bitmap loadBitmapFromView(View v) {
        if (v.getMeasuredHeight() <= 0) {
            v.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(c);
            return b;
        }
        Log.d("Rod", v.getWidth() + " " + v.getHeight() + " " + v.getLayoutParams().width + " " + v.getLayoutParams().height);
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }

    public void deleteTextures(int[] textures) {
        GLES20.glDeleteTextures(textures.length, textures, 0);
    }
}
