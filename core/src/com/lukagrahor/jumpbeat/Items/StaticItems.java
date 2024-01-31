package com.lukagrahor.jumpbeat.Items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.lukagrahor.jumpbeat.Scenes.Hud;
import com.lukagrahor.jumpbeat.Screens.PlayScreen;

public abstract class StaticItems extends Sprite {
    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Hud hud;
    public StaticItems(PlayScreen screen, float x, float y, Hud hud) {
        this.hud=hud;
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineStaticItems();
    }
    protected abstract void defineStaticItems();
    public abstract void update (float dt);
    public abstract void hit();
    public abstract void use();
}
