package com.asus.glview.mesh;

/**
 * Created by root on 8/9/16.
 */

public class Cylinder extends Mesh {

    public Cylinder() {
        this(1f, 1f, 16 ,16);
    }
    public Cylinder(float radius, float height, int roundSegments, int heightSegments) {
        float[] vertices = new float[(roundSegments + 1) * (heightSegments + 1)
                * 3];
        float[] normals = new float[(roundSegments + 1) * (heightSegments + 1)
                * 3];
        float[] textureCoord = new float[(roundSegments + 1) * (heightSegments + 1)
                * 2];
        short[] indices = new short[(roundSegments) * (heightSegments)
                * 6];

        float yOffset = height / 2;
        float yHeight = height / (heightSegments);
        int currentVertex = 0;
        int currentIndex = 0;
        int currentTexture = 0;
        short w = (short) (roundSegments + 1);
        for (int y = 0; y < heightSegments + 1; y++) {
            for (int x = 0; x < roundSegments + 1; x++) {
                float theta = (float) (x * Math.PI * 2 / roundSegments);
                vertices[currentVertex] = (float) (radius * Math.cos(theta));
                vertices[currentVertex + 1] = (float) (radius * Math.sin(theta));
                vertices[currentVertex + 2] = yOffset - y * yHeight;
                normals[currentVertex] = (float) (radius * Math.cos(theta));
                normals[currentVertex + 1] = (float) (radius * Math.sin(theta));
                normals[currentVertex + 2] = 0f;
                textureCoord[currentTexture] = 1f - (float) x / (float) roundSegments;
                textureCoord[currentTexture + 1] = 1f - (float) y / (float) heightSegments;
                currentTexture += 2;
                currentVertex += 3;

                int n = y * (roundSegments + 1) + x;

                if (y < heightSegments && x < roundSegments) {
                    // Face one
                    indices[currentIndex] = (short) n;
                    indices[currentIndex + 1] = (short) (n + w);
                    indices[currentIndex + 2] = (short) (n + 1 + w);
                    // Face two
                    indices[currentIndex + 3] = (short) n;
                    indices[currentIndex + 4] = (short) (n + 1 + w);
                    indices[currentIndex + 5] = (short) (n + 1);

                    currentIndex += 6;
                }
            }
        }

        setIndices(indices);
        setVertices(vertices);
        setNormals(normals);
        setTextureCoord(textureCoord);
    }
}
