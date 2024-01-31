package com.lukagrahor.jumpbeat.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lukagrahor.jumpbeat.Scenes.Controller;
import com.lukagrahor.jumpbeat.Items.Item;
import com.lukagrahor.jumpbeat.Items.ItemDef;
import com.lukagrahor.jumpbeat.Items.Orb;
import com.lukagrahor.jumpbeat.Items.StaticItems;
import com.lukagrahor.jumpbeat.JumpBeatClass;
import com.lukagrahor.jumpbeat.Scenes.Hud;
import com.lukagrahor.jumpbeat.Sprites.Checkpoint;
import com.lukagrahor.jumpbeat.Sprites.Chest;
import com.lukagrahor.jumpbeat.Sprites.Coin;
import com.lukagrahor.jumpbeat.Sprites.Enemy;
import com.lukagrahor.jumpbeat.Sprites.Hunter;
import com.lukagrahor.jumpbeat.Sprites.Trampoline;
import com.lukagrahor.jumpbeat.Tools.B2WorldCreator;
import com.lukagrahor.jumpbeat.Tools.FileEditing;
import com.lukagrahor.jumpbeat.Tools.WorldContactListener;
import com.lukagrahor.jumpbeat.Tools.Variables;

import java.util.concurrent.LinkedBlockingQueue;


public class PlayScreen implements Screen{
    private OrthographicCamera gameCam;
    private JumpBeatClass game;
    private Viewport gamePort;
    private Hud hud;
    private TextureAtlas atlas;
    private TextureAtlas atlas2;
    private TextureAtlas character;
    private TextureAtlas characterGold;
    private TextureAtlas trampoline;
    private TextureAtlas coin;
    private TextureAtlas chest;
    private TextureAtlas checkpoint;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    //box2d zadeve
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;
    //sprite-i
    private Hunter player;
    //item-i
    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;
    //glasba
    private AssetManager manager;
    private Music music;

    private Hunter.State state;

    private Controller controller;

    //parallax
    private OrthogonalTiledMapRenderer backgroundRenderer;
    private OrthogonalTiledMapRenderer cloudRenderer;
    private OrthogonalTiledMapRenderer midgroundRenderer;
    private OrthogonalTiledMapRenderer foregroundRenderer;
    private OrthogonalTiledMapRenderer groundRenderer;
    private OrthographicCamera camera2;
    private Viewport viewport2;
    private MapLayers mapLayers;
    private TiledMapTileLayer backgroundLayer;
    private TiledMapTileLayer paralaxLayer;
    private TiledMapTileLayer graphicsLayer;
    private TiledMapTileLayer caveLayer;
    private TiledMapTileLayer midgroundLayer;
    private TiledMapTileLayer foregroundLayer;
    private TiledMapTileLayer groundLayer;
    private OrthographicCamera camera3;
    private Viewport viewport3;

    private OrthographicCamera camera4;
    private Viewport viewport4;

    private OrthographicCamera camera5;
    private Viewport viewport5;

    private OrthographicCamera camera6;
    private Viewport viewport6;

    private float timeCount;

    private int mapNumber;
    private FileEditing fe;

