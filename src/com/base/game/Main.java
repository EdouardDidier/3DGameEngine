package com.base.game;

        import com.base.engine.core.CoreEngine;

public class Main {
    public static void main(String[] args) {
        CoreEngine engine = new CoreEngine(1600, 900, 120.0, new TestGame());
        engine.createWindow("3D Game Engine");
        engine.start();
    }
}
