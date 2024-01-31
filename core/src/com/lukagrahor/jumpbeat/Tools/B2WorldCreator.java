package com.lukagrahor.jumpbeat.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.lukagrahor.jumpbeat.JumpBeatClass;
import com.lukagrahor.jumpbeat.Scenes.Hud;
import com.lukagrahor.jumpbeat.Screens.PlayScreen;
import com.lukagrahor.jumpbeat.Sprites.Boar;
import com.lukagrahor.jumpbeat.Sprites.Checkpoint;
import com.lukagrahor.jumpbeat.Sprites.Chest;
import com.lukagrahor.jumpbeat.Sprites.Coin;
import com.lukagrahor.jumpbeat.Sprites.Floating_block;
import com.lukagrahor.jumpbeat.Sprites.Spike;
import com.lukagrahor.jumpbeat.Sprites.StaticOrb;
import com.lukagrahor.jumpbeat.Sprites.Trampoline;
import com.sun.tools.javac.comp.Check;

public class B2WorldCreator {//checkni video 9 kodr dodajas stvari za pickat up
    private Array<Boar> drummy;
    private Array<StaticOrb> orb;
    private Array<Trampoline> trampoline;
    private Array<Coin> coin;
    private Array<Chest> chest;
    private Array<Checkpoint> checkpoint;
    public B2WorldCreator(PlayScreen screen, Hud hud){
        World world=screen.getWorld();
        TiledMap map=screen.getMap();
        BodyDef bdef= new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(MapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;//Static body - za objekte, ki se ne premikajo. Dynamic body - gravitacija deluje nanj, uporabjen za objekte ki se premikajo
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / JumpBeatClass.PPM,(rect.getY()+rect.getHeight() / 2) / JumpBeatClass.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / JumpBeatClass.PPM,rect.getHeight() / 2 / JumpBeatClass.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }
        //ustvari collision za bodice
        for(MapObject object : map.getLayers().get(8).getObjects().getByType(MapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            new Spike(screen,object);
        }
        for(MapObject object : map.getLayers().get(7).getObjects().getByType(MapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            new Floating_block(screen,object);
        }
        //ustvari collision za drummy-je
        drummy = new Array<Boar>();
        for(MapObject object : map.getLayers().get(9).getObjects().getByType(MapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            drummy.add(new Boar(screen,rect.getX()/JumpBeatClass.PPM,rect.getY()/JumpBeatClass.PPM,hud));
        }
        orb = new Array<StaticOrb>();
        for(MapObject object : map.getLayers().get(10).getObjects().getByType(MapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            orb.add(new StaticOrb(screen,rect.getX()/JumpBeatClass.PPM,rect.getY()/JumpBeatClass.PPM,hud));
        }
        trampoline = new Array<Trampoline>();
        for(MapObject object : map.getLayers().get(11).getObjects().getByType(MapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            trampoline.add(new Trampoline(screen,rect.getX()/JumpBeatClass.PPM,rect.getY()/JumpBeatClass.PPM,hud));
        }
        coin = new Array<Coin>();
        for(MapObject object : map.getLayers().get(12).getObjects().getByType(MapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            coin.add(new Coin(screen,rect.getX()/JumpBeatClass.PPM,rect.getY()/JumpBeatClass.PPM,hud));
        }
        chest = new Array<Chest>();
        for(MapObject object : map.getLayers().get(13).getObjects().getByType(MapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            chest.add(new Chest(screen,rect.getX()/JumpBeatClass.PPM,rect.getY()/JumpBeatClass.PPM,hud));
        }
        if(Variables.checkpointsON) {
            checkpoint = new Array<Checkpoint>();
            for (MapObject object : map.getLayers().get(14).getObjects().getByType(MapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                checkpoint.add(new Checkpoint(screen, rect.getX() / JumpBeatClass.PPM, rect.getY() / JumpBeatClass.PPM, hud));
            }
        }
    }
    public Array<Boar> getDrummy() {
        return drummy;
    }

    public Array<StaticOrb> getOrb() {
        return orb;
    }

    public Array<Trampoline> getTrampoline() {
        return trampoline;
    }
    public Array<Coin> getCoin() {
        return coin;
    }
    public Array<Chest> getChest() {
        return chest;
    }
    public Array<Checkpoint> getCheckpoint() {
        return checkpoint;
    }
}