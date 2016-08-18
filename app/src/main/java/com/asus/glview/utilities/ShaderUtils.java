package com.asus.glview.utilities;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Created by rod on 8/4/16.
 */

public class ShaderUtils {
    public static final String TAG = "ShaderUtils";

    public static int sShaderProgram;

    public static int sPositionHandle;
    public static int sTexCoordLoc;
    public static int sColorHandle;
    public static int sPMatrixHandle;
    public static int sVMatrixHandle;
    public static int sMMatrixHandle;
    public static int sNMatrixHandle;
    public static int sLightPosHandle;
    public static int sNormalHandle;
    public static int sSamplerLoc;
    public static int sAmbientColorHandle;
    public static int sDiffuseColorHandle;
    public static int sSpecColorHandle;
    public static int sSpecPowHandle;
    public static int sUseTextureHandle;
    public static int sUseLightHandle;
    public static int sTextureDiffuseLevelHandle;

    private static final String _vertexShader =
            "uniform mat4 u_PMatrix;                                             \n"
                    + "uniform mat4 u_VMatrix;                                            \n"
                    + "uniform mat4 u_MMatrix;                                            \n"
                    + "uniform mat4 u_NMatrix;                                            \n"

                    + "attribute vec4 a_Position;                                          \n"
                    + "attribute vec4 a_Color;                                             \n"
                    + "attribute vec3 a_Normal;                                            \n"
                    + "attribute vec2 a_textureCoord;                                      \n"

                    + "varying vec3 v_Position;                                            \n"
                    + "varying vec4 v_Color;                                               \n"
                    + "varying vec3 v_Normal;                                              \n"
                    + "varying vec2 v_textureCoord;                                         \n"

                    + "void main()                                                         \n"
                    + "{                                                                   \n"
                    + "   v_Position = vec3(u_VMatrix * u_MMatrix * a_Position);            \n"
                    + "   v_Color = a_Color;                                               \n"
                    + "   v_Normal = vec3(u_NMatrix * vec4(a_Normal, 0.0));               \n"
                    + "   v_textureCoord = a_textureCoord;                                  \n"
                    + "   gl_Position = u_PMatrix                                        \n"
                    + "               * u_VMatrix                                       \n"
                    + "               * u_MMatrix                                       \n"
                    + "               * a_Position;                                        \n"
                    + "}                                                                   \n";
    private static final String _fragmentShader =
            "precision mediump float;                                              \n"

                    + "uniform vec3 u_LightPos;                                             \n"
                    + "uniform mat4 u_VMatrix;                                            \n"
                    + "uniform sampler2D s_texture;\n"
                    + "uniform vec3 u_AmbientColor;\n"
                    + "uniform vec3 u_DiffuseColor; \n"
                    + "uniform vec3 u_SpecColor; \n"
                    + "uniform float u_SpecPow;\n"
                    + "uniform int u_UseTexture;\n"
                    + "uniform int u_UseLight;\n"
                    + "uniform float u_TextureDiffuseLevel;\n"

