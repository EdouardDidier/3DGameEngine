package com.base.game;

import com.base.engine.components.*;
import com.base.engine.core.*;
import com.base.engine.rendering.*;
import com.base.game.components.ContinueRotation;
import com.base.game.components.LookAtComponent;

import static org.lwjgl.glfw.GLFW.*;

public class TestGame extends Game {
    public TestGame() {

    }

    public void init() {
        float fieldDepth = 10.0f;
        float fieldWidth = 10.0f;

        Vertex[] vertices = new Vertex[] {
                new Vertex(new Vector3f(-fieldWidth, 0.0f, -fieldDepth), new Vector2f(0.0f, 1.0f), new Vector3f(0.0f,1.0f, 0.0f)),
                new Vertex(new Vector3f(-fieldWidth, 0.0f, fieldDepth * 3), new Vector2f(0.0f, 0.0f), new Vector3f(0.0f,1.0f, 0.0f)),
                new Vertex(new Vector3f(fieldWidth * 3, 0.0f, -fieldDepth), new Vector2f(1.0f, 1.0f), new Vector3f(0.0f,1.0f, 0.0f)),
                new Vertex(new Vector3f(fieldWidth * 3, 0.0f, fieldDepth * 3), new Vector2f(1.0f, 0.0f), new Vector3f(0.0f,1.0f, 0.0f))
        };

        Vertex[] vertices2 = new Vertex[]{
                new Vertex(new Vector3f(-fieldWidth / 10, 0.0f, -fieldDepth / 10), new Vector2f(0.0f, 1.0f), new Vector3f(0.0f,1.0f, 0.0f)),
                new Vertex(new Vector3f(-fieldWidth / 10, 0.0f, fieldDepth * 3 / 10), new Vector2f(0.0f, 0.0f), new Vector3f(0.0f,1.0f, 0.0f)),
                new Vertex(new Vector3f(fieldWidth * 3 / 10, 0.0f, -fieldDepth / 10), new Vector2f(1.0f, 1.0f), new Vector3f(0.0f,1.0f, 0.0f)),
                new Vertex(new Vector3f(fieldWidth * 3 / 10, 0.0f, fieldDepth * 3 / 10), new Vector2f(1.0f, 0.0f), new Vector3f(0.0f,1.0f, 0.0f))
        };

        int[] indices = new int[] {
                0, 1, 2,
                2, 1, 3
        };

        Mesh mesh = new Mesh(vertices, indices, false);
        Mesh mesh2 = new Mesh("plane3.obj");
        Mesh cubeMesh = new Mesh("cubeUV.obj");
        Mesh monkeyMesh = new Mesh("monkey.obj");
        Mesh bricksMesh = new Mesh("cube.obj");

        Material material = new Material(new Texture("pinkTest2x.png"), 1, 8);
        Material material2 = new Material(new Texture("testTexture.png"), 1, 8);
        Material materialCube = new Material(new Texture("cubeText.png"), 1, 32);
        Material materialMonkey = new Material(new Texture("textMonkey.png"), 0.8f, 1);

        Material materialBrick = new Material(
                new Texture("bricks.png"), 0.5f, 4,
                new Texture("bricks_normal.png"),
                new Texture("bricks_disp.png"), 0.03f, -0.3f);

        Material materialBrickOff = new Material(
                new Texture("bricks.png"), 0.5f, 4);

        Material materialBrick2 = new Material(
                new Texture("bricks2.png"), 0.5f, 4,
                new Texture("bricks2_normal.png"),
                new Texture("bricks2_disp.png"), 0.04f, -0.5f);

        GameObject planeObject = new GameObject();
        planeObject.addComponent(new MeshRenderer(mesh, materialBrick2));
        planeObject.getTransform().getPos().set(0, -1, 5);

        GameObject directionalLightObject = new GameObject();
        DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1,1,1), 0.4f);
        directionalLightObject.addComponent(directionalLight);

        directionalLight.getTransform().setRot(new Quaternion(new Vector3f(1, -1, 0), (float)Math.toRadians(-45)));
