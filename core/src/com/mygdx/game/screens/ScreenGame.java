package com.mygdx.game.screens;

import static com.mygdx.game.GameResources.BUTTON_SHORT_BG_IMG_PATH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.components.RecordListView;
import com.mygdx.game.managers.ContactManager;
import com.mygdx.game.GameResources;
import com.mygdx.game.GameSession;
import com.mygdx.game.GameSettings;
import com.mygdx.game.GameState;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.components.ButtonView;
import com.mygdx.game.components.ImageView;
import com.mygdx.game.components.LiveView;
import com.mygdx.game.components.MovingBackgroundView;
import com.mygdx.game.GameObjects.BulletObject;
import com.mygdx.game.GameObjects.ShipObject;
import com.mygdx.game.GameObjects.TrashObject;
import com.mygdx.game.components.TextView;
import com.mygdx.game.managers.MemoryManager;

import java.util.ArrayList;

public class ScreenGame extends ScreenAdapter {

    MyGdxGame myGdxGame;
    
    // PAUSED state UI
    ImageView fullBlackoutView;
    MenuScreen menuScreen;
    TextView pauseTextView;
    TextView scoreTextView;
    GameSession gameSession;
    ShipObject shipObject;
    ButtonView pauseButton;
    ButtonView homeButton;


    ButtonView continueButton;

    RecordListView recordsListView;
    TextView recordsTextView;
    ButtonView homeButton2;


    GameState state;
    public static int counter = 0;
    MovingBackgroundView backgroundView;
    ImageView topBlackoutView2;
    ImageView topBlackoutView;
    LiveView liveView;


    ArrayList<TrashObject> trashArray;
    ArrayList<BulletObject> bulletArray;

    ContactManager contactManager;

    public ScreenGame(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        gameSession = new GameSession();
        fullBlackoutView = new ImageView(0,0,"textures/blackout_full.png");

        pauseTextView = new TextView(myGdxGame.largeWhiteFont, 590, 600, "Pause"); // Раскомментировал

        scoreTextView = new TextView(myGdxGame.commonWhiteFont, 590,690);


        contactManager = new ContactManager(myGdxGame.world);
        pauseButton = new ButtonView(1200, 683, 24, 32, GameResources.PAUSE_IMG_PATH);
        homeButton = new ButtonView(320,320,200,70,myGdxGame.commonBlackFont,BUTTON_SHORT_BG_IMG_PATH,"home");
        continueButton = new ButtonView(750,320,200,70,myGdxGame.commonBlackFont,BUTTON_SHORT_BG_IMG_PATH,"continue");

        trashArray = new ArrayList<>();
        bulletArray = new ArrayList<>();

        shipObject = new ShipObject(
                GameSettings.SCREEN_WIDTH / 2, 150,
                GameSettings.SHIP_WIDTH, GameSettings.SHIP_HEIGHT,
                GameResources.SHIP_IMG_PATH,
                myGdxGame.world
        );
        backgroundView = new MovingBackgroundView(GameResources.BACKGROUND_IMG_PATH);

        topBlackoutView2 = new ImageView(0, 680,GameResources.BLACKOUT_TOP_IMG_PATH);
        topBlackoutView = new ImageView(720, 680,GameResources.BLACKOUT_TOP_IMG_PATH);
        liveView = new LiveView(50,685);

        recordsTextView = new TextView(myGdxGame.largeWhiteFont,470,620,"Records list");
        recordsListView = new RecordListView(myGdxGame.unicWhiteFont,570,400);
        homeButton2 = new ButtonView(500,50,200,70,myGdxGame.commonBlackFont,BUTTON_SHORT_BG_IMG_PATH,"home");
    }

    @Override
    public void show() {
        restartGame();
    }

