package com.lukagrahor.jumpbeat.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.lukagrahor.jumpbeat.Items.InteractiveTileObject;
import com.lukagrahor.jumpbeat.JumpBeatClass;
import com.lukagrahor.jumpbeat.Screens.PlayScreen;

public class Spike extends InteractiveTileObject {
    public Spike(PlayScreen screen, MapObject object){
        super(screen,object);
        fixture.setUserData(this);//Nastavimo uporabnikove informacije na ta objekt
        setCategoryFilter(JumpBeatClass.SPIKE_BIT);//dodelimo bit bodicam, da lahko določimo kdo se lahko dotakne bodic in kdo gre skozi
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Dotik bodice z glavo","");
    }

    @Override
    public void onFeetHit() {
        Gdx.app.log("Dotik bodice z nogami","");
    }
}
