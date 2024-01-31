package com.lukagrahor.jumpbeat.Sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.lukagrahor.jumpbeat.JumpBeatClass;
import com.lukagrahor.jumpbeat.Screens.PlayScreen;
import com.lukagrahor.jumpbeat.Tools.Variables;


public class Hunter extends Sprite {
    public enum State{FALLING,JUMPING,STANDING,RUNNING,DEAD};
    public State currentState;
    public State previousState;
    public World world;//svet v katerem bo deloval Hunter
    public Body b2body;//telo igralčevega lika
    private TextureRegion jumpyStand;
    private TextureRegion jumpyDead;
    private Animation <TextureRegion>jumpyRun;
    private Animation <TextureRegion>jumpyJump;
    private Animation <TextureRegion>jumpyIdle;
    private float stateTimer;//koliko časa je igralec v določenem stanju
    private boolean runningRight;//v katero smer se premika igralec
    private FixtureDef fdef2;
    private FixtureDef fdef3;
    private EdgeShape feet;
    private EdgeShape right;
    private AssetManager manager;
    private Sound sound;
    private boolean jumpyIsDead;
    private BodyDef bdef;
    private EdgeShape glava;
    private EdgeShape rightSide;
    private EdgeShape leftSide;
    private int level;
    public Hunter(PlayScreen screen, int level){
        super(screen.getAtlas3().findRegion("hunter"));
        this.level=level;
        this.world=screen.getWorld();
        currentState= State.STANDING;
        previousState= State.STANDING;
        stateTimer=0;
        runningRight=true;//na začetku bo igralec obrnjen v desno
        Array <TextureRegion> frames =new Array<TextureRegion>();
        for(int i=0;i<4;i++){
            frames.add(new TextureRegion(getTexture(),17+i*40,10,15,35));//pove nam lokacijo slike, ki je del animacije
        }
        jumpyRun = new Animation<TextureRegion>(0.1f,frames);//0.1f pove koliko časa se predvaja posamezna slika, frames pa poda tabelo slik, ki se menjujejo - animacija
        frames.clear();//to določi kateri frame-i spadajo pod jumpyRun --> določa konec

        for(int i=4;i<6;i++){
            frames.add(new TextureRegion(getTexture(),17+i*40,10,18,35));
        }
        //textura za skok
        jumpyJump = new Animation<TextureRegion>(0.1f,frames);
        frames.clear();//to določi kateri frame-i spadajo pod jumpyJump --> določa konec

        for(int i=6;i<8;i++){
            frames.add(new TextureRegion(getTexture(),20+i*40,10,15,35));
        }
        //idle animacija
        jumpyIdle = new Animation<TextureRegion>(0.4f,frames);
        frames.clear();//to določi kateri frame-i spadajo pod jumpyIdle --> določa konec

        //textura za stanje
        jumpyStand = new TextureRegion(getTexture(),17,10,15,35);//poisce koordinate v .pack dokumentu
        //textura za smrt
        jumpyDead=new TextureRegion(screen.getAtlas3().findRegion("hunter"),17,10,15,35);
        defineJumpy();
        setBounds(0,0,15 / JumpBeatClass.PPM,35 / JumpBeatClass.PPM);//doloci velikost
        setRegion(jumpyStand);
        manager = new AssetManager();
        manager.load("audio/sounds/death.mp3", Sound.class);
        manager.finishLoading();
        sound = manager.get("audio/sounds/death.mp3",Sound.class);
    }
    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 - 4/JumpBeatClass.PPM);//postavimo sprite v krog
        setRegion(getFrame(dt));//ta metoda vrne frame, ki ga potrebujemo za prikazovanje slike
    }
    public TextureRegion getFrame(float dt){
        currentState = getState();
        TextureRegion region;
        switch (currentState){
            case DEAD:
                region=jumpyDead;
                break;
            case JUMPING:
                region = jumpyJump.getKeyFrame(stateTimer);//stateTimer določi kateri frame se potegne iz animacije, preveri če je čas ki smo ga zgoraj določili potekel in če je, zamenja trenuten frame z drugim frame-om
                break;
            case RUNNING:
                region = jumpyRun.getKeyFrame(stateTimer,true);//true pomeni, da je ponavljajoča animacija
                break;
            case STANDING:
                region = jumpyIdle.getKeyFrame(stateTimer,true);//true pomeni, da je ponavljajoča animacija
                break;
            case FALLING:
            default:
                region = jumpyStand;
                break;
        }
        if((b2body.getLinearVelocity().x<0 || !runningRight) && !region.isFlipX()){//ko se premika v levo. Region.isFlipx() nam vrne true če je textura obrnjena.
            region.flip(true,false);//Če se igralec premika v levo in textura ni obrnjena v levo, se obrne v levo
            runningRight = false;//sedaj je igralec obrnjen v levo
        }
        else if((b2body.getLinearVelocity().x>0 || runningRight) && region.isFlipX()){//obratno od tega kar se dogaja v gornjem if-u
            region.flip(true,false);
            runningRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer + dt :0; //če se je spremenilo stanje igralca moramo restirati Timer na 0
        previousState = currentState;
        return region;
    }
    public Body getBody(){
        return b2body;
    }
    public State getState(){
        if(jumpyIsDead)
            return State.DEAD;
        if(b2body.getLinearVelocity().y>0 || (b2body.getLinearVelocity().y<0 && previousState == State.JUMPING)) //če je y večji od 0, potem se igralec dviguje, kar pomeni, da skače. Drugi del določa, da se prikaže animacija za skok med padanjem, če je pred tem igralec skočil
            return State.JUMPING;
        else if(b2body.getLinearVelocity().y<0)//če je y manjši od 0, potem igralec pada
            return State.FALLING;
        else if(b2body.getLinearVelocity().x!=0)//če se premika v levo ali desno
            return State.RUNNING;
        else
            return State.STANDING;
    }
    public void hit(){
        sound.play();
        jumpyIsDead=true;//spremeni stanje v dead
        Filter filter=new Filter();
        filter.maskBits=JumpBeatClass.NOTHING_BIT;
        for(Fixture fixture:b2body.getFixtureList()) {//spremeni igralca, da se ne more več premikati in dotikati stvari
            fixture.setFilterData(filter);
        }
        b2body.applyLinearImpulse(new Vector2(0,4f),b2body.getWorldCenter(),true);//vrže igralca v zrak
        Variables.using=true;
    }
    public void headHit(){
        Variables.headH=true;
    }
    public void rightSideHit(){
        Variables.wallHRight=true;
    }
    public void leftSideHit(){
        Variables.wallHLeft=true;
    }
    public void groundHit(){
        Variables.wallHLeft=false;
        Variables.wallHRight=false;
    }
    public boolean isDead(){
        return jumpyIsDead;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    private void defineJumpy() {
        bdef = new BodyDef();
        positionSet();
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();//dejanski fizičen del
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / JumpBeatClass.PPM);

        fdef.filter.categoryBits = JumpBeatClass.JUMPY_BIT;
        //Določi kaj se lahko igralec dotika
        fdef.filter.maskBits = JumpBeatClass.GROUND_BIT |
                JumpBeatClass.ENEMY_BIT |
                JumpBeatClass.SPIKE_BIT |
                JumpBeatClass.ORB_BIT |
                JumpBeatClass.ENEMY_HEAD_BIT |
                JumpBeatClass.FLOATING_BLOCK_BIT|
                JumpBeatClass.TRAMPOLINE_BIT|
                JumpBeatClass.TRAMPOLINE_SURFACE_BIT|
                JumpBeatClass.COIN_BIT|
                JumpBeatClass.CHEST_BIT|
                JumpBeatClass.CHECKPOINT_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        shape.setPosition(new Vector2(0,-14/JumpBeatClass.PPM));
        b2body.createFixture(fdef).setUserData(this);

        //fdef = new FixtureDef();
        feet = new EdgeShape(); //EdgeShape se obnaša kot noge igralca in gladko drsi čez teren brez da bi se spreminjala animacija ob manjših luknjah ali hribčkih
        feet.set(new Vector2(-3 /JumpBeatClass.PPM, -19.1f / JumpBeatClass.PPM), new Vector2(3 / JumpBeatClass.PPM, -19.1f / JumpBeatClass.PPM));
        fdef.shape = feet;
        b2body.createFixture(fdef).setUserData(this);

        //EdgeShape je linija med dvema tockama
        glava = new EdgeShape();
        glava.set(new Vector2(-4/JumpBeatClass.PPM,6/JumpBeatClass.PPM),new Vector2(4/JumpBeatClass.PPM,6/JumpBeatClass.PPM));
        fdef.shape=glava;

        fdef.filter.categoryBits = JumpBeatClass.JUMPY_HEAD_BIT;
        //Določi kaj se lahko igralec dotika
        fdef.filter.maskBits =
                JumpBeatClass.ORB_BIT |
                JumpBeatClass.FLOATING_BLOCK_BIT;

        b2body.createFixture(fdef).setUserData(this);
        //da igralec ne more hodit v zid - desna stran
        rightSide = new EdgeShape();
        rightSide.set(new Vector2(6/JumpBeatClass.PPM,4/JumpBeatClass.PPM),new Vector2(6/JumpBeatClass.PPM,-17/JumpBeatClass.PPM));
        fdef.shape=rightSide;

        fdef.filter.categoryBits = JumpBeatClass.JUMPY_RIGHT_SIDE_BIT;
        //Določi kaj se lahko desni del igralca dotika
        fdef.filter.maskBits =
                        JumpBeatClass.ORB_BIT |
                        JumpBeatClass.FLOATING_BLOCK_BIT;

        b2body.createFixture(fdef).setUserData(this);
        //da igralec ne more hodit v zid - leva stran
        leftSide = new EdgeShape();
        leftSide.set(new Vector2(-6/JumpBeatClass.PPM,4/JumpBeatClass.PPM),new Vector2(-6/JumpBeatClass.PPM,-17/JumpBeatClass.PPM));
        fdef.shape=leftSide;

        fdef.filter.categoryBits = JumpBeatClass.JUMPY_LEFT_SIDE_BIT;
        //Določi kaj se lahko desni del igralca dotika
        fdef.filter.maskBits =
                        JumpBeatClass.ORB_BIT |
                        JumpBeatClass.FLOATING_BLOCK_BIT;

        b2body.createFixture(fdef).setUserData(this);

        //EdgeShape noge = new EdgeShape();//ta EdgeShape je enak kot zgornji, le da se obnaša samo kot senzor
        //noge.set(new Vector2(-3/JumpBeatClass.PPM,-6/JumpBeatClass.PPM),new Vector2(3/JumpBeatClass.PPM,-6/JumpBeatClass.PPM));
        //fdef.shape=noge;
        //fdef.isSensor=true;

        //b2body.createFixture(fdef).setUserData(this);
    }
    public void positionSet(){
        if(level==1&&Variables.checkpointPositionX==0f)
            bdef.position.set(190/JumpBeatClass.PPM,70/JumpBeatClass.PPM);
        else if(level==1&&Variables.checkpointPositionX>0f) {
            bdef.position.set(Variables.checkpointPositionX / JumpBeatClass.PPM, Variables.checkpointPositionY / JumpBeatClass.PPM);
            Variables.using=false;
        }

        if(level==2&&Variables.checkpointPositionX==0f)
            bdef.position.set(170/JumpBeatClass.PPM,70/JumpBeatClass.PPM);
        else if(level==2&&Variables.checkpointPositionX>0f)
            bdef.position.set(Variables.checkpointPositionX/JumpBeatClass.PPM,Variables.checkpointPositionY/JumpBeatClass.PPM);

        if(level==3&&Variables.checkpointPositionX==0f)
            bdef.position.set(80/JumpBeatClass.PPM,170/JumpBeatClass.PPM);
        else if(level==3&&Variables.checkpointPositionX>0f)
            bdef.position.set(Variables.checkpointPositionX/JumpBeatClass.PPM,Variables.checkpointPositionY/JumpBeatClass.PPM);
    }
}