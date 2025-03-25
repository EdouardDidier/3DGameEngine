package com.base.engine.rendering;

import com.base.engine.rendering.resourceManagement.MappedValues;

import java.util.HashMap;

public class Material extends MappedValues {

    public Material(Texture diffuse, float specularIntensity, float specularPower, Texture normalMap, Texture dispMap, float dispMapScale, float dispMapOffset) {
        super();

        setTexture("diffuse", diffuse);
        setFloat("specularIntensity", specularIntensity);
        setFloat("specularPower", specularPower);

        setTexture("normalMap", normalMap);

        float baseBias = dispMapScale / 2.0f;
        setTexture("dispMap", dispMap);
        setFloat("dispMapScale", dispMapScale);
        setFloat("dispMapBias", -baseBias + baseBias * dispMapOffset);
    }

    public Material(Texture diffuse, float specularIntensity, float specularPower, Texture normalMap, Texture dispMap, float dispMapScale) {
        this(diffuse, specularIntensity, specularPower, normalMap, dispMap, dispMapScale, 0.0f);
    }

    public Material(Texture diffuse, float specularIntensity, float specularPower, Texture normalMap, Texture dispMap) {
        this(diffuse, specularIntensity, specularPower, normalMap, dispMap, 0.0f);
    }

    public Material(Texture diffuse, float specularIntensity, float specularPower, Texture normalMap) {
        this(diffuse, specularIntensity, specularPower, normalMap, new Texture("default_disp.png"));
    }

    public Material(Texture diffuse, float specularIntensity, float specularPower) {
        this(diffuse, specularIntensity, specularPower, new Texture("default_normal.png"));
    }
}
