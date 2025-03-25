package com.base.engine.components;

import com.base.engine.core.Input;
import com.base.engine.core.Quaternion;
import com.base.engine.core.Vector2f;
import com.base.engine.core.Vector3f;
import com.base.engine.rendering.Window;

import static org.lwjgl.glfw.GLFW.*;

public class FreeLook extends GameComponent {
    public static final Vector3f yAxis = new Vector3f(0, 1, 0);

    private boolean useMouse = false;

    private boolean mouseLocked = false;
    private boolean yAxisLocked = true;
    Vector2f centerPosition = new Vector2f((float) Window.getWidth()/2, (float)Window.getHeight()/2);

    public FreeLook(boolean useMouse) {
        this.useMouse = useMouse;
    }

    @Override
    public void input(float delta) {
        float sensitivity = 0.5f;
        float rotAmt = 100 * delta;

        if(Input.getKeyDown(GLFW_KEY_F) && useMouse) {
            if(mouseLocked) {
                Input.setCursor(true);
                mouseLocked = false;
            } else {
                Input.setMousePosition(new Vector2f((float) Window.getWidth()/2, (float)Window.getHeight()/2));
                Input.setCursor(false);
                mouseLocked = true;
            }
        }
        if(Input.getKeyDown(GLFW_KEY_R)) {
            yAxisLocked = !yAxisLocked;
        }

        if(Input.getKey(GLFW_KEY_UP) == GLFW_PRESS)
            getTransform().rotate(getTransform().getRot().getRight(), (float)Math.toRadians(rotAmt));
        if(Input.getKey(GLFW_KEY_DOWN) == GLFW_PRESS)
            getTransform().rotate(getTransform().getRot().getRight(), (float)Math.toRadians(-rotAmt));
        if(Input.getKey(GLFW_KEY_LEFT) == GLFW_PRESS)
            getTransform().rotate(yAxisLocked ? yAxis : getTransform().getRot().getUp(), (float)Math.toRadians(rotAmt));
        if(Input.getKey(GLFW_KEY_RIGHT) == GLFW_PRESS)
            getTransform().rotate(yAxisLocked ? yAxis : getTransform().getRot().getUp(), (float)Math.toRadians(-rotAmt));
        if(Input.getKey(GLFW_KEY_Q) == GLFW_PRESS)
            getTransform().rotate(getTransform().getRot().getForward(), (float)Math.toRadians(rotAmt));
        if(Input.getKey(GLFW_KEY_E) == GLFW_PRESS)
            getTransform().rotate(getTransform().getRot().getForward(), (float)Math.toRadians(-rotAmt));

        if(Input.getKeyDown(GLFW_KEY_V))
            getTransform().setRot(new Quaternion(new Vector3f(0, 0, 0), 0));
        if(Input.getKeyDown(GLFW_KEY_B))
            getTransform().setRot(new Quaternion(new Vector3f(0, 1, 0), (float)Math.toRadians(90)));
        if(Input.getKeyDown(GLFW_KEY_N))
            getTransform().setRot(new Quaternion(new Vector3f(0, 0, 1), 90));

        if (mouseLocked && useMouse) {
            Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);

            boolean rotY = deltaPos.getX() != 0;
            boolean rotX = deltaPos.getY() != 0;

            if (rotY)
                getTransform().rotate(yAxisLocked ? yAxis : getTransform().getRot().getUp(), (float)Math.toRadians(deltaPos.getX() * sensitivity));
            if (rotX)
                getTransform().rotate(getTransform().getRot().getRight(), (float)Math.toRadians(deltaPos.getY() * sensitivity));

            if(rotY || rotX)
                Input.setMousePosition(new Vector2f((float)Window.getWidth() / 2, (float)Window.getHeight() / 2));
        }
    }
}
