package com.monstrous.isometric;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class OrthoCamController extends InputAdapter {

    public final static float PAN_SPEED = 5f;
    public final static float MAX_ZOOM = 0.1f;
    public final static float ZOOM_SPEED = 0.5f;

    private final OrthographicCamera cam;
    private boolean zoomInPressed = false;
    private boolean zoomOutPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean forwardPressed = false;
    private boolean backPressed = false;

    public OrthoCamController(OrthographicCamera cam) {
        this.cam = cam;

    }

    public void update( float deltaTime ) {
        if (leftPressed)
            cam.position.x -= PAN_SPEED * deltaTime;
        else if (rightPressed)
            cam.position.x += PAN_SPEED * deltaTime;

        if (forwardPressed)
            cam.position.z -= PAN_SPEED * deltaTime;
        else if (backPressed)
            cam.position.z += PAN_SPEED * deltaTime;

        if (zoomInPressed)
            zoom(ZOOM_SPEED * deltaTime);
        else if (zoomOutPressed)
            zoom(-ZOOM_SPEED * deltaTime);

        if (leftPressed || rightPressed || forwardPressed || backPressed || zoomInPressed || zoomOutPressed)
            cam.update();
    }

    @Override
    public boolean scrolled (float amountX, float amountY) {
        zoom(amountY * 0.1f * ZOOM_SPEED );
        cam.update();
        return true;
    }

    public void zoom(float amount) {
        cam.zoom += amount;
        if(cam.zoom < MAX_ZOOM)
            cam.zoom = MAX_ZOOM;
    }

    @Override
    public boolean keyDown (int keycode) {
        setKeyState(keycode, true);
        return false;
    }

    @Override
    public boolean keyUp (int keycode) {
        setKeyState(keycode, false);
        return false;
    }

    private void setKeyState(int keycode, boolean state) {
        if (keycode == Input.Keys.P)
            zoomInPressed = state;
        else if (keycode == Input.Keys.O)
            zoomOutPressed = state;

        if (keycode == Input.Keys.A)
            leftPressed = state;
        else if (keycode == Input.Keys.D)
            rightPressed = state;

        if (keycode == Input.Keys.W)
            forwardPressed = state;
        else if (keycode == Input.Keys.S)
            backPressed = state;
    }
}
