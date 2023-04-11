package com.monstrous.isometric;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

// test of isometric rendering
//


public class Main extends ApplicationAdapter {

	final static float VIEW_SIZE = 20f;

	private OrthographicCamera cam;
	private Environment environment;
	private OrthoCamController camController;
	private ModelBatch modelBatch;
	private World world;
	private DirectionalShadowLight shadowLight;
	private ModelBatch shadowBatch;

	@Override
	public void create () {
		cam = new OrthographicCamera();

		cam.position.set(10f, 10f, 10f);
		cam.lookAt(0,0,0);
		cam.near = 1f;
		cam.far = 30f;
		cam.zoom = 1;
		cam.update();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f));

		// use the 'deprecated' shadow light
		// tweak these values so that all the scene is covered and the shadows are not too blocky
		shadowLight = new DirectionalShadowLight(1024, 1024, 32, 32, 5f, 50);

		Vector3 lightVector = new Vector3(.4f, -.8f, -0.4f).nor();
		float dl = 0.6f;
		shadowLight.set(new Color(dl, dl, dl, 1), lightVector);
		environment.add(shadowLight);
		environment.shadowMap = shadowLight;
		shadowBatch = new ModelBatch(new DepthShaderProvider());

		world = new World();

		modelBatch = new ModelBatch();

		camController = new OrthoCamController(cam);
		Gdx.input.setInputProcessor(camController);

	}


	@Override
	public void resize (int width, int height) {
		// define view port in logical units (tiles) but maintain aspect ratio if the screen/window
		cam.viewportWidth = VIEW_SIZE;
		cam.viewportHeight = VIEW_SIZE * height/width;
		cam.update();
	}



	@Override
	public void render () {
		float deltaTime = Gdx.graphics.getDeltaTime();

		camController.update( deltaTime );

		// prepare shadow buffer
		shadowLight.begin(Vector3.Zero, cam.direction);
		shadowBatch.begin(shadowLight.getCamera());
		shadowBatch.render(world.instances, environment);
		shadowBatch.end();
		shadowLight.end();

		// clear screen
		ScreenUtils.clear(0.8f, 0.8f, 1.0f, 1, true);

		// render world
		modelBatch.begin(cam);
		modelBatch.render(world.instances, environment);
		modelBatch.end();
	}


	@Override
	public void dispose () {
		modelBatch.dispose();
		world.dispose();
		shadowBatch.dispose();
		shadowLight.dispose();
	}
}