                    + "varying vec4 v_Color;                                               \n"
                    + "varying vec3 v_Position;                                            \n"
                    + "varying vec3 v_Normal;                                              \n"
                    + "varying vec2 v_textureCoord;                                         \n"
                    + "void main()                                                         \n"
                    + "{                                                                   \n"
                    + "   vec3 lightPos = vec3(u_VMatrix * vec4(u_LightPos, 0.0));"
                    + "   vec3 normal = normalize(v_Normal);                \n"
                    + "   vec3 lightDir = normalize(lightPos - v_Position);           \n"
                    + "   vec3 reflectDir = reflect(-lightDir, normal); \n"
                    + "   vec3 viewDir = normalize(-v_Position);            \n"
                    + "   float lambertian = 0.0;\n"
                    + "   float textureDiffuseLevel = 0.0;\n"
                    + "   if (u_UseLight == 1) {\n"
                    + "      lambertian = max(dot(lightDir,normal), 0.0);\n"
                    + "      textureDiffuseLevel = u_TextureDiffuseLevel;\n"
                    + "   } \n"
                    + "   float specular = 0.0;\n"
                    + "   if(lambertian > 0.0) {\n"
                    + "      float specAngle = max(dot(reflectDir, viewDir), 0.0);\n"
                    + "      specular = pow(specAngle, u_SpecPow);\n"
                    + "   }\n"
                    + "   vec4 texColor = texture2D(s_texture, v_textureCoord);\n"
                    + "   vec3 ambientColor = u_AmbientColor;\n"
                    + "   vec3 diffuseColor = u_DiffuseColor;\n"
                    + "   if (u_UseTexture == 1) {\n"
                    + "      ambientColor = vec3(texColor) * (1.0 - u_TextureDiffuseLevel);\n"
                    + "      diffuseColor = vec3(texColor) * u_TextureDiffuseLevel;\n"
                    + "   }\n"
                    + "   gl_FragColor = vec4(ambientColor +  \n"
                    + "   lambertian * diffuseColor + \n"
                    + "   specular * u_SpecColor, texColor.w); \n"
//                    + "   gl_FragColor = vec4(lambertian * u_DiffuseColor, 1.0);"
//                    + "   gl_FragColor = texture2D(s_texture, v_textureCoord);\n"
                    + "}                                                                   \n";

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            Log.d("Rod", "Could not compile shader " + type + ":");
            Log.d("Rod", GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            shader = 0;
        }
        // return the shader
        return shader;
    }


    public static void initShaderHandles() {

        sPositionHandle = GLES20.glGetAttribLocation(sShaderProgram,
                "a_Position");
        sTexCoordLoc = GLES20.glGetAttribLocation(sShaderProgram,
                "a_textureCoord");
        sColorHandle = GLES20.glGetAttribLocation(sShaderProgram,
                "a_Color");
        sNormalHandle = GLES20.glGetAttribLocation(sShaderProgram,
                "a_Normal");
        sPMatrixHandle = GLES20.glGetUniformLocation(sShaderProgram,
                "u_PMatrix");
        sVMatrixHandle = GLES20.glGetUniformLocation(sShaderProgram,
                "u_VMatrix");
        sMMatrixHandle = GLES20.glGetUniformLocation(sShaderProgram,
                "u_MMatrix");
        sNMatrixHandle = GLES20.glGetUniformLocation(sShaderProgram,
                "u_NMatrix");
        sLightPosHandle = GLES20.glGetUniformLocation(sShaderProgram,
                "u_LightPos");
        sSamplerLoc = GLES20.glGetUniformLocation(sShaderProgram,
                "s_texture");
        sAmbientColorHandle = GLES20.glGetUniformLocation(sShaderProgram,
                "u_AmbientColor");
        sDiffuseColorHandle = GLES20.glGetUniformLocation(sShaderProgram,
                "u_DiffuseColor");
        sSpecColorHandle = GLES20.glGetUniformLocation(sShaderProgram,
                "u_SpecColor");
        sSpecPowHandle = GLES20.glGetUniformLocation(sShaderProgram,
                "u_SpecPow");
        sUseTextureHandle = GLES20.glGetUniformLocation(sShaderProgram,
                "u_UseTexture");
        sUseLightHandle = GLES20.glGetUniformLocation(sShaderProgram,
                "u_UseLight");
        sTextureDiffuseLevelHandle = GLES20.glGetUniformLocation(sShaderProgram,
                "u_TextureDiffuseLevel");

        GLES20.glEnableVertexAttribArray(sPositionHandle);
        GLES20.glEnableVertexAttribArray(sNormalHandle);
        GLES20.glEnableVertexAttribArray(sColorHandle);
        GLES20.glEnableVertexAttribArray(sTexCoordLoc);
    }

    public static void setupShader() {
        cleanup();
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
                _vertexShader);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
                _fragmentShader);

        // Shader for original scene
        sShaderProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(sShaderProgram, vertexShader);
        GLES20.glAttachShader(sShaderProgram, fragmentShader);
        GLES20.glLinkProgram(sShaderProgram);
        GLES20.glUseProgram(sShaderProgram);

        initShaderHandles();
    }

    public static void cleanup() {
        GLES20.glDisableVertexAttribArray(sPositionHandle);
        GLES20.glDisableVertexAttribArray(sColorHandle);
        GLES20.glDisableVertexAttribArray(sNormalHandle);
        GLES20.glDisableVertexAttribArray(sTexCoordLoc);
        GLES20.glDeleteShader(sShaderProgram);
    }
}
