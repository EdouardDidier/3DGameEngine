package com.base.game.components;

import com.base.engine.components.GameComponent;
import com.base.engine.core.Vector3f;

public class ContinueRotation extends GameComponent {
    @Override
    public void update(float delta) {
        getTransform().rotate(new Vector3f(0, 1, 0), (float)Math.toRadians(delta * 100f));
        //getTransform().rotate(getTransform().getRot().getForward(), (float)Math.toRadians(delta * 100f));
    }
}