    public PlayScreen(JumpBeatClass game,int mapNumber){
        this.mapNumber=mapNumber;
        this.mapNumber=mapNumber;
        Variables.wallHRight=false;
        timeCount = 0;
        atlas = new TextureAtlas("boar.atlas");
        atlas2 = new TextureAtlas("movementOrb.atlas");
        character = new TextureAtlas("character.atlas");
        characterGold = new TextureAtlas("hunterGold.atlas");
        trampoline= new TextureAtlas("jump.atlas");
        coin= new TextureAtlas("coinAnimation.atlas");
        chest=new TextureAtlas("chestAnimation.atlas");
        checkpoint=new TextureAtlas("checkpointAnimation.atlas");
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(JumpBeatClass.V_WIDTH / JumpBeatClass.PPM,JumpBeatClass.V_HEIGHT / JumpBeatClass.PPM,gameCam);//ScreenViewport pomeni, da če imaš večji zaslon boš več videl, FitScreenViewPort pa pomeni, da se ohrani aspect ratio in dodajo črni robovi ob potrebi

        camera2 = new OrthographicCamera();
        viewport2= new FitViewport(JumpBeatClass.V_WIDTH / JumpBeatClass.PPM,JumpBeatClass.V_HEIGHT / JumpBeatClass.PPM,camera2);

        camera3 = new OrthographicCamera();
        viewport3= new FitViewport(JumpBeatClass.V_WIDTH / JumpBeatClass.PPM,JumpBeatClass.V_HEIGHT / JumpBeatClass.PPM,camera3);

        camera4 = new OrthographicCamera();
        viewport4= new FitViewport(JumpBeatClass.V_WIDTH / JumpBeatClass.PPM,JumpBeatClass.V_HEIGHT / JumpBeatClass.PPM,camera4);

        camera5 = new OrthographicCamera();
        viewport5= new FitViewport(JumpBeatClass.V_WIDTH / JumpBeatClass.PPM,JumpBeatClass.V_HEIGHT / JumpBeatClass.PPM,camera5);

        camera6 = new OrthographicCamera();
        viewport6= new FitViewport(JumpBeatClass.V_WIDTH / JumpBeatClass.PPM,JumpBeatClass.V_HEIGHT / JumpBeatClass.PPM,camera6);
        hud = new Hud(game);
        fe=new FileEditing(hud);
        mapLoader = new TmxMapLoader();
        if(mapNumber==1)
            map = mapLoader.load("novaMapa.tmx");
        else if(mapNumber==2)
            map = mapLoader.load("level2.tmx");
        else if(mapNumber==3)
            map = mapLoader.load("level3.tmx");
        else
            map = mapLoader.load("novaMapa.tmx");

        mapLayers = map.getLayers();

        backgroundLayer = (TiledMapTileLayer) mapLayers.get("Background");
        graphicsLayer= (TiledMapTileLayer) mapLayers.get("Graphics");
        paralaxLayer = (TiledMapTileLayer) mapLayers.get("ParalaxOblaki");
        midgroundLayer= (TiledMapTileLayer) mapLayers.get("Midground");
        foregroundLayer= (TiledMapTileLayer) mapLayers.get("Foreground");
        caveLayer= (TiledMapTileLayer) mapLayers.get("Cave");
        groundLayer= (TiledMapTileLayer) mapLayers.get("GroundL");

        cloudRenderer = new OrthogonalTiledMapRenderer(map,1 / JumpBeatClass.PPM);
        midgroundRenderer = new OrthogonalTiledMapRenderer(map,1 / JumpBeatClass.PPM);
        backgroundRenderer = new OrthogonalTiledMapRenderer(map,1 / JumpBeatClass.PPM);
        foregroundRenderer = new OrthogonalTiledMapRenderer(map,1 / JumpBeatClass.PPM);
        groundRenderer = new OrthogonalTiledMapRenderer(map,1 / JumpBeatClass.PPM);
        renderer = new OrthogonalTiledMapRenderer(map,1 / JumpBeatClass.PPM);

        gameCam.position.set(gamePort.getWorldWidth()/2,gamePort.getWorldHeight()/2,0);//centriramo kamero
        camera2.position.set(viewport2.getWorldWidth()/2+Variables.checkpointPositionX2,viewport2.getWorldHeight()/2,0);
        camera3.position.set(viewport3.getWorldWidth()/2+Variables.checkpointPositionX2,viewport3.getWorldHeight()/2,0);
        camera4.position.set(viewport4.getWorldWidth()/2+Variables.checkpointPositionX2,viewport4.getWorldHeight()/2,0);
        camera5.position.set(viewport5.getWorldWidth()/2+Variables.checkpointPositionX2,viewport5.getWorldHeight()/2,0);
        camera6.position.set(viewport6.getWorldWidth()/2+Variables.checkpointPositionX2,viewport6.getWorldHeight()/2,0);


        world = new World(new Vector2(0,-10),true);
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this,hud);
        manager = new AssetManager();
        if(mapNumber==1) {
            manager.load("audio/music/AcousticGuitar1.mp3", Music.class);
            player = new Hunter(this,1);
        }
        else if(mapNumber==2) {
            manager.load("audio/music/Essence.mp3", Music.class);
            player = new Hunter(this,2);
        }
        else if(mapNumber==3) {
            manager.load("audio/music/SkokMusic.mp3", Music.class);
            player = new Hunter(this,3);
        }
        manager.finishLoading();
        if(mapNumber==1)
            music = manager.get("audio/music/AcousticGuitar1.mp3",Music.class);
        else if(mapNumber==2)
            music = manager.get("audio/music/Essence.mp3",Music.class);
        else if(mapNumber==3)
            music = manager.get("audio/music/SkokMusic.mp3",Music.class);

