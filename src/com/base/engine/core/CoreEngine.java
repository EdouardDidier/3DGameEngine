package com.base.engine.core;

import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Window;

public class CoreEngine {
    private boolean isRunning;
    private Game game;
    private RenderingEngine renderingEngine;
    private int width;
    private int height;
    private double frameTime;

    public CoreEngine(int width, int height, double frameRate, Game game) {
        this.isRunning = false;
        this.game = game;
        this.width = width;
        this.height = height;
        this.frameTime = 1.0/frameRate;

        game.setEngine(this);
    }

    public void createWindow(String title) {
        Window.createWindow(width, height, title);
        this.renderingEngine = new RenderingEngine();
    }

    public void start() {
        if (isRunning)
            return;

        run();
    }

    public void stop() {
        if (!isRunning)
            return;

        isRunning = false;
    }

    public void run() {
        isRunning = true;

        int frames = 0;
        double frameCounter = 0;

        game.init();

        double lastTime = Time.getTime();
        double unprocessedTime = 0;

        while(isRunning) {
            boolean render = false;

            double startTime = Time.getTime();
            double passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime;
            frameCounter += passedTime;

            while(unprocessedTime > frameTime) {
                render = true;

                unprocessedTime -= frameTime;

                if (Window.isCloseRequested()) stop();

                game.input((float)frameTime);
                Input.update();

                game.update((float)frameTime);

                //Display frame counter
                if (frameCounter >= 1.0f) {
                    System.out.println(frames);
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if (render) {
                game.render(renderingEngine);
                Window.render();
                frames++;
            }
            else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        cleanUp();
    }

    public void cleanUp() {
        Window.dispose();
    }

    public RenderingEngine getRenderingEngine() {
        return renderingEngine;
    }
}
