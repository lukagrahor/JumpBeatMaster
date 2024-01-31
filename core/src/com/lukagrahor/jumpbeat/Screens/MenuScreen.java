package com.lukagrahor.jumpbeat.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lukagrahor.jumpbeat.JumpBeatClass;
import com.lukagrahor.jumpbeat.Tools.FileEditing;
import com.lukagrahor.jumpbeat.Tools.Variables;

public class MenuScreen implements Screen, InputProcessor {

    private JumpBeatClass game;
    //mapa
    private TmxMapLoader mapLoader;
    private TiledMap map;
    //parallax
    private OrthogonalTiledMapRenderer backgroundRenderer;
    private OrthogonalTiledMapRenderer cloudRenderer;
    private OrthographicCamera camera;
    private OrthographicCamera camera2;
    private  MapLayers mapLayers;
    private TiledMapTileLayer backgroundLayer;
    private TiledMapTileLayer paralaxLayer;
    private Viewport viewport;
    private Viewport viewport2;

    //glasba
    private AssetManager manager;
    private Music music;

    //gumbi
    private Stage stage;
    private Texture playButton;
    private Image image;
    private Texture shopButton;
    private Image image2;
    private Texture helpButton;
    private Image image3;


    private TiledMapTileLayer midgroundLayer;
    private TiledMapTileLayer foregroundLayer;
    private TiledMapTileLayer groundLayer;

    private OrthographicCamera camera3;
    private Viewport buttonViewport;

    Texture exitB;
    Image exit;
    boolean back=false;

    boolean click=false;
    boolean click2=false;
    boolean click3=false;

    private FileHandle fh;
    private FileEditing fe;

    Label scoreLabel;

    @SuppressWarnings("DefaultLocale")
    public MenuScreen(JumpBeatClass game){
        Variables.using=true;
        this.game=game;

        camera = new OrthographicCamera();
        viewport= new FitViewport(JumpBeatClass.V_WIDTH / JumpBeatClass.PPM,JumpBeatClass.V_HEIGHT / JumpBeatClass.PPM,camera);

        camera2 = new OrthographicCamera();
        viewport2= new FitViewport(JumpBeatClass.V_WIDTH / JumpBeatClass.PPM,JumpBeatClass.V_HEIGHT / JumpBeatClass.PPM,camera2);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("menu.tmx");
        mapLayers = map.getLayers();
        backgroundLayer = (TiledMapTileLayer) mapLayers.get("Background");
        paralaxLayer = (TiledMapTileLayer) mapLayers.get("ParalaxOblaki");
        midgroundLayer= (TiledMapTileLayer) mapLayers.get("Midground");
        foregroundLayer= (TiledMapTileLayer) mapLayers.get("Foreground");
        groundLayer= (TiledMapTileLayer) mapLayers.get("GroundL");

        backgroundRenderer = new OrthogonalTiledMapRenderer(map,1 / JumpBeatClass.PPM);
        cloudRenderer = new OrthogonalTiledMapRenderer(map,1 / JumpBeatClass.PPM);

        camera.position.set(viewport.getWorldWidth()/2,viewport.getWorldHeight()/2,0);
        camera2.position.set(viewport2.getWorldWidth()/2,viewport2.getWorldHeight()/2,0);
        //nastavitev glasbe
        manager = new AssetManager();
        manager.load("audio/music/RiverMeditation.mp3", Music.class);
        manager.finishLoading();
        music = manager.get("audio/music/RiverMeditation.mp3",Music.class);
        music.setLooping(true);
        music.play();
        //kamera za gumbe
        camera3 = new OrthographicCamera();
        buttonViewport= new FitViewport(320,180,camera3);
        stage=new Stage(buttonViewport,game.batch);
        Gdx.input.setInputProcessor(stage);
        //podlaga za gumbe
        Table table = new Table();
        table.center();
        table.setFillParent(true);//se centrira glede na ozadje
        //slike gumbov
        playButton = new Texture("play.png");

        image=new Image(playButton);
        image.setSize(150,35);
        image.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                click=true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                click=false;
            }
        });
        shopButton = new Texture("shop.png");

        image2=new Image(shopButton);
        image2.setSize(150,35);
        image2.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                click2=true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                click2=false;
            }
        });

        helpButton = new Texture("help.png");

        image3=new Image(helpButton);
        image3.setSize(150,35);
        image3.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                click3=true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                click3=false;
            }
        });

        exitB = new Texture("exit3.png");

        exit = new Image(new Texture("exit3.png"));//puščica desno
        exit.setSize(150,35);
        exit.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                back=true;
                String a="";
                a+=back;
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                back=false;
            }
        });
        fh= Gdx.files.local("Score.txt");
        if(!fh.exists()){
            fh.writeString("0",false);
        }

        fe=new FileEditing();
        Table table2 = new Table();
        table2.top().right();
        table2.setFillParent(true);//se centrira glede na ozadje
        fe.setUp();
        int a=fe.returnOldSc();
        scoreLabel = new Label(String.format("%01d",a),new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table2.add(scoreLabel).padTop(5f);
        table2.add(scoreLabel).padRight(5f);

        table.add(image).padTop(40f);
        table.row();
        table.add(image2).padTop(5f);
        table.row();
        table.add(image3).padTop(5f);
        table.row();
        table.add(exit).padTop(10f);
        stage.addActor(table);
        stage.addActor(table2);
    }

    @Override
    public void show() {

    }

    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        backgroundRenderer.setView(camera.combined, (camera.position.x - camera.viewportWidth / 2), camera.position.y - camera.viewportHeight / 2, camera.viewportWidth, camera.viewportHeight);
        backgroundRenderer.getBatch().begin();
        backgroundRenderer.renderTileLayer(backgroundLayer);
        backgroundRenderer.renderTileLayer(midgroundLayer);
        backgroundRenderer.renderTileLayer(foregroundLayer);
        backgroundRenderer.renderTileLayer(groundLayer);
        backgroundRenderer.getBatch().end();
        cloudRenderer.setView(camera2.combined, (camera2.position.x - camera2.viewportWidth / 2), camera2.position.y - camera2.viewportHeight / 2, camera2.viewportWidth, camera2.viewportHeight);
        cloudRenderer.getBatch().begin();
        cloudRenderer.renderTileLayer(paralaxLayer);
        cloudRenderer.getBatch().end();
        stage.draw();
        camera2.position.x += 0.4 * delta;
        if(click) {
            game.setScreen(new LevelSelectScreen((JumpBeatClass)game));
            dispose();
        }
        else if(click2) {
            game.setScreen(new ShopScreen((JumpBeatClass)game));
            dispose();
        }
        else if(click3) {
            game.setScreen(new HelpScreen((JumpBeatClass)game));
            dispose();
        }
        else if(back) {
            fullyDispose();
            Gdx.app.exit();
        }
    }
    public void update(float dt){
        camera.update();
        camera2.update();
        camera3.update();
    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
        viewport2.update(width,height);
        buttonViewport.update(width,height);
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
        playButton.dispose();
        map.dispose();
        cloudRenderer.dispose();
        backgroundRenderer.dispose();
        manager.dispose();
        stage.dispose();
        music.dispose();
    }
    public void fullyDispose() {
        playButton.dispose();
        map.dispose();
        cloudRenderer.dispose();
        backgroundRenderer.dispose();
        manager.dispose();
        stage.dispose();
        music.dispose();
        game.dispose();
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
