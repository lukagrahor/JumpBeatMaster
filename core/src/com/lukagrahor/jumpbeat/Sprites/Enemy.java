package com.lukagrahor.jumpbeat.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.lukagrahor.jumpbeat.Scenes.Hud;
import com.lukagrahor.jumpbeat.Screens.PlayScreen;

public abstract class Enemy extends Sprite {
    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocity;
    public Hud hud;
    public Enemy(PlayScreen screen, float x, float y, Hud hud){
        this.world = screen.getWorld();
        this.screen = screen;
        this.hud=hud;
        setPosition(x,y);
        defineEnemy();
        velocity = new Vector2(1,0);
        b2body.setActive(false);//daš nasprotnika v spanje --> se ne premika
    }

    protected abstract void defineEnemy();
    public abstract void update (float dt);
    public abstract void hitOnHead();

    public void reverseVelocity(boolean x, boolean y){ //obrne gravitacijo v drugo smer
        if(x)
            velocity.x=-velocity.x;
        if(y)
            velocity.y=-velocity.y;
    }
}
