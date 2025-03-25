package com.base.engine.components;

import com.base.engine.core.Matrix4f;

public class ShadowInfo {
    private Matrix4f projection;
    private float bias;
    private boolean flipFace;

    public ShadowInfo(Matrix4f projection, float bias, boolean flipFace) {
        this.projection = projection;
        this.bias = bias;
        this.flipFace = flipFace;
    }

    //Getters
    public Matrix4f getProjection() {
        return projection;
    }

    public float getBias() {
        return bias;
    }

    public boolean isFlipFace() {
        return flipFace;
    }
}
