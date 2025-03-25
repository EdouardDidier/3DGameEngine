package com.base.engine.rendering;

import com.base.engine.core.Vector2f;
import com.base.engine.core.Vector3f;

public class Vertex {
    public static final int SIZE = 11;

    private Vector3f pos;
    private Vector2f textCoord;
    private Vector3f normal;
    private Vector3f tangent;

    public Vertex(Vector3f pos) {
        this(pos, new Vector2f(0, 0));
    }

    public Vertex(Vector3f pos, Vector2f textCoord) {
        this(pos, textCoord, new Vector3f(0, 0, 0));
    }
    public Vertex(Vector3f pos, Vector2f textCoord, Vector3f normal) {
        this(pos, textCoord, normal, new Vector3f(0, 0, 0));
    }

    public Vertex(Vector3f pos, Vector2f textCoord, Vector3f normal, Vector3f tangent) {
        this.pos = pos;
        this.textCoord = textCoord;
        this.normal = normal;
        this.tangent = tangent;
    }

    //Getters & Setters
    public Vector3f getPos() {
        return pos;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }


    public Vector2f getTextCoord() {
        return textCoord;
    }

    public void setTextCoord(Vector2f textCoord) {
        this.textCoord = textCoord;
    }

    public Vector3f getNormal() {
        return normal;
    }

    public void setNormal(Vector3f normal) {
        this.normal = normal;
    }

    public Vector3f getTangent() {
        return tangent;
    }

    public void setTangent(Vector3f tangent) {
        this.tangent = tangent;
    }
}
