package com.mygdx.game;

import static com.mygdx.game.GameSettings.POSITION_ITERATIONS;
import static com.mygdx.game.GameSettings.STEP_TIME;
import static com.mygdx.game.GameSettings.VELOCITY_ITERATIONS;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.managers.AudioManager;
import com.mygdx.game.screens.MenuScreen;
import com.mygdx.game.screens.ScreenGame;
import com.mygdx.game.screens.SettingsScreen;

public class MyGdxGame extends Game {

	public World world;

	public BitmapFont largeWhiteFont;
	public SettingsScreen settings;
	public BitmapFont commonWhiteFont;
	public BitmapFont commonBlackFont;
	public  BitmapFont unicWhiteFont;
	public AudioManager audioManager;

	public Vector3 touch; // Теперь будет инициализирован
	public SpriteBatch batch;
	public OrthographicCamera camera;

	public ScreenGame gameScreen;
	public MenuScreen menuScreen;

	float accumulator = 0;

	@Override
	public void create() {

		Box2D.init();
		world = new World(new Vector2(0, 0), true);

		commonWhiteFont = FontBuilder.generate(24, Color.WHITE, GameResources.FONT_PATH);
		largeWhiteFont = FontBuilder.generate(48,Color.WHITE,GameResources.FONT_PATH);
		commonBlackFont = FontBuilder.generate(24,Color.BLACK,GameResources.FONT_PATH);
		unicWhiteFont = FontBuilder.generate(34,Color.WHITE,GameResources.FONT_PATH);
		audioManager = new AudioManager();

		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);

		// Инициализируем touch
		touch = new Vector3();

		gameScreen = new ScreenGame(this);
		menuScreen = new MenuScreen(this);
		settings = new SettingsScreen(this);

		setScreen(menuScreen);
	}

	@Override
	public void dispose() {
		commonWhiteFont.dispose();
		largeWhiteFont.dispose();
		commonBlackFont.dispose();
		batch.dispose();
		world.dispose(); // Не забываем dispose world
	}

	public void stepWorld() {
		float delta = Gdx.graphics.getDeltaTime();
		accumulator += Math.min(delta, 0.25f);

		if (accumulator >= STEP_TIME) {
			accumulator -= STEP_TIME;
			world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
		}
	}

	// Добавим метод для обновления позиции касания
	public void updateTouch() {
		if (Gdx.input.isTouched()) {
			touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touch);
		}
	}
}