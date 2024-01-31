package com.lukagrahor.jumpbeat.Sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.lukagrahor.jumpbeat.Items.StaticItems;
import com.lukagrahor.jumpbeat.JumpBeatClass;
import com.lukagrahor.jumpbeat.Scenes.Hud;
import com.lukagrahor.jumpbeat.Screens.PlayScreen;
import com.lukagrahor.jumpbeat.Tools.Variables;

public class Coin extends StaticItems {
    private float stateTime;
    private Animation<TextureRegion> animation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    private AssetManager manager;
    private Sound sound;


    public Coin(PlayScreen screen, float x, float y, Hud hud) {
        super(screen, x, y,hud);
        frames = new Array<TextureRegion>();
        for(int i = 0; i < 7; i++){
            frames.add(new TextureRegion(screen.getAtlas5().findRegion("CnAnimation-Sheet"),i*10,0,10,11));
        }
        animation = new Animation<TextureRegion>(0.1f,frames);
        stateTime = 0;
        setBounds(getX(),getY(),16/JumpBeatClass.PPM,16/JumpBeatClass.PPM);
        setToDestroy = false;
        destroyed = false;
        manager = new AssetManager();
        manager.load("audio/sounds/coinSound.wav", Sound.class);
        manager.finishLoading();
        sound = manager.get("audio/sounds/coinSound.wav",Sound.class);
    }

    public void update(float dt){
        stateTime += dt;
        if(setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas5().findRegion("CnAnimation-Sheet"),0,0,10,11));
            stateTime = 0;
        }
        else if(!destroyed) {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(animation.getKeyFrame(stateTime, true));
        }
    }
    protected void defineStaticItems() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();//dejanski fizičen del
        CircleShape shape = new CircleShape();
        fdef.filter.categoryBits = JumpBeatClass.COIN_BIT;
        fdef.filter.maskBits = JumpBeatClass.GROUND_BIT |
                JumpBeatClass.JUMPY_BIT |
                JumpBeatClass.FLOATING_BLOCK_BIT;
        shape.setRadius(7 / JumpBeatClass.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape sensorTop = new EdgeShape();//ta EdgeShape je enak kot zgornji, le da se obnaša samo kot senzor
        sensorTop.set(new Vector2(-16/JumpBeatClass.PPM,11/JumpBeatClass.PPM),new Vector2(16/JumpBeatClass.PPM,11/JumpBeatClass.PPM));
        fdef.shape=sensorTop;
        fdef.filter.categoryBits = JumpBeatClass.COIN_BIT;
        fdef.filter.maskBits = JumpBeatClass.GROUND_BIT |
                JumpBeatClass.JUMPY_BIT |
                JumpBeatClass.FLOATING_BLOCK_BIT;
        shape.setRadius(7 / JumpBeatClass.PPM);
        fdef.isSensor=true;

        b2body.createFixture(fdef).setUserData(this);
    }
    public void draw(Batch batch){
        if(!destroyed)//nasprotnikovo telo izgine
            super.draw(batch);
    }
    @Override
    public void use(){
        if(!Variables.using){
            Variables.using=true;

        }
        Variables.using=false;
    }
    @Override
    public void hit() {
        setToDestroy = true;
        hud.addScore(10);
        sound.play();
    }
}
