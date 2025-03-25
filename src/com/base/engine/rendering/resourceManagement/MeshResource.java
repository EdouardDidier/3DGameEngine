package com.base.engine.rendering.resourceManagement;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

public class MeshResource {
    private int vbo;
    private int ibo;
    private int size;
    private int refCount;

    public MeshResource(int size) {
        this.vbo = glGenBuffers();
        this.ibo = glGenBuffers();
        this.size = size;
        this.refCount = 1;
    }

    public void destroy() {
        glDeleteBuffers(vbo);
        glDeleteBuffers(ibo);
    }

    public void addReference() {
        refCount++;
    }

    public boolean removeReference() {
        refCount--;
        return refCount == 0;
    }

    //Getters & Setters
    public int getVbo() {
        return vbo;
    }

    public int getIbo() {
        return ibo;
    }

    public int getSize() {
        return size;
    }
}
