package com.lukagrahor.jumpbeat.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lukagrahor.jumpbeat.JumpBeatClass;
import com.lukagrahor.jumpbeat.Tools.Variables;

import java.awt.Menu;

public class GameOverScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    private Game game;
    private int mapNumber;
    Texture exitB;
    Image exit;
    boolean back=false;
    boolean retry=false;

    boolean click=false;
    public GameOverScreen(Game game,int mapNumber){
        this.mapNumber=mapNumber;
        this.game=game;
        viewport=new FitViewport(JumpBeatClass.V_WIDTH,JumpBeatClass.V_HEIGHT,new OrthographicCamera());
        stage=new Stage(viewport,((JumpBeatClass)game).batch);
        Gdx.input.setInputProcessor(stage);
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        Table table = new Table();
        table.center();
        table.setFillParent(true);//se centrira glede na ozadje

        Label gameOverLabel = new Label("GAME OVER", font);
        Label playAgainLabel = new Label("Click to Play Again", font);

        playAgainLabel.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                retry=true;
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                retry=false;
            }
        });

        exitB = new Texture("exit2.png");

        exit = new Image(new Texture("exit2.png"));//puščica desno
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

        table.add(gameOverLabel).expandX();
        table.row();
        table.add(playAgainLabel).expandX().padTop(10f);
        table.row();
        table.add(exit).expandX().padTop(10f);


        stage.addActor(table);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(retry){
            game.setScreen(new PlayScreen((JumpBeatClass)game,mapNumber));
            dispose();
        }
        if(back) {
            Variables.checkpointPositionX=0f;
            Variables.checkpointPositionX2=0f;
            Variables.checkpointPositionY=0f;
            game.setScreen(new MenuScreen((JumpBeatClass)game));
            dispose();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.SPACE) ||Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            game.setScreen(new PlayScreen((JumpBeatClass)game,mapNumber));
            dispose();
        }
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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
        stage.dispose();
    }
}
