package com.base.engine.rendering;

import com.base.engine.components.BaseLight;
import com.base.engine.components.Camera;
import com.base.engine.components.ShadowInfo;
import com.base.engine.core.*;
import com.base.engine.rendering.resourceManagement.MappedValues;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

public class RenderingEngine extends MappedValues {
    private static final Matrix4f biasMatrix = new Matrix4f().initScale(0.5f, 0.5f, 0.5f).mul(new Matrix4f().initTranslation(1.0f, 1.0f, 1.0f));

    private HashMap<String, Integer> samplerMap;
    private ArrayList<BaseLight> lights;
    private BaseLight activeLight;

    private Shader forwardAmbient;
    private Shader shadowMapShader;
    private Matrix4f lightMatrix;

    private Camera mainCamera;
    private Camera altCamera;

    private GameObject altCameraObject;

    private Material planeMaterial;
    private Mesh plane;
    private Transform planeTransform;
    private Texture tempTarget;

    public RenderingEngine() {
        super();

        lights = new ArrayList<BaseLight>();
        samplerMap = new HashMap<String, Integer>();
        samplerMap.put("diffuse", 0);
        samplerMap.put("normalMap", 1);
        samplerMap.put("dispMap", 2);
        samplerMap.put("shadowMap", 3);

        setVector3f("ambient", new Vector3f(0.1f, 0.1f, 0.1f));
        setTexture("shadowMap", new Texture(1024, 1024, null, GL_TEXTURE_2D, GL_LINEAR, GL_RG32F, GL_RGBA, true, GL_COLOR_ATTACHMENT0)); //TODO: Change resolution with dynamic attribution

        forwardAmbient = new Shader("forward-ambient");
        shadowMapShader = new Shader("shadowMapGenerator");

        //Fill screen starting color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        //Defining Cull Faces
        glFrontFace(GL_CW);
        glCullFace(GL_BACK);
        glEnable(GL_CULL_FACE);

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_DEPTH_CLAMP);

        altCamera = new Camera(new Matrix4f().initIdentity());
        altCameraObject = new GameObject().addComponent(altCamera);

        altCamera.getTransform().rotate(new Vector3f(0, 1, 0), (float)Math.toRadians(180.0f));

        //Temp
        int width = Window.getWidth();
        int height = Window.getHeight();
//        int dataSize = width * height * 4;

        //ByteBuffer data = Util.createByteBuffer(dataSize);

        tempTarget = new Texture(width, height, null, GL_TEXTURE_2D, GL_NEAREST, GL_RGBA, GL_RGBA, false, GL_COLOR_ATTACHMENT0);
//
//        Vertex[] vertices = new Vertex[] {
//          new Vertex(new Vector3f(-1, -1, 0), new Vector2f(1, 0)),
//                new Vertex(new Vector3f(-1, 1, 0), new Vector2f(1, 1)),
//                new Vertex(new Vector3f(1, 1, 0), new Vector2f(0, 1)),
//                new Vertex(new Vector3f(1, -1, 0), new Vector2f(0, 0))
//        };
//
//        int[] indices = new int[] {
//          2, 1, 0,
//          3, 2, 0
//        };

        planeMaterial = new Material(tempTarget,1, 8);
        //plane = new Mesh(vertices, indices, true);
        plane = new Mesh("plane.obj");

        planeTransform = new Transform();
        planeTransform.setScale(new Vector3f(0.9f, 0.9f, 0.9f));
        planeTransform.rotate(new Vector3f(1, 0, 0), (float)Math.toRadians(90));
        planeTransform.rotate(planeTransform.getRot().getUp(), (float)Math.toRadians(180.0f));
    }

    public void updateUniformStruct(Transform transform, Material material, Shader shader, String uniformName, String uniformType) {
        throw new IllegalArgumentException(uniformType + "is not a supported type in Rendering Engine");
    }

    public void render(GameObject object) {
        Window.bindAsRenderTarget();
//        tempTarget.binAsRenderTarget(); //Temp

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f); //Temp
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); //TODO: Stencil buffer?

        object.renderAll(forwardAmbient, this);

        for(BaseLight light : lights) {
            activeLight = light;
            ShadowInfo shadowInfo = activeLight.getShadowInfo();

            getTexture("shadowMap").bindAsRenderTarget();
            glClear(GL_DEPTH_BUFFER_BIT);

            if(shadowInfo != null) {
                altCamera.setProjection(shadowInfo.getProjection());
                altCamera.getTransform().setPos(activeLight.getTransform().getTransformedPos());
                altCamera.getTransform().setRot(activeLight.getTransform().getTransformedRot());

                lightMatrix = biasMatrix.mul(altCamera.getViewProjection());

                setVector3f("shadowTexelSize", new Vector3f(1.0f / 1024.0f, 1.0f / 1024.0f, 0.0f));
                setFloat("shadowBias", shadowInfo.getBias() / 1024.0f);
                boolean flipFaces = shadowInfo.isFlipFace();

                Camera temp = mainCamera;
                mainCamera = altCamera;

                if(flipFaces) glCullFace(GL_FRONT);
                object.renderAll(shadowMapShader, this);
                if(flipFaces) glCullFace(GL_BACK);

                mainCamera = temp;
            }

            Window.bindAsRenderTarget();
            glEnable(GL_BLEND);
            glBlendFunc(GL_ONE, GL_ONE);
            glDepthMask(false);
            glDepthFunc(GL_EQUAL);

            object.renderAll(activeLight.getShader(), this);

            glDepthFunc(GL_LESS);
            glDepthMask(true);
            glDisable(GL_BLEND);
        }
    }

    public static String getOpenGLVersion() {
        return glGetString(GL_VERSION);
    }

    public void addLight(BaseLight light) {
        lights.add(light);
    }

    public BaseLight getActiveLight() {
        return activeLight;
    }

    public int getSamplerSlot(String samplerName) {
        return samplerMap.get(samplerName);
    }

    public void addCamera(Camera camera) {
        mainCamera = camera;
    }

    public Camera getMainCamera() {
        return mainCamera;
    }

    public void setMainCamera(Camera mainCamera) {
        this.mainCamera = mainCamera;
    }

    public Matrix4f getLightMatrix() {
        return lightMatrix;
    }
}
