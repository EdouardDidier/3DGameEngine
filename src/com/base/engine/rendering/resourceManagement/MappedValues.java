package com.base.engine.rendering.resourceManagement;

import com.base.engine.core.Vector3f;
import com.base.engine.rendering.Texture;

import java.util.HashMap;

public abstract class MappedValues {
    private HashMap<String, Vector3f> vector3fHashMap;
    private HashMap<String, Float> floatHashMap;
    private HashMap<String, Texture> textureHashMap;

    public MappedValues() {
        vector3fHashMap = new HashMap<String, Vector3f>();
        floatHashMap = new HashMap<String, Float>();
        textureHashMap = new HashMap<String, Texture>();
    }

    public void setVector3f(String name, Vector3f vector3f) {vector3fHashMap.put(name, vector3f);}
    public void setFloat(String name, float floatValue) {floatHashMap.put(name, floatValue);}
    public void setTexture(String name, Texture texture) {textureHashMap.put(name, texture);}

    public Vector3f getVector3f(String name) {
        Vector3f result = vector3fHashMap.get(name);
        if(result != null)
            return result;

        return new Vector3f(0, 0, 0);
    }

    public float getFloat(String name) {
        Float result = floatHashMap.get(name);
        if(result != null)
            return result;

        return 0;
    }

    public Texture getTexture(String name) {
        Texture result = textureHashMap.get(name);
        if(result != null)
            return result;

        return new Texture("testTexture.png"); //TODO: Change to no texture
    }
}