        world.setContactListener(new WorldContactListener());
        state=player.getState();

        music.setLooping(true);
        music.play();
        items = new Array<Item>();
        itemsToSpawn=new LinkedBlockingQueue<ItemDef>();

        controller=new Controller(game);


    }
    public void spawnItem(ItemDef idef){
        itemsToSpawn.add(idef);
    }
    public void handleItemSpawns(){
        if(!itemsToSpawn.isEmpty()){
            ItemDef idef=itemsToSpawn.poll();
            if(idef.type== Orb.class)
                items.add(new Orb(this,idef.position.x,idef.position.y));
        }
    }
    public TextureAtlas getAtlas(){
        return atlas;
    }
    public TextureAtlas getAtlas2(){
        return atlas2;
    }
    public TextureAtlas getAtlas3(){
        if(!Variables.goldHat)
            return character;
        else
            return characterGold;
    }
    public TextureAtlas getAtlas4(){
        return trampoline;
    }
    public TextureAtlas getAtlas5(){
        return coin;
    }
    public TextureAtlas getAtlas6(){
        return chest;
    }
    public TextureAtlas getAtlas7(){
        return checkpoint;
    }
    @Override
    public void show() {

    }
    public void handleInput(float dt) {
        boolean moveRight = (Gdx.input.isKeyPressed(Input.Keys.RIGHT) ||Gdx.input.isKeyPressed(Input.Keys.D));
        boolean moveLeft = (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A));
        boolean moveUp = (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.SPACE));
        if(player.currentState != Hunter.State.DEAD) {//če igralec umre, se ne more več premikati
            //android kontrole
            if(Gdx.app.getType() == Application.ApplicationType.Android) {
                moveRight=controller.isRightPressed();
                moveLeft=controller.isLeftPressed();
                if(!Variables.using) {
                    if (moveRight) {
                        if(player.b2body.getLinearVelocity().x>0f) {
                            camera2.position.x += 1.3f * dt;
                            camera6.position.x += 0.90f * dt;
                            camera5.position.x += 0.80f * dt;
                            camera4.position.x += 0.70f * dt;
                            camera3.position.x += 0.60f * dt;
                        }
                        while(player.b2body.getLinearVelocity().x <= 1.05f){
                            player.b2body.applyLinearImpulse(new Vector2(0.2f, 0), player.b2body.getWorldCenter(), true);
                        }
                    }
                    else if (moveLeft) {
                        if(player.b2body.getLinearVelocity().x<0f) {
                            camera2.position.x -= 1.3f * dt;
                            camera6.position.x -= 0.90f * dt;
                            camera5.position.x -= 0.80f * dt;
                            camera4.position.x -= 0.70f * dt;
                            camera3.position.x -= 0.60f * dt;
                        }
                        while(player.b2body.getLinearVelocity().x >= -1.05f){
                            player.b2body.applyLinearImpulse(new Vector2(-0.2f, 0), player.b2body.getWorldCenter(), true);
                        }
                    }
                    else if (moveRight == moveLeft) {
                        player.b2body.setLinearVelocity(0f, player.b2body.getLinearVelocity().y);//onemogoča drsenje
                    }
                }
                else{
                    if(Variables.wallHRight) {
                        player.hit();
                    }
                    camera2.position.x += 1.3f * dt;
                    camera6.position.x += 0.90f * dt;
                    camera5.position.x += 0.80f * dt;
                    camera4.position.x += 0.70f * dt;
                    camera3.position.x += 0.60f * dt;
                    while(player.b2body.getLinearVelocity().x <= 1.05f) {
                        player.b2body.applyLinearImpulse(new Vector2(0.2f, 0), player.b2body.getWorldCenter(), true);
                    }
                }

                if (Gdx.input.isTouched()) { // vedno ko pritisnemo na zaslon se to zažene
                    //naslednja koda omogoča, da lahko igralec skače tudi, če z enim prstom drži gumb za premikanje in preprečuje, da bi igralčev lik skočil ko ima igralec na zaslonu le en prst in to na gumbu za premikanje
                    final int MAX_NUMBER_OF_POINTERS = 2;
                    int x1=0;
                    int x2=0;

                    for(int i = 0; i < MAX_NUMBER_OF_POINTERS; i++)//zaznava koliko prstov je na zaslonu
                    {
                        if( Gdx.input.isTouched(0) ) {//beleži x koordinato za prvi prst
                            x1 = Gdx.input.getX(0);
                        }
                        if( Gdx.input.isTouched(1) ) {//beleži x koordinato za drugi prst
                            x2 = Gdx.input.getX(1);
                        }
                    }
                    if(!Variables.headH) {
                        if (player.getBody().getLinearVelocity().y == 0 && x2 > 0) {
                            player.b2body.applyLinearImpulse(new Vector2(0, 3f), player.b2body.getWorldCenter(), true);//igralec lahko skače ob premikanju
                        } else if (player.getBody().getLinearVelocity().y == 0 && x1 > 750) {//skakanje na mestu
                            player.b2body.applyLinearImpulse(new Vector2(0, 3f), player.b2body.getWorldCenter(), true);//igralec lahko skače ko je na mestu in ne drži gumba za premikanje
                        } else if (moveRight == moveLeft && !Variables.using) {
                            player.b2body.setLinearVelocity(0f, player.b2body.getLinearVelocity().y);//onemogoča drsenje
                        }
                    }
                    else{
                        Variables.headH=false;
                    }
                }
            }
            //pc kontrole
            else {
                if (moveUp) {// lahko uporabimo force - postopna sprememba hitrosti ali impulse - takojšna sprememba hitrosti
                    if(!Variables.headH) {
                        if (player.getBody().getLinearVelocity().y == 0) {
                            player.b2body.applyLinearImpulse(new Vector2(0, 3f), player.b2body.getWorldCenter(), true);
                        }
                    }
                    else{
                        Variables.headH=false;//da se igralec ne zatakne v strop
                    }
                }
                if(!Variables.using) {

                    if (moveRight) {
                        if(player.b2body.getLinearVelocity().x>0f) {
                            camera2.position.x += 1.3f * dt;
                            camera6.position.x += 0.90f * dt;
                            camera5.position.x += 0.80f * dt;
                            camera4.position.x += 0.70f * dt;
                            camera3.position.x += 0.60f * dt;
                        }
                        while (player.b2body.getLinearVelocity().x <= 1.05f) {
                            player.b2body.applyLinearImpulse(new Vector2(0.2f, 0), player.b2body.getWorldCenter(), true);
                        }
                    }
                    else if (moveLeft) {
                            if(player.b2body.getLinearVelocity().x<0f) {
                                camera2.position.x -= 1.3f * dt;
                                camera6.position.x -= 0.90f * dt;
                                camera5.position.x -= 0.80f * dt;
                                camera4.position.x -= 0.70f * dt;
                                camera3.position.x -= 0.60f * dt;
                            }
                        while(player.b2body.getLinearVelocity().x >= -1.05f){
                            player.b2body.applyLinearImpulse(new Vector2(-0.2f, 0), player.b2body.getWorldCenter(), true);
                        }
                        Variables.wallHRight=false;
                    }
                    else {
                        player.b2body.setLinearVelocity(0f, player.b2body.getLinearVelocity().y);
                    }
                }
                else{
                    if(Variables.wallHRight) {
                        player.hit();
                    }
                        camera2.position.x += 1.3f * dt;
                        camera6.position.x += 0.90f * dt;
                        camera5.position.x += 0.80f * dt;
                        camera4.position.x += 0.70f * dt;
                        camera3.position.x += 0.60f * dt;

                        while (player.b2body.getLinearVelocity().x <= 1.05f) {
                            player.b2body.applyLinearImpulse(new Vector2(0.2f, 0), player.b2body.getWorldCenter(), true);
                        }
                }
            }

        }
    }
    public void update (float dt) {//kar se spreminja s časom mora bit tukaj
        handleInput(dt);
        handleItemSpawns();
        if (player.getY() < 0&&!player.isDead()){
            player.hit();
        }
        else if (player.getY() > 1.70f&&!player.isDead()){
            gameCam.position.y=2.25f;
        }
        else{
            gameCam.position.y=0.9f;
        }

        world.step(1/60f,6,2);//kako reagirajo dve telesi med collision-om
        player.update(dt);
        for(Enemy enemy : creator.getDrummy()){
            enemy.update(dt);
            if(enemy.getX()< player.getX() + 155/JumpBeatClass.PPM)
                enemy.b2body.setActive(true);//aktivira nasprotnike --> se začnejo premikati
        }
        //stvari, ki so določene v Tiled, poda jim delta time in jim omogoči animacijo
        for(StaticItems sItems : creator.getOrb()){
            sItems.update(dt);
        }
        for(Item item : items) {
            item.update(dt);
        }
        for(Trampoline trampoline : creator.getTrampoline()) {
            trampoline.update(dt);
        }
        for(Coin coin : creator.getCoin()) {
            coin.update(dt);
        }
        for(Chest chest : creator.getChest()) {
            chest.update(dt);
        }
        if(Variables.checkpointsON) {
            for (Checkpoint checkpoint : creator.getCheckpoint()) {
                checkpoint.update(dt);
            }
        }
        hud.update(dt);
        if(player.currentState != Hunter.State.DEAD) {//ko igralec umre, mu kamera ne sledi več
            gameCam.position.x = player.b2body.getPosition().x;//kamera se premika z igralcem
            //camera2.position.x = player.b2body.getPosition().x;
        }
        camera2.update();
        camera3.update();
        camera4.update();
        camera5.update();
        camera6.update();
        gameCam.update();

        renderer.setView(gameCam);
        backgroundRenderer.setView(camera3);
        midgroundRenderer.setView(camera4);
        foregroundRenderer.setView(camera5);
        groundRenderer.setView(camera6);
        cloudRenderer.setView(camera2);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0,0,0,1);//barva
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);//očisti zaslon
        backgroundRenderer.getBatch().begin();
        backgroundRenderer.renderTileLayer(backgroundLayer);
        backgroundRenderer.getBatch().end();
        midgroundRenderer.getBatch().begin();
        midgroundRenderer.renderTileLayer(midgroundLayer);
        midgroundRenderer.getBatch().end();
        foregroundRenderer.getBatch().begin();
        foregroundRenderer.renderTileLayer(foregroundLayer);
        foregroundRenderer.getBatch().end();
        groundRenderer.getBatch().begin();
        groundRenderer.renderTileLayer(groundLayer);
        groundRenderer.getBatch().end();
        renderer.getBatch().begin();
        renderer.renderTileLayer(caveLayer);
        renderer.renderTileLayer(graphicsLayer);
        renderer.getBatch().end();
        cloudRenderer.getBatch().begin();
        cloudRenderer.renderTileLayer(paralaxLayer);
        cloudRenderer.getBatch().end();
        //b2dr.render(world,gameCam.combined);//pokaže hitboxe
        if(Gdx.app.getType() == Application.ApplicationType.Android&& !Variables.using)
            controller.draw();

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);//nariše texturo igralca
        for(Enemy enemy : creator.getDrummy()){
            enemy.draw(game.batch);
        }
        for(StaticItems sItems : creator.getOrb()){
            sItems.draw(game.batch);
        }
        for(Item item : items){
            item.draw(game.batch);
        }
        for(Trampoline trampoline : creator.getTrampoline()){//izriše texturo trampolina
            trampoline.draw(game.batch);
        }
        for(Coin coin : creator.getCoin()){//izriše texturo kovanca
            coin.draw(game.batch);
        }
        for(Chest chest : creator.getChest()){//izriše texturo skrinje
            chest.draw(game.batch);
        }
        if(Variables.checkpointsON) {
            for (Checkpoint checkpoint : creator.getCheckpoint()) {//izriše texturo skrinje
                checkpoint.draw(game.batch);
            }
        }

        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);//render-a samo to tisto kar kamera vidi
        hud.stage.draw();
        if(gameOver()){
            fe.readSc();
            game.setScreen(new GameOverScreen(game,mapNumber));
            dispose();
        }
        else if(mapNumber==1) {
            if (player.getX() > 38.0f && !player.isDead()) {
                fe.addScore();
                fe.readSc();
                Variables.checkpointPositionX=0f;
                Variables.checkpointPositionX2=0f;
                Variables.checkpointPositionY=0f;
                game.setScreen(new MenuScreen((JumpBeatClass) game));
                dispose();
            } else if (player.getX() < 0.38f && !player.isDead()) {
                fe.addScore();
                Variables.checkpointPositionX=0f;
                Variables.checkpointPositionX2=0f;
                Variables.checkpointPositionY=0f;
                game.setScreen(new MenuScreen((JumpBeatClass) game));
                dispose();
            }
        }
        else if(mapNumber==2) {
            if (player.getX() > 85.0f && !player.isDead()) {
                fe.addScore();
                fe.readSc();
                Variables.checkpointPositionX=0f;
                Variables.checkpointPositionX2=0f;
                Variables.checkpointPositionY=0f;
                game.setScreen(new MenuScreen((JumpBeatClass) game));
                dispose();
            } else if (player.getX() < 0.38f && !player.isDead()) {
                fe.addScore();
                Variables.checkpointPositionX=0f;
                Variables.checkpointPositionX2=0f;
                Variables.checkpointPositionY=0f;
                game.setScreen(new MenuScreen((JumpBeatClass) game));
                dispose();
            }
        }
        else if(mapNumber==3) {
            if (player.getX() > 46.5f && !player.isDead()) {
                fe.addScore();
                fe.readSc();
                Variables.checkpointPositionX=0f;
                Variables.checkpointPositionX2=0f;
                Variables.checkpointPositionY=0f;
                game.setScreen(new MenuScreen((JumpBeatClass) game));
                dispose();
            } else if (player.getX() < 0.38f && !player.isDead()) {
                fe.addScore();
                Variables.checkpointPositionX=0f;
                Variables.checkpointPositionX2=0f;
                Variables.checkpointPositionY=0f;
                game.setScreen(new MenuScreen((JumpBeatClass) game));
                dispose();
            }
        }
    }
    public boolean gameOver(){
        if(player.currentState== Hunter.State.DEAD && player.getStateTimer()>3){
            return true;
        }
        return false;
    }
    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height); //ko spremenimo velikost zaslona, mora to vedeti Viewport
        viewport2.update(width,height);
        viewport3.update(width,height);
        viewport4.update(width,height);
        viewport5.update(width,height);
        viewport6.update(width,height);
        controller.resize(width,height);
    }
    public TiledMap getMap(){
        return map;
    }
    public World getWorld(){
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        cloudRenderer.dispose();
        midgroundRenderer.dispose();
        foregroundRenderer.dispose();
        groundRenderer.dispose();
        backgroundRenderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
        manager.dispose();
        music.dispose();
    }
}