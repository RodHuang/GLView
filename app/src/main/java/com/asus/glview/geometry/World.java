package com.asus.glview.geometry;

import android.opengl.Matrix;

/**
 * Created by root on 8/11/16.
 */

public class World extends Geometry {
    private float[] idendityM = new float[16];

    public World() {
        super(null);
        Matrix.setIdentityM(idendityM, 0);
    }

    public void draw(float[] matrixView, float[] matrixProjection) {
        super.draw(matrixView, matrixProjection, idendityM);
    }
}
