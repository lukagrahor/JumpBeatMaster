package com.lukagrahor.jumpbeat.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.lukagrahor.jumpbeat.JumpBeatClass;
import com.lukagrahor.jumpbeat.Scenes.Hud;
import com.lukagrahor.jumpbeat.Screens.PlayScreen;


public class Boar extends Enemy{
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    boolean runningRight;
    private float stateTimer;

    public Boar(PlayScreen screen, float x, float y, Hud hud) {
        super(screen, x, y,hud);
        runningRight=true;//na začetku bo igralec obrnjen v desno
        frames = new Array<TextureRegion>();
        for(int i = 0; i < 7; i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("boar-Sheet"),i*33,0,33,18));
        }
        walkAnimation = new Animation<TextureRegion>(0.07f,frames);
        stateTime = 0;
        setBounds(getX(),getY(),33/JumpBeatClass.PPM,18/JumpBeatClass.PPM);
        setToDestroy = false;
        destroyed = false;
        stateTimer=0;
    }
    public TextureRegion getFrame(float dt){
        TextureRegion region;
        region=walkAnimation.getKeyFrame(stateTimer,true);
        if((b2body.getLinearVelocity().x<0 || !runningRight) && !region.isFlipX()){//ko se premika v levo. Region.isFlipx() nam vrne true če je textura obrnjena.
            region.flip(true,false);//Če se igralec premika v levo in textura ni obrnjena v levo, se obrne v levo
            runningRight = false;//sedaj je igralec obrnjen v levo
        }
        else if((b2body.getLinearVelocity().x>0 || runningRight) && region.isFlipX()){//obratno od tega kar se dogaja v gornjem if-u
            region.flip(true,false);
            runningRight = true;
        }
        stateTimer =stateTimer + dt;
        return region;
    }

    public void update(float dt){
        stateTime += dt;
        if(setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("boar-Sheet"),0,0,33,18));
            stateTime = 0;
        }
        else if(!destroyed) {
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(getFrame(dt));//animacija se ponavlja
        }

    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();//dejanski fizičen del
        CircleShape shape = new CircleShape();
        fdef.filter.categoryBits = JumpBeatClass.ENEMY_BIT;
        fdef.filter.maskBits = JumpBeatClass.GROUND_BIT |
            JumpBeatClass.SPIKE_BIT |
            JumpBeatClass.ENEMY_BIT |
            JumpBeatClass.JUMPY_BIT |
            JumpBeatClass.FLOATING_BLOCK_BIT;
        shape.setRadius(6 / JumpBeatClass.PPM);

        fdef.shape = shape;
        shape.setPosition(new Vector2(7/JumpBeatClass.PPM,0));
        b2body.createFixture(fdef).setUserData(this);
        shape.setPosition(new Vector2(-7/JumpBeatClass.PPM,0));
        b2body.createFixture(fdef).setUserData(this);

        //Glava nasprotnika
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-11,9).scl(1/JumpBeatClass.PPM);
        vertice[1] = new Vector2(10,9).scl(1/JumpBeatClass.PPM);
        vertice[2] = new Vector2(-10,6).scl(1/JumpBeatClass.PPM);
        vertice[3] = new Vector2(9,6).scl(1/JumpBeatClass.PPM);
        head.set(vertice);//polygonu head dodamo točke iz tabele vertice
        fdef.shape = head; //dodam glavo
        fdef.restitution = 1.3f;//Odboj - 1 pomeni, da če se igralec pade za 10px in se dotakne glave nasprotnika, se bo odbil za 10 px v zrak
        fdef.filter.categoryBits = JumpBeatClass.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);//setUSerData(this) --> omogoči da je Boar dostopen iz WorldContactListener-ja
    }
    public void draw(Batch batch){
        if(!destroyed ||stateTime<2)//nasprotnikovo telo izgine po 2 sekundah
            super.draw(batch);
    }
    @Override
    public void hitOnHead() {
        setToDestroy = true;
        hud.addScore(20);
    }
}