    @Override
    public void render(float delta) {

        handleInput();

        if (gameSession.state == GameState.PLAYING) {
            if (gameSession.shouldSpawnTrash()) {
                TrashObject trashObject = new TrashObject(
                        GameSettings.TRASH_WIDTH, GameSettings.TRASH_HEIGHT,
                        GameResources.TRASH_IMG_PATH,
                        myGdxGame.world
                );
                trashArray.add(trashObject);
            }

            if (shipObject.needToShoot()) {
                BulletObject laserBullet = new BulletObject(
                        shipObject.getX(), shipObject.getY() + shipObject.height / 2,
                        GameSettings.BULLET_WIDTH, GameSettings.BULLET_HEIGHT,
                        GameResources.BULLET_IMG_PATH,
                        myGdxGame.world
                );
                bulletArray.add(laserBullet);
                if (myGdxGame.audioManager.isSoundOn) myGdxGame.audioManager.shootSound.play();
            }

            if (!shipObject.isAlive()) {
                gameSession.endGame();
                recordsListView.setRecords(MemoryManager.loadRecordsTable());

            }

            updateTrash();
            updateBullets();
            backgroundView.move();
            scoreTextView.setText("Score: " + counter);
            liveView.setLeftLives(shipObject.getLiveLeft());


            myGdxGame.stepWorld();
        }

        draw();
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            switch (gameSession.state) {
                case PLAYING:
                    if (pauseButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.pauseGame();
                    }
                    shipObject.move(myGdxGame.touch);
                    break;

                case PAUSED:
                    if (continueButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.resumeGame();
                    }
                    if (homeButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        myGdxGame.setScreen(myGdxGame.menuScreen);
                    }
                    break;
                case ENDED:
                    if (homeButton2.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        myGdxGame.setScreen(myGdxGame.menuScreen);
                    }
                    break;
            }

        }
    }



    private void draw() {
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();
        backgroundView.draw(myGdxGame.batch);

        for (TrashObject trash : trashArray) trash.draw(myGdxGame.batch);

        shipObject.draw(myGdxGame.batch);

        for (BulletObject bullet : bulletArray) bullet.draw(myGdxGame.batch);

        topBlackoutView.draw(myGdxGame.batch);
        topBlackoutView2.draw(myGdxGame.batch);
        scoreTextView.draw(myGdxGame.batch);
        liveView.draw(myGdxGame.batch);
        pauseButton.draw(myGdxGame.batch);

        if (gameSession.state == GameState.PAUSED) {
            fullBlackoutView.draw(myGdxGame.batch);
            pauseTextView.draw(myGdxGame.batch);
            homeButton.draw(myGdxGame.batch);
            continueButton.draw(myGdxGame.batch);
        } else if (gameSession.state == GameState.ENDED){
            fullBlackoutView.draw(myGdxGame.batch);
            recordsTextView.draw(myGdxGame.batch);
            recordsListView.draw(myGdxGame.batch);
            homeButton2.draw(myGdxGame.batch);
        }

        myGdxGame.batch.end();
    }

    private void updateTrash() {
        for (int i = 0; i < trashArray.size(); i++) {

            boolean hasToBeDestroyed = !trashArray.get(i).isAlive() || !trashArray.get(i).isInFrame();

            if (!trashArray.get(i).isAlive()) {
                if (myGdxGame.audioManager.isSoundOn) myGdxGame.audioManager.explosionSound.play(0.2f);
            }

            if (hasToBeDestroyed) {
                myGdxGame.world.destroyBody(trashArray.get(i).body);
                trashArray.remove(i--);
            }
        }
    }

    private void updateBullets() {
        for (int i = 0; i < bulletArray.size(); i++) {
            if (bulletArray.get(i).hasToBeDestroyed()) {
                myGdxGame.world.destroyBody(bulletArray.get(i).body);
                bulletArray.remove(i--);
            }
        }
    }
    private void restartGame() {

        for (int i = 0; i < trashArray.size(); i++) {
            myGdxGame.world.destroyBody(trashArray.get(i).body);
            trashArray.remove(i--);
        }

        if (shipObject != null) {
            myGdxGame.world.destroyBody(shipObject.body);
        }

        shipObject = new ShipObject(
                GameSettings.SCREEN_WIDTH / 2, 150,
                GameSettings.SHIP_WIDTH, GameSettings.SHIP_HEIGHT,
                GameResources.SHIP_IMG_PATH,
                myGdxGame.world
        );

        bulletArray.clear();
        gameSession.startGame();
        counter=0;
    }
    public int setScore(){
        return counter;
    }
}