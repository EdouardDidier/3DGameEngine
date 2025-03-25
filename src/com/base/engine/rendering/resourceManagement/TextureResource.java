package com.base.engine.rendering.resourceManagement;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

public class TextureResource {
    private int[] id;
    private int numTextures = 1;

    private int textureTarget;
    private int frameBuffer;
    private int renderBuffer;

    private int width;
    private int height;

    private int refCount;

    public TextureResource(int textureTarget, int width, int height, int numTextures, ByteBuffer[] data, int[] filters, int[] internalFormat, int[] format, boolean clamp, int[] attachments) {
        this.id = new int[numTextures];

        this.textureTarget = textureTarget;
        this.numTextures = numTextures;
        this.refCount = 1;

        this.width = width;
        this.height = height;

        initTextures(data, filters, internalFormat, format, clamp);
        initRenderTargets(attachments);
    }

    public TextureResource(int textureTarget, int width, int height, int numTextures, ByteBuffer[] data, int[] filters, int[] attachments) {
        this(textureTarget, width, height, numTextures, data, filters, new int[] {GL_RGBA}, new int[] {GL_RGBA}, false, attachments);
    }


    public void initTextures(ByteBuffer[] data, int[] filters, int[] internalFormat, int[] format, boolean clamp) {
        glGenTextures(this.id);

        for(int i = 0; i < numTextures; i++) {
            glBindTexture(textureTarget, this.id[i]);

            //glTexParameteri(textureTarget, GL_TEXTURE_WRAP_S, GL_REPEAT);
            //glTexParameteri(textureTarget, GL_TEXTURE_WRAP_T, GL_REPEAT);

            glTexParameteri(textureTarget, GL_TEXTURE_MIN_FILTER, filters[i]);
            glTexParameteri(textureTarget, GL_TEXTURE_MAG_FILTER, filters[i]);

            if (clamp) {
                glTexParameteri(textureTarget, GL_TEXTURE_WRAP_S, GL_CLAMP);
                glTexParameteri(textureTarget, GL_TEXTURE_WRAP_T, GL_CLAMP);
            }

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_BASE_LEVEL, 0);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 0);

            glTexImage2D(textureTarget, 0, internalFormat[i], width, height, 0, format[i], GL_UNSIGNED_BYTE, data[i]);
        }
    }

    public void initRenderTargets(int[] attachments) {
        if(attachments == null)
            return;

        int[] drawBuffers = new int[numTextures];
        boolean hasDepth = false;

        for(int i = 0; i < numTextures; i++) {
            if(attachments[i] == GL_DEPTH_ATTACHMENT) {
                drawBuffers[i] = GL_NONE;
                hasDepth = true;
            }
            else
                drawBuffers[i] = attachments[i];

            if(attachments[i] == GL_NONE)
                continue;

            if(frameBuffer == 0) {
                frameBuffer = glGenFramebuffers();
                glBindFramebuffer(GL_DRAW_FRAMEBUFFER, frameBuffer);
            }

            glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER, attachments[i], textureTarget, id[i], 0);
        }

        if(frameBuffer == 0)
            return;

        if(!hasDepth) {
            renderBuffer = glGenRenderbuffers();
            glBindRenderbuffer(GL_RENDERBUFFER, renderBuffer);
            glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
            glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renderBuffer);
            System.out.println("test:" + renderBuffer + " " + frameBuffer + " " + width + " " + height);
        }

        glDrawBuffers(drawBuffers);

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("Error: Framebuffer creation failed!");
            new Exception().printStackTrace();
            System.exit(1);
        }
    }

    public void bind(int textureNum) {
        glBindTexture(GL_TEXTURE_2D, this.id[textureNum]);
    }

    public void bindAsRenderTarget() {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, frameBuffer);
        glViewport(0, 0, width, height);
    }

    public void destroy() {
        if(frameBuffer != 0) glDeleteBuffers(frameBuffer);
        if(renderBuffer != 0) glDeleteBuffers(renderBuffer);
        glDeleteBuffers(this.id);
    }

    public void addReference() {
        refCount++;
    }

    public boolean removeReference() {
        refCount--;
        return refCount == 0;
    }

    //Getters & Setters
    public int getId() {
        return id[0];
    }
}
