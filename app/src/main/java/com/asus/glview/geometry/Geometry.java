package com.asus.glview.geometry;

import android.opengl.Matrix;

import com.asus.glview.mesh.Mesh;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 8/11/16.
 */

public class Geometry {
    private Mesh mMesh;
    private float[] mLocalMatrix = new float[16];
    private float[] tempM = new float[16];
    private List<Geometry> mChildren = new ArrayList<>();

    public Geometry(Mesh mesh) {
        mMesh = mesh;
        Matrix.setIdentityM(mLocalMatrix, 0);
    }

    public void add(Geometry child) {
        mChildren.add(child);
    }

    public void remove(Geometry child) {
        mChildren.remove(child);
    }

    public void clear() {
        mChildren.clear();
    }

    public void translate(float x, float y, float z) {
        Matrix.setIdentityM(tempM, 0);
        Matrix.translateM(tempM, 0, x, y, z);
        Matrix.multiplyMM(mLocalMatrix, 0, tempM, 0, mLocalMatrix, 0);
    }

    public void rotateX(float angle) {
        Matrix.setIdentityM(tempM, 0);
        Matrix.rotateM(tempM, 0, angle, 1f, 0f, 0f);
        Matrix.multiplyMM(mLocalMatrix, 0, tempM, 0, mLocalMatrix, 0);
    }

    public void rotateY(float angle) {
        Matrix.setIdentityM(tempM, 0);
        Matrix.rotateM(tempM, 0, angle, 0f, 1f, 0f);
        Matrix.multiplyMM(mLocalMatrix, 0, tempM, 0, mLocalMatrix, 0);
    }

    public void rotateZ(float angle) {
        Matrix.setIdentityM(tempM, 0);
        Matrix.rotateM(tempM, 0, angle, 0f, 0f, 1f);
        Matrix.multiplyMM(mLocalMatrix, 0, tempM, 0, mLocalMatrix, 0);
    }

    public void scale(float x, float y, float z) {
        Matrix.setIdentityM(tempM, 0);
        Matrix.scaleM(tempM, 0, x, y, z);
        Matrix.multiplyMM(mLocalMatrix, 0, tempM, 0, mLocalMatrix, 0);
    }

    public void identityMatrixModel() {
        Matrix.setIdentityM(mLocalMatrix, 0);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        if (!mChildren.isEmpty()) {
            b.append("Childern: ");
            for (Geometry g : mChildren) {
                b.append(g.toString());
            }
        }
        return "Geometry{" +
                "mMesh=" + mMesh + " " + b +
                '}';
    }

    public void draw(float[] matrixView, float[] matrixProjection, float[] matrixParent) {
        Matrix.multiplyMM(tempM, 0, matrixParent, 0, mLocalMatrix, 0);
        if (mMesh != null) {
            mMesh.draw(matrixView, matrixProjection, tempM);
        }
        if (!mChildren.isEmpty()) {
            for (Geometry geometry : mChildren) {
                geometry.draw(matrixView, matrixProjection, tempM);
            }
        }
    }
}
