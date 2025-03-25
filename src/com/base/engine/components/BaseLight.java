package com.base.engine.components;

import com.base.engine.core.CoreEngine;
import com.base.engine.core.Vector3f;
import com.base.engine.rendering.Shader;

public class BaseLight extends GameComponent {
    private Vector3f color;
    private float intensity;
    private Shader shader;
    private ShadowInfo shadowInfo;

    public BaseLight(Vector3f color, float intensity) {
        this.color = color;
        this.intensity = intensity;
    }

    @Override
    public void addToEngine(CoreEngine engine) {
        engine.getRenderingEngine().addLight(this);
    }

    //Getters & Setters
    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public void setShader (Shader shader) {
        this.shader = shader;
    }

    public Shader getShader() {
        return shader;
    }

    protected void setShadowInfo(ShadowInfo shadowInfo) {
        this.shadowInfo = shadowInfo;
    }

    public ShadowInfo getShadowInfo() {
        return shadowInfo;
    }
}
