package com.lukagrahor.jumpbeat.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lukagrahor.jumpbeat.JumpBeatClass;

public class Hud implements Disposable { //ko se svet premika mora HUD ostat na istem mesti, zatu rabmo novo kamero
    public Stage stage;
    private Viewport viewport;
    private Integer worldTimer;
    private float timeCount;
    private Integer score;

    Label countdownLabel;
    Label scoreLabel;
    Label timeLabel;
    Label levelLabel;
    Label scoreTextLabel;


    public  Hud(JumpBeatClass game){
        worldTimer = 0;
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(JumpBeatClass.V_WIDTH,JumpBeatClass.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport,game.batch);
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();//na table postavimo elemente
        table.top();
        table.setFillParent(true);//table je velikosti našega stage-a

        countdownLabel = new Label(String.format("%03d",worldTimer),new Label.LabelStyle(new BitmapFont(), Color.WHITE));//%03 številu decimalk, d pomeni integer
        scoreLabel = new Label(String.format("%06d",score),new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreTextLabel = new Label("SCORE",new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1-1",new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(scoreTextLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();

        table.add(countdownLabel).expandX();

        stage.addActor(table);
    }
    public void update(float dt){
        timeCount += dt;
        if(timeCount >= 1){
            worldTimer++;
            countdownLabel.setText(String.format("%03d",worldTimer));
            timeCount = 0;
        }
        stage.act();

    }
    public void draw(){
        stage.draw();
    }
    public void addScore(int value){
        score += value;
        scoreLabel.setText(String.format("%06d",score));
    }
    public Integer getScore(){
        return score;
    }
    @Override
    public void dispose() {
        stage.dispose();
    }
    public void resize(int width,int height){//prilagajanje zaslonu
        viewport.update(width,height);
    }
}
