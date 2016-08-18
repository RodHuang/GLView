package com.asus.glview.mesh;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.asus.glview.utilities.ShaderUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by rod on 8/4/16.
 */

public abstract class Mesh {

    private static int sCurrentTexture = -1;

    private int mTexture = -1;
    // Our vertex buffer.
    private FloatBuffer verticesBuffer = null;

    // Our index buffer.
    private ShortBuffer indicesBuffer = null;
    // Normal buffer
    private FloatBuffer normalBuffer = null;

    // The number of indices.
    private int numOfIndices = -1;
    private int numOfVertices = -1;

    // Flat Color
    private float[] rgba = new float[]{1.0f, 1.0f, 1.0f, 1.0f};

    private float specPow = 4f;
    private boolean useTexture = true;
    private boolean useLight = true;
    private float textureDiffuseLevel = .5f;
    // Smooth Colors
    private FloatBuffer colorBuffer = null;

    private static FloatBuffer lightBuffer = null;
    private FloatBuffer textureBuffer = null;
    private FloatBuffer ambientBuffer = null;
    private FloatBuffer diffuseBuffer = null;
    private FloatBuffer specBuffer = null;

    private float[] normalM = new float[]{1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};

    public void draw(float[] matrixView, float[] matrixProjection, float[] matrixModel) {
        if (sCurrentTexture == -1 || mTexture != sCurrentTexture) {
            sCurrentTexture = mTexture;
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, sCurrentTexture);
        }
        Log.d("Rod", "draw texture: " + sCurrentTexture);
        // Counter-clockwise winding.
//        GLES20.glFrontFace(GLES20.GL_CCW);
        // Enable face culling.
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        // What faces to remove with the face culling.
        GLES20.glCullFace(GLES20.GL_BACK);
        // Specifies the location and data format of an array of vertex
        // coordinates to use when rendering.
        GLES20.glVertexAttribPointer(ShaderUtils.sPositionHandle, 3, GLES20.GL_FLOAT,
                false, 0, verticesBuffer);
        GLES20.glVertexAttribPointer(ShaderUtils.sNormalHandle, 3, GLES20.GL_FLOAT,
                false, 0, normalBuffer);
        if (colorBuffer == null) {
            float[] colors = new float[numOfVertices * 4];
            for (int i = 0; i < numOfVertices; i++) {
                for (int j = 0; j < 4; j++) {
                    colors[i * 4 + j] = rgba[j];
                }
            }
            setColors(colors);
        }
        GLES20.glVertexAttribPointer(ShaderUtils.sColorHandle, 4, GLES20.GL_FLOAT, false,
                0, colorBuffer);

        // Coloring Uniforms
        if (lightBuffer == null) {
            setLightPos(new float[]{0f, 5f, 0f});
        }
        GLES20.glUniform3fv(ShaderUtils.sLightPosHandle, 1, lightBuffer);

        if (ambientBuffer == null) {
            setAmbientColor(new float[]{.5f, .5f, .5f});
        }
        GLES20.glUniform3fv(ShaderUtils.sAmbientColorHandle, 1, ambientBuffer);

        if (diffuseBuffer == null) {
            setDiffuseColor(new float[]{.5f, .5f, .5f});
        }
        GLES20.glUniform3fv(ShaderUtils.sDiffuseColorHandle, 1, diffuseBuffer);

        if (specBuffer == null) {
            setSpecColor(new float[]{1f, 1f, 1f});
        }
        GLES20.glUniform3fv(ShaderUtils.sSpecColorHandle, 1, specBuffer);

        GLES20.glUniform1f(ShaderUtils.sSpecPowHandle, specPow);
        GLES20.glUniform1i(ShaderUtils.sUseTextureHandle, useTexture ? 1 : 0);
        GLES20.glUniform1i(ShaderUtils.sUseLightHandle, useLight ? 1 : 0);
        GLES20.glUniform1f(ShaderUtils.sTextureDiffuseLevelHandle, textureDiffuseLevel);
        GLES20.glUniformMatrix4fv(ShaderUtils.sMMatrixHandle, 1, false, matrixModel, 0);
        GLES20.glUniformMatrix4fv(ShaderUtils.sVMatrixHandle, 1, false, matrixView, 0);
        // Normal matrix would be the invert and transpose of model matrix
        float[] temp = new float[16];
        Matrix.invertM(temp, 0, matrixModel, 0);
        Matrix.transposeM(normalM, 0, temp, 0);
        GLES20.glUniformMatrix4fv(ShaderUtils.sNMatrixHandle, 1, false, normalM, 0);
        GLES20.glUniformMatrix4fv(ShaderUtils.sPMatrixHandle, 1, false, matrixProjection, 0);

