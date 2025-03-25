package com.base.engine.components;

import com.base.engine.core.Matrix4f;
import com.base.engine.core.Vector3f;
import com.base.engine.rendering.Shader;

public class DirectionalLight extends BaseLight {
    public DirectionalLight(Vector3f color, float intensity) {
        super(color, intensity);

        setShader(new Shader("forward-directional"));

        setShadowInfo(new ShadowInfo(new Matrix4f().initOrthographic(-40, 40, -40, 40, -40, 40), 5.0f, true)); //TODO: Change resolution with dynamic attribution
    }

    //Getters & Setters
    public Vector3f getDirection() {
        return getTransform().getTransformedRot().getForward();
    }
}
