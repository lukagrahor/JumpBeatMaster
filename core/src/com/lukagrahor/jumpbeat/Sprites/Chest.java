package com.lukagrahor.jumpbeat.Sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.lukagrahor.jumpbeat.Items.StaticItems;
import com.lukagrahor.jumpbeat.JumpBeatClass;
import com.lukagrahor.jumpbeat.Scenes.Hud;
import com.lukagrahor.jumpbeat.Screens.PlayScreen;
import com.lukagrahor.jumpbeat.Tools.Variables;

public class Chest extends StaticItems {
    private float stateTime;
    private Animation<TextureRegion> animation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    private AssetManager manager;
    private Sound sound;

    public Chest(PlayScreen screen, float x, float y, Hud hud) {
        super(screen, x, y,hud);
        frames = new Array<TextureRegion>();

        for(int i = 0; i < 12; i++){
            frames.add(new TextureRegion(screen.getAtlas6().findRegion("ChestAnimation-Sheet"),i*19,0,19,23));
        }
        stateTime = 0;
        animation = new Animation<TextureRegion>(0.15f,frames);
        setBounds(getX(),getY(),19/JumpBeatClass.PPM,23/JumpBeatClass.PPM);
        setToDestroy = false;
        destroyed = false;
        manager = new AssetManager();
        manager.load("audio/sounds/chestOpen.wav", Sound.class);
        manager.finishLoading();
        sound = manager.get("audio/sounds/chestOpen.wav",Sound.class);
    }

    public void update(float dt){
        stateTime += dt;
        if(setToDestroy && !destroyed) {
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas6().findRegion("ChestAnimation-Sheet"),0,0,19,23));
            stateTime = 0;
        }
        else if(!destroyed) {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(new TextureRegion(screen.getAtlas6().findRegion("ChestAnimation-Sheet"),0,0,19,23));
        }
        else if(destroyed){
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(animation.getKeyFrame(stateTime, false));
        }
    }
    protected void defineStaticItems() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();//dejanski fizičen del
        CircleShape shape = new CircleShape();
        fdef.filter.categoryBits = JumpBeatClass.CHEST_BIT;
        fdef.filter.maskBits = JumpBeatClass.GROUND_BIT |
                JumpBeatClass.JUMPY_BIT |
                JumpBeatClass.FLOATING_BLOCK_BIT;
        shape.setRadius(7 / JumpBeatClass.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }
    public void draw(Batch batch){
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
        if(!destroyed) {
            hud.addScore(100);
            sound.play();
        }
    }
}