package com.monstrous.isometric;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ScreenUtils;

// test of isometric rendering
//


public class Main extends ApplicationAdapter {

	final static float WORLD_VIEW_SIZE = 20f;

	private OrthographicCamera cam;
	private Environment environment;
	private OrthoCamController camController;
	private PlayerController playerController;
	private ModelInstance playerInstance;
	private Array<ModelInstance> instances;
	private Array<Model> models;
	private ModelBatch modelBatch;


	@Override
	public void create () {

		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		cam.position.set(5f, 3f, 5f);
		cam.lookAt(0,0,0);
		cam.near = .1f;
		cam.far = 30f;
		cam.zoom = WORLD_VIEW_SIZE/Gdx.graphics.getWidth();
		cam.update();




		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));


		instances = new Array<>();
		models = new Array<>();
		ModelBuilder modelBuilder = new ModelBuilder();

		// create models

		Model modelBox = modelBuilder.createBox(1f, 1f, 1f,
				new Material(ColorAttribute.createDiffuse(Color.GREEN)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal  );

		playerInstance = new ModelInstance(modelBox, -0.5f, 0.5f,-2.5f);
		instances.add( playerInstance );
		models.add(modelBox);

		Model modelXYZ = modelBuilder.createXYZCoordinates(5f, new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.ColorPacked);
		instances.add( new ModelInstance(modelXYZ, new Vector3(0,0,0)) );
		models.add(modelXYZ);

		Model modelGrid = modelBuilder.createLineGrid(20, 20, 1, 1, new Material(ColorAttribute.createDiffuse(Color.WHITE)),   VertexAttributes.Usage.Position);
		instances.add( new ModelInstance(modelGrid) );
		models.add(modelGrid);

		modelBatch = new ModelBatch();

		camController = new OrthoCamController(cam, playerInstance);
		playerController = new PlayerController(playerInstance);
		InputMultiplexer im = new InputMultiplexer();
		im.addProcessor(camController);
		im.addProcessor(playerController);
		Gdx.input.setInputProcessor(im);

	}


	@Override
	public void resize (int width, int height) {
		cam.viewportHeight = height;
		cam.viewportWidth = width;
		cam.update();
	}



	@Override
	public void render () {
		float deltaTime = Gdx.graphics.getDeltaTime();

		camController.update( deltaTime );
		playerController.update( deltaTime );

		ScreenUtils.clear(0.8f, 0.8f, 1.0f, 1, true);

		modelBatch.begin(cam);
		modelBatch.render(instances, environment);
		modelBatch.end();
	}


	@Override
	public void dispose () {
		for(Model model: models)
			model.dispose();
		modelBatch.dispose();
	}
}
