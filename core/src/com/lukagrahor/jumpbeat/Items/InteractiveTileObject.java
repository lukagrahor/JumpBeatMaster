package com.lukagrahor.jumpbeat.Items;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.lukagrahor.jumpbeat.JumpBeatClass;
import com.lukagrahor.jumpbeat.Screens.PlayScreen;

public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected MapObject object;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;
    protected PlayScreen screen;

    public InteractiveTileObject(PlayScreen screen, MapObject object){
        this.object=object;
        this.screen=screen;
        this.world=screen.getWorld();
        this.map=screen.getMap();
        this.bounds=((RectangleMapObject) object).getRectangle();

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;//Static body - za objekte, ki se ne premikajo. Dynamic body - gravitacija deluje nanj, uporabjen za objekte ki se premikajo
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / JumpBeatClass.PPM,(bounds.getY()+bounds.getHeight() / 2) / JumpBeatClass.PPM);

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 2 / JumpBeatClass.PPM,bounds.getHeight() / 2 / JumpBeatClass.PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);//zajamemo fixture, ki obkoljuje objekte
    }

    public abstract void  onHeadHit();
    public abstract void  onFeetHit();
    public void setCategoryFilter(short filterBit){ //Omogoči da filtri delujejo
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }
    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int)(body.getPosition().x * JumpBeatClass.PPM / 16),
                (int)(body.getPosition().y * JumpBeatClass.PPM / 16));
    }
    }
