package com.lukagrahor.jumpbeat.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.lukagrahor.jumpbeat.Scenes.Hud;
import com.lukagrahor.jumpbeat.Screens.PlayScreen;

import java.util.logging.FileHandler;

public class FileEditing {
    private Hud hud;
    private FileHandle fh;
    private FileHandle fh2;
    public FileEditing(Hud hud){
        this.hud=hud;
        fh= Gdx.files.local("Score.txt");
        fh2= Gdx.files.local("Hat.txt");
    }
    public void setUp(){
        fh= Gdx.files.local("Score.txt");
    }
    public void setUp2(){
        fh2= Gdx.files.local("Hat.txt");
    }
    public boolean exist(){
        return fh2.exists();
    }
    public FileEditing(){

    }
    public int returnOldSc(){
        String a="";
        a+=fh.readString();
        int sc=Integer.parseInt(a);
        return sc;
    }
    public int returnNewScore(){
        int a=hud.getScore();
        return a;
    }
    public void readSc(){
        String a="";
        a+=fh.readString();
        Gdx.app.log("tocke",a);
    }
    public void writeScore(){
        String a="";
        a+=hud.getScore();
        fh.writeString(a,false);
    }
    public void addScore(){
        int oldScore=returnOldSc();
        int newScore=returnNewScore();
        int sc=oldScore+newScore;
        String a="";
        a+=sc;
        fh.writeString(a,false);
    }
    public void subtract(int a){
        String b="";
        b+=returnOldSc()-a;
        fh.writeString(b,false);
    }
    public void resetScore(){
        fh2.writeString("0",false);
    }
    public void goldenHatSold(){
        fh2.writeString("0",false);
    }
    public void goldenHatBought(){
        fh2.writeString("1",false);
    }
    public int returnHat(){
        int b=Integer.parseInt(fh2.readString());
        return b;
    }
    public void setDefault(){
        fh2.writeString("0",false);
    }
}