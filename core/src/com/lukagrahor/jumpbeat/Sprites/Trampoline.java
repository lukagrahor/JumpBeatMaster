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
import com.lukagrahor.jumpbeat.Items.StaticItems;
import com.lukagrahor.jumpbeat.JumpBeatClass;
import com.lukagrahor.jumpbeat.Scenes.Hud;
import com.lukagrahor.jumpbeat.Screens.PlayScreen;
import com.lukagrahor.jumpbeat.Tools.Variables;

public class Trampoline extends StaticItems {
    private float stateTime;
    private Animation<TextureRegion> animation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;


    public Trampoline(PlayScreen screen, float x, float y, Hud hud) {
        super(screen, x, y,hud);
        frames = new Array<TextureRegion>();
        for(int i = 0; i < 9; i++){
            frames.add(new TextureRegion(screen.getAtlas4().findRegion("trampoline-Sheet"),i*27,0,27,16));
        }
        animation = new Animation<TextureRegion>(0.1f,frames);
        stateTime = 0;
        setBounds(getX(),getY(),27/JumpBeatClass.PPM,16/JumpBeatClass.PPM);
        setToDestroy = false;
        destroyed = false;
    }

    public void update(float dt){
        stateTime += dt;
        if(setToDestroy && !destroyed) {
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas4().findRegion("trampoline-Sheet"),0,0,27,16));
            setRegion(animation.getKeyFrame(stateTime, true));
            stateTime = 0;
        }
        else if(!destroyed) {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(new TextureRegion(screen.getAtlas4().findRegion("trampoline-Sheet"),0,0,27,16));
        }
    }
    protected void defineStaticItems() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();//dejanski fizičen del
        CircleShape shape = new CircleShape();
        fdef.filter.categoryBits = JumpBeatClass.TRAMPOLINE_BIT;
        fdef.filter.maskBits = JumpBeatClass.GROUND_BIT |
                JumpBeatClass.JUMPY_BIT |
                JumpBeatClass.FLOATING_BLOCK_BIT;
        shape.setRadius(8 / JumpBeatClass.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-10,11).scl(1/JumpBeatClass.PPM);
        vertice[1] = new Vector2(10,11).scl(1/JumpBeatClass.PPM);
        vertice[2] = new Vector2(-9,8).scl(1/JumpBeatClass.PPM);
        vertice[3] = new Vector2(9,8).scl(1/JumpBeatClass.PPM);
        head.set(vertice);//polygonu head dodamo točke iz tabele vertice
        fdef.shape = head; //dodam glavo
        fdef.restitution = 3f;//Odboj - 1 pomeni, da če se igralec pade za 10px in se dotakne glave nasprotnika, se bo odbil za 10 px v zrak
        fdef.filter.categoryBits = JumpBeatClass.TRAMPOLINE_SURFACE_BIT;
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
    }
}