//        directionalLightObject.addComponent(new ContinueRotation());
        directionalLightObject.addComponent(new FreeLook(false));

        GameObject pointLightObject = new GameObject();
        PointLight pointLight = new PointLight(new Vector3f(0, 1, 0), 0.4f,
                new Attenuation(0, 0, 1));
        pointLightObject.addComponent(pointLight);

        GameObject spotLightObject = new GameObject();
        SpotLight spotLight = new SpotLight(new Vector3f(1, 0, 0), 0.9f,
                new Attenuation(0, 0, 0.1f), 0.7f);
        spotLightObject.addComponent(spotLight);

        spotLightObject.getTransform().getPos().set(2, 5, 15);
        spotLightObject.getTransform().setRot(new Quaternion(new Vector3f(0, 1, 0), (float)Math.toRadians(90.0f)));
        spotLightObject.getTransform().rotate(spotLightObject.getTransform().getRot().getRight(), (float)Math.toRadians(45.0f));

        addObject(planeObject);
        addObject(directionalLightObject);
//        addObject(pointLightObject);
//        addObject(spotLightObject);

        GameObject testMesh1 = new GameObject().addComponent(new MeshRenderer(mesh2, material2));
        GameObject testMesh2 = new GameObject().addComponent(new MeshRenderer(mesh2, material));

        GameObject testMeshCube = new GameObject()
                .addComponent(new MeshRenderer(cubeMesh, materialCube))
                .addComponent(new ContinueRotation());

        GameObject bricksObject = new GameObject()
                .addComponent(new MeshRenderer(bricksMesh, materialBrick))
                .addComponent(new FreeMove(GLFW_KEY_8, GLFW_KEY_5, GLFW_KEY_4, GLFW_KEY_6, GLFW_KEY_9, GLFW_KEY_7));
                //.addComponent(new ContinueRotation());

        GameObject bricks2Object = new GameObject()
                .addComponent(new MeshRenderer(bricksMesh, materialBrick2));

        GameObject bricks3Object = new GameObject()
                .addComponent(new MeshRenderer(bricksMesh, materialBrickOff));

        GameObject testMeshMonkey = new GameObject()
                .addComponent(new MeshRenderer(monkeyMesh, materialMonkey))
                .addComponent(new LookAtComponent())
                .addComponent(new FreeMove(GLFW_KEY_I, GLFW_KEY_K, GLFW_KEY_J, GLFW_KEY_L, GLFW_KEY_O, GLFW_KEY_U));

        GameObject monkey2 = new GameObject().addComponent(new MeshRenderer(monkeyMesh, materialBrick2));

        GameObject camera = new GameObject()
                .addComponent(new FreeMove())
                .addComponent(new FreeLook(true))
                .addComponent(new Camera((float)Math.toRadians(70.0f), (float)Window.getWidth() / (float)Window.getHeight(), 0.01f, 1000.0f));

        testMesh1.addChild(testMesh2);

        addObject(testMesh1);
        addObject(testMeshMonkey);
        addObject(testMeshCube);
        addObject(bricksObject);
        addObject(bricks2Object);
        addObject(bricks3Object);
        addObject(monkey2);

        addObject(camera);

        testMesh1.getTransform().getPos().set(0, 2, 0);
        testMesh1.getTransform().setScale(new Vector3f(0.3f, 1.0f, 0.3f));
        testMesh1.getTransform().setRot(new Quaternion(new Vector3f(0, 1, 0), 0.4f));
        testMesh2.getTransform().getPos().set(0, 0, 20);

        bricksObject.getTransform().getPos().set(6, 0, 10);
        bricks3Object.getTransform().getPos().set(4, 0, 10);

        bricks2Object.getTransform().getPos().set(15, 0, 10);
        bricks2Object.getTransform().rotate(new Vector3f(1, 0, 0), (float)Math.toRadians(90));

        testMeshCube.getTransform().getPos().set(5, 2, 15);
        testMeshMonkey.getTransform().getPos().set(5, 5, 5);

        //*
        camera.getTransform().getPos().set(10, -0.5f, 12);
        camera.getTransform().rotate(new Vector3f(1, 0, 0), (float)Math.toRadians(-20));
        camera.getTransform().rotate(new Vector3f(0, 1, 0), (float)Math.toRadians(220)); //*/
    }
}
