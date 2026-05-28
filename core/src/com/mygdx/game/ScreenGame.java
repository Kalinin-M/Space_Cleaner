package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.GameObjects.ShipObject;

public class ScreenGame implements Screen {
    MyGdxGame myGdxGame;
    ShipObject shipObject;

    public ScreenGame(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        shipObject = new ShipObject(
                GameSettings.SCREEN_WIDTH / 2, 150,
                GameSettings.SHIP_WIDTH, GameSettings.SHIP_HEIGHT,
                GameResources.SHIP_IMG_PATH,
                myGdxGame.world
        );
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        myGdxGame.stepWorld();
        handleInput();
        draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
shipObject.dispose();
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            shipObject.move(myGdxGame.touch);
        }
    }
    private void draw() {
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();
        shipObject.draw(myGdxGame.batch);
        myGdxGame.batch.end();
    }
}
