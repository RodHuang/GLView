package com.asus.glview.mesh;

/**
 * Created by rod on 8/4/16.
 */

public class Triangle extends Mesh {


    public Triangle() {
        float[] vertices = new float[]{-0.5f, -0.25f, 0.0f, 0.5f, -0.25f, 0.0f, 0.0f, 0.559016994f, 0.0f};
        float[] normals = new float[]{0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f};
        short[] indices = new short[]{0, 1, 2};
        setIndices(indices);
        setVertices(vertices);
        setNormals(normals);
    }
}
