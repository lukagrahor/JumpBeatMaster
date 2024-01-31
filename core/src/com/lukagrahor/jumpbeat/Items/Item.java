package com.lukagrahor.jumpbeat.Items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.lukagrahor.jumpbeat.JumpBeatClass;
import com.lukagrahor.jumpbeat.Screens.PlayScreen;

public abstract class Item extends Sprite {
    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;

    public Item(PlayScreen screen,float x,float y){
        this.screen=screen;
        this.world=world;
        setPosition(x,y);
        setBounds(getX(),getY(),16/ JumpBeatClass.PPM,16/JumpBeatClass.PPM);
        defineItem();
        toDestroy = false;
        destroyed = false;
    }
    public abstract void defineItem();
    public abstract void use();

    public void update(float dt){
        if(toDestroy&&!destroyed){
            world.destroyBody(body);
            destroyed=true;
        }
    }

    public void draw(Batch batch){
        if(!destroyed)
            super.draw(batch);//nariše predmet, samo če ni že pobran
    }
    public void destroy(){
        toDestroy=true;
    }
}
