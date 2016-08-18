package com.asus.glview.mesh;

/**
 * Created by root on 8/10/16.
 */

public class Sphere extends Mesh {

    public Sphere(float radius, int loSegments, int laSegments) {
        float[] vertices = new float[(loSegments + 1) * (laSegments + 1)
                * 3];
        float[] normals = new float[(loSegments + 1) * (laSegments + 1)
                * 3];
        float[] textureCoord = new float[(loSegments + 1) * (laSegments + 1)
                * 2];
        short[] indices = new short[(loSegments) * (laSegments)
                * 6];

        int currentVertex = 0;
        int currentIndex = 0;
        int currentTexture = 0;
        short w = (short) (loSegments + 1);
        for (int y = 0; y < laSegments + 1; y++) {
            for (int x = 0; x < loSegments + 1; x++) {
                float theta = (float) -(x * Math.PI * 2 / loSegments);
                float phi = (float) (y * Math.PI / laSegments - (Math.PI / 2));
                vertices[currentVertex] = normals[currentVertex] = (float) (radius * Math.cos(theta) * Math.cos(phi));
                vertices[currentVertex + 1] = normals[currentVertex + 1] = (float) (radius * Math.sin(theta) * Math.cos(phi));
                vertices[currentVertex + 2] = normals[currentVertex + 2] = (float) (radius * Math.sin(phi));
//                normals[currentVertex] = (float) (Math.cos(theta) * Math.cos(phi));
//                normals[currentVertex + 1] = (float) (Math.sin(theta) * Math.cos(phi));
//                normals[currentVertex + 2] = (float) (Math.sin(phi));
                textureCoord[currentTexture] = (float) x / (float) loSegments;
                textureCoord[currentTexture + 1] = (float) y / (float) laSegments;
                currentTexture += 2;
                currentVertex += 3;

                int n = y * (loSegments + 1) + x;

                if (y < laSegments && x < loSegments) {
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
