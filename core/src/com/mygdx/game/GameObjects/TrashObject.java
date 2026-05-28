package com.mygdx.game.GameObjects;

import static com.mygdx.game.GameSettings.TRASH_VELOCITY;
import static com.mygdx.game.screens.ScreenGame.counter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameSettings;

import java.util.Random;

public class TrashObject extends GameObjects {
    private static final int paddingHorizontal = 30;
    Texture texture;
    private int livesLeft;

    public TrashObject(int width, int height, String texturePath, World world) {
        super(
                texturePath,
                width / 2 + paddingHorizontal + (new Random()).nextInt((GameSettings.SCREEN_WIDTH - 2 * paddingHorizontal - width)),
                GameSettings.SCREEN_HEIGHT + height / 2,
                width, height,
                GameSettings.TRASH_BIT,
                world
        );
        body.setLinearVelocity(new Vector2(0, -TRASH_VELOCITY));
        texture = new Texture(texturePath);
        livesLeft = 1;
    }
    public boolean isInFrame() {
        return getY() + height / 2 > 0;
    }

@Override
    public void hit(){
    livesLeft -= 1;
    counter+=1;
    TRASH_VELOCITY+=0.2;
}
public boolean isAlive(){
        return livesLeft > 0;
}

}
