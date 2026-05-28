package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.GameResources;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.components.ButtonView;
import com.mygdx.game.components.MovingBackgroundView;
import com.mygdx.game.components.TextView;

public class MenuScreen extends ScreenAdapter {
    MyGdxGame myGdxGame;
    TextView titleView;

    MovingBackgroundView backgroundView;
    ButtonView startButtonView;
    ButtonView settingsButtonView;
    ButtonView exitButtonView;


    public MenuScreen(MyGdxGame myGdxGame){
        this.myGdxGame = myGdxGame;

        backgroundView = new MovingBackgroundView(GameResources.BACKGROUND_IMG_PATH);
        titleView = new TextView(myGdxGame.largeWhiteFont, 470, 600, "Space Cleaner");
        startButtonView = new ButtonView(410,400,440,70,myGdxGame.commonBlackFont, GameResources.BUTTON_LONG_BG_IMG_PATH, "start");
        settingsButtonView = new ButtonView(410, 310, 440, 70, myGdxGame.commonBlackFont, GameResources.BUTTON_LONG_BG_IMG_PATH, "settings");
        exitButtonView = new ButtonView(410, 220, 440, 70, myGdxGame.commonBlackFont, GameResources.BUTTON_LONG_BG_IMG_PATH, "exit");
    }
    @Override
    public void render(float delta){
        backgroundView.move();
        handleInput();

        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();

        backgroundView.draw(myGdxGame.batch);
        titleView.draw(myGdxGame.batch);
        exitButtonView.draw(myGdxGame.batch);
        settingsButtonView.draw(myGdxGame.batch);
        startButtonView.draw(myGdxGame.batch);

        myGdxGame.batch.end();
    }
    private void handleInput() {
        if (Gdx.input.justTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (startButtonView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                myGdxGame.setScreen(myGdxGame.gameScreen);
            }
            if (exitButtonView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                Gdx.app.exit();
            }
            if (settingsButtonView.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                myGdxGame.setScreen(myGdxGame.settings);
            }
        }
    }

}
