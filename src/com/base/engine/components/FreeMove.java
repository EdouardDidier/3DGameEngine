package com.base.engine.components;

import com.base.engine.core.Input;
import com.base.engine.core.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class FreeMove extends GameComponent {
    public static final Vector3f yAxis = new Vector3f(0, 1, 0);

    private int keyForward, keyBackward, keyLeft, keyRight, keyUp, keyDown;

    private float speedUp = 1.0f;

    public FreeMove() {
        this(GLFW_KEY_W, GLFW_KEY_S, GLFW_KEY_A, GLFW_KEY_D, GLFW_KEY_SPACE, GLFW_KEY_LEFT_CONTROL);
    }

    public FreeMove(int keyForward, int keyBackward, int keyLeft, int keyRight, int keyUp, int keyDown) {
        this.keyForward = keyForward;
        this.keyBackward = keyBackward;
        this.keyLeft = keyLeft;
        this.keyRight = keyRight;
        this.keyUp = keyUp;
        this.keyDown = keyDown;
    }

    @Override
    public void input(float delta) {
        float moveAmt = 10 * delta * speedUp;

        if(Input.getKey(keyForward) == GLFW_PRESS)
            move(getTransform().getRot().getForward(), moveAmt);
        if(Input.getKey(keyBackward) == GLFW_PRESS)
            move(getTransform().getRot().getForward(), -moveAmt);
        if(Input.getKey(keyLeft) == GLFW_PRESS)
            move(getTransform().getRot().getLeft(), moveAmt);
        if(Input.getKey(keyRight) == GLFW_PRESS)
            move(getTransform().getRot().getRight(), moveAmt);

        if(Input.getKeyDown(GLFW_KEY_LEFT_SHIFT)) {
            speedUp = 3.0f;
        }
        if(Input.getKeyUp(GLFW_KEY_LEFT_SHIFT)) {
            speedUp = 1.0f;
        }

        if(Input.getKey(keyUp) == GLFW_PRESS)
            move(yAxis, moveAmt);
        if(Input.getKey(keyDown) == GLFW_PRESS)
            move(yAxis, -moveAmt);
    }

    public void move(Vector3f dir, float amt) { //TODO: Make it all-in-one?
        getTransform().setPos(getTransform().getPos().add(dir.mul(amt)));
    }
}
