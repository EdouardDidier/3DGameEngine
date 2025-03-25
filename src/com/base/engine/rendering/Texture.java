package com.base.engine.rendering;

import com.base.engine.core.Util;
import com.base.engine.rendering.resourceManagement.TextureResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;

public class Texture {
    private static HashMap<String, TextureResource> loadedTextures = new HashMap<String, TextureResource>();
    private TextureResource resource;
    private String fileName;

    public Texture(String fileName, int textureTarget, int textureFilter, int internalFormat, int format, boolean clamp, int attachments) {
        this.fileName = fileName;

        TextureResource oldResource = loadedTextures.get(fileName);

        if(oldResource != null) {
            resource = oldResource;
            resource.addReference();
        }
        else {
            try {
                BufferedImage image = ImageIO.read(new File("./res/textures/" + fileName));
                int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

                ByteBuffer buffer = Util.createByteBuffer(image.getWidth() * image.getHeight() * 4);
                boolean hasAlpha = image.getColorModel().hasAlpha();

                for(int y = 0; y < image.getHeight(); y++) {
                    for(int x = 0; x < image.getWidth(); x++) {
                        int pixel = pixels[y * image.getWidth() + x];

                        buffer.put((byte)((pixel >> 16) & 0xFF));
                        buffer.put((byte)((pixel >> 8) & 0xFF));
                        buffer.put((byte)((pixel) & 0xFF));

                        if(hasAlpha)
                            buffer.put((byte)((pixel >> 24) & 0xFF));
                        else
                            buffer.put((byte)(0xFF));
                    }
                }

                buffer.flip();

                //TODO: Rempolacer avec TextureResource

                resource = new TextureResource(textureTarget, image.getWidth(), image.getHeight(), 1, new ByteBuffer[] {buffer}, new int[] {textureFilter}, new int[] {internalFormat}, new int[] {format}, clamp, new int[] {attachments});

/*
            glBindTexture(GL_TEXTURE_2D, resource.getId());

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);*/
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }

            loadedTextures.put(fileName, resource);
        }
    }

    public Texture(String fileName) {
        this(fileName, GL_TEXTURE_2D, GL_LINEAR, GL_RGBA, GL_RGBA, false, GL_NONE);
    }

    public Texture(int width, int height, ByteBuffer[] data, int textureTarget, int[] filter, int[] internalFormat, int[] format, boolean clamp, int[] attachment) {
        fileName = "";
        resource = new TextureResource(textureTarget, width, height, 1, data, filter, internalFormat, format, clamp, attachment);
    }

    public Texture(int width, int height, ByteBuffer data, int textureTarget, int filter, int internalFormat, int format, boolean clamp, int attachment) {
        this(width, height, new ByteBuffer[] {data}, textureTarget, new int[] {filter}, new int[] {internalFormat}, new int[] {format}, clamp, new int[] {attachment});
    }

    public Texture(int width, int height, ByteBuffer data, int textureTarget, int filter, int attachment) {
        this(width, height, data, textureTarget, filter, GL_RGBA, GL_RGBA, false, attachment);
    }

    public void bind() {
        bind(0);
    }

    public void bind(int samplerSlot) {
        assert(samplerSlot >= 0 && samplerSlot <= 31);
        glActiveTexture(GL_TEXTURE0 + samplerSlot);
        resource.bind(0);
    }

    public void bindAsRenderTarget() {
        resource.bindAsRenderTarget();
    }

    public int getID() {
        return resource.getId();
    }

    //Resource Loader
    private void loadTexture(String fileName) {

    }

    public void destroy() { //TODO: Improve
        if(resource.removeReference() && !fileName.isEmpty()) {
            loadedTextures.get(fileName).destroy();
            loadedTextures.remove(fileName); //TODO: Improve
        }
    }
}
