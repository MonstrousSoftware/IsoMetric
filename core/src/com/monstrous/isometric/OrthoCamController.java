package com.monstrous.isometric;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class OrthoCamController extends InputAdapter {

    public final static float MAX_ZOOM = 0.005f;
    public final static float ZOOM_SPEED = 0.01f;

    private OrthographicCamera cam;
    private ModelInstance trackedObject;
    private Vector3 position;
    private Vector3 prevPosition;
    private boolean zoomInPressed = false;
    private boolean zoomOutPressed = false;

    public OrthoCamController(OrthographicCamera cam, final ModelInstance trackedObject) {
        this.cam = cam;
        this.trackedObject = trackedObject;
        position = new Vector3();
        prevPosition = new Vector3();
        trackedObject.transform.getTranslation(prevPosition);
    }

    public void update( float deltaTime ) {
        boolean mustUpdate = false;
        if(zoomInPressed) {
            zoom(ZOOM_SPEED * deltaTime);
            mustUpdate = true;
        }
        else if(zoomOutPressed) {
            zoom(-ZOOM_SPEED * deltaTime);
            mustUpdate = true;
        }

        trackedObject.transform.getTranslation(position);
        if( ! position.epsilonEquals(prevPosition )) {
            cam.position.add(position).sub(prevPosition);
            prevPosition.set(position);
            mustUpdate = true;
        }

        if(mustUpdate)
            cam.update();
    }

    @Override
    public boolean scrolled (float amountX, float amountY) {
        zoom(amountY * 0.1f * ZOOM_SPEED );
        cam.update();
        return true;
    }

    public void zoom (float amount) {
        cam.zoom += amount;
        if(cam.zoom < MAX_ZOOM)
            cam.zoom = MAX_ZOOM;

        Gdx.app.log("zoom", ""+cam.zoom);
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
    }
}
