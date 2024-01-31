package com.lukagrahor.jumpbeat.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lukagrahor.jumpbeat.JumpBeatClass;
import com.lukagrahor.jumpbeat.Tools.Variables;

public class LevelSelectScreen implements Screen, InputProcessor {
    private JumpBeatClass game;
    //mapa
    private TmxMapLoader mapLoader;
    private TiledMap map;
    //parallax
    private OrthogonalTiledMapRenderer backgroundRenderer;
    private OrthogonalTiledMapRenderer cloudRenderer;
    private OrthographicCamera camera;
    private OrthographicCamera cameraBg;
    private MapLayers mapLayers;
    private TiledMapTileLayer backgroundLayer;
    private TiledMapTileLayer paralaxLayer;
    private Viewport viewport;
    private Viewport viewportBg;

    //glasba
    private AssetManager manager;
    private Music music;

    //gumbi
    private Stage bgr;
    private Stage stage;
    private Table table;
    private Texture background;
    private Texture lvl1;
    private Texture lvl2;
    private Texture lvl3;
    private Image bg;
    private Image image;
    private Image image2;
    private Image image3;
    private Vector3 temp;

    private TiledMapTileLayer midgroundLayer;
    private TiledMapTileLayer foregroundLayer;
    private TiledMapTileLayer groundLayer;

    private OrthographicCamera camera3;
    private Viewport buttonViewport;
    private int mapNumber=1;

    boolean click=false;
    public LevelSelectScreen(JumpBeatClass game){
        Variables.using=true;
        this.game=game;
        cameraBg = new OrthographicCamera();
        viewportBg= new FitViewport(JumpBeatClass.V_WIDTH,JumpBeatClass.V_HEIGHT,cameraBg);

        camera = new OrthographicCamera();
        viewport= new FitViewport(JumpBeatClass.V_WIDTH,JumpBeatClass.V_HEIGHT,camera);
        cameraBg.position.set(viewportBg.getWorldWidth()/2,viewportBg.getWorldHeight()/2,0);
        camera.position.set(viewport.getWorldWidth()/2,viewport.getWorldHeight()/2,0);
        //nastavitev glasbe
        manager = new AssetManager();
        manager.load("audio/music/Words.mp3", Music.class);
        manager.finishLoading();
        music = manager.get("audio/music/Words.mp3",Music.class);
        music.setLooping(true);
        music.play();
        //kamera za gumbe
        bgr=new Stage(viewportBg,game.batch);
        stage=new Stage(viewport,game.batch);
        Gdx.input.setInputProcessor(stage);
        //podlaga za gumbe
        Table tableBg=new Table();
        tableBg.center();
        tableBg.setFillParent(true);//se centrira glede na ozadje

        Table table = new Table();
        table.center();
        table.setFillParent(true);//se centrira glede na ozadje
        //slike gumbov
        background = new Texture("backgroundV7.png");
        lvl1 = new Texture("level1.png");
        lvl2 = new Texture("lvl2.png");
        lvl3 = new Texture("lvl3.png");

        bg=new Image(background);
        image=new Image(lvl1);
        image2=new Image(lvl2);
        image3=new Image(lvl3);


        image.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                click=true;
                mapNumber=1;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                click=false;
            }
        });
        image2.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                click=true;
                mapNumber=2;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                click=false;
            }
        });
        image3.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                click=true;
                mapNumber=3;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                click=false;
            }
        });
        //downSprite.setPosition(300,100);
        tableBg.add(bg);
        bgr.addActor(tableBg);

        table.add(image).expandX();
        table.add(image2).expandX();
        table.add(image3).expandX();
        stage.addActor(table);

        //Gdx.input.setInputProcessor(this);

    }

    @Override
    public void show() {

    }

    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        bgr.draw();
        stage.draw();
        if(click) {
            game.setScreen(new PlayScreen(game,mapNumber));
            dispose();
        }
    }
    public void update(float dt){
        handleInput(dt);
        cameraBg.update();
        camera.update();
        bgr.act(dt);
        stage.act(dt);
    }
    public void handleInput(float dt){

    }
    @Override
    public void resize(int width, int height) {
        viewportBg.update(width,height);
        viewport.update(width,height);
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
        background.dispose();
        lvl1.dispose();
        lvl2.dispose();
        lvl3.dispose();
        manager.dispose();
        music.dispose();
        bgr.dispose();
        stage.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
