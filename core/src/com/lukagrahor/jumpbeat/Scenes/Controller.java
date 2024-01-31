package com.lukagrahor.jumpbeat.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lukagrahor.jumpbeat.JumpBeatClass;

public class Controller{
    private JumpBeatClass game;
    Viewport viewport;
    Stage stage;
    boolean upPressed;
    boolean leftPressed;
    boolean rightPressed;
    OrthographicCamera cam;
    public Controller(JumpBeatClass game){
        cam=new OrthographicCamera();
        viewport=new FitViewport(400,208,cam);
        stage=new Stage(viewport,game.batch);
        Gdx.input.setInputProcessor(stage);

        Table table=new Table();
        table.left().bottom();

        Image rightImg = new Image(new Texture("right.png"));//puščica desno
        rightImg.setSize(35,35);
        rightImg.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed=true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed=false;
            }
        });

        Image leftImg = new Image(new Texture("left.png"));//puščica levo
        leftImg.setSize(35,35);
        leftImg.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed=true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed=false;
            }
        });
        //struktura tabele je 2x3 pravokotnik
        table.add();
        table.add();
        table.add();
        table.row().pad(5,5,5,5);
        table.add(leftImg).size(leftImg.getWidth(),leftImg.getHeight());
        table.add();
        table.add(rightImg).size(rightImg.getWidth(),rightImg.getHeight());
        stage.addActor(table);
    }
    public void draw(){
        stage.draw();
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public void resize(int width,int height){//prilagajanje zaslonu
        viewport.update(width,height);
    }
}
