package com.monstrous.isometric;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class PlayerController extends InputAdapter {

        public final static float PAN_SPEED = 6f;

        private ModelInstance player;

        private boolean leftPressed = false;
        private boolean rightPressed = false;
        private boolean forwardPressed = false;
        private boolean backwardPressed = false;

        public PlayerController(ModelInstance player) {
            this.player = player;
        }

        public void update( float deltaTime ) {

            if(leftPressed)
                player.transform.translate(-PAN_SPEED * deltaTime,0, 0);
            else if (rightPressed)
                player.transform.translate(PAN_SPEED * deltaTime, 0,0);

            if(forwardPressed)
                player.transform.translate(0,0,-PAN_SPEED * deltaTime);
            else if (backwardPressed)
                player.transform.translate(0,0, PAN_SPEED * deltaTime);
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
            if (keycode == Input.Keys.W)
                forwardPressed = state;
            else if (keycode == Input.Keys.A)
                leftPressed = state;
            else if (keycode == Input.Keys.S)
                backwardPressed = state;
            else if (keycode == Input.Keys.D)
                rightPressed = state;
        }

}