        GLES20.glVertexAttribPointer(ShaderUtils.sTexCoordLoc, 2, GLES20.GL_FLOAT, false,
                0, textureBuffer);
        GLES20.glUniform1i(ShaderUtils.sSamplerLoc, 0);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, numOfIndices,
                GLES20.GL_UNSIGNED_SHORT, indicesBuffer);
        // Disable face culling.
        GLES20.glDisable(GLES20.GL_CULL_FACE);
    }

    protected void setVertices(float[] vertices) {
        // a float is 4 bytes, therefore we multiply the number if
        // vertices with 4.
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        verticesBuffer = vbb.asFloatBuffer();
        verticesBuffer.put(vertices);
        verticesBuffer.position(0);
        numOfVertices = vertices.length;
    }

    protected void setIndices(short[] indices) {
        // short is 2 bytes, therefore we multiply the number if
        // vertices with 2.
        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indicesBuffer = ibb.asShortBuffer();
        indicesBuffer.put(indices);
        indicesBuffer.position(0);
        numOfIndices = indices.length;
    }

    protected void setColor(float red, float green, float blue, float alpha) {
        // Setting the flat color.
        rgba[0] = red;
        rgba[1] = green;
        rgba[2] = blue;
        rgba[3] = alpha;
    }

    protected void setColors(float[] colors) {
        // float has 4 bytes.
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        colorBuffer = cbb.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);
    }

    protected void setNormals(float[] normals) {
        // float has 4 bytes.
        ByteBuffer cbb = ByteBuffer.allocateDirect(normals.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        normalBuffer = cbb.asFloatBuffer();
        normalBuffer.put(normals);
        normalBuffer.position(0);
    }

    public static void setLightPos(float[] lightPos) {
        // float has 4 bytes.
        ByteBuffer cbb = ByteBuffer.allocateDirect(lightPos.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        lightBuffer = cbb.asFloatBuffer();
        lightBuffer.put(lightPos);
        lightBuffer.position(0);
    }

    public void setAmbientColor(float[] ambientColor) {
        // float has 4 bytes.
        ByteBuffer cbb = ByteBuffer.allocateDirect(ambientColor.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        ambientBuffer = cbb.asFloatBuffer();
        ambientBuffer.put(ambientColor);
        ambientBuffer.position(0);
    }

    public void setDiffuseColor(float[] diffuseColor) {
        // float has 4 bytes.
        ByteBuffer cbb = ByteBuffer.allocateDirect(diffuseColor.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        diffuseBuffer = cbb.asFloatBuffer();
        diffuseBuffer.put(diffuseColor);
        diffuseBuffer.position(0);
    }

    public void setSpecColor(float[] specColor) {
        // float has 4 bytes.
        ByteBuffer cbb = ByteBuffer.allocateDirect(specColor.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        specBuffer = cbb.asFloatBuffer();
        specBuffer.put(specColor);
        specBuffer.position(0);
    }

    public void setSpecPow(float specPow) {
        this.specPow = specPow;
    }

    public void setUseTexture(boolean useTexture) {
        this.useTexture = useTexture;
    }

    public void setUseLight(boolean useLight) {
        this.useLight = useLight;
    }

    public void setTextureDiffuseLevel(float textureDiffuseLevel) {
        this.textureDiffuseLevel = textureDiffuseLevel;
    }

    public void setTextureCoord(float[] textureCoord) {
        // float has 4 bytes.
        ByteBuffer cbb = ByteBuffer.allocateDirect(textureCoord.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        textureBuffer = cbb.asFloatBuffer();
        textureBuffer.put(textureCoord);
        textureBuffer.position(0);
    }

    public void setTexture(int texture) {
        mTexture = texture;
    }

    public int getTexture() {
        return mTexture;
    }

    @Override
    public String toString() {
        return "Mesh{" +
                "mTexture=" + mTexture +
                ", useLight=" + useLight +
                ", useTexture=" + useTexture +
                '}';
    }

    public static void resetBoundTexture() {
        sCurrentTexture = -1;
    }
}
