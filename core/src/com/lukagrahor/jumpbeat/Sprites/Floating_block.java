package com.lukagrahor.jumpbeat.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.lukagrahor.jumpbeat.Items.InteractiveTileObject;
import com.lukagrahor.jumpbeat.JumpBeatClass;
import com.lukagrahor.jumpbeat.Screens.PlayScreen;

public class Floating_block extends InteractiveTileObject {
    public Floating_block(PlayScreen screen, MapObject object){
        super(screen,object);
        fixture.setUserData(this);//Nastavimo uporabnikove informacije na ta objekt
        setCategoryFilter(JumpBeatClass.FLOATING_BLOCK_BIT);//dodelimo bit bodicam, da lahko določimo kdo se lahko dotakne bodic in kdo gre skozi
    }
    @Override
    public void onHeadHit() {

    }

    @Override
    public void onFeetHit() {

    }
}
