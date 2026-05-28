package com.mygdx.game.GameObjects;

import static com.mygdx.game.GameSettings.BULLET_VELOCITY;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameSettings;

public class BulletObject extends  GameObjects{

boolean wasHit;


    public BulletObject(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.BULLET_BIT, world);
        body.setLinearVelocity(new Vector2(0,BULLET_VELOCITY));
        body.setBullet(true);
        wasHit = false;
    }

    public boolean hasToBeDestroyed() {
        return wasHit || (getY() - height / 2 > GameSettings.SCREEN_HEIGHT);
    }
    @Override
    public void hit() {
        wasHit = true;
    }

}
